/*
 * Created on 18/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantTypeNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidGrantPaymentEntityException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 */
public class CreateOrEditGrantContract implements IService {

    public void run(InfoGrantContract infoGrantContract) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher pTeacher = sp.getIPersistentTeacher();
        IPersistentGrantType pGrantType = sp.getIPersistentGrantType();
        IPersistentGrantContract pGrantContract = sp.getIPersistentGrantContract();
        IPersistentGrantOwner pGrantOwner = sp.getIPersistentGrantOwner();
        IPersistentGrantOrientationTeacher pGrantOrientationTeacher = sp
                .getIPersistentGrantOrientationTeacher();
        IPersistentGrantCostCenter pGrantCostCenter = sp.getIPersistentGrantCostCenter();

        final IGrantContract grantContract;
        if (infoGrantContract.getContractNumber() == null) {

            // set the contract number!
            Integer maxNumber = pGrantContract.readMaxGrantContractNumberByGrantOwner(infoGrantContract
                    .getGrantOwnerInfo().getIdInternal());
            Integer newContractNumber = new Integer(maxNumber.intValue() + 1);
            infoGrantContract.setContractNumber(newContractNumber);

            grantContract = new GrantContract();
        } else {
            grantContract = pGrantContract.readGrantContractByNumberAndGrantOwner(infoGrantContract
                    .getContractNumber(), infoGrantContract.getGrantOwnerInfo().getIdInternal());
        }
        pGrantContract.simpleLockWrite(grantContract);

        IGrantOwner grantOwner = (IGrantOwner) pGrantOwner.readByOID(GrantOwner.class, infoGrantContract
                .getGrantOwnerInfo().getIdInternal());

        grantContract.setGrantOwner(grantOwner);

        IGrantType grantType = pGrantType.readGrantTypeBySigla(infoGrantContract.getGrantTypeInfo()
                .getSigla());
        if (grantType == null)
            throw new GrantTypeNotFoundException();
        grantContract.setGrantType(grantType);

        IGrantOrientationTeacher grantOrientationTeacher = (IGrantOrientationTeacher) pGrantOrientationTeacher
                .readByOID(GrantOrientationTeacher.class, infoGrantContract
                        .getGrantOrientationTeacherInfo().getIdInternal());

        if (grantOrientationTeacher == null) {
            if (infoGrantContract.getIdInternal() != null
                    || !infoGrantContract.getIdInternal().equals(new Integer(0)))
                grantOrientationTeacher = pGrantOrientationTeacher
                        .readActualGrantOrientationTeacherByContract(infoGrantContract.getIdInternal(),
                                new Integer(0));

            if (grantOrientationTeacher == null) {
                grantOrientationTeacher = createNewGrantOrientationTeacher(sp, infoGrantContract
                        .getGrantOrientationTeacherInfo(), grantContract);
            } else {

                final ITeacher teacher = pTeacher
                        .readByNumber(infoGrantContract.getGrantOrientationTeacherInfo()
                                .getOrientationTeacherInfo().getTeacherNumber());

                pGrantOrientationTeacher.simpleLockWrite(grantOrientationTeacher);
                grantOrientationTeacher.setOrientationTeacher(teacher);

            }

        }

        grantContract.setGrantOrientationTeacher(grantOrientationTeacher);

        if (infoGrantContract.getGrantCostCenterInfo() != null
                && infoGrantContract.getGrantCostCenterInfo().getNumber() != null) {
            IGrantCostCenter grantCostCenter = pGrantCostCenter
                    .readGrantCostCenterByNumber(infoGrantContract.getGrantCostCenterInfo().getNumber());
            if (grantCostCenter == null)
                throw new InvalidGrantPaymentEntityException();
            grantContract.setGrantCostCenter(grantCostCenter);

        } else {
            grantContract.setGrantCostCenter(null);
        }

        grantContract.setContractNumber(infoGrantContract.getContractNumber());
        grantContract.setDateAcceptTerm(infoGrantContract.getDateAcceptTerm());
        grantContract.setEndContractMotive(infoGrantContract.getEndContractMotive());
    }

    private IGrantOrientationTeacher createNewGrantOrientationTeacher(ISuportePersistente sp,
            InfoGrantOrientationTeacher grantOrientationTeacherInfo, IGrantContract grantContract)
            throws FenixServiceException, ExcepcaoPersistencia {

        IPersistentTeacher pTeacher = sp.getIPersistentTeacher();
        IPersistentGrantOrientationTeacher pGrantOrientationTeacher = sp
                .getIPersistentGrantOrientationTeacher();

        final ITeacher teacher = pTeacher.readByNumber(grantOrientationTeacherInfo
                .getOrientationTeacherInfo().getTeacherNumber());

        final IGrantOrientationTeacher newGrantOrientationTeacher;
        if (teacher == null)
            throw new GrantOrientationTeacherNotFoundException();

        newGrantOrientationTeacher = new GrantOrientationTeacher();
        pGrantOrientationTeacher.simpleLockWrite(newGrantOrientationTeacher);
        newGrantOrientationTeacher.setBeginDate(grantOrientationTeacherInfo.getBeginDate());
        newGrantOrientationTeacher.setEndDate(grantOrientationTeacherInfo.getEndDate());

        newGrantOrientationTeacher.setGrantContract(grantContract);
        newGrantOrientationTeacher.setKeyContract(grantContract.getIdInternal());

        newGrantOrientationTeacher.setKeyTeacher(teacher.getIdInternal());
        newGrantOrientationTeacher.setOrientationTeacher(teacher);

        return newGrantOrientationTeacher;
    }

}
