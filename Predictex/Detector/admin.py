from django.contrib import admin
from .models import covidImages,Userrating
# Register your models here.
admin.site.register(covidImages)

class ratingAdmin(admin.ModelAdmin):
    list_display = ('disease','Arating','Brating','Crating','Drating','Erating','gender','age')
    list_filter = (
         'disease','Arating','Brating','Crating','Drating','Erating','gender','age',
    )
    search_fields = (
       'disease','Arating','Brating','Crating','Drating','Erating','gender','age',
    )
admin.site.register(Userrating,ratingAdmin)