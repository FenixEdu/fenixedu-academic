/*
 * Created on May 17, 2004
 */

package ServidorAplicacao.Servico.grant.contract;

import java.util.Iterator;
import java.util.List;

import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.GrantContractMovement;
import Dominio.grant.contract.GrantContractRegime;
import Dominio.grant.contract.GrantInsurance;
import Dominio.grant.contract.GrantOrientationTeacher;
import Dominio.grant.contract.GrantPart;
import Dominio.grant.contract.GrantSubsidy;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantContractMovement;
import Dominio.grant.contract.IGrantContractRegime;
import Dominio.grant.contract.IGrantInsurance;
import Dominio.grant.contract.IGrantOrientationTeacher;
import Dominio.grant.contract.IGrantPart;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantContractMovement;
import ServidorPersistente.grant.IPersistentGrantContractRegime;
import ServidorPersistente.grant.IPersistentGrantInsurance;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;
import ServidorPersistente.grant.IPersistentGrantPart;
import ServidorPersistente.grant.IPersistentGrantSubsidy;

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
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia {
        return sp.getIPersistentGrantContract();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#doBeforeDelete(Dominio.IDomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected void doBeforeDelete(IDomainObject domainObject, ISuportePersistente sp)
            throws FenixServiceException, ExcepcaoPersistencia {
        IGrantContract grantContract = (IGrantContract) domainObject;

        /*
         * Delete respective GrantOrientationTeacher entry
         */
        deleteAssociatedOrientationTeacher(sp, grantContract);
        /*
         * Delete GrantContract Regimes associated with contract
         */
        deleteAssociatedRegimes(sp, grantContract);
        /*
         * Delete GrantContract Movements associated with contract
         */
        deleteAssociatedMovements(sp, grantContract);
        /*
         * Delete GrantContract Subsidies and associated Grant Parts with
         * contract
         */
        deleteAssociatedSubsidiesAndParts(sp, grantContract);
        /*
         * Delete GrantContract Insurance associated with contract
         */
        deleteAssociatedInsurance(sp, grantContract);
    }

    /**
     * @param sp
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedOrientationTeacher(ISuportePersistente sp, IGrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantOrientationTeacher pgot = sp.getIPersistentGrantOrientationTeacher();
        IGrantOrientationTeacher grantOrientationTeacher = pgot
                .readActualGrantOrientationTeacherByContract(grantContract, new Integer(0));
        if (grantOrientationTeacher != null)
            pgot.deleteByOID(GrantOrientationTeacher.class, grantOrientationTeacher.getIdInternal());
    }

    /**
     * @param sp
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedInsurance(ISuportePersistente sp, IGrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantInsurance persistentGrantInsurance = sp.getIPersistentGrantInsurance();
        IGrantInsurance grantInsurance = persistentGrantInsurance
                .readGrantInsuranceByGrantContract(grantContract.getIdInternal());
        if (grantInsurance != null)
            persistentGrantInsurance.deleteByOID(GrantInsurance.class, grantInsurance.getIdInternal());
    }

    /**
     * @param sp
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedSubsidiesAndParts(ISuportePersistente sp, IGrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantSubsidy persistentGrantSubsidy = sp.getIPersistentGrantSubsidy();
        List subsidiesList = persistentGrantSubsidy.readAllSubsidiesByGrantContract(grantContract
                .getIdInternal());
        if (subsidiesList != null) {
            Iterator subsidiesIter = subsidiesList.iterator();
            while (subsidiesIter.hasNext()) {
                IGrantSubsidy grantSubsidy = (IGrantSubsidy) subsidiesIter.next();
                persistentGrantSubsidy.deleteByOID(GrantSubsidy.class, grantSubsidy.getIdInternal());
                //Delete GrantParts associated with each subsidy
                IPersistentGrantPart persistentGrantPart = sp.getIPersistentGrantPart();
                List partsList = persistentGrantPart.readAllGrantPartsByGrantSubsidy(grantSubsidy
                        .getIdInternal());
                if (partsList != null) {
                    Iterator partsIter = partsList.iterator();
                    while (partsIter.hasNext()) {
                        IGrantPart grantPart = (IGrantPart) partsIter.next();
                        persistentGrantPart.deleteByOID(GrantPart.class, grantPart.getIdInternal());
                    }
                }
            }
        }
    }

    /**
     * @param sp
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedMovements(ISuportePersistente sp, IGrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantContractMovement persistentGrantContractMovement = sp
                .getIPersistentGrantContractMovement();
        List movementsList = persistentGrantContractMovement.readAllMovementsByContract(grantContract
                .getIdInternal());
        if (movementsList != null) {
            Iterator movementsIter = movementsList.iterator();
            while (movementsIter.hasNext()) {
                IGrantContractMovement grantContractMovement = (IGrantContractMovement) movementsIter
                        .next();
                persistentGrantContractMovement.deleteByOID(GrantContractMovement.class,
                        grantContractMovement.getIdInternal());
            }
        }
    }

    /**
     * @param sp
     * @param grantContract
     * @throws ExcepcaoPersistencia
     */
    private void deleteAssociatedRegimes(ISuportePersistente sp, IGrantContract grantContract)
            throws ExcepcaoPersistencia {
        IPersistentGrantContractRegime pgcr = sp.getIPersistentGrantContractRegime();
        List regimesList = pgcr.readGrantContractRegimeByGrantContract(grantContract.getIdInternal());
        if (regimesList != null) {
            Iterator regIter = regimesList.iterator();
            while (regIter.hasNext()) {
                IGrantContractRegime grantContractRegime = (IGrantContractRegime) regIter.next();
                pgcr.deleteByOID(GrantContractRegime.class, grantContractRegime.getIdInternal());
            }
        }
    }
}