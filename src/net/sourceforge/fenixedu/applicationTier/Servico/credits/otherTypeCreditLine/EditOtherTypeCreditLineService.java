/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.otherTypeCreditLine;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.IOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditOtherTypeCreditLineService extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject,
            IDomainObject domainObject) throws ExcepcaoPersistencia {
        InfoOtherTypeCreditLine infoOtherTypeCreditLine = (InfoOtherTypeCreditLine) infoObject;
        IOtherTypeCreditLine otherTypeCreditLine = (OtherTypeCreditLine) domainObject;

        otherTypeCreditLine.setCredits(infoOtherTypeCreditLine.getCredits());

        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        IExecutionPeriod executionPeriod = (ExecutionPeriod) persistentExecutionPeriod.readByOID(
                ExecutionPeriod.class, infoOtherTypeCreditLine.getInfoExecutionPeriod().getIdInternal());
        otherTypeCreditLine.setExecutionPeriod(executionPeriod);
        otherTypeCreditLine.setKeyExecutionPeriod(executionPeriod.getIdInternal());

        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, infoOtherTypeCreditLine
                .getInfoTeacher().getIdInternal());
        otherTypeCreditLine.setKeyTeacher(teacher.getIdInternal());
        otherTypeCreditLine.setTeacher(teacher);

        otherTypeCreditLine.setReason(infoOtherTypeCreditLine.getReason());

    }

    @Override
    protected IDomainObject createNewDomainObject(InfoObject infoObject) {
        return new OtherTypeCreditLine();
    }

    @Override
    protected Class getDomainObjectClass() {
        return OtherTypeCreditLine.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentOtherTypeCreditLine();
    }

}