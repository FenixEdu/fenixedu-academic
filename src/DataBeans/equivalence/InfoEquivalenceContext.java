package DataBeans.equivalence;

import java.util.ArrayList;
import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;

public class InfoEquivalenceContext extends DataTranferObject {
	private List infoEnrolmentsToGiveEquivalence;
    private List infoCurricularCoursesToGetEquivalence;   
   
	private List infoCurricularCourseScopesToGetEquivalence;
   
    
	private List chosenInfoEnrolmentsToGiveEquivalence;
	private List chosenInfoCurricularCourseScopesToGetEquivalence;
    private List chosenInfoCurricularCoursesToGetEquivalence;
    
	private List chosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade;
    private List chosenInfoCurricularCoursesToGiveEquivalenceWithGrade;
    
	private List chosenInfoCurricularCourseScopesToGetEquivalenceWithGrade;
    private List chosenInfoCurricularCoursesToGetEquivalenceWithGrade;
	private List errorMessages;
	private InfoExecutionPeriod currentInfoExecutionPeriod;
	private InfoStudentCurricularPlan infoStudentCurricularPlan;
	private IUserView responsible;
	private boolean success;
	
	public InfoEquivalenceContext() {
	}

	/**
     * @deprecated
	 * @return
	 */
	public List getInfoCurricularCourseScopesToGetEquivalence() {
		return infoCurricularCourseScopesToGetEquivalence;
	}

	/**
     * @deprecated
	 * @param list
	 */
	public void setInfoCurricularCourseScopesToGetEquivalence(List list) {
		infoCurricularCourseScopesToGetEquivalence = list;
	}

	/**
	 * @return
	 */
	public InfoExecutionPeriod getCurrentInfoExecutionPeriod() {
		return currentInfoExecutionPeriod;
	}

	/**
	 * @return
	 */
	public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
		return infoStudentCurricularPlan;
	}

	/**
	 * @param period
	 */
	public void setCurrentInfoExecutionPeriod(InfoExecutionPeriod period) {
		currentInfoExecutionPeriod = period;
	}

	/**
	 * @param plan
	 */
	public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan plan) {
		infoStudentCurricularPlan = plan;
	}

	/**
     * @deprecated
	 * @return
	 */
	public List getChosenInfoCurricularCourseScopesToGetEquivalence() {
		return chosenInfoCurricularCourseScopesToGetEquivalence;
	}

	/**
     * @deprecated
	 * @param list
	 */
	public void setChosenInfoCurricularCourseScopesToGetEquivalence(List list) {
		chosenInfoCurricularCourseScopesToGetEquivalence = list;
	}

	/**
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param b
	 */
	public void setSuccess(boolean b) {
		success = b;
	}

	/**
	 * @return
	 */
	public List getErrorMessages() {
		return errorMessages;
	}

	/**
	 * @param list
	 */
	public void setErrorMessages(List list) {
		errorMessages = list;
	}

	/**
	 * @param string
	 */
	public void setErrorMessage(String string) {
		if(errorMessages == null) {
			errorMessages = new ArrayList();
		}
		errorMessages.add(string);
	}

	/**
	 * @return
	 */
	public IUserView getResponsible() {
		return responsible;
	}

	/**
	 * @param view
	 */
	public void setResponsible(IUserView view) {
		responsible = view;
	}

	/**
	 * @return
	 */
	public List getInfoEnrolmentsToGiveEquivalence() {
		return infoEnrolmentsToGiveEquivalence;
	}

	/**
	 * @param list
	 */
	public void setInfoEnrolmentsToGiveEquivalence(List list) {
		infoEnrolmentsToGiveEquivalence = list;
	}

	/**
	 * @return
	 */
	public List getChosenInfoEnrolmentsToGiveEquivalence() {
		return chosenInfoEnrolmentsToGiveEquivalence;
	}

	/**
	 * @param list
	 */
	public void setChosenInfoEnrolmentsToGiveEquivalence(List list) {
		chosenInfoEnrolmentsToGiveEquivalence = list;
	}

	/**
     * @deprecated
	 * @return
	 */
	public List getChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade() {
		return chosenInfoCurricularCourseScopesToGetEquivalenceWithGrade;
	}

	/**
     * @deprecated
	 * @param list
	 */
	public void setChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade(List list) {
		chosenInfoCurricularCourseScopesToGetEquivalenceWithGrade = list;
	}

	/**
     * @deprecated
	 * @return
	 */
	public List getChosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade() {
		return chosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade;
	}

	/**
     * @deprecated
	 * @param list
	 */
	public void setChosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade(List list) {
		chosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade = list;
	}

    /**
     * @return Returns the chosenInfoCurricularCoursesToGetEquivalence.
     */
    public List getChosenInfoCurricularCoursesToGetEquivalence()
    {
        return chosenInfoCurricularCoursesToGetEquivalence;
    }

    /**
     * @param chosenInfoCurricularCoursesToGetEquivalence The chosenInfoCurricularCoursesToGetEquivalence to set.
     */
    public void setChosenInfoCurricularCoursesToGetEquivalence(List chosenInfoCurricularCoursesToGetEquivalence)
    {
        this.chosenInfoCurricularCoursesToGetEquivalence = chosenInfoCurricularCoursesToGetEquivalence;
    }

    /**
     * @return Returns the chosenInfoCurricularCoursesToGetEquivalenceWithGrade.
     */
    public List getChosenInfoCurricularCoursesToGetEquivalenceWithGrade()
    {
        return chosenInfoCurricularCoursesToGetEquivalenceWithGrade;
    }

    /**
     * @param chosenInfoCurricularCoursesToGetEquivalenceWithGrade The chosenInfoCurricularCoursesToGetEquivalenceWithGrade to set.
     */
    public void setChosenInfoCurricularCoursesToGetEquivalenceWithGrade(List chosenInfoCurricularCoursesToGetEquivalenceWithGrade)
    {
        this.chosenInfoCurricularCoursesToGetEquivalenceWithGrade =
            chosenInfoCurricularCoursesToGetEquivalenceWithGrade;
    }

    /**
     * @return Returns the chosenInfoCurricularCoursesToGiveEquivalenceWithGrade.
     */
    public List getChosenInfoCurricularCoursesToGiveEquivalenceWithGrade()
    {
        return chosenInfoCurricularCoursesToGiveEquivalenceWithGrade;
    }

    /**
     * @param chosenInfoCurricularCoursesToGiveEquivalenceWithGrade The chosenInfoCurricularCoursesToGiveEquivalenceWithGrade to set.
     */
    public void setChosenInfoCurricularCoursesToGiveEquivalenceWithGrade(List chosenInfoCurricularCoursesToGiveEquivalenceWithGrade)
    {
        this.chosenInfoCurricularCoursesToGiveEquivalenceWithGrade =
            chosenInfoCurricularCoursesToGiveEquivalenceWithGrade;
    }

    /**
     * @return Returns the infoCurricularCoursesToGetEquivalence.
     */
    public List getInfoCurricularCoursesToGetEquivalence()
    {
        return infoCurricularCoursesToGetEquivalence;
    }

    /**
     * @param infoCurricularCoursesToGetEquivalence The infoCurricularCoursesToGetEquivalence to set.
     */
    public void setInfoCurricularCoursesToGetEquivalence(List infoCurricularCoursesToGetEquivalence)
    {
        this.infoCurricularCoursesToGetEquivalence = infoCurricularCoursesToGetEquivalence;
    }

}