/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.otherTypeCreditLine;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author jpvl
 */
public class EditOtherTypeCreditLineService extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(InfoObject infoObject, DomainObject domainObject) throws ExcepcaoPersistencia {
        InfoOtherTypeCreditLine infoOtherTypeCreditLine = (InfoOtherTypeCreditLine) infoObject;
        OtherTypeCreditLine otherTypeCreditLine = (OtherTypeCreditLine) domainObject;

        otherTypeCreditLine.setCredits(infoOtherTypeCreditLine.getCredits());

        ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, infoOtherTypeCreditLine.getInfoExecutionPeriod().getIdInternal());
        otherTypeCreditLine.setExecutionPeriod(executionPeriod);

        Teacher teacher = (Teacher) persistentObject.readByOID(Teacher.class, infoOtherTypeCreditLine
                .getInfoTeacher().getIdInternal());
        otherTypeCreditLine.setTeacher(teacher);

        otherTypeCreditLine.setReason(infoOtherTypeCreditLine.getReason());

    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return DomainFactory.makeOtherTypeCreditLine();
    }

    @Override
    protected Class getDomainObjectClass() {
        return OtherTypeCreditLine.class;
    }

    @Override
    protected IPersistentObject getIPersistentObject() {
        return persistentSupport.getIPersistentOtherTypeCreditLine();
    }

}