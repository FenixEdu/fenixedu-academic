/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.publication;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.publication.IAttribute;
import Dominio.publication.PublicationType;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublicationType;

/**
 * @author TJBF	
 * @author PFON
 *  
 */
public class ReadNonRequiredAttributes implements IServico
{
    private static ReadNonRequiredAttributes service = new ReadNonRequiredAttributes();

    /**
	 *  
	 */
    private ReadNonRequiredAttributes()
    {

    }

    public static ReadNonRequiredAttributes getService()
    {

        return service;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadNonRequiredAttributes";
    }

    public List run(String user, int publicationTypeId) throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentPublicationType persistentPublicationType = persistentSuport.getIPersistentPublicationType();
            PublicationType publicationType = (PublicationType)persistentPublicationType.readByOID(PublicationType.class, new Integer(publicationTypeId));
            
            List nonRequiredAttributeList = publicationType.getNonRequiredAttributes();

            
            List result = (List) CollectionUtils.collect(nonRequiredAttributeList, new Transformer()
            {
                public Object transform(Object o)
                {
                    IAttribute publicationAttribute = (IAttribute) o;
                    return Cloner.copyIAttribute2InfoAttribute(publicationAttribute);
                }
            });

            return result;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
