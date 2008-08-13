package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * Do a search on database.
 * 
 * @author jpvl
 */
public abstract class SearchService extends Service {

    /**
     * @param searchParameters
     *            HashMap that contains name of attribute for key and value of
     *            the attribute
     * @return An empty list if nothing was found.
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     *             if it can't get persistent support
     */
    public List run(HashMap searchParameters) {
        final List domainList = doSearch(searchParameters);

        final List infoList = (List) CollectionUtils.collect(domainList, new Transformer() {
            public Object transform(Object input) {
                InfoObject infoObject = newInfoFromDomain((DomainObject) input);
                return infoObject;
            }
        });

        return infoList;
    }

    /**
     * Clones the de DomainObject to InfoObject
     * 
     * @param object
     * @return
     */
    abstract protected InfoObject newInfoFromDomain(DomainObject object);

    /**
     * Do the search using search using the search parameters.
     * 
     * @param searchParameters
     * @return A list of DomainObject.
     */
    abstract protected List doSearch(HashMap searchParameters)
            ;

}
