/*
 * Created on Feb 17, 2004
 */
package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantPaymentEntity;
import Dominio.grant.contract.IGrantPaymentEntity;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantPaymentEntity;

/**
 * @author pica
 * @author barbosa
 */
public class ReadPaymentEntityByNumberAndClass implements IService
{
	public ReadPaymentEntityByNumberAndClass()
	{
	}

    public InfoGrantPaymentEntity run(String paymentEntityNumber, String className) throws FenixServiceException
    {
        IGrantPaymentEntity grantPaymentEntity = null;
        InfoGrantPaymentEntity result = null;
        IPersistentGrantPaymentEntity pgpe = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            pgpe = sp.getIPersistentGrantPaymentEntity();
            grantPaymentEntity = pgpe.readByNumberAndClass(paymentEntityNumber, className);
            result = InfoGrantPaymentEntity.newInfoFromDomain(grantPaymentEntity);
            
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }
        return result;
    }
}
