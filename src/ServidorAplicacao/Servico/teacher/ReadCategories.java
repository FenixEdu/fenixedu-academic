/*
 * Created on Nov 13, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.util.Cloner;
import Dominio.teacher.ICategory;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentCategory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCategories implements IServico
{
    private static ReadCategories service = new ReadCategories();

    public static ReadCategories getService()
    {
        return service;
    }

    public ReadCategories()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadCategories";
    }

    public List run() throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentCategory persistentCategory = persistentSuport.getIPersistentCategory();
            List categories = persistentCategory.readAll();

            List result = (List) CollectionUtils.collect(categories, new Transformer()
            {
                public Object transform(Object input)
                {
                    ICategory category = (ICategory) input;
                    return Cloner.copyICategory2InfoCategory(category);
                }
            });
            return result;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
