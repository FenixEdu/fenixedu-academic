package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

public class ShiftToEnrol extends DataTranferObject {

    private DomainReference<ExecutionCourse> executionCourse;

    private ShiftType theoricType;

    private ShiftType praticType;

    private ShiftType laboratoryType;

    private ShiftType theoricoPraticType;

    private ShiftType fieldWorkType;
    private ShiftType problemsType;
    private ShiftType seminaryType;
    private ShiftType trainingType;
    private ShiftType tutorialOrientationType;

    private DomainReference<Shift> theoricShift;

    private DomainReference<Shift> praticShift;

    private DomainReference<Shift> laboratoryShift;

    private DomainReference<Shift> theoricoPraticShift;

    private DomainReference<Shift> fieldWorkShift;
    private DomainReference<Shift> problemsShift;
    private DomainReference<Shift> seminaryShift;
    private DomainReference<Shift> trainingShift;
    private DomainReference<Shift> tutorialOrientationShift;

    private boolean enrolled;

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse == null ? null : executionCourse.getObject();
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse == null ? null : new DomainReference<ExecutionCourse>(
                executionCourse);
    }

    public Shift getLaboratoryShift() {
        return laboratoryShift == null ? null : laboratoryShift.getObject();
    }

    public void setLaboratoryShift(Shift laboratoryShift) {
        this.laboratoryShift = laboratoryShift == null ? null : new DomainReference<Shift>(
                laboratoryShift);
    }

    public ShiftType getLaboratoryType() {
        return laboratoryType;
    }

    public void setLaboratoryType(ShiftType laboratoryType) {
        this.laboratoryType = laboratoryType;
    }

    public Shift getPraticShift() {
        return praticShift == null ? null : praticShift.getObject();
    }

    public void setPraticShift(Shift praticShift) {
        this.praticShift = praticShift == null ? null : new DomainReference<Shift>(
                praticShift);
    }

    public ShiftType getPraticType() {
        return praticType;
    }

    public void setPraticType(ShiftType praticType) {
        this.praticType = praticType;
    }

    public Shift getTheoricoPraticShift() {
        return theoricoPraticShift == null ? null : theoricoPraticShift.getObject();
    }

    public void setTheoricoPraticShift(Shift theoricoPraticShift) {
        this.theoricoPraticShift = theoricoPraticShift == null ? null : new DomainReference<Shift>(
                theoricoPraticShift);
    }

    public ShiftType getTheoricoPraticType() {
        return theoricoPraticType;
    }

    public void setTheoricoPraticType(ShiftType theoricoPraticType) {
        this.theoricoPraticType = theoricoPraticType;
    }

    public Shift getTheoricShift() {
        return theoricShift == null ? null : theoricShift.getObject();
    }

    public void setTheoricShift(Shift theoricShift) {
        this.theoricShift = theoricShift == null ? null : new DomainReference<Shift>(
                theoricShift);
    }

    public ShiftType getTheoricType() {
        return theoricType;
    }

    public void setTheoricType(ShiftType theoricType) {
        this.theoricType = theoricType;
    }

    public ShiftType getFieldWorkType() {
        return fieldWorkType;
    }

    public void setFieldWorkType(ShiftType fieldWorkType) {
        this.fieldWorkType = fieldWorkType;
    }

    public ShiftType getProblemsType() {
        return problemsType;
    }

    public void setProblemsType(ShiftType problemsType) {
        this.problemsType = problemsType;
    }

    public ShiftType getSeminaryType() {
        return seminaryType;
    }

    public void setSeminaryType(ShiftType seminaryType) {
        this.seminaryType = seminaryType;
    }

    public ShiftType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(ShiftType trainingType) {
        this.trainingType = trainingType;
    }

    public ShiftType getTutorialOrientationType() {
        return tutorialOrientationType;
    }

    public void setTutorialOrientationType(ShiftType tutorialOrientationType) {
        this.tutorialOrientationType = tutorialOrientationType;
    }

    public Shift getFieldWorkShift() {
        return fieldWorkShift == null ? null : fieldWorkShift.getObject();
    }

    public void setFieldWorkShift(Shift fieldWorkShift) {
        this.fieldWorkShift = fieldWorkShift == null ? null : new DomainReference<Shift>(fieldWorkShift);
    }

    public Shift getProblemsShift() {
        return problemsShift == null ? null : problemsShift.getObject();
    }

    public void setProblemsShift(Shift problemsShift) {
        this.problemsShift = problemsShift == null ? null : new DomainReference<Shift>(problemsShift);
    }

    public Shift getSeminaryShift() {
        return seminaryShift == null ? null : seminaryShift.getObject();
    }

    public void setSeminaryShift(Shift seminaryShift) {
        this.seminaryShift = seminaryShift == null ? null : new DomainReference<Shift>(seminaryShift);
    }

    public Shift getTrainingShift() {
        return trainingShift == null ? null : trainingShift.getObject();
    }

    public void setTrainingShift(Shift trainingShift) {
        this.trainingShift = trainingShift == null ? null : new DomainReference<Shift>(trainingShift);
    }

    public Shift getTutorialOrientationShift() {
        return tutorialOrientationShift == null ? null : tutorialOrientationShift.getObject();
    }

    public void setTutorialOrientationShift(Shift tutorialOrientationShift) {
        this.tutorialOrientationShift = tutorialOrientationShift == null ? null : new DomainReference<Shift>(tutorialOrientationShift);
    }

}
