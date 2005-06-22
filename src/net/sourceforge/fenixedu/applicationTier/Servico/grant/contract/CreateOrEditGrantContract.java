/*
 * Created on 18/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantTypeNotFoundException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
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

import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 */
public class CreateOrEditGrantContract implements IService {

    // protected IDomainObject clone2DomainObject(InfoObject infoObject) {
    // return InfoGrantContractWithGrantOwnerAndGrantType
    // .newDomainFromInfo((InfoGrantContract) infoObject);
    // }

    // protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    // {
    // return sp.getIPersistentGrantContract();
    // }

//    protected void doBeforeLock(IDomainObject domainObjectToLock,
//            InfoObject infoObject, ISuportePersistente sp)
//            throws FenixServiceException {
//        checkIfGrantTeacherRelationExists(domainObjectToLock, infoObject, sp);
//    }
//
//    protected void checkIfGrantTeacherRelationExists(
//            IDomainObject newDomainObject, InfoObject infoObject,
//            ISuportePersistente sp) throws FenixServiceException {
//
//        try {
//            IPersistentGrantOrientationTeacher ot = sp
//                    .getIPersistentGrantOrientationTeacher();
//            InfoGrantContract infoGrantContract = (InfoGrantContract) infoObject;
//            IGrantOrientationTeacher oldGrantOrientationTeacher = null;
//            IGrantOrientationTeacher newGrantOrientationTeacher = null;
//
//            // check if the GrantOrientation relation exists
//            Integer orientationId = infoGrantContract
//                    .getGrantOrientationTeacherInfo().getIdInternal();
//            if ((orientationId != null)
//                    && !(orientationId.equals(new Integer(0)))) {
//                // lock the existent object to write (EDIT)
//
//                newGrantOrientationTeacher = (IGrantOrientationTeacher) ot
//                        .readByOID(GrantOrientationTeacher.class,
//                                infoGrantContract
//                                        .getGrantOrientationTeacherInfo()
//                                        .getIdInternal());// , true
//            } else {
//
//                newGrantOrientationTeacher = new GrantOrientationTeacher();
//                newGrantOrientationTeacher.setIdInternal(new Integer(0));
//                ot.simpleLockWrite(newGrantOrientationTeacher);
//            }
//            oldGrantOrientationTeacher = InfoGrantOrientationTeacherWithTeacherAndGrantContract
//                    .newDomainFromInfo(infoGrantContract
//                            .getGrantOrientationTeacherInfo());
//            Integer ack_opt_lock = newGrantOrientationTeacher.getAckOptLock();
//            PropertyUtils.copyProperties(newGrantOrientationTeacher,
//                    oldGrantOrientationTeacher);
//            newGrantOrientationTeacher
//                    .setGrantContract((IGrantContract) newDomainObject);
//            newGrantOrientationTeacher.setAckOptLock(ack_opt_lock);
//        } catch (ExcepcaoPersistencia e) {
//            throw new FenixServiceException(e);
//        } catch (Exception e) {
//            throw new FenixServiceException(e);
//        }
//
//    }
//
//    private InfoTeacher checkIfGrantOrientationTeacherExists(
//            Integer teacherNumber, IPersistentTeacher pt)
//            throws FenixServiceException {
//        // When creating a New Contract its needed to verify if the teacher
//        // chosen for orientator really exists
//        InfoTeacher infoTeacher = new InfoTeacher();
//        ITeacher teacher = new Teacher();
//        try {
//            teacher = pt.readByNumber(teacherNumber);
//            if (teacher == null)
//                throw new GrantOrientationTeacherNotFoundException();
//            infoTeacher = InfoTeacherWithPerson.newInfoFromDomain(teacher);
//        } catch (ExcepcaoPersistencia persistentException) {
//            throw new FenixServiceException(persistentException.getMessage());
//        }
//        return infoTeacher;
//    }
//
//    private InfoGrantCostCenter checkIfGrantCostContractExists(
//            String costContractNumber, IPersistentGrantCostCenter pt)
//            throws FenixServiceException {
//        // When creating a New Contract its needed to verify if the costContract
//        // exists
//        // chosen for orientator really exists
//        InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
//        IGrantCostCenter costCenter = new GrantCostCenter();
//        try {
//            costCenter = pt.readGrantCostCenterByNumber(costContractNumber);
//            if (costCenter == null)
//                throw new GrantOrientationTeacherNotFoundException();
//            infoGrantCostCenter = InfoGrantCostCenter
//                    .newInfoFromDomain(costCenter);
//        } catch (ExcepcaoPersistencia persistentException) {
//            throw new FenixServiceException(persistentException.getMessage());
//        }
//        return infoGrantCostCenter;
//    }

    public void run(InfoGrantContract infoGrantContract)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentTeacher pTeacher = sp.getIPersistentTeacher();
        IPersistentGrantType pGrantType = sp.getIPersistentGrantType();
        IPersistentGrantContract pGrantContract = sp
                .getIPersistentGrantContract();
        IPersistentGrantOwner pGrantOwner = sp.getIPersistentGrantOwner();
        IPersistentGrantOrientationTeacher pGrantOrientationTeacher = sp
                .getIPersistentGrantOrientationTeacher();
        IPersistentGrantCostCenter pGrantCostCenter = sp
                .getIPersistentGrantCostCenter();

