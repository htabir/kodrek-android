package app.kodrek;

import java.util.List;

public class PresetList {
    List<Integer> id;
    List<String> name;
    List<Integer> total;
    List<Integer> touch;
    List<Integer> like;
    List<Integer> days;

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<Integer> getTotal() {
        return total;
    }

    public void setTotal(List<Integer> total) {
        this.total = total;
    }

    public List<Integer> getTouch() {
        return touch;
    }

    public void setTouch(List<Integer> touch) {
        this.touch = touch;
    }

    public List<Integer> getLike() {
        return like;
    }

    public void setLike(List<Integer> like) {
        this.like = like;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }

    public int getPresetTotal(int id){
        return total.get(id-1);
    }

    public String getPresetName(int id){
        return name.get(id-1);
    }

    public int getPresetTouch(int id){
        return touch.get(id-1);
    }
    public int getPresetLike(int id){
        return like.get(id-1);
    }

    public int getPresetDays(int id){
        return days.get(id-1)%86400;
    }
}
