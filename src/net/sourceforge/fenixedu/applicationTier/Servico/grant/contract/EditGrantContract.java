/*
 * Created on 18/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantTypeNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantContract extends EditDomainObjectService {

    public EditGrantContract() {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return InfoGrantContractWithGrantOwnerAndGrantType
                .newDomainFromInfo((InfoGrantContract) infoObject);
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContract();
    }

    protected void doBeforeLock(IDomainObject domainObjectToLock, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {
        checkIfGrantTeacherRelationExists(domainObjectToLock, infoObject, sp);
    }

    protected void checkIfGrantTeacherRelationExists(IDomainObject newDomainObject,
            InfoObject infoObject, ISuportePersistente sp) throws FenixServiceException {

    	try {
            IPersistentGrantOrientationTeacher ot = sp.getIPersistentGrantOrientationTeacher();
            InfoGrantContract infoGrantContract = (InfoGrantContract) infoObject;
            IGrantOrientationTeacher oldGrantOrientationTeacher = null;
            IGrantOrientationTeacher newGrantOrientationTeacher = null;
 
     
            //check if the GrantOrientation relation exists
            Integer orientationId = infoGrantContract.getGrantOrientationTeacherInfo().getIdInternal();
            if ((orientationId != null) && !(orientationId.equals(new Integer(0)))) {
                //lock the existent object to write (EDIT)
           	
            	newGrantOrientationTeacher = (IGrantOrientationTeacher) ot.readByOID(
                        GrantOrientationTeacher.class, infoGrantContract
                                .getGrantOrientationTeacherInfo().getIdInternal(), true);
            } else {
            	 
                newGrantOrientationTeacher = new GrantOrientationTeacher();
                newGrantOrientationTeacher.setIdInternal(new Integer(0));
                ot.simpleLockWrite(newGrantOrientationTeacher);
            }
            oldGrantOrientationTeacher = InfoGrantOrientationTeacherWithTeacherAndGrantContract
                    .newDomainFromInfo(infoGrantContract.getGrantOrientationTeacherInfo());
            Integer ack_opt_lock = newGrantOrientationTeacher.getAckOptLock();
            PropertyUtils.copyProperties(newGrantOrientationTeacher, oldGrantOrientationTeacher);
            newGrantOrientationTeacher.setGrantContract((IGrantContract) newDomainObject);
            newGrantOrientationTeacher.setAckOptLock(ack_opt_lock);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
   
    }

    protected InfoGrantType checkIfGrantTypeExists(String sigla, IPersistentGrantType pt)
            throws FenixServiceException {
        InfoGrantType infoGrantType = new InfoGrantType();
        IGrantType grantType = new GrantType();
        try {
            grantType = pt.readGrantTypeBySigla(sigla);
            infoGrantType = InfoGrantType.newInfoFromDomain(grantType);
            if (infoGrantType == null)
                throw new GrantTypeNotFoundException();
        } catch (ExcepcaoPersistencia persistentException) {
            throw new FenixServiceException(persistentException.getMessage());
        }
        return infoGrantType;
    }

    private InfoTeacher checkIfGrantOrientationTeacherExists(Integer teacherNumber, IPersistentTeacher pt)
            throws FenixServiceException {
        //When creating a New Contract its needed to verify if the teacher
        //chosen for orientator really exists
        InfoTeacher infoTeacher = new InfoTeacher();
        ITeacher teacher = new Teacher();
        try {
            teacher = pt.readByNumber(teacherNumber);
            if (teacher == null)
                throw new GrantOrientationTeacherNotFoundException();
            infoTeacher = InfoTeacherWithPerson.newInfoFromDomain(teacher);
        } catch (ExcepcaoPersistencia persistentException) {
            throw new FenixServiceException(persistentException.getMessage());
        }
        return infoTeacher;
    }
    
    
    private InfoGrantCostCenter checkIfGrantCostContractExists(String costContractNumber, IPersistentGrantCostCenter pt)
    throws FenixServiceException {
		//When creating a New Contract its needed to verify if the costContract exists
		//chosen for orientator really exists
		InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
		IGrantCostCenter costCenter = new GrantCostCenter();
		try {  
			costCenter = pt.readGrantCostCenterByNumber(costContractNumber);
		    if (costCenter == null)
		        throw new GrantOrientationTeacherNotFoundException();
		    infoGrantCostCenter = InfoGrantCostCenter.newInfoFromDomain(costCenter);
		} catch (ExcepcaoPersistencia persistentException) {
		    throw new FenixServiceException(persistentException.getMessage());
		}
		return infoGrantCostCenter;
	}

    /**
     * Executes the service.
     *  
     */
    public void run(InfoGrantContract infoGrantContract) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher pTeacher = sp.getIPersistentTeacher();
            IPersistentGrantType pGrantType = sp.getIPersistentGrantType();
            IPersistentGrantContract pGrantContract = sp.getIPersistentGrantContract();
            IPersistentGrantCostCenter pCostContract = sp.getIPersistentGrantCostCenter();

            infoGrantContract.setGrantTypeInfo(checkIfGrantTypeExists(infoGrantContract
                    .getGrantTypeInfo().getSigla(), pGrantType));

            infoGrantContract.getGrantOrientationTeacherInfo().setOrientationTeacherInfo(
                    checkIfGrantOrientationTeacherExists(infoGrantContract
                            .getGrantOrientationTeacherInfo().getOrientationTeacherInfo()
                            .getTeacherNumber(), pTeacher));
            if (infoGrantContract.getGrantCostCenterInfo().getNumber().length()!=0){
            infoGrantContract.setGrantCostCenterInfo(
            		checkIfGrantCostContractExists(infoGrantContract.getGrantCostCenterInfo().getNumber()
                            , pCostContract));
            }

            if (infoGrantContract.getContractNumber() == null) {
                // set the contract number!
                Integer maxNumber = pGrantContract
                        .readMaxGrantContractNumberByGrantOwner(infoGrantContract.getGrantOwnerInfo()
                                .getIdInternal());
                int aux = maxNumber.intValue() + 1;
                Integer newContractNumber = new Integer(aux);
                infoGrantContract.setContractNumber(newContractNumber);
            }
       
            super.run(infoGrantContract.getIdInternal(), infoGrantContract);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            if (e instanceof FenixServiceException) {
                throw (FenixServiceException) e;
            }
            throw new FenixServiceException(e);
        }
    }
}