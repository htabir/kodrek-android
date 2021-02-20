package app.kodrek;

import java.util.HashMap;
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
        return getSolved(ojData, ojName).size();
    }

    @Override
    public int countUnsolved(OjData ojData, String ojName) {
        return getUnsolved(ojData, ojName).size();
    }

    public Map<String, Integer> getSolved(OjData ojData, String ojName){
        Map<String, Integer> set= new HashMap<>();
        if(ojName=="cf"){
            Map<String, Integer> cfSolved = ojData.getSolvedSet();
            for(String prob : super.cf){
                if(cfSolved.get(prob) != null){
                    set.put((String)prob, cfSolved.get(prob));
                }
            }
        }else if(ojName == "uva"){
            Map<String, Integer> uvaSolved = ojData.getSolvedSet();
            for(String prob : super.uva){
                if(uvaSolved.get(prob) != null){
                    set.put((String)prob, uvaSolved.get(prob));
                }
            }
        }
        return set;
    }

    public Map<String, Integer> getUnsolved(OjData ojData, String ojName){
        Map<String, Integer> set= new HashMap<>();
        if(ojName=="cf"){
            Map<String, Integer> cfUnsolved = ojData.getUnsolvedSet();
            for(String prob : super.cf){
                if(cfUnsolved.get(prob) != null){
                    set.put((String)prob, cfUnsolved.get(prob));
                }
            }
        }else if(ojName == "uva"){
            Map<String, Integer> uvaUnsolved = ojData.getUnsolvedSet();
            for(String prob : super.uva){
                if(uvaUnsolved.get(prob) != null){
                    set.put((String)prob, uvaUnsolved.get(prob));
                }
            }
        }
        return set;
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
