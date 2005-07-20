/*
 * Created on Dec 17, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.ICareer;
import net.sourceforge.fenixedu.domain.teacher.IExternalActivity;
import net.sourceforge.fenixedu.domain.teacher.IOldPublication;
import net.sourceforge.fenixedu.domain.teacher.IOrientation;
import net.sourceforge.fenixedu.domain.teacher.IPublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.IServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.IWeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQualification;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;
import net.sourceforge.fenixedu.util.OldPublicationType;
import net.sourceforge.fenixedu.util.OrientationType;
import net.sourceforge.fenixedu.util.PublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadTeachersInformation implements IService {

    public List run(Integer executionDegreeId, Boolean basic, String executionYearString)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

        List professorships = null;
        IExecutionYear executionYear = null;
        if (executionYearString != null && !executionYearString.equals("")) {
            executionYear = persistentExecutionYear.readExecutionYearByName(executionYearString);
        } else {
            executionYear = persistentExecutionYear.readCurrentExecutionYear();
        }
        if (executionDegreeId == null) {
            List<IExecutionDegree> executionDegrees = persistentExecutionDegree
                    .readByExecutionYear(executionYear.getYear());
            List<Integer> degreeCurricularPlanIDs = getDegreeCurricularPlanIDs(executionDegrees);
            Integer executionYearID = (!degreeCurricularPlanIDs.isEmpty()) ? executionDegrees
                    .get(0).getExecutionYear().getIdInternal()
                    : null;

            if (basic == null) {
                professorships = persistentProfessorship.readByDegreeCurricularPlansAndExecutionYear(
                        degreeCurricularPlanIDs, executionYearID);
            } else {
                professorships = persistentProfessorship
                        .readByDegreeCurricularPlansAndExecutionYearAndBasic(degreeCurricularPlanIDs,
                                executionYearID, basic);
            }
        } else {
            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, executionDegreeId);

            if (basic == null) {
                professorships = persistentProfessorship.readByDegreeCurricularPlanAndExecutionYear(
                        executionDegree.getDegreeCurricularPlan().getIdInternal(), executionDegree.getExecutionYear()
                                .getIdInternal());
            } else {
                professorships = persistentProfessorship.readByDegreeCurricularPlanAndBasic(
                        executionDegree.getDegreeCurricularPlan().getIdInternal(), executionDegree.getExecutionYear()
                                .getIdInternal(), basic);
            }
        }

        List teachers = (List) CollectionUtils.collect(professorships, new Transformer() {
            public Object transform(Object o) {
                IProfessorship professorship = (IProfessorship) o;
                return professorship.getTeacher();
            }
        });
        teachers = removeDuplicates(teachers);
        List infoSiteTeachersInformation = new ArrayList();
        Iterator iter = teachers.iterator();
        while (iter.hasNext()) {
            ITeacher teacher = (ITeacher) iter.next();
            infoSiteTeachersInformation.add(getTeacherInformation(teacher.getPerson().getUsername(),
                    executionYear));
        }
        Collections.sort(infoSiteTeachersInformation, new Comparator() {
            public int compare(Object o1, Object o2) {
                InfoSiteTeacherInformation information1 = (InfoSiteTeacherInformation) o1;
                InfoSiteTeacherInformation information2 = (InfoSiteTeacherInformation) o2;
                return information1.getInfoTeacher().getInfoPerson().getNome().compareTo(
                        information2.getInfoTeacher().getInfoPerson().getNome());
            }
        });
        return infoSiteTeachersInformation;

    }

    private List removeDuplicates(List teachers) {
        List result = new ArrayList();
        Iterator iter = teachers.iterator();
        while (iter.hasNext()) {
            ITeacher teacher = (ITeacher) iter.next();
            if (!result.contains(teacher))
                result.add(teacher);
        }

        return result;
    }

    private InfoSiteTeacherInformation getTeacherInformation(String user, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {

        InfoSiteTeacherInformation infoSiteTeacherInformation = new InfoSiteTeacherInformation();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();        
        ITeacher teacher = persistentTeacher.readTeacherByUsername(user);

        InfoTeacher infoTeacher = InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher);
        infoSiteTeacherInformation.setInfoTeacher(infoTeacher);

        infoSiteTeacherInformation.setInfoQualifications(getInfoQualifications(sp, teacher));

        infoSiteTeacherInformation.setInfoProfessionalCareers(getInfoCareers(sp, teacher,
                CareerType.PROFESSIONAL));
        infoSiteTeacherInformation.setInfoTeachingCareers(getInfoCareers(sp, teacher,
                CareerType.TEACHING));

        IPersistentServiceProviderRegime persistentServiceProviderRegime = sp
                .getIPersistentServiceProviderRegime();
        IServiceProviderRegime serviceProviderRegime = persistentServiceProviderRegime
                .readByTeacher(teacher);
        if (serviceProviderRegime == null) {
            InfoServiceProviderRegime infoServiceProviderRegime = new InfoServiceProviderRegime();
            infoServiceProviderRegime.setInfoTeacher(infoTeacher);
            infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
        } else {
            InfoServiceProviderRegime infoServiceProviderRegime = InfoServiceProviderRegime
                    .newInfoFromDomain(serviceProviderRegime);
            infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
        }

        infoSiteTeacherInformation.setInfoExternalActivities(getInfoExternalActivities(sp, teacher));

        infoSiteTeacherInformation.setInfoLecturingExecutionCourses(getInfoLecturingExecutionCourses(
                teacher, sp, executionYear));
        infoSiteTeacherInformation
                .setInfoResponsibleExecutionCourses(getInfoResponsibleExecutionCourses(teacher, sp,
                        executionYear));

        IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
        IWeeklyOcupation weeklyOcupation = persistentWeeklyOcupation.readByTeacher(teacher);
        if (weeklyOcupation == null) {
            InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
            infoWeeklyOcupation.setInfoTeacher(infoTeacher);
            infoWeeklyOcupation.setResearch(new Integer(0));
            infoWeeklyOcupation.setManagement(new Integer(0));
            infoWeeklyOcupation.setLecture(new Integer(0));
            infoWeeklyOcupation.setSupport(new Integer(0));
            infoWeeklyOcupation.setOther(new Integer(0));
            infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
        } else {
            InfoWeeklyOcupation infoWeeklyOcupation = InfoWeeklyOcupation
                    .newInfoFromDomain(weeklyOcupation);
            infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
        }

        infoSiteTeacherInformation.setInfoDegreeOrientation(getInfoOrientation(sp, teacher,
                OrientationType.DEGREE));
        infoSiteTeacherInformation.setInfoMasterOrientation(getInfoOrientation(sp, teacher,
                OrientationType.MASTER));
        infoSiteTeacherInformation.setInfoPhdOrientation(getInfoOrientation(sp, teacher,
                OrientationType.PHD));

        infoSiteTeacherInformation.setInfoArticleChapterPublicationsNumber(getInfoPublicationsNumber(sp,
                teacher, PublicationType.ARTICLES_CHAPTERS));
        infoSiteTeacherInformation.setInfoEditBookPublicationsNumber(getInfoPublicationsNumber(sp,
                teacher, PublicationType.EDITOR_BOOK));
        infoSiteTeacherInformation.setInfoAuthorBookPublicationsNumber(getInfoPublicationsNumber(sp,
                teacher, PublicationType.AUTHOR_BOOK));
        infoSiteTeacherInformation.setInfoMagArticlePublicationsNumber(getInfoPublicationsNumber(sp,
                teacher, PublicationType.MAG_ARTICLE));
        infoSiteTeacherInformation.setInfoComunicationPublicationsNumber(getInfoPublicationsNumber(sp,
                teacher, PublicationType.COMUNICATION));

        infoSiteTeacherInformation.setInfoOldCientificPublications(getInfoOldPublications(sp, teacher,
                OldPublicationType.CIENTIFIC));
        infoSiteTeacherInformation.setInfoOldDidacticPublications(getInfoOldPublications(sp, teacher,
                OldPublicationType.DIDACTIC));

        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();

        infoSiteTeacherInformation.setInfoExecutionPeriod(InfoExecutionPeriod
                .newInfoFromDomain(executionPeriod));
        return infoSiteTeacherInformation;
    }

    private List getInfoResponsibleExecutionCourses(ITeacher teacher, ISuportePersistente sp,
            final IExecutionYear executionYear) throws ExcepcaoPersistencia {
        
        List responsiblesFor = teacher.responsibleFors();

        // filter only the execution courses of the chosen execution year or the
        // current in case none was chosen
        responsiblesFor = (List) CollectionUtils.select(responsiblesFor, new Predicate() {

            public boolean evaluate(Object o) {
                IProfessorship responsibleFor = (IProfessorship) o;
                IExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
                IExecutionYear executionYearElem = executionCourse.getExecutionPeriod()
                        .getExecutionYear();
                return executionYear.equals(executionYearElem);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(responsiblesFor, new Transformer() {
            public Object transform(Object o) {
                IProfessorship responsibleFor = (IProfessorship) o;
                IExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                List infoCurricularCourses = (List) CollectionUtils.collect(curricularCourses,
                        new Transformer() {
                            public Object transform(Object o) {
                                ICurricularCourse curricularCourse = (ICurricularCourse) o;

                                return InfoCurricularCourseWithInfoDegree
                                        .newInfoFromDomain(curricularCourse);
                            }
                        });

                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                        .newInfoFromDomain(executionCourse);
                infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
                return infoExecutionCourse;
            }
        });
        return infoExecutionCourses;
    }

    private List getInfoExternalActivities(ISuportePersistente sp, ITeacher teacher)
            throws ExcepcaoPersistencia {
        IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
        List externalActivities = persistentExternalActivity.readAllByTeacher(teacher);

        List infoExternalActivities = (List) CollectionUtils.collect(externalActivities,
                new Transformer() {
                    public Object transform(Object o) {
                        IExternalActivity externalActivity = (IExternalActivity) o;

                        return InfoExternalActivity.newInfoFromDomain(externalActivity);
                    }
                });
        return infoExternalActivities;
    }

    private List getInfoLecturingExecutionCourses(ITeacher teacher, ISuportePersistente sp,
            final IExecutionYear executionYear) throws ExcepcaoPersistencia {
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        List professorships = persistentProfessorship.readByTeacher(teacher.getIdInternal());

        // filter only the execution courses of the chosen execution year or the
        // current in case none was chosen
        professorships = (List) CollectionUtils.select(professorships, new Predicate() {

            public boolean evaluate(Object o) {
                IProfessorship professorship = (IProfessorship) o;
                IExecutionCourse executionCourse = professorship.getExecutionCourse();
                IExecutionYear executionYearElem = executionCourse.getExecutionPeriod()
                        .getExecutionYear();
                return executionYear.equals(executionYearElem);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(professorships, new Transformer() {
            public Object transform(Object o) {
                IProfessorship professorship = (IProfessorship) o;
                IExecutionCourse executionCourse = professorship.getExecutionCourse();
                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                List infoCurricularCourses = (List) CollectionUtils.collect(curricularCourses,
                        new Transformer() {
                            public Object transform(Object o) {
                                ICurricularCourse curricularCourse = (ICurricularCourse) o;

                                return InfoCurricularCourseWithInfoDegree
                                        .newInfoFromDomain(curricularCourse);
                            }
                        });

                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                        .newInfoFromDomain(executionCourse);
                infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
                return infoExecutionCourse;
            }
        });
        return infoExecutionCourses;
    }

    private List getInfoQualifications(ISuportePersistente sp, ITeacher teacher)
            throws ExcepcaoPersistencia {
        IPersistentQualification persistentQualification = sp.getIPersistentQualification();
        List qualifications = persistentQualification.readQualificationsByPerson(teacher.getPerson());
        List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer() {
            public Object transform(Object o) {
                IQualification qualification = (IQualification) o;

                return InfoQualification.newInfoFromDomain(qualification);
            }
        });
        return infoQualifications;
    }

    private List getInfoCareers(ISuportePersistente sp, ITeacher teacher, CareerType careerType)
            throws ExcepcaoPersistencia {
        IPersistentCareer persistentCareer = sp.getIPersistentCareer();
        List careers = persistentCareer.readAllByTeacherAndCareerType(teacher, careerType);
        List infoCareers = (List) CollectionUtils.collect(careers, new Transformer() {
            public Object transform(Object o) {
                ICareer career = (ICareer) o;
                return InfoCareer.newInfoFromDomain(career);
            }
        });
        return infoCareers;
    }

    private List getInfoOldPublications(ISuportePersistente sp, ITeacher teacher,
            OldPublicationType oldPublicationType) throws ExcepcaoPersistencia {
        IPersistentOldPublication persistentOldPublication = sp.getIPersistentOldPublication();
        List oldCientificPublications = persistentOldPublication.readAllByTeacherAndOldPublicationType(
                teacher, oldPublicationType);

        List infoOldPublications = (List) CollectionUtils.collect(oldCientificPublications,
                new Transformer() {
                    public Object transform(Object o) {
                        IOldPublication oldPublication = (IOldPublication) o;
                        return InfoOldPublication.newInfoFromDomain(oldPublication);
                    }
                });
        return infoOldPublications;
    }

    private InfoOrientation getInfoOrientation(ISuportePersistente sp, ITeacher teacher,
            OrientationType orientationType) throws ExcepcaoPersistencia {
        IPersistentOrientation persistentOrientation = sp.getIPersistentOrientation();
        IOrientation orientation = persistentOrientation.readByTeacherAndOrientationType(teacher,
                orientationType);
        InfoOrientation infoOrientation = null;
        if (orientation != null) {
            infoOrientation = InfoOrientation.newInfoFromDomain(orientation);
        } else {
            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
            infoOrientation = new InfoOrientation();
            infoOrientation.setInfoTeacher(infoTeacher);
            infoOrientation.setOrientationType(orientationType);
        }
        return infoOrientation;
    }

    private InfoPublicationsNumber getInfoPublicationsNumber(ISuportePersistente sp, ITeacher teacher,
            PublicationType publicationType) throws ExcepcaoPersistencia {
        IPersistentPublicationsNumber persistentPublicationsNumber = sp
                .getIPersistentPublicationsNumber();
        IPublicationsNumber publicationsNumber = persistentPublicationsNumber
                .readByTeacherAndPublicationType(teacher, publicationType);
        InfoPublicationsNumber infoPublicationsNumber = null;
        if (publicationsNumber != null) {
            infoPublicationsNumber = InfoPublicationsNumber.newInfoFromDomain(publicationsNumber);
        } else {
            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
            infoPublicationsNumber = new InfoPublicationsNumber();
            infoPublicationsNumber.setInfoTeacher(infoTeacher);
            infoPublicationsNumber.setPublicationType(publicationType);
        }
        return infoPublicationsNumber;
    }

    private List getDegreeCurricularPlanIDs(final List<IExecutionDegree> executionDegrees) {
        final List<Integer> result = new ArrayList<Integer>();
        for (final IExecutionDegree executionDegree : executionDegrees) {
            result.add(executionDegree.getDegreeCurricularPlan().getIdInternal());
        }
        return result;
    }
}