# Generated by Django 4.0.4 on 2022-04-16 22:28

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Detector', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Userrating',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('disease', models.CharField(blank=True, max_length=20)),
                ('Arating', models.FloatField()),
                ('Brating', models.FloatField()),
                ('Crating', models.FloatField()),
                ('Drating', models.FloatField()),
                ('Erating', models.FloatField()),
            ],
        ),
    ]