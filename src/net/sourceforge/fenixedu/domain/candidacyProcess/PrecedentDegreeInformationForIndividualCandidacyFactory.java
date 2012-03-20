package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusStudentDataBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

public class PrecedentDegreeInformationForIndividualCandidacyFactory {

    /* CREATE */

    public static PrecedentDegreeInformation create(final IndividualCandidacy individualCandidacy,
	    final IndividualCandidacyProcessBean processBean) {

	if (processBean.isStandalone()) {
	    return null;
	}

	PrecedentDegreeInformation pid = null;
	if (processBean.isDegreeChange() || processBean.isDegreeTransfer()) {
	    pid = createForDegreeTransferOrDegreeChange(processBean);
	} else if (processBean.isSecondCycle() || processBean.isDegreeCandidacyForGraduatedPerson()) {
	    pid = createForSecondCycleOrDegreeCandidacyForGraduatedPerson(processBean);
	} else if (processBean.isOver23()) {
	    pid = createForOver23(processBean);
	} else if (processBean.isErasmus()) {
	    pid = createForErasmus(processBean);
	}

	pid.setIndividualCandidacy(individualCandidacy);

	return pid;
    }

    private static PrecedentDegreeInformation createForErasmus(final IndividualCandidacyProcessBean processBean) {
	ErasmusIndividualCandidacyProcessBean erasmusBean = (ErasmusIndividualCandidacyProcessBean) processBean;
	ErasmusStudentDataBean erasmusStudentDataBean = erasmusBean.getErasmusStudentDataBean();

	PrecedentDegreeInformation pid = new PrecedentDegreeInformation();
	pid.setCandidacyInternal(false);

	pid.setPrecedentCountry(erasmusStudentDataBean.getSelectedCountry());
	pid.setPrecedentInstitution(erasmusStudentDataBean.getSelectedUniversity());

	if (erasmusStudentDataBean.getHasDiplomaOrDegree()) {
	    pid.setDegreeDesignation(erasmusStudentDataBean.getDiplomaName());
	    pid.setConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
	}

	return pid;
    }

    private static PrecedentDegreeInformation createForOver23(IndividualCandidacyProcessBean processBean) {
	Over23IndividualCandidacyProcessBean over23Bean = (Over23IndividualCandidacyProcessBean) processBean;

	if (!over23Bean.getFormationConcludedBeanList().isEmpty()) {
	    PrecedentDegreeInformation pid = new PrecedentDegreeInformation();
	    pid.setCandidacyInternal(false);
	    FormationBean formationBean = over23Bean.getFormationConcludedBeanList().get(0);
	    
	    pid.setDegreeDesignation(formationBean.getDesignation());
	    pid.setInstitution(getOrCreateInstitution(formationBean.getInstitutionName()));
	    pid.setConclusionYear(formationBean.getConclusionExecutionYear().getEndCivilYear());
	    
	    return pid;
	} else if (!over23Bean.getFormationNonConcludedBeanList().isEmpty()) {
	    PrecedentDegreeInformation pid = new PrecedentDegreeInformation();
	    pid.setCandidacyInternal(false);
	    FormationBean formationBean = over23Bean.getFormationNonConcludedBeanList().get(0);

	    pid.setDegreeDesignation(formationBean.getDesignation());
	    pid.setInstitution(getOrCreateInstitution(formationBean.getInstitutionName()));

	    return pid;
	}

	return null;
    }

