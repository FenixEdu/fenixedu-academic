/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.lists;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.BranchCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.academicAdministration.SearchStudentsByDegreeParametersBean;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.dto.student.RegistrationWithStateForExecutionYearBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminListingsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.StyledExcelSpreadsheet;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
            Set<DegreeType> degreeTypesForOperation = AcademicAccessRule
                    .getDegreeTypesAccessibleToFunction(AcademicOperationType.STUDENT_LISTINGS, Authenticate.getUser())
                    .collect(Collectors.toSet());
            degreeTypesForOperation.addAll(PermissionService.getDegreeTypes("ACADEMIC_OFFICE_REPORTS", Authenticate.getUser()));

            Set<Degree> degreesForOperation = AcademicAccessRule
                    .getDegreesAccessibleToFunction(AcademicOperationType.STUDENT_LISTINGS, Authenticate.getUser())
                    .collect(Collectors.toSet());
            degreesForOperation.addAll(PermissionService.getDegrees("ADMIN_OFFICE_REPORTS", Authenticate.getUser()));
            bean = new SearchStudentsByDegreeParametersBean(degreeTypesForOperation, degreesForOperation);
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
        Stream<DegreeCurricularPlan> stream =
                executionYear.getExecutionDegreesSet().stream().map(ExecutionDegree::getDegreeCurricularPlan);

        if (chosenDegreeType != null) {
            stream = stream.filter(dcp -> dcp.getDegreeType() == chosenDegreeType);
        }

        if (chosenDegree != null) {
            stream = stream.filter(dcp -> dcp.getDegree() == chosenDegree);
        }

        stream = stream.filter(
                dcp -> !dcp.getDegreeType().isEmpty() && searchbean.getAdministratedDegreeTypes().contains(dcp.getDegreeType())
                        || dcp.getDegreeType().isEmpty() && searchbean.getAdministratedDegrees().contains(dcp.getDegree()));

        stream.forEach(dcp -> {
            dcp.getRegistrations(executionYear, registrations);
        });

        return filterResults(searchbean, registrations, executionYear);

    }

    private static List<RegistrationWithStateForExecutionYearBean> filterResults(SearchStudentsByDegreeParametersBean searchBean,
            final Set<Registration> registrations, final ExecutionYear executionYear) {
        final List<RegistrationWithStateForExecutionYearBean> result = new ArrayList<RegistrationWithStateForExecutionYearBean>();
        Stream<Registration> registrationStream = registrations.stream();

        registrationStream = registrationStream.filter(r -> r.getLastRegistrationState(executionYear) != null);

        if (searchBean.hasAnyRegistrationProtocol()) {
            registrationStream =
                    registrationStream.filter(r -> searchBean.getRegistrationProtocols().contains(r.getRegistrationProtocol()));
        }

        if (searchBean.hasAnyStudentStatuteType()) {
            registrationStream = registrationStream.filter(r -> hasStudentStatuteType(searchBean, r));
        }

        if (searchBean.hasAnyRegistrationStateTypes()) {
            registrationStream = registrationStream.filter(r -> searchBean.getRegistrationStateTypes()
                    .contains(r.getLastRegistrationState(executionYear).getStateType()));
        }

        if (searchBean.isIngressedInChosenYear()) {
            registrationStream = registrationStream.filter(r -> r.getIngressionYear() == executionYear);
        }

        if (searchBean.hasAnyProgramConclusion()) {
            registrationStream = registrationStream.filter(r -> hasProgramConclusion(r,
                    searchBean.isIncludeConcludedWithoutConclusionProcess(), searchBean.getProgramConclusions(), executionYear));
        }
        if (searchBean.getActiveEnrolments()) {
            registrationStream = registrationStream.filter(r -> r.hasAnyEnrolmentsIn(executionYear));
        }

        if (searchBean.getStandaloneEnrolments()) {
            registrationStream = registrationStream.filter(r -> r.hasAnyStandaloneEnrolmentsIn(executionYear));
        }

        if (searchBean.getRegime() != null) {
            registrationStream = registrationStream.filter(r -> r.getRegimeType(executionYear) == searchBean.getRegime());
        }

        if (searchBean.getNationality() != null) {
            registrationStream = registrationStream.filter(r -> r.getPerson().getCountry() == searchBean.getNationality());
        }

        if (searchBean.getIngressionType() != null) {
            registrationStream = registrationStream.filter(r -> r.getIngressionType() == searchBean.getIngressionType());
        }

        registrationStream
                .forEach(r -> result.add(new RegistrationWithStateForExecutionYearBean(r, r.getLastStateType(), executionYear)));

        return result;
    }

    static private boolean hasStudentStatuteType(final SearchStudentsByDegreeParametersBean searchBean,
            final Registration registration) {
        return CollectionUtils.containsAny(searchBean.getStudentStatuteTypes(),
                registration.getStudent().getStatutesTypesValidOnAnyExecutionSemesterFor(searchBean.getExecutionYear()));
    }

    private static boolean hasProgramConclusion(final Registration registration, boolean includeConcludedWithoutConclusionProcess,
            final List<ProgramConclusion> programConclusions, ExecutionYear executionYear) {
        return programConclusions.stream().anyMatch(pc -> pc.groupFor(registration).filter(curriculumGroup -> {
            if (includeConcludedWithoutConclusionProcess) {
                if (curriculumGroup.isConcluded() && curriculumGroup.calculateConclusionYear() == executionYear) {
                    return true;
                }
            }
            return curriculumGroup.getConclusionYear() == executionYear;
        }).isPresent());
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
                filename += "_" + degreeType.getName().getContent().replace(' ', '_');
            }
            filename += "_" + executionYear.getYear();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=\"" + filename + ".xls\"");
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

    private void fillSpreadSheetFilters(SearchStudentsByDegreeParametersBean searchBean,
            final StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.newHeaderRow();
        if (searchBean.isIngressedInChosenYear()) {
            spreadsheet.addHeader(getResourceMessage("label.ingressedInChosenYear"));
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
        if (searchBean.getIngressionType() != null) {
            spreadsheet.addHeader(getResourceMessage("label.ingression.short") + ": "
                    + searchBean.getIngressionType().getLocalizedName(Locale.getDefault()));
        }

        spreadsheet.newHeaderRow();
        if (searchBean.hasAnyRegistrationProtocol()) {
            spreadsheet.addHeader(getResourceMessage("label.registrationAgreement") + ":");
            for (RegistrationProtocol protocol : searchBean.getRegistrationProtocols()) {
                spreadsheet.addHeader(protocol.getDescription().getContent());
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
            for (StatuteType statute : searchBean.getStudentStatuteTypes()) {
                spreadsheet.addHeader(statute.getName().getContent());
            }
        }
        spreadsheet.newHeaderRow();
        if (searchBean.hasAnyProgramConclusion()) {
            spreadsheet.addHeader(getResourceMessage("label.programConclusions") + ":");
            for (ProgramConclusion programConclusion : searchBean.getProgramConclusions()) {
                spreadsheet.addHeader(
                        programConclusion.getName().getContent() + "-" + programConclusion.getDescription().getContent());
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
            spreadsheet.addCell(
                    !StringUtils.isEmpty(degree.getSigla()) ? degree.getSigla() : degree.getNameFor(executionYear).getContent());
            spreadsheet.addCell(degree.getNameFor(executionYear));
            spreadsheet.addCell(registration.getNumber().toString());

            final Person person = registration.getPerson();
            spreadsheet.addCell(person.getUsername());
            spreadsheet.addCell(person.getName());
            spreadsheet.addCell(person.getDocumentIdNumber());

            final RegistrationState lastRegistrationState = registration.getLastRegistrationState(executionYear);
            spreadsheet.addCell(lastRegistrationState.getStateType().getDescription());
            spreadsheet.addCell(lastRegistrationState.getStateDate().toString("yyyy-MM-dd"));
            spreadsheet.addCell(registration.getRegistrationProtocol().getCode());

            if (extendedInfo) {
//                spreadsheet.addCell(getAlmaMater(person, registration));
//                spreadsheet.addCell(getAlmaMaterCountry(person, registration));
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

//    private String getAlmaMater(final Person person, final Registration registration) {
//
//        if (registration.getRegistrationProtocol().attemptAlmaMatterFromPrecedent()) {
//
//            if (registration.getStudentCandidacy() == null) {
//                return EMPTY;
//            }
//
//            StudentCandidacy studentCandidacy = registration.getStudentCandidacy();
//            if (studentCandidacy.getPrecedentDegreeInformation() == null) {
//                return EMPTY;
//            }
//
//            PrecedentDegreeInformation precedentDegreeInformation = studentCandidacy.getPrecedentDegreeInformation();
//            if (precedentDegreeInformation.getPrecedentInstitution() == null) {
//                return EMPTY;
//            }
//
//            return precedentDegreeInformation.getPrecedentInstitution().getNameI18n().getContent();
//        }
//
//        return EMPTY;
//    }

//    private String getAlmaMaterCountry(final Person person, final Registration registration) {
//        if (registration.getRegistrationProtocol().attemptAlmaMatterFromPrecedent()) {
//
//            if (registration.getStudentCandidacy() == null) {
//                return EMPTY;
//            }
//
//            StudentCandidacy studentCandidacy = registration.getStudentCandidacy();
//            if (studentCandidacy.getPrecedentDegreeInformation() == null) {
//                return EMPTY;
//            }
//
//            PrecedentDegreeInformation precedentDegreeInformation = studentCandidacy.getPrecedentDegreeInformation();
//            if (precedentDegreeInformation.getPrecedentCountry() == null) {
//                return EMPTY;
//            }
//
//            return precedentDegreeInformation.getPrecedentCountry().getLocalizedName().getContent();
//        }
//
//        return EMPTY;
//    }

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

    private void addBranchsInformation(final StyledExcelSpreadsheet spreadsheet,
            final StudentCurricularPlan studentCurricularPlan) {

        final StringBuilder majorBranches = new StringBuilder();
        final StringBuilder minorBranches = new StringBuilder();

        for (final BranchCurriculumGroup group : studentCurricularPlan.getBranchCurriculumGroups()) {
            if (group.isMajor()) {
                majorBranches.append(group.getName().getContent()).append(",");
            } else if (group.isMinor()) {
                minorBranches.append(group.getName().getContent()).append(",");
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
        spreadsheet.addCell(EMPTY);
        spreadsheet.addCell(EMPTY);
        spreadsheet.addCell(EMPTY);
        spreadsheet.addCell(EMPTY);
        spreadsheet.addCell(EMPTY);
    }

    private void fillSpreadSheetBolonhaInfo(StyledExcelSpreadsheet spreadsheet, Registration registration,
            CycleCurriculumGroup cycle) {
        if (cycle != null && !cycle.isExternal()) {
            RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration, cycle);
            fillSpreadSheetRegistrationInfo(spreadsheet, registrationConclusionBean, registrationConclusionBean.isConcluded());
        } else {
            fillSpreadSheetEmptyCells(spreadsheet);
        }
    }

    private void fillSpreadSheetRegistrationInfo(StyledExcelSpreadsheet spreadsheet,
            RegistrationConclusionBean registrationConclusionBean, boolean isConcluded) {
        spreadsheet.addCell(BundleUtil.getString(Bundle.APPLICATION, "label." + (isConcluded ? "yes" : "no") + ".capitalized"));

        spreadsheet.addCell(isConcluded ? registrationConclusionBean.getConclusionDate().toString(YMD_FORMAT) : EMPTY);

        ExecutionYear conclusionYear = registrationConclusionBean.getCurriculumGroup().getConclusionYear();

        spreadsheet.addCell(conclusionYear != null ? conclusionYear.getName() : EMPTY);

        spreadsheet.addCell(registrationConclusionBean.getRawGrade().getValue());

        spreadsheet.addCell(BundleUtil.getString(Bundle.APPLICATION,
                "label." + (registrationConclusionBean.isConclusionProcessed() ? "yes" : "no") + ".capitalized"));

        spreadsheet.addCell(registrationConclusionBean.getCalculatedEctsCredits());
    }

    private void fillSpreadSheetEmptyCells(StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.addCell(EMPTY);
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
        spreadsheet.addHeader(getResourceMessage("label.username"));
        spreadsheet.addHeader(getResourceMessage("label.name"));
        spreadsheet.addHeader(getResourceMessage("label.documentIdNumber"));
        spreadsheet.addHeader(getResourceMessage("label.registration.state"));
        spreadsheet.addHeader(getResourceMessage("label.registration.state.start.date"));
        spreadsheet.addHeader(getResourceMessage("label.registrationAgreement"));
        if (extendedInfo) {
//            spreadsheet.addHeader(getResourceMessage("label.almamater"));
//            spreadsheet.addHeader(getResourceMessage("label.almamater.country"));
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
                spreadsheet.addHeader(getResourceMessage("label.firstCycle.conclusionYear"));
                spreadsheet.addHeader(getResourceMessage("label.firstCycle.average"));
                spreadsheet.addHeader(getResourceMessage("label.firstCycle.hasConclusionProcess"));
                spreadsheet.addHeader(getResourceMessage("label.firstCycle.ects"));
            }
            if (getAdministratedCycleTypes().contains(CycleType.SECOND_CYCLE)) {
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.concluded"));
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.conclusionDate"));
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.conclusionYear"));
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.average"));
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.hasConclusionProcess"));
                spreadsheet.addHeader(getResourceMessage("label.secondCycle.ects"));
            }
            if (getAdministratedCycleTypes().contains(CycleType.THIRD_CYCLE)) {
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.concluded"));
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.conclusionDate"));
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.conclusionYear"));
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.average"));
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.hasConclusionProcess"));
                spreadsheet.addHeader(getResourceMessage("label.thirdCycle.ects"));
            }

            spreadsheet.addHeader(getResourceMessage("label.main.branch"));
            spreadsheet.addHeader(getResourceMessage("label.minor.branch"));
            spreadsheet.addHeader(getResourceMessage("label.student.enrolments.number.first.semester"));
            spreadsheet.addHeader(getResourceMessage("label.student.enrolments.number.second.semester"));
            spreadsheet.addHeader(getResourceMessage("label.student.enrolments.approved.first.semester"));
            spreadsheet.addHeader(getResourceMessage("label.student.enrolments.approved.second.semester"));
        }
    }

    protected static String getResourceMessage(String key) {
        return BundleUtil.getString(Bundle.ACADEMIC, key);
    }

    protected Set<CycleType> getAdministratedCycleTypes() {
        Set<CycleType> cycles = new HashSet<CycleType>();
        Set<DegreeType> programs = AcademicAccessRule
                .getDegreeTypesAccessibleToFunction(AcademicOperationType.STUDENT_LISTINGS, Authenticate.getUser())
                .collect(Collectors.toSet());
        programs.addAll(PermissionService.getDegreeTypes("ACADEMIC_OFFICE_REPORTS", Authenticate.getUser()));
        for (DegreeType degreeType : programs) {
            cycles.addAll(degreeType.getCycleTypes());
        }
        return cycles;
    }
}
