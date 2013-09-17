package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.teacher.ReadTeacherInformationCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.util.OldPublicationType;
import net.sourceforge.fenixedu.util.OrientationType;
import net.sourceforge.fenixedu.util.PublicationType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class ReadTeacherInformation {

    public SiteView run(String user, String argExecutionYear) {

        InfoSiteTeacherInformation infoSiteTeacherInformation = new InfoSiteTeacherInformation();

        Teacher teacher = Teacher.readTeacherByUsername(user);

        // if no execution year is persistentSupportecified, the info shown
        // should refer to the previous execution year
        ExecutionYear executionYear = null;
        if (argExecutionYear == null || argExecutionYear.equals("")) {
            ExecutionSemester actualExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
            ExecutionSemester previousExecutionPeriod = actualExecutionPeriod.getPreviousExecutionPeriod();
            if (previousExecutionPeriod != null) {
                while (previousExecutionPeriod.getExecutionYear().equals(actualExecutionPeriod.getExecutionYear())) {
                    previousExecutionPeriod = previousExecutionPeriod.getPreviousExecutionPeriod();
                }
            }

            if (previousExecutionPeriod != null) {
                executionYear = previousExecutionPeriod.getExecutionYear();
            } else {
                executionYear = actualExecutionPeriod.getExecutionYear();
            }
        } else {
            executionYear = ExecutionYear.readExecutionYearByName(argExecutionYear);
        }

        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
        infoSiteTeacherInformation.setInfoTeacher(infoTeacher);
        infoSiteTeacherInformation.setInfoQualifications(getInfoQualifications(teacher));
        infoSiteTeacherInformation.setInfoProfessionalCareers(getInfoCareers(teacher, CareerType.PROFESSIONAL));
        infoSiteTeacherInformation.setInfoTeachingCareers(getInfoCareers(teacher, CareerType.TEACHING));

        ServiceProviderRegime serviceProviderRegime = teacher.getServiceProviderRegime();
        if (serviceProviderRegime == null) {
            InfoServiceProviderRegime infoServiceProviderRegime = new InfoServiceProviderRegime();
            infoServiceProviderRegime.setInfoTeacher(infoTeacher);
            infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
        } else {

            InfoServiceProviderRegime infoServiceProviderRegime =
                    InfoServiceProviderRegime.newInfoFromDomain(serviceProviderRegime);
            infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
        }

        infoSiteTeacherInformation.setInfoExternalActivities(getInfoExternalActivities(teacher));
        infoSiteTeacherInformation.setInfoLecturingExecutionCourses(getInfoLecturingExecutionCourses(teacher, executionYear));
        infoSiteTeacherInformation.setInfoResponsibleExecutionCourses(getInfoResponsibleExecutionCourses(teacher, executionYear));

        WeeklyOcupation weeklyOcupation = teacher.getWeeklyOcupation();
        if (weeklyOcupation == null) {
            InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
            infoWeeklyOcupation.setInfoTeacher(infoTeacher);
            infoWeeklyOcupation.setResearch(Integer.valueOf(0));
            infoWeeklyOcupation.setManagement(Integer.valueOf(0));
            infoWeeklyOcupation.setLecture(Integer.valueOf(0));
            infoWeeklyOcupation.setSupport(Integer.valueOf(0));
            infoWeeklyOcupation.setOther(Integer.valueOf(0));
            infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
        } else {
            InfoWeeklyOcupation infoWeeklyOcupation = InfoWeeklyOcupation.newInfoFromDomain(weeklyOcupation);
            infoWeeklyOcupation.setInfoTeacher(infoTeacher);
            infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
        }

        infoSiteTeacherInformation.setInfoDegreeOrientation(getInfoOrientation(teacher, OrientationType.DEGREE));
        infoSiteTeacherInformation.setInfoMasterOrientation(getInfoOrientation(teacher, OrientationType.MASTER));
        infoSiteTeacherInformation.setInfoPhdOrientation(getInfoOrientation(teacher, OrientationType.PHD));
        infoSiteTeacherInformation.setInfoArticleChapterPublicationsNumber(getInfoPublicationsNumber(teacher,
                PublicationType.ARTICLES_CHAPTERS));
        infoSiteTeacherInformation.setInfoEditBookPublicationsNumber(getInfoPublicationsNumber(teacher,
                PublicationType.EDITOR_BOOK));
        infoSiteTeacherInformation.setInfoAuthorBookPublicationsNumber(getInfoPublicationsNumber(teacher,
                PublicationType.AUTHOR_BOOK));
        infoSiteTeacherInformation.setInfoMagArticlePublicationsNumber(getInfoPublicationsNumber(teacher,
                PublicationType.MAG_ARTICLE));
        infoSiteTeacherInformation.setInfoComunicationPublicationsNumber(getInfoPublicationsNumber(teacher,
                PublicationType.COMUNICATION));
        infoSiteTeacherInformation.setInfoOldCientificPublications(getInfoOldPublications(teacher, OldPublicationType.CIENTIFIC));
        infoSiteTeacherInformation.setInfoOldDidacticPublications(getInfoOldPublications(teacher, OldPublicationType.DIDACTIC));
        infoSiteTeacherInformation.setInfoDidaticPublications(getInfoPublications(teacher, PublicationConstants.DIDATIC));
        infoSiteTeacherInformation.setInfoCientificPublications(getInfoPublications(teacher, PublicationConstants.CIENTIFIC));

        // FIXME possible cause of error: this execution period is used for
        // what?
        infoSiteTeacherInformation.setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(executionYear
                .getExecutionPeriods().iterator().next()));
        infoSiteTeacherInformation.setPersonFunctions(getPersonFunctions(teacher, executionYear));

        return new SiteView(infoSiteTeacherInformation);
    }

    private List<PersonFunction> getPersonFunctions(Teacher teacher, ExecutionYear executionYear) {
        Date beginDate = executionYear.getBeginDate();
        Date endDate = executionYear.getEndDate();
        return teacher.getPersonFuntions(YearMonthDay.fromDateFields(beginDate), YearMonthDay.fromDateFields(endDate));
    }

    private List getInfoResponsibleExecutionCourses(Teacher teacher, final ExecutionYear wantedExecutionYear) {
        List responsiblesFor = teacher.responsibleFors();

        // filter only the execution courses of the wanted execution year
        responsiblesFor = (List) CollectionUtils.select(responsiblesFor, new Predicate() {

            @Override
            public boolean evaluate(Object o) {
                Professorship responsibleFor = (Professorship) o;
                ExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
                ExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
                return executionYear.equals(wantedExecutionYear);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(responsiblesFor, new Transformer() {
            @Override
            public Object transform(Object o) {
                return InfoExecutionCourse.newInfoFromDomain(((Professorship) o).getExecutionCourse());
            }
        });
        return infoExecutionCourses;
    }

    private List getInfoExternalActivities(Teacher teacher) {
        Collection<ExternalActivity> externalActivities = teacher.getAssociatedExternalActivities();

        List infoExternalActivities = (List) CollectionUtils.collect(externalActivities, new Transformer() {
            @Override
            public Object transform(Object o) {
                ExternalActivity externalActivity = (ExternalActivity) o;
                return InfoExternalActivity.newInfoFromDomain(externalActivity);
            }
        });
        return infoExternalActivities;
    }

    private List getInfoLecturingExecutionCourses(Teacher teacher, final ExecutionYear wantedExecutionYear) {

        Collection professorships = teacher.getProfessorships();

        // filter only the execution courses of the wanted execution year
        professorships = CollectionUtils.select(professorships, new Predicate() {

            @Override
            public boolean evaluate(Object o) {
                Professorship professorship = (Professorship) o;
                ExecutionCourse executionCourse = professorship.getExecutionCourse();
                ExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
                return executionYear.equals(wantedExecutionYear);
            }
        });
        List infoExecutionCourses = (List) CollectionUtils.collect(professorships, new Transformer() {
            @Override
            public Object transform(Object o) {
                return InfoExecutionCourse.newInfoFromDomain(((Professorship) o).getExecutionCourse());
            }
        });
        return infoExecutionCourses;
    }

    private List getInfoQualifications(Teacher teacher) {
        Collection<Qualification> qualifications = teacher.getPerson().getAssociatedQualifications();
        List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer() {
            @Override
            public Object transform(Object o) {
                Qualification qualification = (Qualification) o;
                return InfoQualification.newInfoFromDomain(qualification);
            }
        });

        Collections.sort(infoQualifications, new Comparator() {

            @Override
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
                    return Integer.valueOf(getYear(strings1)).compareTo(Integer.valueOf(getYear(strings2)));
                }
            }

            private String getYear(String[] strings1) {
                String year1 = null;
                if (strings1.length == 1) {
                    year1 = strings1[0];
                } else if (strings1.length == 2) {
                    year1 = strings1[1];
                }
                return year1;
            }
        });

        return infoQualifications;
    }

    private List getInfoCareers(Teacher teacher, CareerType careerType) {
        final List<InfoCareer> oldestCareers = new ArrayList<InfoCareer>();
        final List<InfoCareer> newestCareers = new ArrayList<InfoCareer>();

        final Collection<Career> careers = teacher.getAssociatedCareers();
        for (final Career career : careers) {
            boolean addCareer = false;
            if (careerType == null
                    || (careerType.equals(CareerType.PROFESSIONAL) && career.getClass().getName()
                            .equals(ProfessionalCareer.class.getName()))) {
                addCareer = true;
            } else if (careerType.equals(CareerType.TEACHING)
                    && career.getClass().getName().equals(TeachingCareer.class.getName())) {
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

    private List getInfoOldPublications(Teacher teacher, OldPublicationType oldPublicationType) {
        return Collections.emptyList();
    }

    private InfoOrientation getInfoOrientation(Teacher teacher, OrientationType orientationType) {
        Orientation orientation = teacher.readOrientationByType(orientationType);
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

    private InfoPublicationsNumber getInfoPublicationsNumber(Teacher teacher, PublicationType publicationType) {
        PublicationsNumber publicationsNumber = teacher.readPublicationsNumberByType(publicationType);
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

    private List getInfoPublications(Teacher teacher, Integer typePublication) {
        return Collections.emptyList();
    }

    // Service Invokers migrated from Berserk

    private static final ReadTeacherInformation serviceInstance = new ReadTeacherInformation();

    @Atomic
    public static SiteView runReadTeacherInformation(String user, String argExecutionYear) throws NotAuthorizedException {
        try {
            TeacherAuthorizationFilter.instance.execute();
            return serviceInstance.run(user, argExecutionYear);
        } catch (NotAuthorizedException ex1) {
            try {
                ReadTeacherInformationCoordinatorAuthorizationFilter.instance.execute(user, argExecutionYear);
                return serviceInstance.run(user, argExecutionYear);
            } catch (NotAuthorizedException ex2) {
                try {
                    GEPAuthorizationFilter.instance.execute();
                    return serviceInstance.run(user, argExecutionYear);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}