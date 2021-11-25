package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class final_view extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_view);

        Intent intent = getIntent(); /*데이터 수신*/

        ImageView picture = (ImageView)findViewById(R.id.grade_image);
        int grade = intent.getExtras().getInt("grade");

        if(grade==1) {
            picture.setImageResource(R.drawable._1grade);
        }
        else if(grade==2) {
            picture.setImageResource(R.drawable._2grade);
        }
        else if(grade==3) {
            picture.setImageResource(R.drawable._3grade);
        }
        else if(grade==4) {
            picture.setImageResource(R.drawable._4grade);
        }

        Button btn = (Button) findViewById(R.id.button_2);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        //int grade = intent.getExtras().getInt("grade"); /*int형*/
        /*String name = intent.getExtras().getString("name"); /*String형*/
        /*tx1.setText(name);*/

    }

}