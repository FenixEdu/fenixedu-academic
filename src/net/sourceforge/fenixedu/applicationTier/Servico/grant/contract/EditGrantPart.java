package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidGrantPaymentEntityException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidPartResponsibleTeacherException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditGrantPart implements IService {

    public void run(InfoGrantPart infoGrantPart) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentGrantPaymentEntity persistentGrantPaymentEntity = persistentSupport
                .getIPersistentGrantPaymentEntity();
        final IPersistentGrantSubsidy persistentGrantSubsidy = persistentSupport
                .getIPersistentGrantSubsidy();
        final IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();

        GrantPart grantPart = (GrantPart) persistentSupport.getIPersistentObject().readByOID(
                GrantPart.class, infoGrantPart.getIdInternal());
        if (grantPart == null) {
            grantPart = DomainFactory.makeGrantPart();
        }

        final GrantPaymentEntity grantPaymentEntity = (GrantPaymentEntity) persistentGrantPaymentEntity
                .readByOID(GrantPaymentEntity.class, infoGrantPart.getInfoGrantPaymentEntity()
                        .getIdInternal());
        if (grantPaymentEntity == null) {
            throw new InvalidGrantPaymentEntityException();
        }
        grantPart.setGrantPaymentEntity(grantPaymentEntity);

        final GrantSubsidy grantSubsidy = (GrantSubsidy) persistentGrantSubsidy.readByOID(
                GrantSubsidy.class, infoGrantPart.getInfoGrantSubsidy().getIdInternal());
        grantPart.setGrantSubsidy(grantSubsidy);

        grantPart.setPercentage(infoGrantPart.getPercentage());

        if (infoGrantPart.getInfoResponsibleTeacher() != null) {
            final Teacher teacher = persistentTeacher.readByNumber(infoGrantPart
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
