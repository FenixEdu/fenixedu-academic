/*
 * Created on Jan 24, 2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;

/**
 * @author pica
 * @author barbosa
 */
public class EditGrantContractRegime extends EditDomainObjectService {

    @Override
	protected void copyInformationFromIntoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject) throws ExcepcaoPersistencia {
    	InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
		IGrantContractRegime grantContractRegime = (IGrantContractRegime) domainObject;
		grantContractRegime.setDateBeginContract(infoGrantContractRegime.getDateBeginContract());
		grantContractRegime.setDateDispatchCC(infoGrantContractRegime.getDateDispatchCC());
		grantContractRegime.setDateDispatchCD(infoGrantContractRegime.getDateDispatchCD());
		grantContractRegime.setDateEndContract(infoGrantContractRegime.getDateEndContract());
		grantContractRegime.setDateSendDispatchCC(infoGrantContractRegime.getDateSendDispatchCC());
		grantContractRegime.setDateSendDispatchCD(infoGrantContractRegime.getDateSendDispatchCD());
		grantContractRegime.setCostCenterKey(infoGrantContractRegime.getCostCenterKey());
		grantContractRegime.setState(infoGrantContractRegime.getState());
		
		
		IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
		IGrantContract grantContract = (IGrantContract) persistentGrantContract.readByOID(GrantContract.class ,infoGrantContractRegime.getInfoGrantContract().getIdInternal());
		grantContractRegime.setGrantContract(grantContract);

		if (grantContract.getCostCenterKey() != null && grantContract.getCostCenterKey() != new Integer(0)){
			IPersistentGrantCostCenter persistentGrantCostCenter = sp.getIPersistentGrantCostCenter();	
			IGrantCostCenter grantCostCenter = (IGrantCostCenter)persistentGrantCostCenter.readByOID(GrantCostCenter.class,infoGrantContractRegime.getIdInternal());
			grantContractRegime.setGrantCostCenter(grantCostCenter);
		}else{
			grantContractRegime.setGrantCostCenter(null);
		}
		
	}

	@Override
	protected IDomainObject createNewDomainObject(InfoObject infoObject) {
		return new GrantContractRegime();
	}

	@Override
	protected Class getDomainObjectClass() {	
		return GrantContractRegime.class;
	}

	public EditGrantContractRegime() {
    }

