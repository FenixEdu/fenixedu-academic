/*
 * Created on 23/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidGrantPaymentEntityException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidPartResponsibleTeacherException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantProject;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantPart extends EditDomainObjectService {

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity
                .newDomainFromInfo((InfoGrantPart) infoObject);
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantPart();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantPart pgs = sp.getIPersistentGrantPart();
        IGrantPart grantPart = (IGrantPart) domainObject;
        return pgs.readGrantPartByUnique(grantPart.getGrantSubsidy(), grantPart.getGrantPaymentEntity());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doAfterLock(IDomainObject domainObjectToLock, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {
        IGrantPart grantPart = (IGrantPart) domainObjectToLock;
        InfoGrantPart infoGrantPart = (InfoGrantPart) infoObject;
        IGrantPaymentEntity paymentEntity = null;

        //set the payment entity
        try {
            IPersistentGrantPaymentEntity pgp = sp.getIPersistentGrantPaymentEntity();

            paymentEntity = (IGrantPaymentEntity) pgp.readByOID(GrantPaymentEntity.class, infoGrantPart
                    .getInfoGrantPaymentEntity().getIdInternal());
            if (paymentEntity == null) {
                throw new InvalidGrantPaymentEntityException();
            }
            if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantCostCenter)
                grantPart.setGrantPaymentEntity(new GrantCostCenter());
            else if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantProject)
                grantPart.setGrantPaymentEntity(new GrantProject());

            BeanUtils.copyProperties(grantPart.getGrantPaymentEntity(), paymentEntity);

            domainObjectToLock = grantPart;
        } catch (Exception e) {
            throw new FenixServiceException(e.getMessage());
        }

        //set the teacher
        try {
            ITeacher teacher = new Teacher();

            IPersistentTeacher pt = sp.getIPersistentTeacher();

            teacher = pt.readByNumber(infoGrantPart.getInfoResponsibleTeacher().getTeacherNumber());
            if (teacher == null) {
                throw new InvalidPartResponsibleTeacherException();
            }
            grantPart.setResponsibleTeacher(new Teacher());
            BeanUtils.copyProperties(grantPart.getResponsibleTeacher(), teacher);

            domainObjectToLock = grantPart;

        } catch (InvalidPartResponsibleTeacherException e) {
            throw new InvalidPartResponsibleTeacherException();
        } catch (Exception e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    public void run(InfoGrantPart infoGrantPart) throws FenixServiceException {
        super.run(new Integer(0), infoGrantPart);
    }
}