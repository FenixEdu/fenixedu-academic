/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author jpvl
 */
public abstract class ReadDomainObjectService implements IService {
    public InfoObject run(Integer objectId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = getIPersistentObject(sp);
            IDomainObject domainObject = persistentObject.readByOID(getDomainObjectClass(), objectId);
            InfoObject infoObject = null;

            if (domainObject != null) {
                infoObject = clone2InfoObject(domainObject);
            }

            return infoObject;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
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

    /**
     * This method invokes the Cloner to convert from IDomainObject to
     * InfoObject
     * 
     * @param infoObject
     * @return
     */
    protected abstract InfoObject clone2InfoObject(IDomainObject domainObject);
}