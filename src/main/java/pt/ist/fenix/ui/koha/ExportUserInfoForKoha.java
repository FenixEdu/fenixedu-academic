package pt.ist.fenix.ui.koha;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.ExternalInterfaceDispatchAction;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.idcards.domain.CardGenerationBatch;
import org.fenixedu.idcards.domain.CardGenerationEntry;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(module = "external", path = "/exportUserInfoForKoha", scope = "request", parameter = "method")
public class ExportUserInfoForKoha extends ExternalInterfaceDispatchAction {

    private boolean chackCredentials(final HttpServletRequest request) {
        final String username = (String) getFromRequest(request, "username");
        final String password = (String) getFromRequest(request, "password");
        final String usernameProp = FenixConfigurationManager.getConfiguration().getExternalServicesKohaUsername();
        final String passwordProp = FenixConfigurationManager.getConfiguration().getExternalServicesKohaPassword();

        return !StringUtils.isEmpty(username) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(usernameProp)
                && !StringUtils.isEmpty(passwordProp) && username.equals(usernameProp) && password.equals(passwordProp);
    }

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        if (chackCredentials(request)) {
            super.execute(mapping, actionForm, request, response);
        } else {
            response.sendError(404, "Not authorized");
        }
        return null;
    }

    private ActionForward sendXls(final HttpServletResponse response, final Spreadsheet spreadsheet) throws IOException {
        final OutputStream stream = response.getOutputStream();
        response.setContentType("application/vnd.ms-access");
        response.setHeader("Content-disposition", "attachment; filename=list.xls");
        spreadsheet.exportToXLSSheet(stream);
        stream.close();
        return null;
    }

    public ActionForward getDegreeTypes(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet("DegreeTypes");
        spreadsheet.setHeader("*ID").setHeader("descrição");

        for (final DegreeType degreeType : DegreeType.values()) {
            final Row row = spreadsheet.addRow();
            row.setCell(degreeType.getName()).setCell(degreeType.getLocalizedName());
        }

        return sendXls(response, spreadsheet);
    }

    public ActionForward getDegrees(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet("Degrees");
        spreadsheet.setHeader("*ID").setHeader("descrição").setHeader("abreviatura").setHeader("*grau");

        for (final Degree degree : rootDomainObject.getDegreesSet()) {
            final Row row = spreadsheet.addRow();
            row.setCell(degree.getExternalId()).setCell(degree.getPresentationName()).setCell(degree.getSigla())
                    .setCell(degree.getDegreeType().name());
        }

        return sendXls(response, spreadsheet);
    }

    public ActionForward getDepartments(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet("Departments");
        spreadsheet.setHeader("*ID").setHeader("descrição");

        for (final Department department : rootDomainObject.getDepartmentsSet()) {
            final Row row = spreadsheet.addRow();
            row.setCell(department.getExternalId()).setCell(department.getName());
        }

        return sendXls(response, spreadsheet);
    }

    public ActionForward getTeachersAndResearchers(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet("TeachersAndResearchers");
        spreadsheet.setHeader("IST-ID").setHeader("*departamento").setHeader("nome").setHeader("email").setHeader("telefone")
                .setHeader("cgdCode");

        final Role personRole = Role.getRoleByRoleType(RoleType.PERSON);
        for (final Person person : personRole.getAssociatedPersonsSet()) {
            if (person.hasRole(RoleType.TEACHER)) {
                final Teacher teacher = person.getTeacher();
                if (teacher != null && teacher.getCurrentWorkingUnit() != null && person.getPersonProfessionalData() != null
                        && person.getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.TEACHER) != null) {
                    addEmployeeInformation(spreadsheet, person);
                }
            } else if (person.hasRole(RoleType.RESEARCHER) && person.hasRole(RoleType.EMPLOYEE) && person.getEmployee() != null
                    && person.getEmployee().getCurrentWorkingPlace() != null) {
                addEmployeeInformation(spreadsheet, person);
            }
        }

        return sendXls(response, spreadsheet);
    }

    private void addEmployeeInformation(final Spreadsheet spreadsheet, final Person person) {
        final Row row = spreadsheet.addRow();
        row.setCell(person.getUsername()).setCell(getWorkingDepartment(person)).setCell(person.getName())
                .setCell(getEmail(person)).setCell(getTelefone(person)).setCell(getCGDCode(person));
    }

    public ActionForward getStudents(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet("Students");
        spreadsheet.setHeader("IST-ID").setHeader("polo").setHeader("curso").setHeader("nome").setHeader("email")
                .setHeader("telefone").setHeader("cgdCode");

        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final DateTime begin = executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight();
        final DateTime end = executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight();;

        final Role personRole = Role.getRoleByRoleType(RoleType.PERSON);
        for (final Person person : personRole.getAssociatedPersonsSet()) {
            if (person.getStudent() != null) {
                final Student student = person.getStudent();
                final StudentCurricularPlan scp = findStudentCurricularPlan(student, begin, end);
                if (scp != null) {
                    final Row row = spreadsheet.addRow();
                    row.setCell(person.getUsername()).setCell(getCampus(scp.getCampus(executionYear)));
                    row.setCell(scp.getDegree().getExternalId()).setCell(person.getName());
                    row.setCell(getEmail(person)).setCell(getTelefone(person)).setCell(getCGDCode(person));
                } else {
                    final PhdIndividualProgramProcess phd = findPhd(person);
                    if (phd != null) {
                        final Row row = spreadsheet.addRow();
                        row.setCell(person.getUsername()).setCell(getCampus(phd));
                        row.setCell(getDegree(phd).getExternalId()).setCell(person.getName());
                        row.setCell(getEmail(person)).setCell(getTelefone(person)).setCell(getCGDCode(person));
                    }
                }
            }
        }

        return sendXls(response, spreadsheet);
    }

    private Degree getDegree(final PhdIndividualProgramProcess phd) {
        return phd.getPhdProgram().getDegree();
    }

    private String getCampus(final PhdIndividualProgramProcess phd) {
        final DegreeCurricularPlan degreeCurricularPlan = phd.getPhdProgram().getDegree().getLastActiveDegreeCurricularPlan();
        return getCampus(degreeCurricularPlan.getCurrentCampus());
    }

    private String getCampus(final Space campus) {
        return campus == null ? " " : campus.getName();
    }

    private StudentCurricularPlan findStudentCurricularPlan(final Student student, final DateTime begin, final DateTime end) {
        final Set<StudentCurricularPlan> studentCurricularPlans = getStudentCurricularPlans(begin, end, student);
        if (studentCurricularPlans.size() == 1) {
            return studentCurricularPlans.iterator().next();
        } else if (studentCurricularPlans.size() > 1) {
            return findMaxStudentCurricularPlan(studentCurricularPlans);
        }
        return null;
    }

    public static Set<StudentCurricularPlan> getStudentCurricularPlans(final DateTime begin, final DateTime end,
            final Student student) {
        final Set<StudentCurricularPlan> studentCurricularPlans = new HashSet<StudentCurricularPlan>();

        for (final Registration registration : student.getRegistrationsSet()) {
            if (!registration.isActive()) {
                continue;
            }
            final DegreeType degreeType = registration.getDegreeType();
            if (!degreeType.isBolonhaType()) {
                continue;
            }
            for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                if (studentCurricularPlan.isActive()) {
                    if (degreeType == DegreeType.BOLONHA_DEGREE || degreeType == DegreeType.BOLONHA_MASTER_DEGREE
                            || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE
                            || degreeType == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
                        studentCurricularPlans.add(studentCurricularPlan);
                    } else {
                        final RegistrationState registrationState = registration.getActiveState();
                        if (registrationState != null) {
                            final DateTime dateTime = registrationState.getStateDate();
                            if (!dateTime.isBefore(begin) && !dateTime.isAfter(end)) {
                                studentCurricularPlans.add(studentCurricularPlan);
                            }
                        }
                    }
                }
            }
        }
        return studentCurricularPlans;
    }

    private static StudentCurricularPlan findMaxStudentCurricularPlan(final Set<StudentCurricularPlan> studentCurricularPlans) {
        return Collections.max(studentCurricularPlans, new Comparator<StudentCurricularPlan>() {

            @Override
            public int compare(final StudentCurricularPlan o1, final StudentCurricularPlan o2) {
                final DegreeType degreeType1 = o1.getDegreeType();
                final DegreeType degreeType2 = o2.getDegreeType();
                if (degreeType1 == degreeType2) {
                    final YearMonthDay yearMonthDay1 = o1.getStartDateYearMonthDay();
                    final YearMonthDay yearMonthDay2 = o2.getStartDateYearMonthDay();
                    final int c = yearMonthDay1.compareTo(yearMonthDay2);
                    return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
                } else {
                    return degreeType1.compareTo(degreeType2);
                }
            }

        });
    }

    private PhdIndividualProgramProcess findPhd(final Person person) {
        final InsuranceEvent event = person.getInsuranceEventFor(ExecutionYear.readCurrentExecutionYear());
        return event != null && event.isClosed() ? findPhd(person.getPhdIndividualProgramProcessesSet()) : null;
    }

    private PhdIndividualProgramProcess findPhd(final Collection<PhdIndividualProgramProcess> phdIndividualProgramProcesses) {
        PhdIndividualProgramProcess result = null;
        for (final PhdIndividualProgramProcess process : phdIndividualProgramProcesses) {
            if (process.getActiveState() == PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
                if (result != null) {
                    return null;
                }
                result = process;
            }
        }
        return result;
    }

    private String getWorkingDepartment(final Person person) {
        if (person.getEmployee() != null) {
            final Employee employee = person.getEmployee();
            final Unit unit = employee.getCurrentWorkingPlace();
            if (unit != null) {
                final DepartmentUnit departmentUnit = unit.getDepartmentUnit();
                if (departmentUnit != null) {
                    return departmentUnit.getDepartment().getExternalId();
                }
            }
        }
        return " ";
    }

    public String getEmail(final Person person) {
        final EmailAddress email = person.getEmailAddressForSendingEmails();
        return email != null ? email.getValue() : " ";
    }

    private String getTelefone(final Person person) {
        final StringBuilder builder = new StringBuilder();
        for (final PartyContact partyContact : person.getPartyContactsSet()) {
            if (partyContact.isActiveAndValid()) {
                if (partyContact.isPhone()) {
                    final Phone phone = (Phone) partyContact;
                    if (builder.length() > 0) {
                        builder.append(", ");
                    }
                    builder.append(phone.getNumber());
                } else if (partyContact.isMobile()) {
                    final MobilePhone mobilePhone = (MobilePhone) partyContact;
                    if (builder.length() > 0) {
                        builder.append(", ");
                    }
                    builder.append(mobilePhone.getNumber());
                }
            }
        }
        return builder.toString();
    }

    private String getCGDCode(final Person person) {
        CardGenerationEntry result = null;
        for (final CardGenerationEntry entry : person.getCardGenerationEntriesSet()) {
            final CardGenerationBatch batch = entry.getCardGenerationBatch();
            if (batch.getSent() != null && batch.getCardGenerationProblemsSet().size() == 0) {
                if (result == null || result.getCardGenerationBatch().getSent().isBefore(batch.getSent())) {
                    result = entry;
                }
            }
        }
        return result == null ? " " : result.getCgdIdentifier();
    }

}
