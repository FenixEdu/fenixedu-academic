/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.framework;

import org.apache.commons.beanutils.PropertyUtils;

import DataBeans.InfoObject;
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
public abstract class EditDomainObjectService implements IServico
{
    public Boolean run(Integer objectId, InfoObject infoObject) throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = getIPersistentObject(sp);
            IDomainObject oldDomainObject = clone2DomainObject(infoObject);

            if (canCreate(oldDomainObject, sp))
            {
                /**
                 * FIXME: Edit an existing object problems. It seems that we can't upgrade lock.
                 * @see ServidorAplicacao.Servicos.teacher.EditWeeklyOcupationTest#testEditExistingWeeklyOcupation()
                 * Without this two lines the test above doesn't run.
                 */
                sp.confirmarTransaccao();
                sp.iniciarTransaccao();
                /************************************************************************************************/
                
                IDomainObject newDomainObject = (IDomainObject) oldDomainObject.getClass().newInstance();
                newDomainObject.setIdInternal(oldDomainObject.getIdInternal());
                persistentObject.simpleLockWrite(newDomainObject);
                PropertyUtils.copyProperties(newDomainObject, oldDomainObject);
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
	 * @param domainObject
	 * @return
	 */
    protected boolean canCreate(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        return true;
    }

    /**
	 * @param sp
	 * @return
	 */
    protected abstract IPersistentObject getIPersistentObject(ISuportePersistente sp);

    /**
	 * This method invokes the Cloner to convert from InfoObject to IDomainObject
	 * 
	 * @param infoObject
	 * @return
	 */
    protected abstract IDomainObject clone2DomainObject(InfoObject infoObject);

}
