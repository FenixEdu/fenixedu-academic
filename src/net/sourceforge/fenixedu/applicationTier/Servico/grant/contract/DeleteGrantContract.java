/*
 * Created on May 17, 2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;

/**
 * @author Pica
 * @author Barbosa
 */
public class DeleteGrantContract extends DeleteDomainObjectService {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return GrantContract.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {
        return persistentSupport.getIPersistentGrantContract();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#doBeforeDelete(Dominio.DomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected void doBeforeDelete(DomainObject domainObject, ISuportePersistente persistentSupport)
            throws ExcepcaoPersistencia {
        GrantContract grantContract = (GrantContract) domainObject;

        /*
         * Delete respective GrantOrientationTeacher entry
         */
        deleteAssociatedOrientationTeacher(grantContract);
        /*
         * Delete GrantContract Regimes associated with contract
         */
        deleteAssociatedRegimes(grantContract);
        /*
         * Delete GrantContract Movements associated with contract
         */
        deleteAssociatedMovements(grantContract);
        /*
         * Delete GrantContract Subsidies and associated Grant Parts with
         * contract
         */
        deleteAssociatedSubsidiesAndParts(grantContract);
        /*
         * Delete GrantContract Insurance associated with contract
         */
        deleteAssociatedInsurance(grantContract);
    }

    /**
     * @param persistentSupport
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedOrientationTeacher(GrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantOrientationTeacher pgot = persistentSupport.getIPersistentGrantOrientationTeacher();
        GrantOrientationTeacher grantOrientationTeacher = pgot
                .readActualGrantOrientationTeacherByContract(grantContract.getIdInternal(), new Integer(0));
        if (grantOrientationTeacher != null)
            pgot.deleteByOID(GrantOrientationTeacher.class, grantOrientationTeacher.getIdInternal());
    }

    /**
     * @param persistentSupport
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedInsurance(GrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantInsurance persistentGrantInsurance = persistentSupport.getIPersistentGrantInsurance();
        GrantInsurance grantInsurance = persistentGrantInsurance
                .readGrantInsuranceByGrantContract(grantContract.getIdInternal());
        if (grantInsurance != null)
            persistentGrantInsurance.deleteByOID(GrantInsurance.class, grantInsurance.getIdInternal());
    }

    /**
     * @param persistentSupport
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedSubsidiesAndParts(GrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantSubsidy persistentGrantSubsidy = persistentSupport.getIPersistentGrantSubsidy();
        List subsidiesList = persistentGrantSubsidy.readAllSubsidiesByGrantContract(grantContract
                .getIdInternal());
        if (subsidiesList != null) {
            Iterator subsidiesIter = subsidiesList.iterator();
            while (subsidiesIter.hasNext()) {
                GrantSubsidy grantSubsidy = (GrantSubsidy) subsidiesIter.next();
                persistentGrantSubsidy.deleteByOID(GrantSubsidy.class, grantSubsidy.getIdInternal());
                //Delete GrantParts associated with each subsidy
                IPersistentGrantPart persistentGrantPart = persistentSupport.getIPersistentGrantPart();
                List partsList = persistentGrantPart.readAllGrantPartsByGrantSubsidy(grantSubsidy
                        .getIdInternal());
                if (partsList != null) {
                    Iterator partsIter = partsList.iterator();
                    while (partsIter.hasNext()) {
                        GrantPart grantPart = (GrantPart) partsIter.next();
                        persistentGrantPart.deleteByOID(GrantPart.class, grantPart.getIdInternal());
                    }
                }
            }
        }
    }

    /**
     * @param persistentSupport
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedMovements(GrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantContractMovement persistentGrantContractMovement = persistentSupport
                .getIPersistentGrantContractMovement();
        List movementsList = persistentGrantContractMovement.readAllMovementsByContract(grantContract
                .getIdInternal());
        if (movementsList != null) {
            Iterator movementsIter = movementsList.iterator();
            while (movementsIter.hasNext()) {
                GrantContractMovement grantContractMovement = (GrantContractMovement) movementsIter
                        .next();
                persistentGrantContractMovement.deleteByOID(GrantContractMovement.class,
                        grantContractMovement.getIdInternal());
            }
        }
    }

    /**
     * @param persistentSupport
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedRegimes(GrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantContractRegime pgcr = persistentSupport.getIPersistentGrantContractRegime();
        List regimesList = pgcr.readGrantContractRegimeByGrantContract(grantContract.getIdInternal());
        if (regimesList != null) {
            Iterator regIter = regimesList.iterator();
            while (regIter.hasNext()) {
                GrantContractRegime grantContractRegime = (GrantContractRegime) regIter.next();
                pgcr.deleteByOID(GrantContractRegime.class, grantContractRegime.getIdInternal());
            }
        }
    }
	
    /* (non-Javadoc)
	 * @see net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService#deleteDomainObject(net.sourceforge.fenixedu.domain.DomainObject)
	 */
	protected void deleteDomainObject(DomainObject domainObject) {
		try{
	      persistentObject.deleteByOID(getDomainObjectClass(), domainObject.getIdInternal());
			
		} catch (Exception e) {
			
		}
		
	}

}