package app.kodrek;

import java.util.Map;

public class OjData {
    private int totalSub;
    private int disAc;
    private int totalAc;
    private int totalWa;
    private int totalOt;
    private Map<String, Integer> solvedSet;
    private Map<String, Integer> unsolvedSet;

    public int getTotalSub() { return totalSub; }

    public void setTotalSub(int totalSub) { this.totalSub = totalSub; }

    public int getDisAc() { return disAc; }

    public void setDisAc(int disAc) { this.disAc = disAc; }

    public int getTotalAc() { return totalAc; }

    public void setTotalAc(int totalAc) { this.totalAc = totalAc; }

    public int getTotalWa() { return totalWa; }

    public void setTotalWa(int totalWa) { this.totalWa = totalWa; }

    public int getTotalOt() { return totalOt; }

    public void setTotalOt(int totalOt) { this.totalOt = totalOt; }

    public Map<String, Integer> getSolvedSet() {
        return solvedSet;
    }

    public void setSolvedSet(Map<String, Integer> solvedSet) {
        this.solvedSet = solvedSet;
    }

    public Map<String, Integer> getUnsolvedSet() { return unsolvedSet; }

    public void setUnsolvedSet(Map<String, Integer> unsolvedSet) { this.unsolvedSet = unsolvedSet; }

    public int getDayWiseAc(int day){
        int ac = 0;
        long unixTime = System.currentTimeMillis() / 1000L;
        unixTime = (unixTime - (unixTime % 86400)) - (86400 * day);
        for(int value : solvedSet.values()){
            if(value>=unixTime && value<=(unixTime+86400)){
                ac++;
            }
        }
        return ac;
    }

    public int getDayWiseUn(int day){
        int un = 0;
        long unixTime = System.currentTimeMillis() / 1000L;
        unixTime = (unixTime - (unixTime % 86400)) - (86400 * day);
        for(int value : unsolvedSet.values()){
            if(value>=unixTime && value<=(unixTime+86400)){
                un++;
            }
        }
        return un;
    }
}