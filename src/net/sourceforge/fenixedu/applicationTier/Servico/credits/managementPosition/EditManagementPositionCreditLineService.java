/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class EditManagementPositionCreditLineService extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(InfoObject infoObject, DomainObject domainObject)
            throws ExcepcaoPersistencia {
        InfoManagementPositionCreditLine infoManagementPositionCreditLine = (InfoManagementPositionCreditLine) infoObject;
        ManagementPositionCreditLine managementPositionCreditLine = (ManagementPositionCreditLine) domainObject;
        managementPositionCreditLine.setCredits(infoManagementPositionCreditLine.getCredits());
        managementPositionCreditLine.setEnd(infoManagementPositionCreditLine.getEnd());
        Teacher teacher = (Teacher) persistentObject.readByOID(Teacher.class,
                infoManagementPositionCreditLine.getInfoTeacher().getIdInternal());

        managementPositionCreditLine.setPosition(infoManagementPositionCreditLine.getPosition());
        managementPositionCreditLine.setStart(infoManagementPositionCreditLine.getStart());
        managementPositionCreditLine.setTeacher(teacher);
    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return DomainFactory.makeManagementPositionCreditLine();
    }

    @Override
    protected Class getDomainObjectClass() {
        return ManagementPositionCreditLine.class;
    }
}
