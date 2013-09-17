package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PHDProgramCandidacy extends PHDProgramCandidacy_Base {

    public PHDProgramCandidacy(Person person, ExecutionDegree executionDegree) {
        super();
        init(person, executionDegree);

    }

    public PHDProgramCandidacy(Person person) {
        super();
        init(person);
    }

    @Override
    public String getDescription() {
        return ResourceBundle.getBundle("resources.PhdResources", Language.getLocale()).getString("label.phd") + " - "
                + getDegreeInformation();
    }

    private String getDegreeInformation() {

        if (hasExecutionDegree()) {
            return getDegreeCurricularPlan().getName() + " - " + getExecutionYear().getYear();

        } else if (hasRegistration()) {
            return getRegistration().getDegreeCurricularPlanName();

        } else if (getCandidacyProcess().hasPhdProgram()) {
            return getCandidacyProcess().getPhdProgram().getName().getContent();

        } else {
            return getCandidacyProcess().getPhdProgramFocusArea().getName().getContent();
        }

    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
        return Collections.emptySet();
    }

    @Override
    protected void moveToNextState(CandidacyOperationType candidacyOperationType, Person person) {
    }

    @Override
    public String getDefaultState() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getStateMapping() {
        return Collections.emptyMap();
    }

    public void copyFromStudentCandidacy(final StudentCandidacy studentCandidacy) {
        setContigent(studentCandidacy.getContigent());
        setEntryGrade(studentCandidacy.getEntryGrade());
        setEntryPhase(studentCandidacy.getEntryPhase());
        setIngression(studentCandidacy.getIngression());
        setApplyForResidence(studentCandidacy.getApplyForResidence());
        setNotesAboutResidenceAppliance(studentCandidacy.getNotesAboutResidenceAppliance());
        setStudentPersonalDataAuthorizationChoice(studentCandidacy.getStudentPersonalDataAuthorizationChoice());
        setDislocatedFromPermanentResidence(studentCandidacy.getDislocatedFromPermanentResidence());
        setPlacingOption(studentCandidacy.getPlacingOption());
        setGrantOwnerType(studentCandidacy.getGrantOwnerType());
        setNumberOfCandidaciesToHigherSchool(studentCandidacy.getNumberOfCandidaciesToHigherSchool());
        setNumberOfFlunksOnHighSchool(studentCandidacy.getNumberOfFlunksOnHighSchool());
        setHighSchoolType(studentCandidacy.getHighSchoolType());
        setMaritalStatus(studentCandidacy.getMaritalStatus());
        setProfessionType(studentCandidacy.getProfessionType());
        setProfessionalCondition(studentCandidacy.getProfessionalCondition());
        setMotherSchoolLevel(studentCandidacy.getMotherSchoolLevel());
        setMotherProfessionType(studentCandidacy.getMotherProfessionType());
        setMotherProfessionalCondition(studentCandidacy.getMotherProfessionalCondition());
        setFatherSchoolLevel(studentCandidacy.getFatherSchoolLevel());
        setFatherProfessionType(studentCandidacy.getFatherProfessionType());
        setFatherProfessionalCondition(studentCandidacy.getFatherProfessionalCondition());
        setSpouseSchoolLevel(studentCandidacy.getSpouseSchoolLevel());
        setSpouseProfessionType(studentCandidacy.getSpouseProfessionType());
        setSpouseProfessionalCondition(studentCandidacy.getSpouseProfessionalCondition());
        setFirstTimeCandidacy(studentCandidacy.getFirstTimeCandidacy());

        getPrecedentDegreeInformation().setConclusionGrade(studentCandidacy.getPrecedentDegreeInformation().getConclusionGrade());
        getPrecedentDegreeInformation().setConclusionYear(studentCandidacy.getPrecedentDegreeInformation().getConclusionYear());
        getPrecedentDegreeInformation().setDegreeDesignation(
                studentCandidacy.getPrecedentDegreeInformation().getDegreeDesignation());
        getPrecedentDegreeInformation().setSchoolLevel(studentCandidacy.getPrecedentDegreeInformation().getSchoolLevel());
        getPrecedentDegreeInformation().setOtherSchoolLevel(
                studentCandidacy.getPrecedentDegreeInformation().getOtherSchoolLevel());
        getPrecedentDegreeInformation().setCountry(studentCandidacy.getPrecedentDegreeInformation().getCountry());
        getPrecedentDegreeInformation().setInstitution(studentCandidacy.getPrecedentDegreeInformation().getInstitution());
        getPrecedentDegreeInformation().setSourceInstitution(
                studentCandidacy.getPrecedentDegreeInformation().getSourceInstitution());

        setExecutionDegree(studentCandidacy.getExecutionDegree());
        setRegistration(studentCandidacy.getRegistration());
        setGrantOwnerProvider(studentCandidacy.getGrantOwnerProvider());
        getCandidacyDocuments().addAll(studentCandidacy.getCandidacyDocuments());
        getCandidacySituations().addAll(studentCandidacy.getCandidacySituations());

        setSchoolTimeDistrictSubDivisionOfResidence(studentCandidacy.getSchoolTimeDistrictSubDivisionOfResidence());
        setCountryOfResidence(studentCandidacy.getCountryOfResidence());
        setDistrictSubdivisionOfResidence(studentCandidacy.getDistrictSubdivisionOfResidence());
    }

    @Deprecated
    public boolean hasCandidacyProcess() {
        return getCandidacyProcess() != null;
    }

}
