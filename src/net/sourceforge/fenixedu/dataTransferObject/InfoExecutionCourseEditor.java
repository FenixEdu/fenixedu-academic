package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

public class InfoExecutionCourseEditor extends InfoObject {

    protected String _nome;

    protected String _sigla;

    protected String _programa;

    private Double _theoreticalHours;

    private Double _praticalHours;

    private Double _theoPratHours;

    private Double _labHours;

    private Double _seminaryHours;

    private Double _problemsHours;

    private Double _fieldWorkHours;

    private Double _trainingPeriodHours;

    private Double _tutorialOrientationHours;
    
    private Boolean availableGradeSubmission;

    protected String comment;

    protected InfoExecutionPeriod infoExecutionPeriod;

    protected List associatedInfoCurricularCourses;

    public InfoExecutionCourseEditor() {
    }

    public boolean equals(Object obj) {
	boolean resultado = false;
	if (obj instanceof InfoExecutionCourseEditor) {
	    InfoExecutionCourseEditor infoExecutionCourse = (InfoExecutionCourseEditor) obj;
	    resultado = (getIdInternal() != null && infoExecutionCourse.getIdInternal() != null && getIdInternal()
		    .equals(infoExecutionCourse.getIdInternal()))
		    || (getSigla().equals(infoExecutionCourse.getSigla()) && getInfoExecutionPeriod()
			    .equals(infoExecutionCourse.getInfoExecutionPeriod()));
	}
	return resultado;
    }

    public int hashCode() {
	return 0;
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

    public String getComment() {
	return comment;
    }

    public void setComment(String string) {
	comment = string;
    }

    public Double getFieldWorkHours() {
	return _fieldWorkHours;
    }

    public void setFieldWorkHours(Double workHours) {
	_fieldWorkHours = workHours;
    }

    public Double getProblemsHours() {
	return _problemsHours;
    }

    public void setProblemsHours(Double hours) {
	_problemsHours = hours;
    }

    public Double getSeminaryHours() {
	return _seminaryHours;
    }

    public void setSeminaryHours(Double hours) {
	_seminaryHours = hours;
    }

    public Double getTrainingPeriodHours() {
	return _trainingPeriodHours;
    }

    public void setTrainingPeriodHours(Double periodHours) {
	_trainingPeriodHours = periodHours;
    }

    public Double getTutorialOrientationHours() {
	return _tutorialOrientationHours;
    }

    public void setTutorialOrientationHours(Double orientationHours) {
	_tutorialOrientationHours = orientationHours;
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

    public void setAssociatedInfoCurricularCourses(List list) {
	associatedInfoCurricularCourses = list;
    }

    public Boolean getAvailableGradeSubmission() {
        return availableGradeSubmission;
    }

    public void setAvailableGradeSubmission(Boolean availableGradeSubmission) {
        this.availableGradeSubmission = availableGradeSubmission;
    }

}
