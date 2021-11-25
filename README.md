<div align="center">

![image](https://user-images.githubusercontent.com/68915940/143439231-ed220f69-6d8e-4d84-b049-51e4cb47b11d.png)
<h1><span style="color:indianred">Apple condition detector</span></h1>

<p align="center">
Use yolov5 to detect apple conditions and provide apple rating.

</p>

<div>

![image](https://img.shields.io/badge/yolov5-6.0-green)
![image](https://img.shields.io/badge/pytorch-1.10.0%2Bcu111-lightgrey)
![image](https://img.shields.io/badge/tensorflow-1.15.2v-orange)
![image](https://img.shields.io/badge/andorid%20studio-%403.5.3-blue)
![image](https://img.shields.io/badge/roboflow-labeling-red)
[![Github](https://flat.badgen.net/badge/icon/Q-100?icon=github&label)](https://github.com/Q-100)


</div>

The Apple condition detector detects the pests, cuts, bruises, and colors of apples and checks their grades in real time. You can use it if you want to know the grade of an apple or what pests you harvested are.
</div>
<div align="center">
<img src="https://user-images.githubusercontent.com/68915940/143449367-a05e0642-139b-4463-8008-a0bb3b81980a.png"  width="200" height="400"/>
</div>






## [Development Environment]()
- Google colaboratory, local(GPU : 2070super 8GB, RAM : 16GB, CPU : 3700x)
- Yolov5
- Tensorflow 1.15.2v
- pytorch 1.10.0+cu111
- Android Studio @3.5.3
- ---
## [Features]()

**⚡️ Real-time Detection**

Just take a video of an apple and it shows the conditions of the apple in real time.


**🐜 Small object detection**

During the image preprocessing step, even small objects can be detected by increasing the size of the image to 640

![image](https://user-images.githubusercontent.com/68915940/143448929-c3171db6-5a8a-41ba-a60b-5edb8e53061f.png)

**🍎 Detecting various types of apple pests**

It can detect five pests that occur frequently in Fuji(부사사과).

**🌈 Can Differentiate the colors of apples.**

The apple condition detector can detect the color of the apple by dividing it into 3 colors.

**‍🩹Detects many factors that affect apples**

Various types of cuts and bruises that affect apple grade can be detected.


----
## 📀 Apple Dataset

Data and number of annotations used in the apple condition detector :<br>



![image](https://user-images.githubusercontent.com/78460820/142620555-fe57cbc5-a21f-4496-bc2c-5ab80d9bbf04.png)

1. Image : 1533(After Data Augmentation)
2. Class : 11
3. ImageSize : 640
4. Annotations : 7188

Images of apples were crawled by Google and Coupang, and pests used images provided by 🇰🇷[NCPMS](https://ncpms.rda.go.kr/npms/ImageSearchDtlR2.np?kncrCode=FT010601&kncrNm=%EC%82%AC%EA%B3%BC&upperNm=%EA%B3%BC%EC%88%98&flagCode=S&queryFlag=A&nextAction=%2Fnpms%2FImageSearchDtlR2.np).

### Download dataset
You can download apple_grade dataset here: https://app.roboflow.com/ds/GN9aUMJdR1?key=gcxVDmN2be

If you want to use the dataset from colab,

```python
curl -L "https://app.roboflow.com/ds/5FcXgt0BjG?key=ElfkiFKHaY" > roboflow.zip; unzip roboflow.zip; rm roboflow.zip
```
-------
## 📀 Apple Class example
1. apple : It is a class to check if it is an apple(Using all kinds of apples)

![image](https://user-images.githubusercontent.com/68915940/143457063-853401a1-59e0-4479-8277-784838a2a5f1.png)

2. apple_A : An apple with superior color(Using only Fuji apples)

![image](https://user-images.githubusercontent.com/68915940/143457080-68842204-2bc1-4c3b-85ce-0f401523936c.png)

3. apple_B : An apple with medium color(Using only Fuji apples)

![image](https://user-images.githubusercontent.com/68915940/143457100-28bc1cf5-4a38-4dfe-91c7-ca8f3b580809.png)

4. apple_C : An apple with bed color(Using only Fuji apples)

![image](https://user-images.githubusercontent.com/68915940/143457118-f5259696-e1fb-4a7b-b9ff-018325113a92.png)

5. dmg_s : Apple with small cuts or weak bruises that can be sold as a prize(Using all kinds of apples)

![image](https://user-images.githubusercontent.com/68915940/143457137-12c01f15-0d7b-46db-8e51-c7ae73135e3d.png)

6. dms_l : Apple with strong cuts or strong bruises that cannot be sold as a prize(Using all kinds of apples)

![image](https://user-images.githubusercontent.com/68915940/143457157-c78e1174-9233-4d4c-96c3-5db7bb6fae4b.png)

7. Scab

![image](https://user-images.githubusercontent.com/68915940/143457168-277bdca6-5b15-4fd4-89e4-e88ea67105e3.png)

8. Anthracnose

![image](https://user-images.githubusercontent.com/68915940/143457194-c89d7ba9-07af-4c1e-a3ba-0ce8ac0113c2.png)

9. Sooty blotch

![image](https://user-images.githubusercontent.com/68915940/143457176-aeca2a35-2bed-4679-a6d4-bc4222b9dc20.png)

10. Fly speck

![image](https://user-images.githubusercontent.com/68915940/143457219-a4aef23d-78f2-46e8-9b54-20b05ebf4951.png)

11. White rot

![image](https://user-images.githubusercontent.com/68915940/143457209-e4d7dd97-5923-4ee6-880b-1b711baa6802.png)

## 💻 Team
### [Sangmyung University](https://www.smu.ac.kr/ko/index.do) Graduation Capstone Project

- Advisor : Professor Seongjoo Lee
- Team Leader : Kyuback Kim (email aderess : opea5954@gmail.com)
- Team member : Dongeun Kang
- Team member : Woonam Kim

Demo Video : 



