package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.documents;

import java.io.Serializable;

public class ApprovementMobilityCertificateBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private String courseName;

    private String ectsCredits;

    private String istGrade;

    private String ectsGrade;

    private String date;

    public ApprovementMobilityCertificateBean() {
        super();
        this.courseName = null;
        this.ectsCredits = null;
        this.istGrade = null;
        this.ectsGrade = null;
        this.date = null;
    }

    public ApprovementMobilityCertificateBean(String courseName, String ectsCredits, String istGrade, String ectsGrade,
            String date) {
        super();
        this.courseName = courseName;
        this.ectsCredits = ectsCredits;
        this.istGrade = istGrade;
        this.ectsGrade = ectsGrade;
        this.date = date;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEctsCredits() {
        return ectsCredits;
    }

    public void setEctsCredits(String ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    public String getIstGrade() {
        return istGrade;
    }

    public void setIstGrade(String istGrade) {
        this.istGrade = istGrade;
    }

    public String getEctsGrade() {
        return ectsGrade;
    }

    public void setEctsGrade(String ectsGrade) {
        this.ectsGrade = ectsGrade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
