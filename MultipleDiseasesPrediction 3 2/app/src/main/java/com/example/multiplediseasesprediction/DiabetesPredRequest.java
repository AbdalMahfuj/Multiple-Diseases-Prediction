package com.example.multiplediseasesprediction;

public class DiabetesPredRequest {

    public String getPregnancies() {
        return Pregnancies;
    }

    public void setPregnancies(String pregnancies) {
        Pregnancies = pregnancies;
    }

    public String getGlucose() {
        return Glucose;
    }

    public void setGlucose(String glucose) {
        Glucose = glucose;
    }

    public String getBloodPressure() {
        return BloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        BloodPressure = bloodPressure;
    }

    public String getSkinThickness() {
        return SkinThickness;
    }

    public void setSkinThickness(String skinThickness) {
        SkinThickness = skinThickness;
    }

    public String getInsulin() {
        return Insulin;
    }

    public void setInsulin(String insulin) {
        Insulin = insulin;
    }

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public String getDiabetesPedigreeFunction() {
        return DiabetesPedigreeFunction;
    }

    public void setDiabetesPedigreeFunction(String diabetesPedigreeFunction) {
        DiabetesPedigreeFunction = diabetesPedigreeFunction;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    private String Pregnancies,Glucose,BloodPressure,SkinThickness,Insulin,BMI,DiabetesPedigreeFunction,Age;

}
