/*
 * Created on 29/Fev/2004
 */
package ServidorAplicacao.Servico.credits.otherTypeCreditLine;

import DataBeans.InfoObject;
import DataBeans.credits.InfoOtherTypeCreditLine;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditOtherTypeCreditLineService extends EditDomainObjectService {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return Cloner.copyInfoOtherTypeCreditLine2IOtherCreditLine((InfoOtherTypeCreditLine) infoObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia {
        return sp.getIPersistentOtherTypeCreditLine();
    }
}