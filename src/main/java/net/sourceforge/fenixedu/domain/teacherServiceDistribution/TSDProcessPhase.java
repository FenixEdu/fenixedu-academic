package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.commons.beanutils.BeanComparator;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.TSDProcessPhasePredicates;

public class TSDProcessPhase extends TSDProcessPhase_Base {

    protected TSDProcessPhase() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public TSDProcessPhase(TSDProcess tsdProcess, String name, TSDProcessPhase previousTSDProcessPhase,
            TSDProcessPhase nextTSDProcessPhase, TSDProcessPhaseStatus status) {
        this();

        this.setTSDProcess(tsdProcess);
        this.setName(name);
        this.setPreviousTSDProcessPhase(previousTSDProcessPhase);
        this.setNextTSDProcessPhase(nextTSDProcessPhase);
        this.setStatus(status);
        this.setIsPublished(false);

        TSDFactory tsdFactory = TSDFactory.getInstance();
        tsdFactory.createTSDTreeStructure(this);
    }

    public TSDProcessPhase getCurrentTSDProcessPhase() {
        for (TSDProcessPhase tsdProcessPhase = getFirstTSDProcessPhase(); tsdProcessPhase != null; tsdProcessPhase =
                tsdProcessPhase.getNextTSDProcessPhase()) {
            if (tsdProcessPhase.getStatus() == TSDProcessPhaseStatus.CURRENT) {
                return tsdProcessPhase;
            }
        }

        return null;
    }

    public TSDProcessPhase getFirstTSDProcessPhase() {
        TSDProcessPhase tsdProcessPhase = this;

        while (tsdProcessPhase.getPreviousTSDProcessPhase() != null) {
            tsdProcessPhase = tsdProcessPhase.getPreviousTSDProcessPhase();
        }

        return tsdProcessPhase;
    }

    public TSDProcessPhase getLastTSDProcessPhase() {
        TSDProcessPhase tsdProcessPhase = this;

        while (tsdProcessPhase.getNextTSDProcessPhase() != null) {
            tsdProcessPhase = tsdProcessPhase.getNextTSDProcessPhase();
        }

        return tsdProcessPhase;
    }

    public List<TSDProcessPhase> getPreviousTSDProcessPhases() {
        List<TSDProcessPhase> previousTSDProcessPhaseList = new ArrayList<TSDProcessPhase>();
        TSDProcessPhase phase = getPreviousTSDProcessPhase();

        while (phase != null) {
            previousTSDProcessPhaseList.add(phase);
            phase = phase.getPreviousTSDProcessPhase();
        }

        return previousTSDProcessPhaseList;
    }

    public Integer getNumberOfTeacherServiceDistributions() {
        return getGroupings().size();
    }

    public Boolean getIsPrevious(TSDProcessPhase tsdProcessPhase) {
        if (tsdProcessPhase == this) {
            return false;
        }

        for (; (tsdProcessPhase != null); tsdProcessPhase = tsdProcessPhase.getNextTSDProcessPhase()) {
            if (tsdProcessPhase.getNextTSDProcessPhase() == this) {
                return false;
            }
        }

        return true;
    }

    public void setCurrent() {
        check(this, TSDProcessPhasePredicates.writePredicate);
        if (getStatus() == TSDProcessPhaseStatus.OPEN) {
            getCurrentTSDProcessPhase().setStatus(TSDProcessPhaseStatus.OPEN);
            setStatus(TSDProcessPhaseStatus.CURRENT);
        }
    }

    public void setClosed() {
        check(this, TSDProcessPhasePredicates.writePredicate);
        if (getStatus() == TSDProcessPhaseStatus.OPEN) {
            setStatus(TSDProcessPhaseStatus.CLOSED);
        }
    }

    public void setOpen() {
        check(this, TSDProcessPhasePredicates.writePredicate);
        if (getStatus() == TSDProcessPhaseStatus.CLOSED) {
            setStatus(TSDProcessPhaseStatus.OPEN);
        }
    }

    public TeacherServiceDistribution getRootTSD() {
        return getGroupings().iterator().next().getRootTSD();
    }

