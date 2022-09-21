import os
os.environ["OMP_NUM_THREADS"] = "1"
from pathlib import Path
from io import StringIO
from django.http import HttpResponse
from matplotlib import pyplot as plt
from rest_framework.decorators import api_view
from rest_framework.renderers import JSONRenderer
from rest_framework.response import Response
from rest_framework.utils import json
import tensorflow as tf
from .models import covidImages,Userrating
from django.shortcuts import render
import joblib
import numpy as np
import lime
import pandas as pd
from lime import lime_tabular
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image

sess = tf.compat.v1.Session(config=
    tf.compat.v1.ConfigProto(inter_op_parallelism_threads=1,
                   intra_op_parallelism_threads=1))
# Create your views here.
model = load_model('vgg16_model4.h5')
def home(request):
    return render(request,"index.html")


def diabetes(request):
    if request.method == 'POST':
        context = diabetesPrediction(Pregnancies=request.POST.get('Pregnancies'),Glucose=request.POST.get('Glucose'),
                                     BloodPressure=request.POST.get('BloodPressure'),
                           SkinThickness=request.POST.get('SkinThickness'),Insulin=request.POST.get('Insulin'),
                                     BMI=request.POST.get('BMI'),
                           DiabetesPedigreeFunction=request.POST.get('DiabetesPedigreeFunction'),Age=request.POST.get('dAge'))

        return render(request,'result.html',context)
    return render(request,"diabetes.html")

def heart(request):
    if request.method == 'POST':
        to_predict_list = request.POST
        if request.POST.get('smoke') == 1:
            smoke=request.POST.get('smoke')
        else:
            smoke=0
        if request.POST.get('Alcoholintake') == 1:
            alcohol=request.POST.get('Alcoholintake')
        else:
            alcohol=0
        if request.POST.get('Physicalactivity') == 1:
            active=request.POST.get('Physicalactivity')
        else:
            active=0

        context = heartPrediction(Age=request.POST.get('hAge'),Gender=request.POST.get('Gender'),Height=request.POST.get('Height'),
                            Weight=request.POST.get('Weight'),sbd=request.POST.get('sbd'),dbd=request.POST.get('dbd'),
                            Cholesterol=request.POST.get('Cholesterol'),Glucose=request.POST.get('Glucose'),
                           smoke=smoke,alcohol=alcohol,
                           active=active)


        return render(request,'result.html',context )
    return render(request,"heart.html")

def kidney(request):
    if request.method == 'POST':
        print(request.POST)
        context = kidneyPrdeiction(Age=request.POST.get('kAge'),bp=request.POST.get('bp'),rbc=request.POST.get('rbc'),
                            wbc=request.POST.get('wbc'),appet=request.POST.get('appet'),pc_normal=request.POST.get('pc_normal'),
                           htn=request.POST.get('htn'),hemo=request.POST.get('hemo'),
                           bgr=request.POST.get('bgr'),dm=request.POST.get('dm'),
                           ane=request.POST.get('ane'))

        return render(request,'result.html',context)
    return render(request,"kidney.html")


def liver(request):
    if request.method == 'POST':
        context = liverPrediction(Age=request.POST.get('lAge'),Gender=request.POST.get('Gender'),tb=request.POST.get('tb'),
                            db=request.POST.get('db'),ap=request.POST.get('ap'),aa=request.POST.get('aa'),
                            aa2=request.POST.get('aa2'),tp=request.POST.get('tp'),
                           a=request.POST.get('a'),ag=request.POST.get('ag'))


        return render(request,'result.html',context)
    return render(request,"liver.html")


def covid(request):
    if request.method == 'POST':

        
        
        BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        
        file = request.FILES['image']
        img=covidImages(image=file)
        img.save()
        b = """/"""
        u = str(img.image)
        print(u)
        MEDIA_ROOT = os.path.join(BASE_DIR, 'media')
        loc = (str(MEDIA_ROOT) + b + u)

        print(loc)
        data=image.load_img(loc,target_size=(256,256,3))
        data = np.expand_dims(data, axis=0)
        data = data * 1.0 / 255

        print(data.shape)
        
        result=model.predict(data)
        print(result)
        
        indices = {0: 'COVID', 1: 'normal', 2: 'Viral Pneumonia'}
        # predicted_class=0
        # accuracy=95.6
        predicted_class = np.asscalar(np.argmax(result, axis=1))
        accuracy = round(result[0][predicted_class] * 100, 2)

        label = indices[predicted_class]
        print(predicted_class)
        print(accuracy)
        print(label)
        return render(request,"result.html",{'accuracy':accuracy,'label':label,'covid':'true','disease':'Covid-19','image':img.image})

    return render(request,"covid.html")


#for api using rest framework
@api_view(['GET'])
def apiView(request):
    api_urls = {
        'diabetes': 'api/diabetes/',
        'liver disease ': 'api/liver_disease/',

    }
    return Response(api_urls)


