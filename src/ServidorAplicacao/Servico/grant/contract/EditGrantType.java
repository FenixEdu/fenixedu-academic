/*
 * Created on Jan 24, 2004
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantType;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.IGrantType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantType;

/**
 * @author pica
 * @author barbosa
 */
public class EditGrantType extends EditDomainObjectService {

    /**
     * The constructor of this class.
     */
    public EditGrantType() {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return Cloner.copyInfoGrantType2IGrantType((InfoGrantType) infoObject);
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantType();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantType pgs = sp.getIPersistentGrantType();
        IGrantType grantType = (IGrantType) domainObject;
        return pgs.readGrantTypeBySigla(grantType.getSigla());
    }

    public void run(InfoGrantType infoGrantType) throws FenixServiceException {
        super.run(new Integer(0), infoGrantType);
    }
}