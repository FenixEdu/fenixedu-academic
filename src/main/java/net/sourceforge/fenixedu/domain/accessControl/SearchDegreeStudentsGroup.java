package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.NobodyGroup;
import org.fenixedu.bennu.core.domain.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;

public class SearchDegreeStudentsGroup extends Group {

    private static final long serialVersionUID = -1670838875686375271L;

    private DegreeCurricularPlan degreeCurricularPlan;

    private ExecutionYear executionYear;

    private String sortBy = null;

    private RegistrationStateType registrationStateType;

    private Double minGrade;

    private Double maxGrade;

    private Double minNumberApproved;

    private Double maxNumberApproved;

    private Double minStudentNumber;

    private Double maxStudentNumber;

    private Integer minimumYear;

    private Integer maximumYear;

    public SearchDegreeStudentsGroup(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionYear executionYear,
            final String sortBy, final RegistrationStateType registrationStateType, final Double minGrade, final Double maxGrade,
            final Double minNumberApproved, final Double maxNumberApproved, final Double minStudentNumber,
            final Double maxStudentNumber, final Integer minimumYear, final Integer maximumYear) {
        this.degreeCurricularPlan = degreeCurricularPlan;
        this.executionYear = executionYear;
        this.sortBy = sortBy;
        this.registrationStateType = registrationStateType;
        this.minGrade = minGrade;
        this.maxGrade = maxGrade;
        this.minNumberApproved = minNumberApproved;
        this.maxNumberApproved = maxNumberApproved;
        this.minStudentNumber = minStudentNumber;
        this.maxStudentNumber = maxStudentNumber;
        this.minimumYear = minimumYear;
        this.maximumYear = maximumYear;
    }

    public RegistrationStateType getRegistrationStateType() {
        return registrationStateType;
    }

    public void setRegistrationStateType(RegistrationStateType state) {
        this.registrationStateType = state;
    }

    public Double getMinGrade() {
        return minGrade;
    }

    public String getMinGradeString() {
        return minGrade != null ? String.valueOf(minGrade) : "ND";
    }

    public void setMinGrade(Double minGrade) {
        this.minGrade = minGrade;
    }

    public Double getMaxGrade() {
        return maxGrade;
    }

    public String getMaxGradeString() {
        return maxGrade != null ? String.valueOf(maxGrade) : "ND";
    }

    public void setMaxGrade(Double maxGrade) {
        this.maxGrade = maxGrade;
    }

    public Double getMinNumberApproved() {
        return minNumberApproved;
    }

    public String getMinNumberApprovedString() {
        return minNumberApproved != null ? String.valueOf(minNumberApproved) : "ND";
    }

    public void setMinNumberApproved(Double minNumberApproved) {
        this.minNumberApproved = minNumberApproved;
    }

    public Double getMaxNumberApproved() {
        return maxNumberApproved;
    }

    public String getMaxNumberApprovedString() {
        return maxNumberApproved != null ? String.valueOf(maxNumberApproved) : "ND";
    }

    public void setMaxNumberApproved(Double maxNumberApproved) {
        this.maxNumberApproved = maxNumberApproved;
    }

    public Double getMinStudentNumber() {
        return minStudentNumber;
    }

    public String getMinStudentNumberString() {
        return minStudentNumber != null ? String.valueOf(minStudentNumber) : "ND";
    }

    public void setMinStudentNumber(Double minStudentNumber) {
        this.minStudentNumber = minStudentNumber;
    }

    public Double getMaxStudentNumber() {
        return maxStudentNumber;
    }

    public String getMaxStudentNumberString() {
        return maxStudentNumber != null ? String.valueOf(maxStudentNumber) : "ND";
    }

    public void setMaxStudentNumber(Double maxStudentNumber) {
        this.maxStudentNumber = maxStudentNumber;
    }

    public Integer getMinimumYear() {
        return minimumYear;
    }

    public String getMinimumYearString() {
        return minimumYear != null ? String.valueOf(minimumYear) : "ND";
    }

    public void setMinimumYear(Integer minimumYear) {
        this.minimumYear = minimumYear;
    }