@api_view(['POST'])
def diabetesApi(request):
    if request.method == 'POST':
        body_unicode = request.body.decode('utf-8')
        body = json.loads(body_unicode)
        js = diabetesPrediction(Pregnancies=body['Pregnancies'], Glucose=body['Glucose'],
                           BloodPressure=body['BloodPressure'],
                           SkinThickness=body['SkinThickness'], Insulin=body['Insulin'], BMI=body['BMI'],
                           DiabetesPedigreeFunction=body['DiabetesPedigreeFunction'], Age=body['Age'])
        json_data = JSONRenderer().render(js)
        return HttpResponse(json_data, content_type='application/json')

@api_view(['POST'])
def liverApi(request):
    if request.method == 'POST':
        body_unicode = request.body.decode('utf-8')
        body = json.loads(body_unicode)
        js = liverPrediction(Age=body['Age'], Gender=body['Gender'],
                           tb=body['TotalBilirubin'],
                           db=body['DirectBilirubin'], ap=body['AlkalinePhosphatase'], aa=body['AlanineAminotransferase'],
                           aa2=body['AsparateAminotransferase'],tp=body['TotalProtein'],a=body['Albumin'],ag=body['AlbuminGlobulinRatio'])
        json_data = JSONRenderer().render(js)
        return HttpResponse(json_data, content_type='application/json')
@api_view(['POST'])
def kidneyApi(request):
    if request.method == 'POST':
        body_unicode = request.body.decode('utf-8')
        body = json.loads(body_unicode)
        js = kidneyPrdeiction(Age=body['age'], bp=body['bp'],
                           rbc=body['rbc'],
                           wbc=body['wbc'], appet=body['appetite'], pc_normal=body['pusCell'],
                           htn=body['hypertension'],hemo=body['hemoglobin'],bgr=body['blood_glucoseRandom'],dm=body['diabetesMellitus'],ane=body['anemia'])
        json_data = JSONRenderer().render(js)
        return HttpResponse(json_data, content_type='application/json')
@api_view(['POST'])
def heartApi(request):
    if request.method == 'POST':
        body_unicode = request.body.decode('utf-8')
        body = json.loads(body_unicode)
        js = heartPrediction(Age=body['age'], Gender=body['gender'], Height=body['height'],
                       Weight=body['weight'], sbd=body['sbp'],dbd=body['dbp'],
                       Cholesterol=body['cholesterol'],Glucose=body['glucose'],
                       smoke=body['smoking'], alcohol=body['alcohol'],
                       active=body['phys_activity'])
        json_data = JSONRenderer().render(js)
        return HttpResponse(json_data, content_type='application/json')

@api_view(['POST'])
def covidApi(request):
    print(request)
    print(request.FILES['image'])
    js=covidprediction(request.FILES['image'])
    json_data = JSONRenderer().render(js)
    return HttpResponse(json_data, content_type='application/json')
    
    
    
@api_view(['POST'])
def ratingApi(request):
   if request.method == 'POST':
        body_unicode = request.body.decode('utf-8')
        body = json.loads(body_unicode)
        disease=body['disease']
        gender=body['gender']
        age=body['age']
        Arating=body['rating1']
    
        Brating =body['rating2']
        Crating = body['rating3']
        Drating = body['rating4']
        Erating = body['rating5']

        r=Userrating(disease=disease,gender=gender,age=age,Arating=Arating,Brating=Brating,Crating=Crating,Drating=Drating,Erating=Erating)
        r.save()
        js = {'response': "1"}
        json_data = JSONRenderer().render(js)
        return HttpResponse(json_data, content_type='application/json')

def diabetesPrediction(**par):
    to_predict_list = [par['Pregnancies'], par['Glucose'],
                       par['BloodPressure'],
                       par['SkinThickness'], par['Insulin'], par['BMI'],
                       par['DiabetesPedigreeFunction'], par['Age']]
    # to_predict_list = list(map(float, to_predict_list))
    to_predict_list = np.array(to_predict_list).reshape(1, 8)
    print(to_predict_list)
    # to_predict_list=to_predict_list.reshape(-1, 1)
    loaded_model = joblib.load("diabetes_detector_ex")
    result = loaded_model.predict(to_predict_list)
    print(result[0])
    if (int(result[0]) == 1):
        print('Sorry ! You are Suffering Diabetes')
    else:
        print('Congrats ! you are Healthy')

    xtrain=np.load('xtrain_diabetes.npy')
    interpretor = lime_tabular.LimeTabularExplainer(
        training_data=np.array(xtrain),
        feature_names=['Pregnancies','Glucose',
                       'BloodPressure',
                       'SkinThickness', 'Insulin', 'BMI',
                       'DiabetesPedigreeFunction', 'Age'],
        mode='classification'
    )
    print(to_predict_list)

    data = {'Pregnancies': [int(par['Pregnancies'])],
            'Glucose': [int(par['Glucose'])],
            'BloodPressure': [int(par['BloodPressure'])],
            'SkinThickness': [int(par['SkinThickness'])],
            'Insulin': [int(par['Insulin'])],
            'BMI': [float(par['BMI'])],
            'DiabetesPedigreeFunction': [float(par['DiabetesPedigreeFunction'])],
            'Age': [int(par['Age'])],


            }

    df = pd.DataFrame(data)
    print(df.iloc[0])
    exp = interpretor.explain_instance(
        data_row=df.iloc[0],  ##new data
        predict_fn=loaded_model.predict_proba
    )

    fig = exp.as_pyplot_figure();

    plt.tight_layout()
    imgdata = StringIO()
    fig.savefig(imgdata, format='svg')
    fig.savefig('lime_report.jpg')
    imgdata.seek(0)

    data = imgdata.getvalue()

    # return render(request, 'result.html', {'result': int(result[0]), "disease": "diabetes"})
    js = {'result': int(result[0]), "disease": "diabetes","graph_div":data}

    return js

