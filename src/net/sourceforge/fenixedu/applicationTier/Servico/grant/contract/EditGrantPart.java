/*
 * Created on 23/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidGrantPaymentEntityException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidPartResponsibleTeacherException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantPart implements IService {

    public void run(InfoGrantPart infoGrantPart) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentGrantPart persistentGrantPart = persistentSupport.getIPersistentGrantPart();
        final IPersistentGrantPaymentEntity persistentGrantPaymentEntity = persistentSupport
                .getIPersistentGrantPaymentEntity();
        final IPersistentGrantSubsidy persistentGrantSubsidy = persistentSupport
                .getIPersistentGrantSubsidy();
        final IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();

        IGrantPart grantPart = (IGrantPart) persistentGrantPart.readByOID(GrantPart.class, infoGrantPart
                .getIdInternal());
        if (grantPart == null)
            grantPart = new GrantPart();
        persistentGrantPart.simpleLockWrite(grantPart);

        final IGrantPaymentEntity grantPaymentEntity = (IGrantPaymentEntity) persistentGrantPaymentEntity
                .readByOID(GrantPaymentEntity.class, infoGrantPart.getInfoGrantPaymentEntity()
                        .getIdInternal());
        if (grantPaymentEntity == null) {
            throw new InvalidGrantPaymentEntityException();
        }
        grantPart.setGrantPaymentEntity(grantPaymentEntity);

        final IGrantSubsidy grantSubsidy = (IGrantSubsidy) persistentGrantSubsidy.readByOID(
                GrantSubsidy.class, infoGrantPart.getInfoGrantSubsidy().getIdInternal());
        grantPart.setGrantSubsidy(grantSubsidy);

        grantPart.setPercentage(infoGrantPart.getPercentage());

        if (infoGrantPart.getInfoResponsibleTeacher() != null) {
            final ITeacher teacher = persistentTeacher.readByNumber(infoGrantPart
                    .getInfoResponsibleTeacher().getTeacherNumber());
            if (teacher == null) {
                throw new InvalidPartResponsibleTeacherException();
            }
            grantPart.setResponsibleTeacher(teacher);

        } else {
            grantPart.setResponsibleTeacher(null);
        }
    }

}
