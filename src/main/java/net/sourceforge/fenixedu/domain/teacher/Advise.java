package net.sourceforge.fenixedu.domain.teacher;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

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
        if (getTeacherAdviseServices() == null || getTeacherAdviseServices().isEmpty()) {
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
        return (TeacherAdviseService) CollectionUtils.find(getTeacherAdviseServices(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                TeacherAdviseService teacherAdviseService = (TeacherAdviseService) arg0;
                return teacherAdviseService.getTeacherService().getExecutionPeriod() == executionSemester;
            }
        });
    }

    public void checkPercentageCoherenceWithOtherAdvises(ExecutionSemester executionSemester, double percentage)
            throws AdvisePercentageException {
        for (Advise advise : getStudent().getAdvises()) {
            if (advise != this && advise.getAdviseType().equals(getAdviseType())) {
                TeacherAdviseService teacherAdviseService = advise.getTeacherAdviseServiceByExecutionPeriod(executionSemester);
                if (teacherAdviseService != null) {
                    percentage += teacherAdviseService.getPercentage().doubleValue();
                    if (percentage > 100) {
                        throw new AdvisePercentageException("message.invalid.advise.percentage", getStudent().getAdvises(),
                                executionSemester, getAdviseType());
                    }
                }
            }
        }
    }

    public void updateEndExecutionPeriod() {
        ExecutionSemester executionSemester = getStartExecutionPeriod();
        for (TeacherAdviseService teacherAdviseService : getTeacherAdviseServices()) {
            ExecutionSemester adviseServiceEP = teacherAdviseService.getTeacherService().getExecutionPeriod();
            if (adviseServiceEP.getEndDate().after(executionSemester.getEndDate())) {
                executionSemester = adviseServiceEP;
            }
        }
        setEndExecutionPeriod(executionSemester);
    }

    public void updateStartExecutionPeriod() {
        ExecutionSemester executionSemester = getEndExecutionPeriod();
        for (TeacherAdviseService teacherAdviseService : getTeacherAdviseServices()) {
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
                if (advise.getTeacherAdviseServices() == null || advise.getTeacherAdviseServices().isEmpty()) {
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService> getTeacherAdviseServices() {
        return getTeacherAdviseServicesSet();
    }

    @Deprecated
    public boolean hasAnyTeacherAdviseServices() {
        return !getTeacherAdviseServicesSet().isEmpty();
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndExecutionPeriod() {
        return getEndExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasAdviseType() {
        return getAdviseType() != null;
    }

    @Deprecated
    public boolean hasStartExecutionPeriod() {
        return getStartExecutionPeriod() != null;
    }

}
