package net.sourceforge.fenixedu.domain.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class Advise extends Advise_Base {

    static {
        TeacherAdviseService.AdviseTeacherAdviseService
                .addListener(new AdviseTeacherAdviseServiceListener());
    }

    public Advise(Teacher teacher, Registration student, AdviseType adviseType, ExecutionPeriod startPeriod,
            ExecutionPeriod endPeriod) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        if (teacher == null || student == null || adviseType == null || startPeriod == null
                || endPeriod == null) {
            throw new DomainException("arguments can't be null");
        }
        setTeacher(teacher);
        setStudent(student);
        setAdviseType(adviseType);
        setStartExecutionPeriod(startPeriod);
        setEndExecutionPeriod(endPeriod);
    }

    public void delete() {
        if (getTeacherAdviseServices() == null || getTeacherAdviseServices().isEmpty()) {
            removeStudent();
            removeTeacher();
            removeEndExecutionPeriod();
            removeStartExecutionPeriod();
            removeRootDomainObject();
            deleteDomainObject();
        } else {
            throw new DomainException("error.delete.Advise.hasTeacherAdviseServices");
        }
    }

    public TeacherAdviseService getTeacherAdviseServiceByExecutionPeriod(
            final ExecutionPeriod executionPeriod) {
        return (TeacherAdviseService) CollectionUtils.find(getTeacherAdviseServices(), new Predicate() {
            public boolean evaluate(Object arg0) {
                TeacherAdviseService teacherAdviseService = (TeacherAdviseService) arg0;
                return teacherAdviseService.getTeacherService().getExecutionPeriod() == executionPeriod;
            }
        });
    }

    public void checkPercentageCoherenceWithOtherAdvises(ExecutionPeriod executionPeriod,
            double percentage) throws AdvisePercentageException {
        for (Advise advise : getStudent().getAdvises()) {
            if (advise != this && advise.getAdviseType().equals(getAdviseType())) {
                TeacherAdviseService teacherAdviseService = advise
                        .getTeacherAdviseServiceByExecutionPeriod(executionPeriod);
                if (teacherAdviseService != null) {
                    percentage += teacherAdviseService.getPercentage().doubleValue();
                    if (percentage > 100) {
                        throw new AdvisePercentageException("message.invalid.advise.percentage",
                                getStudent().getAdvises(), executionPeriod, getAdviseType());
                    }
                }
            }
        }
    }

    public void updateEndExecutionPeriod() {
        ExecutionPeriod executionPeriod = getStartExecutionPeriod();
        for (TeacherAdviseService teacherAdviseService : getTeacherAdviseServices()) {
            ExecutionPeriod adviseServiceEP = teacherAdviseService.getTeacherService()
                    .getExecutionPeriod();
            if (adviseServiceEP.getEndDate().after(executionPeriod.getEndDate())) {
                executionPeriod = adviseServiceEP;
            }
        }
        setEndExecutionPeriod(executionPeriod);
    }

    public void updateStartExecutionPeriod() {
        ExecutionPeriod executionPeriod = getEndExecutionPeriod();
        for (TeacherAdviseService teacherAdviseService : getTeacherAdviseServices()) {
            ExecutionPeriod adviseServiceEP = teacherAdviseService.getTeacherService()
                    .getExecutionPeriod();
            if (adviseServiceEP.getBeginDate().before(executionPeriod.getBeginDate())) {
                executionPeriod = adviseServiceEP;
            }
        }
        setStartExecutionPeriod(executionPeriod);
    }

    public class AdvisePercentageException extends DomainException {

        private final String key;

        private final List<Advise> advises;

        private final ExecutionPeriod executionPeriod;

        private final AdviseType adviseType;

        public AdvisePercentageException(String key, List<Advise> advises,
                ExecutionPeriod executionPeriod, AdviseType adviseType) {
            super(key);
            this.key = key;
            this.advises = advises;
            this.executionPeriod = executionPeriod;
            this.adviseType = adviseType;
        }

        public List<Advise> getAdvises() {
            return advises;
        }

        public String getKey() {
            return key;
        }

        public ExecutionPeriod getExecutionPeriod() {
            return executionPeriod;
        }

        public AdviseType getAdviseType() {
            return adviseType;
        }
    }

    private static class AdviseTeacherAdviseServiceListener extends
            dml.runtime.RelationAdapter<TeacherAdviseService, Advise> {
        @Override
        public void afterAdd(TeacherAdviseService teacherAdviseServices, Advise advise) {
            if (advise != null) {
                ExecutionPeriod executionPeriod = teacherAdviseServices.getTeacherService()
                        .getExecutionPeriod();
                if (executionPeriod.getEndDate().after(advise.getEndExecutionPeriod().getEndDate())) {
                    advise.setEndExecutionPeriod(executionPeriod);
                }
                if (executionPeriod.getBeginDate().before(
                        advise.getStartExecutionPeriod().getBeginDate())) {
                    advise.setStartExecutionPeriod(executionPeriod);
                }
            }
        }

        @Override
        public void afterRemove(TeacherAdviseService teacherAdviseServices, Advise advise) {
            if (advise != null) {
                if (advise.getTeacherAdviseServices() == null
                        || advise.getTeacherAdviseServices().isEmpty()) {
                    advise.delete();
                } else if (teacherAdviseServices != null) {
                    ExecutionPeriod executionPeriod = teacherAdviseServices.getTeacherService()
                            .getExecutionPeriod();
                    if (executionPeriod == advise.getEndExecutionPeriod()) {
                        advise.updateEndExecutionPeriod();
                    } else if (executionPeriod == advise.getStartExecutionPeriod()) {
                        advise.updateStartExecutionPeriod();
                    }
                }
            }
        }
    }
}
