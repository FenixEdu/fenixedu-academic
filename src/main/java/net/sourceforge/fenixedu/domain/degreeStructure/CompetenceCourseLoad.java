package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class CompetenceCourseLoad extends CompetenceCourseLoad_Base implements Comparable {

    public static int NUMBER_OF_WEEKS = 14;

    protected CompetenceCourseLoad() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public CompetenceCourseLoad(CompetenceCourseLoad existingLoad) {
        this(existingLoad.getTheoreticalHours(), existingLoad.getProblemsHours(), existingLoad.getLaboratorialHours(),
                existingLoad.getSeminaryHours(), existingLoad.getFieldWorkHours(), existingLoad.getTrainingPeriodHours(),
                existingLoad.getTutorialOrientationHours(), existingLoad.getAutonomousWorkHours(), existingLoad.getEctsCredits(),
                existingLoad.getLoadOrder(), existingLoad.getAcademicPeriod());
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

        setTheoreticalHours(theoreticalHours == null ? Double.valueOf(0) : theoreticalHours);
        setProblemsHours(problemsHours == null ? Double.valueOf(0) : problemsHours);
        setLaboratorialHours(laboratorialHours == null ? Double.valueOf(0) : laboratorialHours);
        setSeminaryHours(seminaryHours == null ? Double.valueOf(0) : seminaryHours);
        setFieldWorkHours(fieldWorkHours == null ? Double.valueOf(0) : fieldWorkHours);
        setTrainingPeriodHours(trainingPeriodHours == null ? Double.valueOf(0) : trainingPeriodHours);
        setTutorialOrientationHours(tutorialOrientationHours == null ? Double.valueOf(0) : tutorialOrientationHours);
        setAutonomousWorkHours(autonomousWorkHours == null ? Double.valueOf(0) : autonomousWorkHours);
        setEctsCredits(ectsCredits);
        setLoadOrder(order);
        setAcademicPeriod(academicPeriod);
    }

    public void delete() {
        setCompetenceCourseInformation(null);
        setRootDomainObject(null);
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

    @Override
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

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSeminaryHours() {
        return getSeminaryHours() != null;
    }

    @Deprecated
    public boolean hasEctsCredits() {
        return getEctsCredits() != null;
    }

    @Deprecated
    public boolean hasLoadOrder() {
        return getLoadOrder() != null;
    }

    @Deprecated
    public boolean hasLaboratorialHours() {
        return getLaboratorialHours() != null;
    }

    @Deprecated
    public boolean hasTrainingPeriodHours() {
        return getTrainingPeriodHours() != null;
    }

    @Deprecated
    public boolean hasProblemsHours() {
        return getProblemsHours() != null;
    }

    @Deprecated
    public boolean hasAutonomousWorkHours() {
        return getAutonomousWorkHours() != null;
    }

    @Deprecated
    public boolean hasCompetenceCourseInformation() {
        return getCompetenceCourseInformation() != null;
    }

    @Deprecated
    public boolean hasAcademicPeriod() {
        return getAcademicPeriod() != null;
    }

    @Deprecated
    public boolean hasFieldWorkHours() {
        return getFieldWorkHours() != null;
    }

    @Deprecated
    public boolean hasTheoreticalHours() {
        return getTheoreticalHours() != null;
    }

    @Deprecated
    public boolean hasTutorialOrientationHours() {
        return getTutorialOrientationHours() != null;
    }

}
