package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Date;

public class CompetenceCourseInformation extends CompetenceCourseInformation_Base {

    protected CompetenceCourseInformation() {
        super();
    }

    public CompetenceCourseInformation(String name, String nameEn, String acronym, Boolean basic, RegimeType regimeType, Date endDate) {        
        this();
        setName(name);
        setNameEn(nameEn);
        setAcronym(acronym);
        setBasic(basic);
        setRegime(regimeType);
        setEndDate(endDate);
    }
    
    public void edit(String name, String nameEn, String acronym, Boolean basic) {
        setName(name);
        setNameEn(nameEn);
        setAcronym(acronym);
        setBasic(basic);
    }

    
    public void edit(String objectives, String program, String evaluationMethod, String objectivesEn,
            String programEn, String evaluationMethodEn) {
        setObjectives(objectives);
        setProgram(program);
        setEvaluationMethod(evaluationMethod);
        setObjectivesEn(objectivesEn);
        setProgramEn(programEn);
        setEvaluationMethodEn(evaluationMethodEn);
    }
    
    public void delete() {       
        removeCompetenceCourse();
        for (; !getCompetenceCourseLoads().isEmpty(); getCompetenceCourseLoads().get(0).delete());
        super.deleteDomainObject();
    }
    
    public double getTotalEctsCredits() {
        double result = 0;
        for (final ICompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            result += competenceCourseLoad.getEctsCredits().doubleValue();
        }
        return result;
    }
}
