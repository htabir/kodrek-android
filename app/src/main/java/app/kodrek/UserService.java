package app.kodrek;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @POST("auth/login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("auth/check/email")
    Call<CheckingResponse> checkEmail(@Body RegistrationRequest registrationRequest);

    @POST("auth/check/username")
    Call<CheckingResponse> checkUsername(@Body RegistrationRequest registrationRequest);

    @POST("oj/check/cf")
    Call<CheckingResponse> checkCf(@Body RegistrationRequest registrationRequest);

    @POST("oj/check/uva")
    Call<CheckingResponse> checkUva(@Body RegistrationRequest registrationRequest);

    @POST("auth/register")
    Call<CheckingResponse> userRegister(@Body RegistrationRequest registrationRequest);

    @POST("oj/cf/stats/{id}")
    Call<OjData> getCf(@Path("id") String username, @Header("Authorization") String Token);

    @POST("oj/cf/solved/{id}")
    Call<Map<String, Integer>> getCfSolved(@Path("id") String username, @Header("Authorization") String Token);

    @POST("oj/cf/unsolved/{id}")
    Call<Map<String, Integer>> getCfUnsolved(@Path("id") String username, @Header("Authorization") String Token);

    @POST("oj/uva/stats/{id}")
    Call<OjData> getUva(@Path("id") String username, @Header("Authorization") String Token);

    @POST("oj/uva/solved/{id}")
    Call<Map<String, Integer>> getUvaSolved(@Path("id") String username, @Header("Authorization") String Token);

    @POST("oj/uva/unsolved/{id}")
    Call<Map<String, Integer>> getUvaUnsolved(@Path("id") String username, @Header("Authorization") String Token);

    @POST("auth/refresh")
    Call<String> refreshToken(@Header("Authorization") String Token);

}
