package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;

public class CompetenceCourseLoad extends CompetenceCourseLoad_Base {
    
    protected CompetenceCourseLoad() {
        super();
    }
    
    public CompetenceCourseLoad(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order, CurricularPeriodType curricularPeriodType) {        
        this();
        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours,
                trainingPeriodHours, tutorialOrientationHours, autonomousWorkHours, ectsCredits, order, curricularPeriodType);
    }
    
    public void edit(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order, CurricularPeriodType curricularPeriodType) {
        
        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours,
                trainingPeriodHours, tutorialOrientationHours, autonomousWorkHours, ectsCredits, order, curricularPeriodType);
    }
    
    private void setInformation(Double theoreticalHours, Double problemsHours, Double laboratorialHours,
            Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order, CurricularPeriodType curricularPeriodType) {
        
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
        setCurricularPeriodType(curricularPeriodType);
    }
    
    public void delete() {
        removeCompetenceCourseInformation();
        super.deleteDomainObject();
    }
    
    public double getTotalLessonHours() {
        return getTheoreticalHours().doubleValue() + getProblemsHours().doubleValue() + getLaboratorialHours().doubleValue() +
            getSeminaryHours().doubleValue() + getFieldWorkHours().doubleValue() + getTrainingPeriodHours().doubleValue() +
            getTutorialOrientationHours().doubleValue();
    }
    
    public double getContactLoad() {
        return 14 * getTotalLessonHours();
    }
    
    public double getTotalLoad() {
        return getAutonomousWorkHours() + getContactLoad();
    }
    
}
