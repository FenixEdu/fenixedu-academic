/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.framework;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IDomainObject;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author jpvl
 */
public abstract class DeleteDomainObjectService implements IService {
    public void run(Integer objectId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = getIPersistentObject(sp);

            IDomainObject domainObject = persistentObject.readByOID(getDomainObjectClass(), objectId);

            if ((domainObject == null) || !canDelete(domainObject, sp)) {
                throw new NonExistingServiceException("The object does not exist");
            }
            doBeforeDelete(domainObject, sp);
            persistentObject.deleteByOID(getDomainObjectClass(), objectId);
            doAfterDelete(domainObject, sp);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }
    }

    /**
     * @param domainObject
     * @param sp
     */
    protected void doBeforeDelete(IDomainObject domainObject, ISuportePersistente sp)
            throws FenixServiceException, ExcepcaoPersistencia {
    }

    /**
     * @param domainObject
     */
    protected void doAfterDelete(IDomainObject domainObject, ISuportePersistente sp)
            throws FenixServiceException, ExcepcaoPersistencia {

    }

    /**
     * By default returns true
     * 
     * @param newDomainObject
     * @return
     */
    protected boolean canDelete(IDomainObject newDomainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        return true;
    }

    /**
     * This is the class in witch the broker will read and delete the
     * DomainObject
     * 
     * @return
     */
    protected abstract Class getDomainObjectClass();

    /**
     * @param sp
     * @return
     */
    protected abstract IPersistentObject getIPersistentObject(ISuportePersistente sp)
            throws ExcepcaoPersistencia;
}