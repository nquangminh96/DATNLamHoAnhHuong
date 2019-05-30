package com.example.datn20182;


import com.example.datn20182.Notifications.MyResponse;
import com.example.datn20182.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAAkCktGQ:APA91bFsCq-vZchq272Uxu7oEhyFBGsdUcsa_duR5Y4cXZ5PTmY1TgGlWYJt9lbMDH-qlNDetNQGqcmqnq_VTVwFV3dDClr9w_CuVyG4GWEZvq8oM4NZYFvFxPozXaJoeX6BvDn1T7Cv"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}