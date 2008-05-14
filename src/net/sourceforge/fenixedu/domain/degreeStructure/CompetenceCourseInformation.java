package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager.CompetenceCourseLoadBean;
import net.sourceforge.fenixedu.util.StringFormatter;

public class CompetenceCourseInformation extends CompetenceCourseInformation_Base {

    static public final Comparator<CompetenceCourseInformation> COMPARATORY_BY_EXECUTION_PERIOD = new Comparator<CompetenceCourseInformation>() {
	public int compare(CompetenceCourseInformation o1, CompetenceCourseInformation o2) {
	    return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
	}
    };

    protected CompetenceCourseInformation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CompetenceCourseInformation(String name, String nameEn, Boolean basic, RegimeType regimeType,
	    CompetenceCourseLevel competenceCourseLevel, ExecutionSemester period) {

	this();
	checkParameters(name, nameEn, basic, regimeType, competenceCourseLevel);
	setName(StringFormatter.prettyPrint(name));
	setNameEn(StringFormatter.prettyPrint(nameEn));
	setBasic(basic);
	setRegime(regimeType);
	setCompetenceCourseLevel(competenceCourseLevel);
	setBibliographicReferences(new BibliographicReferences());
	setExecutionPeriod(period);
    }

    private void checkParameters(String name, String nameEn, Boolean basic, RegimeType regimeType,
	    CompetenceCourseLevel competenceCourseLevel) {

	if (name == null || nameEn == null || basic == null || regimeType == null || competenceCourseLevel == null) {
	    throw new DomainException("competence.course.information.invalid.parameters");
	}
    }

    public void edit(String name, String nameEn, Boolean basic, CompetenceCourseLevel competenceCourseLevel) {
	checkParameters(name, nameEn, basic, getRegime(), competenceCourseLevel);
	setName(StringFormatter.prettyPrint(name));
	setNameEn(StringFormatter.prettyPrint(nameEn));
	setBasic(basic);
	setCompetenceCourseLevel(competenceCourseLevel);
    }

    public void edit(String objectives, String program, String evaluationMethod, String objectivesEn, String programEn,
	    String evaluationMethodEn) {
	setObjectives(objectives);
	setProgram(program);
	setEvaluationMethod(evaluationMethod);
	setObjectivesEn(objectivesEn);
	setProgramEn(programEn);
	setEvaluationMethodEn(evaluationMethodEn);
    }

    public void delete() {
	removeExecutionPeriod();
	removeCompetenceCourse();
	for (; !getCompetenceCourseLoads().isEmpty(); getCompetenceCourseLoads().get(0).delete())
	    ;
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public BibliographicReference getBibliographicReference(Integer oid) {
	return getBibliographicReferences().getBibliographicReference(oid);
    }

    public Double getTheoreticalHours(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getTheoreticalHours();
	}
	return result;
    }

    public Double getProblemsHours(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getProblemsHours();
	}
	return result;
    }

    public Double getLaboratorialHours(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getLaboratorialHours();
	}
	return result;
    }

    public Double getSeminaryHours(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getSeminaryHours();
	}
	return result;
    }

    public Double getFieldWorkHours(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getFieldWorkHours();
	}
	return result;
    }

    public Double getTrainingPeriodHours(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getTrainingPeriodHours();
	}
	return result;
    }

    public Double getTutorialOrientationHours(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getTutorialOrientationHours();
	}
	return result;
    }

    public Double getAutonomousWorkHours(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getAutonomousWorkHours();
	}
	return result;
    }

    public Double getContactLoad(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getContactLoad();
	}
	return result;
    }

    public Double getTotalLoad(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getTotalLoad();
	}
	return result;
    }

    public double getEctsCredits(Integer order) {
	double result = 0.0;
	for (final CompetenceCourseLoadBean competenceCourseLoad : getCompetenceCourseLoadBeans(order)) {
	    result += competenceCourseLoad.getEctsCredits();
	}
	return result;
    }

    private List<CompetenceCourseLoadBean> getCompetenceCourseLoadBeans(final Integer order) {

	if (isSemestrial()) {
	    return Collections.singletonList(new CompetenceCourseLoadBean(getCompetenceCourseLoads().get(0)));
	}

	if (isAnual()) {
	    final List<CompetenceCourseLoadBean> result = new ArrayList<CompetenceCourseLoadBean>();

	    for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
		result.add(new CompetenceCourseLoadBean(competenceCourseLoad));
	    }

	    if (getCompetenceCourseLoadsCount() == 1) { // hack
		final CompetenceCourseLoad courseLoad = getCompetenceCourseLoads().get(0);
		final CompetenceCourseLoadBean courseLoadBean = new CompetenceCourseLoadBean(courseLoad);
		courseLoadBean.setLoadOrder(courseLoad.getLoadOrder() + 1);
		result.add(courseLoadBean);
	    }

	    final Iterator<CompetenceCourseLoadBean> loads = result.iterator();
	    while (loads.hasNext()) {
		final CompetenceCourseLoadBean courseLoadBean = loads.next();
		if (order != null && !courseLoadBean.getLoadOrder().equals(order)) {
		    loads.remove();
		}
	    }
	    return result;
	}

	return Collections.emptyList();
    }

    public boolean isAnual() {
	return getRegime() == RegimeType.ANUAL;
    }

    public boolean isSemestrial() {
	return getRegime() == RegimeType.SEMESTRIAL;
    }

    public List<CompetenceCourseInformationChangeRequest> getCompetenceCourseInformationChangeRequest() {
	final List<CompetenceCourseInformationChangeRequest> requests = new ArrayList<CompetenceCourseInformationChangeRequest>();
	for (final CompetenceCourseInformationChangeRequest request : this.getCompetenceCourse()
		.getCompetenceCourseInformationChangeRequests()) {
	    if (request.getExecutionPeriod().equals(this.getExecutionPeriod())) {
		requests.add(request);
	    }
	}
	return requests;
    }

    public boolean isCompetenceCourseInformationChangeRequestDraftAvailable() {
	for (final CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequest()) {
	    if (request.getApproved() == null) {
		return true;
	    }
	}
	return false;
    }
    
    public ExecutionYear getExecutionYear() {
	return getExecutionPeriod().getExecutionYear();
    }
    
}
