/*
 * Created on 17/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.teacher.InfoOrientation;
import DataBeans.teacher.InfoPublicationsNumber;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoSiteTeacherInformation;
import DataBeans.teacher.InfoWeeklyOcupation;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IProfessorship;
import Dominio.IQualification;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.teacher.ICareer;
import Dominio.teacher.IExternalActivity;
import Dominio.teacher.IOldPublication;
import Dominio.teacher.IOrientation;
import Dominio.teacher.IPublicationsNumber;
import Dominio.teacher.IServiceProviderRegime;
import Dominio.teacher.IWeeklyOcupation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentCareer;
import ServidorPersistente.teacher.IPersistentExternalActivity;
import ServidorPersistente.teacher.IPersistentOldPublication;
import ServidorPersistente.teacher.IPersistentOrientation;
import ServidorPersistente.teacher.IPersistentPublicationsNumber;
import ServidorPersistente.teacher.IPersistentServiceProviderRegime;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;
import Util.CareerType;
import Util.OldPublicationType;
import Util.OrientationType;
import Util.PublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadTeacherInformation implements IServico
{
    public ReadTeacherInformation()
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

            infoSiteTeacherInformation.setInfoQualifications(getInfoQualifications(sp, teacher));

            infoSiteTeacherInformation.setInfoProfessionalCareers(
                getInfoCareers(sp, teacher, CareerType.PROFESSIONAL));
            infoSiteTeacherInformation.setInfoTeachingCareers(
                getInfoCareers(sp, teacher, CareerType.TEACHING));

            IPersistentServiceProviderRegime persistentServiceProviderRegime =
                sp.getIPersistentServiceProviderRegime();
            IServiceProviderRegime serviceProviderRegime =
                persistentServiceProviderRegime.readByTeacher(teacher);
            if (serviceProviderRegime == null)
            {
                InfoServiceProviderRegime infoServiceProviderRegime = new InfoServiceProviderRegime();
                infoServiceProviderRegime.setInfoTeacher(infoTeacher);
                infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
            }
            else
            {
                InfoServiceProviderRegime infoServiceProviderRegime =
                    Cloner.copyIServiceProviderRegime2InfoServiceProviderRegime(serviceProviderRegime);
                infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
            }

            infoSiteTeacherInformation.setInfoExternalActivities(getInfoExternalActivities(sp, teacher));

            infoSiteTeacherInformation.setInfoLecturingExecutionCourses(
                getInfoLecturingExecutionCourses(sp, teacher));
            infoSiteTeacherInformation.setInfoResponsibleExecutionCourses(
                getInfoResponsibleExecutionCourses(sp, teacher));

            IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
            IWeeklyOcupation weeklyOcupation = persistentWeeklyOcupation.readByTeacher(teacher);
            if (weeklyOcupation == null)
            {
                InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
                infoWeeklyOcupation.setInfoTeacher(infoTeacher);
                infoWeeklyOcupation.setResearch(new Integer(0));
                infoWeeklyOcupation.setManagement(new Integer(0));
                infoWeeklyOcupation.setLecture(new Integer(0));
                infoWeeklyOcupation.setSupport(new Integer(0));
                infoWeeklyOcupation.setOther(new Integer(0));
                infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
            }
            else
            {
                InfoWeeklyOcupation infoWeeklyOcupation =
                    Cloner.copyIWeeklyOcupation2InfoWeeklyOcupation(weeklyOcupation);
                infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
            }

            infoSiteTeacherInformation.setInfoDegreeOrientation(
                getInfoOrientation(sp, teacher, OrientationType.DEGREE));
            infoSiteTeacherInformation.setInfoMasterOrientation(
                getInfoOrientation(sp, teacher, OrientationType.MASTER));
            infoSiteTeacherInformation.setInfoPhdOrientation(
                getInfoOrientation(sp, teacher, OrientationType.PHD));

            infoSiteTeacherInformation.setInfoArticleChapterPublicationsNumber(
                getInfoPublicationsNumber(sp, teacher, PublicationType.ARTICLES_CHAPTERS));
            infoSiteTeacherInformation.setInfoEditBookPublicationsNumber(
                getInfoPublicationsNumber(sp, teacher, PublicationType.EDITOR_BOOK));
            infoSiteTeacherInformation.setInfoAuthorBookPublicationsNumber(
                getInfoPublicationsNumber(sp, teacher, PublicationType.AUTHOR_BOOK));
            infoSiteTeacherInformation.setInfoMagArticlePublicationsNumber(
                getInfoPublicationsNumber(sp, teacher, PublicationType.MAG_ARTICLE));
            infoSiteTeacherInformation.setInfoComunicationPublicationsNumber(
                getInfoPublicationsNumber(sp, teacher, PublicationType.COMUNICATION));

            infoSiteTeacherInformation.setInfoOldCientificPublications(
                getInfoOldPublications(sp, teacher, OldPublicationType.CIENTIFIC));
            infoSiteTeacherInformation.setInfoOldDidacticPublications(
                getInfoOldPublications(sp, teacher, OldPublicationType.DIDACTIC));

            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            infoSiteTeacherInformation.setInfoExecutionPeriod(
                (InfoExecutionPeriod) Cloner.get(executionPeriod));

            return new SiteView(infoSiteTeacherInformation);
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

    /**
	 * @param sp
	 * @param teacher
	 * @return
	 */
    private List getInfoResponsibleExecutionCourses(ISuportePersistente sp, ITeacher teacher)
        throws ExcepcaoPersistencia
    {
        IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
        final IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        List responsiblesFor = persistentResponsibleFor.readByTeacher(teacher);

        // filter only the execution courses of the current execution year
        responsiblesFor = (List) CollectionUtils.select(responsiblesFor, new Predicate()
        {
            IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
            public boolean evaluate(Object o)
            {
                IResponsibleFor responsibleFor = (IResponsibleFor) o;
                IExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
                IExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
                return executionYear.equals(this.executionYear);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(responsiblesFor, new Transformer()
        {
            public Object transform(Object o)
            {
                IResponsibleFor responsibleFor = (IResponsibleFor) o;
                IExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                List infoCurricularCourses =
                    (List) CollectionUtils.collect(curricularCourses, new Transformer()
                {
                    public Object transform(Object o)
                    {
                        ICurricularCourse curricularCourse = (ICurricularCourse) o;
                        return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                    }
                });
                InfoExecutionCourse infoExecutionCourse;
                infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
                infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
                return infoExecutionCourse;
            }
        });
        return infoExecutionCourses;
    }

    private List getInfoExternalActivities(ISuportePersistente sp, ITeacher teacher)
        throws ExcepcaoPersistencia
    {
        IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
        List externalActivities = persistentExternalActivity.readAllByTeacher(teacher);

        List infoExternalActivities =
            (List) CollectionUtils.collect(externalActivities, new Transformer()
        {
            public Object transform(Object o)
            {
                IExternalActivity externalActivity = (IExternalActivity) o;
                return Cloner.copyIExternalActivity2InfoExternalActivity(externalActivity);
            }
        });
        return infoExternalActivities;
    }

    private List getInfoLecturingExecutionCourses(ISuportePersistente sp, ITeacher teacher)
        throws ExcepcaoPersistencia
    {
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        final IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        List professorships = persistentProfessorship.readByTeacher(teacher);

        // filter only the execution courses of the current execution year
        professorships = (List) CollectionUtils.select(professorships, new Predicate()
        {
            IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
            public boolean evaluate(Object o)
            {
                IProfessorship professorship = (IProfessorship) o;
                IExecutionCourse executionCourse = professorship.getExecutionCourse();
                IExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
                return executionYear.equals(this.executionYear);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(professorships, new Transformer()
        {
            public Object transform(Object o)
            {
                IProfessorship professorship = (IProfessorship) o;
                IExecutionCourse executionCourse = professorship.getExecutionCourse();
                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                List infoCurricularCourses =
                    (List) CollectionUtils.collect(curricularCourses, new Transformer()
                {
                    public Object transform(Object o)
                    {
                        ICurricularCourse curricularCourse = (ICurricularCourse) o;
                        return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                    }
                });
                InfoExecutionCourse infoExecutionCourse;
                infoExecutionCourse =  (InfoExecutionCourse) Cloner.get(executionCourse);
                infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
                return infoExecutionCourse;
            }
        });
        return infoExecutionCourses;
    }

    private List getInfoQualifications(ISuportePersistente sp, ITeacher teacher)
        throws ExcepcaoPersistencia
    {
        IPersistentQualification persistentQualification = sp.getIPersistentQualification();
        List qualifications = persistentQualification.readQualificationsByPerson(teacher.getPerson());
        List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer()
        {
            public Object transform(Object o)
            {
                IQualification qualification = (IQualification) o;
                return Cloner.copyIQualification2InfoQualification(qualification);
            }
        });
        return infoQualifications;
    }

    private List getInfoCareers(ISuportePersistente sp, ITeacher teacher, CareerType careerType)
        throws ExcepcaoPersistencia
    {
        IPersistentCareer persistentCareer = sp.getIPersistentCareer();
        List careers = persistentCareer.readAllByTeacherAndCareerType(teacher, careerType);
        List infoCareers = (List) CollectionUtils.collect(careers, new Transformer()
        {
            public Object transform(Object o)
            {
                ICareer career = (ICareer) o;
                return Cloner.copyICareer2InfoCareer(career);
            }
        });
        return infoCareers;
    }

    private List getInfoOldPublications(
        ISuportePersistente sp,
        ITeacher teacher,
        OldPublicationType oldPublicationType)
        throws ExcepcaoPersistencia
    {
        IPersistentOldPublication persistentOldPublication = sp.getIPersistentOldPublication();
        List oldCientificPublications =
            persistentOldPublication.readAllByTeacherAndOldPublicationType(teacher, oldPublicationType);

        List infoOldPublications =
            (List) CollectionUtils.collect(oldCientificPublications, new Transformer()
        {
            public Object transform(Object o)
            {
                IOldPublication oldPublication = (IOldPublication) o;
                return Cloner.copyIOldPublication2InfoOldPublication(oldPublication);
            }
        });
        return infoOldPublications;
    }

    private InfoOrientation getInfoOrientation(
        ISuportePersistente sp,
        ITeacher teacher,
        OrientationType orientationType)
        throws ExcepcaoPersistencia
    {
        IPersistentOrientation persistentOrientation = sp.getIPersistentOrientation();
        IOrientation orientation =
            persistentOrientation.readByTeacherAndOrientationType(teacher, orientationType);
        InfoOrientation infoOrientation = null;
        if (orientation != null)
        {
            infoOrientation = Cloner.copyIOrientation2InfoOrientation(orientation);
        }
        else
        {
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            infoOrientation = new InfoOrientation();
            infoOrientation.setInfoTeacher(infoTeacher);
            infoOrientation.setOrientationType(orientationType);
        }
        return infoOrientation;
    }

    private InfoPublicationsNumber getInfoPublicationsNumber(
        ISuportePersistente sp,
        ITeacher teacher,
        PublicationType publicationType)
        throws ExcepcaoPersistencia
    {
        IPersistentPublicationsNumber persistentPublicationsNumber =
            sp.getIPersistentPublicationsNumber();
        IPublicationsNumber publicationsNumber =
            persistentPublicationsNumber.readByTeacherAndPublicationType(teacher, publicationType);
        InfoPublicationsNumber infoPublicationsNumber = null;
        if (publicationsNumber != null)
        {
            infoPublicationsNumber =
                Cloner.copyIPublicationsNumber2InfoPublicationsNumber(publicationsNumber);
        }
        else
        {
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            infoPublicationsNumber = new InfoPublicationsNumber();
            infoPublicationsNumber.setInfoTeacher(infoTeacher);
            infoPublicationsNumber.setPublicationType(publicationType);
        }
        return infoPublicationsNumber;
    }
}