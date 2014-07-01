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
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.utils.ExecutionCourseUtils;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SeperateExecutionCourse {

    @Atomic
    public static ExecutionCourse run(final ExecutionCourse originExecutionCourse, ExecutionCourse destinationExecutionCourse,
            final List<Shift> shiftsToTransfer, final List<CurricularCourse> curricularCourseToTransfer) {

        if (originExecutionCourse.hasAnyInquiryResults()) {
            throw new DomainException("error.manager.executionCourseManagement.separateCourse.inqueriesPresent");
        }

        if (curricularCourseToTransfer == null || curricularCourseToTransfer.isEmpty()) {
            throw new DomainException("error.selection.noCurricularCourse");
        }

        if (destinationExecutionCourse == null) {
            destinationExecutionCourse = createNewExecutionCourse(originExecutionCourse);
            ExecutionCourseUtils.copyBibliographicReference(originExecutionCourse, destinationExecutionCourse);
            ExecutionCourseUtils.copyEvaluationMethod(originExecutionCourse, destinationExecutionCourse);
        }

        if (destinationExecutionCourse.equals(originExecutionCourse)) {
            throw new DomainException("error.selection.sameSourceDestinationCourse");
        }

        transferCurricularCourses(originExecutionCourse, destinationExecutionCourse, curricularCourseToTransfer);

        transferAttends(originExecutionCourse, destinationExecutionCourse);

        transferShifts(originExecutionCourse, destinationExecutionCourse, shiftsToTransfer);

        fixStudentShiftEnrolements(originExecutionCourse);
        fixStudentShiftEnrolements(destinationExecutionCourse);

        associateGroupings(originExecutionCourse, destinationExecutionCourse);

        return destinationExecutionCourse;
    }

    private static void transferCurricularCourses(final ExecutionCourse originExecutionCourse,
            final ExecutionCourse destinationExecutionCourse, final List<CurricularCourse> curricularCoursesToTransfer) {
        // The last curricular course must not be removed.
        if (originExecutionCourse.getAssociatedCurricularCourses().size() - curricularCoursesToTransfer.size() < 1) {
            throw new DomainException("error.manager.executionCourseManagement.lastCurricularCourse");
        }

        for (final CurricularCourse curricularCourse : curricularCoursesToTransfer) {
            originExecutionCourse.removeAssociatedCurricularCourses(curricularCourse);
            destinationExecutionCourse.addAssociatedCurricularCourses(curricularCourse);
        }
    }

    private static void transferAttends(final ExecutionCourse originExecutionCourse,
            final ExecutionCourse destinationExecutionCourse) {
        final Collection<CurricularCourse> curricularCourses = destinationExecutionCourse.getAssociatedCurricularCourses();
        List<Attends> allAttends = new ArrayList<>(originExecutionCourse.getAttendsSet());
        for (Attends attends : allAttends) {
            final Enrolment enrolment = attends.getEnrolment();
            if (enrolment != null && curricularCourses.contains(enrolment.getCurricularCourse())) {
                attends.setDisciplinaExecucao(destinationExecutionCourse);
            }
        }
    }

    private static void transferShifts(final ExecutionCourse originExecutionCourse,
            final ExecutionCourse destinationExecutionCourse, final List<Shift> shiftsToTransfer) {
        for (final Shift shift : shiftsToTransfer) {

            Collection<CourseLoad> courseLoads = shift.getCourseLoads();
            for (Iterator<CourseLoad> iter = courseLoads.iterator(); iter.hasNext();) {
                CourseLoad courseLoad = iter.next();
                CourseLoad newCourseLoad = destinationExecutionCourse.getCourseLoadByShiftType(courseLoad.getType());
                if (newCourseLoad == null) {
                    newCourseLoad =
                            new CourseLoad(destinationExecutionCourse, courseLoad.getType(), courseLoad.getUnitQuantity(),
                                    courseLoad.getTotalQuantity());
                }
                iter.remove();
                shift.removeCourseLoads(courseLoad);
                shift.addCourseLoads(newCourseLoad);
            }

        }
    }

    private static void fixStudentShiftEnrolements(final ExecutionCourse executionCourse) {
        for (final Shift shift : executionCourse.getAssociatedShifts()) {
            for (Registration registration : shift.getStudentsSet()) {
                if (!registration.attends(executionCourse)) {
                    shift.removeStudents(registration);
                }
            }
        }
    }

    private static void associateGroupings(final ExecutionCourse originExecutionCourse,
            final ExecutionCourse destinationExecutionCourse) {
        for (final Grouping grouping : originExecutionCourse.getGroupings()) {
            for (final StudentGroup studentGroup : grouping.getStudentGroupsSet()) {
                studentGroup.getAttends().clear();
                studentGroup.delete();
            }
            grouping.delete();
        }
    }

    private static ExecutionCourse createNewExecutionCourse(ExecutionCourse originExecutionCourse) {
        final String sigla =
                getUniqueExecutionCourseCode(originExecutionCourse.getNome(), originExecutionCourse.getExecutionPeriod(),
                        originExecutionCourse.getSigla());

        final ExecutionCourse destinationExecutionCourse =
                new ExecutionCourse(originExecutionCourse.getNome(), sigla, originExecutionCourse.getExecutionPeriod(), null);

        for (CourseLoad courseLoad : originExecutionCourse.getCourseLoads()) {
            new CourseLoad(destinationExecutionCourse, courseLoad.getType(), courseLoad.getUnitQuantity(),
                    courseLoad.getTotalQuantity());
        }

        for (final Professorship professorship : originExecutionCourse.getProfessorships()) {
            final Professorship newProfessorship = new Professorship();
            newProfessorship.setExecutionCourse(destinationExecutionCourse);
            newProfessorship.setPerson(professorship.getPerson());
            newProfessorship.setResponsibleFor(professorship.getResponsibleFor());
            professorship.getPermissions().copyPremissions(newProfessorship);
            destinationExecutionCourse.getProfessorships().add(newProfessorship);
        }

        return destinationExecutionCourse;
    }

    private static String getUniqueExecutionCourseCode(final String executionCourseName,
            final ExecutionSemester executionSemester, final String originalExecutionCourseCode) {
        Set<String> executionCourseCodes = getExecutionCourseCodes(executionSemester);
        return CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod.getUniqueSigla(executionCourseCodes,
                originalExecutionCourseCode);
        //
        // StringBuilder executionCourseCode = new
        // StringBuilder(originalExecutionCourseCode);
        // executionCourseCode.append("-1");
        // int startVariablePartIndex = executionCourseCode.length() - 1;
        // String destinationExecutionCourseCode =
        // executionCourseCode.toString();
        // for (int i = 1;
        // executionCourseCodes.contains(destinationExecutionCourseCode); ++i) {
        // executionCourseCode.replace(startVariablePartIndex,
        // executionCourseCode.length(), "");
        // executionCourseCode.append(i);
        // destinationExecutionCourseCode = executionCourseCode.toString();
        // }
        //
        // return destinationExecutionCourseCode;
    }

    private static Set<String> getExecutionCourseCodes(ExecutionSemester executionSemester) {
        Collection<ExecutionCourse> executionCourses = executionSemester.getAssociatedExecutionCourses();
        return new HashSet<String>(CollectionUtils.collect(executionCourses, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                ExecutionCourse executionCourse = (ExecutionCourse) arg0;
                return executionCourse.getSigla().toUpperCase();
            }
        }));
    }

    boolean contains(Integer[] integerArray, Integer integer) {
        for (Integer element : integerArray) {
            if (integer.equals(element)) {
                return true;
            }
        }

        return false;
    }

    @Atomic
    public static ExecutionCourse run(String executionCourseId, String destinationExecutionCourseID, String[] shiftIdsToTransfer,
            String[] curricularCourseIdsToTransfer) {

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        ExecutionCourse destinationExecutionCourse = FenixFramework.getDomainObject(destinationExecutionCourseID);
        List<Shift> shiftsToTransfer = readShiftsOIDsToTransfer(shiftIdsToTransfer);
        List<CurricularCourse> curricularCoursesToTransfer = readCurricularCoursesOIDsToTransfer(curricularCourseIdsToTransfer);

        return run(executionCourse, destinationExecutionCourse, shiftsToTransfer, curricularCoursesToTransfer);
    }

    private static List<Shift> readShiftsOIDsToTransfer(final String[] shiftIdsToTransfer) {
        List<Shift> result = new ArrayList<Shift>();

        if (shiftIdsToTransfer == null) {
            return result;
        }

        for (String oid : shiftIdsToTransfer) {
            result.add(FenixFramework.<Shift> getDomainObject(oid));
        }

        return result;
    }

    private static List<CurricularCourse> readCurricularCoursesOIDsToTransfer(final String[] curricularCourseIdsToTransfer) {
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();

        if (curricularCourseIdsToTransfer == null) {
            return result;
        }

        for (String oid : curricularCourseIdsToTransfer) {
            result.add((CurricularCourse) FenixFramework.getDomainObject(oid));
        }

        return result;
    }
}
