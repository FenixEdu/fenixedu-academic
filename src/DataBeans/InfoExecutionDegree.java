/*
 * InfoExecutionDegree.java
 * 
 * Created on 24 de Novembro de 2002, 23:05
 */

package DataBeans;

import java.io.Serializable;
import java.util.List;

/**
 * @author tfc130
 */
public class InfoExecutionDegree extends InfoObject implements Serializable
{
    private InfoExecutionYear infoExecutionYear;
    private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    //added by Tânia Pousão
    private List coordinatorsList;

    private Boolean temporaryExamMap;

    //added by Tânia Pousão
    private InfoCampus infoCampus;

    public InfoExecutionDegree()
    {
    }

    /**
	 * @param infoDegreeCurricularPlan
	 * @param infoExecutionYear
	 */
    public InfoExecutionDegree(
        InfoDegreeCurricularPlan infoDegreeCurricularPlan,
        InfoExecutionYear infoExecutionYear)
    {
        setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        setInfoExecutionYear(infoExecutionYear);
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof InfoExecutionDegree)
        {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) obj;
            result =
                getInfoExecutionYear().equals(infoExecutionDegree.getInfoExecutionYear())
                    && getInfoDegreeCurricularPlan().equals(
                        infoExecutionDegree.getInfoDegreeCurricularPlan());
        }
        return result;
    }

    public String toString()
    {
        String result = "[INFOEXECUTIONDEGREE";
        result += ", infoExecutionYear=" + infoExecutionYear;
        result += ", infoDegreeCurricularPlan=" + infoDegreeCurricularPlan;
        if(coordinatorsList != null) {
        	result += ", coordinatorsList=" + coordinatorsList.size();
        } else {
        	result += ", coordinatorsList is NULL";
        }
        result += ", infoCampus= " + infoCampus;
        result += "]";
        return result;
    }

    /**
	 * Returns the infoExecutionYear.
	 * 
	 * @return InfoExecutionYear
	 */
    public InfoExecutionYear getInfoExecutionYear()
    {
        return infoExecutionYear;
    }

    /**
	 * Sets the infoExecutionYear.
	 * 
	 * @param infoExecutionYear
	 *            The infoExecutionYear to set
	 */
    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear)
    {
        this.infoExecutionYear = infoExecutionYear;
    }

    /**
	 * Returns the infoDegreeCurricularPlan.
	 * 
	 * @return InfoDegreeCurricularPlan
	 */
    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan()
    {
        return infoDegreeCurricularPlan;
    }

    /**
	 * Sets the infoDegreeCurricularPlan.
	 * 
	 * @param infoDegreeCurricularPlan
	 *            The infoDegreeCurricularPlan to set
	 */
    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan)
    {
        this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
    }

    /**
	 * @return
	 */
    public Boolean getTemporaryExamMap()
    {
        return temporaryExamMap;
    }

    /**
	 * @param boolean1
	 */
    public void setTemporaryExamMap(Boolean temporary)
    {
        temporaryExamMap = temporary;
    }

    /**
	 * @return Returns the infoCampus.
	 */
    public InfoCampus getInfoCampus()
    {
        return infoCampus;
    }

    /**
	 * @param infoCampus
	 *            The infoCampus to set.
	 */
    public void setInfoCampus(InfoCampus infoCampus)
    {
        this.infoCampus = infoCampus;
    }

    /**
     * @return Returns the coordinatorList.
     */
    public List getCoordinatorsList()
    {
        return coordinatorsList;
    }

    /**
     * @param coordinatorList The coordinatorList to set.
     */
    public void setCoordinatorsList(List coordinatorsList)
    {
        this.coordinatorsList = coordinatorsList;
    }

}
