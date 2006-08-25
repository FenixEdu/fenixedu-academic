package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;

/*
 * InfoMasterDegreeCandidate.java
 *
 * Created on 29 de Novembro de 2002, 15:57
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

public class InfoMasterDegreeCandidate extends InfoObject {

    private String majorDegree = null;

    private Integer candidateNumber = null;

    private String majorDegreeSchool = null;

    private Integer majorDegreeYear = null;

    private Double average = null;

    private InfoPerson infoPerson = null;

    private InfoExecutionDegree infoExecutionDegree = null;

    private Specialization specialization = null;

    private InfoCandidateSituation infoCandidateSituation = null;

    private List situationList = null;

    private String specializationArea;

    private Integer substituteOrder;

    private Double givenCredits;

    private String givenCreditsRemarks;

    private Boolean courseAssistant;

    private String coursesToAssist;

    private InfoTeacher infoGuider;

    private Boolean hasGuider;

    /**
     * @return
     */
    public String getGivenCreditsRemarks() {
        return givenCreditsRemarks;
    }

    /**
     * @param givenCreditsRemarks
     */
    public void setGivenCreditsRemarks(String givenCreditsRemarks) {
        this.givenCreditsRemarks = givenCreditsRemarks;
    }

    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public InfoMasterDegreeCandidate() {
    }

    public InfoMasterDegreeCandidate(InfoPerson person, InfoExecutionDegree executionDegree,
            Integer candidateNumber, Specialization specialization, String majorDegree,
            String majorDegreeSchool, Integer majorDegreeYear, Double average, Boolean courseAssistant,
            String coursesToAssist, InfoTeacher infoGuider, Boolean hasGuider) {
        this.infoPerson = person;
        this.infoExecutionDegree = executionDegree;
        this.candidateNumber = candidateNumber;
        this.specialization = specialization;
        this.majorDegree = majorDegree;
        this.majorDegreeSchool = majorDegreeSchool;
        this.majorDegreeYear = majorDegreeYear;
        this.average = average;
        this.courseAssistant = courseAssistant;
        this.coursesToAssist = coursesToAssist;
        this.infoGuider = infoGuider;
        this.hasGuider = hasGuider;
    }

    public boolean equals(Object o) {
        return ((o instanceof InfoMasterDegreeCandidate)
                &&

                ((this.infoPerson.equals(((InfoMasterDegreeCandidate) o).getInfoPerson()))
                        && (this.specialization.equals(((InfoMasterDegreeCandidate) o)
                                .getSpecialization())) && (this.infoExecutionDegree
                        .equals(((InfoMasterDegreeCandidate) o).getInfoExecutionDegree()))) ||

        ((this.infoExecutionDegree.equals(((InfoMasterDegreeCandidate) o).getInfoExecutionDegree()))
                && (this.candidateNumber.equals(((InfoMasterDegreeCandidate) o).getCandidateNumber())) && (this.specialization
                .equals(((InfoMasterDegreeCandidate) o).getSpecialization()))));

    }

    /**
     * @return Specialization Area
     */
    public String getSpecializationArea() {
        return specializationArea;
    }

    /**
     * @param specializationArea
     */
    public void setSpecializationArea(String specializationArea) {
        this.specializationArea = specializationArea;
    }

    public String toString() {
        String result = "Master Degree Candidate :\n";
        result += "\n  - Person : " + infoPerson;
        result += "\n  - Major Degree : " + majorDegree;
        result += "\n  - Candidate Number : " + candidateNumber;
        result += "\n  - Specialization : " + specialization;
        result += "\n  - Major Degree School : " + majorDegreeSchool;
        result += "\n  - Major Degree Year : " + majorDegreeYear;
        result += "\n  - Major Degree Average : " + average;
        result += "\n  - Master Degree : " + infoExecutionDegree;
        result += "\n  - Specialization Area  : " + specializationArea;
        result += "\n  - Substitute Order  : " + substituteOrder;
        result += "\n  - Given Credits  : " + givenCredits;
        result += "\n  - Given Credits Remarks  : " + givenCreditsRemarks;
        result += "\n  - Deseja dar aulas? : " + (this.courseAssistant.booleanValue() ? "Sim" : "Nao");
        if (this.courseAssistant.booleanValue())
            result += "\n  - Deseja dar aulas as seguintes cadeiras : " + this.coursesToAssist;
        if (this.infoGuider != null) {
            if (this.hasGuider)
                result += "\n  - Orientador : " + this.infoGuider.getTeacherNumber() + " - " +
                    this.infoGuider.getInfoPerson().getNome();
            else result += "\n  - Conselheiro : " + this.infoGuider.getTeacherNumber() + " - " +
                    this.infoGuider.getInfoPerson().getNome();
        }
        return result;
    }

    /**
     * @return
     */
    public Double getAverage() {
        return average;
    }

    /**
     * @return
     */
    public Integer getCandidateNumber() {
        return candidateNumber;
    }

    /**
     * @return
     */
    public InfoCandidateSituation getInfoCandidateSituation() {
        return infoCandidateSituation;
    }

    /**
     * @return
     */
    public InfoExecutionDegree getInfoExecutionDegree() {
        return infoExecutionDegree;
    }

    /**
     * @return
     */   
    public InfoPerson getInfoPerson() {
        return infoPerson;
    }
 
    /**
     * @return
     */
    public String getMajorDegree() {
        return majorDegree;
    }

    /**
     * @return
     */
    public String getMajorDegreeSchool() {
        return majorDegreeSchool;
    }

    /**
     * @return
     */
    public Integer getMajorDegreeYear() {
        return majorDegreeYear;
    }

    /**
     * @return
     */
    public List getSituationList() {
        return situationList;
    }

    /**
     * @return
     */
    public Specialization getSpecialization() {
        return specialization;
    }

    public Boolean getCourseAssistant() {
            return courseAssistant;
        }
    
    public String getCoursesToAssist() {
        return coursesToAssist;
    }

    /**
     * @param double1
     */
    public void setAverage(Double double1) {
        average = double1;
    }

    /**
     * @param integer
     */
    public void setCandidateNumber(Integer integer) {
        candidateNumber = integer;
    }

    /**
     * @param situation
     */
    public void setInfoCandidateSituation(InfoCandidateSituation situation) {
        infoCandidateSituation = situation;
    }

    /**
     * @param degree
     */
    public void setInfoExecutionDegree(InfoExecutionDegree degree) {
        infoExecutionDegree = degree;
    }

    /**
     * @param person
     */
    public void setInfoPerson(InfoPerson person) {
        infoPerson = person;
    }

    /**
     * @param string
     */
    public void setMajorDegree(String string) {
        majorDegree = string;
    }

    /**
     * @param string
     */
    public void setMajorDegreeSchool(String string) {
        majorDegreeSchool = string;
    }

    /**
     * @param integer
     */
    public void setMajorDegreeYear(Integer integer) {
        majorDegreeYear = integer;
    }

    /**
     * @param list
     */
    public void setSituationList(List list) {
        situationList = list;
    }

    /**
     * @param string
     */
    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    /**
     * @return
     */
    public Integer getSubstituteOrder() {
        return substituteOrder;
    }

    /**
     * @param integer
     */
    public void setSubstituteOrder(Integer integer) {
        substituteOrder = integer;
    }

    /**
     * @return
     */
    public Double getGivenCredits() {
        return givenCredits;
    }

    /**
     * @param double1
     */
    public void setGivenCredits(Double double1) {
        givenCredits = double1;
    }

    public void setCourseAssistant(Boolean courseAssistant) {
        this.courseAssistant = courseAssistant;
    }

    public void setCoursesToAssist(String coursesToAssist) {
        this.coursesToAssist = coursesToAssist;
    }

    public void copyFromDomain(MasterDegreeCandidate masterDegreeCandidate) {
        super.copyFromDomain(masterDegreeCandidate);
        if (masterDegreeCandidate != null) {
            setAverage(masterDegreeCandidate.getAverage());
            setCandidateNumber(masterDegreeCandidate.getCandidateNumber());
            setGivenCredits(masterDegreeCandidate.getGivenCredits());
            setGivenCreditsRemarks(masterDegreeCandidate.getGivenCreditsRemarks());
            setMajorDegree(masterDegreeCandidate.getMajorDegree());
            setMajorDegreeSchool(masterDegreeCandidate.getMajorDegreeSchool());
            setMajorDegreeYear(masterDegreeCandidate.getMajorDegreeYear());
            setSpecializationArea(masterDegreeCandidate.getSpecializationArea());
            setSpecialization(masterDegreeCandidate.getSpecialization());
            setCourseAssistant(masterDegreeCandidate.getCourseAssistant());
            setCoursesToAssist(masterDegreeCandidate.getCoursesToAssist());
            setSubstituteOrder(masterDegreeCandidate.getSubstituteOrder());
            setInfoCandidateSituation(InfoCandidateSituation.newInfoFromDomain(masterDegreeCandidate.getActiveCandidateSituation()));
            setInfoExecutionDegree(InfoExecutionDegree.newInfoFromDomain(masterDegreeCandidate.getExecutionDegree()));
            if (masterDegreeCandidate.getGuider() != null)
                setInfoGuider(InfoTeacher.newInfoFromDomain(masterDegreeCandidate.getGuider()));
            setHasGuider(masterDegreeCandidate.getHasGuider());
        }
    }

    public static InfoMasterDegreeCandidate newInfoFromDomain(
            MasterDegreeCandidate masterDegreeCandidate) {
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
        if (masterDegreeCandidate != null) {
            infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
            infoMasterDegreeCandidate.copyFromDomain(masterDegreeCandidate);
        }
        return infoMasterDegreeCandidate;
    }

    public InfoTeacher getInfoGuider() {
        return infoGuider;
}
    public void setInfoGuider(InfoTeacher infoGuider) {
        this.infoGuider = infoGuider;
    }

    public Boolean getHasGuider() {
        return hasGuider;
    }

    public void setHasGuider(Boolean hasGuider) {
        this.hasGuider = hasGuider;
    }
}

// RMCC&TAFN Stop Here