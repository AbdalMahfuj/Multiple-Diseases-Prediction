from django.db import models

# Create your models here.
class covidImages(models.Model):
    image = models.ImageField(blank=True, upload_to="covidimages")
class Userrating(models.Model):
    disease=models.CharField(blank=True,max_length=20)
    gender=models.CharField(blank=True,max_length=20)
    age=models.CharField(blank=True,max_length=20)
    Arating=models.FloatField()
    Brating=models.FloatField()
    Crating=models.FloatField()
    Drating = models.FloatField()
    Erating = models.FloatField()
    def __str__(self):
        return self.disease