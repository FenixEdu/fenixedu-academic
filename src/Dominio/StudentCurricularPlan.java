package Dominio;

import java.util.Date;
import java.util.List;

import Util.Specialization;
import Util.StudentCurricularPlanState;

public class StudentCurricularPlan extends DomainObject implements IStudentCurricularPlan {

	protected Integer studentKey; 
	protected Integer branchKey;
	protected Integer degreeCurricularPlanKey;
	protected Specialization specialization;
	protected Double givenCredits;
	protected Date when;
	protected Integer secundaryBranchKey;
	protected IBranch secundaryBranch;
	
	protected IStudent student;
	protected IBranch branch;
	protected IDegreeCurricularPlan degreeCurricularPlan;
	protected IEmployee employee; 
	protected Date startDate;
	protected StudentCurricularPlanState currentState;
	protected List enrolments;
    protected Integer completedCourses;
    protected Double classification;
    protected Integer enrolledCourses;
	protected String observations;
	protected Integer employeeKey;

	public StudentCurricularPlan() {
		setStudent(null);
		setBranch(null);
		setDegreeCurricularPlan(null);
		setStartDate(null);
		setCurrentState(null);
		setDegreeCurricularPlanKey(null);
		setStudentKey(null);
		setBranchKey(null);
		setSpecialization(null);
	}

	public StudentCurricularPlan(IStudent student, IDegreeCurricularPlan degreeCurricularPlan, IBranch branch, Date startDate, StudentCurricularPlanState currentState) {
		this();
		setStudent(student);
		setDegreeCurricularPlan(degreeCurricularPlan);
		setBranch(branch);
		setStartDate(startDate);
		setCurrentState(currentState);
	}

