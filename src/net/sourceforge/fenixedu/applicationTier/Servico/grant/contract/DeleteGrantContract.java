/*
 * Created on May 17, 2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantSubsidy;
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
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContract();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#doBeforeDelete(Dominio.IDomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected void doBeforeDelete(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
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