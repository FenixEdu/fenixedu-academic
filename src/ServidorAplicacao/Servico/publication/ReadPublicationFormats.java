/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.publication;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.util.Cloner;
import Dominio.publication.IPublicationFormat;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublicationFormat;


/**
 * @author TJBF	
 * @author PFON
 *  
 */
public class ReadPublicationFormats implements IServico
{
    private static ReadPublicationFormats service = new ReadPublicationFormats();

    /**
	 *  
	 */
    private ReadPublicationFormats()
    {

    }

    public static ReadPublicationFormats getService()
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
        return "ReadPublicationFormats";
    }

    public List run(String user, int publicationTypeId) throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            //IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            //ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            //InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentPublicationFormat persistentPublicationFormat = persistentSuport.getIPersistentPublicationFormat();

            
            List publicationFormatList = persistentPublicationFormat.readAll();

            List result = (List) CollectionUtils.collect(publicationFormatList, new Transformer()
            {
                public Object transform(Object o)
                {
                    IPublicationFormat publicationFormat = (IPublicationFormat) o;
                    return Cloner.copyIPublicationFormat2InfoPublicationFormat(publicationFormat);
                }
            });

            return result;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
