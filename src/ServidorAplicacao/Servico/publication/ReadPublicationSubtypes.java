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
import Dominio.publication.IPublicationSubtype;
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
public class ReadPublicationSubtypes implements IServico
{
    private static ReadPublicationSubtypes service = new ReadPublicationSubtypes();

    /**
	 *  
	 */
    private ReadPublicationSubtypes()
    {

    }

    public static ReadPublicationSubtypes getService()
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
        return "ReadPublicationSubtypes";
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
            
            List publicationSubtypeList = publicationType.getSubtypes();
            
            List result = (List) CollectionUtils.collect(publicationSubtypeList, new Transformer()
            {
                public Object transform(Object o)
                {
                    IPublicationSubtype publicationSubtype = (IPublicationSubtype) o;
                    return Cloner.copyIPublicationSubtype2InfoPublicationSubtype(publicationSubtype);
                }
            });

            return result;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
