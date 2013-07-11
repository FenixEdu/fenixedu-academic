package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

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

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setCurrent() {
        if (getStatus() == TSDProcessPhaseStatus.OPEN) {
            getCurrentTSDProcessPhase().setStatus(TSDProcessPhaseStatus.OPEN);
            setStatus(TSDProcessPhaseStatus.CURRENT);
        }
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setClosed() {
        if (getStatus() == TSDProcessPhaseStatus.OPEN) {
            setStatus(TSDProcessPhaseStatus.CLOSED);
        }
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setOpen() {
        if (getStatus() == TSDProcessPhaseStatus.CLOSED) {
            setStatus(TSDProcessPhaseStatus.OPEN);
        }
    }

    public TeacherServiceDistribution getRootTSD() {
        return getGroupings().get(0).getRootTSD();
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void delete() {
        for (TeacherServiceDistribution tsd : getGroupings()) {
            removeGroupings(tsd);
        }

        setNextTSDProcessPhase(null);
        setPreviousTSDProcessPhase(null);
        setTSDProcess(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void deleteTSDProcessPhaseData() {
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

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void deleteDataAndPhase() {
        deleteTSDProcessPhaseData();
        delete();
    }

    public List<TeacherServiceDistribution> getTeacherServiceDistributionsOrderedByName() {
        List<TeacherServiceDistribution> orderedGrouping = new ArrayList<TeacherServiceDistribution>(getGroupings());

        Collections.sort(orderedGrouping, new BeanComparator("name"));

        return orderedGrouping;
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setIsPublished(Boolean isPublished) {
        super.setIsPublished(isPublished);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setTSDProcess(TSDProcess tsdProcess) {
        super.setTSDProcess(tsdProcess);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setName(String name) {

        super.setName(name);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setNextTSDProcessPhase(TSDProcessPhase nextTSDProcessPhase) {

        super.setNextTSDProcessPhase(nextTSDProcessPhase);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setPreviousTSDProcessPhase(TSDProcessPhase previousTSDProcessPhase) {

        super.setPreviousTSDProcessPhase(previousTSDProcessPhase);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStatus(TSDProcessPhaseStatus status) {

        super.setStatus(status);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerLaboratorialShift(Integer studentsPerLaboratorialShift) {

        super.setStudentsPerLaboratorialShift(studentsPerLaboratorialShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerFieldWorkShift(Integer studentsPerFieldWorkShift) {
        super.setStudentsPerFieldWorkShift(studentsPerFieldWorkShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerProblemShift(Integer studentsPerProblemShift) {
        super.setStudentsPerProblemShift(studentsPerProblemShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerSeminaryShift(Integer studentsPerSeminaryShift) {
        super.setStudentsPerSeminaryShift(studentsPerSeminaryShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerTrainingPeriodShift(Integer studentsPerTrainingPeriodShift) {
        super.setStudentsPerTrainingPeriodShift(studentsPerTrainingPeriodShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerTutDirectionShift(Integer studentsPerTutDirectionShift) {
        super.setStudentsPerTutDirectionShift(studentsPerTutDirectionShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerTheoreticalShift(Integer studentsPerTheoreticalShift) {

        super.setStudentsPerTheoreticalShift(studentsPerTheoreticalShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerLabShift(Double weightFirstTimeEnrolledStudentsPerLabShift) {

        super.setWeightFirstTimeEnrolledStudentsPerLabShift(weightFirstTimeEnrolledStudentsPerLabShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerTheoShift(Double weightFirstTimeEnrolledStudentsPerTheoShift) {

        super.setWeightFirstTimeEnrolledStudentsPerTheoShift(weightFirstTimeEnrolledStudentsPerTheoShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerLabShift(Double weightSecondTimeEnrolledStudentsPerLabShift) {

        super.setWeightSecondTimeEnrolledStudentsPerLabShift(weightSecondTimeEnrolledStudentsPerLabShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerTheoShift(Double weightSecondTimeEnrolledStudentsPerTheoShift) {

        super.setWeightSecondTimeEnrolledStudentsPerTheoShift(weightSecondTimeEnrolledStudentsPerTheoShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerFieldWorkShift(Double weightFirstTimeEnrolledStudentsPerFieldWorkShift) {
        super.setWeightFirstTimeEnrolledStudentsPerFieldWorkShift(weightFirstTimeEnrolledStudentsPerFieldWorkShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerProblemShift(Double weightFirstTimeEnrolledStudentsPerProblemShift) {
        super.setWeightFirstTimeEnrolledStudentsPerProblemShift(weightFirstTimeEnrolledStudentsPerProblemShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerSeminaryShift(Double weightFirstTimeEnrolledStudentsPerSeminaryShift) {
        super.setWeightFirstTimeEnrolledStudentsPerSeminaryShift(weightFirstTimeEnrolledStudentsPerSeminaryShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerTrainingShift(Double weightFirstTimeEnrolledStudentsPerTrainingShift) {
        super.setWeightFirstTimeEnrolledStudentsPerTrainingShift(weightFirstTimeEnrolledStudentsPerTrainingShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerTutDirectionShift(Double weightFirstTimeEnrolledStudentsPerTutDirectionShift) {
        super.setWeightFirstTimeEnrolledStudentsPerTutDirectionShift(weightFirstTimeEnrolledStudentsPerTutDirectionShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerFieldWorkShift(Double weightSecondTimeEnrolledStudentsPerFieldWorkShift) {
        super.setWeightSecondTimeEnrolledStudentsPerFieldWorkShift(weightSecondTimeEnrolledStudentsPerFieldWorkShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerProblemShift(Double weightSecondTimeEnrolledStudentsPerProblemShift) {
        super.setWeightSecondTimeEnrolledStudentsPerProblemShift(weightSecondTimeEnrolledStudentsPerProblemShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerSeminaryShift(Double weightSecondTimeEnrolledStudentsPerSeminaryShift) {
        super.setWeightSecondTimeEnrolledStudentsPerSeminaryShift(weightSecondTimeEnrolledStudentsPerSeminaryShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerTrainingShift(Double weightSecondTimeEnrolledStudentsPerTrainingShift) {
        super.setWeightSecondTimeEnrolledStudentsPerTrainingShift(weightSecondTimeEnrolledStudentsPerTrainingShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerTutDirectionShift(
            Double weightSecondTimeEnrolledStudentsPerTutDirectionShift) {
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
