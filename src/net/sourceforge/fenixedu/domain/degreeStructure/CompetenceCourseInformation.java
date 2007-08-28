package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.StringFormatter;

public class CompetenceCourseInformation extends CompetenceCourseInformation_Base {

    public static final Comparator<CompetenceCourseInformation> COMPARATORY_BY_EXECUTION_PERIOD = new Comparator<CompetenceCourseInformation>() {
        public int compare(CompetenceCourseInformation o1, CompetenceCourseInformation o2) {
            return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
        }
    };

    protected CompetenceCourseInformation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public CompetenceCourseInformation(String name, String nameEn, Boolean basic,
            RegimeType regimeType, CompetenceCourseLevel competenceCourseLevel, ExecutionPeriod period) {        
        
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
    
    private void checkParameters(String name, String nameEn, Boolean basic,
            RegimeType regimeType, CompetenceCourseLevel competenceCourseLevel) {
        
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
        removeRootDomainObject();
        super.deleteDomainObject();
    }
    
    public BibliographicReference getBibliographicReference(Integer oid) {
        return getBibliographicReferences().getBibliographicReference(oid);
    }
    
    public Double getTheoreticalHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getTheoreticalHours();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getTheoreticalHours();
                break;
            }
        }
        return result;
    }
    
    public Double getProblemsHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getProblemsHours();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getProblemsHours();
                break;
            }
        }
        return result;
    }
    
    public Double getLaboratorialHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getLaboratorialHours();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getLaboratorialHours();
                break;
            }
        }
        return result;
    }
    
    public Double getSeminaryHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getSeminaryHours();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getSeminaryHours();
                break;
            }
        }
        return result;
    }
    
    public Double getFieldWorkHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getFieldWorkHours();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getFieldWorkHours();
                break;
            }
        }
        return result;
    }
    
    public Double getTrainingPeriodHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getTrainingPeriodHours();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getTrainingPeriodHours();
                break;
            }
        }
        return result;
    }
    
    public Double getTutorialOrientationHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getTutorialOrientationHours();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getTutorialOrientationHours();
                break;
            }
        }
        return result;
    }
    
    public Double getAutonomousWorkHours(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getAutonomousWorkHours();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getAutonomousWorkHours();
                break;
            }
        }
        return result;
    }
    
    public Double getContactLoad(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getContactLoad();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getContactLoad();
                break;
            }
        }
        return result;
    }
    
    public Double getTotalLoad(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getTotalLoad();
            } else if (competenceCourseLoad.getOrder().equals(order)) {
                result += competenceCourseLoad.getTotalLoad();
                break;
            }
        }
        return result;
    }

    public double getEctsCredits(Integer order) {
        double result = 0.0;
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourseLoads()) {
            if (order == null || getCompetenceCourseLoadsCount() == 1) {
                result += competenceCourseLoad.getEctsCredits();
            } else if (competenceCourseLoad.getLoadOrder().equals(order)) {
                result += competenceCourseLoad.getEctsCredits();
                break;
            }
        }
        return result;
    }
    
    public List<CompetenceCourseInformationChangeRequest> getCompetenceCourseInformationChangeRequest() {
	List<CompetenceCourseInformationChangeRequest> requests = new ArrayList<CompetenceCourseInformationChangeRequest> ();
	
	for(CompetenceCourseInformationChangeRequest request : this.getCompetenceCourse().getCompetenceCourseInformationChangeRequests()) {
	    if(request.getExecutionPeriod().equals(this.getExecutionPeriod())) {
		requests.add(request);
	    }
	}
	
	return requests;
    }
    
    public boolean isCompetenceCourseInformationChangeRequestDraftAvailable() {
	for(CompetenceCourseInformationChangeRequest request : getCompetenceCourseInformationChangeRequest()) {
	    if (request.getApproved() == null) {
		return true;
	    }
	}
	
	return false;
    }
}
