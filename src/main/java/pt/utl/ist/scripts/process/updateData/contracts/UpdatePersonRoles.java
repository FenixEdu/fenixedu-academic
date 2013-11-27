package pt.utl.ist.scripts.process.updateData.contracts;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;

@Task(englishTitle = "UpdatePersonRoles")
public class UpdatePersonRoles extends CronTask {
    public UpdatePersonRoles() {

    }

    @Override
    public void runTask() {
        getLogger().debug("Start UpdatePersonRoles");
        Set<Person> activeExternalTeacher = new HashSet<Person>();
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();
        ExecutionSemester previousExecutionSemester = currentExecutionSemester.getPreviousExecutionPeriod();
        Set<TeacherAuthorization> teacherAuthorizations = new HashSet<TeacherAuthorization>();
        teacherAuthorizations.addAll(currentExecutionSemester.getAuthorizationSet());
        teacherAuthorizations.addAll(previousExecutionSemester.getAuthorizationSet());
        for (TeacherAuthorization teacherAuthorization : teacherAuthorizations) {
            if (teacherAuthorization instanceof ExternalTeacherAuthorization) {
                if (teacherAuthorization.getTeacher().getTeacherAuthorization(currentExecutionSemester) == null) {
                    if (teacherAuthorization.getTeacher().getPerson().hasRole(RoleType.TEACHER)) {
                        teacherAuthorization.getTeacher().getPerson().removeRoleByType(RoleType.TEACHER);
                        getLogger().info(
                                "Removed TEACHER Role -> TeacherNumber: " + teacherAuthorization.getTeacher().getTeacherId());
                    }
                } else {
                    if (!teacherAuthorization.getTeacher().getPerson().hasRole(RoleType.TEACHER)) {
                        teacherAuthorization.getTeacher().getPerson().addPersonRoleByRoleType(RoleType.TEACHER);
                        getLogger().info(
                                "Added TEACHER Role -> TeacherNumber: " + teacherAuthorization.getTeacher().getTeacherId());
                    }
                    activeExternalTeacher.add(teacherAuthorization.getTeacher().getPerson());
                }

            }
        }

        for (Employee employee : Bennu.getInstance().getEmployeesSet()) {
            Person person = employee.getPerson();
            if (isActive(person, CategoryType.TEACHER)) {
                createTeacher(employee);
            } else {
                if (person.hasRole(RoleType.TEACHER) && !activeExternalTeacher.contains(person)) {
                    person.removeRoleByType(RoleType.TEACHER);
                    getLogger().info("Removed Teacher Role -> TeacherNumber: " + person.getEmployee().getEmployeeNumber());
                }
            }
            if (employee.getPerson().getTeacher() != null) {
                if (employee.getPerson().getTeacher().getCurrentWorkingDepartment() == null
                        && employee.getPerson().hasRole(RoleType.DEPARTMENT_MEMBER)) {
                    employee.getPerson().removeRoleByType(RoleType.DEPARTMENT_MEMBER);
                    getLogger().info("Removed DEPARTMENT_MEMBER Role -> TeacherNumber: " + employee.getEmployeeNumber());
                } else if (employee.getPerson().getTeacher().getCurrentWorkingDepartment() != null
                        && !employee.getPerson().hasRole(RoleType.DEPARTMENT_MEMBER)) {
                    employee.getPerson().addPersonRoleByRoleType(RoleType.DEPARTMENT_MEMBER);
                    getLogger().info("Added DEPARTMENT_MEMBER Role -> TeacherNumber: " + employee.getEmployeeNumber());
                }
            }
        }

        for (Employee employee : Bennu.getInstance().getEmployeesSet()) {
            Person person = employee.getPerson();
            if (person != null) {
                boolean active = isActive(person, CategoryType.RESEARCHER);
                if (active || hasActiveResearchContracts(person)) {
                    if (!person.hasRole(RoleType.RESEARCHER)) {
                        person.addPersonRoleByRoleType(RoleType.RESEARCHER);
                        getLogger().info("Added Researcher Role -> ResearcherNumber: " + employee.getEmployeeNumber());
                    }
                    if (active && !person.hasRole(RoleType.EMPLOYEE)) {
                        person.addPersonRoleByRoleType(RoleType.EMPLOYEE);
                    }
                } else {
                    if (person.hasRole(RoleType.RESEARCHER) && (!person.hasRole(RoleType.TEACHER))) {
                        person.removeRoleByType(RoleType.RESEARCHER);
                        getLogger().info("Removed Researcher Role -> ResearcherNumber: " + employee.getEmployeeNumber());
                        if (person.hasRole(RoleType.EMPLOYEE)) {
                            person.removeRoleByType(RoleType.EMPLOYEE);
                        }
                    }
                }
            }
        }
        for (Person person : Role.getRoleByRoleType(RoleType.RESEARCHER).getAssociatedPersons()) {
            if ((!person.hasRole(RoleType.TEACHER)) && !isActive(person, CategoryType.RESEARCHER)
                    && !hasActiveResearchContracts(person)) {
                if (person.hasRole(RoleType.RESEARCHER)) {
                    person.removeRoleByType(RoleType.RESEARCHER);
                    getLogger().info("Removed Researcher Role -> ResearcherNumber: " + person.getUsername());
                }
            }
        }

        LocalDate today = new LocalDate();
        for (Employee employee : Bennu.getInstance().getEmployeesSet()) {
            Person person = employee.getPerson();
            if (isActive(person, CategoryType.EMPLOYEE)) {
                if (!person.hasRole(RoleType.EMPLOYEE)) {
                    person.addPersonRoleByRoleType(RoleType.EMPLOYEE);
                    getLogger().info("Added Employee Role -> EmployeeNumber: " + person.getEmployee().getEmployeeNumber());
                }
            } else {
                GiafProfessionalData giafProfessionalData = getGiafProfessionalData(person, CategoryType.EMPLOYEE);
                if (person.hasRole(RoleType.EMPLOYEE) && !person.hasRole(RoleType.TEACHER)
                        && !person.hasRole(RoleType.RESEARCHER)) {
                    if (giafProfessionalData == null
                            || (giafProfessionalData.getContractSituation().getEndSituation() && (giafProfessionalData
                                    .getTerminationSituationDate() == null || giafProfessionalData.getTerminationSituationDate()
                                    .plusDays(45).isBefore(today)))) {
                        person.removeRoleByType(RoleType.EMPLOYEE);
                        getLogger().info("Removed Employee Role -> EmployeeNumber: " + person.getEmployee().getEmployeeNumber());
                    }
                } else if (!person.hasRole(RoleType.EMPLOYEE) && !person.hasRole(RoleType.TEACHER)
                        && !person.hasRole(RoleType.RESEARCHER)) {
                    if (giafProfessionalData != null && !giafProfessionalData.getContractSituation().getEndSituation()
                            && giafProfessionalData.getTerminationSituationDate() != null
                            && giafProfessionalData.getTerminationSituationDate().plusDays(45).isAfter(today)) {
                        person.addPersonRoleByRoleType(RoleType.EMPLOYEE);
                        getLogger().info(
                                "Not active but has no endDate ?!?!? Added Employee Role -> EmployeeNumber: "
                                        + person.getEmployee().getEmployeeNumber());
                    }
                }
            }
        }
        for (Employee employee : Bennu.getInstance().getEmployeesSet()) {
            Person person = employee.getPerson();
            if (isActive(person, CategoryType.GRANT_OWNER)) {
                if (!person.hasRole(RoleType.GRANT_OWNER)) {
                    person.addPersonRoleByRoleType(RoleType.GRANT_OWNER);
                    getLogger().info("Added GrantOwner Role -> GrantOwnerNumber: " + person.getEmployee().getEmployeeNumber());
                }
            } else if (person.hasRole(RoleType.GRANT_OWNER)) {
                person.removeRoleByType(RoleType.GRANT_OWNER);
                getLogger().info("Removed GrantOwner Role -> GrantOwnerNumber: " + employee.getEmployeeNumber());
            }
        }
        getLogger().debug("The end");
    }

