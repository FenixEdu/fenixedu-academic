package DataBeans;
import java.util.Date;
import java.util.List;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
/**
 * @author dcs-rjao
 * 
 * 22/Abr/2003
 */
public class InfoEnrolment extends InfoObject {
	private InfoStudentCurricularPlan infoStudentCurricularPlan;
	private InfoCurricularCourse infoCurricularCourse;
	private InfoExecutionPeriod infoExecutionPeriod;
	private EnrolmentState enrolmentState;
	private EnrolmentEvaluationType enrolmentEvaluationType;
	private Date creationDate;
	// to be used to keep the actual enrolment evaluation
	private InfoEnrolmentEvaluation infoEnrolmentEvaluation;
	private List infoEvaluations;
	public InfoEnrolment() {
	}
	public InfoEnrolment(InfoStudentCurricularPlan infoStudentCurricularPlan,
			InfoCurricularCourse infoCurricularCourse, EnrolmentState state,
			InfoExecutionPeriod infoExecutionPeriod) {
		this();
		setInfoCurricularCourse(infoCurricularCourse);
		setInfoStudentCurricularPlan(infoStudentCurricularPlan);
		setEnrolmentState(state);
		setInfoExecutionPeriod(infoExecutionPeriod);
	}
	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoEnrolment) {
			InfoEnrolment enrolment = (InfoEnrolment) obj;
			resultado = this.getInfoStudentCurricularPlan().equals(
					enrolment.getInfoStudentCurricularPlan())
					&& this.getInfoCurricularCourse().equals(
							enrolment.getInfoCurricularCourse())
					&& this.getInfoExecutionPeriod().equals(
							enrolment.getInfoExecutionPeriod());
		}
		return resultado;
	}
	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "infoStudentCurricularPlan = "
				+ this.infoStudentCurricularPlan + "; ";
		result += "infoExecutionPeriod = " + this.infoExecutionPeriod + "; ";
		result += "state = " + this.enrolmentState + "; ";
		result += "infoCurricularCourse = " + this.infoCurricularCourse + "; ";
		result += "enrolmentEvaluationType = " + this.enrolmentEvaluationType
				+ "; ";
		result += "infoEvaluations = " + this.infoEvaluations + "]\n";
		return result;
	}
	public InfoCurricularCourse getInfoCurricularCourse() {
		return infoCurricularCourse;
	}
	/**
	 * @return InfoExecutionPeriod
	 */
	public InfoExecutionPeriod getInfoExecutionPeriod() {
		return infoExecutionPeriod;
	}
	/**
	 * @return InfoStudentCurricularPlan
	 */
	public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
		return infoStudentCurricularPlan;
	}
	/**
	 * @return EnrolmentState
	 */
	public EnrolmentState getEnrolmentState() {
		return enrolmentState;
	}
	public void setInfoCurricularCourse(
			InfoCurricularCourse infoCurricularCourse) {
		this.infoCurricularCourse = infoCurricularCourse;
	}
	/**
	 * Sets the infoExecutionPeriod.
	 * 
	 * @param infoExecutionPeriod
	 *            The infoExecutionPeriod to set
	 */
	public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
		this.infoExecutionPeriod = infoExecutionPeriod;
	}
	/**
	 * Sets the infoStudentCurricularPlan.
	 * 
	 * @param infoStudentCurricularPlan
	 *            The infoStudentCurricularPlan to set
	 */
	public void setInfoStudentCurricularPlan(
			InfoStudentCurricularPlan infoStudentCurricularPlan) {
		this.infoStudentCurricularPlan = infoStudentCurricularPlan;
	}
	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            The state to set
	 */
	public void setEnrolmentState(EnrolmentState state) {
		this.enrolmentState = state;
	}
	public EnrolmentEvaluationType getEnrolmentEvaluationType() {
		return this.enrolmentEvaluationType;
	}
	public void setEnrolmentEvaluationType(EnrolmentEvaluationType type) {
		this.enrolmentEvaluationType = type;
	}
	public List getInfoEvaluations() {
		return infoEvaluations;
	}
	public void setInfoEvaluations(List list) {
		infoEvaluations = list;
	}
	/**
	 * @return InfoEnrolmentEvaluation
	 */
	public InfoEnrolmentEvaluation getInfoEnrolmentEvaluation() {
		return infoEnrolmentEvaluation;
	}
	/**
	 * @param evaluation
	 */
	public void setInfoEnrolmentEvaluation(InfoEnrolmentEvaluation evaluation) {
		infoEnrolmentEvaluation = evaluation;
	}
	/**
	 * @return Returns the creationDate.
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate
	 *            The creationDate to set.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public void copyFromDomain(IEnrolment enrolment) {
		super.copyFromDomain(enrolment);
		if (enrolment != null) {
			setCreationDate(enrolment.getCreationDate());
			setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
			setEnrolmentState(enrolment.getEnrolmentState());
		}
	}
	public static InfoEnrolment newInfoFromDomain(IEnrolment enrolment) {
		InfoEnrolment infoEnrolment = null;
		if (enrolment != null) {
			if (enrolment instanceof IEnrolmentInOptionalCurricularCourse) {
				infoEnrolment = new InfoEnrolmentInOptionalCurricularCourse();
				infoEnrolment = InfoEnrolment.newInfoFromDomain(enrolment);
			} else {
				infoEnrolment = new InfoEnrolment();
				infoEnrolment.copyFromDomain(enrolment);
			}
		}
		return infoEnrolment;
	}
}