package DataBeans;

import java.util.List;

import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 * 
 * 22/Abr/2003
 */
public class InfoEnrolment extends InfoObject
{
    private InfoStudentCurricularPlan infoStudentCurricularPlan;
    //	private InfoCurricularCourse infoCurricularCourse;
    private InfoExecutionPeriod infoExecutionPeriod;
    private EnrolmentState enrolmentState;
    private EnrolmentEvaluationType evaluationType;
    private InfoCurricularCourseScope infoCurricularCourseScope;

    // to be used to keep the actual enrolment evaluation
    private InfoEnrolmentEvaluation infoEnrolmentEvaluation;

    private List infoEvaluations;

    public InfoEnrolment()
    {
    }

    public InfoEnrolment(
        InfoStudentCurricularPlan infoStudentCurricularPlan,
        InfoCurricularCourseScope infoCurricularCourseScope,
        EnrolmentState state,
        InfoExecutionPeriod infoExecutionPeriod)
    {
        this();
        setInfoCurricularCourseScope(infoCurricularCourseScope);
        setInfoStudentCurricularPlan(infoStudentCurricularPlan);
        setEnrolmentState(state);
        setInfoExecutionPeriod(infoExecutionPeriod);
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;

        if (obj instanceof InfoEnrolment)
        {
            InfoEnrolment enrolment = (InfoEnrolment) obj;

            resultado =
                this.getInfoStudentCurricularPlan().equals(enrolment.getInfoStudentCurricularPlan()) &&
                //						this.getInfoCurricularCourse().equals(enrolment.getInfoCurricularCourse()) &&
    this.getInfoCurricularCourseScope().equals(enrolment.getInfoCurricularCourseScope())
        && this.getInfoExecutionPeriod().equals(enrolment.getInfoExecutionPeriod());
        }
        return resultado;
    }

    public String toString()
    {
        String result = "[" + this.getClass().getName() + "; ";
        result += "infoStudentCurricularPlan = " + this.infoStudentCurricularPlan + "; ";
        result += "infoExecutionPeriod = " + this.infoExecutionPeriod + "; ";
        result += "state = " + this.enrolmentState + "; ";
        //		result += "infoCurricularCourse = " + this.infoCurricularCourse + "; ";
        result += "infoCurricularCourseScope = " + this.infoCurricularCourseScope + "; ";
        result += "enrolmentEvaluationType = " + this.evaluationType + "; ";
        result += "infoEvaluations = " + this.infoEvaluations + "]\n";

        return result;
    }

    //	public InfoCurricularCourse getInfoCurricularCourse() {
    //		return infoCurricularCourse;
    //	}

    /**
	 * @return InfoExecutionPeriod
	 */
    public InfoExecutionPeriod getInfoExecutionPeriod()
    {
        return infoExecutionPeriod;
    }

    /**
	 * @return InfoStudentCurricularPlan
	 */
    public InfoStudentCurricularPlan getInfoStudentCurricularPlan()
    {
        return infoStudentCurricularPlan;
    }

    /**
	 * @return EnrolmentState
	 */
    public EnrolmentState getEnrolmentState()
    {
        return enrolmentState;
    }

    //	public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
    //		this.infoCurricularCourse = infoCurricularCourse;
    //	}

    /**
	 * Sets the infoExecutionPeriod.
	 * 
	 * @param infoExecutionPeriod
	 *            The infoExecutionPeriod to set
	 */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
    {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
	 * Sets the infoStudentCurricularPlan.
	 * 
	 * @param infoStudentCurricularPlan
	 *            The infoStudentCurricularPlan to set
	 */
    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan)
    {
        this.infoStudentCurricularPlan = infoStudentCurricularPlan;
    }

    /**
	 * Sets the state.
	 * 
	 * @param state
	 *            The state to set
	 */
    public void setEnrolmentState(EnrolmentState state)
    {
        this.enrolmentState = state;
    }

    public EnrolmentEvaluationType getEvaluationType()
    {
        return this.evaluationType;
    }

    public void setEvaluationType(EnrolmentEvaluationType type)
    {
        this.evaluationType = type;
    }

    public List getInfoEvaluations()
    {
        return infoEvaluations;
    }

    public void setInfoEvaluations(List list)
    {
        infoEvaluations = list;
    }

    public InfoCurricularCourseScope getInfoCurricularCourseScope()
    {
        return infoCurricularCourseScope;
    }

    public void setInfoCurricularCourseScope(InfoCurricularCourseScope scope)
    {
        infoCurricularCourseScope = scope;
    }

    /**
	 * @return InfoEnrolmentEvaluation
	 */
    public InfoEnrolmentEvaluation getInfoEnrolmentEvaluation()
    {
        return infoEnrolmentEvaluation;
    }

    /**
	 * @param evaluation
	 */
    public void setInfoEnrolmentEvaluation(InfoEnrolmentEvaluation evaluation)
    {
        infoEnrolmentEvaluation = evaluation;
    }

}