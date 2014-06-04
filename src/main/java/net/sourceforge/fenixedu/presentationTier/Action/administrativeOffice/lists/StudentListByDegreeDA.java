/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.academicAdministration.SearchStudentsByDegreeParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationWithStateForExecutionYearBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.BranchCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminListingsApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Angela Almeida (argelina@ist.utl.pt)
 */
@StrutsFunctionality(app = AcademicAdminListingsApp.class, path = "students-by-degree", titleKey = "link.studentsListByDegree",
        accessGroup = "academic(STUDENT_LISTINGS)")
@Mapping(path = "/studentsListByDegree", module = "academicAdministration")
@Forwards(@Forward(name = "searchRegistrations", path = "/academicAdminOffice/lists/searchRegistrationsByDegree.jsp"))
public class StudentListByDegreeDA extends FenixDispatchAction {

    private static final String YMD_FORMAT = "yyyy-MM-dd";

    @EntryPoint
    public ActionForward prepareByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("searchParametersBean", getOrCreateSearchParametersBean());
        return mapping.findForward("searchRegistrations");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final SearchStudentsByDegreeParametersBean searchParametersBean = getOrCreateSearchParametersBean();
        RenderUtils.invalidateViewState();
        request.setAttribute("searchParametersBean", searchParametersBean);

