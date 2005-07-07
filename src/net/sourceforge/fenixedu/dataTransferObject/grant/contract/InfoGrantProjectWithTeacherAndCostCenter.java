/*
 * Created on Jun 25, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantProject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantProjectWithTeacherAndCostCenter extends InfoGrantProject {

    public void copyFromDomain(IGrantProject grantProject) {
        if (grantProject != null) {
            super.copyFromDomain(grantProject);
            if (grantProject.getResponsibleTeacher() != null) {
                setInfoResponsibleTeacher(InfoTeacherWithPerson.newInfoFromDomain(grantProject
                        .getResponsibleTeacher()));
            }
            if (grantProject.getOjbConcreteClass().equals(GrantProject.class.getName())){
                if (grantProject.getGrantCostCenter() != null) {
                    setInfoGrantCostCenter(InfoGrantCostCenterWithTeacher.newInfoFromDomain(grantProject
                            .getGrantCostCenter()));
                }
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

    public void copyToDomain(InfoGrantProject infoGrantProject, IGrantProject grantProject) throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantProject, grantProject);

        grantProject.setResponsibleTeacher(InfoTeacherWithPerson.newDomainFromInfo(infoGrantProject
                .getInfoResponsibleTeacher()));
        grantProject.setGrantCostCenter(InfoGrantCostCenterWithTeacher
                .newDomainFromInfo(infoGrantProject.getInfoGrantCostCenter()));
    }

    public static IGrantProject newDomainFromInfo(InfoGrantProject infoGrantProject) throws ExcepcaoPersistencia {
        IGrantProject grantProject = null;
        InfoGrantProjectWithTeacherAndCostCenter infoGrantProjectWithTeacherAndCostCenter = null;
        if (infoGrantProject != null) {
            grantProject = new GrantProject();
            infoGrantProjectWithTeacherAndCostCenter = new InfoGrantProjectWithTeacherAndCostCenter();
            infoGrantProjectWithTeacherAndCostCenter.copyToDomain(infoGrantProject, grantProject);
        }
        return grantProject;
    }
}