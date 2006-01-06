package net.sourceforge.fenixedu.domain.degreeStructure;

public class CompetenceCourseLoad extends CompetenceCourseLoad_Base {
    
    protected CompetenceCourseLoad() {
        super();
    }
    
    public CompetenceCourseLoad(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order) {        
        this();
        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours,
                trainingPeriodHours, tutorialOrientationHours, autonomousWorkHours, ectsCredits, order);
    }
    
    public void edit(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order) {
        
        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours,
                trainingPeriodHours, tutorialOrientationHours, autonomousWorkHours, ectsCredits, order);
    }
    
    private void setInformation(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order) {
        
        setTheoreticalHours(theoreticalHours);
        setProblemsHours(problemsHours);
        setLaboratorialHours(laboratorialHours);
        setSeminaryHours(seminaryHours);
        setFieldWorkHours(fieldWorkHours);
        setTrainingPeriodHours(trainingPeriodHours);
        setTutorialOrientationHours(tutorialOrientationHours);
        setAutonomousWorkHours(autonomousWorkHours);
        setEctsCredits(ectsCredits);
        setOrder(order);
    }
    
    public void delete() {
        removeCompetenceCourseInformation();
        super.deleteDomainObject();
    }
    
    public double getTotalContactLessonHours() {
        return getTheoreticalHours().doubleValue() + getProblemsHours().doubleValue() + getLaboratorialHours().doubleValue() +
            getSeminaryHours().doubleValue() + getFieldWorkHours().doubleValue() + getTrainingPeriodHours().doubleValue() +
            getTutorialOrientationHours().doubleValue();
    }
}