//    protected IDomainObject clone2DomainObject(InfoObject infoObject) throws ExcepcaoPersistencia {
//        return InfoGrantContractRegimeWithTeacherAndContract
//                .newDomainFromInfo((InfoGrantContractRegime) infoObject);
//    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContractRegime();
    }

    protected IDomainObject readObjectByUnique(InfoObject infoObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantContractRegime persistentGrantContractRegime = sp
                .getIPersistentGrantContractRegime();
        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        return persistentGrantContractRegime.readByOID(GrantContractRegime.class, infoGrantContractRegime
                .getIdInternal());
    }

    public void run(InfoGrantContractRegime infoGrantContractRegime) throws FenixServiceException, ExcepcaoPersistencia {
 
        super.run(new Integer(0), infoGrantContractRegime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {

        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        if (infoGrantContractRegime.getState().equals(InfoGrantContractRegime.getActiveState())) {
            //Active Contract Regime
 
            IGrantContractRegime grantContractRegime = (IGrantContractRegime) domainObjectLocked;
            //Set the correct grant orientation teacher
            try {
                IPersistentGrantOrientationTeacher persistentGrantOrientationTeacher = sp
                        .getIPersistentGrantOrientationTeacher();
                IPersistentGrantCostCenter pGrantCostCenter = sp
                .getIPersistentGrantCostCenter();
                IPersistentTeacher pTeacher = sp.getIPersistentTeacher();
 
                IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
        		IGrantContract grantContract = (IGrantContract) persistentGrantContract.readByOID(GrantContract.class ,infoGrantContractRegime.getInfoGrantContract().getIdInternal());
               // IGrantCostCenter grantCostCenter = new GrantCostCenter();
                if (infoGrantContractRegime.getGrantCostCenterInfo()!= null && ((infoGrantContractRegime.getGrantCostCenterInfo().getNumber()).trim()).length()>0) {   //||        
                	IGrantCostCenter grantCostCenter = pGrantCostCenter
                                .readGrantCostCenterByNumber(infoGrantContractRegime
                                        .getGrantCostCenterInfo().getNumber());
                        if (grantCostCenter == null)
                            throw new GrantOrientationTeacherNotFoundException();
                        grantContract.setGrantCostCenter(grantCostCenter);
                    
                } else {
                    grantContract.setGrantCostCenter(null);
                }
              
                grantContractRegime.setGrantCostCenter(grantContract.getGrantCostCenter());

                IGrantOrientationTeacher grantOrientationTeacher = persistentGrantOrientationTeacher
                        .readActualGrantOrientationTeacherByContract(grantContract.getIdInternal(), new Integer(0));
                if (grantOrientationTeacher!=null){

                persistentGrantOrientationTeacher.simpleLockWrite(grantOrientationTeacher);
                InfoTeacher infoTeacher = new InfoTeacher();
                //If grantOrientationTeacher is filled in grantContractRegime
                final ITeacher teacher;
                if (infoGrantContractRegime.getInfoTeacher() != null) {
                    if (infoGrantContractRegime.getInfoTeacher().getTeacherNumber().equals(grantOrientationTeacher.getOrientationTeacher().getTeacherNumber())){
                    //Update grant orientation teacher of contract
                    	IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

                        
                    	teacher = teacherDAO.readByNumber(grantOrientationTeacher.getOrientationTeacher().getTeacherNumber());
                    	IPessoaPersistente persistentPerson =sp.getIPessoaPersistente();
                    	IPerson person = (IPerson)persistentPerson.readByOID(Person.class,teacher.getPerson().getIdInternal() );
                    	infoTeacher.setTeacherNumber(teacher.getTeacherNumber());
                    	InfoPerson infoPerson = new InfoPerson();
                    	infoPerson = (InfoPerson) getInfoPerson(person);
                    	infoTeacher.setInfoPerson(infoPerson);
                    //InfoTeacherWithPerson.newInfoFromDomain(grantOrientationTeacher.getOrientationTeacher()); //
                    }else{
                        teacher = pTeacher.readByNumber(infoGrantContractRegime.getInfoTeacher().getTeacherNumber());
                        IPessoaPersistente persistentPerson =sp.getIPessoaPersistente();
                    	IPerson person = (IPerson)persistentPerson.readByOID(Person.class,teacher.getPerson().getIdInternal() );
                    	infoTeacher.setTeacherNumber(teacher.getTeacherNumber());
                    	InfoPerson infoPerson = new InfoPerson();
                    	infoPerson = (InfoPerson) getInfoPerson(person);
                    	infoTeacher.setInfoPerson(infoPerson);
                        //infoTeacher = InfoTeacherWithPerson.newInfoFromDomain(teacher);                    
                    }
                    
                    
                   grantOrientationTeacher.setOrientationTeacher(teacher);
                }
                grantOrientationTeacher.setBeginDate(infoGrantContractRegime.getDateBeginContract());
                grantOrientationTeacher.setEndDate(infoGrantContractRegime.getDateEndContract());
                
                grantContractRegime.setTeacher(grantOrientationTeacher.getOrientationTeacher());
                
                }
            } catch (ExcepcaoPersistencia persistentException) {
                throw new FenixServiceException(persistentException.getMessage());
            }

            //Set all the others GrantContractRegime that are active to state
            // inactive

            try {
                IPersistentGrantContractRegime persistentGrantContractRegime = sp
                        .getIPersistentGrantContractRegime();
                List activeContractRegime = persistentGrantContractRegime
                        .readGrantContractRegimeByGrantContractAndState(grantContractRegime
                                .getGrantContract().getIdInternal(), new Integer(1));
                if (activeContractRegime != null && !activeContractRegime.isEmpty()) {
                    //Desactivate the contracts
                    for (int i = 0; i < activeContractRegime.size(); i++) {
                        IGrantContractRegime grantContractRegimeTemp = (IGrantContractRegime) activeContractRegime
                                .get(i);
                        if (!grantContractRegimeTemp.equals(grantContractRegime)) {
                            persistentGrantContractRegime.simpleLockWrite(grantContractRegimeTemp);
                            grantContractRegimeTemp.setState(InfoGrantContractRegime.getInactiveState());
                        }
                    }
                }
            } catch (ExcepcaoPersistencia persistentException) {
                throw new FenixServiceException(persistentException.getMessage());
            }
           
            try {
                //Change the data from the insurance
                IPersistentGrantInsurance persistentGrantInsurance = sp.getIPersistentGrantInsurance();
               
                IGrantInsurance grantInsurance = persistentGrantInsurance
                        .readGrantInsuranceByGrantContract(grantContractRegime.getGrantContract()
                                .getIdInternal());
                if (grantInsurance != null) {
                	
                    persistentGrantInsurance.simpleLockWrite(grantInsurance);
                    grantInsurance.setDateEndInsurance(infoGrantContractRegime.getDateEndContract());
                    grantInsurance.setTotalValue(InfoGrantInsurance.calculateTotalValue(grantInsurance
                            .getDateBeginInsurance(), grantInsurance.getDateEndInsurance()));
                }
            } catch (ExcepcaoPersistencia e) {
                throw new FenixServiceException();
            }
           
        }
        
       
    }
    protected InfoPerson getInfoPerson(IPerson person)
    {
        InfoPerson infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPerson();
           
            infoPerson.setIdInternal(person.getIdInternal());
            infoPerson. setNome(person.getNome());        
            infoPerson.setUsername(person.getUsername());
            
        }
        return infoPerson;
   	
   	
   }
 

}

