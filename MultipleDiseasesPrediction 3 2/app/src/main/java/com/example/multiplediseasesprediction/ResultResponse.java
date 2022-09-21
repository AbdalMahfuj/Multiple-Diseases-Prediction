package com.example.multiplediseasesprediction;

import java.io.Serializable;

public class ResultResponse implements Serializable {
    private String result,disease;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }
}
