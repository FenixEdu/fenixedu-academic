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
import DataBeans.teacher.InfoSiteCareers;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.teacher.ICareer;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentCareer;
import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCareers implements IServico
{
    private static ReadCareers service = new ReadCareers();

    /**
	 *  
	 */
    private ReadCareers()
    {

    }

    public static ReadCareers getService()
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
        return "ReadCareers";
    }

    public SiteView run(CareerType careerType, String user) throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentCareer persistentCareer = persistentSuport.getIPersistentCareer();
            List careers = persistentCareer.readAllByTeacherAndCareerType(teacher, careerType);

            List result = (List) CollectionUtils.collect(careers, new Transformer()
            {
                public Object transform(Object o)
                {
                    ICareer career = (ICareer) o;
                    return Cloner.copyICareer2InfoCareer(career);
                }
            });

            InfoSiteCareers bodyComponent = new InfoSiteCareers();
            bodyComponent.setInfoCareers(result);
            bodyComponent.setCareerType(careerType);
            bodyComponent.setInfoTeacher(infoTeacher);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
