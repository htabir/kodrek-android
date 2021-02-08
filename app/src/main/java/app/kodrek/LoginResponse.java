package app.kodrek;

public class LoginResponse {
    private String name;
    private String username;
    private int dailyGoal;
    private int presetDailyGoal;
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDailyGoal() { return dailyGoal; }

    public void setDailyGoal(int dailyGoal) { this.dailyGoal = dailyGoal; }

    public int getPresetDailyGoal() { return presetDailyGoal; }

    public void setPresetDailyGoal(int presetDailyGoal) { this.presetDailyGoal = presetDailyGoal; }
}
