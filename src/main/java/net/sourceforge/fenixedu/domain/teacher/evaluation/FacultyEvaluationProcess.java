package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FacultyEvaluationProcess extends FacultyEvaluationProcess_Base implements Comparable<FacultyEvaluationProcess> {

    public static Comparator<FacultyEvaluationProcess> COMPARATOR_BY_INTERVAL = new Comparator<FacultyEvaluationProcess>() {
        @Override
        public int compare(FacultyEvaluationProcess p1, FacultyEvaluationProcess p2) {
            if (p1.getAutoEvaluationInterval().getStart().compareTo(p2.getAutoEvaluationInterval().getStart()) != 0) {
                return -p1.getAutoEvaluationInterval().getStart().compareTo(p2.getAutoEvaluationInterval().getStart());
            }
            if (p1.getTitle().compareTo(p2.getTitle()) != 0) {
                return p1.getTitle().compareTo(p2.getTitle());
            }
            return p1.getExternalId().compareTo(p2.getExternalId());
        }
    };

    public FacultyEvaluationProcess() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setAreApprovedMarksPublished(false);
    }

    public FacultyEvaluationProcess(final MultiLanguageString title, final Interval autoEvaluationInterval,
            final Interval evaluationInterval) {
        this();
        if (title == null || !title.hasContent()) {
            throw new DomainException("error.title.cannot.be.null");
        }
        setTitle(title);
        setAutoEvaluationInterval(autoEvaluationInterval);
        setEvaluationInterval(evaluationInterval);
    }

    public void uploadEvaluators(final byte[] bytes) {
        final String contents = new String(bytes);
        final String[] lines = contents.split("[\\r\\n]+");
        int lineNumber = 0;
        final StringBuilder stringBuilder = new StringBuilder();
        final Set<Person> evaluees = new HashSet<Person>();
        for (final String line : lines) {
            lineNumber++;
            final String[] parts = line.split("\t");
            if (parts.length < 2) {
                throw new DomainException("error.invalid.file.format");
            }
            final String evaluee = parts[0];
            final String evaluator = parts[1];
            final String coevaluator = parts.length > 2 ? parts[2] : null;
            final String coevaluatorString = parts.length > 3 ? parts[3].trim() : null;

            final Person evalueePerson = findPerson(evaluee);
            final Person evaluatorPerson = findPerson(evaluator);
            if (evalueePerson == null) {
                appendMessage(stringBuilder, lineNumber, "error.evaluee.not.found", new String[] { evaluee });
            } else {
                if (evaluees.contains(evalueePerson)) {
                    appendMessage(stringBuilder, lineNumber, "error.evaluee.duplicate", new String[] { evaluee });
                } else {
                    evaluees.add(evalueePerson);
                }
            }
            if (evaluatorPerson == null) {
                appendMessage(stringBuilder, lineNumber, "error.evaluator.not.found", new String[] { evaluator });
            }
            if (evalueePerson != null && evaluatorPerson != null) {
                final Person coEvaluatorPerson = findPerson(coevaluator);
                TeacherEvaluationProcess existingTeacherEvaluationProcess = null;
                for (final TeacherEvaluationProcess teacherEvaluationProcess : evalueePerson
                        .getTeacherEvaluationProcessFromEvalueeSet()) {
                    if (teacherEvaluationProcess.getFacultyEvaluationProcess() == this) {
                        existingTeacherEvaluationProcess = teacherEvaluationProcess;
                        break;
                    }
                }
                if (existingTeacherEvaluationProcess == null) {
                    existingTeacherEvaluationProcess = new TeacherEvaluationProcess(this, evalueePerson, evaluatorPerson);
                } else {
                    if (evaluatorPerson != existingTeacherEvaluationProcess.getEvaluator()) {
                        existingTeacherEvaluationProcess.setEvaluator(evaluatorPerson);

                        final TeacherEvaluation teacherEvaluation =
                                existingTeacherEvaluationProcess.getCurrentTeacherEvaluation();
                        if (teacherEvaluation != null) {
                            teacherEvaluation.copyAutoEvaluation();
                        }
                    }
                }

                boolean updatedCoEvaluator = false;
                boolean updatedCoEvaluatorString = false;
                for (final TeacherEvaluationCoEvaluator teacherEvaluationCoEvaluator : existingTeacherEvaluationProcess
                        .getTeacherEvaluationCoEvaluatorSet()) {
                    if (teacherEvaluationCoEvaluator instanceof InternalCoEvaluator) {
                        final InternalCoEvaluator internalCoEvaluator = (InternalCoEvaluator) teacherEvaluationCoEvaluator;
                        updatedCoEvaluator = true;
                        if (coEvaluatorPerson == null) {
                            internalCoEvaluator.delete();
                        } else {
                            internalCoEvaluator.setPerson(coEvaluatorPerson);
                        }
                    } else if (teacherEvaluationCoEvaluator instanceof ExternalCoEvaluator) {
                        final ExternalCoEvaluator externalCoEvaluator = (ExternalCoEvaluator) teacherEvaluationCoEvaluator;
                        updatedCoEvaluatorString = true;
                        if (coevaluatorString == null || coevaluatorString.isEmpty()) {
                            externalCoEvaluator.delete();
                        } else {
                            externalCoEvaluator.setName(coevaluatorString);
                        }
                    } else {
                        throw new DomainException("unknown type: " + teacherEvaluationCoEvaluator.getClass().getName());
                    }
                }
                if (coEvaluatorPerson != null && !updatedCoEvaluator) {
                    new InternalCoEvaluator(existingTeacherEvaluationProcess, coEvaluatorPerson);
                }
                if (coevaluatorString != null && !coevaluatorString.isEmpty() && !updatedCoEvaluatorString) {
                    new ExternalCoEvaluator(existingTeacherEvaluationProcess, coevaluatorString);
                }
            }
        }
        if (stringBuilder.length() > 0) {
            throw new DomainException("error.invalid.file.contents", stringBuilder.toString());
        }
    }

    private void appendMessage(final StringBuilder stringBuilder, final int lineNumber, final String key, final String[] args) {
        final String description = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", key, args);
        final String message =
                BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "error.invalid.file.contents.line",
                        Integer.toString(lineNumber), description);
        stringBuilder.append("\n\t");
        stringBuilder.append(message);
    }

    private Person findPerson(final String string) {
        if (string != null) {
            final User user = User.findByUsername(string);
            if (user != null) {
                return user.getPerson();
            }
            if (!string.isEmpty() && StringUtils.isNumeric(string)) {
                final int number = Integer.parseInt(string);
                if (number > 0) {
                    Employee employee = Employee.readByNumber(new Integer(number));
                    if (employee != null && employee.hasPerson() && employee.getPerson().hasTeacher()) {
                        return employee.getPerson();
                    }
                }
            }
        }
        return null;
    }

    public SortedSet<TeacherEvaluationProcess> getSortedTeacherEvaluationProcess() {
        final SortedSet<TeacherEvaluationProcess> result =
                new TreeSet<TeacherEvaluationProcess>(TeacherEvaluationProcess.COMPARATOR_BY_EVALUEE);
        result.addAll(getTeacherEvaluationProcessSet());
        return result;
    }

    public TeacherEvaluationState getState() {
        if (getAutoEvaluationInterval().isAfterNow()) {
            return null;
        } else {
            return TeacherEvaluationState.AUTO_EVALUATION;
        }
    }

    public int getAutoEvaluatedCount() {
        int count = 0;
        for (final TeacherEvaluationProcess teacherEvaluationProcess : getTeacherEvaluationProcessSet()) {
            final TeacherEvaluation currentTeacherEvaluation = teacherEvaluationProcess.getCurrentTeacherEvaluation();
            if (currentTeacherEvaluation != null && currentTeacherEvaluation.getAutoEvaluationLock() != null) {
                count++;
            }
        }
        return count;
    }

    public int getEvaluatedCount() {
        int count = 0;
        for (final TeacherEvaluationProcess teacherEvaluationProcess : getTeacherEvaluationProcessSet()) {
            final TeacherEvaluation currentTeacherEvaluation = teacherEvaluationProcess.getCurrentTeacherEvaluation();
            if (currentTeacherEvaluation != null && currentTeacherEvaluation.getEvaluationLock() != null) {
                count++;
            }
        }
        return count;
    }

    public void uploadApprovedEvaluations(byte[] bytes) {
        final StringBuilder stringBuilder = new StringBuilder();

        final String contents = new String(bytes);
        final String[] lines = contents.split("\n");
        int lineNumber = 0;

        FacultyEvaluationProcessYear[] yearIndex = null;
        for (final String line : lines) {
            lineNumber++;
            final String[] parts = line.split("\t");

            if (lineNumber == 1) {
                yearIndex = new FacultyEvaluationProcessYear[parts.length - 2];
                for (int i = 2; i < parts.length; i++) {
                    final String year = parts[i];
                    yearIndex[i - 2] = createFacultyEvaluationProcessYear(year);
                }
            } else {
                final String evaluee = parts[0];
                final TeacherEvaluationProcess teacherEvaluationProcess = getTeacherEvaluationProcess(evaluee);
                if (teacherEvaluationProcess != null) {
                    for (int i = 2; i < parts.length; i++) {
                        final String mark = parts[i];
                        final FacultyEvaluationProcessYear facultyEvaluationProcessYear = yearIndex[i - 2];
                        final TeacherEvaluationMark teacherEvaluationMark = parseMark(mark);
                        teacherEvaluationProcess.setApprovedTeacherEvaluationProcessMark(facultyEvaluationProcessYear,
                                teacherEvaluationMark);
                    }
                } else {
                    final String message =
                            BundleUtil.getStringFromResourceBundle("resources/ResearcherResources",
                                    "error.evaluee.has.no.process", evaluee);
                    stringBuilder.append(message);
                    stringBuilder.append('\n');
                }
            }
        }

        if (stringBuilder.length() > 0) {
            throw new DomainException("error.invalid.file.contents", stringBuilder.toString());
        }
    }

    private TeacherEvaluationMark parseMark(final String mark) {
        if (!StringUtils.isEmpty(mark)) {
            String trimmedMark = mark.trim();
            if ("Excelente".equals(trimmedMark)) {
                return TeacherEvaluationMark.EXCELLENT;
            }
            if ("Muito Bom".equals(trimmedMark)) {
                return TeacherEvaluationMark.VERY_GOOD;
            }
            if ("Bom".equals(trimmedMark)) {
                return TeacherEvaluationMark.GOOD;
            }
            if ("N/A".equals(trimmedMark)) {
                return null;
            }
            if ("Inadequado".equals(trimmedMark)) {
                return TeacherEvaluationMark.INADEQUATE;
            }
        }
        throw new DomainException("error.unknown.mark.value", mark);
    }

    private TeacherEvaluationProcess getTeacherEvaluationProcess(final String evaluee) {
        for (final TeacherEvaluationProcess teacherEvaluationProcess : getTeacherEvaluationProcessSet()) {
            final Person person = teacherEvaluationProcess.getEvaluee();
            final String username = person.getUsername();
            if (username.equals(evaluee)) {
                return teacherEvaluationProcess;
            }
            final Teacher teacher = person.getTeacher();
            if (teacher != null) {
                final Integer number = teacher.getPerson().getEmployee().getEmployeeNumber();
                if (number.toString().equals(evaluee)) {
                    return teacherEvaluationProcess;
                }
            }
        }
        return null;
    }

    private FacultyEvaluationProcessYear getFacultyEvaluationProcessYear(final String year) {
        for (final FacultyEvaluationProcessYear facultyEvaluationProcessYear : getFacultyEvaluationProcessYearSet()) {
            if (facultyEvaluationProcessYear.getYear().equals(year)) {
                return facultyEvaluationProcessYear;
            }
        }
        return null;
    }

    private FacultyEvaluationProcessYear createFacultyEvaluationProcessYear(final String year) {
        final FacultyEvaluationProcessYear facultyEvaluationProcessYear = getFacultyEvaluationProcessYear(year);
        return facultyEvaluationProcessYear == null ? new FacultyEvaluationProcessYear(this, year) : facultyEvaluationProcessYear;
    }

    @Atomic
    public void delete() {
        for (final TeacherEvaluationProcess teacherEvaluationProcess : getTeacherEvaluationProcessSet()) {
            teacherEvaluationProcess.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public int getApprovedEvaluatedCount() {
        int result = 0;
        for (final TeacherEvaluationProcess teacherEvaluationProcess : getTeacherEvaluationProcessSet()) {
            if (teacherEvaluationProcess.hasAnyApprovedTeacherEvaluationProcessMark()) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int compareTo(FacultyEvaluationProcess o) {
        return COMPARATOR_BY_INTERVAL.compare(this, o);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess> getTeacherEvaluationProcess() {
        return getTeacherEvaluationProcessSet();
    }

    @Deprecated
    public boolean hasAnyTeacherEvaluationProcess() {
        return !getTeacherEvaluationProcessSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessYear> getFacultyEvaluationProcessYear() {
        return getFacultyEvaluationProcessYearSet();
    }

    @Deprecated
    public boolean hasAnyFacultyEvaluationProcessYear() {
        return !getFacultyEvaluationProcessYearSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSuffix() {
        return getSuffix() != null;
    }

    @Deprecated
    public boolean hasAutoEvaluationInterval() {
        return getAutoEvaluationInterval() != null;
    }

    @Deprecated
    public boolean hasEvaluationInterval() {
        return getEvaluationInterval() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

}
