/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.framework;

import Dominio.IDomainObject;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author jpvl
 */
public abstract class DeleteDomainObjectService implements IServico
{
    public Boolean run(Integer objectId) throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = getIPersistentObject(sp);

            IDomainObject domainObject =
                (IDomainObject) persistentObject.readByOID(getDomainObjectClass(), objectId);

            if (domainObject != null && canDelete(domainObject))
            {
                persistentObject.deleteByOID(getDomainObjectClass(), objectId);
                return Boolean.TRUE;
            }

            return Boolean.FALSE;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        } catch (Exception e)
        {
            throw new FenixServiceException(e);
        }
    }

    /**
	 * By default returns true
	 * 
	 * @param newDomainObject
	 * @return
	 */
    protected boolean canDelete(IDomainObject newDomainObject)
    {
        return true;
    }

    /**
	 * This is the class in witch the broker will read and delete the DomainObject
	 * 
	 * @return
	 */
    protected abstract Class getDomainObjectClass();

    /**
	 * @param sp
	 * @return
	 */
    protected abstract IPersistentObject getIPersistentObject(ISuportePersistente sp);
}
