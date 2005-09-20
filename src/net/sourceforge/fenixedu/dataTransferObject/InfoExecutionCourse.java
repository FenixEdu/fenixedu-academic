/*
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * @author tfc130
 */
public class InfoExecutionCourse extends InfoObject {

    protected String _nome;

    protected String _sigla;

    protected String _programa;

    private Double _theoreticalHours;

    private Double _praticalHours;

    private Double _theoPratHours;

    private Double _labHours;

    private Double occupancy;

    protected List associatedInfoCurricularCourses;

    protected List associatedInfoExams;

    protected List associatedInfoEvaluations;

    protected Integer numberOfAttendingStudents;

    protected List associatedInfoShifts;
    
    protected List nonAffiliatedTeachers;

    protected List infoGroupings;

    protected String comment;

    // useful for coordinator portal
    protected InfoSiteEvaluationStatistics infoSiteEvaluationStatistics;

    protected String courseReportFilled;

    /**
     * Tells if all the associated Curricular Courses load are the same
     */
    protected String equalLoad;

    // A chave do responsavel falta ainda porque ainda nao existe a respeciva
    // ligacao
    // na base de dados.
    protected InfoExecutionPeriod infoExecutionPeriod;

    // The following variable serves the purpose of indicating the
    // the curricular year in which the execution course is given
    // for a certain execution degree through which
    // the execution course was obtained. It should serve only for
    // view purposes!!!
    // It was created to be used and set by the ExamsMap Utilities.
    // It has no meaning in the buisness logic.
    private Integer curricularYear;

    private Boolean hasSite;

    public InfoExecutionCourse() {
    }

