/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package DataBeans.credits;

import java.io.Serializable;
import java.util.List;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public class TeacherCreditsSheetDTO implements Serializable
{

    private List infoCreditLineList;

    private List infoDegreeFinalProjectOrientationList;
    private InfoExecutionPeriod infoExecutionPeriod;

    private List infoMasterDegreeProfessorships;

    private List infoShiftProfessorshipList;
    private InfoTeacher infoTeacher;

    /**
	 * @return Returns the infoCreditLineList.
	 */
    public List getInfoCreditLineList()
    {
        return this.infoCreditLineList;
    }

    /**
	 * @return Returns the infoTfcOrientation.
	 */
    public List getInfoDegreeFinalProjectOrientationList()
    {
        return this.infoDegreeFinalProjectOrientationList;
    }

    /**
	 * @return Returns the infoExecutionPeriod.
	 */
    public InfoExecutionPeriod getInfoExecutionPeriod()
    {
        return this.infoExecutionPeriod;
    }

    /**
	 * @return Returns the infoMasterDegreeProfessorships.
	 */
    public List getInfoMasterDegreeProfessorships()
    {
        return this.infoMasterDegreeProfessorships;
    }

    /**
	 * @return Returns the infoShiftProfessorshipList.
	 */
    public List getInfoShiftProfessorshipList()
    {
        return this.infoShiftProfessorshipList;
    }

    /**
	 * @return Returns the infoTeacher.
	 */
    public InfoTeacher getInfoTeacher()
    {
        return this.infoTeacher;
    }

    /**
	 * @param infoCreditLineList
	 *                   The infoCreditLineList to set.
	 */
    public void setInfoCreditLineList(List infoCreditLineList)
    {
        this.infoCreditLineList = infoCreditLineList;
    }

    /**
	 * @param infoTfcOrientation
	 *                   The infoTfcOrientation to set.
	 */
    public void setInfoDegreeFinalProjectOrientationList(List infoTfcOrientation)
    {
        this.infoDegreeFinalProjectOrientationList = infoTfcOrientation;
    }

    /**
	 * @param infoExecutionPeriod
	 *                   The infoExecutionPeriod to set.
	 */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
    {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
	 * @param infoMasterDegreeProfessorships
	 *                   The infoMasterDegreeProfessorships to set.
	 */
    public void setInfoMasterDegreeProfessorships(List infoMasterDegreeProfessorships)
    {
        this.infoMasterDegreeProfessorships = infoMasterDegreeProfessorships;
    }

    /**
	 * @param infoShiftProfessorshipList
	 *                   The infoShiftProfessorshipList to set.
	 */
    public void setInfoShiftProfessorshipList(List infoShiftProfessorshipList)
    {
        this.infoShiftProfessorshipList = infoShiftProfessorshipList;
    }

    /**
	 * @param infoTeacher
	 *                   The infoTeacher to set.
	 */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }

}
