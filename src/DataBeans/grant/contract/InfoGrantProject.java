/*
 * Created on Jan 21, 2004
 */
package DataBeans.grant.contract;

import Dominio.grant.contract.GrantProject;
import Dominio.grant.contract.IGrantProject;

/**
 * @author pica
 * @author barbosa
 */
public class InfoGrantProject extends InfoGrantPaymentEntity {

    private InfoGrantCostCenter infoGrantCostCenter;

    /**
     * @return Returns the infoGrantCostCenter.
     */
    public InfoGrantCostCenter getInfoGrantCostCenter() {
        return infoGrantCostCenter;
    }

    /**
     * @param infoGrantCostCenter
     *            The infoGrantCostCenter to set.
     */
    public void setInfoGrantCostCenter(InfoGrantCostCenter infoGrantCostCenter) {
        this.infoGrantCostCenter = infoGrantCostCenter;
    }

    /**
     * @param GrantProject
     */
    public void copyFromDomain(IGrantProject grantProject)
    {
    	if(grantProject != null){
    		
    		setIdInternal(grantProject.getIdInternal()); //FIXME
    		setNumber(grantProject.getNumber());
    		setDesignation(grantProject.getDesignation());
    		setOjbConcreteClass(grantProject.getOjbConcreteClass());
    	}
    }

    /**
     * @param GrantProject
     * @return
     */
    public static InfoGrantProject newInfoFromDomain(IGrantProject grantProject)
    {
    	InfoGrantProject infoGrantProject = null;
    	if (grantProject != null)
    	{
    		infoGrantProject = new InfoGrantProject();
    		infoGrantProject.copyFromDomain(grantProject);
    	}
    	return infoGrantProject;
    }
    
    public void copyToDomain(InfoGrantProject infoGrantProject,IGrantProject grantProject)
    {
        super.copyToDomain(infoGrantProject,grantProject);
        
        grantProject.setDesignation(infoGrantProject.getDesignation());
        grantProject.setNumber(infoGrantProject.getNumber());
        grantProject.setOjbConcreteClass(infoGrantProject.getOjbConcreteClass());
    }
    
    public static IGrantProject newDomainFromInfo(InfoGrantProject infoGrantProject)
    {
        IGrantProject grantProject = null;
        if(infoGrantProject != null)
        {
            grantProject = new GrantProject();
            infoGrantProject.copyToDomain(infoGrantProject,grantProject);
        }
        return grantProject;
    }
    
}
