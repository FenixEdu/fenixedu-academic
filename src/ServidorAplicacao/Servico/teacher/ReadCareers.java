/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.SiteView;
import DataBeans.teacher.InfoCareer;
import DataBeans.teacher.InfoSiteCareers;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.teacher.ICareer;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.UserView;
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
    public ReadCareers()
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

    public SiteView run(CareerType careerType, UserView userView) throws FenixServiceException
    {
        try
        {
            System.out.println("inicio do servico");
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(userView.getUtilizador());

            IPersistentCareer persistentCareer = persistentSuport.getIPersistentCareer();
            List careers;

            if (careerType == null)
            {
                careers = persistentCareer.readAllByTeacher(teacher);
            } else
            {
                if (careerType.equals(new CareerType(CareerType.PROFESSIONAL)))
                {
                    careers = persistentCareer.readAllProfessionalCareersByTeacher(teacher);
                } else
                    careers = persistentCareer.readAllTeachingCareerByTeacher(teacher);
            }

            List result = new ArrayList();
            Iterator iter = careers.iterator();
            while (iter.hasNext())
            {
                ICareer career = (ICareer) iter.next();
                InfoCareer infoCareer = Cloner.copyICareer2InfoCareer(career);
                result.add(infoCareer);
            }

            InfoSiteCareers bodyComponent = new InfoSiteCareers();
            bodyComponent.setInfoCareers(result);
            bodyComponent.setCareerType(careerType);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