def liverPrediction(**par):

    to_predict_list = [par['Age'], par['Gender'],
                       par['tb'],
                       par['db'], par['ap'], par['aa'],
                       par['aa2'], par['tp'],par['a'], par['ag']]
    # to_predict_list = list(map(float, to_predict_list))
    to_predict_list = np.array(to_predict_list).reshape(1, 10)
    print(to_predict_list)
    # to_predict_list=to_predict_list.reshape(-1, 1)
    loaded_model = joblib.load("liver_detector_random")
    result = loaded_model.predict(to_predict_list)
    print(result[0])
    if (int(result[0]) == 1):
        print('Sorry ! You are Suffering Liver disease')
    else:
        print('Congrats ! you are Healthy')
    # return render(request, 'result.html', {'result': int(result[0]), "disease": "diabetes"})
    js = {'result': int(result[0]), "disease": "Liver disease"}

    return js

def kidneyPrdeiction(**par):
    to_predict_list = [par['Age'], par['bp'], par['rbc'],
                       par['wbc'], par['appet'], par['pc_normal'],
                       par['htn'], par['hemo'],
                       par['bgr'], par['dm'],
                       par['ane']]
    # to_predict_list = list(map(float, to_predict_list))
    to_predict_list = np.array(to_predict_list).reshape(1, 11)
    print(to_predict_list)
    # # to_predict_list=to_predict_list.reshape(-1, 1)
    loaded_model = joblib.load("kidney_detector_ex")
    result = loaded_model.predict(to_predict_list)
    print(result)
    if (int(result[0]) == 0):
        r = 1
        print('Sorry ! You are Suffering kidey disease')
    else:
        r = 0
        print('Congrats ! you are Healthy')
    js={'result': r, "disease": "kidney disease"}

    return js
def heartPrediction(**par):
    to_predict_list = [par['Age'],par['Gender'], par['Height'],
                       par['Weight'], par['sbd'],par['dbd'],
                       par['Cholesterol'],par['Glucose'],
                       par['smoke'], par['alcohol'],
                       par['active']]
    # to_predict_list = list(map(float, to_predict_list))
    to_predict_list = np.array(to_predict_list).reshape(1, 11)
    print(to_predict_list)
    # to_predict_list=to_predict_list.reshape(-1, 1)
    loaded_model = joblib.load("heart_detector_random")
    result = loaded_model.predict(to_predict_list)
    print(result)
    if (int(result[0]) == 1):
        print('Sorry ! You are Suffering Diabetes')
    else:
        print('Congrats ! you are Healthy')
    js={'result': int(result[0]), "disease": "heart disease"}
    return js

def covidprediction(file):
    BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    MEDIA_ROOT = os.path.join(BASE_DIR, 'media')


    img = covidImages(image=file)
    img.save()
    b = """/"""
    u = str(img.image)
    print(u)
    MEDIA_ROOT = os.path.join(BASE_DIR, 'media')
    loc = (str(MEDIA_ROOT) + b + u)
    
    print(loc)
    data=image.load_img(loc,target_size=(256,256,3))
    data = np.expand_dims(data, axis=0)
    data = data * 1.0 / 255
    
    print(data.shape)
            
    result=model.predict(data)
    print(result)
            
    indices = {0: 'COVID', 1: 'normal', 2: 'Viral Pneumonia'}
   

    predicted_class = np.asscalar(np.argmax(result, axis=1))
    accuracy = round(result[0][predicted_class] * 100, 2)

    label = indices[predicted_class]
    print(predicted_class)
    print(accuracy)
    print(label)

    js = {'accuracy': accuracy, 'label': label, 'covid': 'true', 'disease': 'Covid-19', 'image': str(img.image)}

    return js
    
    
def rating(request):
    disease=request.POST.get('disease')
    result=request.POST.get('result')
    Arating=request.POST.get('1rating')

    Brating = request.POST.get('2rating')
    Crating = request.POST.get('3rating')
    Drating = request.POST.get('4rating')
    Erating = request.POST.get('5rating')

    print(Arating)
    print(Brating)
    print(Crating)
    print(Drating)
    print(Erating)
    r=Userrating(disease=disease,Arating=Arating,Brating=Brating,Crating=Crating,Drating=Drating,Erating=Erating)
    r.save()
    if str(disease) == "Covid-19":
        return render(request, 'index.html')
    else:   
        context = {'result': int(result), "disease": disease,"review": "1"}
        return render(request, 'result.html', context)