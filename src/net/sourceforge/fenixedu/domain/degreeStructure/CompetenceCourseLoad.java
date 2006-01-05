package net.sourceforge.fenixedu.domain.degreeStructure;

public class CompetenceCourseLoad extends CompetenceCourseLoad_Base {
    
    protected CompetenceCourseLoad() {
        super();
    }
    
    public CompetenceCourseLoad(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits) {        
        this();
        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours,
                trainingPeriodHours, tutorialOrientationHours, autonomousWorkHours, ectsCredits);
    }
    
    public void edit(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits) {
        
        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours,
                trainingPeriodHours, tutorialOrientationHours, autonomousWorkHours, ectsCredits);
    }
    
    private void setInformation(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits) {
        
        setTheoreticalHours(theoreticalHours);
        setProblemsHours(problemsHours);
        setLaboratorialHours(laboratorialHours);
        setSeminaryHours(seminaryHours);
        setFieldWorkHours(fieldWorkHours);
        setTrainingPeriodHours(trainingPeriodHours);
        setTutorialOrientationHours(tutorialOrientationHours);
        setAutonomousWorkHours(autonomousWorkHours);
        setEctsCredits(ectsCredits);
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
