/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.SiteView;
import DataBeans.teacher.InfoExternalActivity;
import DataBeans.teacher.InfoSiteCareers;
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

            IPersistentExternalActivity persistentExternalActivity = persistentSuport.getIPersistentExternalActivity();
            List externalActivities = persistentExternalActivity.readAllByTeacher(teacher);

            List result = new ArrayList();
            Iterator iter = externalActivities.iterator();
            while (iter.hasNext())
            {
                IExternalActivity externalActivity = (IExternalActivity) iter.next();
                InfoExternalActivity infoExternalActivity = Cloner.copyIExternalActivity2InfoExternalActivity(externalActivity);
                result.add(infoExternalActivity);
            }

            InfoSiteExternalActivities bodyComponent = new InfoSiteExternalActivities();
            bodyComponent.setInfoExternalActivities(externalActivities);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
