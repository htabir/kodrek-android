package app.kodrek;

import java.util.List;
import java.util.Map;

public abstract class ProblemSet {
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

    public abstract int countSolved(OjData ojData, String ojName);

    public abstract int countUnsolved(OjData ojData, String ojName);

}
