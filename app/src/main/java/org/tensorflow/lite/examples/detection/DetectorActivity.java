/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tensorflow.lite.examples.detection.customview.OverlayView;
import org.tensorflow.lite.examples.detection.customview.OverlayView.DrawCallback;
import org.tensorflow.lite.examples.detection.env.BorderedText;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.env.Logger;
import org.tensorflow.lite.examples.detection.tflite.Classifier;
import org.tensorflow.lite.examples.detection.tflite.DetectorFactory;
import org.tensorflow.lite.examples.detection.tflite.YoloV5Classifier;
import org.tensorflow.lite.examples.detection.tracking.MultiBoxTracker;


/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();

    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.2f;
    private static final boolean MAINTAIN_ASPECT = true;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 640);
    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private Integer sensorOrientation;

    private YoloV5Classifier detector;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingDetection = false;

    private long timestamp = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;

    private BorderedText borderedText;
    public static Set<String> set = new HashSet<>();

    private String htmlContentInStringFormat;
    public HashMap<String, Float> map = new HashMap<String, Float>();
    public int applegrade;
    String link;


    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        tracker = new MultiBoxTracker(this);

        final int modelIndex = modelView.getCheckedItemPosition();
        final String modelString = modelStrings.get(modelIndex);

        try {
            detector = DetectorFactory.getDetector(getAssets(), modelString);
        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.e(e, "Exception initializing classifier!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        int cropSize = detector.getInputSize();

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        cropSize, cropSize,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                new DrawCallback() {
                    @Override
                    public void drawCallback(final Canvas canvas) {
                        tracker.draw(canvas);
                        if (isDebug()) {
                            tracker.drawDebug(canvas);
                        }
                    }
                });

        tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
    }

    protected void updateActiveModel() {
        // Get UI information before delegating to background
        final int modelIndex = modelView.getCheckedItemPosition();
        final int deviceIndex = deviceView.getCheckedItemPosition();
        String threads = "5";//threadsTextView.getText().toString().trim();
        final int numThreads = Integer.parseInt(threads);

        handler.post(() -> {
            if (modelIndex == currentModel && deviceIndex == currentDevice
                    && numThreads == currentNumThreads) {
                return;
            }
            currentModel = modelIndex;
            currentDevice = deviceIndex;
            currentNumThreads = numThreads;

            // Disable classifier while updating
            if (detector != null) {
                detector.close();
                detector = null;
            }

            // Lookup names of parameters.
            String modelString = modelStrings.get(modelIndex);
            String device = deviceStrings.get(deviceIndex);

            LOGGER.i("Changing model to " + modelString + " device " + device);

            // Try to load model.

            try {
                detector = DetectorFactory.getDetector(getAssets(), modelString);
                // Customize the interpreter to the type of device we want to use.
                if (detector == null) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.e(e, "Exception in updateActiveModel()");
                Toast toast =
                        Toast.makeText(
                                getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }


            if (device.equals("CPU")) {
                detector.useCPU();
            } else if (device.equals("GPU")) {
                detector.useGpu();
            } else if (device.equals("NNAPI")) {
                detector.useNNAPI();
            }
            detector.setNumThreads(numThreads);

            int cropSize = detector.getInputSize();
            croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

            frameToCropTransform =
                    ImageUtils.getTransformationMatrix(
                            previewWidth, previewHeight,
                            cropSize, cropSize,
                            sensorOrientation, MAINTAIN_ASPECT);

            cropToFrameTransform = new Matrix();
            frameToCropTransform.invert(cropToFrameTransform);
        });
    }

    @Override
    protected void processImage() {
        ++timestamp;
        final long currTimestamp = timestamp;
        trackingOverlay.postInvalidate();

        // No mutex needed as this method is not reentrant.
        if (computingDetection) {
            readyForNextImage();
            return;
        }
        computingDetection = true;
        LOGGER.i("Preparing image " + currTimestamp + " for detection in bg thread.");

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

        readyForNextImage();

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }

        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        LOGGER.i("Running detection on image " + currTimestamp);
                        final long startTime = SystemClock.uptimeMillis();
                        final List<Classifier.Recognition> results = detector.recognizeImage(croppedBitmap);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

                        Log.e("CHECK", "run: " + results.size());

                        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                        final Canvas canvas = new Canvas(cropCopyBitmap);
                        final Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStyle(Style.STROKE);
                        paint.setStrokeWidth(1.0f);

                        float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                        switch (MODE) {
                            case TF_OD_API:
                                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                                break;
                        }

                        final List<Classifier.Recognition> mappedRecognitions =
                                new LinkedList<Classifier.Recognition>();

                        for (final Classifier.Recognition result : results) {
                            final RectF location = result.getLocation();
                            if (location != null && result.getConfidence() >= minimumConfidence) {
                                canvas.drawRect(location, paint);

                                cropToFrameTransform.mapRect(location);
                                /*map.put(result.getTitle(),result.getConfidence());
                                if(result.getTitle().equals("???(???)") && result.getConfidence()>map.containsValue("???(???)")){

                                }
                                else if(result.getTitle().equals("???(???)")){

                                }
                                else if(result.getTitle().equals("???(???)")){

                                }*/

                                result.setLocation(location);
                                mappedRecognitions.add(result);
                            }
                        }

                        tracker.trackResults(mappedRecognitions, currTimestamp);
                        trackingOverlay.postInvalidate();

                        computingDetection = false;

                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            ArrayAdapter appleadapter = (ArrayAdapter) appleView.getAdapter();

                                            for (final MultiBoxTracker.TrackedRecognition recognition : tracker.trackedObjects) {
                                                set.add(recognition.title);

                                                if(recognition.title.equals("???(???)")){
                                                    if(appleStrings.contains("???(???)")){
                                                        appleStrings.remove("???(???)");
                                                        appleStrings.add(recognition.title);
                                                    }
                                                    else if(appleStrings.contains("???(???)")){
                                                        appleStrings.remove("???(???)");
                                                        appleStrings.add(recognition.title);
                                                    }
                                                    else if(!appleStrings.contains("???(???)")){
                                                        appleStrings.add(recognition.title);
                                                    }
                                                }
                                                else if(recognition.title.equals("???(???)")){
                                                    if(appleStrings.contains("???(???)")){
                                                        appleStrings.remove("???(???)");
                                                        appleStrings.add(recognition.title);
                                                    }
                                                    else if(appleStrings.contains("???(???)")){
                                                        appleStrings.remove("???(???)");
                                                        appleStrings.add(recognition.title);
                                                    }
                                                    else if(!appleStrings.contains("???(???)")){
                                                        appleStrings.add(recognition.title);
                                                    }

                                                }
                                                else if(recognition.title.equals("???(???)")){
                                                    if(appleStrings.contains("???(???)")){
                                                        appleStrings.remove("???(???)");
                                                        appleStrings.add(recognition.title);
                                                    }
                                                    else if(appleStrings.contains("???(???)")){
                                                        appleStrings.remove("???(???)");
                                                        appleStrings.add(recognition.title);
                                                    }
                                                    else if(!appleStrings.contains("???(???)")){
                                                        appleStrings.add(recognition.title);
                                                    }
                                                }
                                                else if (!appleStrings.contains(recognition.title)) {
                                                    appleStrings.add(recognition.title);
                                                }
                                            }
                                            appleadapter.notifyDataSetChanged();
                                            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                                            jsoupAsyncTask.execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        showCropInfo(cropCopyBitmap.getWidth() + "x" + cropCopyBitmap.getHeight());
                                        showInference(lastProcessingTimeMs + "ms");

                                        //showFrameInfo(previewWidth + "x" + previewHeight);
                                    }

                                });
                    }
                });
    }

    //??????
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //try {
                String urls = "https://www.10000recipe.com/recipe/list.html?q=";
                String tail = "&query=&cat1=&cat2=&cat3=&cat4=&fct=&order=accuracy&lastcate=order&dsearch=&copyshot=&scrap=&degree=&portion=&time=&niresource=";
                String one_tail = "&query=&cat1=&cat2=&cat3=&cat4=&fct=&order=reco&lastcate=order&dsearch=&copyshot=&scrap=&degree=&portion=&time=&niresource=";

                if((appleStrings.size()==1 && appleStrings.contains("???(???)") ) ||  (appleStrings.size()==2 && appleStrings.contains("???(???)") && appleStrings.contains("??????"))){
                    applegrade=1;
                    System.out.println("?????? ?????????");
                }
                else if(appleStrings.contains("?????????")||appleStrings.contains("?????????????????????")||appleStrings.contains("??????????????????")||appleStrings.contains("????????????")||appleStrings.contains("??????????????????")){
                    applegrade=4;
                    System.out.println("????????? ???????????????.");
                }
                else if((appleStrings.contains("???(???)")&&appleStrings.contains("????????????")&&!appleStrings.contains("????????????"))||(appleStrings.contains("???(???)") && appleStrings.contains("????????????") && !appleStrings.contains("????????????")) ||
                        (appleStrings.contains("???(???)") && appleStrings.contains("??????")&&!appleStrings.contains("????????????"))){
                    applegrade=2;
                    System.out.println("?????????");
                }
                else if(((appleStrings.contains("????????????")||appleStrings.contains("???(???)")||(appleStrings.contains("????????????")||appleStrings.contains("????????????"))))){
                    applegrade=3;
                    System.out.println("?????????");
                }
                /*
                if (appleStrings.size() == 1) {
                    urls += appleStrings.get(0);
                    String food_one_url = urls + one_tail;
                    Document doc = Jsoup.connect(food_one_url).get();
                    Elements title = doc.select(".rcp_m_list2").select(".common_sp_list_li").select(".common_sp_caption").select(".common_sp_caption_tit");
                    link = doc.select(".rcp_m_list2").select(".common_sp_list_li").select("div[class=common_sp_thumb] a").attr("href");
                    htmlContentInStringFormat = title.get(0).text();
                }
                else if (appleStrings.size() == 2){
                    for (int i = 0; i < appleStrings.size(); i++) {
                        urls += appleStrings.get(i);
                        if (i + 1 == appleStrings.size()) {
                            continue;
                        } else
                            urls += "+";
                    }
                    String food_url = urls + tail;
                    Document doc = Jsoup.connect(food_url).get();
                    Elements title = doc.select(".rcp_m_list2").select(".common_sp_list_li").select(".common_sp_caption").select(".common_sp_caption_tit");
                    link = doc.select(".rcp_m_list2").select(".common_sp_list_li").select("div[class=common_sp_thumb] a").attr("href");
                    htmlContentInStringFormat = title.get(0).text();

                }
                else if (appleStrings.size() != 0){
                    for (int i = 0; i < appleStrings.size(); i++) {
                        urls += appleStrings.get(i);
                        if (i + 1 == appleStrings.size()) {
                            continue;
                        } else
                            urls += "+";
                    }

                    String food_url = urls + tail;
                    Document doc = Jsoup.connect(food_url).get();
                    Elements title = doc.select(".rcp_m_list2").select(".common_sp_list_li").select(".common_sp_caption").select(".common_sp_caption_tit");
                    link = doc.select(".rcp_m_list2").select(".common_sp_list_li").select("div[class=common_sp_thumb] a").attr("href");
                    htmlContentInStringFormat = title.get(0).text();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
           // textView.setText("?????? ?????? ?????? : "+ htmlContentInStringFormat);
        }
    }

    public void onButton2Clicked(View view) {
       // String urls = "https://www.10000recipe.com/";
        if (!(appleStrings.contains("???(???)")||appleStrings.contains("???(???)")||appleStrings.contains("???(???)"))){
            Toast.makeText(
                    this,
                    "????????? ?????? ?????????????????????.",
                    Toast.LENGTH_LONG)
                    .show();
            appleStrings.clear();
            appleAdapter.notifyDataSetChanged();

        } else {
            Intent intent = new Intent(getApplicationContext(), final_view.class);
            intent.putExtra("grade",applegrade);
            startActivity(intent);
            appleStrings.clear();
            appleAdapter.clear();
        }
    }



    @Override
    protected int getLayoutId() {
        return R.layout.tfe_od_camera_connection_fragment_tracking;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

// Which detection model to use: by default uses Tensorflow Object Detection API frozen
// checkpoints.
private enum DetectorMode {
    TF_OD_API;
}

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        runInBackground(() -> detector.setUseNNAPI(isChecked));
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        runInBackground(() -> detector.setNumThreads(numThreads));
    }

}
