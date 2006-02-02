package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Date;

import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;

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
        setBibliographicReferences(new BibliographicReferences());
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
    
    public double getEctsCredits() {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            result += competenceCourseLoad.getEctsCredits().doubleValue();
        }
        return result;
    }
    
    public BibliographicReference getBibliographicReference(Integer oid) {
        return getBibliographicReferences().getBibliographicReference(oid);
    }
    
    public Double getContactLoad(Integer order) {
        double result = 0.0;
        
        if (this.getRegime().equals(RegimeType.ANUAL) && this.getCompetenceCourseLoadsCount() > 1) {
            return (this.getCompetenceCourseLoads(order) != null) ? this.getCompetenceCourseLoads(order).getContactLoad() : result;
        } 
        if (this.getCompetenceCourseLoadsCount() == 1) {
            return (this.getCompetenceCourseLoads(1) != null) ? this.getCompetenceCourseLoads(1).getContactLoad() : result;
        }

        return result;
    }
    
    private CompetenceCourseLoad getCompetenceCourseLoads(Integer order) {
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (competenceCourseLoad.getOrder().equals(order)) {
                return competenceCourseLoad;
            }
        }
        return null;
    }

    public Double getAutonomousWorkHours(Integer order) {
        double result = 0.0;

        if (this.getRegime().equals(RegimeType.ANUAL) && this.getCompetenceCourseLoadsCount() > 1) {
            return (this.getCompetenceCourseLoads(order) != null) ? this.getCompetenceCourseLoads(order).getAutonomousWorkHours() : result;
        } 
        if (this.getCompetenceCourseLoadsCount() == 1) {
            return (this.getCompetenceCourseLoads(1) != null) ? this.getCompetenceCourseLoads(1).getAutonomousWorkHours() : result;
        }
        
        return result;
    }
    
    public Double getTotalLoad(Integer order) {
        double result = 0.0;

        if (this.getRegime().equals(RegimeType.ANUAL) && this.getCompetenceCourseLoadsCount() > 1) {
            return (this.getCompetenceCourseLoads(order) != null) ? this.getCompetenceCourseLoads(order).getTotalLoad() : result;
        } 
        if (this.getCompetenceCourseLoadsCount() == 1) {
            return (this.getCompetenceCourseLoads(1) != null) ? this.getCompetenceCourseLoads(1).getTotalLoad() : result;
        }
        
        return result;
    }

}
