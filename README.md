# ingredients-classification


# Yolov5를 이용한 식재료 객체탐지 모델
상명대학교 SW-PBL 프로젝트 <br />
- email address : opea5954@gmail.com <br />
- Demo Video : https://youtu.be/o9u2amtgCto <br />

## Introduction
Yolov5와 Tensorflow Lite를 기반으로한 레시피 추천 어플리케이션
1. Yolov5로 학습한 식재료 탐지 모델을 이용하여 실시간으로 식재료 탐지
2. 현재까지 인식한 식재료 중 가장 연관성 높은 요리를 실시간으로 제공
3. 연관성 높은 요리의 레시피를 볼 수 있는 사이트 링크 제공

## Development Environment
- Google colaboratory
- Yolov5
- Tensorflow 1.15.2v
- Android Studio @3.5.3

## Food ingredients dataset
1. Class : 11
    -> 마늘, 감자, 달걀, 양파, 닭고기, 돼지고기, 대파, 소고기, 김치, 햄, 콩나물
2. Images : 905
3. TRAIN/TEST SPLIT(100%) 
    -> Train : 631(70%)
    -> Valid : 179(20%)
    -> Test : 95(10%)
    
![image](https://user-images.githubusercontent.com/68915940/120195304-93a6eb80-c259-11eb-8743-99db1c743449.png)
    
Dataset download : https://drive.google.com/file/d/1TMjSp__xeLrYLKl-XmWTIy10e4tXcjHB/view?usp=sharing

## Model Training
yolov5 설치
```bash
!git clone https://github.com/zldrobit/yolov5.git
```

requirements.txt 설치
```bash
%cd /content/yolov5/
pip install -r requirements.txt
```

Model 학습
```bash
!python train.py --img 416 --batch 16 --epochs 300 --data /content/data.yaml --cfg ./models/yolov5s.yaml --weights yolov5s.pt --name food_ingredients_model
```

weight(.tflite) 
https://drive.google.com/file/d/1lvwR8SvFC8moYsfMrxvUV1SeIQBPFx1L/view?usp=sharing



