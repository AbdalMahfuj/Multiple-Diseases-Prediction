package com.example.multiplediseasesprediction;

public class rating_request {
    String disease,gender,age;
    float rating1,rating2,rating3,rating4,rating5;
    public String getDisease() {
        return disease;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public float getRating1() {
        return rating1;
    }

    public void setRating1(float rating1) {
        this.rating1 = rating1;
    }

    public float getRating2() {
        return rating2;
    }

    public void setRating2(float rating2) {
        this.rating2 = rating2;
    }

    public float getRating3() {
        return rating3;
    }

    public void setRating3(float rating3) {
        this.rating3 = rating3;
    }

    public float getRating4() {
        return rating4;
    }

    public void setRating4(float rating4) {
        this.rating4 = rating4;
    }

    public float getRating5() {
        return rating5;
    }

    public void setRating5(float rating5) {
        this.rating5 = rating5;
    }

}
