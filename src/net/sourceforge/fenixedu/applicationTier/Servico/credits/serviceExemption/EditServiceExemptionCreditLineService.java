/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.serviceExemption;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoServiceExemptionCreditLine;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.IServiceExemptionCreditLine;
import net.sourceforge.fenixedu.domain.credits.ServiceExemptionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentServiceExemptionCreditLine;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class EditServiceExemptionCreditLineService implements IService {

    public void run(Integer objectId, InfoObject infoObject) throws ExcepcaoPersistencia {
        final InfoServiceExemptionCreditLine infoServiceExemptionCreditLine = (InfoServiceExemptionCreditLine) infoObject;
        final InfoTeacher infoTeacher = infoServiceExemptionCreditLine.getInfoTeacher();

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentServiceExemptionCreditLine persistentServiceExemptionCreditLine = persistentSupport
                .getIPersistentServiceExemptionCreditLine();
        final IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();

        final IServiceExemptionCreditLine serviceExemptionCreditLine;
        if (infoServiceExemptionCreditLine.getIdInternal() == null || infoServiceExemptionCreditLine.getIdInternal().intValue() == 0) {
            serviceExemptionCreditLine = new ServiceExemptionCreditLine();
        } else {
            serviceExemptionCreditLine = (IServiceExemptionCreditLine) persistentServiceExemptionCreditLine
                    .readByOID(ServiceExemptionCreditLine.class, objectId);
        }
        persistentServiceExemptionCreditLine.simpleLockWrite(serviceExemptionCreditLine);


        if (infoTeacher != null) {
            final ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, infoTeacher.getIdInternal());
            serviceExemptionCreditLine.setTeacher(teacher);
        }

        infoServiceExemptionCreditLine.populateDomainObject(serviceExemptionCreditLine);
    }

}