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
import DataBeans.teacher.InfoSiteExternalActivities;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.teacher.IExternalActivity;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadExternalActivities implements IServico
{
    private static ReadExternalActivities service = new ReadExternalActivities();

    /**
	 *  
	 */
    public ReadExternalActivities()
    {

    }

    public static ReadExternalActivities getService()
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
        return "ReadExternalActivities";
    }

    public SiteView run(String user) throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentExternalActivity persistentExternalActivity =
                persistentSuport.getIPersistentExternalActivity();
            List externalActivities = persistentExternalActivity.readAllByTeacher(teacher);

            List result = (List) CollectionUtils.collect(externalActivities, new Transformer()
            {
                public Object transform(Object o)
                {
                    IExternalActivity externalActivity = (IExternalActivity) o;
                    return Cloner.copyIExternalActivity2InfoExternalActivity(externalActivity);
                }
            });

            InfoSiteExternalActivities bodyComponent = new InfoSiteExternalActivities();
            bodyComponent.setInfoExternalActivities(result);
            bodyComponent.setInfoTeacher(infoTeacher);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
