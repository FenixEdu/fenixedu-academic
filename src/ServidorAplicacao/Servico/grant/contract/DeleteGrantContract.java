/*
 * Created on May 17, 2004
 */

package ServidorAplicacao.Servico.grant.contract;

import java.util.Iterator;
import java.util.List;

import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.GrantContractRegime;
import Dominio.grant.contract.GrantOrientationTeacher;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantContractRegime;
import Dominio.grant.contract.IGrantOrientationTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantContractRegime;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;

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
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        return sp.getIPersistentGrantContract();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#doBeforeDelete(Dominio.IDomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected void doBeforeDelete(IDomainObject domainObject,ISuportePersistente sp) 
    		throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentGrantOrientationTeacher pgot = sp.getIPersistentGrantOrientationTeacher();
        IPersistentGrantContractRegime pgcr = sp.getIPersistentGrantContractRegime();
        
        IGrantContract grantContract = (IGrantContract) domainObject;
        
        /*
         * Delete respective GrantOrientationTeacher entry
         */
        IGrantOrientationTeacher grantOrientationTeacher = pgot
                .readActualGrantOrientationTeacherByContract(grantContract,
                        new Integer(0));
        if (grantOrientationTeacher != null)
                pgot.deleteByOID(GrantOrientationTeacher.class,
                        grantOrientationTeacher.getIdInternal());
        
        /*
         * Delete GrantContract Regimes associated with contract
         */
        List regimesList = pgcr
                .readGrantContractRegimeByGrantContract(grantContract
                        .getIdInternal());
        if (regimesList != null) {
            Iterator regIter = regimesList.iterator();
            while (regIter.hasNext()) {
                IGrantContractRegime grantContractRegime = (IGrantContractRegime) regIter
                        .next();
                pgcr.deleteByOID(GrantContractRegime.class, grantContractRegime
                        .getIdInternal());
            }
        }
    }
}
