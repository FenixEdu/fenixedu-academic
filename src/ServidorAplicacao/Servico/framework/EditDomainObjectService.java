/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.framework;

import java.beans.Beans;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import Dominio.IDomainObject;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.beanUtils.FenixPropertyUtils;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author jpvl
 * @author Barbosa
 */
public abstract class EditDomainObjectService implements IService {

    /**
     * Checks if the objectToEdit can be created or update
     * 
     * @param objectToEdit
     * @param existingDomainObject
     * @return
     */
    private boolean canCreate(IDomainObject objectToEdit, IDomainObject existingDomainObject) {
        /*
         * Not new and exist on database and object ids are equal OR is a new
         * object and doesn't exist on database OR is not new and doesn't exist
         * on database (unique changed)
         */

        return (!isNew(objectToEdit)
                && ((existingDomainObject != null) && (objectToEdit.getIdInternal()
                        .equals(existingDomainObject.getIdInternal())))
                || ((existingDomainObject == null) && isNew(objectToEdit)) || ((!isNew(objectToEdit) && (existingDomainObject == null))));
    }

    /**
     * This method invokes the Cloner to convert from InfoObject to
     * IDomainObject
     * 
     * @param infoObject
     * @return
     */
    protected abstract IDomainObject clone2DomainObject(InfoObject infoObject);

    /**
     * By default this method does nothing
     * 
     * @param newDomainObject
     * @param infoObject
     * @param sp
     */
    protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {
    }

    /**
     * By default this method does nothing
     * 
     * @param objectLocked
     * @param infoObject
     * @param sp
     */
    protected void doBeforeLock(IDomainObject domainObjectToLock, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {
    }

    /**
     * @param newDomainObject
     * @param sp
     */
    private void fillAssociatedObjects(IDomainObject newDomainObject, IPersistentObject po,
            IDomainObject objectToEdit) throws FenixServiceException {
        try {
            Map propertiesMap = PropertyUtils.describe(objectToEdit);
            Iterator iterator = propertiesMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object value = entry.getValue();

                if ((value != null) && (Beans.isInstanceOf(value, IDomainObject.class))) {
                    IDomainObject o = po.readByOID(value.getClass(), ((IDomainObject) value)
                            .getIdInternal(), false);
                    PropertyUtils.setProperty(newDomainObject, entry.getKey().toString(), o);
                }
            }
        } catch (Exception e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
     * @param sp
     * @return
     */
    protected abstract IPersistentObject getIPersistentObject(ISuportePersistente sp)
            throws ExcepcaoPersistencia;

    /**
     * Checks if the internalId of the object is null or 0
     * 
     * @param domainObject
     * @return
     */
    protected boolean isNew(IDomainObject domainObject)

    {
        Integer objectId = domainObject.getIdInternal();
        return ((objectId == null) || objectId.equals(new Integer(0)));
    }

    /**
     * This method invokes a persistent method to read an IDomainObject from
     * database
     * 
     * @param domainObject
     * @return By default returns null. When there is no unique in domainObject
     *         the object that we want to create never exists.
     */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws FenixServiceException, ExcepcaoPersistencia {
        return null;
    }

    /**
     * Executes the service
     * 
     * @param objectId
     * @param infoObject
     * @return @throws
     *         FenixServiceException
     * 
     * TODO Remove objectId from method signature.
     */
    public void run(Integer objectId, InfoObject infoObject) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = getIPersistentObject(sp);
            IDomainObject objectToEdit = clone2DomainObject(infoObject);

            IDomainObject objectFromDatabase = getObjectFromDatabase(objectToEdit, sp);
            if (!canCreate(objectToEdit, objectFromDatabase)) {
                throw new ExistingServiceException("The object already exists");
            }
            IDomainObject domainObject = getObjectToLock(objectToEdit, objectFromDatabase);

            doBeforeLock(domainObject, infoObject, sp);

            persistentObject.simpleLockWrite(domainObject);
            //Integer ackOptLock = domainObject.getAckOptLock();
            //Integer id = domainObject.getIdInternal();
            //PropertyUtils.copyProperties(domainObject, objectToEdit);
            FenixPropertyUtils.copyProperties(domainObject, objectToEdit);
            //domainObject.setIdInternal(id);
            //domainObject.setAckOptLock(ackOptLock);
            fillAssociatedObjects(domainObject, persistentObject, objectToEdit);

            doAfterLock(domainObject, infoObject, sp);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            if (e instanceof FenixServiceException) {
                throw (FenixServiceException) e;
            }
            throw new FenixServiceException(e);
        }
    }

    private IDomainObject getObjectToLock(IDomainObject objectToEdit, IDomainObject objectFromDatabase)
            throws InstantiationException, IllegalAccessException {
        IDomainObject domainObject = null;

        if (isNew(objectToEdit)) {
            domainObject = (IDomainObject) objectToEdit.getClass().newInstance();
        } else {
            domainObject = objectFromDatabase;
        }
        return domainObject;
    }

    private IDomainObject getObjectFromDatabase(IDomainObject objectToEdit, ISuportePersistente sp)
            throws ExcepcaoPersistencia, FenixServiceException, NonExistingServiceException {
        IDomainObject objectFromDatabase = readObjectByUnique(objectToEdit, sp);

        // if the editing means alter unique keys or the there is no unique
        // then read by oid to get the object from database.
        if (objectFromDatabase == null && !isNew(objectToEdit)) {
            objectFromDatabase = getIPersistentObject(sp).readByOID(objectToEdit.getClass(),
                    objectToEdit.getIdInternal(), false);
            // if the object still null then the object doesn't exist.
            if (objectFromDatabase == null) {
                throw new NonExistingServiceException("Object doesn't exist!");
            }
        }
        return objectFromDatabase;
    }
}