/*
 * Created on 18/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import org.apache.commons.beanutils.PropertyUtils;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import DataBeans.InfoTeacherWithPerson;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import DataBeans.grant.contract.InfoGrantCostCenter;
import DataBeans.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import DataBeans.grant.contract.InfoGrantType;
import Dominio.IDomainObject;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.grant.contract.GrantCostCenter;
import Dominio.grant.contract.GrantOrientationTeacher;
import Dominio.grant.contract.GrantType;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantCostCenter;
import Dominio.grant.contract.IGrantOrientationTeacher;
import Dominio.grant.contract.IGrantType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import ServidorAplicacao.Servico.exceptions.grant.GrantTypeNotFoundException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantCostCenter;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;
import ServidorPersistente.grant.IPersistentGrantType;
import ServidorPersistente.managementAssiduousness.IPersistentCostCenter;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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