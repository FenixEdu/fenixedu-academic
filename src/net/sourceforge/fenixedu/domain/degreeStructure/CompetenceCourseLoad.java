package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class CompetenceCourseLoad extends CompetenceCourseLoad_Base implements Comparable {

    public static int NUMBER_OF_WEEKS = 14;

    protected CompetenceCourseLoad() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CompetenceCourseLoad(Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
	    Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
	    Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {
	this();
	setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours, trainingPeriodHours,
		tutorialOrientationHours, autonomousWorkHours, ectsCredits, order, academicPeriod);
    }

    public void edit(Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
	    Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
	    Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {

	setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours, trainingPeriodHours,
		tutorialOrientationHours, autonomousWorkHours, ectsCredits, order, academicPeriod);
    }

    private void setInformation(Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
	    Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
	    Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {

	setTheoreticalHours(theoreticalHours);
	setProblemsHours(problemsHours);
	setLaboratorialHours(laboratorialHours);
	setSeminaryHours(seminaryHours);
	setFieldWorkHours(fieldWorkHours);
	setTrainingPeriodHours(trainingPeriodHours);
	setTutorialOrientationHours(tutorialOrientationHours);
	setAutonomousWorkHours(autonomousWorkHours);
	setEctsCredits(ectsCredits);
	setLoadOrder(order);
	setAcademicPeriod(academicPeriod);
    }

    public void delete() {
	removeCompetenceCourseInformation();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public double getTotalLessonHours() {
	return getTheoreticalHours().doubleValue() + getProblemsHours().doubleValue() + getLaboratorialHours().doubleValue()
		+ getSeminaryHours().doubleValue() + getFieldWorkHours().doubleValue() + getTrainingPeriodHours().doubleValue()
		+ getTutorialOrientationHours().doubleValue();
    }

    public Double getContactLoad() {
	return NUMBER_OF_WEEKS * getTotalLessonHours();
    }

    public Double getTotalLoad() {
	return getAutonomousWorkHours() + getContactLoad();
    }

    public int compareTo(Object o) {
	return getOrder().compareTo(((CompetenceCourseLoad) o).getOrder());
    }

    @Deprecated
    public Integer getOrder() {
	return super.getLoadOrder();
    }

    @Deprecated
    public void setOrder(Integer order) {
	super.setLoadOrder(order);
    }

}