        final IGrantContract grantContract;
        if (infoGrantContract.getContractNumber() == null) {
            // set the contract number!
            Integer maxNumber = pGrantContract
                    .readMaxGrantContractNumberByGrantOwner(infoGrantContract
                            .getGrantOwnerInfo().getIdInternal());
            Integer newContractNumber = new Integer(maxNumber.intValue() + 1);
            infoGrantContract.setContractNumber(newContractNumber);

            grantContract = new GrantContract();
            grantContract.setAssociatedGrantContractMovements(new ArrayList(0));
            grantContract.setAssociatedGrantInsurances(new ArrayList(0));
            grantContract.setAssociatedGrantSubsidies(new ArrayList(0));
            grantContract.setContractRegimes(new ArrayList(0));
        } else {
            grantContract = pGrantContract
                    .readGrantContractByNumberAndGrantOwner(infoGrantContract
                            .getContractNumber(), infoGrantContract.getGrantOwnerInfo()
                            .getIdInternal());
        }
        pGrantContract.simpleLockWrite(grantContract);

        IGrantOwner grantOwner = (IGrantOwner) pGrantOwner.readByOID(
                GrantOwner.class, infoGrantContract.getGrantOwnerInfo()
                        .getIdInternal());
        
        grantContract.setGrantOwner(grantOwner);

        IGrantType grantType = pGrantType.readGrantTypeBySigla(infoGrantContract.getGrantTypeInfo().getSigla());
        if (grantType == null)
            throw new GrantTypeNotFoundException();
        grantContract.setGrantType(grantType);
 
  
        IGrantOrientationTeacher grantOrientationTeacher = (IGrantOrientationTeacher) pGrantOrientationTeacher
                .readByOID(GrantOrientationTeacher.class, infoGrantContract
                        .getGrantOrientationTeacherInfo().getIdInternal());

   //     if(infoGrantContract.getGrantOrientationTeacherInfo().getOrientationTeacherInfo().getTeacherNumber()!= grantOrientationTeacher.getOrientationTeacher().getTeacherNumber())
        if (grantOrientationTeacher == null) {
            if (infoGrantContract.getIdInternal() != null || !infoGrantContract.getIdInternal().equals(new Integer(0)))
                grantOrientationTeacher = pGrantOrientationTeacher
                .readActualGrantOrientationTeacherByContract(infoGrantContract
                        .getIdInternal(),new Integer(0));
         
            if (grantOrientationTeacher == null) {
                grantOrientationTeacher = createNewGrantOrientationTeacher(sp,
                        infoGrantContract.getGrantOrientationTeacherInfo(),
                        grantContract);
            }else{
               
              final ITeacher teacher = pTeacher.readByNumber(infoGrantContract.getGrantOrientationTeacherInfo().getOrientationTeacherInfo().getTeacherNumber());
          
              pGrantOrientationTeacher.simpleLockWrite(grantOrientationTeacher);
              grantOrientationTeacher.setOrientationTeacher(teacher);

            }
               
        }
        
        grantContract.setGrantOrientationTeacher(grantOrientationTeacher);
        
        IGrantCostCenter grantCostCenter = new GrantCostCenter();
        if (infoGrantContract.getGrantCostCenterInfo()!= null && infoGrantContract.getGrantCostCenterInfo().getNumber() != null){   //|| 
            grantCostCenter = pGrantCostCenter
                    .readGrantCostCenterByNumber(infoGrantContract
                            .getGrantCostCenterInfo().getNumber());
            if (grantCostCenter == null)
                throw new GrantOrientationTeacherNotFoundException();
            grantContract.setGrantCostCenter(grantCostCenter);
        
        } else {
            grantContract.setGrantCostCenter(null);
        }
        

        grantContract.setContractNumber(infoGrantContract.getContractNumber());
        grantContract.setDateAcceptTerm(infoGrantContract.getDateAcceptTerm());
        grantContract.setEndContractMotive(infoGrantContract
                .getEndContractMotive());
    }

    private IGrantOrientationTeacher createNewGrantOrientationTeacher(
            ISuportePersistente sp,
            InfoGrantOrientationTeacher grantOrientationTeacherInfo,
            IGrantContract grantContract)throws FenixServiceException, ExcepcaoPersistencia {
        
            IPersistentTeacher pTeacher = sp.getIPersistentTeacher();
            IPersistentGrantOrientationTeacher pGrantOrientationTeacher = sp
            .getIPersistentGrantOrientationTeacher();
            
            final ITeacher teacher = pTeacher.readByNumber(grantOrientationTeacherInfo.getOrientationTeacherInfo().getTeacherNumber());
            final IGrantOrientationTeacher oldGrantOrientationTeacher;
            final IGrantOrientationTeacher newGrantOrientationTeacher;
            final IGrantOrientationTeacher grantOrientationTeacher;
            
            if (teacher == null)
                throw new GrantOrientationTeacherNotFoundException();
            
//             InfoTeacher infoTeacher = InfoTeacherWithPerson.newInfoFromDomain(teacher);
//             grantOrientationTeacherInfo.setOrientationTeacherInfo(infoTeacher);
//             grantOrientationTeacherInfo.setGrantContractInfo(InfoGrantContract.newInfoFromDomain(grantContract));
             
             newGrantOrientationTeacher = new GrantOrientationTeacher();
             pGrantOrientationTeacher.simpleLockWrite(newGrantOrientationTeacher);
             oldGrantOrientationTeacher = InfoGrantOrientationTeacherWithTeacherAndGrantContract.newDomainFromInfo(grantOrientationTeacherInfo);
//             PropertyUtils.copyProperties(newGrantOrientationTeacher,
//                   oldGrantOrientationTeacher);
             try {
                 PropertyUtils.copyProperties(newGrantOrientationTeacher,
                       oldGrantOrientationTeacher);
              //  FenixPropertyUtils.copyProperties(newGrantOrientationTeacher,oldGrantOrientationTeacher);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             newGrantOrientationTeacher.setGrantContract(grantContract);
             newGrantOrientationTeacher.setOrientationTeacher(teacher);
             
  
            
        // TODO Auto-generated method stub
        return newGrantOrientationTeacher;
    }
}