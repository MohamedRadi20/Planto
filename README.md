  <h1>Planto</h1>
<!--   <img src="https://github.com/MohamedRadi20/Hot_dog/blob/main/logo.png" alt="Hot Dog" style="width: 93px; margin-top: 1px;">
   -->
   
[![Coming Soon on Google Play](https://img.shields.io/badge/Coming%20Soon-36A3F7?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps) [![TensorFlow Lite](https://img.shields.io/badge/TensorFlow_Lite-2.3.0-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)](https://www.tensorflow.org/lite) [![Version](https://img.shields.io/badge/Version-1.0.0-2bbc8a?style=for-the-badge)]() [![This is Fun](https://img.shields.io/badge/This%20is-Fun-FFD700?style=for-the-badge)]()

Planto is an Android Java app that aims to address plant problems and provide AI-based solutions. With a range of features and functionalities, Planto offers a comprehensive platform for plant enthusiasts. This README file provides an overview of the app, its features, and the technologies used.

 <p align="center">
  <img src="https://github.com/MohamedRadi20/Planto/blob/master/photos/70cc91164038111_Y3JvcCwzNDcyLDI3MTYsMzzCww-transformed.jpeg_auto_x2.jpg" alt="Planto app preview" width="900" style="border: 2px solid #4CAF50">
</p> 

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
    - [1. Plant Disease Classification](#plant-disease-classification)
    - [2. Real-Time Plant Identification (AR)](#real-time-plant-identification-ar)
    - [3. Community Feature](#community-feature)
    - [4. Weather Forecast and Analysis](#weather-forecast-and-analysis)
    - [5. Plantune Feature](#plantune-feature)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Contributing](#contributing)
- [Acknowledgements](#acknowledgements)

## Introduction
Planto is a mobile application developed to help plant enthusiasts identify plant diseases, recognize plants in real-time, interact with a community, and access weather forecasts and plant-based information. By leveraging AI solutions and various APIs, Planto provides an intuitive and informative experience for plant lovers.

## Features

###    1. Plant Disease Classification
[![PyTorch](https://img.shields.io/badge/PyTorch-EE4C2C?style=for-the-badge&logo=pytorch&logoColor=white)](https://pytorch.org/) [![Open In Colab](https://img.shields.io/badge/Open%20In-Colab-blueviolet?style=for-the-badge&logo=google-colab)](https://colab.research.google.com/drive/1GLpoWzARntJUXCr0kewFisOHN7ELax7z?usp=sharing) ![Vision Transformers](https://img.shields.io/badge/Vision%20Transformers-3F51B5?style=for-the-badge)

Planto incorporates a state-of-the-art deep learning model based on the DeiT (ViT) architecture. This model has been trained using the PyTorch framework and a large dataset sourced from Kaggle. By utilizing transformers, Planto achieves high accuracy and performance with less data and computational resources compared to convolution-based networks. The model has been converted to the TensorFlow Lite format and seamlessly integrated into the app, enabling users to classify plant diseases accurately.

###    2. Real-Time Plant Identification (AR)
[![TensorFlow 2.0 Object Detection API](https://img.shields.io/badge/TensorFlow%202.0%20Object%20Detection%20API-API-ff6f00?style=for-the-badge&logo=tensorflow&logoColor=white)](https://github.com/tensorflow/models/tree/master/research/object_detection) [![OpenCV](https://img.shields.io/badge/OpenCV-5C3EE8?style=for-the-badge&logo=opencv&logoColor=white)](https://opencv.org/) [![Open In Colab](https://img.shields.io/badge/Open%20In-Colab-blueviolet?style=for-the-badge&logo=google-colab)](https://colab.research.google.com/drive/1y8IqcYdw81AUuDcZbzOm3N__vtITx3qX?usp=sharing)

With the real-time plant identification feature, Planto harnesses the power of computer vision. By utilizing the OpenCV library and an SSD-MobileNet-FPNLite model trained with TensorFlow 2.0 object detection API, Planto can detect plants in real-time through the device's camera. The app overlays a shape around the detected plant and performs inference to identify the plant species. The model has been trained using a diverse dataset from Roboflow, ensuring accurate and reliable plant recognition.

###    3. Community Feature
[![Firebase](https://img.shields.io/badge/firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)

Planto encourages users to engage with a vibrant community of plant enthusiasts. The app allows users to create and share posts, like and comment on others' content, and interact with fellow plant lovers in real-time. Firebase authentication and database functionalities ensure secure storage of user data. By fostering a sense of belonging and facilitating knowledge sharing, the community feature enhances the overall user experience. Additionally, Firebase provides valuable tools like app analytics and user-friendly APIs and SDKs.

###    4. Weather Forecast and Analysis
[![OpenWeatherMap API](https://img.shields.io/badge/OpenWeatherMap-API-ff9a00?style=for-the-badge&logo=openweathermap&logoColor=white)](https://openweathermap.org/api)

Planto provides comprehensive weather forecast and analysis capabilities. By integrating the OpenWeatherMap API, the app offers a 5-day forecast for any location worldwide, with weather data available at 3-hour intervals. The current weather data is accessible for over 200,000 cities. Planto collects and processes weather information from multiple sources, including weather models, satellites, radars, and weather stations. <u>Planto also provides some line charts for analysis purposes.</u> Additionally, the Geocoding API enables seamless conversion between city names, zip codes, and geographic coordinates, enhancing user convenience.

###    5. Plantune Feature (Over 10,000+ Species of Plants Available)
[![Plant API](https://img.shields.io/badge/Plant%20API-API-2bbc8a?style=for-the-badge&logo=perenual&logoColor=white)](https://perenual.com/docs/api)

Planto leverages a custom-built RESTful Plant API to provide users with a vast amount of plant-based information. The API offers access to a rich repository of plant species, care guides, growth stages, images, hardiness zones, and much more. With its comprehensive resources, the API caters to various plant enthusiasts, including those interested in tropical houseplants, sustainable outdoor plants, non-poisonous edible fruits/berries, and medical herbs. The Plantune feature empowers users with knowledge and helps them make informed decisions regarding plant care and selection.

## Technologies Used
- Android Java
- PyTorch
- TensorFlow 2.0
- OpenCV
- Firebase (Authentication, Database)
- OpenWeatherMap API
- Custom-built RESTful Plant API

## Installation :hourglass_flowing_sand:
#### Planto is coming soon to the Google Play Store. Once available, the app can be easily downloaded and installed on most Android devices. Stay tuned for updates on the release date and installation instructions.

## Contributing
We welcome contributions to enhance Planto and make it even better. If you would like to contribute, please follow these guidelines:
- Fork the repository.
- Create a new branch for your feature or bug fix: `git checkout -b my-feature`
- Make the necessary changes and ensure that the code is properly formatted.
- Test your changes to ensure they work as intended.
- Commit your changes: `git commit -m 'Add my feature'`
- Push to your branch: `git push origin my-feature`
- Create a pull request, describing your changes in detail and explaining the benefits.

## Acknowledgements :grin:

Planto was inspired by my love for plants and my curiosity for AI. I would like to thank the following sources and contributors for helping me with this project:
- [Meta](https://github.com/facebookresearch/deit) for the DeiT model and code
- [Kaggle](https://www.kaggle.com/) for the plant disease dataset
- [Roboflow](https://public.roboflow.com/) for the plant detection dataset
- [OpenCV](https://opencv.org/) for the computer vision library
- [TensorFlow](https://www.tensorflow.org/) for the machine learning framework
- [Firebase](https://firebase.google.com/) for the authentication and database services
- [OpenWeatherMap](https://openweathermap.org/) for the weather API
- [Plant API](https://perenual.com/docs/api) for the plant information API
  
  
<div class="footer">
        &copy; 2023 Graduation pro  :+1:
</div>
