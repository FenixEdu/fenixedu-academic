package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

public class FenixExecutionCourse {

    String acronym;
    String credits;
    String name;
    String id;
    String ecYear;
    String sem;

    public FenixExecutionCourse(String acronym, String credits, String name, String id, String ecYear, String sem) {
        super();
        this.acronym = acronym;
        this.credits = credits;
        this.name = name;
        this.id = id;
        this.ecYear = ecYear;
        this.sem = sem;
    }

    public String getSigla() {
        return acronym;
    }

    public void setSigla(String acronym) {
        this.acronym = acronym;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEcYear() {
        return ecYear;
    }

    public void setEcYear(String ecYear) {
        this.ecYear = ecYear;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

}