    public Integer getMaximumYear() {
        return maximumYear;
    }

    public String getMaximumYearString() {
        return maximumYear != null ? String.valueOf(maximumYear) : "ND";
    }

    public void setMaximumYear(Integer maximumYear) {
        this.maximumYear = maximumYear;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return this.degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degree) {
        this.degreeCurricularPlan = degree;
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public Set<Person> getElements() {
        final Set<Person> elements = super.buildSet();
        final Map<StudentCurricularPlan, RegistrationStateType> students = searchStudentCurricularPlans(null, null);
        for (StudentCurricularPlan student : students.keySet()) {
            elements.add(student.getPerson());
        }
        return elements;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getDegreeCurricularPlan()), new OidOperator(getExecutionYear()),
                new StaticArgument(getSortBy()), new StaticArgument(getRegistrationStateType()),
                new StaticArgument(getMinGrade()), new StaticArgument(getMaxGrade()), new StaticArgument(getMinNumberApproved()),
                new StaticArgument(getMaxNumberApproved()), new StaticArgument(getMinStudentNumber()),
                new StaticArgument(getMaxStudentNumber()), new StaticArgument(getMinimumYear()),
                new StaticArgument(getMaximumYear()) };
    }

    private Comparator<StudentCurricularPlan> determineComparatorKind() {
        final String sortBy;
        if (getSortBy() != null && !getSortBy().equalsIgnoreCase("null")) {
            sortBy = getSortBy();
        } else {
            sortBy = "student.number";
        }

        if (sortBy.equals("registration.average")) {
            return new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(StudentCurricularPlan left, StudentCurricularPlan right) {
                    if (isConcludedAndRegistrationConclusionProcessed(left.getRegistration())
                            && isConcludedAndRegistrationConclusionProcessed(right.getRegistration())) {
                        return left.getRegistration().getAverage().compareTo(right.getRegistration().getAverage());
                    }

                    if (isConcludedAndRegistrationConclusionProcessed(left.getRegistration())) {
                        return 1;
                    }

                    if (isConcludedAndRegistrationConclusionProcessed(right.getRegistration())) {
                        return -1;
                    }

                    if (left.getRegistration().isConcluded() && right.getRegistration().isConcluded()) {
                        return left.getExternalId().compareTo(right.getExternalId());
                    }

                    if (left.getRegistration().isConcluded()) {
                        return -1;
                    }

                    if (right.getRegistration().isConcluded()) {
                        return 1;
                    }

                    int result = left.getRegistration().getAverage().compareTo(right.getRegistration().getAverage());
                    return result == 0 ? left.getRegistration().getStudent().getNumber()
                            .compareTo(right.getRegistration().getStudent().getNumber()) : result;

                }

                private boolean isConcludedAndRegistrationConclusionProcessed(final Registration registration) {
                    return registration.isConcluded() && registration.isRegistrationConclusionProcessed();

                }
            };
        } else if (sortBy.equals("currentState")) {
            return new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(StudentCurricularPlan left, StudentCurricularPlan right) {
                    int result =
                            left.getRegistration()
                                    .getLastRegistrationState(getExecutionYear())
                                    .getStateType()
                                    .getDescription()
                                    .compareTo(
                                            right.getRegistration().getLastRegistrationState(getExecutionYear()).getStateType()
                                                    .getDescription());
                    return result == 0 ? left.getRegistration().getStudent().getNumber()
                            .compareTo(right.getRegistration().getStudent().getNumber()) : result;
                }
            };

        } else if (sortBy.equals("registration.person.name")) {
            return new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(StudentCurricularPlan left, StudentCurricularPlan right) {
                    int result =
                            left.getRegistration().getPerson().getName().compareTo(right.getRegistration().getPerson().getName());
                    return result == 0 ? left.getRegistration().getStudent().getNumber()
                            .compareTo(right.getRegistration().getStudent().getNumber()) : result;
                }
            };

        } else if (sortBy.equals("student.number")) {
            return new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(StudentCurricularPlan left, StudentCurricularPlan right) {
                    return left.getRegistration().getStudent().getNumber()
                            .compareTo(right.getRegistration().getStudent().getNumber());
                }
            };
        } else if (sortBy.equals("registration.person.email")) {
            return new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(StudentCurricularPlan left, StudentCurricularPlan right) {
                    if (left.getRegistration().getPerson().getDefaultEmailAddress() == null
                            && right.getRegistration().getPerson().getDefaultEmailAddress() == null) {
                        return left.getRegistration().getStudent().getNumber()
                                .compareTo(right.getRegistration().getStudent().getNumber());
                    }
                    if (left.getRegistration().getPerson().getDefaultEmailAddress() == null) {
                        return -1;
                    }
                    if (right.getRegistration().getPerson().getDefaultEmailAddress() == null) {
                        return 1;
                    }
                    int result =
                            EmailAddress.COMPARATOR_BY_EMAIL.compare(left.getRegistration().getPerson().getDefaultEmailAddress(),
                                    right.getRegistration().getPerson().getDefaultEmailAddress());
                    if (result > 0) {
                        return 1;
                    }
                    if (result < 0) {
                        return -1;
                    }
                    return left.getRegistration().getStudent().getNumber()
                            .compareTo(right.getRegistration().getStudent().getNumber());
                }
            };
        } else if (sortBy.equals("registration.numberOfCurriculumEntries")) {
            return new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(StudentCurricularPlan left, StudentCurricularPlan right) {
                    if (left.getRegistration().getNumberOfCurriculumEntries() > right.getRegistration()
                            .getNumberOfCurriculumEntries()) {
                        return 1;
                    }
                    if (left.getRegistration().getNumberOfCurriculumEntries() < right.getRegistration()
                            .getNumberOfCurriculumEntries()) {
                        return -1;
                    }
                    return left.getRegistration().getStudent().getNumber()
                            .compareTo(right.getRegistration().getStudent().getNumber());
                }
            };
        } else if (sortBy.equals("registration.ectsCredits")) {
            return new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(StudentCurricularPlan left, StudentCurricularPlan right) {
                    if (left.getRegistration().getEctsCredits() > right.getRegistration().getEctsCredits()) {
                        return 1;
                    }
                    if (left.getRegistration().getEctsCredits() < right.getRegistration().getEctsCredits()) {
                        return -1;
                    }
                    return left.getRegistration().getStudent().getNumber()
                            .compareTo(right.getRegistration().getStudent().getNumber());
                }
            };
        } else if (sortBy.equals("registration.curricularYear")) {
            return new Comparator<StudentCurricularPlan>() {
                @Override
                public int compare(StudentCurricularPlan left, StudentCurricularPlan right) {
                    if (left.getRegistration().getCurricularYear() > right.getRegistration().getCurricularYear()) {
                        return 1;
                    }
                    if (left.getRegistration().getCurricularYear() < right.getRegistration().getCurricularYear()) {
                        return -1;
                    }
                    return left.getRegistration().getStudent().getNumber()
                            .compareTo(right.getRegistration().getStudent().getNumber());
                }
            };
        }

        return null;
    }

    public Map<StudentCurricularPlan, RegistrationStateType> searchStudentCurricularPlans(Integer minIndex, Integer maxIndex) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
        for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlans()) {
            if (matchesSelectCriteria(studentCurricularPlan)) {
                studentCurricularPlans.add(studentCurricularPlan);
            }
        }

        Comparator<StudentCurricularPlan> comparator = determineComparatorKind();
        Map<StudentCurricularPlan, RegistrationStateType> map =
                new TreeMap<StudentCurricularPlan, RegistrationStateType>(comparator);

        if (minIndex != null || maxIndex != null) {
            for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans.subList(minIndex - 1,
                    Math.min(maxIndex, studentCurricularPlans.size()))) {
                map.put(studentCurricularPlan, null);
            }
        } else {
            for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
                map.put(studentCurricularPlan, null);
            }
        }
        return map;
    }

    public boolean matchesSelectCriteria(final StudentCurricularPlan studentCurricularPlan) {
        if (!studentCurricularPlan.hasRegistration() || studentCurricularPlan.getRegistration().isTransition()) {
            return false;
        }

        if (registrationStateType == null) {
            return true;
        }

        final RegistrationState lastRegistrationState =
                studentCurricularPlan.getRegistration().getLastRegistrationState(executionYear);
        if (lastRegistrationState == null || lastRegistrationState.getStateType() != registrationStateType) {
            return false;
        }

        final double arithmeticMean = studentCurricularPlan.getRegistration().getArithmeticMean();

        if (minGrade != null && minGrade > arithmeticMean) {
            return false;
        }

        if (maxGrade != null && maxGrade < arithmeticMean) {
            return false;
        }

        final int approvedEnrollmentsNumber = studentCurricularPlan.getRegistration().getNumberOfCurriculumEntries();

        if (minNumberApproved != null && minNumberApproved > approvedEnrollmentsNumber) {
            return false;
        }

        if (maxNumberApproved != null && maxNumberApproved < approvedEnrollmentsNumber) {
            return false;
        }

        final int studentNumber = studentCurricularPlan.getRegistration().getNumber();

        if (minStudentNumber != null && minStudentNumber > studentNumber) {
            return false;
        }

        if (maxStudentNumber != null && maxStudentNumber < studentNumber) {
            return false;
        }

        final int curricularYear = studentCurricularPlan.getRegistration().getCurricularYear();

        if (minimumYear != null && minimumYear > curricularYear) {
            return false;
        }

        if (maximumYear != null && maximumYear < curricularYear) {
            return false;
        }

        return true;
    }

    public String getApplicationResourcesString(String name) {
        return ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale()).getString(name);
    }

    public String getLabel() {
        String label = new String();
        label =
                String.format("%s : %s \n%s : %s \n%s : %s \n%s : %s \n%s : %s",
                        getApplicationResourcesString("label.selectStudents"), getRegistrationStateType().getDescription(),
                        getApplicationResourcesString("label.student.number"), getMinStudentNumberString() + " - "
                                + getMaxStudentNumberString(), getApplicationResourcesString("label.average"),
                        getMinGradeString() + " - " + getMaxGradeString(),
                        getApplicationResourcesString("label.number.approved.curricular.courses"), getMinNumberApprovedString()
                                + " - " + getMaxNumberApprovedString(),
                        getApplicationResourcesString("label.student.curricular.year"), getMinimumYearString() + " - "
                                + getMaximumYearString());
        return label;
    }

    public static class Builder implements GroupBuilder {

        private void verifyArgs(Object[] arguments) {
            for (int i = 4; i < 10; i++) {
                String arg = (String) arguments[i];
                if (arg.equalsIgnoreCase("null")) {
                    arguments[i] = null;
                } else if (i < 10) {
                    arguments[i] = Double.valueOf(arg);
                }
            }
            for (int i = 10; i < 12; i++) {
                if (arguments[i] instanceof String) {
                    arguments[i] = null;
                }
            }
        }

        @Override
        public Group build(Object[] arguments) {
            verifyArgs(arguments);
            try {
                return new SearchDegreeStudentsGroup((DegreeCurricularPlan) arguments[0], (ExecutionYear) arguments[1],
                        (String) arguments[2], RegistrationStateType.valueOf((String) arguments[3]), (Double) arguments[4],
                        (Double) arguments[5], (Double) arguments[6], (Double) arguments[7], (Double) arguments[8],
                        (Double) arguments[9], (Integer) arguments[10], (Integer) arguments[11]);
            } catch (final ClassCastException e) {
                throw new Error(e);
            }
        }

        @Override
        public int getMinArguments() {
            return 12;
        }

        @Override
        public int getMaxArguments() {
            return 12;
        }

    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        ImmutableSet<User> users = FluentIterable.from(getElements()).filter(new Predicate<Person>() {
            @Override
            public boolean apply(Person person) {
                return person.getUser() != null;
            }
        }).transform(new Function<Person, User>() {
            @Override
            public User apply(Person person) {
                return person.getUser();
            }
        }).toSet();
        if (users.isEmpty()) {
            return NobodyGroup.getInstance();
        }
        return UserGroup.getInstance(users);
    }
}