/*
 * Created on Jun 25, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoTeacherWithPerson;
import Dominio.grant.contract.IGrantProject;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantProjectWithTeacherAndCostCenter extends InfoGrantProject {

    public void copyFromDomain(IGrantProject grantProject) {
        if (grantProject != null) {
            super.copyFromDomain(grantProject);
            if (grantProject.getResponsibleTeacher() != null) {
                setInfoResponsibleTeacher(InfoTeacherWithPerson
                        .newInfoFromDomain(grantProject.getResponsibleTeacher()));
            }
            if (grantProject.getGrantCostCenter() != null) {
                setInfoGrantCostCenter(InfoGrantCostCenterWithTeacher
                        .newInfoFromDomain(grantProject.getGrantCostCenter()));
            }
        }
    }

    public static InfoGrantProject newInfoFromDomain(IGrantProject grantProject) {
        InfoGrantProjectWithTeacherAndCostCenter infoGrantProject = null;
        if (grantProject != null) {
            infoGrantProject = new InfoGrantProjectWithTeacherAndCostCenter();
            infoGrantProject.copyFromDomain(grantProject);
        }
        return infoGrantProject;
    }

}
