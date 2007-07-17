/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

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
    public void copyFromDomain(GrantProject grantProject) {
        if (grantProject != null) {

            super.copyFromDomain(grantProject);
            setNumber(grantProject.getNumber());
            setDesignation(grantProject.getDesignation());
        }
    }

    /**
     * @param GrantProject
     * @return
     */
    public static InfoGrantProject newInfoFromDomain(GrantProject grantProject) {
        InfoGrantProject infoGrantProject = null;
        if (grantProject != null) {
            infoGrantProject = new InfoGrantProject();
            infoGrantProject.copyFromDomain(grantProject);
        }
        return infoGrantProject;
    }

    public void copyToDomain(InfoGrantProject infoGrantProject, GrantProject grantProject)
            throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantProject, grantProject);

        grantProject.setDesignation(infoGrantProject.getDesignation());
        grantProject.setNumber(infoGrantProject.getNumber());
    }

}
