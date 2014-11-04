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
package org.fenixedu.academic.domain.teacher.evaluation;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class TeacherEvaluationProcess extends TeacherEvaluationProcess_Base {

    public static Comparator<TeacherEvaluationProcess> COMPARATOR_BY_EVALUEE = new Comparator<TeacherEvaluationProcess>() {
        @Override
        public int compare(TeacherEvaluationProcess p1, TeacherEvaluationProcess p2) {
            final int i = Collator.getInstance().compare(p1.getEvaluee().getName(), p2.getEvaluee().getName());
            return i == 0 ? p2.hashCode() - p1.hashCode() : i;
        }
    };

    public static Comparator<TeacherEvaluationProcess> COMPARATOR_BY_INTERVAL = new Comparator<TeacherEvaluationProcess>() {
        @Override
        public int compare(TeacherEvaluationProcess p1, TeacherEvaluationProcess p2) {
            return FacultyEvaluationProcess.COMPARATOR_BY_INTERVAL.compare(p1.getFacultyEvaluationProcess(),
                    p2.getFacultyEvaluationProcess());
        }
    };

    public TeacherEvaluationProcess() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public TeacherEvaluationProcess(final FacultyEvaluationProcess facultyEvaluationProcess, final Person evaluee,
            final Person evaluator) {
        setFacultyEvaluationProcess(facultyEvaluationProcess);
        setEvaluee(evaluee);
        setEvaluator(evaluator);
        if (facultyEvaluationProcess.getAllowNoEval()) {
            new NoEvaluation(this);
        }
    }

    public TeacherEvaluation getCurrentTeacherEvaluation() {
        TeacherEvaluation last = null;
        for (TeacherEvaluation evaluation : getTeacherEvaluationSet()) {
            if (last == null || evaluation.getCreatedDate().isAfter(last.getCreatedDate())) {
                last = evaluation;
            }
        }
        return last;
    }

    public TeacherEvaluationState getState() {
        TeacherEvaluation current = getCurrentTeacherEvaluation();
        return current != null ? current.getState() : getFacultyEvaluationProcess().getState();
    }

    public TeacherEvaluationType getType() {
        TeacherEvaluation current = getCurrentTeacherEvaluation();
        return current != null ? current.getType() : null;
    }

    public boolean isAutoEvaluationLocked() {
        TeacherEvaluation current = getCurrentTeacherEvaluation();
        return current != null && current.getAutoEvaluationLock() != null;
    }

    public boolean isEvaluationLocked() {
        TeacherEvaluation current = getCurrentTeacherEvaluation();
        return current != null && current.getEvaluationLock() != null;
    }

    public TeacherEvaluationMark getAutoEvaluationMark() {
        TeacherEvaluation current = getCurrentTeacherEvaluation();
        return current != null ? current.getAutoEvaluationMark() : null;
    }

    public void setEvaluationMark(TeacherEvaluationMark mark) {
        getCurrentTeacherEvaluation().setEvaluationMark(mark);
    }

    public TeacherEvaluationMark getEvaluationMark() {
        TeacherEvaluation current = getCurrentTeacherEvaluation();
        return current != null ? current.getEvaluationMark() : null;
    }

    public boolean isInAutoEvaluationInterval() {
        return getFacultyEvaluationProcess().getAutoEvaluationInterval().containsNow();
    }

    public boolean isInEvaluationInterval() {
        return getFacultyEvaluationProcess().getEvaluationInterval().containsNow();
    }

    public boolean isInAutoEvaluation() {
        return isInAutoEvaluationInterval() && !isAutoEvaluationLocked();
    }

    public boolean isInEvaluation() {
        return isInEvaluationInterval()
                && isAutoEvaluationLocked()
                && !isEvaluationLocked()
                && (getEvaluator().equals(AccessControl.getPerson()) || AccessControl.getPerson()
                        .isTeacherEvaluationCoordinatorCouncilMember());
    }

    public boolean isPossibleToInsertApprovedMark() {
        return (isEvaluationLocked() || getFacultyEvaluationProcess().getEvaluationInterval().getEnd().isBeforeNow())
                && (AccessControl.getPerson().isTeacherEvaluationCoordinatorCouncilMember() || AccessControl.getPerson().hasRole(
                        RoleType.MANAGER));
    }

    public boolean isPossibleToLockAutoEvaluation() {
        return hasAllNeededFilesForAuto();
    }

    public boolean isPossibleToLockEvaluation() {
        return getEvaluationMark() != null
                && hasAllNeededFiles()
                && (getEvaluator().equals(AccessControl.getPerson()) || AccessControl.getPerson()
                        .isTeacherEvaluationCoordinatorCouncilMember());
    }

    public boolean isPossibleToViewEvaluation() {
        return isPossibleToInsertApprovedMark() || getEvaluator().equals(AccessControl.getPerson())
                || AccessControl.getPerson().isTeacherEvaluationCoordinatorCouncilMember();
    }

    public boolean isPossibleToUnlockAutoEvaluation() {
        return isAutoEvaluationLocked() && !isEvaluationLocked();
    }

    public boolean isPossibleToUnlockEvaluation() {
        return isEvaluationLocked();
    }

    public boolean isReadyForCCADConsideration() {
        return isEvaluationLocked() || getFacultyEvaluationProcess().getEvaluationInterval().getEnd().isBeforeNow();
    }

    public Set<TeacherEvaluationFileBean> getTeacherEvaluationFileBeanSet() {
        SortedSet<TeacherEvaluationFileBean> teacherEvaluationFileBeans =
                new TreeSet<TeacherEvaluationFileBean>(TeacherEvaluationFileBean.COMPARATOR_BY_TYPE);
        TeacherEvaluation currentTeacherEvaluation = getCurrentTeacherEvaluation();
        if (currentTeacherEvaluation != null) {
            if (isAutoEvaluationLocked()) {
                for (TeacherEvaluationFileType teacherEvaluationFileType : currentTeacherEvaluation.getAutoEvaluationFileSet()) {
                    TeacherEvaluationFileBean e =
                            new TeacherEvaluationFileBean(currentTeacherEvaluation, teacherEvaluationFileType);
                    teacherEvaluationFileBeans.add(e);
                }
                if (isPossibleToViewEvaluation()) {
                    for (TeacherEvaluationFileType teacherEvaluationFileType : currentTeacherEvaluation.getEvaluationFileSet()) {
                        TeacherEvaluationFileBean e =
                                new TeacherEvaluationFileBean(currentTeacherEvaluation, teacherEvaluationFileType);
                        teacherEvaluationFileBeans.add(e);
                    }
                }
            }
        }
        return teacherEvaluationFileBeans;
    }

    public Set<TeacherEvaluationFileBean> getTeacherAutoEvaluationFileBeanSet() {
        SortedSet<TeacherEvaluationFileBean> teacherEvaluationFileBeans =
                new TreeSet<TeacherEvaluationFileBean>(TeacherEvaluationFileBean.COMPARATOR_BY_TYPE);
        TeacherEvaluation currentTeacherEvaluation = getCurrentTeacherEvaluation();
        if (currentTeacherEvaluation != null) {
            for (TeacherEvaluationFileType teacherEvaluationFileType : currentTeacherEvaluation.getAutoEvaluationFileSet()) {
                TeacherEvaluationFileBean e = new TeacherEvaluationFileBean(currentTeacherEvaluation, teacherEvaluationFileType);
                teacherEvaluationFileBeans.add(e);
            }
            if (isEvaluationLocked()) {
                for (TeacherEvaluationFileType teacherEvaluationFileType : currentTeacherEvaluation.getEvaluationFileSet()) {
                    TeacherEvaluationFileBean e =
                            new TeacherEvaluationFileBean(currentTeacherEvaluation, teacherEvaluationFileType);
                    teacherEvaluationFileBeans.add(e);
                }
            }
        }
        return teacherEvaluationFileBeans;
    }

    private boolean hasAllNeededFilesForAuto() {
        TeacherEvaluation currentTeacherEvaluation = getCurrentTeacherEvaluation();
        Set<TeacherEvaluationFileType> files = new HashSet<TeacherEvaluationFileType>();
        if (currentTeacherEvaluation != null) {
            for (TeacherEvaluationFile teacherEvaluationFile : currentTeacherEvaluation.getTeacherEvaluationFileSet()) {
                files.add(teacherEvaluationFile.getTeacherEvaluationFileType());
            }
            if (files.containsAll(currentTeacherEvaluation.getAutoEvaluationFileSet())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAllNeededFiles() {
        TeacherEvaluation currentTeacherEvaluation = getCurrentTeacherEvaluation();
        Set<TeacherEvaluationFileType> files = new HashSet<TeacherEvaluationFileType>();
        if (currentTeacherEvaluation != null) {
            for (TeacherEvaluationFile teacherEvaluationFile : currentTeacherEvaluation.getTeacherEvaluationFileSet()) {
                files.add(teacherEvaluationFile.getTeacherEvaluationFileType());
            }
            if (files.containsAll(currentTeacherEvaluation.getEvaluationFileSet())) {
                return true;
            }
        }
        return false;
    }

    public String getCoEvaluatorsAsString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final TeacherEvaluationCoEvaluator teacherEvaluationCoEvaluator : getTeacherEvaluationCoEvaluatorSet()) {
            stringBuilder.append(teacherEvaluationCoEvaluator.getDescription());
        }
        return stringBuilder.toString();
    }

    public void delete() {
        setEvaluator(null);
        setEvaluee(null);
        setFacultyEvaluationProcess(null);
        for (final TeacherEvaluation teacherEvaluation : getTeacherEvaluationSet()) {
            teacherEvaluation.delete();
        }
        for (final TeacherEvaluationCoEvaluator teacherEvaluationCoEvaluator : getTeacherEvaluationCoEvaluatorSet()) {
            teacherEvaluationCoEvaluator.delete();
        }
        for (ApprovedTeacherEvaluationProcessMark mark : getApprovedTeacherEvaluationProcessMarkSet()) {
            mark.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void setApprovedTeacherEvaluationProcessMark(final FacultyEvaluationProcessYear facultyEvaluationProcessYear,
            final TeacherEvaluationMark teacherEvaluationMark) {
        final ApprovedTeacherEvaluationProcessMark approvedTeacherEvaluationProcessMark =
                createApprovedTeacherEvaluationProcessMark(facultyEvaluationProcessYear);
        approvedTeacherEvaluationProcessMark.setApprovedEvaluationMark(teacherEvaluationMark);
    }

    private ApprovedTeacherEvaluationProcessMark createApprovedTeacherEvaluationProcessMark(
            final FacultyEvaluationProcessYear facultyEvaluationProcessYear) {
        for (final ApprovedTeacherEvaluationProcessMark approvedTeacherEvaluationProcessMark : getApprovedTeacherEvaluationProcessMarkSet()) {
            if (approvedTeacherEvaluationProcessMark.getFacultyEvaluationProcessYear() == facultyEvaluationProcessYear) {
                return approvedTeacherEvaluationProcessMark;
            }
        }
        return new ApprovedTeacherEvaluationProcessMark(facultyEvaluationProcessYear, this);
    }

    public SortedSet<ApprovedTeacherEvaluationProcessMark> getOrderedApprovedTeacherEvaluationProcessMark() {
        final SortedSet<ApprovedTeacherEvaluationProcessMark> result =
                new TreeSet<ApprovedTeacherEvaluationProcessMark>(ApprovedTeacherEvaluationProcessMark.COMPARATOR_BY_YEAR);
        result.addAll(getApprovedTeacherEvaluationProcessMarkSet());
        return result;
    }

    public String getApprovedEvaluationMarkAsStringForCCAD() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final ApprovedTeacherEvaluationProcessMark approvedTeacherEvaluationProcessMark : getOrderedApprovedTeacherEvaluationProcessMark()) {
            final FacultyEvaluationProcessYear facultyEvaluationProcessYear =
                    approvedTeacherEvaluationProcessMark.getFacultyEvaluationProcessYear();
            final String year = facultyEvaluationProcessYear.getYear();
            final TeacherEvaluationMark approvedEvaluationMark = approvedTeacherEvaluationProcessMark.getApprovedEvaluationMark();
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(year);
            stringBuilder.append(" ");
            if (approvedEvaluationMark == null) {
                stringBuilder.append("N/A");
            } else {
                stringBuilder.append(BundleUtil.getString(Bundle.ENUMERATION, approvedEvaluationMark.name()));
            }
        }
        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    public String getApprovedEvaluationMarkAsString() {
        final FacultyEvaluationProcess facultyEvaluationProcess = getFacultyEvaluationProcess();
        return facultyEvaluationProcess.getAreApprovedMarksPublished() ? getApprovedEvaluationMarkAsStringForCCAD() : null;
    }

}
