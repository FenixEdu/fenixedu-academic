/*
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Shift;

/**
 * @author tfc130
 */
public class InfoExecutionCourse extends InfoObject {

    private DomainReference<ExecutionCourse> executionCourseDomainReference;

    public InfoExecutionCourse(final ExecutionCourse executionCourse) {
	executionCourseDomainReference = new DomainReference<ExecutionCourse>(executionCourse);
    }

    public static InfoExecutionCourse newInfoFromDomain(final ExecutionCourse executionCourse) {
	return executionCourse == null ? null : new InfoExecutionCourse(executionCourse);
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourseDomainReference == null ? null : executionCourseDomainReference
		.getObject();
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoExecutionCourse
		&& getExecutionCourse() == ((InfoExecutionCourse) obj).getExecutionCourse();
    }

    public int hashCode() {
	return getExecutionCourse().hashCode();
    }

    @Override
    public Integer getIdInternal() {
	return getExecutionCourse().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

    //=================== FIELDS RETRIEVED BY DOMAIN LOGIC =======================

    public String getNome() {
	return getExecutionCourse().getNome();
    }

    public String getSigla() {
	return getExecutionCourse().getSigla();
    }

    public String getComment() {
	return getExecutionCourse().getComment();
    }

    public Boolean getHasSite() {
	return getExecutionCourse().hasSite();
    }

    public Double getTheoreticalHours() {
	return getExecutionCourse().getTheoreticalHours();
    }

    public Double getPraticalHours() {
	return getExecutionCourse().getPraticalHours();
    }

    public Double getTheoPratHours() {
	return getExecutionCourse().getTheoPratHours();
    }

    public Double getLabHours() {
	return getExecutionCourse().getLabHours();
    }

    public Double getFieldWorkHours() {
	return getExecutionCourse().getFieldWorkHours();
    }

    public Double getProblemsHours() {
	return getExecutionCourse().getProblemsHours();
    }

    public Double getSeminaryHours() {
	return getExecutionCourse().getSeminaryHours();
    }

    public Double getTrainingPeriodHours() {
	return getExecutionCourse().getTrainingPeriodHours();
    }

    public Double getTutorialOrientationHours() {
	return getExecutionCourse().getTutorialOrientationHours();
    }

    public Integer getNumberOfAttendingStudents() {
	return getExecutionCourse().getAttendsCount();
    }

    public String getCourseReportFilled() {
	return (!getExecutionCourse().hasCourseReport() || getExecutionCourse().getCourseReport()
		.getReport() == null) ? "false" : "true";
    }

    public String getEqualLoad() {
	return getExecutionCourse().getEqualLoad();
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
	return InfoExecutionPeriod.newInfoFromDomain(getExecutionCourse().getExecutionPeriod());
    }

    public List<InfoShift> getAssociatedInfoShifts() {
	final List<InfoShift> result = new ArrayList<InfoShift>();

	for (final Shift shift : getExecutionCourse().getAssociatedShifts()) {
	    result.add(InfoShift.newInfoFromDomain(shift));
	}

	return result;
    }

    public List<InfoNonAffiliatedTeacher> getNonAffiliatedTeachers() {
	final List<InfoNonAffiliatedTeacher> result = new ArrayList<InfoNonAffiliatedTeacher>();

	for (final NonAffiliatedTeacher nonAffiliatedTeacher : getExecutionCourse()
		.getNonAffiliatedTeachers()) {
	    result.add(InfoNonAffiliatedTeacher.newInfoFromDomain(nonAffiliatedTeacher));
	}

	return result;
    }

    public List<InfoEvaluation> getAssociatedInfoEvaluations() {
	final List<InfoEvaluation> result = new ArrayList<InfoEvaluation>();

	for (final Evaluation nonAffiliatedTeacher : getExecutionCourse().getAssociatedEvaluations()) {
	    result.add(InfoEvaluation.newInfoFromDomain(nonAffiliatedTeacher));
	}

	return result;
    }

    public List<InfoBibliographicReference> getAssociatedInfoBibliographicReferences() {
	final List<InfoBibliographicReference> result = new ArrayList<InfoBibliographicReference>();
	
        for (final BibliographicReference bibliographicReference : getExecutionCourse().getAssociatedBibliographicReferencesSet()) {
            result.add(InfoBibliographicReference.newInfoFromDomain(bibliographicReference));
        }
        
        return result;
    }

    public List<InfoCurricularCourse> getAssociatedInfoCurricularCourses() {
	if (filteredAssociatedInfoCurricularCourses == null) {
	    List<InfoCurricularCourse> result = new ArrayList<InfoCurricularCourse>();

	    for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCourses()) {
		final InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
		infoCurricularCourse.setInfoScopes(getInfoScopes(curricularCourse));
		
		result.add(infoCurricularCourse);
	    }

	    return result;
	} else {
	    return getFilteredAssociatedInfoCurricularCourses();
	}
    }

