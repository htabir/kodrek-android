package app.kodrek;

import java.util.List;
import java.util.Map;

public class ProblemSet {
    String name;
    String owner;
    List<String> cf;
    List<String> uva;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getCf() {
        return cf;
    }

    public void setCf(List<String> cf) {
        this.cf = cf;
    }

    public List<String> getUva() {
        return uva;
    }

    public void setUva(List<String> uva) {
        this.uva = uva;
    }

    public int cfTotal(){
        return cf.size();
    }

    public int uvaTotal(){
        return uva.size();
    }

    public int countSolved(OjData ojData, String ojName){
        int c = 0;
        if(ojName=="cf"){
            Map<String, Integer> cfSolved = ojData.getSolvedSet();
            for(String prob : cf){
                if(cfSolved.get(prob) != null){
                    c++;
                }
            }
        }else if(ojName == "uva"){
            Map<String, Integer> cfSolved = ojData.getSolvedSet();
            for(String prob : uva){
                if(cfSolved.get(prob) != null){
                    c++;
                }
            }
        }
        return c;
    }

    public int countUnsolved(OjData ojData, String ojName){
        int c = 0;
        if(ojName=="cf"){
            Map<String, Integer> cfSolved = ojData.getUnsolvedSet();
            for(String prob : cf){
                if(cfSolved.get(prob) != null){
                    c++;
                }
            }
        }else if(ojName == "uva"){
            Map<String, Integer> cfSolved = ojData.getUnsolvedSet();
            for(String prob : uva){
                if(cfSolved.get(prob) != null){
                    c++;
                }
            }
        }
        return c;
    }

}
