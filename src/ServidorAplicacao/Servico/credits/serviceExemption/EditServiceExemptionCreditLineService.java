/*
 * Created on 29/Fev/2004
 */
package ServidorAplicacao.Servico.credits.serviceExemption;

import DataBeans.InfoObject;
import DataBeans.credits.InfoServiceExemptionCreditLine;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditServiceExemptionCreditLineService extends EditDomainObjectService
{

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoServiceExemptionCreditLine2IServiceExemptionCreditLine(
            (InfoServiceExemptionCreditLine) infoObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentServiceExemptionCreditLine();
    }
}
