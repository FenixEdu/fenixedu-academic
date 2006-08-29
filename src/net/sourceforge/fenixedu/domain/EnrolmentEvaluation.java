package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.curriculum.GradeFactory;
import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.FenixDigestUtils;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;

public class EnrolmentEvaluation extends EnrolmentEvaluation_Base implements Comparable {

    public static final Comparator<EnrolmentEvaluation> SORT_BY_STUDENT_NUMBER = new BeanComparator(
            "enrolment.studentCurricularPlan.student.number");
    
    public static final Comparator<EnrolmentEvaluation> SORT_SAME_TYPE_GRADE = new Comparator<EnrolmentEvaluation>() {
        public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
            if (o1.getEnrolmentEvaluationType() != o2.getEnrolmentEvaluationType()) {
                throw new RuntimeException("error.enrolmentEvaluation.different.types");
            }
            if (o1.getEnrolmentEvaluationState().getWeight() == o2.getEnrolmentEvaluationState()
                    .getWeight()) {
                return o1.compareMyWhenAlteredDateToAnotherWhenAlteredDate(o2.getWhen());
            }
            return o1.getEnrolmentEvaluationState().getWeight()
                    - o2.getEnrolmentEvaluationState().getWeight();
        }
    };

    public static final Comparator<EnrolmentEvaluation> SORT_BY_GRADE = new Comparator<EnrolmentEvaluation>() {
        public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
            return o1.getGradeWrapper().compareTo(o2.getGradeWrapper());
        }
    };

    private static final String RECTIFICATION = "RECTIFICAÇÃO";

    public EnrolmentEvaluation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType type) {
        this();
        if (enrolment == null || type == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setEnrolment(enrolment);
        setEnrolmentEvaluationType(type);
    }

    public EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationState enrolmentEvaluationState,
            EnrolmentEvaluationType type, Person responsibleFor, String grade, Date availableDate,
            Date examDate) {

        this(enrolment, type);

        if (enrolmentEvaluationState == null || responsibleFor == null || examDate == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setEnrolmentEvaluationState(enrolmentEvaluationState);
        setPersonResponsibleForGrade(responsibleFor);
        setGrade(grade);
        setGradeAvailableDate(availableDate);
        setExamDate(examDate);

        generateCheckSum();
    }

    public EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType, EnrolmentEvaluationState evaluationState) {
		this(enrolment, enrolmentEvaluationType);
		if(evaluationState == null) {
			throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
		}
		setEnrolmentEvaluationState(evaluationState);
	}

	public int compareTo(Object o) {
        EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
        EnrollmentState myEnrolmentState = this.getEnrollmentStateByGrade(this.getGrade());
        EnrollmentState otherEnrolmentState = this.getEnrollmentStateByGrade(enrolmentEvaluation
                .getGrade());
        String otherGrade = enrolmentEvaluation.getGrade();
        Date otherWhenAltered = enrolmentEvaluation.getWhen();

        if (this.getEnrolment().getStudentCurricularPlan().getStudent().getDegreeType().equals(
                DegreeType.MASTER_DEGREE)) {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
        } else if (this.getObservation() != null && this.getObservation().equals(RECTIFICATION)
                && enrolmentEvaluation.getObservation() != null
                && enrolmentEvaluation.getObservation().equals(RECTIFICATION)) {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
        } else if (this.getObservation() != null && this.getObservation().equals(RECTIFICATION)) {
            return 1;
        } else if (enrolmentEvaluation.getObservation() != null
                && enrolmentEvaluation.getObservation().equals(RECTIFICATION)) {
            if (this.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT)) {
                return compareForEqualStates(myEnrolmentState, otherEnrolmentState, otherGrade,
                        otherWhenAltered);
            }
            return -1;
        } else if (myEnrolmentState.equals(otherEnrolmentState)) {
            return compareForEqualStates(myEnrolmentState, otherEnrolmentState, otherGrade,
                    otherWhenAltered);
        } else {
            return compareForNotEqualStates(myEnrolmentState, otherEnrolmentState);
        }
    }

    private int compareMyWhenAlteredDateToAnotherWhenAlteredDate(Date whenAltered) {
        if (this.getWhen() == null) {
            return -1;
        }
        if (whenAltered == null) {
            return 1;
        }
        if (this.getWhen().compareTo(whenAltered) >= 0) {
            return 1;
        }

        return -1;

    }

    private int compareMyGradeToAnotherGrade(String grade) {
        Integer myGrade = null;
        Integer otherGrade = null;
        if (this.getGrade() == null) {
            myGrade = new Integer(0);
        } else {
            myGrade = Integer.valueOf(this.getGrade());
        }
        if (grade == null) {
            otherGrade = new Integer(0);
        } else {

            otherGrade = Integer.valueOf(grade);
        }

        if (myGrade.intValue() >= otherGrade.intValue()) {
            return 1;
        }

        return -1;

    }

    private int compareForEqualStates(EnrollmentState myEnrolmentState,
            EnrollmentState otherEnrolmentState, String otherGrade, Date otherWhenAltered) {
        if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
            return compareMyGradeToAnotherGrade(otherGrade);
        }
        return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
    }

    private int compareForNotEqualStates(EnrollmentState myEnrolmentState,
            EnrollmentState otherEnrolmentState) {
        if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
            return 1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED)
                && otherEnrolmentState.equals(EnrollmentState.APROVED)) {
            return -1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED)) {
            return 1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_EVALUATED)) {
            return -1;
        } else {
            return 0;
        }
    }

    public EnrollmentState getEnrollmentStateByGrade() {
        return getEnrollmentStateByGrade(getGrade());
    }

    private EnrollmentState getEnrollmentStateByGrade(String grade) {
        if (grade == null) {
            return EnrollmentState.NOT_EVALUATED;
        }
        if (grade.equals("")) {
            return EnrollmentState.NOT_EVALUATED;
        }
        if (grade.equals("0")) {
            return EnrollmentState.NOT_EVALUATED;
        }
        if (grade.equals("NA")) {
            return EnrollmentState.NOT_EVALUATED;
        }
        if (grade.equals("RE")) {
            return EnrollmentState.NOT_APROVED;
        }
        if (grade.equals("AP")) {
            return EnrollmentState.APROVED;
        }
        return EnrollmentState.APROVED;
    }

    public boolean isNormal() {
        return getEnrolmentEvaluationType() == EnrolmentEvaluationType.NORMAL;
    }

    public boolean isImprovment() {
        return getEnrolmentEvaluationType() == EnrolmentEvaluationType.IMPROVEMENT;
    }

    public boolean isSpecialSeason() {
        return getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON;
    }

    public boolean isNotEvaluated() {
        return getEnrollmentStateByGrade() == EnrollmentState.NOT_EVALUATED;
    }

    public boolean isFlunked() {
        return getEnrollmentStateByGrade() == EnrollmentState.NOT_APROVED;
    }

    public boolean isApproved() {
        return getEnrollmentStateByGrade() == EnrollmentState.APROVED;
    }
    
    public void edit(Person responsibleFor, Date evaluationDate) {
        if (responsibleFor == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setPersonResponsibleForGrade(responsibleFor);
        setExamDate(evaluationDate);
        generateCheckSum();
    }

    public void edit(Person responsibleFor, String grade, Date availableDate, Date examDate) {
        if (responsibleFor == null) {
            throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
        }
        setGrade(grade);
        setGradeAvailableDate(availableDate);
        setPersonResponsibleForGrade(responsibleFor);

        if (examDate != null) {
            setExamDate(examDate);
        } else if (grade == null) {
            setExamDate(null);
        } else {
            setExamDate(availableDate);
        }
        generateCheckSum();
    }

    public void confirmSubmission(Employee employee, String observation) {
    	confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, employee, observation);
    }
    
    public void confirmSubmission(EnrolmentEvaluationState enrolmentEvaluationState, Employee employee, String observation) {
        
        if(enrolmentEvaluationState == EnrolmentEvaluationState.RECTIFICATION_OBJ && hasRectified()) {
            getRectified().setEnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFIED_OBJ);
        }
        
        setEnrolmentEvaluationState(enrolmentEvaluationState); // TODO:
        setWhenDateTime(new DateTime());
        setEmployee(employee);
        setObservation(observation);

        EnrollmentState newEnrolmentState = EnrollmentState.APROVED;
        if (MarkType.getRepMarks().contains(getGrade())) {
            newEnrolmentState = EnrollmentState.NOT_APROVED;
        } else if (MarkType.getNaMarks().contains(getGrade())) {
            newEnrolmentState = EnrollmentState.NOT_EVALUATED;
        }
        this.getEnrolment().setEnrollmentState(newEnrolmentState);
    }

    public boolean getCanBeDeleted() {
        return isTemporary();
    }

    public boolean isTemporary() {
        return getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ);
    }
    
    public boolean isFinal() {
        return getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ)
        		|| getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)
        		|| getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ);
    }

    public void delete() {

        if (!getCanBeDeleted()) {
            throw new DomainException("error.enrolmentEvaluation.cannot.be.deleted");
        }

        removePersonResponsibleForGrade();
        removeEmployee();
        removeEnrolment();
        removeMarkSheet();
        removeRectification();
        removeRectified();

        removeRootDomainObject();
        super.deleteDomainObject();
    }
    
    public void removeFromMarkSheet() {
	if (!getCanBeDeleted()) {
            throw new DomainException("error.enrolmentEvaluation.cannot.be.deleted");
        }
	
	setCheckSum(null);
	setExamDateYearMonthDay(null);
	setGradeAvailableDateYearMonthDay(null);
	
        removeMarkSheet();
    }

    public void insertStudentFinalEvaluationForMasterDegree(String grade, Person responsibleFor,
            Date examDate) throws DomainException {

        DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan()
                .getDegreeCurricularPlan();

        if (grade == null || grade.length() == 0) {

            if (getGrade() != null && getGrade().length() > 0)
                edit(null, null, null, null);
        }

        else if (grade != null && grade.length() > 0) {

            if (degreeCurricularPlan.isGradeValid(grade)) {
                Calendar calendar = Calendar.getInstance();
                edit(responsibleFor, grade, calendar.getTime(), examDate);
            }

            else
                throw new DomainException("error.invalid.grade");
        }
    }

    public void alterStudentEnrolmentEvaluationForMasterDegree(String grade, Employee employee,
            Person responsibleFor, EnrolmentEvaluationType evaluationType, Date evaluationAvailableDate,
            Date examDate, String observation) throws DomainException {

        Enrolment enrolment = getEnrolment();
        DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan()
                .getDegreeCurricularPlan();

        if (grade.equals("0") || grade.equals("")) {

            EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment,
                    getEnrolmentEvaluationType());
            enrolmentEvaluation.confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, employee, observation);
            enrolment.setEnrollmentState(EnrollmentState.ENROLLED);

        } else {

            if (degreeCurricularPlan.isGradeValid(grade)) {

                EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment,
                        evaluationType);
                enrolmentEvaluation.edit(responsibleFor, grade, evaluationAvailableDate, examDate);
                enrolmentEvaluation.confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, employee, observation);
            }

            else
                throw new DomainException("error.invalid.grade");
        }
    }

    public IGrade getGradeWrapper() {
        return GradeFactory.getInstance().getGrade(getGrade());
    }

    protected void generateCheckSum() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getExamDateYearMonthDay().toString()).append(getGrade());
        stringBuilder.append(getEnrolmentEvaluationType());
        stringBuilder.append(getEnrolment().getStudentCurricularPlan().getStudent().getNumber());
        setCheckSum(FenixDigestUtils.createDigest(stringBuilder.toString()));
    }
    
    /*    @Override
    public void setEnrolmentEvaluationState(EnrolmentEvaluationState enrolmentEvaluationState) {
        checkNewEnrolmentEvaluationState(enrolmentEvaluationState);
        super.setEnrolmentEvaluationState(enrolmentEvaluationState);
        this.getEnrolment().calculateNewEnrolmentState(enrolmentEvaluationState);
    }

    private void checkNewEnrolmentEvaluationState(EnrolmentEvaluationState enrolmentEvaluationState) {
        if (this.getEnrolmentEvaluationState() != null) {
            if(this.getEnrolmentEvaluationState().getWeight() > enrolmentEvaluationState.getWeight()){
                throw new DomainException("invalid enrolmentEvaluationState");
            }
        }
    }
    */

    @Override
    public void setGrade(String grade) {
        super.setGrade((grade != null) ? grade.toUpperCase() : null);
    }
    
    public Registration getStudent() {
        return this.getEnrolment().getStudentCurricularPlan().getStudent();
    }
    
    public MarkSheet getRectificationMarkSheet() {
        if (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ) && hasRectification()) {
            return getRectification().getMarkSheet();
        } else {
            return null;
        }
    }

}
