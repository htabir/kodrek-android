package app.kodrek;


public class Preset extends ProblemSet{

    int presetId;
    int like;
    int days;
    int checkpoint;

    public int getPresetId() { return presetId; }

    public void setPresetId(int presetId) { this.presetId = presetId; }

    public int getLike() { return like; }

    public void setLike(int like) { this.like = like; }

    public int getDays() { return days; }

    public void setDays(int days) { this.days = days; }

    public int getCheckpoint() { return checkpoint; }

    public void setCheckpoint(int checkpoint) { this.checkpoint = checkpoint; }

    public int getTotal(){
        return super.cfTotal()+super.uvaTotal();
    }
}
