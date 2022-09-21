package com.example.multiplediseasesprediction;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {
@POST("api/diabetes")
    Call<ResultResponse> diabetespred(@Body DiabetesPredRequest diabetesPredRequest);

@POST("api/heart_disease")
    Call<ResultResponse> heartPredi(@Body HeartRequest heartRequest);

@POST("api/kidney_disease")
    Call<ResultResponse> kidneyPred(@Body KidneyRequest kidneyRequest);

@POST("api/liver_disease")
    Call<ResultResponse> liverPred(@Body LiverRequest liverRequest);
    @POST("api/rating")
    Call<ratingResponse> ratingSubmit(@Body rating_request ratingRequest);
    @Multipart
    @POST("api/covid")
    Call<CovidResponseBody> upload(
            @Part MultipartBody.Part image
    );

}
