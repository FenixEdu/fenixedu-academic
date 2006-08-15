package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
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
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.OldPublicationType;
import net.sourceforge.fenixedu.util.OrientationType;
import net.sourceforge.fenixedu.util.PublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

public class ReadTeachersInformation extends Service {

    public List run(Integer executionDegreeId, Boolean basic, String executionYearString) throws FenixServiceException, ExcepcaoPersistencia {

        List professorships = null;
        ExecutionYear executionYear = null;
        
        if (executionYearString != null && !executionYearString.equals("")) {
            executionYear = ExecutionYear.readExecutionYearByName(executionYearString);
        } else {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        }
        if (executionDegreeId == null) {
            List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYear(executionYear.getYear());
            List<DegreeCurricularPlan> degreeCurricularPlans = getDegreeCurricularPlans(executionDegrees);
            ExecutionYear executionDegressExecutionYear = (!degreeCurricularPlans.isEmpty()) ? executionDegrees.get(0).getExecutionYear() : null;

            if (basic == null) {
                professorships = Professorship.readByDegreeCurricularPlansAndExecutionYear(degreeCurricularPlans, executionDegressExecutionYear);
            } else {
                professorships = Professorship.readByDegreeCurricularPlansAndExecutionYearAndBasic(degreeCurricularPlans,executionDegressExecutionYear, basic);
            }
        } else {
            ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);

            if (basic == null) {
                professorships = Professorship.readByDegreeCurricularPlanAndExecutionYear(
                        executionDegree.getDegreeCurricularPlan(), executionDegree.getExecutionYear());
            } else {
                professorships = Professorship.readByDegreeCurricularPlanAndExecutionYearAndBasic(
                        executionDegree.getDegreeCurricularPlan(), executionDegree
                                .getExecutionYear(), basic);
            }
        }

