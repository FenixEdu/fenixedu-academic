package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Service;
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

public class EditGrantPart extends Service {

    public void run(InfoGrantPart infoGrantPart) throws FenixServiceException, ExcepcaoPersistencia {

        GrantPart grantPart = (GrantPart) persistentObject.readByOID(
                GrantPart.class, infoGrantPart.getIdInternal());
        if (grantPart == null) {
            grantPart = DomainFactory.makeGrantPart();
        }

        final GrantPaymentEntity grantPaymentEntity = (GrantPaymentEntity) persistentObject
                .readByOID(GrantPaymentEntity.class, infoGrantPart.getInfoGrantPaymentEntity()
                        .getIdInternal());
        if (grantPaymentEntity == null) {
            throw new InvalidGrantPaymentEntityException();
        }
        grantPart.setGrantPaymentEntity(grantPaymentEntity);

        final GrantSubsidy grantSubsidy = (GrantSubsidy) persistentObject.readByOID(
                GrantSubsidy.class, infoGrantPart.getInfoGrantSubsidy().getIdInternal());
        grantPart.setGrantSubsidy(grantSubsidy);

        grantPart.setPercentage(infoGrantPart.getPercentage());

        if (infoGrantPart.getInfoResponsibleTeacher() != null) {
            final Teacher teacher = Teacher.readByNumber(infoGrantPart
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