    private static PrecedentDegreeInformation createForDegreeTransferOrDegreeChange(IndividualCandidacyProcessBean processBean) {
	IndividualCandidacyProcessWithPrecedentDegreeInformationBean candidacyProcessWithPrecedentDegreeInformationBean = (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) processBean;
	PrecedentDegreeInformationBean bean = candidacyProcessWithPrecedentDegreeInformationBean.getPrecedentDegreeInformation();

	PrecedentDegreeInformation pid = new PrecedentDegreeInformation();
	pid.setPrecedentDegreeDesignation(bean.getDegreeDesignation());
	pid.setPrecedentInstitution(getOrCreateInstitution(bean));
	pid.setNumberOfEnroledCurricularCourses(bean.getNumberOfEnroledCurricularCourses());
	pid.setNumberOfApprovedCurricularCourses(bean.getNumberOfApprovedCurricularCourses());
	pid.setGradeSum(bean.getGradeSum());
	pid.setApprovedEcts(bean.getApprovedEcts());
	pid.setEnroledEcts(bean.getEnroledEcts());

	if (candidacyProcessWithPrecedentDegreeInformationBean.isExternalPrecedentDegreeType()) {
	    pid.setCandidacyInternal(false);
	} else {
	    pid.setCandidacyInternal(true);
	    final StudentCurricularPlan studentCurricularPlan = candidacyProcessWithPrecedentDegreeInformationBean
		    .getPrecedentStudentCurricularPlan();

	    if (studentCurricularPlan == null) {
		throw new DomainException("error.IndividualCandidacy.invalid.precedentDegreeInformation");
	    }

	    pid.setStudentCurricularPlan(studentCurricularPlan);
	}

	return pid;
    }

    private static PrecedentDegreeInformation createForSecondCycleOrDegreeCandidacyForGraduatedPerson(
	    IndividualCandidacyProcessBean processBean) {
	IndividualCandidacyProcessWithPrecedentDegreeInformationBean candidacyProcessWithPrecedentDegreeInformationBean = (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) processBean;
	
	PrecedentDegreeInformationBean bean = candidacyProcessWithPrecedentDegreeInformationBean.getPrecedentDegreeInformation();
	
	PrecedentDegreeInformation pid = new PrecedentDegreeInformation();
	pid.setCandidacyInternal(false);
	pid.setDegreeDesignation(bean.getDegreeDesignation());
	pid.setInstitution(getOrCreateInstitution(bean));
	pid.setCountry(bean.getCountry());
	pid.setConclusionDate(bean.getConclusionDate());

	if (bean.getConclusionDate() != null) {
	    pid.setConclusionYear(bean.getConclusionDate().getYear());
	}

	pid.setConclusionGrade(bean.getConclusionGrade());
	
	return pid;
    }

    private static Unit getOrCreateInstitution(final PrecedentDegreeInformationBean bean) {
	return getOrCreateInstitution(bean.getInstitutionName());
    }

    private static Unit getOrCreateInstitution(final String institutionName) {
	if (institutionName == null || institutionName.isEmpty()) {
	    throw new DomainException("error.ExternalPrecedentDegreeCandidacy.invalid.institution.name");
	}

	final Unit unit = Unit.findFirstExternalUnitByName(institutionName);
	return (unit != null) ? unit : Unit.createNewNoOfficialExternalInstitution(institutionName);
    }

    /* EDIT */

    public static final void edit(final IndividualCandidacyProcessBean processBean) {
	if (processBean.isDegreeChange() || processBean.isDegreeTransfer()) {
	    editForDegreeTransferOrDegreeChange(processBean);
	} else if (processBean.isSecondCycle() || processBean.isDegreeCandidacyForGraduatedPerson()) {
	    editForSecondCycleOrDegreeCandidacyForGraduatedPerson(processBean);
	} else if (processBean.isOver23()) {
	    editForOver23(processBean);
	} else if (processBean.isErasmus()) {
	    editForErasmus(processBean);
	}
    }

    private static void editForErasmus(IndividualCandidacyProcessBean processBean) {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) processBean;
	ErasmusStudentDataBean erasmusStudentDataBean = bean.getErasmusStudentDataBean();

	ErasmusIndividualCandidacyProcess erasmusIndividualCandidacyProcess = (ErasmusIndividualCandidacyProcess) processBean
		.getIndividualCandidacyProcess();
	ErasmusIndividualCandidacy erasmusCandidacy = erasmusIndividualCandidacyProcess.getCandidacy();

	PrecedentDegreeInformation pid = erasmusCandidacy.getRefactoredPrecedentDegreeInformation();
	
