/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.publication;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.util.Cloner;
import Dominio.publication.IPublicationType;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublicationType;

/**
 * @author TJBF
 * @author PFON
 *  
 */
public class ReadPublicationTypes implements IServico
{
    private static ReadPublicationTypes service = new ReadPublicationTypes();

    /**
	 *  
	 */
    private ReadPublicationTypes()
    {

    }

    public static ReadPublicationTypes getService()
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
        return "ReadPublicationTypes";
    }

    public List run(String user) throws FenixServiceException
    {
        try
        {
        	
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

//            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
//            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
//            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentPublicationType persistentPublicationType = persistentSuport.getIPersistentPublicationType();
            List publicationTypeList = persistentPublicationType.readAll();
            
            List result = (List) CollectionUtils.collect(publicationTypeList, new Transformer()
            {
                public Object transform(Object o)
                {
                    IPublicationType publicationType = (IPublicationType) o;
                    return Cloner.copyIPublicationType2InfoPublicationType(publicationType);
                }
            });

            
            return result;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
