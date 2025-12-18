package com.example.restaurant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // 1. Database Setup
    @POST("create_student/{student_id}")
    Call<Void> createDatabase(@Path("student_id") String studentId);

    // 2. Register New User
    @POST("create_user/{student_id}")
    Call<Void> registerUser(@Path("student_id") String studentId, @Body User user);

    // 3. Get All Users
    @GET("read_all_users/{student_id}")
    Call<UserResponse> getAllUsers(@Path("student_id") String studentId);
}

