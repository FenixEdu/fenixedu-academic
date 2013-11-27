package pt.utl.ist.scripts.process.cron.cardGeneration;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;

@Task(englishTitle = "CreateCardGenerationEntries")
public class CreateCardGenerationEntries extends CronTask {

    @Override
    public void runTask() {
        for (final CardGenerationBatch cardGenerationBatch : Bennu.getInstance().getCardGenerationBatchesSet()) {
            if (cardGenerationBatch.getSent() == null) {
                final String peopleString = cardGenerationBatch.getPeopleForEntryCreation();
                if ("ALL".equals(peopleString)) {
                    if (cardGenerationBatch.getCardGenerationEntriesSet().size() == 0) {
                        final ExecutionYear executionYear = cardGenerationBatch.getExecutionYear();
                        final DateTime begin = executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight();
                        final DateTime end = executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight();;

                        int ok = 0, u = 0, c = 500, i = 0, dups = 0;

                        final Role personRole = Role.getRoleByRoleType(RoleType.PERSON);
                        for (final Person person : personRole.getAssociatedPersonsSet()) {
                            if (--c == 0) {
                                getLogger().info("Processed: " + ((i + 1) * 500) + " people.");
                                c = 500;
                                i++;
                            }

                            final String professionalLine;
                            if (person.hasRole(RoleType.TEACHER)) {
                                final Teacher teacher = person.getTeacher();
                                if (teacher != null
                                        && teacher.getCurrentWorkingUnit() != null
                                        && person.getPersonProfessionalData() != null
                                        && person.getPersonProfessionalData().getGiafProfessionalDataByCategoryType(
                                                CategoryType.TEACHER) != null) {
                                    professionalLine = CardGenerationEntry.createLine(teacher);
                                } else {
                                    professionalLine = null;
                                }
                            } else if (person.hasRole(RoleType.RESEARCHER) && person.hasRole(RoleType.EMPLOYEE)
                                    && person.getEmployee() != null && person.getEmployee().getCurrentWorkingPlace() != null) {
                                final Employee employee = person.getEmployee();
                                professionalLine = CardGenerationEntry.createResearcherLine(employee);
                            } else if (person.hasRole(RoleType.EMPLOYEE) && person.hasPersonProfessionalData()) {
                                final Employee employee = person.getEmployee();
                                professionalLine = CardGenerationEntry.createLine(employee);
                            } else if (person.hasRole(RoleType.GRANT_OWNER) && person.hasEmployee()
                                    && !person.hasRole(RoleType.EMPLOYEE) && person.hasPersonProfessionalData()) {
                                final Employee employee = person.getEmployee();
                                professionalLine = CardGenerationEntry.createGrantOwnerLine(employee);
                            } else {
                                professionalLine = null;
                            }

                            String studentLine = null;
                            if (person.hasStudent()) {
                                final Student student = person.getStudent();
                                if (!student.getActiveRegistrations().isEmpty()) {
                                    final StudentCurricularPlan studentCurricularPlan =
                                            findStudentCurricularPlan(cardGenerationBatch, student, begin, end);
                                    if (studentCurricularPlan != null) {
                                        studentLine = CardGenerationEntry.createLine(studentCurricularPlan);
                                    }
                                }
                                if (studentLine == null) {
                                    final PhdIndividualProgramProcess phdIndividualProgramProcess = find(person);
                                    if (phdIndividualProgramProcess != null) {
                                        studentLine = CardGenerationEntry.createLine(phdIndividualProgramProcess);
                                    }
                                }
                            }

                            final String finalLine;
                            if (professionalLine != null && studentLine != null) {
                                finalLine = CardGenerationBatch.merge(professionalLine, studentLine);
                            } else if (professionalLine != null) {
                                finalLine = professionalLine;
                            } else if (studentLine != null) {
                                finalLine = studentLine;
                            } else {
                                finalLine = null;
                            }

                            if (finalLine != null) {
                                if (!hasMatchingLine(person, CardGenerationEntry.fix(finalLine))) {
                                    final CardGenerationEntry cardGenerationEntry = new CardGenerationEntry();
                                    cardGenerationEntry.setCreated(new DateTime());
                                    cardGenerationEntry.setCardGenerationBatch(cardGenerationBatch);
                                    cardGenerationEntry.setPerson(person);
                                    cardGenerationEntry.setLine(finalLine);
                                } else {
                                    dups++;
                                }
                            }
                        }

                        getLogger().info("Ok        : " + ok);
                        getLogger().info("Duplicados: " + dups);
                        getLogger().info("unknown   : " + u);
                    }
                    cardGenerationBatch.setPeopleForEntryCreation(null);
                    final SystemSender systemSender = Bennu.getInstance().getSystemSender();
                    new Message(
                            systemSender,
                            Collections.EMPTY_SET,
                            Collections.EMPTY_SET,
                            Collections.EMPTY_SET,
                            getRecipients(),
                            BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                                    "label.card.generation.entry.fill.subject"),
                            BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                                    "label.card.generation.entry.fill.body",
                                    cardGenerationBatch.getCreated().toString("yyyy-MM-dd"), cardGenerationBatch.getDescription()),
                            Collections.EMPTY_SET);
                }
            }
        }
    }

    private Collection<Recipient> getRecipients() {
        return Collections.singleton(new Recipient(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                "label.card.generation.manager.group"), new RoleGroup(RoleType.IDENTIFICATION_CARD_MANAGER)));
    }

    private PhdIndividualProgramProcess find(final Collection<PhdIndividualProgramProcess> phdIndividualProgramProcesses) {
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

    private PhdIndividualProgramProcess find(final Person person) {
        final InsuranceEvent event = person.getInsuranceEventFor(ExecutionYear.readCurrentExecutionYear());
        return event != null && event.isClosed() ? find(person.getPhdIndividualProgramProcesses()) : null;
    }

    private boolean hasMatchingLine(final Person person, final String line) {
        for (final CardGenerationEntry cardGenerationEntry : person.getCardGenerationEntriesSet()) {
            if (cardGenerationEntry.matches(line) && isInValidTimeFrame(cardGenerationEntry)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInValidTimeFrame(final CardGenerationEntry cardGenerationEntry) {
        final String expirationDate = cardGenerationEntry.getExpirationDate();
        final DateTime now = new DateTime();
        final int year = (now.getCenturyOfEra() * 100) + Integer.parseInt(expirationDate.substring(0, 2));
        final int month = Integer.parseInt(expirationDate.substring(2, 4));
        return year > now.getYear() || (year == now.getYear() && month >= now.getMonthOfYear());
    }

//    private boolean isInValidTimeFrame(final ExecutionYear executionYear) {
//	return executionYear.isCurrent() || isInValidTimeFrameLevel1(executionYear.getNextExecutionYear());
//    }
//
//    private boolean isInValidTimeFrameLevel1(final ExecutionYear executionYear) {
//	return executionYear != null && (executionYear.isCurrent() || isInValidTimeFrameLevel2(executionYear.getNextExecutionYear()));
//    }
//
//    private boolean isInValidTimeFrameLevel2(final ExecutionYear executionYear) {
//	return executionYear != null && executionYear.isCurrent();
//    }

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

    private static StudentCurricularPlan findStudentCurricularPlan(final CardGenerationBatch cardGenerationBatch,
            final Student student, final DateTime begin, final DateTime end) {
        final Set<StudentCurricularPlan> studentCurricularPlans =
                cardGenerationBatch.getStudentCurricularPlans(begin, end, student);
        if (studentCurricularPlans.size() == 1) {
            return studentCurricularPlans.iterator().next();
        } else if (studentCurricularPlans.size() > 1) {
            final StudentCurricularPlan max = findMaxStudentCurricularPlan(studentCurricularPlans);
            return max;
        }
        return null;
    }

}
