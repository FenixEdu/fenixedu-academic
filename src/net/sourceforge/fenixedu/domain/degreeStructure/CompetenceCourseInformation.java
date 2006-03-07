package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Date;

import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.util.StringFormatter;

public class CompetenceCourseInformation extends CompetenceCourseInformation_Base {

    protected CompetenceCourseInformation() {
        super();
    }

    public CompetenceCourseInformation(String name, String nameEn, String acronym, Boolean basic, RegimeType regimeType, Date endDate) {        
        this();
        setName(StringFormatter.prettyPrint(name));
        setNameEn(StringFormatter.prettyPrint(nameEn));
        setAcronym(acronym);
        setBasic(basic);
        setRegime(regimeType);
        setEndDate(endDate);
        setBibliographicReferences(new BibliographicReferences());
    }
    
    public void edit(String name, String nameEn, String acronym, Boolean basic) {
        setName(StringFormatter.prettyPrint(name));
        setNameEn(StringFormatter.prettyPrint(nameEn));        
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
    
    public BibliographicReference getBibliographicReference(Integer oid) {
        return getBibliographicReferences().getBibliographicReference(oid);
    }
    
    public Double getTheoreticalHours(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getTheoreticalHours();
            }
        }

        return result;
    }
    
    public Double getProblemsHours(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getProblemsHours();
            }
        }

        return result;
    }
    
    public Double getLaboratorialHours(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getLaboratorialHours();
            }
        }

        return result;
    }
    
    public Double getSeminaryHours(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getSeminaryHours();
            }
        }

        return result;
    }
    
    public Double getFieldWorkHours(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getFieldWorkHours();
            }
        }

        return result;
    }
    
    public Double getTrainingPeriodHours(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getTrainingPeriodHours();
            }
        }

        return result;
    }
    
    public Double getTutorialOrientationHours(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getTutorialOrientationHours();
            }
        }

        return result;
    }
    
    public Double getAutonomousWorkHours(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getAutonomousWorkHours();
            }
        }

        return result;
    }
    
    public Double getContactLoad(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getContactLoad();
            }
        }

        return result;
    }
    
    public Double getTotalLoad(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getTotalLoad();
            }
        }
        
        return result;
    }

    public double getEctsCredits(Integer order) {
        double result = 0.0;
        
        for (CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getEctsCredits();
            }
        }

        return result;
    }

}
