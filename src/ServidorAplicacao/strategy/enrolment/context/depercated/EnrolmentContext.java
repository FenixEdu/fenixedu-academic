package ServidorAplicacao.strategy.enrolment.context.depercated;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;

/**
 * @author jpvl
 */

public final class EnrolmentContext {

	/**
	 * Map like this: key = curricularCourseCode + curricularCourseName
	 */
	private Map acumulatedEnrolments;

	private IStudent student;
	
	private IExecutionPeriod executionPeriod;
	private IStudentCurricularPlan studentActiveCurricularPlan;
	private EnrolmentValidationResult enrolmentValidationResult;
	private ICurso chosenOptionalDegree;
	private ICurricularCourseScope chosenOptionalCurricularCourseScope;
    private ICurricularCourse chosenOptionalCurricularCourse;

	private List enrolmentsAprovedByStudent;
	private List actualEnrolments;
	private List curricularCoursesFromStudentCurricularPlan;
	private List finalCurricularCoursesScopesSpanToBeEnrolled;
    private List finalCurricularCoursesSpanToBeEnrolled;
	private List curricularCoursesScopesAutomaticalyEnroled;
    private List curricularCoursesAutomaticalyEnroled;

	private List degreesForOptionalCurricularCourses;
	private List optionalCurricularCoursesToChooseFromDegree;
	private List optionalCurricularCoursesEnrolments;
   
    
	protected EnrolmentContext() {
		super();
		actualEnrolments = new ArrayList();
	}

	public List getEnrolmentsAprovedByStudent() {
		return this.enrolmentsAprovedByStudent;
	}

	public boolean isCurricularCourseDone(ICurricularCourse curricularCourse){
		
		Iterator iterator = enrolmentsAprovedByStudent.iterator();
		while (iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if(enrolment instanceof IEnrolmentInOptionalCurricularCourse) {
				if (((IEnrolmentInOptionalCurricularCourse) enrolment).getCurricularCourseForOption().equals(curricularCourse)) {
					return true;
				}
			} else {
				if (enrolment.getCurricularCourse().equals(curricularCourse)) {
					return true;
				}
			}
		}
		return false;
	}

	public Integer getSemester() {
		return this.executionPeriod.getSemester();
	}

	public IStudent getStudent() {
		return student;
	}

	public void setEnrolmentsAprovedByStudent(List list) {
		this.enrolmentsAprovedByStudent = list;
	}

	public void setStudent(IStudent student) {
		this.student = student;
	}

    /**
     * @deprecated
     */
	public List getFinalCurricularCoursesScopesSpanToBeEnrolled() {
		return finalCurricularCoursesScopesSpanToBeEnrolled;
	}
    /**
     * @deprecated
     */
	public void setFinalCurricularCoursesScopesSpanToBeEnrolled(List list) {
		finalCurricularCoursesScopesSpanToBeEnrolled = list;
	}

	/**
	 * @deprecated
	 */
	public Map getAcumulatedEnrolments() {
		return acumulatedEnrolments;
	}
	
	public Integer getCurricularCourseAcumulatedEnrolments(ICurricularCourse curricularCourse){
		Integer curricularCourseAcumulatedEnrolments = (Integer) this.acumulatedEnrolments.get(curricularCourse.getCode()+curricularCourse.getName()); 
		if (curricularCourseAcumulatedEnrolments == null){
			curricularCourseAcumulatedEnrolments = new Integer (0);
		}
		return curricularCourseAcumulatedEnrolments;
	}

	public void setAcumulatedEnrolments(Map map) {
		acumulatedEnrolments = map;
	}

	public List getActualEnrolments() {
		return actualEnrolments;
	}

	public IStudentCurricularPlan getStudentActiveCurricularPlan() {
		return studentActiveCurricularPlan;
	}

	public void setActualEnrolments(List list) {
		actualEnrolments = list;
	}

	public void setStudentActiveCurricularPlan(IStudentCurricularPlan plan) {
		studentActiveCurricularPlan = plan;
	}

	public List getCurricularCoursesFromStudentCurricularPlan() {
		return curricularCoursesFromStudentCurricularPlan;
	}

	/**
	 * Sets the curricularCoursesFromStudentCurricularPlan.
	 * @param curricularCoursesFromStudentCurricularPlan The curricularCoursesFromStudentCurricularPlan to set
	 */
	public void setCurricularCoursesFromStudentCurricularPlan(List curricularCoursesFromStudentCurricularPlan) {
		this.curricularCoursesFromStudentCurricularPlan = curricularCoursesFromStudentCurricularPlan;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "acumulatedEnrolments = " + this.acumulatedEnrolments + "\n";
		result += "StudentActiveCurricularPlan = " + this.studentActiveCurricularPlan + "\n";
		result += "actualEnrolments = " + this.actualEnrolments + "\n";
		result += "finalCurricularCoursesScopesSpanToBeEnrolled = " + this.finalCurricularCoursesScopesSpanToBeEnrolled + "\n";
		result += "enrolmentValidationResult = " + this.enrolmentValidationResult + "\n";
		result += "chosenOptionalDegree = " + this.chosenOptionalDegree + "\n";
		result += "degreesForOptionalCurricularCourses = " + this.degreesForOptionalCurricularCourses + "\n";
		result += "optionalCurricularCoursesToChooseFromDegree = " + this.optionalCurricularCoursesToChooseFromDegree + "\n";
		result += "student = " + this.student + "]";
		return result;
	}	

	/**
	 * @return EnrolmentValidationResult
	 */
	public EnrolmentValidationResult getEnrolmentValidationResult() {
		return enrolmentValidationResult;
	}