    public void delete() {
        check(this, TSDProcessPhasePredicates.writePredicate);
        for (TeacherServiceDistribution tsd : getGroupings()) {
            removeGroupings(tsd);
        }

        setNextTSDProcessPhase(null);
        setPreviousTSDProcessPhase(null);
        setTSDProcess(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public void deleteTSDProcessPhaseData() {
        check(this, TSDProcessPhasePredicates.writePredicate);
        deleteTeacherServiceDistributions();
    }

    private void deleteTeacherServiceDistributions() {
        for (TeacherServiceDistribution tsd : getGroupings()) {
            for (TSDTeacher tsdTeacher : tsd.getTSDTeachers()) {
                tsdTeacher.delete();
            }
            for (TSDCourse course : tsd.getTSDCourses()) {
                course.delete();
            }

            tsd.delete();
        }
    }

    public void deleteDataAndPhase() {
        check(this, TSDProcessPhasePredicates.writePredicate);
        deleteTSDProcessPhaseData();
        delete();
    }

    public List<TeacherServiceDistribution> getTeacherServiceDistributionsOrderedByName() {
        List<TeacherServiceDistribution> orderedGrouping = new ArrayList<TeacherServiceDistribution>(getGroupings());

        Collections.sort(orderedGrouping, new BeanComparator("name"));

        return orderedGrouping;
    }

    @Override
    public void setIsPublished(Boolean isPublished) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setIsPublished(isPublished);
    }

    @Override
    public void setTSDProcess(TSDProcess tsdProcess) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setTSDProcess(tsdProcess);
    }

