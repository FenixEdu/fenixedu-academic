/*
 * Created on 2:49:04 PM,Mar 10, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 2:49:04 PM, Mar 10, 2005
 */
public class InfoExternalDegreeCurricularPlanInfo
{
    private String name;
    private String code;
    private InfoExternalDegreeBranchInfo branch;
    /**
     * @return Returns the branch.
     */
    public InfoExternalDegreeBranchInfo getBranch()
    {
        return this.branch;
    }
    /**
     * @param branch The branch to set.
     */
    public void setBranch(InfoExternalDegreeBranchInfo branch)
    {
        this.branch = branch;
    }
    /**
     * @return Returns the code.
     */
    public String getCode()
    {
        return this.code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return this.name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    public static InfoExternalDegreeCurricularPlanInfo newFromDegreeCurricularPlan(IDegreeCurricularPlan curricularPlan)
    {
        InfoExternalDegreeCurricularPlanInfo externalDegreeInfo = new InfoExternalDegreeCurricularPlanInfo();
        externalDegreeInfo.setName(curricularPlan.getName());
        
        return externalDegreeInfo;
    }
}
