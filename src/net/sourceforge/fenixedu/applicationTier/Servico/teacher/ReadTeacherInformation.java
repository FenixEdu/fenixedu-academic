/*
 * Created on 17/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;
import net.sourceforge.fenixedu.util.OldPublicationType;
import net.sourceforge.fenixedu.util.OrientationType;
import net.sourceforge.fenixedu.util.PublicationArea;
import net.sourceforge.fenixedu.util.PublicationType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadTeacherInformation implements IService {

    public SiteView run(String user, String argExecutionYear) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoSiteTeacherInformation infoSiteTeacherInformation = new InfoSiteTeacherInformation();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        Teacher teacher = persistentTeacher.readTeacherByUsername(user);

        // if no execution year is specified, the info shown should refer to
        // the previous execution year
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        ExecutionYear executionYear = null;
        if (argExecutionYear == null || argExecutionYear.equals("")) {
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            ExecutionPeriod actualExecutionPeriod = persistentExecutionPeriod
                    .readActualExecutionPeriod();
            ExecutionPeriod previousExecutionPeriod = actualExecutionPeriod
                    .getPreviousExecutionPeriod();
            while (previousExecutionPeriod.getExecutionYear().equals(
                    actualExecutionPeriod.getExecutionYear())) {
                previousExecutionPeriod = previousExecutionPeriod.getPreviousExecutionPeriod();
            }

            executionYear = previousExecutionPeriod.getExecutionYear();
        } else
            executionYear = persistentExecutionYear.readExecutionYearByName(argExecutionYear);

        InfoTeacher infoTeacher = InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher);
        infoSiteTeacherInformation.setInfoTeacher(infoTeacher);

        infoSiteTeacherInformation.setInfoQualifications(getInfoQualifications(sp, teacher));

        infoSiteTeacherInformation.setInfoProfessionalCareers(getInfoCareers(teacher,
                CareerType.PROFESSIONAL));
        infoSiteTeacherInformation.setInfoTeachingCareers(getInfoCareers(teacher, CareerType.TEACHING));

        IPersistentServiceProviderRegime persistentServiceProviderRegime = sp
                .getIPersistentServiceProviderRegime();
        ServiceProviderRegime serviceProviderRegime = persistentServiceProviderRegime
                .readByTeacherId(teacher.getIdInternal());
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

        infoSiteTeacherInformation.setInfoLecturingExecutionCourses(getInfoLecturingExecutionCourses(sp,
                teacher, executionYear));
        infoSiteTeacherInformation
                .setInfoResponsibleExecutionCourses(getInfoResponsibleExecutionCourses(sp, teacher,
                        executionYear));

        IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
        WeeklyOcupation weeklyOcupation = persistentWeeklyOcupation.readByTeacherId(teacher
                .getIdInternal());
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
            infoWeeklyOcupation.setInfoTeacher(infoTeacher);
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

        infoSiteTeacherInformation.setInfoOldCientificPublications(getInfoOldPublications(sp, teacher
                .getIdInternal(), OldPublicationType.CIENTIFIC));
        infoSiteTeacherInformation.setInfoOldDidacticPublications(getInfoOldPublications(sp, teacher
                .getIdInternal(), OldPublicationType.DIDACTIC));

        infoSiteTeacherInformation.setInfoDidaticPublications(getInfoPublications(sp, teacher,
                PublicationConstants.DIDATIC));

        infoSiteTeacherInformation.setInfoCientificPublications(getInfoPublications(sp, teacher,
                PublicationConstants.CIENTIFIC));

        
        // FIXME possible cause of error: this execution period is used for
        // what?
        infoSiteTeacherInformation.setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear
                .newInfoFromDomain(executionYear.getExecutionPeriods().get(0)));

        infoSiteTeacherInformation.setPersonFunctions(getPersonFunctions(teacher, executionYear));
        
        return new SiteView(infoSiteTeacherInformation);
    }

    private List<PersonFunction> getPersonFunctions(Teacher teacher, ExecutionYear executionYear) {       
        return teacher.getPersonFuntions(executionYear.getBeginDate(), executionYear.getEndDate());        
    }

    private List getInfoResponsibleExecutionCourses(ISuportePersistente sp, Teacher teacher,
            final ExecutionYear wantedExecutionYear) throws ExcepcaoPersistencia {

        List responsiblesFor = teacher.responsibleFors();

        // filter only the execution courses of the wanted execution year
        responsiblesFor = (List) CollectionUtils.select(responsiblesFor, new Predicate() {

            public boolean evaluate(Object o) {
                Professorship responsibleFor = (Professorship) o;
                ExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
                ExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
                return executionYear.equals(wantedExecutionYear);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(responsiblesFor, new Transformer() {
            public Object transform(Object o) {
                Professorship responsibleFor = (Professorship) o;
                ExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                List infoCurricularCourses = (List) CollectionUtils.collect(curricularCourses,
                        new Transformer() {
                            public Object transform(Object o) {
                                CurricularCourse curricularCourse = (CurricularCourse) o;

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

    private List getInfoExternalActivities(ISuportePersistente sp, Teacher teacher)
            throws ExcepcaoPersistencia {
        IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
        List externalActivities = persistentExternalActivity.readByTeacherId(teacher.getIdInternal());

        List infoExternalActivities = (List) CollectionUtils.collect(externalActivities,
                new Transformer() {
                    public Object transform(Object o) {
                        ExternalActivity externalActivity = (ExternalActivity) o;
                        return InfoExternalActivity.newInfoFromDomain(externalActivity);
                    }
                });
        return infoExternalActivities;
    }

    private List getInfoLecturingExecutionCourses(ISuportePersistente sp, Teacher teacher,
            final ExecutionYear wantedExecutionYear) throws ExcepcaoPersistencia {
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        List professorships = persistentProfessorship.readByTeacher(teacher.getIdInternal());

        // filter only the execution courses of the wanted execution year
        professorships = (List) CollectionUtils.select(professorships, new Predicate() {

            public boolean evaluate(Object o) {
                Professorship professorship = (Professorship) o;
                ExecutionCourse executionCourse = professorship.getExecutionCourse();
                ExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
                return executionYear.equals(wantedExecutionYear);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(professorships, new Transformer() {
            public Object transform(Object o) {
                Professorship professorship = (Professorship) o;
                ExecutionCourse executionCourse = professorship.getExecutionCourse();
                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                List infoCurricularCourses = (List) CollectionUtils.collect(curricularCourses,
                        new Transformer() {
                            public Object transform(Object o) {
                                CurricularCourse curricularCourse = (CurricularCourse) o;

                                return InfoCurricularCourseWithInfoDegree
                                        .newInfoFromDomain(curricularCourse);
                            }
                        });
                InfoExecutionCourse infoExecutionCourse;

                infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                        .newInfoFromDomain(executionCourse);
                infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
                return infoExecutionCourse;
            }
        });
        return infoExecutionCourses;
    }

    private List getInfoQualifications(ISuportePersistente sp, Teacher teacher)
            throws ExcepcaoPersistencia {
        List<Qualification> qualifications = teacher.getPerson().getAssociatedQualifications();
        List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer() {
            public Object transform(Object o) {
                Qualification qualification = (Qualification) o;
                return InfoQualification.newInfoFromDomain(qualification);
            }
        });
        
        //Collections.sort(infoQualifications, new BeanComparator("year"));
        
        Collections.sort(infoQualifications, new Comparator() {

            public int compare(Object o1, Object o2) {
                InfoQualification infoQualification1 = (InfoQualification) o1;
                InfoQualification infoQualification2 = (InfoQualification) o2;
                if (infoQualification1.getYear() == null && infoQualification2.getYear() == null) {
                    return 0;
                } else if (infoQualification1.getYear() == null) {
                    return -1;
                } else if (infoQualification2.getYear() == null) {
                    return 1;
                } else {
                    String[] strings1 = infoQualification1.getYear().split("/");
                    String[] strings2 = infoQualification2.getYear().split("/");                    
                    
                    if (Integer.valueOf(getYear(strings1)) < Integer.valueOf(getYear(strings2))) {
                        return -1;                     
                    } else if (Integer.valueOf(getYear(strings1)) > Integer.valueOf(getYear(strings2))) {
                        return 1;                        
                    } else if (infoQualification1.getYear().equals(infoQualification2.getYear())) {
                        return 0;
                    }
                }
                return 0;
            }
        
            private String getYear(String[] strings1) {
                String year1 = null;
                if(strings1.length == 1){
                    year1 = strings1[0];
                }
                else if(strings1.length == 2){
                    year1 = strings1[1];
                }
                return year1;
            }
        });

        return infoQualifications;
    }

    private List getInfoCareers(Teacher teacher, CareerType careerType) throws ExcepcaoPersistencia {

        final List<InfoCareer> oldestCareers = new ArrayList();
        final List<InfoCareer> newestCareers = new ArrayList();

        final List<Career> careers = teacher.getAssociatedCareers();
        for (final Career career : careers) {
            boolean addCareer = false;
            if (careerType == null
                    || (careerType.equals(CareerType.PROFESSIONAL) && career.getOjbConcreteClass()
                            .equals(ProfessionalCareer.class.getName()))) {
                addCareer = true;
            } else if (careerType.equals(CareerType.TEACHING)
                    && career.getOjbConcreteClass().equals(TeachingCareer.class.getName())) {
                addCareer = true;
            }
            if (addCareer && career.getBeginYear() == null) {
                oldestCareers.add(InfoCareer.newInfoFromDomain(career));
            } else if (addCareer) {
                newestCareers.add(InfoCareer.newInfoFromDomain(career));
            }
        }
        Collections.sort(newestCareers, new BeanComparator("beginYear"));
        oldestCareers.addAll(newestCareers);

        return oldestCareers;
    }

    private List getInfoOldPublications(ISuportePersistente sp, Integer teacherId,
            OldPublicationType oldPublicationType) throws ExcepcaoPersistencia {
        IPersistentOldPublication persistentOldPublication = sp.getIPersistentOldPublication();
        List oldCientificPublications = persistentOldPublication
                .readAllByTeacherIdAndOldPublicationType(teacherId, oldPublicationType);

        List infoOldPublications = (List) CollectionUtils.collect(oldCientificPublications,
                new Transformer() {
                    public Object transform(Object o) {
                        OldPublication oldPublication = (OldPublication) o;
                        return InfoOldPublication.newInfoFromDomain(oldPublication);
                    }
                });
        return infoOldPublications;
    }

    private InfoOrientation getInfoOrientation(ISuportePersistente sp, Teacher teacher,
            OrientationType orientationType) throws ExcepcaoPersistencia {
        IPersistentOrientation persistentOrientation = sp.getIPersistentOrientation();
        Orientation orientation = persistentOrientation.readByTeacherIdAndOrientationType(teacher
                .getIdInternal(), orientationType);
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

    private InfoPublicationsNumber getInfoPublicationsNumber(ISuportePersistente sp, Teacher teacher,
            PublicationType publicationType) throws ExcepcaoPersistencia {
        IPersistentPublicationsNumber persistentPublicationsNumber = sp
                .getIPersistentPublicationsNumber();
        PublicationsNumber publicationsNumber = persistentPublicationsNumber
                .readByTeacherIdAndPublicationType(teacher.getIdInternal(), publicationType);
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

    // TJBF & PFON

    private List getInfoPublications(ISuportePersistente sp, Teacher teacher, Integer typePublication)
            throws ExcepcaoPersistencia {

        IPersistentPublicationTeacher persistentPublicationTeacher = sp
                .getIPersistentPublicationTeacher();

        PublicationArea publicationArea = null;
        if (PublicationConstants.CIENTIFIC.equals(typePublication))
            publicationArea = PublicationArea.CIENTIFIC;
        else
            publicationArea = PublicationArea.DIDATIC;

        List publicationsTeacher = persistentPublicationTeacher.readByTeacherAndPublicationArea(teacher,
                publicationArea);

        List infoPublications = new ArrayList();
        Iterator iter = publicationsTeacher.iterator();
        while (iter.hasNext()) {
            PublicationTeacher publicationTeacher = (PublicationTeacher) iter.next();
            Publication publication = publicationTeacher.getPublication();

            InfoPublication infoPublication = InfoPublication.newInfoFromDomain(publication);
            infoPublication.setPublicationString(publicationTeacher.getPublication().toString());
            infoPublications.add(infoPublication);
        }

        return infoPublications;
    }

}