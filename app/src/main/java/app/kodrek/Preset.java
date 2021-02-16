package app.kodrek;

import java.util.Map;

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

    @Override
    public int countSolved(OjData ojData, String ojName) {
        int c = 0;
        if(ojName=="cf"){
            Map<String, Integer> cfSolved = ojData.getSolvedSet();
            for(String prob : super.cf){
                if(cfSolved.get(prob) != null){
                    c++;
                }
            }
        }else if(ojName == "uva"){
            Map<String, Integer> cfSolved = ojData.getSolvedSet();
            for(String prob : super.uva){
                if(cfSolved.get(prob) != null){
                    c++;
                }
            }
        }
        return c;
    }

    @Override
    public int countUnsolved(OjData ojData, String ojName) {
        int c = 0;
        if(ojName=="cf"){
            Map<String, Integer> set = ojData.getUnsolvedSet();
            for(String prob : super.cf){
                if(set.get(prob) != null){
                    c++;
                }
            }
        }else if(ojName == "uva"){
            Map<String, Integer> set = ojData.getUnsolvedSet();
            for(String prob : super.uva){
                if(set.get(prob) != null){
                    c++;
                }
            }
        }
        return c;
    }

    public int dailySolved(OjData ojData, String ojName){
        int c = 0;
        int time = (int) (System.currentTimeMillis()/1000L);
        time = time - (time%86400);
        if(ojName=="cf"){
            Map<String, Integer> set = ojData.getSolvedSet();
            for(String prob : super.cf){
                if(set.get(prob) != null && set.get(prob) >= time){
                    c++;
                }
            }
        }else if(ojName == "uva"){
            Map<String, Integer> set = ojData.getSolvedSet();
            for(String prob : super.uva){
                if(set.get(prob) != null && set.get(prob) >= time){
                    c++;
                }
            }
        }
        return c;
    }
}
