/*
 * Created on Jun 26, 2004
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantInsuranceWithContract;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantInsurance;
import Dominio.grant.contract.IGrantInsurance;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;


/**
 * @author Barbosa
 * @author Pica
 */
public class ReadGrantInsurance extends ReadDomainObjectService {

    public ReadGrantInsurance () {
        
    }
    
    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return GrantInsurance.class;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia {
        return sp.getIPersistentGrantInsurance();
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject) {

        return InfoGrantInsuranceWithContract.newInfoFromDomain((IGrantInsurance) domainObject);
    }

}
