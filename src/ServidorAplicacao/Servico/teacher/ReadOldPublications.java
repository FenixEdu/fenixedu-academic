/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.teacher.InfoSiteOldPublications;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.teacher.IOldPublication;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentOldPublication;
import Util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadOldPublications implements IServico
{
    private static ReadOldPublications service = new ReadOldPublications();

    /**
	 *  
	 */
    private ReadOldPublications()
    {

    }

    public static ReadOldPublications getService()
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
        return "ReadOldPublications";
    }

    public SiteView run(OldPublicationType oldPublicationType, String user) throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentOldPublication persistentOldPublication = persistentSuport.getIPersistentOldPublication();
            List publications = persistentOldPublication.readAllByTeacherAndOldPublicationType(teacher, oldPublicationType);
            
            List result = (List) CollectionUtils.collect(publications, new Transformer()
            {
                public Object transform(Object o)
                {
                    IOldPublication oldPublication = (IOldPublication) o;
                    return Cloner.copyIOldPublication2InfoOldPublication(oldPublication);
                }
            });

            InfoSiteOldPublications bodyComponent = new InfoSiteOldPublications();
            bodyComponent.setInfoOldPublications(result);
            bodyComponent.setOldPublicationType(oldPublicationType);
            bodyComponent.setInfoTeacher(infoTeacher);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
