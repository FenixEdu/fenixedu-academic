/*
 * Created on Nov 18, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * Do a search on database.
 * 
 * @author jpvl
 */
public abstract class SearchService implements IService {
    /**
     * @param searchParameters
     *            HashMap that contains name of attribute for key and value of
     *            the attribute
     * @return An empty list if nothing was found.
     * @throws FenixServiceException
     *             if it can't get persistent support
     */
    public List run(HashMap searchParameters) throws FenixServiceException {
        ISuportePersistente sp;
        List domainList;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            domainList = doSearch(searchParameters, sp);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems with database!", e);
        }

        List infoList = (List) CollectionUtils.collect(domainList, new Transformer() {
            public Object transform(Object input) {
                InfoObject infoObject = cloneDomainObject((IDomainObject) input);
                return infoObject;
            }
        });

        return infoList;
    }

    /**
     * Clones the de IDomainObject to InfoObject
     * 
     * @param object
     * @return
     */
    abstract protected InfoObject cloneDomainObject(IDomainObject object);

    /**
     * Do the search using search using the search parameters.
     * 
     * @param searchParameters
     * @return A list of IDomainObject.
     */
    abstract protected List doSearch(HashMap searchParameters, ISuportePersistente sp)
            throws ExcepcaoPersistencia;
}