        List<Teacher> teachers = (List<Teacher>) CollectionUtils.collect(professorships, new Transformer() {
            public Object transform(Object o) {
                Professorship professorship = (Professorship) o;
                return professorship.getTeacher();
            }
        });
        teachers = removeDuplicates(teachers);
        List<InfoSiteTeacherInformation> infoSiteTeachersInformation = new ArrayList<InfoSiteTeacherInformation>();
        for (Teacher teacher : teachers) {
            infoSiteTeachersInformation.add(getTeacherInformation(teacher.getPerson().getUsername(), executionYear));
        }
        Collections.sort(infoSiteTeachersInformation, new Comparator() {
            public int compare(Object o1, Object o2) {
                InfoSiteTeacherInformation information1 = (InfoSiteTeacherInformation) o1;
                InfoSiteTeacherInformation information2 = (InfoSiteTeacherInformation) o2;
                return information1.getInfoTeacher().getInfoPerson().getNome().compareTo(information2.getInfoTeacher().getInfoPerson().getNome());
            }
        });
        return infoSiteTeachersInformation;

    }

    private List<Teacher> removeDuplicates(List teachers) {
        List<Teacher> result = new ArrayList<Teacher>();
        Iterator iter = teachers.iterator();
        while (iter.hasNext()) {
            Teacher teacher = (Teacher) iter.next();
            if (!result.contains(teacher))
                result.add(teacher);
        }

        return result;
    }

    private InfoSiteTeacherInformation getTeacherInformation(String user, ExecutionYear executionYear) throws ExcepcaoPersistencia {
        InfoSiteTeacherInformation infoSiteTeacherInformation = new InfoSiteTeacherInformation();

        Teacher teacher = Teacher.readTeacherByUsername(user);

        InfoTeacher infoTeacher = InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher);
        infoSiteTeacherInformation.setInfoTeacher(infoTeacher);
        infoSiteTeacherInformation.setInfoQualifications(getInfoQualifications(teacher));
        infoSiteTeacherInformation.setInfoProfessionalCareers(getInfoCareers(teacher, CareerType.PROFESSIONAL));
        infoSiteTeacherInformation.setInfoTeachingCareers(getInfoCareers(teacher,CareerType.TEACHING));

        ServiceProviderRegime serviceProviderRegime = teacher.getServiceProviderRegime();
        if (serviceProviderRegime == null) {
            InfoServiceProviderRegime infoServiceProviderRegime = new InfoServiceProviderRegime();
            infoServiceProviderRegime.setInfoTeacher(infoTeacher);
            infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
        } else {
            InfoServiceProviderRegime infoServiceProviderRegime = InfoServiceProviderRegime.newInfoFromDomain(serviceProviderRegime);
            infoSiteTeacherInformation.setInfoServiceProviderRegime(infoServiceProviderRegime);
        }

        infoSiteTeacherInformation.setInfoExternalActivities(getInfoExternalActivities(teacher));
        infoSiteTeacherInformation.setInfoLecturingExecutionCourses(getInfoLecturingExecutionCourses(teacher, executionYear));
        infoSiteTeacherInformation.setInfoResponsibleExecutionCourses(getInfoRepersistentSupportonsibleExecutionCourses(teacher, executionYear));

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
            infoSiteTeacherInformation.setInfoWeeklyOcupation(infoWeeklyOcupation);
        }

        infoSiteTeacherInformation.setInfoDegreeOrientation(getInfoOrientation(teacher, OrientationType.DEGREE));
        infoSiteTeacherInformation.setInfoMasterOrientation(getInfoOrientation(teacher, OrientationType.MASTER));
        infoSiteTeacherInformation.setInfoPhdOrientation(getInfoOrientation(teacher, OrientationType.PHD));
        infoSiteTeacherInformation.setInfoArticleChapterPublicationsNumber(getInfoPublicationsNumber(teacher, PublicationType.ARTICLES_CHAPTERS));
        infoSiteTeacherInformation.setInfoEditBookPublicationsNumber(getInfoPublicationsNumber(teacher, PublicationType.EDITOR_BOOK));
        infoSiteTeacherInformation.setInfoAuthorBookPublicationsNumber(getInfoPublicationsNumber(teacher, PublicationType.AUTHOR_BOOK));
        infoSiteTeacherInformation.setInfoMagArticlePublicationsNumber(getInfoPublicationsNumber(teacher, PublicationType.MAG_ARTICLE));
        infoSiteTeacherInformation.setInfoComunicationPublicationsNumber(getInfoPublicationsNumber(teacher, PublicationType.COMUNICATION));
        infoSiteTeacherInformation.setInfoOldCientificPublications(getInfoOldPublications(teacher, OldPublicationType.CIENTIFIC));
        infoSiteTeacherInformation.setInfoOldDidacticPublications(getInfoOldPublications(teacher, OldPublicationType.DIDACTIC));

        ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        infoSiteTeacherInformation.setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        return infoSiteTeacherInformation;
    }

    private List getInfoRepersistentSupportonsibleExecutionCourses(Teacher teacher, final ExecutionYear executionYear) throws ExcepcaoPersistencia {
        List repersistentSupportonsiblesFor = teacher.responsibleFors();

        // filter only the execution courses of the chosen execution year or the
        // current in case none was chosen
        repersistentSupportonsiblesFor = (List) CollectionUtils.select(repersistentSupportonsiblesFor,
                new Predicate() {

                    public boolean evaluate(Object o) {
                        Professorship repersistentSupportonsibleFor = (Professorship) o;
                        ExecutionCourse executionCourse = repersistentSupportonsibleFor
                                .getExecutionCourse();
                        ExecutionYear executionYearElem = executionCourse.getExecutionPeriod()
                                .getExecutionYear();
                        return executionYear.equals(executionYearElem);
                    }
                });
        List infoExecutionCourses = (List) CollectionUtils.collect(repersistentSupportonsiblesFor,
                new Transformer() {
                    public Object transform(Object o) {
                        Professorship repersistentSupportonsibleFor = (Professorship) o;
                        ExecutionCourse executionCourse = repersistentSupportonsibleFor
                                .getExecutionCourse();
                        List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                        List infoCurricularCourses = (List) CollectionUtils.collect(curricularCourses,
                                new Transformer() {
                                    public Object transform(Object o) {
                                        CurricularCourse curricularCourse = (CurricularCourse) o;

                                        return InfoCurricularCourse
                                                .newInfoFromDomain(curricularCourse);
                                    }
                                });

                        InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(executionCourse);
                        infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
                        return infoExecutionCourse;
                    }
                });
        return infoExecutionCourses;
    }

    private List getInfoExternalActivities(Teacher teacher) throws ExcepcaoPersistencia {
        List<ExternalActivity> externalActivities = teacher.getAssociatedExternalActivities();

        List infoExternalActivities = (List) CollectionUtils.collect(externalActivities,
                new Transformer() {
                    public Object transform(Object o) {
                        ExternalActivity externalActivity = (ExternalActivity) o;

                        return InfoExternalActivity.newInfoFromDomain(externalActivity);
                    }
                });
        return infoExternalActivities;
    }

    private List getInfoLecturingExecutionCourses(Teacher teacher, final ExecutionYear executionYear) throws ExcepcaoPersistencia {
        List professorships = teacher.getProfessorships();

        // filter only the execution courses of the chosen execution year or the
        // current in case none was chosen
        professorships = (List) CollectionUtils.select(professorships, new Predicate() {

            public boolean evaluate(Object o) {
                Professorship professorship = (Professorship) o;
                ExecutionCourse executionCourse = professorship.getExecutionCourse();
                ExecutionYear executionYearElem = executionCourse.getExecutionPeriod()
                        .getExecutionYear();
                return executionYear.equals(executionYearElem);
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

                                return InfoCurricularCourse
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

    private List getInfoQualifications(Teacher teacher) throws ExcepcaoPersistencia {
        List qualifications = teacher.getPerson().getAssociatedQualifications();
        List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer() {
            public Object transform(Object o) {
                Qualification qualification = (Qualification) o;

                return InfoQualification.newInfoFromDomain(qualification);
            }
        });
        return infoQualifications;
    }

    private List getInfoCareers(Teacher teacher, CareerType careerType) throws ExcepcaoPersistencia {
        List<Career> careers = Career.readAllByTeacherIdAndCareerType(teacher, careerType);
        List infoCareers = (List) CollectionUtils.collect(careers, new Transformer() {
            public Object transform(Object o) {
                Career career = (Career) o;
                return InfoCareer.newInfoFromDomain(career);
            }
        });
        return infoCareers;
    }

    private List getInfoOldPublications(Teacher teacher, OldPublicationType oldPublicationType) throws ExcepcaoPersistencia {
        
        List<OldPublication> oldCientificPublications = teacher.readOldPublicationsByType(oldPublicationType);

        List infoOldPublications = (List) CollectionUtils.collect(oldCientificPublications,
                new Transformer() {
                    public Object transform(Object o) {
                        OldPublication oldPublication = (OldPublication) o;
                        return InfoOldPublication.newInfoFromDomain(oldPublication);
                    }
                });
        return infoOldPublications;
    }

    private InfoOrientation getInfoOrientation(Teacher teacher, OrientationType orientationType) throws ExcepcaoPersistencia {
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

    private InfoPublicationsNumber getInfoPublicationsNumber(Teacher teacher, PublicationType publicationType) throws ExcepcaoPersistencia {
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

    private List<DegreeCurricularPlan> getDegreeCurricularPlans(final List<ExecutionDegree> executionDegrees) {
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            result.add(executionDegree.getDegreeCurricularPlan());
        }
        return result;
    }
    
}
