/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author jpvl
 * @author Barbosa
 */
public abstract class EditDomainObjectService extends Service {

    /**
         * Checks if the objectToEdit can be created or update
         * 
         * @param objectToEdit
         * @param existingDomainObject
         * @return
         */
    private boolean canCreate(InfoObject objectToEdit, DomainObject existingDomainObject) {
	/*
         * Not new and exist on database and object ids are equal OR is a new
         * object and doesn't exist on database OR is not new and doesn't exist
         * on database (unique changed)
         */

	return (!isNew(objectToEdit)
		&& ((existingDomainObject != null) && (objectToEdit.getIdInternal()
			.equals(existingDomainObject.getIdInternal()))) || ((existingDomainObject == null)));
    }

    /**
         * By default this method does nothing
         * 
         * @param newDomainObject
         * @param infoObject
         * @param persistentSupport
         */
    protected void doAfterLock(DomainObject domainObjectLocked, InfoObject infoObject) throws Exception {
    }

    /**
         * By default this method does nothing
         * 
         * @param objectLocked
         * @param infoObject
         * @param persistentSupport
         */
    protected void doBeforeLock(DomainObject domainObjectToLock, InfoObject infoObject) throws Exception {
    }

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
         * This method invokes a persistent method to read an DomainObject from
         * database
         * 
         * @param domainObject
         * @return By default returns null. When there is no unique in
         *         domainObject the object that we want to create never exists.
         */
    protected DomainObject readObjectByUnique(InfoObject infoObject) throws Exception {
	return null;
    }

    /**
         * Executes the service
         * 
         * @param objectId
         * @param infoObject
         * @return
         * @throws Exception
         */
    public void run(Integer objectId, InfoObject infoObject) throws Exception {
	DomainObject objectFromDatabase = getObjectFromDatabase(infoObject);
	if (!canCreate(infoObject, objectFromDatabase)) {
	    throw new ExistingServiceException("The object already exists");
	}
	DomainObject domainObject = getObjectToLock(infoObject, objectFromDatabase);

	doBeforeLock(domainObject, infoObject);

	copyInformationFromInfoToDomain(infoObject, domainObject);

	doAfterLock(domainObject, infoObject);
    }

    private DomainObject getObjectToLock(InfoObject infoObject, DomainObject objectFromDatabase)
	    throws InstantiationException, IllegalAccessException {
	DomainObject domainObject = null;

	if (isNew(infoObject)) {
	    domainObject = createNewDomainObject(infoObject);
	} else {
	    domainObject = objectFromDatabase;
	}
	return domainObject;
    }

    protected abstract DomainObject createNewDomainObject(InfoObject infoObject);

    protected abstract DomainObject readDomainObject(final Integer idInternal);

    protected abstract void copyInformationFromInfoToDomain(InfoObject infoObject,
	    DomainObject domainObject) throws ExcepcaoPersistencia, FenixServiceException;

    private DomainObject getObjectFromDatabase(InfoObject infoObject) throws Exception {
	DomainObject objectFromDatabase = readObjectByUnique(infoObject);

	// if the editing means alter unique keys or the there is no unique
	// then read by oid to get the object from database.
	if (objectFromDatabase == null && !isNew(infoObject)) {
	    objectFromDatabase = readDomainObject(infoObject.getIdInternal());
	    // if the object still null then the object doesn't exist.
	    if (objectFromDatabase == null) {
		throw new NonExistingServiceException("Object doesn't exist!");
	    }
	}
	return objectFromDatabase;
    }
}