/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.framework;

import java.beans.Beans;
import java.util.Iterator;
import java.util.Map;

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
    public Boolean run(InfoObject infoObject) throws FenixServiceException
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
                fillAssociatedObjects(newDomainObject, persistentObject);
                doAfterLock(newDomainObject, infoObject, sp);
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
     * @param newDomainObject
     * @param infoObject
     * @param sp
     */
    protected void doAfterLock(IDomainObject newDomainObject,InfoObject infoObject,ISuportePersistente sp)
        throws FenixServiceException
    {
        return; //by default this method does nothing
    }

    /**
     * @param newDomainObject
     * @param sp
     */
    private void fillAssociatedObjects(IDomainObject newDomainObject, IPersistentObject po)
        throws FenixServiceException
    {
        try
        {
            Map propertiesMap = PropertyUtils.describe(newDomainObject);
            Iterator iterator = propertiesMap.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object value = entry.getValue();

                if ((value != null) && (Beans.isInstanceOf(value, IDomainObject.class)))
                {
                    IDomainObject o = po.readByOId((IDomainObject) value, false);
                    PropertyUtils.setProperty(newDomainObject, entry.getKey().toString(), o);
                }
            }
        } catch (Exception e)
        {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
     * By default returns true
     * 
     * @param domainObject
     * @return
     */
    protected boolean canCreate(IDomainObject newDomainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IDomainObject existingDomainObject = readObjectByUnique(newDomainObject, sp);
        /**
		 * não é novo e existe na bd e os ids são iguais || não existe na bd e é novo || não é novo e
		 * não existe na bd
		 */
        return (
            !isNew(newDomainObject)
                && ((existingDomainObject != null)
                    && (newDomainObject.getIdInternal().equals(existingDomainObject.getIdInternal())))
                || ((existingDomainObject == null) && isNew(newDomainObject))
                || ((!isNew(newDomainObject) && (existingDomainObject == null))));
    }

    /**
	 * @param sp
	 * @return
	 */
    protected abstract IPersistentObject getIPersistentObject(ISuportePersistente sp)
        throws ExcepcaoPersistencia;

    /*
	 * Checks if the internalId of the object is null or 0
	 * 
	 * @param domainObject @return
	 */
    protected boolean isNew(IDomainObject domainObject)
    
    {
        Integer objectId = domainObject.getIdInternal();
        return ((objectId == null) || objectId.equals(new Integer(0)));
    }

    /**
     * This method invokes the Cloner to convert from InfoObject to IDomainObject
     * 
     * @param infoObject
     * @return
     */
    protected abstract IDomainObject clone2DomainObject(InfoObject infoObject);

    /**
     * This method invokes a persistent method to read an IDomainObject from database
     * 
     * @param domainObject
     * @return
     */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        return null;
    }
}
