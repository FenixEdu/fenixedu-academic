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
package org.fenixedu.academic.domain.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class Advise extends Advise_Base {

    static {
        TeacherAdviseService.getRelationAdviseTeacherAdviseService().addListener(new AdviseTeacherAdviseServiceListener());
    }

    public Advise(Teacher teacher, Registration registration, AdviseType adviseType, ExecutionSemester startPeriod,
            ExecutionSemester endPeriod) {
        super();
        setRootDomainObject(Bennu.getInstance());
        if (teacher == null || registration == null || adviseType == null || startPeriod == null || endPeriod == null) {
            throw new DomainException("arguments can't be null");
        }
        setTeacher(teacher);
        setStudent(registration);
        setAdviseType(adviseType);
        setStartExecutionPeriod(startPeriod);
        setEndExecutionPeriod(endPeriod);
    }

    public void delete() {
        if (getTeacherAdviseServicesSet() == null || getTeacherAdviseServicesSet().isEmpty()) {
            setStudent(null);
            setTeacher(null);
            setEndExecutionPeriod(null);
            setStartExecutionPeriod(null);
            setRootDomainObject(null);
            deleteDomainObject();
        } else {
            throw new DomainException("error.delete.Advise.hasTeacherAdviseServices");
        }
    }

    public TeacherAdviseService getTeacherAdviseServiceByExecutionPeriod(final ExecutionSemester executionSemester) {
        return (TeacherAdviseService) CollectionUtils.find(getTeacherAdviseServicesSet(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                TeacherAdviseService teacherAdviseService = (TeacherAdviseService) arg0;
                return teacherAdviseService.getTeacherService().getExecutionPeriod() == executionSemester;
            }
        });
    }

    public void checkPercentageCoherenceWithOtherAdvises(ExecutionSemester executionSemester, double percentage)
            throws AdvisePercentageException {
        for (Advise advise : getStudent().getAdvisesSet()) {
            if (advise != this && advise.getAdviseType().equals(getAdviseType())) {
                TeacherAdviseService teacherAdviseService = advise.getTeacherAdviseServiceByExecutionPeriod(executionSemester);
                if (teacherAdviseService != null) {
                    percentage += teacherAdviseService.getPercentage().doubleValue();
                    if (percentage > 100) {
                        throw new AdvisePercentageException("message.invalid.advise.percentage", getStudent().getAdvisesSet(),
                                executionSemester, getAdviseType());
                    }
                }
            }
        }
    }

    public void updateEndExecutionPeriod() {
        ExecutionSemester executionSemester = getStartExecutionPeriod();
        for (TeacherAdviseService teacherAdviseService : getTeacherAdviseServicesSet()) {
            ExecutionSemester adviseServiceEP = teacherAdviseService.getTeacherService().getExecutionPeriod();
            if (adviseServiceEP.getEndDate().after(executionSemester.getEndDate())) {
                executionSemester = adviseServiceEP;
            }
        }
        setEndExecutionPeriod(executionSemester);
    }

    public void updateStartExecutionPeriod() {
        ExecutionSemester executionSemester = getEndExecutionPeriod();
        for (TeacherAdviseService teacherAdviseService : getTeacherAdviseServicesSet()) {
            ExecutionSemester adviseServiceEP = teacherAdviseService.getTeacherService().getExecutionPeriod();
            if (adviseServiceEP.getBeginDate().before(executionSemester.getBeginDate())) {
                executionSemester = adviseServiceEP;
            }
        }
        setStartExecutionPeriod(executionSemester);
    }

    public class AdvisePercentageException extends DomainException {

        private final String key;

        private final Collection<Advise> advises;

        private final ExecutionSemester executionSemester;

        private final AdviseType adviseType;

        public AdvisePercentageException(String key, Collection<Advise> advises, ExecutionSemester executionSemester,
                AdviseType adviseType) {
            super(key);
            this.key = key;
            this.advises = advises;
            this.executionSemester = executionSemester;
            this.adviseType = adviseType;
        }

        public Collection<Advise> getAdvises() {
            return advises;
        }

        @Override
        public String getKey() {
            return key;
        }

        public ExecutionSemester getExecutionPeriod() {
            return executionSemester;
        }

        public AdviseType getAdviseType() {
            return adviseType;
        }
    }

    private static class AdviseTeacherAdviseServiceListener extends RelationAdapter<TeacherAdviseService, Advise> {
        @Override
        public void afterAdd(TeacherAdviseService teacherAdviseServices, Advise advise) {
            if (advise != null) {
                ExecutionSemester executionSemester = teacherAdviseServices.getTeacherService().getExecutionPeriod();
                if (executionSemester.getEndDate().after(advise.getEndExecutionPeriod().getEndDate())) {
                    advise.setEndExecutionPeriod(executionSemester);
                }
                if (executionSemester.getBeginDate().before(advise.getStartExecutionPeriod().getBeginDate())) {
                    advise.setStartExecutionPeriod(executionSemester);
                }
            }
        }

        @Override
        public void afterRemove(TeacherAdviseService teacherAdviseServices, Advise advise) {
            if (advise != null) {
                if (advise.getTeacherAdviseServicesSet() == null || advise.getTeacherAdviseServicesSet().isEmpty()) {
                    advise.delete();
                } else if (teacherAdviseServices != null) {
                    ExecutionSemester executionSemester = teacherAdviseServices.getTeacherService().getExecutionPeriod();
                    if (executionSemester == advise.getEndExecutionPeriod()) {
                        advise.updateEndExecutionPeriod();
                    } else if (executionSemester == advise.getStartExecutionPeriod()) {
                        advise.updateStartExecutionPeriod();
                    }
                }
            }
        }
    }

    public static List<Advise> getAdvisesByAdviseTypeAndExecutionYear(Teacher teacher, AdviseType adviseType,
            ExecutionYear executionYear) {

        List<Advise> result = new ArrayList<Advise>();
        Date executionYearStartDate = executionYear.getBeginDate();
        Date executionYearEndDate = executionYear.getEndDate();

        for (Advise advise : teacher.getAdvisesSet()) {
            if ((advise.getAdviseType() == adviseType)) {
                Date adviseStartDate = advise.getStartExecutionPeriod().getBeginDate();
                Date adviseEndDate = advise.getEndExecutionPeriod().getEndDate();

                if (((executionYearStartDate.compareTo(adviseStartDate) < 0) && (executionYearEndDate.compareTo(adviseStartDate) < 0))
                        || ((executionYearStartDate.compareTo(adviseEndDate) > 0) && (executionYearEndDate
                                .compareTo(adviseEndDate) > 0))) {
                    continue;
                }
                result.add(advise);
            }
        }

        return result;
    }

}
