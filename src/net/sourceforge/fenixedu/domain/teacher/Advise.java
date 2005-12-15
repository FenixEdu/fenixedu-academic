package net.sourceforge.fenixedu.domain.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class Advise extends Advise_Base {

    public Advise(ITeacher teacher, IStudent student, AdviseType adviseType,
            IExecutionPeriod startPeriod, IExecutionPeriod endPeriod) {
        super();
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

    public void delete(){
        if (getTeacherAdviseServices() == null || getTeacherAdviseServices().isEmpty()) {
            setStudent(null);
            setTeacher(null);
            setEndExecutionPeriod(null);
            setStartExecutionPeriod(null);
            deleteDomainObject();
        } else {
            throw new DomainException("error.delete.Advise.hasTeacherAdviseServices");
        }
    }
    
    public ITeacherAdviseService getTeacherAdviseServiceByExecutionPeriod(
            final IExecutionPeriod executionPeriod) {
        return (ITeacherAdviseService) CollectionUtils.find(getTeacherAdviseServices(), new Predicate() {
            public boolean evaluate(Object arg0) {
                ITeacherAdviseService teacherAdviseService = (ITeacherAdviseService) arg0;
                return teacherAdviseService.getTeacherService().getExecutionPeriod() == executionPeriod;
            }
        });
    }

    public void checkPercentageCoherenceWithOtherAdvises(IExecutionPeriod executionPeriod,
            double percentage) throws AdvisePercentageException {
        for (IAdvise advise : getStudent().getAdvises()) {
            if (advise != this && advise.getAdviseType().equals(getAdviseType())) {
                ITeacherAdviseService teacherAdviseService = advise
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
        IExecutionPeriod executionPeriod = getStartExecutionPeriod();
        for (ITeacherAdviseService teacherAdviseService : getTeacherAdviseServices()) {
            IExecutionPeriod adviseServiceEP = teacherAdviseService.getTeacherService()
                    .getExecutionPeriod();
            if (adviseServiceEP.getEndDate().after(executionPeriod.getEndDate())) {
                executionPeriod = adviseServiceEP;
            }
        }
        setEndExecutionPeriod(executionPeriod);
    }

    public void updateStartExecutionPeriod() {
        IExecutionPeriod executionPeriod = getEndExecutionPeriod();
        for (ITeacherAdviseService teacherAdviseService : getTeacherAdviseServices()) {
            IExecutionPeriod adviseServiceEP = teacherAdviseService.getTeacherService()
                    .getExecutionPeriod();
            if (adviseServiceEP.getBeginDate().before(executionPeriod.getBeginDate())) {
                executionPeriod = adviseServiceEP;
            }
        }
        setStartExecutionPeriod(executionPeriod);
    }

    public class AdvisePercentageException extends DomainException {

        private final String key;

        private final List<IAdvise> advises;

        private final IExecutionPeriod executionPeriod;

        private final AdviseType adviseType;

        public AdvisePercentageException(String key, List<IAdvise> advises,
                IExecutionPeriod executionPeriod, AdviseType adviseType) {
            super(key);
            this.key = key;
            this.advises = advises;
            this.executionPeriod = executionPeriod;
            this.adviseType = adviseType;
        }

        public List<IAdvise> getAdvises() {
            return advises;
        }

        public String getKey() {
            return key;
        }

        public IExecutionPeriod getExecutionPeriod() {
            return executionPeriod;
        }

        public AdviseType getAdviseType() {
            return adviseType;
        }
    }
}