        return mapping.findForward("searchRegistrations");
    }

    private SearchStudentsByDegreeParametersBean getOrCreateSearchParametersBean() {
        SearchStudentsByDegreeParametersBean bean = getRenderedObject("searchParametersBean");
        if (bean == null) {
            Set<DegreeType> degreeTypesForOperation =
                    AcademicAuthorizationGroup.getDegreeTypesForOperation(AccessControl.getPerson(),
                            AcademicOperationType.STUDENT_LISTINGS);
            bean =
                    new SearchStudentsByDegreeParametersBean(degreeTypesForOperation,
                            AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(),
                                    AcademicOperationType.STUDENT_LISTINGS));
        }
        return bean;
    }

    public ActionForward searchByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final SearchStudentsByDegreeParametersBean searchBean = getOrCreateSearchParametersBean();

        final List<RegistrationWithStateForExecutionYearBean> registrations = search(searchBean);

        request.setAttribute("searchParametersBean", searchBean);
        request.setAttribute("studentCurricularPlanList", registrations);

        return mapping.findForward("searchRegistrations");
    }

    private static List<RegistrationWithStateForExecutionYearBean> search(final SearchStudentsByDegreeParametersBean searchbean) {

        final Set<Registration> registrations = new TreeSet<Registration>(Registration.COMPARATOR_BY_NUMBER_AND_ID);

        final Degree chosenDegree = searchbean.getDegree();
        final DegreeType chosenDegreeType = searchbean.getDegreeType();
        final ExecutionYear executionYear = searchbean.getExecutionYear();
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            if (chosenDegreeType != null && degreeCurricularPlan.getDegreeType() != chosenDegreeType) {
                continue;
            }
            if (chosenDegree != null && degreeCurricularPlan.getDegree() != chosenDegree) {
                continue;
            }
            if (degreeCurricularPlan.getDegreeType() != DegreeType.EMPTY) {
                if (!searchbean.getAdministratedDegreeTypes().contains(degreeCurricularPlan.getDegreeType())) {
                    continue;
                }
                if (!searchbean.getAdministratedDegrees().contains(degreeCurricularPlan.getDegree())) {
                    continue;
                }
            }
            degreeCurricularPlan.getRegistrations(executionYear, registrations);
        }
        DegreeCurricularPlan emptyDegreeCurricularPlan = DegreeCurricularPlan.readEmptyDegreeCurricularPlan();
        if (chosenDegreeType == null || emptyDegreeCurricularPlan.getDegreeType() == chosenDegreeType) {
            emptyDegreeCurricularPlan.getRegistrations(executionYear, registrations);
        }
        return filterResults(searchbean, registrations, executionYear);
    }

    private static List<RegistrationWithStateForExecutionYearBean> filterResults(SearchStudentsByDegreeParametersBean searchBean,
            final Set<Registration> registrations, final ExecutionYear executionYear) {
        final List<RegistrationWithStateForExecutionYearBean> result = new ArrayList<RegistrationWithStateForExecutionYearBean>();
        for (final Registration registration : registrations) {
            if (searchBean.hasAnyRegistrationAgreements()
                    && !searchBean.getRegistrationAgreements().contains(registration.getRegistrationAgreement())) {
                continue;
            }

            if (searchBean.hasAnyStudentStatuteType() && !hasStudentStatuteType(searchBean, registration)) {
                continue;
            }

            final RegistrationState lastRegistrationState = registration.getLastRegistrationState(executionYear);
            if (lastRegistrationState == null) {
                continue;
            }
            if (searchBean.hasAnyRegistrationStateTypes()
                    && !searchBean.getRegistrationStateTypes().contains(lastRegistrationState.getStateType())) {
                continue;
            }

            if ((searchBean.isIngressedInChosenYear()) && (registration.getIngressionYear() != executionYear)) {
                continue;
            }

            if (searchBean.isConcludedInChosenYear()) {
                CycleType cycleType;
                if (searchBean.getDegreeType() != null) {
                    final TreeSet<CycleType> orderedCycleTypes = searchBean.getDegreeType().getOrderedCycleTypes();
                    cycleType = orderedCycleTypes.isEmpty() ? null : orderedCycleTypes.last();
                } else {
                    cycleType = registration.getCycleType(executionYear);
                }

                RegistrationConclusionBean conclusionBean;
                if (registration.isBolonha()) {
                    conclusionBean = new RegistrationConclusionBean(registration, cycleType);
                    if (conclusionBean.getCycleCurriculumGroup() == null || !conclusionBean.isConcluded()) {
                        continue;
                    }
                } else {
                    conclusionBean = new RegistrationConclusionBean(registration);
                    if (!conclusionBean.isConclusionProcessed()) {
                        continue;
                    }
                }

                if (conclusionBean.getConclusionYear() != executionYear) {
                    continue;
                }
            }

            if (searchBean.getActiveEnrolments() && !registration.hasAnyEnrolmentsIn(executionYear)) {
                continue;
            }

            if (searchBean.getStandaloneEnrolments() && !registration.hasAnyStandaloneEnrolmentsIn(executionYear)) {
                continue;
            }

            if ((searchBean.getRegime() != null) && (registration.getRegimeType(executionYear) != searchBean.getRegime())) {
                continue;
            }

            if ((searchBean.getNationality() != null) && (registration.getPerson().getCountry() != searchBean.getNationality())) {
                continue;
            }

            if ((searchBean.getIngression() != null) && (registration.getIngression() != searchBean.getIngression())) {
                continue;
            }

            result.add(new RegistrationWithStateForExecutionYearBean(registration, lastRegistrationState.getStateType(),
                    executionYear));
        }
        return result;
    }

    static private boolean hasStudentStatuteType(final SearchStudentsByDegreeParametersBean searchBean,
            final Registration registration) {
        return CollectionUtils.containsAny(searchBean.getStudentStatuteTypes(), registration.getStudent()
                .getStatutesTypesValidOnAnyExecutionSemesterFor(searchBean.getExecutionYear()));
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final SearchStudentsByDegreeParametersBean searchBean = getOrCreateSearchParametersBean();
        if (searchBean == null) {
            return null;
        }
        final List<RegistrationWithStateForExecutionYearBean> registrations = search(searchBean);

        try {
            String filename = getResourceMessage("label.students");

            Degree degree = searchBean.getDegree();
            DegreeType degreeType = searchBean.getDegreeType();
            ExecutionYear executionYear = searchBean.getExecutionYear();
            if (degree != null) {
                filename += "_" + degree.getNameFor(executionYear).getContent().replace(' ', '_');
            } else if (degreeType != null) {
                filename += "_" + degreeType.getLocalizedName().replace(' ', '_');
            }
            filename += "_" + executionYear.getYear();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
            ServletOutputStream writer = response.getOutputStream();

            final String param = request.getParameter("extendedInfo");
            boolean extendedInfo = param != null && param.length() > 0 && Boolean.valueOf(param).booleanValue();

            exportToXls(registrations, writer, searchBean, extendedInfo);
            writer.flush();
            response.flushBuffer();
            return null;

        } catch (IOException e) {
            throw new FenixServiceException();
        }
    }

    private void exportToXls(List<RegistrationWithStateForExecutionYearBean> registrationList, OutputStream outputStream,
            SearchStudentsByDegreeParametersBean searchBean, boolean extendedInfo) throws IOException {

        final StyledExcelSpreadsheet spreadsheet =
                new StyledExcelSpreadsheet(getResourceMessage("lists.studentByDegree.unspaced"));
        fillSpreadSheetFilters(searchBean, spreadsheet);
        fillSpreadSheetResults(registrationList, spreadsheet, searchBean.getExecutionYear(), extendedInfo);
        spreadsheet.getWorkbook().write(outputStream);
    }

    private void fillSpreadSheetFilters(SearchStudentsByDegreeParametersBean searchBean, final StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.newHeaderRow();
        if (searchBean.isIngressedInChosenYear()) {
            spreadsheet.addHeader(getResourceMessage("label.ingressedInChosenYear"));
        }
        spreadsheet.newHeaderRow();
        if (searchBean.isConcludedInChosenYear()) {
            spreadsheet.addHeader(getResourceMessage("label.concludedInChosenYear"));
        }
        spreadsheet.newHeaderRow();
        if (searchBean.getActiveEnrolments()) {
            spreadsheet.addHeader(getResourceMessage("label.activeEnrolments.capitalized"));
        }
        spreadsheet.newHeaderRow();
        if (searchBean.getStandaloneEnrolments()) {
            spreadsheet.addHeader(getResourceMessage("label.withStandaloneEnrolments"));
        }
        spreadsheet.newHeaderRow();
        if (searchBean.getRegime() != null) {
            spreadsheet.addHeader(getResourceMessage("registration.regime") + ": " + searchBean.getRegime().getLocalizedName());
        }
        spreadsheet.newHeaderRow();
        if (searchBean.getNationality() != null) {
            spreadsheet.addHeader(getResourceMessage("label.nationality") + ": " + searchBean.getNationality().getName());
        }
        spreadsheet.newHeaderRow();
        if (searchBean.getIngression() != null) {
            spreadsheet.addHeader(getResourceMessage("label.ingression.short") + ": "
                    + searchBean.getIngression().getLocalizedName());
        }

        spreadsheet.newHeaderRow();
        if (searchBean.hasAnyRegistrationAgreements()) {
            spreadsheet.addHeader(getResourceMessage("label.registrationAgreement") + ":");
            for (RegistrationAgreement agreement : searchBean.getRegistrationAgreements()) {
                spreadsheet.addHeader(agreement.getDescription());
            }
        }
        spreadsheet.newHeaderRow();
        if (searchBean.hasAnyRegistrationStateTypes()) {
            spreadsheet.addHeader(getResourceMessage("label.registrationState") + ":");
            for (RegistrationStateType state : searchBean.getRegistrationStateTypes()) {
                spreadsheet.addHeader(state.getDescription());
            }
        }
        spreadsheet.newHeaderRow();
        if (searchBean.hasAnyStudentStatuteType()) {
            spreadsheet.addHeader(getResourceMessage("label.statutes") + ":");
            for (StudentStatuteType statute : searchBean.getStudentStatuteTypes()) {
                spreadsheet.addHeader(BundleUtil.getString(Bundle.ENUMERATION, statute.name()));
            }
        }
    }

    private void fillSpreadSheetResults(List<RegistrationWithStateForExecutionYearBean> registrations,
            final StyledExcelSpreadsheet spreadsheet, ExecutionYear executionYear, boolean extendedInfo) {
        spreadsheet.newRow();
        spreadsheet.newRow();
        spreadsheet.addCell(registrations.size() + " " + getResourceMessage("label.students"));

        setHeaders(spreadsheet, extendedInfo);
        for (RegistrationWithStateForExecutionYearBean registrationWithStateForExecutionYearBean : registrations) {

            final Registration registration = registrationWithStateForExecutionYearBean.getRegistration();
            spreadsheet.newRow();

            final Degree degree = registration.getDegree();
            spreadsheet.addCell(!(StringUtils.isEmpty(degree.getSigla())) ? degree.getSigla() : degree.getNameFor(executionYear)
                    .toString());
            spreadsheet.addCell(degree.getFilteredName(executionYear));
            spreadsheet.addCell(registration.getNumber().toString());

            final Person person = registration.getPerson();
            spreadsheet.addCell(person.getName());
            spreadsheet.addCell(person.getDocumentIdNumber());

            final RegistrationState lastRegistrationState = registration.getLastRegistrationState(executionYear);
            spreadsheet.addCell(lastRegistrationState.getStateType().getDescription());
            spreadsheet.addCell(lastRegistrationState.getStateDate().toString("yyyy-MM-dd"));
            spreadsheet.addCell(registration.getRegistrationAgreement().getName());

            if (extendedInfo) {
                spreadsheet.addCell(getAlmaMater(person, registration));
                spreadsheet.addCell(getAlmaMaterCountry(person, registration));
                spreadsheet.addCell(person.getDefaultEmailAddress() == null ? EMPTY : person.getDefaultEmailAddress().getValue());
                spreadsheet.addCell(person.getCountry() == null ? EMPTY : person.getCountry().getName());
                spreadsheet.addCell(getFullAddress(person));
                spreadsheet.addCell(person.hasDefaultMobilePhone() ? person.getDefaultMobilePhoneNumber() : EMPTY);
                spreadsheet.addCell(person.getGender().toLocalizedString());
                spreadsheet.addCell(person.getDateOfBirthYearMonthDay() == null ? EMPTY : person.getDateOfBirthYearMonthDay()
                        .toString(YMD_FORMAT));
                spreadsheet.addCell(registration.getEnrolmentsExecutionYears().size());
                spreadsheet.addCell(registration.getCurricularYear(executionYear));
                spreadsheet.addCell(registration.getEnrolments(executionYear).size());
                spreadsheet.addCell(registration.getRegimeType(executionYear).getLocalizedName());

                fillSpreadSheetPreBolonhaInfo(spreadsheet, registration);

                spreadsheet
                        .addCell(getResourceMessage(registration.getStudent().isSenior(executionYear) ? "label.yes" : "label.no"));

                final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();

                if (getAdministratedCycleTypes().contains(CycleType.FIRST_CYCLE)) {
                    fillSpreadSheetBolonhaInfo(spreadsheet, registration, studentCurricularPlan.getCycle(CycleType.FIRST_CYCLE));
                }
                if (getAdministratedCycleTypes().contains(CycleType.SECOND_CYCLE)) {
                    fillSpreadSheetBolonhaInfo(spreadsheet, registration, studentCurricularPlan.getCycle(CycleType.SECOND_CYCLE));
                }
                if (getAdministratedCycleTypes().contains(CycleType.THIRD_CYCLE)) {
                    fillSpreadSheetBolonhaInfo(spreadsheet, registration, studentCurricularPlan.getCycle(CycleType.THIRD_CYCLE));
                }

                spreadsheet.addCell(registrationWithStateForExecutionYearBean.getPersonalDataAuthorization());

                addBranchsInformation(spreadsheet, studentCurricularPlan);

                final Collection<Enrolment> enrolmentsSem1 = registration.getEnrolments(executionYear.getExecutionSemesterFor(1));
                final Collection<Enrolment> enrolmentsSem2 = registration.getEnrolments(executionYear.getExecutionSemesterFor(2));

                spreadsheet.addCell(enrolmentsSem1.size());
                spreadsheet.addCell(enrolmentsSem2.size());

                spreadsheet.addCell(countApprovedEnrolments(enrolmentsSem1));
                spreadsheet.addCell(countApprovedEnrolments(enrolmentsSem2));
            }
        }
    }

    private int countApprovedEnrolments(final Collection<Enrolment> enrolments) {
        int count = 0;
        for (final Enrolment enrolment : enrolments) {
            if (enrolment.isApproved()) {
                count++;
            }
        }
        return count;
    }

    private String getAlmaMater(final Person person, final Registration registration) {
        for (IndividualCandidacyPersonalDetails shite : person.getIndividualCandidacies()) {
            if (shite.getCandidacy().getCandidacyProcess() instanceof MobilityIndividualApplicationProcess) {
                MobilityIndividualApplicationProcess erasmusShite =
                        (MobilityIndividualApplicationProcess) shite.getCandidacy().getCandidacyProcess();
                return erasmusShite.getCandidacy().getMobilityStudentData().getSelectedOpening().getMobilityAgreement()
                        .getUniversityUnit().getNameI18n().toString();
            }
        }

        if (registration.getRegistrationAgreement() == RegistrationAgreement.ALMEIDA_GARRETT
                || registration.getRegistrationAgreement() == RegistrationAgreement.ERASMUS_MUNDUS
                || registration.getRegistrationAgreement() == RegistrationAgreement.SOCRATES
                || registration.getRegistrationAgreement() == RegistrationAgreement.SOCRATES_ERASMUS) {

            if (!registration.hasStudentCandidacy()) {
                return EMPTY;
            }

            StudentCandidacy studentCandidacy = registration.getStudentCandidacy();
            if (!studentCandidacy.hasPrecedentDegreeInformation()) {
                return EMPTY;
            }

            PrecedentDegreeInformation precedentDegreeInformation = studentCandidacy.getPrecedentDegreeInformation();
            if (!precedentDegreeInformation.hasPrecedentInstitution()) {
                return EMPTY;
            }

            return precedentDegreeInformation.getPrecedentInstitution().getNameI18n().toString();
        }

        return EMPTY;
    }

    private String getAlmaMaterCountry(final Person person, final Registration registration) {
        for (IndividualCandidacyPersonalDetails shite : person.getIndividualCandidacies()) {
            if (shite.getCandidacy().getCandidacyProcess() instanceof MobilityIndividualApplicationProcess) {
                MobilityIndividualApplicationProcess erasmusShite =
                        (MobilityIndividualApplicationProcess) shite.getCandidacy().getCandidacyProcess();
                return erasmusShite.getCandidacy().getMobilityStudentData().getSelectedOpening().getMobilityAgreement()
                        .getUniversityUnit().getCountry().getLocalizedName().toString();
            }
        }

        if (registration.getRegistrationAgreement() == RegistrationAgreement.ALMEIDA_GARRETT
                || registration.getRegistrationAgreement() == RegistrationAgreement.ERASMUS_MUNDUS
                || registration.getRegistrationAgreement() == RegistrationAgreement.SOCRATES
                || registration.getRegistrationAgreement() == RegistrationAgreement.SOCRATES_ERASMUS) {

            if (!registration.hasStudentCandidacy()) {
                return EMPTY;
            }

            StudentCandidacy studentCandidacy = registration.getStudentCandidacy();
            if (!studentCandidacy.hasPrecedentDegreeInformation()) {
                return EMPTY;
            }

            PrecedentDegreeInformation precedentDegreeInformation = studentCandidacy.getPrecedentDegreeInformation();
            if (!precedentDegreeInformation.hasPrecedentCountry()) {
                return EMPTY;
            }

            return precedentDegreeInformation.getPrecedentCountry().getLocalizedName().toString();
        }

        return EMPTY;
    }

    private String getFullAddress(final Person person) {
        if (person.hasDefaultPhysicalAddress()) {
            StringBuilder sb = new StringBuilder();

            if (!StringUtils.isEmpty(person.getDefaultPhysicalAddress().getAddress())) {
                sb.append(person.getDefaultPhysicalAddress().getAddress()).append(" ");
            }

            if (!StringUtils.isEmpty(person.getDefaultPhysicalAddress().getArea())) {
                sb.append(person.getDefaultPhysicalAddress().getArea()).append(" ");
            }

            if (!StringUtils.isEmpty(person.getDefaultPhysicalAddress().getAreaCode())) {
                sb.append(person.getDefaultPhysicalAddress().getAreaCode()).append(" ");
            }

            if (!StringUtils.isEmpty(person.getDefaultPhysicalAddress().getAreaOfAreaCode())) {
                sb.append(person.getDefaultPhysicalAddress().getAreaOfAreaCode()).append(" ");
            }

            return StringUtils.isEmpty(sb.toString()) ? EMPTY : sb.toString();
        }

        return EMPTY;
    }

    private void addBranchsInformation(final StyledExcelSpreadsheet spreadsheet, final StudentCurricularPlan studentCurricularPlan) {

        final StringBuilder majorBranches = new StringBuilder();
        final StringBuilder minorBranches = new StringBuilder();

        for (final BranchCurriculumGroup group : studentCurricularPlan.getBranchCurriculumGroups()) {
            if (group.isMajor()) {
                majorBranches.append(group.getName().toString()).append(",");
            } else if (group.isMinor()) {
                minorBranches.append(group.getName().toString()).append(",");
            }
        }

        if (majorBranches.length() > 0) {
            spreadsheet.addCell(majorBranches.deleteCharAt(majorBranches.length() - 1).toString());
        } else {
            spreadsheet.addCell("");
        }

        if (minorBranches.length() > 0) {
            spreadsheet.addCell(minorBranches.deleteCharAt(minorBranches.length() - 1).toString());
        } else {
            spreadsheet.addCell("");
        }
    }

    private void fillSpreadSheetPreBolonhaInfo(StyledExcelSpreadsheet spreadsheet, Registration registration) {
        if (!registration.isBolonha()) {
            RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);
            fillSpreadSheetRegistrationInfo(spreadsheet, registrationConclusionBean, registration.hasConcluded());
        } else {
            fillSpreadSheetEmptyCells(spreadsheet);
        }
    }

    private void fillSpreadSheetBolonhaInfo(StyledExcelSpreadsheet spreadsheet, Registration registration,
            CycleCurriculumGroup cycle) {
        if ((cycle != null) && (!cycle.isExternal())) {
            RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration, cycle);
            fillSpreadSheetRegistrationInfo(spreadsheet, registrationConclusionBean, registrationConclusionBean.isConcluded());
        } else {
            fillSpreadSheetEmptyCells(spreadsheet);
        }
    }

    private void fillSpreadSheetRegistrationInfo(StyledExcelSpreadsheet spreadsheet,
            RegistrationConclusionBean registrationConclusionBean, boolean isConcluded) {
        spreadsheet.addCell(getResourceMessage("label." + (isConcluded ? "yes" : "no") + ".capitalized"));

        spreadsheet.addCell(isConcluded ? registrationConclusionBean.getConclusionDate().toString(YMD_FORMAT) : EMPTY);

        spreadsheet.addCell(registrationConclusionBean.getAverage().toString());

        spreadsheet.addCell(getResourceMessage("label." + (registrationConclusionBean.isConclusionProcessed() ? "yes" : "no")
                + ".capitalized"));

        spreadsheet.addCell(registrationConclusionBean.getCalculatedEctsCredits());
    }

    private void fillSpreadSheetEmptyCells(StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.addCell(EMPTY);
        spreadsheet.addCell(EMPTY);
        spreadsheet.addCell(EMPTY);
        spreadsheet.addCell(EMPTY);
        spreadsheet.addCell(EMPTY);
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet, final boolean extendedInfo) {
        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(getResourceMessage("label.degree.acronym"));
        spreadsheet.addHeader(getResourceMessage("label.degree.name"));
        spreadsheet.addHeader(getResourceMessage("label.number"));
        spreadsheet.addHeader(getResourceMessage("label.name"));
        spreadsheet.addHeader(getResourceMessage("label.documentIdNumber"));
        spreadsheet.addHeader(getResourceMessage("label.registration.state"));
        spreadsheet.addHeader(getResourceMessage("label.registration.state.start.date"));
        spreadsheet.addHeader(getResourceMessage("label.registrationAgreement"));
        if (extendedInfo) {
            spreadsheet.addHeader(getResourceMessage("label.almamater"));
            spreadsheet.addHeader(getResourceMessage("label.almamater.country"));
            spreadsheet.addHeader(getResourceMessage("label.email"));
            spreadsheet.addHeader(getResourceMessage("label.nationality"));
            spreadsheet.addHeader(getResourceMessage("label.person.title.addressInfo"));
            spreadsheet.addHeader(getResourceMessage("label.person.title.contactInfo"));
            spreadsheet.addHeader(getResourceMessage("label.gender"));
            spreadsheet.addHeader(getResourceMessage("label.dateOfBirth"));
            spreadsheet.addHeader(getResourceMessage("label.registration.enrolments.number.short"));
            spreadsheet.addHeader(getResourceMessage("curricular.year"));
            spreadsheet.addHeader(getResourceMessage("label.student.enrolments.number.short"));
            spreadsheet.addHeader(getResourceMessage("registration.regime"));
            spreadsheet.addHeader(getResourceMessage("degree.concluded"));
            spreadsheet.addHeader(getResourceMessage("label.conclusionDate"));
            spreadsheet.addHeader(getResourceMessage("degree.average"));
            spreadsheet.addHeader(getResourceMessage("degree.hasConclusionProcess"));
            spreadsheet.addHeader("");
            spreadsheet.addHeader(getResourceMessage("student.is.senior"));

            if (getAdministratedCycleTypes().contains(CycleType.FIRST_CYCLE)) {
                spreadsheet.addHeader(getResourceMessage("label.firstCycle.concluded"));
                spreadsheet.addHeader(getResourceMessage("label.firstCycle.conclusionDate"));
                spreadsheet.addHeader(getResourceMessage("label.firstCycle.average"));
                spreadsheet.addHeader(getResourceMessage("label.firstCycle.hasConclusionProcess"));
                spreadsheet.addHeader(getResourceMessage("label.firstCycle.ects"));
            }
            if (getAdministratedCycleTypes().contains(CycleType.SECOND_CYCLE)) {
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.concluded"));
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.conclusionDate"));
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.average"));
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.hasConclusionProcess"));
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.ects"));
            }
            if (getAdministratedCycleTypes().contains(CycleType.THIRD_CYCLE)) {
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.concluded"));
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.conclusionDate"));
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.average"));
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.hasConclusionProcess"));
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.ects"));
            }

            spreadsheet.addHeader(getResourceMessage("label.studentData.personalDataAuthorization"));

            spreadsheet.addHeader(getResourceMessage("label.main.branch"));
            spreadsheet.addHeader(getResourceMessage("label.minor.branch"));
            spreadsheet.addHeader(getResourceMessage("label.student.enrolments.number.first.semester"));
            spreadsheet.addHeader(getResourceMessage("label.student.enrolments.number.second.semester"));
            spreadsheet.addHeader(getResourceMessage("label.student.enrolments.approved.first.semester"));
            spreadsheet.addHeader(getResourceMessage("label.student.enrolments.approved.second.semester"));
        }
    }

    protected static String getResourceMessage(String key) {
        return BundleUtil.getString(Bundle.ACADEMIC, "academicAdminOffice", key);
    }

    protected Set<CycleType> getAdministratedCycleTypes() {
        Set<CycleType> cycles = new HashSet<CycleType>();
        for (DegreeType degreeType : AcademicAuthorizationGroup.getDegreeTypesForOperation(AccessControl.getPerson(),
                AcademicOperationType.STUDENT_LISTINGS)) {
            cycles.addAll(degreeType.getCycleTypes());
        }
        return cycles;
    }
}