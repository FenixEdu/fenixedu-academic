/*
 * Created on 23/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantPaymentEntity;
import Dominio.grant.contract.IGrantPaymentEntity;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantPaymentEntity;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantPaymentEntitiesByClassName implements IService
{
    /**
     * The constructor of this class.
     */
    public ReadAllGrantPaymentEntitiesByClassName()
    {
    }

    public List run(String className) throws FenixServiceException
    {
        List result = null;
        IPersistentGrantPaymentEntity pgpe = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            pgpe = sp.getIPersistentGrantPaymentEntity();
            List grantPaymentEntities = pgpe.readAllPaymentEntitiesByClassName(className);
            
            result = (List) CollectionUtils.collect(grantPaymentEntities, new Transformer()
                {
                public Object transform(Object o)
                {
                    IGrantPaymentEntity grantPaymentEntity = (IGrantPaymentEntity) o;
                    return InfoGrantPaymentEntity.newInfoFromDomain(grantPaymentEntity);
                }
            });
            
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }

        return result;
    }
}