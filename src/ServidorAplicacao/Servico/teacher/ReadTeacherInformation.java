/*
 * Created on 17/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.person.InfoQualification;
import DataBeans.teacher.InfoExternalActivity;
import DataBeans.teacher.InfoProfessionalCareer;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoSiteTeacherInformation;
import DataBeans.teacher.InfoTeachingCareer;
import DataBeans.teacher.InfoWeeklyOcupation;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.IQualification;
import Dominio.ITeacher;
import Dominio.teacher.IExternalActivity;
import Dominio.teacher.IProfessionalCareer;
import Dominio.teacher.IServiceProviderRegime;
import Dominio.teacher.ITeachingCareer;
import Dominio.teacher.IWeeklyOcupation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentCareer;
import ServidorPersistente.teacher.IPersistentExternalActivity;
import ServidorPersistente.teacher.IPersistentServiceProviderRegime;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;
import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadTeacherInformation implements IServico
{
    private static ReadTeacherInformation service = new ReadTeacherInformation();

    public static ReadTeacherInformation getService()
    {
        return service;
    }

    /**
	 *  
	 */
    private ReadTeacherInformation()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadTeacherInformation";
    }

    /**
	 * Executes the service.
	 */
    public SiteView run(String user) throws FenixServiceException
    {
        try
        {
            InfoSiteTeacherInformation infoSiteTeacherInformation = new InfoSiteTeacherInformation();
            
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            infoSiteTeacherInformation.setInfoTeacher(infoTeacher);

            IPersistentQualification persistentQualification = sp.getIPersistentQualification();
            List qualifications =
                persistentQualification.readQualificationsByPerson(teacher.getPerson());
            List infoQualifications = new ArrayList();
            Iterator iter = qualifications.iterator();
            while (iter.hasNext())
            {
                IQualification qualification = (IQualification) iter.next();
                InfoQualification infoQualification =
                    Cloner.copyIQualification2InfoQualification(qualification);
                infoQualifications.add(infoQualification);
            }
            infoSiteTeacherInformation.setInfoQualifications(infoQualifications);

            IPersistentCareer persistentCareer = sp.getIPersistentCareer();
            List professionalCareers =
                persistentCareer.readAllByTeacherAndCareerType(
                    teacher,
                    CareerType.PROFESSIONAL);
            List infoProfessionalCareers = new ArrayList();
            iter = professionalCareers.iterator();
            while (iter.hasNext())
            {
                IProfessionalCareer professionalCareer = (IProfessionalCareer) iter.next();
                InfoProfessionalCareer infoProfessionalCareer =
                    (InfoProfessionalCareer) Cloner.copyICareer2InfoCareer(professionalCareer);
                infoProfessionalCareers.add(infoProfessionalCareer);
            }
            infoSiteTeacherInformation.setInfoProfessionalCareers(infoProfessionalCareers);

            List teachingCareers =
                persistentCareer.readAllByTeacherAndCareerType(
                    teacher,
                    CareerType.TEACHING);
            List infoTeachingCareers = new ArrayList();
            iter = teachingCareers.iterator();
            while (iter.hasNext())
            {
                ITeachingCareer teachingCareer = (ITeachingCareer) iter.next();
                InfoTeachingCareer infoTeachingCareer =
                    (InfoTeachingCareer) Cloner.copyICareer2InfoCareer(teachingCareer);
                infoTeachingCareers.add(infoTeachingCareer);
            }
            infoSiteTeacherInformation.setInfoTeachingCareers(infoTeachingCareers);

            IPersistentServiceProviderRegime persistentServiceProviderRegime =
                sp.getIPersistentServiceProviderRegime();
            IServiceProviderRegime serviceProviderRegime =
                persistentServiceProviderRegime.readByTeacher(teacher);
            InfoServiceProviderRegime infoServiceProviderRegime =
                Cloner.copyIServiceProviderRegime2InfoServiceProviderRegime(serviceProviderRegime);
            infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);

            IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
            List externalActivities = persistentExternalActivity.readAllByTeacher(teacher);
            List infoExternalActivities = new ArrayList();
            iter = externalActivities.iterator();
            while (iter.hasNext())
            {
                IExternalActivity externalActivity = (IExternalActivity) iter.next();
                InfoExternalActivity infoExternalActivity =
                    (InfoExternalActivity) Cloner.copyIExternalActivity2InfoExternalActivity(externalActivity);
                infoExternalActivities.add(infoExternalActivity);
            }
            infoSiteTeacherInformation.setInfoExternalActivities(infoExternalActivities);

            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            List professorShips = persistentProfessorship.readByTeacher(teacher);
            List infoExecutionCourses = new ArrayList();
            iter = professorShips.iterator();
            while (iter.hasNext())
            {
                IProfessorship professorShip = (IProfessorship) iter.next();
                IDisciplinaExecucao executionCourse = professorShip.getExecutionCourse();
                InfoExecutionCourse infoExecutionCourse =
                    (InfoExecutionCourse) Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
                infoExecutionCourses.add(infoExecutionCourse);
            }
            infoSiteTeacherInformation.setInfoExecutionCourses(infoExecutionCourses);
            
            IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
            IWeeklyOcupation weeklyOcupation = persistentWeeklyOcupation.readByTeacher(teacher);
            InfoWeeklyOcupation infoWeeklyOcupation = Cloner.copyIWeeklyOcupation2InfoWeeklyOcupation(weeklyOcupation);
            infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
            // TODO: faltam os estágios, tfc's, mestrados, doutoramentos
            // TODO: faltam os cargos de gestão
            // TODO: faltam as publicacoes

            return new SiteView(infoSiteTeacherInformation);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}