	/**
	 * @param studentCurricularPlanId
	 */
	public StudentCurricularPlan(Integer studentCurricularPlanId) {
		setIdInternal(studentCurricularPlanId);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IStudentCurricularPlan) {
			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) obj;
			resultado = this.getStudent().equals(studentCurricularPlan.getStudent()) &&
									this.getDegreeCurricularPlan().equals(studentCurricularPlan.getDegreeCurricularPlan()) &&
									this.getCurrentState().equals(studentCurricularPlan.getCurrentState()) &&
									this.getBranch().equals(studentCurricularPlan.getBranch());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "internalCode = " + getIdInternal() + "; ";
		result += "student = " + this.student + "; ";
		result += "degreeCurricularPlan = " + this.degreeCurricularPlan + "; ";
		result += "startDate = " + this.startDate + "; ";
		result += "specialization = " + this.specialization + "; ";
		result += "currentState = " + this.currentState + "]\n";
		result += "when alter = " + this.when + "]\n";
		if (this.employee!=null) {
		result += "employee = " + this.employee.getPerson().getNome()  + "]\n";}
		return result;
	}

	

	/**
	 * Returns the degreeCurricularPlan.
	 * @return IDegreeCurricularPlan
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * Returns the degreeCurricularPlanKey.
	 * @return Integer
	 */
	public Integer getDegreeCurricularPlanKey() {
		return degreeCurricularPlanKey;
	}

	/**
	 * Returns the currentState.
	 * @return StudentCurricularPlanState
	 */
	public StudentCurricularPlanState getCurrentState() {
		return currentState;
	}

	/**
	 * Returns the startDate.
	 * @return Date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Returns the student.
	 * @return IStudent
	 */
	public IStudent getStudent() {
		return student;
	}

	/**
	 * Returns the studentKey.
	 * @return Integer
	 */
	public Integer getStudentKey() {
		return studentKey;
	}

	/**
	 * Sets the degreeCurricularPlan.
	 * @param degreeCurricularPlan The degreeCurricularPlan to set
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan courseCurricularPlan) {
		this.degreeCurricularPlan = courseCurricularPlan;
	}

	/**
	 * Sets the degreeCurricularPlanKey.
	 * @param degreeCurricularPlanKey The degreeCurricularPlanKey to set
	 */
	public void setDegreeCurricularPlanKey(Integer courseCurricularPlanKey) {
		this.degreeCurricularPlanKey = courseCurricularPlanKey;
	}

	/**
	 * Sets the currentState.
	 * @param currentState The currentState to set
	 */
	public void setCurrentState(StudentCurricularPlanState currentState) {
		this.currentState = currentState;
	}

	/**
	 * Sets the startDate.
	 * @param startDate The startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Sets the student.
	 * @param student The student to set
	 */
	public void setStudent(IStudent student) {
		this.student = student;
	}

	/**
	 * Sets the studentKey.
	 * @param studentKey The studentKey to set
	 */
	public void setStudentKey(Integer studentKey) {
		this.studentKey = studentKey;
	}

	/**
	 * @return IBranch
	 */
	public IBranch getBranch() {
		return branch;
	}

	/**
	 * @return Integer
	 */
	public Integer getBranchKey() {
		return branchKey;
	}

	/**
	 * Sets the branch.
	 * @param branch The branch to set
	 */
	public void setBranch(IBranch branch) {
		this.branch = branch;
	}

	/**
	 * Sets the branchKey.
	 * @param branchKey The branchKey to set
	 */
	public void setBranchKey(Integer branchKey) {
		this.branchKey = branchKey;
	}

	/**
	 * @return
	 */
	public Specialization getSpecialization() {
		return specialization;
	}

	/**
	 * @param specialization
	 */
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

	/**
	 * @return
	 */
	public Double getGivenCredits() {
		return givenCredits;
	}

	/**
	 * @param Given Credits
	 */
	public void setGivenCredits(Double givenCredits) {
		this.givenCredits = givenCredits;
	}

	/**
	 * @return Enrolments
	 */
	public List getEnrolments() {
		return enrolments;
	}

	/**
	 * @param list
	 */
	public void setEnrolments(List list) {
		enrolments = list;
	}


	/**
	 * @return
	 */
	public Integer getCompletedCourses()
	{
		return completedCourses;
	}

	/**
	 * @param integer
	 */
	public void setCompletedCourses(Integer integer)
	{
		completedCourses= integer;
	}

	/**
	 * @return
	 */
	public Integer getEnrolledCourses()
	{
		return enrolledCourses;
	}

	/**
	 * @param integer
	 */
	public void setEnrolledCourses(Integer integer)
	{
		enrolledCourses= integer;
	}

	/**
	 * @return
	 */
	public Double getClassification()
	{
		return classification;
	}

	/**
	 * @param double1
	 */
	public void setClassification(Double double1)
	{
		classification= double1;
	}

	/**
	 * @return
	 */
	public IEmployee getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 */
	public void setEmployee(IEmployee employee) {
		this.employee = employee;
	}

	/**
	 * @return
	 */
	public Integer getEmployeeKey() {
		return employeeKey;
	}

	/**
	 * @param employeeKey
	 */
	public void setEmployeeKey(Integer employeeKey) {
		this.employeeKey = employeeKey;
	}

	/**
	 * @return
	 */
	public Date getWhen() {
		return when;
	}

	/**
	 * @param when
	 */
	public void setWhen(Date when) {
		this.when = when;
	}

	/**
	 * @return
	 */
	public String getObservations() {
		return observations;
	}

	/**
	 * @param observations
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return Returns the secundaryBranch.
	 */
	public IBranch getSecundaryBranch()
	{
		return secundaryBranch;
	}

	/**
	 * @param secundaryBranch The secundaryBranch to set.
	 */
	public void setSecundaryBranch(IBranch secundaryBranch)
	{
		this.secundaryBranch = secundaryBranch;
	}

	/**
	 * @return Returns the secundaryBranchKey.
	 */
	public Integer getSecundaryBranchKey()
	{
		return secundaryBranchKey;
	}

	/**
	 * @param secundaryBranchKey The secundaryBranchKey to set.
	 */
	public void setSecundaryBranchKey(Integer secundaryBranchKey)
	{
		this.secundaryBranchKey = secundaryBranchKey;
	}

}
