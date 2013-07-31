package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.util.ConfigurationManager;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.domain.User;

public abstract class PublicPhdProgramCandidacyProcessDA extends PhdProgramCandidacyProcessDA {
    private static final String SIBS_ENTITY_CODE = ConfigurationManager.getProperty("sibs.entityCode");

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("sibsEntityCode", SIBS_ENTITY_CODE);

        request.setAttribute("dont-cache-pages-in-search-engines", Boolean.TRUE);

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        if (bean != null && bean.hasCandidacyHashCode()) {
            canEditCandidacy(request, bean.getCandidacyHashCode());
        }

        ActionForward forward = filterDispatchMethod(bean, mapping, actionForm, request, response);

        if (forward != null) {
            return forward;
        }

        return super.execute(mapping, actionForm, request, response);
    }

    protected abstract ActionForward filterDispatchMethod(final PhdProgramCandidacyProcessBean bean, ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected PhdProgramCandidacyProcessBean getCandidacyBean() {
        return getRenderedObject("candidacyBean");
    }

    protected void canEditCandidacy(final HttpServletRequest request, final PhdProgramPublicCandidacyHashCode hashCode) {
        request.setAttribute("canEditCandidacy", !isValidatedByCandidate(hashCode));
    }

    protected boolean isValidatedByCandidate(final PhdProgramPublicCandidacyHashCode hashCode) {
        return hashCode.hasPhdProgramCandidacyProcess() && hashCode.getIndividualProgramProcess().isValidatedByCandidate();
    }

    protected void canEditPersonalInformation(final HttpServletRequest request, final Person person) {
        if (person.hasRole(RoleType.EMPLOYEE)) {
            request.setAttribute("canEditPersonalInformation", false);
            addWarningMessage(request, "message.employee.data.must.be.updated.in.human.resources.section");
        } else if (person.hasAnyPersonRoles() || person.hasUser() || person.hasStudent()) {
            request.setAttribute("canEditPersonalInformation", false);
            addWarningMessage(request, "message.existing.person.data.must.be.updated.in.academic.office");
        } else {
            request.setAttribute("canEditPersonalInformation", true);
        }
    }

    protected void addValidationMessage(final HttpServletRequest request, final String key, final String... args) {
        addActionMessage("validation", request, key, args);
    }

    protected User createMockUserView(final Person person) {
        return person.getBennuUser();
    }

    public abstract ActionForward fillPersonalDataInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response);

    protected ActionForward checkPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();

        final PersonBean personBean = bean.getPersonBean();

        // First case : get persons by document id value (not type)
        final Collection<Person> personsFoundByDocumentId = Person.readByDocumentIdNumber(personBean.getDocumentIdNumber());

        if (personsFoundByDocumentId.size() > 1) {
            // There's more than one person, throw an error
            addErrorMessage(request, "error.phd.public.candidacy.fill.personal.information.and.institution.id.more.than.one");
            return fillPersonalDataInvalid(mapping, form, request, response);
        }

        final Person person = !personsFoundByDocumentId.isEmpty() ? personsFoundByDocumentId.iterator().next() : null;

        if (!StringUtils.isEmpty(personBean.getFiscalCode())) {
            final Party partyFoundBySocialSecurityNumber =
                    PartySocialSecurityNumber.readPartyBySocialSecurityNumber(personBean.getFiscalCode());

            // Second case : person found by documentId and person found by
            // social
            // security number must be equal
            if (person != null || partyFoundBySocialSecurityNumber != null) {
                if (person != partyFoundBySocialSecurityNumber) {
                    addErrorMessage(request,
                            "error.phd.public.candidacy.fill.personal.information.and.institution.id.different.ssn");
                    return fillPersonalDataInvalid(mapping, form, request, response);
                }
            }
        }

        if (bean.hasInstitutionId()) {
            Person personByIstId = Person.readPersonByIstUsername(bean.getInstitutionId());

            if (personByIstId == null) {
                addErrorMessage(request, "error.phd.public.candidacy.fill.personal.information.and.institution.id.noIstIdPerson");
                return fillPersonalDataInvalid(mapping, form, request, response);
            }

            if (person != null && person != personByIstId) {
                addErrorMessage(request,
                        "error.phd.public.candidacy.fill.personal.information.and.institution.id.different.istId");
                return fillPersonalDataInvalid(mapping, form, request, response);
            }
        }

        // check if person already exists
        if (person != null) {
            // Exists
            // Third case person exists so the birth date must be equal and also
            // ist Id if it has

            if (person.getDateOfBirthYearMonthDay().equals(personBean.getDateOfBirth())) {
                if (person.hasIstUsername() && person.getIstUsername().equals(bean.getInstitutionId())) {
                    personBean.setPerson(person);
                } else if (!person.hasIstUsername() && !bean.hasInstitutionId()) {
                    personBean.setPerson(person);
                } else {
                    addErrorMessage(request,
                            "error.phd.public.candidacy.fill.personal.information.and.institution.id.different.istIds");
                    return fillPersonalDataInvalid(mapping, form, request, response);
                }
            } else {
                addErrorMessage(request,
                        "error.phd.public.candidacy.fill.personal.information.and.institution.id.different.birthday");
                return fillPersonalDataInvalid(mapping, form, request, response);
            }

            // Check if person has an application for this period
            if (PhdProgramCandidacyProcess.hasOnlineApplicationForPeriod(person, bean.getPhdCandidacyPeriod())) {
                addErrorMessage(request,
                        "error.phd.public.candidacy.fill.personal.information.and.institution.alreadyHasApplication");
                return fillPersonalDataInvalid(mapping, form, request, response);
            }
        }

        return null;
    }

}
