package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentNotPayedException;
import net.sourceforge.fenixedu.domain.exceptions.InDebtEnrolmentsException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.predicates.MarkSheetPredicates;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.FenixDigestUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.DateTime;

public class MarkSheet extends MarkSheet_Base {

    protected MarkSheet() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCreationDateDateTime(new DateTime());
        setPrinted(Boolean.FALSE);
    }
    
    private MarkSheet(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType,
            MarkSheetState markSheetState, Boolean submittedByTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Employee employee) {

        this();
        checkParameters(curricularCourse, executionPeriod, responsibleTeacher, evaluationDate, markSheetType, markSheetState, evaluationBeans, employee);
        init(curricularCourse, executionPeriod, responsibleTeacher, evaluationDate, markSheetType, markSheetState, submittedByTeacher, employee);

        if (hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
            addEnrolmentEvaluationsWithoutResctrictions(responsibleTeacher, evaluationBeans, EnrolmentEvaluationState.TEMPORARY_OBJ);
        } else {
            addEnrolmentEvaluationsWithResctrictions(responsibleTeacher, evaluationBeans, EnrolmentEvaluationState.TEMPORARY_OBJ);
        }
        generateCheckSum();
    }
    
    
    public static MarkSheet createNormal(CurricularCourse curricularCourse,
            ExecutionPeriod executionPeriod, Teacher responsibleTeacher, Date evaluationDate,
            MarkSheetType markSheetType, Boolean submittedByTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Employee employee) {
        
        return new MarkSheet(curricularCourse, executionPeriod, responsibleTeacher, evaluationDate,
                markSheetType, MarkSheetState.NOT_CONFIRMED, submittedByTeacher, evaluationBeans, employee);
    }
    
    public static MarkSheet createRectification(CurricularCourse curricularCourse,
            ExecutionPeriod executionPeriod, Teacher responsibleTeacher, Date evaluationDate,
            MarkSheetType markSheetType, String reason, MarkSheetEnrolmentEvaluationBean evaluationBean, Employee employee) {

        MarkSheet markSheet = new MarkSheet(curricularCourse, executionPeriod, responsibleTeacher, evaluationDate,
                markSheetType, MarkSheetState.RECTIFICATION_NOT_CONFIRMED, Boolean.FALSE,
                (evaluationBean != null) ? Collections.singletonList(evaluationBean) : null, employee);
        markSheet.setReason(reason);
        return markSheet;
    }

    private void checkParameters(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType,
            MarkSheetState markSheetState, Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Employee employee) {

        if (curricularCourse == null || executionPeriod == null || responsibleTeacher == null
                || evaluationDate == null || markSheetType == null || markSheetState == null || employee == null) {
            throw new DomainException("error.markSheet.invalid.arguments");
        }
        if (evaluationBeans == null || evaluationBeans.size() == 0) {
            throw new DomainException("error.markSheet.create.with.invalid.enrolmentEvaluations.number");
        }
        checkIfTeacherIsResponsibleOrCoordinator(curricularCourse, executionPeriod, responsibleTeacher, markSheetType);
        checkIfEvaluationDateIsInExamsPeriod(curricularCourse, getExecutionDegree(curricularCourse, executionPeriod),
                executionPeriod, evaluationDate, markSheetType);
    }

    private void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    Teacher responsibleTeacher, MarkSheetType markSheetType) throws DomainException {

	if (curricularCourse.isDissertation()) {
	    if (responsibleTeacher.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
		return;
	    }
	    for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
		if (executionCourse.getExecutionPeriod().getExecutionYear() == executionPeriod.getExecutionYear()) {
		    for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
			if (professorship.isResponsibleFor() && professorship.getTeacher() == responsibleTeacher) {
			    return;
			}
		    }
		}
	    }
	}

    	if (markSheetType == MarkSheetType.IMPROVEMENT
		&& curricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod).isEmpty()) {

	    if (!responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse,
		    executionPeriod.getPreviousExecutionPeriod())
		    && !responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse,
			    executionPeriod.getPreviousExecutionPeriod().getPreviousExecutionPeriod())
		    && !responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(
			    curricularCourse,
			    executionPeriod.getPreviousExecutionPeriod().getPreviousExecutionPeriod()
				    .getPreviousExecutionPeriod())) {
		throw new DomainException("error.teacherNotResponsibleOrNotCoordinator");
	    }
	    
	} else if (!responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse, executionPeriod)) {
	    throw new DomainException("error.teacherNotResponsibleOrNotCoordinator");
	}
	
    }
    
    private void checkIfEvaluationDateIsInExamsPeriod(CurricularCourse curricularCourse, ExecutionDegree executionDegree,
	    ExecutionPeriod executionPeriod, Date evaluationDate, MarkSheetType markSheetType) throws DomainException {

	if (executionDegree == null) {
	    if (!markSheetType.equals(MarkSheetType.IMPROVEMENT)
		    || !curricularCourse.getDegreeCurricularPlan().canSubmitImprovementMarkSheets(
			    executionPeriod.getExecutionYear())) {
		throw new DomainException("error.evaluationDateNotInExamsPeriod");
	    }

	} else if (!executionDegree.isEvaluationDateInExamPeriod(evaluationDate, executionPeriod, markSheetType)) {

	    OccupationPeriod occupationPeriod = executionDegree.getOccupationPeriodFor(executionPeriod, markSheetType);
	    if (occupationPeriod == null) {
		throw new DomainException("error.evaluationDateNotInExamsPeriod");
	    } else {
		throw new DomainException("error.evaluationDateNotInExamsPeriod.withEvaluationDateAndPeriodDates", DateFormatUtil
			.format("dd/MM/yyyy", evaluationDate), occupationPeriod.getStartYearMonthDay().toString("dd/MM/yyyy"),
			occupationPeriod.getEndYearMonthDay().toString("dd/MM/yyyy"));
	    }
	}
    }
    
    private ExecutionDegree getExecutionDegree(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
        return curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(executionPeriod.getExecutionYear());
    }

    private void init(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType,
            MarkSheetState markSheetState, Boolean submittedByTeacher, Employee employee) {
        
        setMarkSheetState(markSheetState);
        setCurricularCourse(curricularCourse);
        setExecutionPeriod(executionPeriod);
        setResponsibleTeacher(responsibleTeacher);
        setEvaluationDate(evaluationDate);
        setMarkSheetType(markSheetType);
        setSubmittedByTeacher(submittedByTeacher);
        setCreationEmployee(employee);
    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void addEnrolmentEvaluationsWithoutResctrictions(Teacher responsibleTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans,
            EnrolmentEvaluationState enrolmentEvaluationState) {

        final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());
        
        for (final MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {
            
            checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(), evaluationBean
                    .getEvaluationDate(), getMarkSheetType());
            
            final EnrolmentEvaluation enrolmentEvaluation = evaluationBean.getEnrolment()
                    .addNewEnrolmentEvaluation(enrolmentEvaluationState,
                            getMarkSheetType().getEnrolmentEvaluationType(),
                            responsibleTeacher.getPerson(), evaluationBean.getGradeValue(),
                            getCreationDate(), evaluationBean.getEvaluationDate(), getExecutionPeriod());
            
            addEnrolmentEvaluations(enrolmentEvaluation);
        }
    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void addEnrolmentEvaluationsWithResctrictions(Teacher responsibleTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans,
            EnrolmentEvaluationState enrolmentEvaluationState) {

        final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(),getExecutionPeriod());
        final Set<Enrolment> enrolmentsNotInAnyMarkSheet = getCurricularCourse().getEnrolmentsNotInAnyMarkSheet(getMarkSheetType(), getExecutionPeriod());

        Set<Enrolment> notPayedEnrolments = new HashSet<Enrolment>();
        for (final MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {
            
            if (enrolmentsNotInAnyMarkSheet.contains(evaluationBean.getEnrolment())) {
        	try {
        	    addEnrolmentEvaluationToMarkSheet(responsibleTeacher, enrolmentEvaluationState,
                        evaluationBean, executionDegree);
        	}catch(EnrolmentNotPayedException e) {
        	    notPayedEnrolments.add(e.getEnrolment());
        	}
            } else {
                // TODO:
                throw new DomainException("error.markSheet");
            }
        }
    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void addEnrolmentEvaluationToMarkSheet(Teacher responsibleTeacher,
            EnrolmentEvaluationState enrolmentEvaluationState,
            final MarkSheetEnrolmentEvaluationBean evaluationBean, ExecutionDegree executionDegree) {
        
        checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(),
                evaluationBean.getEvaluationDate(), getMarkSheetType());

        EnrolmentEvaluation enrolmentEvaluation = evaluationBean.getEnrolment()
                .getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(enrolmentEvaluationState,
                        getMarkSheetType().getEnrolmentEvaluationType());

        if (enrolmentEvaluation == null) {
            enrolmentEvaluation = evaluationBean.getEnrolment()
                    .addNewEnrolmentEvaluation(enrolmentEvaluationState,
                            getMarkSheetType().getEnrolmentEvaluationType(),
                            responsibleTeacher.getPerson(), evaluationBean.getGradeValue(),
                            getCreationDate(), evaluationBean.getEvaluationDate(), getExecutionPeriod());
        } else {
            enrolmentEvaluation
                    .edit(responsibleTeacher.getPerson(), evaluationBean.getGradeValue(),
                            getCreationDate(), evaluationBean.getEvaluationDate());
        }
        addEnrolmentEvaluations(enrolmentEvaluation);
    }

    private boolean hasMarkSheetState(MarkSheetState markSheetState) {
        return (getMarkSheetState() == markSheetState);
    }

    public boolean isNotConfirmed() {
        return hasMarkSheetState(MarkSheetState.NOT_CONFIRMED) || hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
    }

    public boolean isConfirmed() {
        return hasMarkSheetState(MarkSheetState.CONFIRMED) || hasMarkSheetState(MarkSheetState.RECTIFICATION);
    }
    
    public boolean isRectification() {
	return hasMarkSheetState(MarkSheetState.RECTIFICATION) || hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
    }
    
    @Checked("MarkSheetPredicates.editPredicate")
    public void editNormal(Teacher responsibleTeacher, Date newEvaluationDate) {
        
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
            
        } else if(hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
            throw new DomainException("error.markSheet.wrong.edit.method");
            
        } else {
            checkIfTeacherIsResponsibleOrCoordinator(getCurricularCourse(), getExecutionPeriod(), responsibleTeacher, getMarkSheetType());
            checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), getExecutionDegree(getCurricularCourse(),
                    getExecutionPeriod()), getExecutionPeriod(), newEvaluationDate, getMarkSheetType());
            
            Date oldEvaluationDate = getEvaluationDateDateTime().toDate();
            setResponsibleTeacher(responsibleTeacher);
            setEvaluationDate(newEvaluationDate);
            
            editMarkSheetEnrolmentEvaluationsWithSameEvaluationDate(responsibleTeacher,
                    oldEvaluationDate, newEvaluationDate, getEnrolmentEvaluationsSet());
            
            generateCheckSum();
        }
    }
    
    @Checked("MarkSheetPredicates.editPredicate")
    public void editRectification(MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean) {
        
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
            
        } else if(hasMarkSheetState(MarkSheetState.NOT_CONFIRMED)) {
            throw new DomainException("error.markSheet.wrong.edit.method");
            
        } else if (enrolmentEvaluationBean == null) {
            throw new DomainException("error.markSheet.edit.with.invalid.enrolmentEvaluations.number");
                
        } else {
            editEnrolmentEvaluations(Collections.singletonList(enrolmentEvaluationBean));
            generateCheckSum();
        }
    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void editMarkSheetEnrolmentEvaluationsWithSameEvaluationDate(Teacher responsibleTeacher,
            Date oldEvaluationDate, Date newEvaluationDate,
            Set<EnrolmentEvaluation> enrolmentEvaluationsToEdit) {
        
        String dateFormat = "dd/MM/yyyy";
        final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());
        for (EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluationsToEdit) {
            if (DateFormatUtil.compareDates(dateFormat, enrolmentEvaluation.getExamDate(), oldEvaluationDate) == 0) {
                checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(), newEvaluationDate, getMarkSheetType());
                enrolmentEvaluation.edit(responsibleTeacher.getPerson(), newEvaluationDate);
            }
        }
    }
    
    @Checked("MarkSheetPredicates.editPredicate")
    public void editNormal(
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToEdit,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToAppend,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToRemove) {
        
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
            
        } else if(hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
            throw new DomainException("error.markSheet.wrong.edit.method");
            
        } else {
            checkIfEnrolmentEvaluationsNumberIsValid(evaluationBeansToAppend, evaluationBeansToRemove);

            editEnrolmentEvaluations(evaluationBeansToEdit);
            removeEnrolmentEvaluations(evaluationBeansToRemove);
            appendEnrolmentEvaluations(evaluationBeansToAppend);
            
            generateCheckSum();
        }
    }

    private void checkIfEnrolmentEvaluationsNumberIsValid(
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToAppend,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToRemove) {

        if (evaluationBeansToAppend.size() == 0
                && evaluationBeansToRemove.size() == getEnrolmentEvaluationsCount()) {
            throw new DomainException("error.markSheet.edit.with.invalid.enrolmentEvaluations.number");
        }
    }
    
    @Checked("MarkSheetPredicates.editPredicate")
    private void editEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToEdit) {
        
        final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());

        for (final MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : evaluationBeansToEdit) {

            if (this.getEnrolmentEvaluationsSet().contains(enrolmentEvaluationBean.getEnrolmentEvaluation())) {
                
                checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(),
                        enrolmentEvaluationBean.getEvaluationDate(), getMarkSheetType());
                
                final EnrolmentEvaluation enrolmentEvaluation = enrolmentEvaluationBean.getEnrolmentEvaluation();
                enrolmentEvaluation.edit(getResponsibleTeacher().getPerson(),
                        enrolmentEvaluationBean.getGradeValue(), new Date(),
                        enrolmentEvaluationBean.getEvaluationDate());
            } else {
                // TODO:
                throw new DomainException("error.markSheet");
            }
        }
    }
    
    @Checked("MarkSheetPredicates.editPredicate")
    private void removeEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToRemove) {
        for (MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : enrolmentEvaluationBeansToRemove) {
            enrolmentEvaluationBean.getEnrolmentEvaluation().removeFromMarkSheet();
            enrolmentEvaluationBean.getEnrolmentEvaluation().setGradeValue(null);
        }
    }

    private void appendEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToAppend) {
        addEnrolmentEvaluationsWithResctrictions(getResponsibleTeacher(), evaluationBeansToAppend, EnrolmentEvaluationState.TEMPORARY_OBJ);
    }

    
    @Checked("MarkSheetPredicates.confirmPredicate")
    public void confirm(Employee employee) {
    	if(employee == null) {
    		throw new DomainException("error.markSheet.invalid.arguments");
    	}
        if (isNotConfirmed()) {
            setConfirmationEmployee(employee);
            
            Set<Enrolment> inDebtEnrolments = new HashSet<Enrolment>();
            for (final EnrolmentEvaluation enrolmentEvaluation : this.getEnrolmentEvaluationsSet()) {
        	try {
        	    enrolmentEvaluation.confirmSubmission(getEnrolmentEvaluationStateToConfirm(), employee, "");
        	}catch (EnrolmentNotPayedException e) {
		    inDebtEnrolments.add(e.getEnrolment());
		}
            }
            
            if(!inDebtEnrolments.isEmpty()) {
        	throw new InDebtEnrolmentsException("EnrolmentEvaluation.cannot.set.grade.on.not.payed.enrolment.evaluation", inDebtEnrolments);
            }
            
            setConfirmationDateDateTime(new DateTime());
            setMarkSheetState(getMarkSheetStateToConfirm());
            
        } else {
        	throw new DomainException("error.markSheet.already.confirmed");
        }
    }

    private MarkSheetState getMarkSheetStateToConfirm() {
        if (this.getMarkSheetState() == MarkSheetState.NOT_CONFIRMED) {
            return MarkSheetState.CONFIRMED;
        } else {
            return MarkSheetState.RECTIFICATION;
        }
    }
    
    private EnrolmentEvaluationState getEnrolmentEvaluationStateToConfirm() {
        if (this.getMarkSheetState() == MarkSheetState.NOT_CONFIRMED) {
            return EnrolmentEvaluationState.FINAL_OBJ;
        } else {
            return EnrolmentEvaluationState.RECTIFICATION_OBJ;
        }
    }

    public boolean getCanBeDeleted() {
        return isNotConfirmed();
    }

    public void delete() {
        if (!getCanBeDeleted()) {
            throw new DomainException("error.markSheet.cannot.be.deleted");
        }
               
        removeExecutionPeriod();
        removeCurricularCourse();
        removeResponsibleTeacher();
        removeConfirmationEmployee();
        removeCreationEmployee();
        
        if(hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
            changeRectifiedEnrolmentEvaluationToPreviowsState();
            for (; !getEnrolmentEvaluations().isEmpty(); getEnrolmentEvaluations().get(0).delete());
        } else {
            for (; !getEnrolmentEvaluations().isEmpty(); getEnrolmentEvaluations().get(0).removeFromMarkSheet());
        }
        
        removeRootDomainObject();
        deleteDomainObject();
    }

    private void changeRectifiedEnrolmentEvaluationToPreviowsState() {
        EnrolmentEvaluation enrolmentEvaluation = this.getEnrolmentEvaluations().get(0).getRectified();
        enrolmentEvaluation.setEnrolmentEvaluationState((enrolmentEvaluation.getMarkSheet().getMarkSheetState() == MarkSheetState.RECTIFICATION) ? EnrolmentEvaluationState.RECTIFICATION_OBJ : EnrolmentEvaluationState.FINAL_OBJ);
    }

    protected void generateCheckSum() {
        if (isNotConfirmed()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getExecutionPeriod().getExecutionYear().getYear()).append(
                    getExecutionPeriod().getSemester());
            stringBuilder.append(getResponsibleTeacher().getTeacherNumber()).append(
                    getEvaluationDateDateTime().toString("yyyy/MM/dd"));
            stringBuilder.append(getMarkSheetType().getName());
            for (EnrolmentEvaluation enrolmentEvaluation : getEnrolmentEvaluationsSortedByStudentNumber()) {
                stringBuilder.append(enrolmentEvaluation.getCheckSum());
            }
            setCheckSum(FenixDigestUtils.createDigest(stringBuilder.toString()));
        }
    }

    public Set<EnrolmentEvaluation> getEnrolmentEvaluationsSortedByStudentNumber() {
        final Set<EnrolmentEvaluation> enrolmentEvaluations = new TreeSet<EnrolmentEvaluation>(
                EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);
        enrolmentEvaluations.addAll(getEnrolmentEvaluationsSet());
        return enrolmentEvaluations; 
    }
    
    public EnrolmentEvaluation getEnrolmentEvaluationByStudent(Student student) {
        for (EnrolmentEvaluation enrolmentEvaluation : this.getEnrolmentEvaluationsSet()) {
            if(enrolmentEvaluation.getEnrolment().getStudentCurricularPlan().getRegistration().getStudent().equals(student)) {
                return enrolmentEvaluation;
            }
        }
        return null;
    }

    /*
     * Override: Getters and Setters
     */

    @Override
    public void addEnrolmentEvaluations(EnrolmentEvaluation enrolmentEvaluations) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.addEnrolmentEvaluations(enrolmentEvaluations);
        }
    }

    @Override
    public void removeCurricularCourse() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.removeCurricularCourse();
        }
    }

    @Override
    public void removeConfirmationEmployee() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.removeConfirmationEmployee();
        }
    }
    
    @Override
    public void removeCreationEmployee() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.removeCreationEmployee();
        }
    }

    @Override
    public void removeEnrolmentEvaluations(EnrolmentEvaluation enrolmentEvaluations) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.removeEnrolmentEvaluations(enrolmentEvaluations);
        }
    }

    @Override
    public void removeExecutionPeriod() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.removeExecutionPeriod();
        }
    }

    @Override
    public void removeResponsibleTeacher() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.removeResponsibleTeacher();
        }
    }

    @Override
    public void removeRootDomainObject() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.removeRootDomainObject();
        }
    }

    @Override
    public void setCheckSum(String checkSum) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCheckSum(checkSum);
        }
    }

    @Override
    public void setConfirmationDate(Date confirmationDate) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setConfirmationDate(confirmationDate);
        }
    }

    @Override
    public void setCreationDate(Date creationDate) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCreationDate(creationDate);
        }
    }

    @Override
    public void setCurricularCourse(CurricularCourse curricularCourse) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCurricularCourse(curricularCourse);
        }
    }

    @Override
    public void setConfirmationEmployee(Employee confirmationEmployee) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setConfirmationEmployee(confirmationEmployee);
        }
    }
    
    @Override
    public void setCreationEmployee(Employee creationEmployee) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCreationEmployee(creationEmployee);
        }
    }

    @Override
    public void setEvaluationDate(Date evaluationDate) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setEvaluationDate(evaluationDate);
        }
    }

    @Override
    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setExecutionPeriod(executionPeriod);
        }
    }

    @Override
    public void setMarkSheetState(MarkSheetState markSheetState) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setMarkSheetState(markSheetState);
        }
    }

    @Override
    public void setMarkSheetType(MarkSheetType markSheetType) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setMarkSheetType(markSheetType);
        }
    }

    @Override
    public void setResponsibleTeacher(Teacher responsibleTeacher) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setResponsibleTeacher(responsibleTeacher);
        }
    }

    @Override
    public void setConfirmationDateDateTime(DateTime confirmationDateDateTime) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setConfirmationDateDateTime(confirmationDateDateTime);
        }
    }

    @Override
    public void setCreationDateDateTime(DateTime creationDateDateTime) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCreationDateDateTime(creationDateDateTime);
        }
    }

    @Override
    public void setEvaluationDateDateTime(DateTime evaluationDateDateTime) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setEvaluationDateDateTime(evaluationDateDateTime);
        }
    }


    @Override
    public void setReason(String reason) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setReason(reason);
        }
    }

    @Override
    public void setSubmittedByTeacher(Boolean submittedByTeacher) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setSubmittedByTeacher(submittedByTeacher);
        }
    }
    
    public String getPrettyCheckSum() {
	return FenixDigestUtils.getPrettyCheckSum(getCheckSum());
    }

    public boolean canManage(final Employee employee) {
        final DegreeCurricularPlan degreeCurricularPlan = getCurricularCourse().getDegreeCurricularPlan();
        return degreeCurricularPlan.canManageMarkSheetsForExecutionPeriod(employee, getExecutionPeriod());
    }

    public AdministrativeOfficeType getAdministrativeOfficeType() {
	return getCurricularCourse().getDegreeType().getAdministrativeOfficeType();
    }
    
    
    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void removeGrades(Collection<EnrolmentEvaluation> enrolmentEvaluations) {
	if(enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
	    if(getMarkSheetState() == MarkSheetState.CONFIRMED) {
		super.setMarkSheetState(MarkSheetState.NOT_CONFIRMED);
	    }
	    if(getMarkSheetState() == MarkSheetState.RECTIFICATION) {
		super.setMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
	    }	
	    
	    setConfirmationEmployee(null);
	    setConfirmationDateDateTime(null);
	    
	    for (EnrolmentEvaluation enrolmentEvaluation : getEnrolmentEvaluationsSet()) {
		if(enrolmentEvaluations.contains(enrolmentEvaluation)) {
		    if(enrolmentEvaluation.hasRectification()) {
			throw new DomainException("error.enrolment.evaluation.has.rectification");
		    }
		    removeEvaluationFromMarkSheet(enrolmentEvaluation);
		} else {
		    changeEvaluationStateToTemporaryState(enrolmentEvaluation);
		}
	    }
	    generateCheckSum();
	}
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    private void removeEvaluationFromMarkSheet(EnrolmentEvaluation enrolmentEvaluation) {
	changeEvaluationStateToTemporaryState(enrolmentEvaluation);
	enrolmentEvaluation.removeMarkSheet();
	enrolmentEvaluation.setGrade(Grade.createEmptyGrade());
	enrolmentEvaluation.setCheckSum(null);
	enrolmentEvaluation.setExamDateYearMonthDay(null);
	enrolmentEvaluation.removeEmployee();
	enrolmentEvaluation.setGradeAvailableDateYearMonthDay(null);
	enrolmentEvaluation.removePersonResponsibleForGrade();
	enrolmentEvaluation.removeRectified();
    }

    
    @Checked("RolePredicates.MANAGER_PREDICATE")
    private void changeEvaluationStateToTemporaryState(final EnrolmentEvaluation enrolmentEvaluation) {
	enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	
	Enrolment enrolment = enrolmentEvaluation.getEnrolment();
	
	if(enrolment.getAllFinalEnrolmentEvaluations().isEmpty()) {
	    enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
	} else {
	    enrolment.setEnrollmentState(enrolment.getLatestEnrolmentEvaluation().getEnrollmentStateByGrade());
	}
    }
    
    public boolean getCanRectify() {
	return isConfirmed() && MarkSheetPredicates.rectifyPredicate.evaluate(this);
    }
    
    public boolean getCanConfirm() {
	return isNotConfirmed() && MarkSheetPredicates.confirmPredicate.evaluate(this);
    }
    
    public boolean getCanEdit() {
	return isNotConfirmed() && MarkSheetPredicates.editPredicate.evaluate(this);
    }
    
    public boolean isDissertation() {
	return getCurricularCourse().isDissertation();
    }

    public String getStateDiscription() {
	StringBuilder stringBuilder = new StringBuilder();
	final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale());
	stringBuilder.append(enumerationResources.getString(getMarkSheetState().getName()).trim());
	if(getSubmittedByTeacher()) {
	    final ResourceBundle academicResources = ResourceBundle.getBundle("resources.AcademicAdminOffice", LanguageUtils.getLocale());
	    stringBuilder.append(" (").append(academicResources.getString("label.markSheet.submittedByTeacher").trim()).append(")");
	}
	return stringBuilder.toString();
    }
    
}