    private boolean hasActiveResearchContracts(Person person) {
        final Collection<? extends Accountability> accountabilities =
                person.getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);
        final YearMonthDay currentDate = new YearMonthDay();
        for (final Accountability accountability : accountabilities) {
            if (accountability.isActive(currentDate)) {
                return true;
            }
        }
        return false;
    }

    private void createTeacher(Employee employee) {
        if (!employee.getPerson().hasTeacher()) {
            new Teacher(employee.getPerson());
            getLogger().info("CREATED Teacher -> TeacherNumber: " + employee.getEmployeeNumber());
        }
        if (!employee.getPerson().hasRole(RoleType.TEACHER)) {
            employee.getPerson().addPersonRoleByRoleType(RoleType.TEACHER);
            getLogger().info("Added Teacher Role -> TeacherNumber: " + employee.getEmployeeNumber());
        }
    }

    private GiafProfessionalData getGiafProfessionalData(Person person, CategoryType categoryType) {
        PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
        if (personProfessionalData != null) {
            GiafProfessionalData giafProfessionalDataByCategoryType =
                    personProfessionalData.getGiafProfessionalDataByCategoryType(categoryType);
            return giafProfessionalDataByCategoryType;
        }
        return null;
    }

    private boolean isActive(Person person, CategoryType categoryType) {
        PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
        if (personProfessionalData != null) {
            GiafProfessionalData giafProfessionalDataByCategoryType =
                    personProfessionalData.getGiafProfessionalDataByCategoryType(categoryType);

            LocalDate today = new LocalDate();

            if (giafProfessionalDataByCategoryType != null
                    && categoryType.equals(CategoryType.TEACHER)
                    && (giafProfessionalDataByCategoryType.getContractSituation().getName().getContent()
                            .equalsIgnoreCase("jubilado")
                            || giafProfessionalDataByCategoryType.getContractSituation().getName().getContent()
                                    .equalsIgnoreCase("Aposentação")
                            || giafProfessionalDataByCategoryType.getContractSituation().getGiafId().equalsIgnoreCase("108") // aposentado
                            // por
                            // idade
                            // limite
                            || giafProfessionalDataByCategoryType.getContractSituation().getGiafId().equalsIgnoreCase("139") // aposentação/destacado
                            || giafProfessionalDataByCategoryType.getContractSituation().getGiafId().equalsIgnoreCase("47") // aposentação
                    || giafProfessionalDataByCategoryType.getContractSituation().getGiafId().equalsIgnoreCase("34") // jubilado
                    )) {
                System.out.println("Docente com situação"
                        + giafProfessionalDataByCategoryType.getContractSituation().getName().getContent() + " "
                        + giafProfessionalDataByCategoryType.getContractSituation().getGiafId() + " "
                        + person.getEmployee().getEmployeeNumber());
                return true;
            }

            return giafProfessionalDataByCategoryType != null
                    && !(giafProfessionalDataByCategoryType.getContractSituation().getEndSituation()
                            || !giafProfessionalDataByCategoryType.getContractSituation().getInExercise() || (giafProfessionalDataByCategoryType
                            .getTerminationSituationDate() != null
                            && giafProfessionalDataByCategoryType.getTerminationSituationDate().isBefore(today) && giafProfessionalDataByCategoryType
                            .getTerminationSituationDate().isAfter(giafProfessionalDataByCategoryType.getContractSituationDate())));

        }
        return false;
    }

}
