/*
 * Created on 17/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.teacher.IPublicationsNumber;
import DataBeans.teacher.InfoOrientation;
import DataBeans.teacher.InfoProfessionalCareer;
import DataBeans.teacher.InfoPublicationsNumber;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoSiteTeacherInformation;
import DataBeans.teacher.InfoTeachingCareer;
import DataBeans.teacher.InfoWeeklyOcupation;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.IQualification;
import Dominio.ITeacher;
import Dominio.teacher.IExternalActivity;
import Dominio.teacher.IOrientation;
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
import ServidorPersistente.teacher.IPersistentOrientation;
import ServidorPersistente.teacher.IPersistentPublicationsNumber;
import ServidorPersistente.teacher.IPersistentServiceProviderRegime;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;
import Util.CareerType;
import Util.OrientationType;
import Util.PublicationType;

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
            List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer()
            {
                public Object transform(Object o)
                {
                    IQualification qualification = (IQualification) o;
                    return Cloner.copyIQualification2InfoQualification(qualification);
                }
            });

            infoSiteTeacherInformation.setInfoQualifications(infoQualifications);

            IPersistentCareer persistentCareer = sp.getIPersistentCareer();
            List professionalCareers =
                persistentCareer.readAllByTeacherAndCareerType(teacher, CareerType.PROFESSIONAL);
            List infoProfessionalCareers = new ArrayList();
            Iterator iter = professionalCareers.iterator();
            while (iter.hasNext())
            {
                IProfessionalCareer professionalCareer = (IProfessionalCareer) iter.next();
                InfoProfessionalCareer infoProfessionalCareer =
                    (InfoProfessionalCareer) Cloner.copyICareer2InfoCareer(professionalCareer);
                infoProfessionalCareers.add(infoProfessionalCareer);
            }
            infoSiteTeacherInformation.setInfoProfessionalCareers(infoProfessionalCareers);

            List teachingCareers =
                persistentCareer.readAllByTeacherAndCareerType(teacher, CareerType.TEACHING);
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
            if (serviceProviderRegime == null)
            {
                InfoServiceProviderRegime infoServiceProviderRegime = new InfoServiceProviderRegime();
                infoServiceProviderRegime.setInfoTeacher(infoTeacher);
                infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
            } else
            {
                InfoServiceProviderRegime infoServiceProviderRegime =
                    Cloner.copyIServiceProviderRegime2InfoServiceProviderRegime(serviceProviderRegime);
                infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
            }

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
            infoSiteTeacherInformation.setInfoExternalActivities(infoExternalActivities);

            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            List professorShips = persistentProfessorship.readByTeacher(teacher);
            List infoExecutionCourses = (List) CollectionUtils.collect(professorShips, new Transformer()
            {
                public Object transform(Object o)
                {
                    IProfessorship professorship = (IProfessorship) o;
                    IDisciplinaExecucao executionCourse = professorship.getExecutionCourse();
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
                    InfoExecutionCourse infoExecutionCourse =
                        Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
                    infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
                    return infoExecutionCourse;
                }
            });
            infoSiteTeacherInformation.setInfoExecutionCourses(infoExecutionCourses);

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
            } else
            {
                InfoWeeklyOcupation infoWeeklyOcupation =
                    Cloner.copyIWeeklyOcupation2InfoWeeklyOcupation(weeklyOcupation);
                infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
            }

            IPersistentOrientation persistentOrientation = sp.getIPersistentOrientation();
            IOrientation degreeOrientation =
                persistentOrientation.readByTeacherAndOrientationType(teacher, OrientationType.DEGREE);
            if (degreeOrientation != null)
            {
                infoSiteTeacherInformation.setInfoDegreeOrientation(
                    Cloner.copyIOrientation2InfoOrientation(degreeOrientation));
            } else
            {
                InfoOrientation infoOrientation = new InfoOrientation();
                infoOrientation.setInfoTeacher(infoTeacher);
                infoOrientation.setNumberOfStudents(new Integer(0));
                infoOrientation.setDescription(new String());
                infoOrientation.setOrientationType(OrientationType.DEGREE);
                infoSiteTeacherInformation.setInfoDegreeOrientation(infoOrientation);
            }

            IOrientation masterOrientation =
                persistentOrientation.readByTeacherAndOrientationType(teacher, OrientationType.MASTER);
            if (masterOrientation != null)
            {
                infoSiteTeacherInformation.setInfoMasterOrientation(
                    Cloner.copyIOrientation2InfoOrientation(masterOrientation));
            } else
            {
                InfoOrientation infoOrientation = new InfoOrientation();
                infoOrientation.setInfoTeacher(infoTeacher);
                infoOrientation.setNumberOfStudents(new Integer(0));
                infoOrientation.setDescription(new String());
                infoOrientation.setOrientationType(OrientationType.MASTER);
                infoSiteTeacherInformation.setInfoMasterOrientation(infoOrientation);
            }

            IOrientation phdOrientation =
                persistentOrientation.readByTeacherAndOrientationType(teacher, OrientationType.PHD);
            if (phdOrientation != null)
            {
                infoSiteTeacherInformation.setInfoPhdOrientation(
                    Cloner.copyIOrientation2InfoOrientation(phdOrientation));
            } else
            {
                InfoOrientation infoOrientation = new InfoOrientation();
                infoOrientation.setInfoTeacher(infoTeacher);
                infoOrientation.setNumberOfStudents(new Integer(0));
                infoOrientation.setDescription(new String());
                infoOrientation.setOrientationType(OrientationType.PHD);
                infoSiteTeacherInformation.setInfoPhdOrientation(infoOrientation);
            }

            IPersistentPublicationsNumber persistentPublicationsNumber =
                sp.getIPersistentPublicationsNumber();
            IPublicationsNumber comunicationPublicationsNumber =
                persistentPublicationsNumber.readByTeacherAndPublicationType(
                    teacher,
                    PublicationType.COMUNICATION);
            if (comunicationPublicationsNumber != null)
            {
                infoSiteTeacherInformation.setInfoComunicationPublicationsNumber(
                    Cloner.copyIPublicationsNumber2InfoPublicationsNumber(
                        comunicationPublicationsNumber));
            } else
            {
                InfoPublicationsNumber infoPublicationsNumber = new InfoPublicationsNumber();
                infoPublicationsNumber.setInfoTeacher(infoTeacher);
                infoPublicationsNumber.setNational(new Integer(0));
                infoPublicationsNumber.setInternational(new Integer(0));
                infoPublicationsNumber.setPublicationType(PublicationType.COMUNICATION);
                infoSiteTeacherInformation.setInfoComunicationPublicationsNumber(infoPublicationsNumber);
            }

            IPublicationsNumber magArticlePublicationsNumber =
                persistentPublicationsNumber.readByTeacherAndPublicationType(
                    teacher,
                    PublicationType.MAG_ARTICLE);
            if (magArticlePublicationsNumber != null)
            {
                infoSiteTeacherInformation.setInfoMagArticlePublicationsNumber(
                    Cloner.copyIPublicationsNumber2InfoPublicationsNumber(magArticlePublicationsNumber));
            } else
            {
                InfoPublicationsNumber infoPublicationsNumber = new InfoPublicationsNumber();
                infoPublicationsNumber.setInfoTeacher(infoTeacher);
                infoPublicationsNumber.setNational(new Integer(0));
                infoPublicationsNumber.setInternational(new Integer(0));
                infoPublicationsNumber.setPublicationType(PublicationType.MAG_ARTICLE);
                infoSiteTeacherInformation.setInfoMagArticlePublicationsNumber(infoPublicationsNumber);
            }

            IPublicationsNumber authorBookPublicationsNumber =
                persistentPublicationsNumber.readByTeacherAndPublicationType(
                    teacher,
                    PublicationType.AUTHOR_BOOK);
            if (authorBookPublicationsNumber != null)
            {
                infoSiteTeacherInformation.setInfoAuthorBookPublicationsNumber(
                    Cloner.copyIPublicationsNumber2InfoPublicationsNumber(authorBookPublicationsNumber));
            } else
            {
                InfoPublicationsNumber infoPublicationsNumber = new InfoPublicationsNumber();
                infoPublicationsNumber.setInfoTeacher(infoTeacher);
                infoPublicationsNumber.setNational(new Integer(0));
                infoPublicationsNumber.setInternational(new Integer(0));
                infoPublicationsNumber.setPublicationType(PublicationType.AUTHOR_BOOK);
                infoSiteTeacherInformation.setInfoAuthorBookPublicationsNumber(infoPublicationsNumber);
            }

            IPublicationsNumber editorBookPublicationsNumber =
                persistentPublicationsNumber.readByTeacherAndPublicationType(
                    teacher,
                    PublicationType.EDITOR_BOOK);
            if (editorBookPublicationsNumber != null)
            {
                infoSiteTeacherInformation.setInfoEditBookPublicationsNumber(
                    Cloner.copyIPublicationsNumber2InfoPublicationsNumber(editorBookPublicationsNumber));
            } else
            {
                InfoPublicationsNumber infoPublicationsNumber = new InfoPublicationsNumber();
                infoPublicationsNumber.setInfoTeacher(infoTeacher);
                infoPublicationsNumber.setNational(new Integer(0));
                infoPublicationsNumber.setInternational(new Integer(0));
                infoPublicationsNumber.setPublicationType(PublicationType.EDITOR_BOOK);
                infoSiteTeacherInformation.setInfoEditBookPublicationsNumber(infoPublicationsNumber);
            }

            IPublicationsNumber articlesChaptersPublicationsNumber =
                persistentPublicationsNumber.readByTeacherAndPublicationType(
                    teacher,
                    PublicationType.ARTICLES_CHAPTERS);
            if (articlesChaptersPublicationsNumber != null)
            {
                infoSiteTeacherInformation.setInfoArticleChapterPublicationsNumber(
                    Cloner.copyIPublicationsNumber2InfoPublicationsNumber(
                        articlesChaptersPublicationsNumber));
            } else
            {
                InfoPublicationsNumber infoPublicationsNumber = new InfoPublicationsNumber();
                infoPublicationsNumber.setInfoTeacher(infoTeacher);
                infoPublicationsNumber.setNational(new Integer(0));
                infoPublicationsNumber.setInternational(new Integer(0));
                infoPublicationsNumber.setPublicationType(PublicationType.ARTICLES_CHAPTERS);
                infoSiteTeacherInformation.setInfoArticleChapterPublicationsNumber(
                    infoPublicationsNumber);
            }

            // TODO: faltam os cargos de gestão
            return new SiteView(infoSiteTeacherInformation);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}