	pid.setPrecedentCountry(erasmusStudentDataBean.getSelectedCountry());
	pid.setPrecedentInstitution(erasmusStudentDataBean.getSelectedUniversity());

	if (erasmusStudentDataBean.getHasDiplomaOrDegree()) {
	    pid.setDegreeDesignation(erasmusStudentDataBean.getDiplomaName());
	    pid.setConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
	}
    }

    private static void editForOver23(IndividualCandidacyProcessBean processBean) {
	Over23IndividualCandidacyProcessBean over23Bean = (Over23IndividualCandidacyProcessBean) processBean;

	IndividualCandidacyProcess individualCandidacyProcess = processBean.getIndividualCandidacyProcess();
	IndividualCandidacy candidacy = individualCandidacyProcess.getCandidacy();
	PrecedentDegreeInformation pid = candidacy.getRefactoredPrecedentDegreeInformation();

	if (!over23Bean.getFormationConcludedBeanList().isEmpty()) {
	    FormationBean formationBean = over23Bean.getFormationConcludedBeanList().get(0);

	    pid.setDegreeDesignation(formationBean.getDesignation());
	    pid.setInstitution(getOrCreateInstitution(formationBean.getInstitutionName()));
	    pid.setConclusionYear(formationBean.getConclusionExecutionYear().getEndCivilYear());
	} else if (over23Bean.getFormationNonConcludedBeanList().isEmpty()) {
	    FormationBean formationBean = over23Bean.getFormationNonConcludedBeanList().get(0);

	    pid.setDegreeDesignation(formationBean.getDesignation());
	    pid.setInstitution(getOrCreateInstitution(formationBean.getInstitutionName()));
	}

    }

    private static void editForSecondCycleOrDegreeCandidacyForGraduatedPerson(IndividualCandidacyProcessBean processBean) {
	IndividualCandidacyProcessWithPrecedentDegreeInformationBean candidacyProcessWithPrecedentDegreeInformationBean = (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) processBean;
	IndividualCandidacyProcess individualCandidacyProcess = processBean.getIndividualCandidacyProcess();
	IndividualCandidacy candidacy = individualCandidacyProcess.getCandidacy();
	PrecedentDegreeInformation pid = candidacy.getRefactoredPrecedentDegreeInformation();

	PrecedentDegreeInformationBean bean = candidacyProcessWithPrecedentDegreeInformationBean.getPrecedentDegreeInformation();

	pid.setDegreeDesignation(bean.getDegreeDesignation());
	pid.setInstitution(getOrCreateInstitution(bean));
	pid.setCountry(bean.getCountry());
	pid.setConclusionDate(bean.getConclusionDate());

	if (bean.getConclusionDate() != null) {
	    pid.setConclusionYear(bean.getConclusionDate().getYear());
	}

	pid.setConclusionGrade(bean.getConclusionGrade());

    }

    private static void editForDegreeTransferOrDegreeChange(IndividualCandidacyProcessBean processBean) {
	IndividualCandidacyProcessWithPrecedentDegreeInformationBean candidacyProcessWithPrecedentDegreeInformationBean = (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) processBean;
	PrecedentDegreeInformationBean bean = candidacyProcessWithPrecedentDegreeInformationBean.getPrecedentDegreeInformation();

	IndividualCandidacyProcess individualCandidacyProcess = processBean.getIndividualCandidacyProcess();
	IndividualCandidacy candidacy = individualCandidacyProcess.getCandidacy();
	PrecedentDegreeInformation pid = candidacy.getRefactoredPrecedentDegreeInformation();

	pid.setPrecedentDegreeDesignation(bean.getDegreeDesignation());
	pid.setPrecedentInstitution(getOrCreateInstitution(bean));
	pid.setNumberOfEnroledCurricularCourses(bean.getNumberOfEnroledCurricularCourses());
	pid.setNumberOfApprovedCurricularCourses(bean.getNumberOfApprovedCurricularCourses());
	pid.setGradeSum(bean.getGradeSum());
	pid.setApprovedEcts(bean.getApprovedEcts());
	pid.setEnroledEcts(bean.getEnroledEcts());
    }

}
