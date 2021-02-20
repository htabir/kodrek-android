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

    @POST("oj/uva/stats/{id}")
    Call<OjData> getUva(@Path("id") String username, @Header("Authorization") String Token);

    @POST("preset/list")
    Call<PresetList> getPresetList(@Header("Authorization") String Token);

    @POST("preset/set/{presetId}")
    Call<Preset> setPreset(@Path("presetId") int presetId, @Header("Authorization") String Token);

    @POST("preset/details/{presetId}")
    Call<Preset> getPresetDetails(@Path("presetId") int presetId, @Header("Authorization") String Token);

    @POST("preset/stats/{id}")
    Call<Preset> getPresetStats(@Path("id") String username, @Header("Authorization") String Token);

    @POST("preset/like")
    Call<CheckingResponse> likePreset(@Header("Authorization") String Token);

    @POST("auth/logout")
    Call<CheckingResponse> logout(@Header("Authorization") String Token);

    @POST("auth/change/dailyGoal/{goal}")
    Call<CheckingResponse> changeDailyGoal(@Path("goal") int goal, @Header("Authorization") String Token);

    @POST("auth/change/presetDailyGoal/{goal}")
    Call<CheckingResponse> changePresetGoal(@Path("goal") int goal, @Header("Authorization") String Token);
}