    private List<InfoCurricularCourseScope> getInfoScopes(final CurricularCourse curricularCourse) {
        final List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>();
        
        for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopesSet()) {
            result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
        }
        
        return result;
    }

    public List getAssociatedInfoExams() {
	if (filteredAssociatedInfoExams == null) {
	    List<InfoExam> result = new ArrayList<InfoExam>();

	    for (final Exam exam : getExecutionCourse().getAssociatedExams()) {
		result.add(InfoExam.newInfoFromDomain(exam));
	    }

	    return result;
	} else {
	    return getFilteredAssociatedInfoExams();
	}
    }

    public List<InfoGrouping> getInfoGroupings() {
	if (filteredInfoGroupings == null) {
	    List<InfoGrouping> result = new ArrayList<InfoGrouping>();

	    for (final Grouping grouping : getExecutionCourse().getGroupings()) {
		result.add(InfoGrouping.newInfoFromDomain(grouping));
	    }

	    return result;
	} else {
	    return getFilteredInfoGroupings();
	}
    }
    

    //=================== FIELDS NOT RETRIEVED BY DOMAIN LOGIC =======================

    // The following variable serves the purpose of indicating the
    // the curricular year in which the execution course is given
    // for a certain execution degree through which
    // the execution course was obtained. It should serve only for
    // view purposes!!!
    // It was created to be used and set by the ExamsMap Utilities.
    // It has no meaning in the buisness logic.
    private Integer curricularYear;

    public Integer getCurricularYear() {
	return curricularYear;
    }

    public void setCurricularYear(Integer integer) {
	curricularYear = integer;
    }

    // useful for coordinator portal
    protected InfoSiteEvaluationStatistics infoSiteEvaluationStatistics;

    public InfoSiteEvaluationStatistics getInfoSiteEvaluationStatistics() {
	return infoSiteEvaluationStatistics;
    }

    public void setInfoSiteEvaluationStatistics(InfoSiteEvaluationStatistics infoSiteEvaluationStatistics) {
	this.infoSiteEvaluationStatistics = infoSiteEvaluationStatistics;
    }

    private Double occupancy;
    
    public Double getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Double occupancy) {
        this.occupancy = occupancy;
    }

    private List<InfoCurricularCourse> filteredAssociatedInfoCurricularCourses;

    private List<InfoCurricularCourse> getFilteredAssociatedInfoCurricularCourses() {
	return filteredAssociatedInfoCurricularCourses;
    }

    public void setFilteredAssociatedInfoCurricularCourses(
	    final List<InfoCurricularCourse> filteredAssociatedInfoCurricularCourses) {
	this.filteredAssociatedInfoCurricularCourses = filteredAssociatedInfoCurricularCourses;
    }

    private List<InfoExam> filteredAssociatedInfoExams;

    private List<InfoExam> getFilteredAssociatedInfoExams() {
	return filteredAssociatedInfoExams;
    }

    public void setFilteredAssociatedInfoExams(final List<InfoExam> filteredAssociatedInfoExams) {
	this.filteredAssociatedInfoExams = filteredAssociatedInfoExams;
    }
    
    public String toString() {
	return getExecutionCourse().toString();
    }

    private List<InfoGrouping> filteredInfoGroupings;
    
    private List<InfoGrouping> getFilteredInfoGroupings() {
	return filteredInfoGroupings;
    }

    public void setFilteredInfoGroupings(List<InfoGrouping> filteredInfoGroupings) {
	this.filteredInfoGroupings = filteredInfoGroupings;
    }

    public Boolean getAvailableForInquiries() {
	return getExecutionCourse().getAvailableForInquiries();
    }
    
    public Boolean getAvailableGradeSubmission() {
	return getExecutionCourse().getAvailableGradeSubmission();
    }

}