    @Override
    public void setName(String name) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setName(name);
    }

    @Override
    public void setNextTSDProcessPhase(TSDProcessPhase nextTSDProcessPhase) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setNextTSDProcessPhase(nextTSDProcessPhase);
    }

    @Override
    public void setPreviousTSDProcessPhase(TSDProcessPhase previousTSDProcessPhase) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setPreviousTSDProcessPhase(previousTSDProcessPhase);
    }

    @Override
    public void setStatus(TSDProcessPhaseStatus status) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setStatus(status);
    }

    @Override
    public void setStudentsPerLaboratorialShift(Integer studentsPerLaboratorialShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setStudentsPerLaboratorialShift(studentsPerLaboratorialShift);
    }

    @Override
    public void setStudentsPerFieldWorkShift(Integer studentsPerFieldWorkShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setStudentsPerFieldWorkShift(studentsPerFieldWorkShift);
    }

    @Override
    public void setStudentsPerProblemShift(Integer studentsPerProblemShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setStudentsPerProblemShift(studentsPerProblemShift);
    }

    @Override
    public void setStudentsPerSeminaryShift(Integer studentsPerSeminaryShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setStudentsPerSeminaryShift(studentsPerSeminaryShift);
    }

    @Override
    public void setStudentsPerTrainingPeriodShift(Integer studentsPerTrainingPeriodShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setStudentsPerTrainingPeriodShift(studentsPerTrainingPeriodShift);
    }

    @Override
    public void setStudentsPerTutDirectionShift(Integer studentsPerTutDirectionShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setStudentsPerTutDirectionShift(studentsPerTutDirectionShift);
    }

    @Override
    public void setStudentsPerTheoreticalShift(Integer studentsPerTheoreticalShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setStudentsPerTheoreticalShift(studentsPerTheoreticalShift);
    }

    @Override
    public void setWeightFirstTimeEnrolledStudentsPerLabShift(Double weightFirstTimeEnrolledStudentsPerLabShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setWeightFirstTimeEnrolledStudentsPerLabShift(weightFirstTimeEnrolledStudentsPerLabShift);
    }

    @Override
    public void setWeightFirstTimeEnrolledStudentsPerTheoShift(Double weightFirstTimeEnrolledStudentsPerTheoShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setWeightFirstTimeEnrolledStudentsPerTheoShift(weightFirstTimeEnrolledStudentsPerTheoShift);
    }

    @Override
    public void setWeightSecondTimeEnrolledStudentsPerLabShift(Double weightSecondTimeEnrolledStudentsPerLabShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setWeightSecondTimeEnrolledStudentsPerLabShift(weightSecondTimeEnrolledStudentsPerLabShift);
    }

    @Override
    public void setWeightSecondTimeEnrolledStudentsPerTheoShift(Double weightSecondTimeEnrolledStudentsPerTheoShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);

        super.setWeightSecondTimeEnrolledStudentsPerTheoShift(weightSecondTimeEnrolledStudentsPerTheoShift);
    }

    @Override
    public void setWeightFirstTimeEnrolledStudentsPerFieldWorkShift(Double weightFirstTimeEnrolledStudentsPerFieldWorkShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightFirstTimeEnrolledStudentsPerFieldWorkShift(weightFirstTimeEnrolledStudentsPerFieldWorkShift);
    }

    @Override
    public void setWeightFirstTimeEnrolledStudentsPerProblemShift(Double weightFirstTimeEnrolledStudentsPerProblemShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightFirstTimeEnrolledStudentsPerProblemShift(weightFirstTimeEnrolledStudentsPerProblemShift);
    }

    @Override
    public void setWeightFirstTimeEnrolledStudentsPerSeminaryShift(Double weightFirstTimeEnrolledStudentsPerSeminaryShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightFirstTimeEnrolledStudentsPerSeminaryShift(weightFirstTimeEnrolledStudentsPerSeminaryShift);
    }

    @Override
    public void setWeightFirstTimeEnrolledStudentsPerTrainingShift(Double weightFirstTimeEnrolledStudentsPerTrainingShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightFirstTimeEnrolledStudentsPerTrainingShift(weightFirstTimeEnrolledStudentsPerTrainingShift);
    }

    @Override
    public void setWeightFirstTimeEnrolledStudentsPerTutDirectionShift(Double weightFirstTimeEnrolledStudentsPerTutDirectionShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightFirstTimeEnrolledStudentsPerTutDirectionShift(weightFirstTimeEnrolledStudentsPerTutDirectionShift);
    }

    @Override
    public void setWeightSecondTimeEnrolledStudentsPerFieldWorkShift(Double weightSecondTimeEnrolledStudentsPerFieldWorkShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightSecondTimeEnrolledStudentsPerFieldWorkShift(weightSecondTimeEnrolledStudentsPerFieldWorkShift);
    }

    @Override
    public void setWeightSecondTimeEnrolledStudentsPerProblemShift(Double weightSecondTimeEnrolledStudentsPerProblemShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightSecondTimeEnrolledStudentsPerProblemShift(weightSecondTimeEnrolledStudentsPerProblemShift);
    }

    @Override
    public void setWeightSecondTimeEnrolledStudentsPerSeminaryShift(Double weightSecondTimeEnrolledStudentsPerSeminaryShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightSecondTimeEnrolledStudentsPerSeminaryShift(weightSecondTimeEnrolledStudentsPerSeminaryShift);
    }

    @Override
    public void setWeightSecondTimeEnrolledStudentsPerTrainingShift(Double weightSecondTimeEnrolledStudentsPerTrainingShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightSecondTimeEnrolledStudentsPerTrainingShift(weightSecondTimeEnrolledStudentsPerTrainingShift);
    }

    @Override
    public void setWeightSecondTimeEnrolledStudentsPerTutDirectionShift(
            Double weightSecondTimeEnrolledStudentsPerTutDirectionShift) {
        check(this, TSDProcessPhasePredicates.writePredicate);
        super.setWeightSecondTimeEnrolledStudentsPerTutDirectionShift(weightSecondTimeEnrolledStudentsPerTutDirectionShift);
    }

    public Integer getStudentsPerShift(ShiftType type) {
        Integer studentsNumber = 0;

        switch (type) {
        case TEORICA:
            studentsNumber = getStudentsPerTheoreticalShift();
            break;
        case PROBLEMS:
            studentsNumber = getStudentsPerProblemShift();
            break;
        case SEMINARY:
            studentsNumber = getStudentsPerSeminaryShift();
            break;
        case FIELD_WORK:
            studentsNumber = getStudentsPerFieldWorkShift();
            break;
        case TRAINING_PERIOD:
            studentsNumber = getStudentsPerTrainingPeriodShift();
            break;
        case TUTORIAL_ORIENTATION:
            studentsNumber = getStudentsPerTutDirectionShift();
            break;
        case LABORATORIAL:
            studentsNumber = getStudentsPerLaboratorialShift();
            break;
        }

        return studentsNumber;
    }

    public Double getWeightFirstTimeEnrolledStudentsPerShift(ShiftType type) {
        Double weight = 0d;

        switch (type) {
        case TEORICA:
            weight = getWeightFirstTimeEnrolledStudentsPerTheoShift();
            break;
        case PROBLEMS:
            weight = getWeightFirstTimeEnrolledStudentsPerProblemShift();
            break;
        case SEMINARY:
            weight = getWeightFirstTimeEnrolledStudentsPerSeminaryShift();
            break;
        case FIELD_WORK:
            weight = getWeightFirstTimeEnrolledStudentsPerFieldWorkShift();
            break;
        case TRAINING_PERIOD:
            weight = getWeightFirstTimeEnrolledStudentsPerTrainingShift();
            break;
        case TUTORIAL_ORIENTATION:
            weight = getWeightFirstTimeEnrolledStudentsPerTutDirectionShift();
            break;
        case LABORATORIAL:
            weight = getWeightFirstTimeEnrolledStudentsPerLabShift();
            break;
        }

        return weight;
    }

    public Double getWeightSecondTimeEnrolledStudentsPerShift(ShiftType type) {
        Double weight = 0d;

        switch (type) {
        case TEORICA:
            weight = getWeightSecondTimeEnrolledStudentsPerTheoShift();
            break;
        case PROBLEMS:
            weight = getWeightSecondTimeEnrolledStudentsPerProblemShift();
            break;
        case SEMINARY:
            weight = getWeightSecondTimeEnrolledStudentsPerSeminaryShift();
            break;
        case FIELD_WORK:
            weight = getWeightSecondTimeEnrolledStudentsPerFieldWorkShift();
            break;
        case TRAINING_PERIOD:
            weight = getWeightSecondTimeEnrolledStudentsPerTrainingShift();
            break;
        case TUTORIAL_ORIENTATION:
            weight = getWeightSecondTimeEnrolledStudentsPerTutDirectionShift();
            break;
        case LABORATORIAL:
            weight = getWeightSecondTimeEnrolledStudentsPerLabShift();
            break;
        }

        return weight;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution> getGroupings() {
        return getGroupingsSet();
    }

    @Deprecated
    public boolean hasAnyGroupings() {
        return !getGroupingsSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTSDProcess() {
        return getTSDProcess() != null;
    }

    @Deprecated
    public boolean hasStudentsPerTheoreticalShift() {
        return getStudentsPerTheoreticalShift() != null;
    }

    @Deprecated
    public boolean hasWeightFirstTimeEnrolledStudentsPerSeminaryShift() {
        return getWeightFirstTimeEnrolledStudentsPerSeminaryShift() != null;
    }

    @Deprecated
    public boolean hasWeightFirstTimeEnrolledStudentsPerTrainingShift() {
        return getWeightFirstTimeEnrolledStudentsPerTrainingShift() != null;
    }

    @Deprecated
    public boolean hasStudentsPerFieldWorkShift() {
        return getStudentsPerFieldWorkShift() != null;
    }

    @Deprecated
    public boolean hasWeightSecondTimeEnrolledStudentsPerTrainingShift() {
        return getWeightSecondTimeEnrolledStudentsPerTrainingShift() != null;
    }

    @Deprecated
    public boolean hasStudentsPerTrainingPeriodShift() {
        return getStudentsPerTrainingPeriodShift() != null;
    }

    @Deprecated
    public boolean hasWeightSecondTimeEnrolledStudentsPerTutDirectionShift() {
        return getWeightSecondTimeEnrolledStudentsPerTutDirectionShift() != null;
    }

    @Deprecated
    public boolean hasPreviousTSDProcessPhase() {
        return getPreviousTSDProcessPhase() != null;
    }

    @Deprecated
    public boolean hasWeightSecondTimeEnrolledStudentsPerLabShift() {
        return getWeightSecondTimeEnrolledStudentsPerLabShift() != null;
    }

    @Deprecated
    public boolean hasWeightFirstTimeEnrolledStudentsPerTheoShift() {
        return getWeightFirstTimeEnrolledStudentsPerTheoShift() != null;
    }

    @Deprecated
    public boolean hasWeightSecondTimeEnrolledStudentsPerSeminaryShift() {
        return getWeightSecondTimeEnrolledStudentsPerSeminaryShift() != null;
    }

    @Deprecated
    public boolean hasWeightFirstTimeEnrolledStudentsPerLabShift() {
        return getWeightFirstTimeEnrolledStudentsPerLabShift() != null;
    }

    @Deprecated
    public boolean hasStudentsPerLaboratorialShift() {
        return getStudentsPerLaboratorialShift() != null;
    }

    @Deprecated
    public boolean hasStudentsPerSeminaryShift() {
        return getStudentsPerSeminaryShift() != null;
    }

    @Deprecated
    public boolean hasIsPublished() {
        return getIsPublished() != null;
    }

    @Deprecated
    public boolean hasWeightSecondTimeEnrolledStudentsPerProblemShift() {
        return getWeightSecondTimeEnrolledStudentsPerProblemShift() != null;
    }

    @Deprecated
    public boolean hasWeightSecondTimeEnrolledStudentsPerFieldWorkShift() {
        return getWeightSecondTimeEnrolledStudentsPerFieldWorkShift() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasWeightFirstTimeEnrolledStudentsPerFieldWorkShift() {
        return getWeightFirstTimeEnrolledStudentsPerFieldWorkShift() != null;
    }

    @Deprecated
    public boolean hasNextTSDProcessPhase() {
        return getNextTSDProcessPhase() != null;
    }

    @Deprecated
    public boolean hasStatus() {
        return getStatus() != null;
    }

    @Deprecated
    public boolean hasStudentsPerProblemShift() {
        return getStudentsPerProblemShift() != null;
    }

    @Deprecated
    public boolean hasWeightFirstTimeEnrolledStudentsPerTutDirectionShift() {
        return getWeightFirstTimeEnrolledStudentsPerTutDirectionShift() != null;
    }

    @Deprecated
    public boolean hasStudentsPerTutDirectionShift() {
        return getStudentsPerTutDirectionShift() != null;
    }

    @Deprecated
    public boolean hasWeightFirstTimeEnrolledStudentsPerProblemShift() {
        return getWeightFirstTimeEnrolledStudentsPerProblemShift() != null;
    }

    @Deprecated
    public boolean hasWeightSecondTimeEnrolledStudentsPerTheoShift() {
        return getWeightSecondTimeEnrolledStudentsPerTheoShift() != null;
    }

}
