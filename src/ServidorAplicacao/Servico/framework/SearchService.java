/*
 * Created on Nov 18, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.framework;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import Dominio.IDomainObject;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * Do a search on database.
 * 
 * @author jpvl
 */
public abstract class SearchService implements IService
{
    /**
	 * @param searchParameters
	 *                   HashMap that contains name of attribute for key and value of the attribute
	 * @return An empty list if nothing was found.
	 * @throws FenixServiceException
	 *                    if it can't get persistent support
	 */
    public List run(HashMap searchParameters) throws FenixServiceException
    {
        ISuportePersistente sp;
        List domainList;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            domainList = doSearch(searchParameters, sp);
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems with database!", e);
        }

        List infoList = (List) CollectionUtils.collect(domainList, new Transformer()
        {
            public Object transform(Object input)
            {
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