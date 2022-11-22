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
package org.fenixedu.academic.domain.accessControl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateTypeEnum;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.common.base.Joiner;

import pt.ist.fenixframework.FenixFramework;

public class SearchDegreeStudentsGroup implements Serializable {

    private static final long serialVersionUID = -1670838875686375271L;

    private DegreeCurricularPlan degreeCurricularPlan;

    private ExecutionYear executionYear;

    private String sortBy = null;

    private RegistrationStateTypeEnum registrationStateType;

    private Double minGrade;

    private Double maxGrade;

    private Double minNumberApproved;

    private Double maxNumberApproved;

    private Double minStudentNumber;

    private Double maxStudentNumber;

    private Integer minimumYear;

    private Integer maximumYear;

    public SearchDegreeStudentsGroup(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionYear executionYear,
            final String sortBy, final RegistrationStateTypeEnum registrationStateType, final Double minGrade, final Double maxGrade,
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

    public RegistrationStateTypeEnum getRegistrationStateType() {
        return registrationStateType;
    }

    public void setRegistrationStateType(RegistrationStateTypeEnum state) {
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
        return sortBy != null ? sortBy : "student.number";
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Set<Person> getElements() {
        final Set<Person> elements = new HashSet<>();
        final Map<StudentCurricularPlan, RegistrationStateTypeEnum> students = searchStudentCurricularPlans(null, null);
        for (StudentCurricularPlan student : students.keySet()) {
            elements.add(student.getPerson());
        }
        return elements;
    }

    public Group getUserGroup() {
        final Map<StudentCurricularPlan, RegistrationStateTypeEnum> students = searchStudentCurricularPlans(null, null);
        return Group.users(students.keySet().stream().map(scp -> scp.getPerson().getUser()).filter(Objects::nonNull));
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
                        return left.getRegistration().getRawGrade().compareTo(right.getRegistration().getRawGrade());
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

                    int result = left.getRegistration().getRawGrade().compareTo(right.getRegistration().getRawGrade());
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
                    int result = left.getRegistration().getLastRegistrationState(getExecutionYear()).getStateTypeEnum()
                            .getDescription().compareTo(right.getRegistration().getLastRegistrationState(getExecutionYear())
                                    .getStateTypeEnum().getDescription());
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

    public Map<StudentCurricularPlan, RegistrationStateTypeEnum> searchStudentCurricularPlans(Integer minIndex, Integer maxIndex) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
        for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
            if (matchesSelectCriteria(studentCurricularPlan)) {
                studentCurricularPlans.add(studentCurricularPlan);
            }
        }

        Comparator<StudentCurricularPlan> comparator = determineComparatorKind();
        Map<StudentCurricularPlan, RegistrationStateTypeEnum> map =
                new TreeMap<StudentCurricularPlan, RegistrationStateTypeEnum>(comparator);

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
        if (!studentCurricularPlan.hasRegistration()) {
            return false;
        }

        if (registrationStateType == null) {
            return true;
        }

        final RegistrationState lastRegistrationState =
                studentCurricularPlan.getRegistration().getLastRegistrationState(executionYear);
        if (lastRegistrationState == null || lastRegistrationState.getStateTypeEnum() != registrationStateType) {
            return false;
        }

        final double arithmeticMean =
                studentCurricularPlan.getRegistration().getCurriculum().getRawGrade().getNumericValue().doubleValue();

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
        return BundleUtil.getString(Bundle.APPLICATION, name);
    }

    public String getLabel() {
        String label = new String();
        label = String.format("%s : %s \n%s : %s \n%s : %s \n%s : %s \n%s : %s",
                getApplicationResourcesString("label.selectStudents"), getRegistrationStateType().getDescription(),
                getApplicationResourcesString("label.student.number"),
                getMinStudentNumberString() + " - " + getMaxStudentNumberString(), getApplicationResourcesString("label.average"),
                getMinGradeString() + " - " + getMaxGradeString(),
                getApplicationResourcesString("label.number.approved.curricular.courses"),
                getMinNumberApprovedString() + " - " + getMaxNumberApprovedString(),
                getApplicationResourcesString("label.student.curricular.year"),
                getMinimumYearString() + " - " + getMaximumYearString());
        return label;
    }

    public static SearchDegreeStudentsGroup parse(String serialized) {
        String[] parts = serialized.split(":");
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(parts[0]);
        ExecutionYear executionYear = FenixFramework.getDomainObject(parts[1]);
        String sortBy = parts[2];
        RegistrationStateTypeEnum registrationStateType = parts[3].equals("ND") ? null : RegistrationStateTypeEnum.valueOf(parts[3]);
        Double minGrade = parts[4].equals("ND") ? null : Double.valueOf(parts[4]);
        Double maxGrade = parts[5].equals("ND") ? null : Double.valueOf(parts[5]);
        Double minNumberApproved = parts[6].equals("ND") ? null : Double.valueOf(parts[6]);
        Double maxNumberApproved = parts[7].equals("ND") ? null : Double.valueOf(parts[7]);
        Double minStudentNumber = parts[8].equals("ND") ? null : Double.valueOf(parts[8]);
        Double maxStudentNumber = parts[9].equals("ND") ? null : Double.valueOf(parts[9]);
        Integer minimumYear = parts[10].equals("ND") ? null : Integer.valueOf(parts[10]);
        Integer maximumYear = parts[11].equals("ND") ? null : Integer.valueOf(parts[11]);
        return new SearchDegreeStudentsGroup(degreeCurricularPlan, executionYear, sortBy, registrationStateType, minGrade,
                maxGrade, minNumberApproved, maxNumberApproved, minStudentNumber, maxStudentNumber, minimumYear, maximumYear);
    }

    public String serialize() {
        List<String> parts = new ArrayList<>();
        parts.add(getDegreeCurricularPlan().getExternalId());
        parts.add(getExecutionYear().getExternalId());
        parts.add(getSortBy());
        parts.add(getRegistrationStateType() != null ? getRegistrationStateType().getName() : "ND");
        parts.add(getMinGradeString());
        parts.add(getMaxGradeString());
        parts.add(getMinNumberApprovedString());
        parts.add(getMaxNumberApprovedString());
        parts.add(getMinStudentNumberString());
        parts.add(getMaxStudentNumberString());
        parts.add(getMinimumYearString());
        parts.add(getMaximumYearString());
        return Joiner.on(':').join(parts);
    }

//  @Override
//  protected Argument[] getExpressionArguments() {
//      return new Argument[] { new OidOperator(getDegreeCurricularPlan()), new OidOperator(getExecutionYear()),
//              new StaticArgument(getSortBy()), new StaticArgument(getRegistrationStateType()),
//              new StaticArgument(getMinGrade()), new StaticArgument(getMaxGrade()), new StaticArgument(getMinNumberApproved()),
//              new StaticArgument(getMaxNumberApproved()), new StaticArgument(getMinStudentNumber()),
//              new StaticArgument(getMaxStudentNumber()), new StaticArgument(getMinimumYear()),
//              new StaticArgument(getMaximumYear()) };
//  }

}