/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
    private boolean canCreate(InfoObject objectToEdit, IDomainObject existingDomainObject) {
        /*
         * Not new and exist on database and object ids are equal OR is a new
         * object and doesn't exist on database OR is not new and doesn't exist
         * on database (unique changed)
         */

        return (!isNew(objectToEdit)
                && ((existingDomainObject != null) && (objectToEdit.getIdInternal()
                        .equals(existingDomainObject.getIdInternal())))
                || ((existingDomainObject == null)));
    }

    /**
     * By default this method does nothing
     * 
     * @param newDomainObject
     * @param infoObject
     * @param sp
     */
    protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws Exception {
    }

    /**
     * By default this method does nothing
     * 
     * @param objectLocked
     * @param infoObject
     * @param sp
     */
    protected void doBeforeLock(IDomainObject domainObjectToLock, InfoObject infoObject,
            ISuportePersistente sp) throws Exception {
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
    protected boolean isNew(InfoObject domainObject)

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
    protected IDomainObject readObjectByUnique(InfoObject infoObject, ISuportePersistente sp)
            throws Exception {
        return null;
    }

    /**
     * Executes the service
     * 
     * @param objectId
     * @param infoObject
     * @return
     * @throws FenixServiceException
     * 
     * TODO Remove objectId from method signature.
     */
    public void run(Integer objectId, InfoObject infoObject) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentObject persistentObject = getIPersistentObject(sp);

            IDomainObject objectFromDatabase = getObjectFromDatabase(infoObject, sp);
            if (!canCreate(infoObject, objectFromDatabase)) {
                throw new ExistingServiceException("The object already exists");
            }
            IDomainObject domainObject = getObjectToLock(infoObject, objectFromDatabase);

            doBeforeLock(domainObject, infoObject, sp);

            persistentObject.simpleLockWrite(domainObject);

            copyInformationFromIntoToDomain(sp, infoObject, domainObject);

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

    private IDomainObject getObjectToLock(InfoObject infoObject, IDomainObject objectFromDatabase)
            throws InstantiationException, IllegalAccessException {
        IDomainObject domainObject = null;

        if (isNew(infoObject)) {
        	domainObject = createNewDomainObject(infoObject);
        } else {
            domainObject = objectFromDatabase;
        }
        return domainObject;
    }

    protected abstract IDomainObject createNewDomainObject(InfoObject infoObject);
    protected abstract Class getDomainObjectClass();
    protected abstract void copyInformationFromIntoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject)throws ExcepcaoPersistencia,FenixServiceException;

	private IDomainObject getObjectFromDatabase(InfoObject infoObject, ISuportePersistente sp)
            throws Exception {
        IDomainObject objectFromDatabase = readObjectByUnique(infoObject, sp);

        // if the editing means alter unique keys or the there is no unique
        // then read by oid to get the object from database.
        if (objectFromDatabase == null && !isNew(infoObject)) {
            objectFromDatabase = getIPersistentObject(sp).readByOID(getDomainObjectClass(),
                    infoObject.getIdInternal(), false);
            // if the object still null then the object doesn't exist.
            if (objectFromDatabase == null) {
                throw new NonExistingServiceException("Object doesn't exist!");
            }
        }
        return objectFromDatabase;
    }
}