	/**
	 * Sets the enrolmentValidationResult.
	 * @param enrolmentValidationResult The enrolmentValidationResult to set
	 */
	public void setEnrolmentValidationResult(EnrolmentValidationResult enrolmentValidationError) {
		this.enrolmentValidationResult = enrolmentValidationError;
	}

	/**
     * @deprecated
	 * @return List
	 */
	public List getCurricularCoursesScopesAutomaticalyEnroled() {
		return curricularCoursesScopesAutomaticalyEnroled;
	}

	/**
     * @deprecated
	 * Sets the curricularCoursesScopesAutomaticalyEnroled.
	 * @param curricularCoursesScopesAutomaticalyEnroled The curricularCoursesScopesAutomaticalyEnroled to set
	 */
	public void setCurricularCoursesScopesAutomaticalyEnroled(List curricularCoursesEnroledByStudent) {
		this.curricularCoursesScopesAutomaticalyEnroled = curricularCoursesEnroledByStudent;
	}

	/**
	 * @return List
	 */
	public List getDegreesForOptionalCurricularCourses() {
		return degreesForOptionalCurricularCourses;
	}

	/**
	 * @return List
	 */
	public List getOptionalCurricularCoursesToChooseFromDegree() {
		return optionalCurricularCoursesToChooseFromDegree;
	}

	/**
	 * Sets the degreesForOptionalCurricularCourses.
	 * @param degreesForOptionalCurricularCourses The degreesForOptionalCurricularCourses to set
	 */
	public void setDegreesForOptionalCurricularCourses(List degreesForOptionalCurricularCourses) {
		this.degreesForOptionalCurricularCourses = degreesForOptionalCurricularCourses;
	}

	/**
	 * Sets the optionalCurricularCoursesToChooseFromDegree.
	 * @param optionalCurricularCoursesToChooseFromDegree The optionalCurricularCoursesToChooseFromDegree to set
	 */
	public void setOptionalCurricularCoursesToChooseFromDegree(List optionalCurricularCoursesToChooseFromDegree) {
		this.optionalCurricularCoursesToChooseFromDegree = optionalCurricularCoursesToChooseFromDegree;
	}

	/**
	 * @return ICurso
	 */
	public ICurso getChosenOptionalDegree() {
		return chosenOptionalDegree;
	}

	/**
	 * Sets the chosenOptionalDegree.
	 * @param chosenOptionalDegree The chosenOptionalDegree to set
	 */
	public void setChosenOptionalDegree(ICurso chosenDegree) {
		this.chosenOptionalDegree = chosenDegree;
	}

	/**
	 * @return List
	 */
	public List getOptionalCurricularCoursesEnrolments() {
		return optionalCurricularCoursesEnrolments;
	}

	/**
	 * Sets the optionalCurricularCoursesEnrolments.
	 * @param optionalCurricularCoursesEnrolments The optionalCurricularCoursesEnrolments to set
	 */
	public void setOptionalCurricularCoursesEnrolments(List optionalCurricularCoursesEquivalences) {
		this.optionalCurricularCoursesEnrolments = optionalCurricularCoursesEquivalences;
	}

	/**
     * @deprecated
	 * @return ICurricularCourse
	 */
	public ICurricularCourseScope getChosenOptionalCurricularCourseScope() {
		return chosenOptionalCurricularCourseScope;
	}

	/**
     * @deprecated
	 * Sets the chosenOptionalCurricularCourseScope.
	 * @param chosenOptionalCurricularCourseScope The chosenOptionalCurricularCourseScope to set
	 */
	public void setChosenOptionalCurricularCourseScope(ICurricularCourseScope chosenOptionalCurricularCourse) {
		this.chosenOptionalCurricularCourseScope = chosenOptionalCurricularCourse;
	}

	/**
	 * @return IExecutionPeriod
	 */
	public IExecutionPeriod getExecutionPeriod() {
		return executionPeriod;
	}

	/**
	 * Sets the executionPeriod.
	 * @param executionPeriod The executionPeriod to set
	 */
	public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
		this.executionPeriod = executionPeriod;
	}

    

    /**
     * @return Returns the finalCurricularCoursesSpanToBeEnrolled.
     */
    public List getFinalCurricularCoursesSpanToBeEnrolled()
    {
        return finalCurricularCoursesSpanToBeEnrolled;
    }

    /**
     * @param finalCurricularCoursesSpanToBeEnrolled The finalCurricularCoursesSpanToBeEnrolled to set.
     */
    public void setFinalCurricularCoursesSpanToBeEnrolled(List finalCurricularCoursesSpanToBeEnrolled)
    {
        this.finalCurricularCoursesSpanToBeEnrolled = finalCurricularCoursesSpanToBeEnrolled;
    }

    /**
     * @return Returns the curricularCoursesAutomaticalyEnroled.
     */
    public List getCurricularCoursesAutomaticalyEnroled()
    {
        return curricularCoursesAutomaticalyEnroled;
    }

    /**
     * @param curricularCoursesAutomaticalyEnroled The curricularCoursesAutomaticalyEnroled to set.
     */
    public void setCurricularCoursesAutomaticalyEnroled(List curricularCoursesAutomaticalyEnroled)
    {
        this.curricularCoursesAutomaticalyEnroled = curricularCoursesAutomaticalyEnroled;
    }

    /**
     * @return Returns the chosenOptionalCurricularCourse.
     */
    public ICurricularCourse getChosenOptionalCurricularCourse()
    {
        return chosenOptionalCurricularCourse;
    }

    /**
     * @param chosenOptionalCurricularCourse The chosenOptionalCurricularCourse to set.
     */
    public void setChosenOptionalCurricularCourse(ICurricularCourse chosenOptionalCurricularCourse)
    {
        this.chosenOptionalCurricularCourse = chosenOptionalCurricularCourse;
    }

}