    public InfoExecutionCourse(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * @param nome
     * @param sigla
     * @param programa
     * @param infoLicenciaturaExecucao
     * @param theoreticalHours
     * @param praticalHours
     * @param theoPratHours
     * @param labHours
     * @deprecated
     */
    public InfoExecutionCourse(String nome, String sigla, String programa,
            InfoExecutionDegree infoLicenciaturaExecucao, Double theoreticalHours, Double praticalHours,
            Double theoPratHours, Double labHours) {
        setNome(nome);
        setSigla(sigla);
        setPrograma(programa);
        // setInfoLicenciaturaExecucao(infoLicenciaturaExecucao);
        setTheoreticalHours(theoreticalHours);
        setPraticalHours(praticalHours);
        setTheoPratHours(theoPratHours);
        setLabHours(labHours);
    }

    public InfoExecutionCourse(String nome, String sigla, String programa, Double theoreticalHours,
            Double praticalHours, Double theoPratHours, Double labHours,
            InfoExecutionPeriod infoExecutionPeriod) {
        setNome(nome);
        setSigla(sigla);
        setPrograma(programa);
        setTheoreticalHours(theoreticalHours);
        setPraticalHours(praticalHours);
        setTheoPratHours(theoPratHours);
        setLabHours(labHours);
        setInfoExecutionPeriod(infoExecutionPeriod);
    }

    /**
     * @deprecated
     * @param nome
     * @param sigla
     * @param programa
     * @param infoLicenciaturaExecucao
     * @param theoreticalHours
     * @param praticalHours
     * @param theoPratHours
     * @param labHours
     * @param semester
     */
    public InfoExecutionCourse(String nome, String sigla, String programa,
            InfoExecutionDegree infoLicenciaturaExecucao, Double theoreticalHours, Double praticalHours,
            Double theoPratHours, Double labHours, Integer semester) {
        setNome(nome);
        setSigla(sigla);
        setPrograma(programa);
        // setInfoLicenciaturaExecucao(infoLicenciaturaExecucao);
        setTheoreticalHours(theoreticalHours);
        setPraticalHours(praticalHours);
        setTheoPratHours(theoPratHours);
        setLabHours(labHours);
    }

    public String getNome() {
        return _nome;
    }

    public void setNome(String nome) {
        _nome = nome;
    }

    public String getSigla() {
        return _sigla;
    }

    public void setSigla(String sigla) {
        _sigla = sigla;
    }

    public String getPrograma() {
        return _programa;
    }

    public void setPrograma(String programa) {
        _programa = programa;
    }

    public Double getTheoreticalHours() {
        return _theoreticalHours;
    }

    public void setTheoreticalHours(Double theoreticalHours) {
        _theoreticalHours = theoreticalHours;
    }

    public Double getPraticalHours() {
        return _praticalHours;
    }

    public void setPraticalHours(Double praticalHours) {
        _praticalHours = praticalHours;
    }

    public Double getTheoPratHours() {
        return _theoPratHours;
    }

    public void setTheoPratHours(Double theoPratHours) {
        _theoPratHours = theoPratHours;
    }

    public Double getLabHours() {
        return _labHours;
    }

    public void setLabHours(Double labHours) {
        _labHours = labHours;
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoExecutionCourse) {
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) obj;
            resultado = (getIdInternal() != null && infoExecutionCourse.getIdInternal() != null && getIdInternal()
                    .equals(infoExecutionCourse.getIdInternal()))
                    || (getSigla().equals(infoExecutionCourse.getSigla()) && getInfoExecutionPeriod()
                            .equals(infoExecutionCourse.getInfoExecutionPeriod()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[INFODISCIPLINAEXECUCAO";
        result += ", nome=" + _nome;
        result += ", sigla=" + _sigla;
        result += ", programa=" + _programa;
        result += ", theoreticalHours=" + _theoreticalHours;
        result += ", praticalHours=" + _praticalHours;
        result += ", theoPratHours=" + _theoPratHours;
        result += ", labHours=" + _labHours;
        result += ", infoExecutionPeriod=" + infoExecutionPeriod;
        result += "]";
        return result;
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    public List getAssociatedInfoCurricularCourses() {
        return associatedInfoCurricularCourses;
    }

    public List getAssociatedInfoExams() {
        return associatedInfoExams;
    }

    public void setAssociatedInfoCurricularCourses(List list) {
        associatedInfoCurricularCourses = list;
    }

    public void setAssociatedInfoExams(List list) {
        associatedInfoExams = list;
    }

    public Integer getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(Integer integer) {
        curricularYear = integer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String string) {
        comment = string;
    }

    public List getAssociatedInfoEvaluations() {
        return associatedInfoEvaluations;
    }

    public void setAssociatedInfoEvaluations(List list) {
        associatedInfoEvaluations = list;
    }

    public Double getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Double occupancy) {
        this.occupancy = occupancy;
    }

    public Integer getNumberOfAttendingStudents() {
        return numberOfAttendingStudents;
    }

    public void setNumberOfAttendingStudents(Integer attendingStudents) {
        this.numberOfAttendingStudents = attendingStudents;
    }

    public String getEqualLoad() {
        return equalLoad;
    }

    public void setEqualLoad(String equalLoad) {
        this.equalLoad = equalLoad;
    }

    public Boolean getHasSite() {
        return hasSite;
    }

    public void setHasSite(Boolean hasSite) {
        this.hasSite = hasSite;
    }

    public String getCourseReportFilled() {
        return courseReportFilled;
    }

    public void setCourseReportFilled(String courseReportFilled) {
        this.courseReportFilled = courseReportFilled;
    }

    public InfoSiteEvaluationStatistics getInfoSiteEvaluationStatistics() {
        return infoSiteEvaluationStatistics;
    }

    public void setInfoSiteEvaluationStatistics(InfoSiteEvaluationStatistics infoSiteEvaluationStatistics) {
        this.infoSiteEvaluationStatistics = infoSiteEvaluationStatistics;
    }

    public List getAssociatedInfoShifts() {
        return associatedInfoShifts;
    }

    public void setAssociatedInfoShifts(List associatedInfoShifts) {
        this.associatedInfoShifts = associatedInfoShifts;
    }

    public void copyFromDomain(IExecutionCourse executionCourse) {
        super.copyFromDomain(executionCourse);
        if (executionCourse != null) {
            setNome(executionCourse.getNome());
            setSigla(executionCourse.getSigla());
            setTheoreticalHours(executionCourse.getTheoreticalHours());
            setTheoPratHours(executionCourse.getTheoPratHours());
            setLabHours(executionCourse.getLabHours());
            setPraticalHours(executionCourse.getPraticalHours());
            setComment(executionCourse.getComment());
			setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(executionCourse.getExecutionPeriod()));
        }
    }

    public static InfoExecutionCourse newInfoFromDomain(IExecutionCourse executionCourse) {
        InfoExecutionCourse infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourse();
            infoExecutionCourse.copyFromDomain(executionCourse);
        }
        return infoExecutionCourse;
    }
    
    public List getNonAffiliatedTeachers() {
        return nonAffiliatedTeachers;
    }
    

    public void setNonAffiliatedTeachers(List nonAffiliatedTeachers) {
        this.nonAffiliatedTeachers = nonAffiliatedTeachers;
    }

    public List getInfoGroupings() {
        return infoGroupings;
    }

    public void setInfoGroupings(List infoGroupings) {
        this.infoGroupings = infoGroupings;
    }
    
    
}