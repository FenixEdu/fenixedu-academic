package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

public class FenixExecutionCourse {

    String acronym;
    String credits;
    String name;
    String id;
    String academicTerm;

    public FenixExecutionCourse(String acronym, String credits, String name, String id, String academicTerm) {
        super();
        this.acronym = acronym;
        this.credits = credits;
        this.name = name;
        this.id = id;
        this.academicTerm = academicTerm;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
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

    public String getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }
}
