package net.sourceforge.fenixedu.dataTransferObject.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsSet;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularSemester;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamStudentRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProperties;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesis;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPrice;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoScientificArea;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteIST;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentKind;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoUniversity;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoWorkLocation;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoServiceExemptionCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.gaugingTests.physics.InfoGaugingTestResult;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoric;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoStudentCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoProfessionalCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICampus;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.IPaymentPhase;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPrice;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IScientificArea;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ISiteIST;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentKind;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.IUniversity;
import net.sourceforge.fenixedu.domain.IWebSite;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.IWebSiteSection;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.domain.credits.IManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.IOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.credits.IServiceExemptionCreditLine;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.IGaugingTestResult;
import net.sourceforge.fenixedu.domain.gesdis.ICourseHistoric;
import net.sourceforge.fenixedu.domain.gesdis.IStudentCourseReport;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.ICareer;
import net.sourceforge.fenixedu.domain.teacher.ICategory;
import net.sourceforge.fenixedu.domain.teacher.IExternalActivity;
import net.sourceforge.fenixedu.domain.teacher.IOldPublication;
import net.sourceforge.fenixedu.domain.teacher.IProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.ITeachingCareer;
import net.sourceforge.fenixedu.domain.teacher.workTime.ITeacherInstitutionWorkTime;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.core.proxy.ProxyHelper;

/**
 * @author jpvl
 * 
 */
public abstract class Cloner {

    public static InfoObject get(IDomainObject domainObject) {
        if (domainObject == null) {
            return null;
        }

        InfoObject infoObject = null;
        Class[] parameters = getParameters(domainObject.getClass());
        Object[] args = { domainObject };
        try {
            Method method = Cloner.class.getDeclaredMethod("copy", parameters);
            if (method != null) {
                infoObject = (InfoObject) method.invoke(Cloner.class, args);
            }
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return infoObject;
    }

    private static Class[] getParameters(Class domainClass) {
        Class[] interfaces = domainClass.getInterfaces();
        // Start with the most frequent case
        if (interfaces.length == 1) {
            return interfaces;
        } else if (interfaces.length == 0) {
            return getParameters(domainClass.getSuperclass());
        } else {

            Class[] parameters = null;
            for (int i = 0; i < interfaces.length; i++) {

                if (Arrays.asList(interfaces[i].getInterfaces()).contains(IDomainObject.class)
                        || ((interfaces[i].getSuperclass() != null) && (interfaces[i].getSuperclass()
                                .getName().equals(IDomainObject.class.getName())))) {
                    parameters = new Class[1];
                    parameters[0] = interfaces[i];
                }
            }
            return parameters;
        }
    }

    // DO NOT DELETE - this is used locally through introspection!!!
    private static InfoExecutionCourse copy(IExecutionCourse executionCourse) {
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionCourse
                .getExecutionPeriod());

        if (infoExecutionPeriod != null) {
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
        }

        copyObjectProperties(infoExecutionCourse, executionCourse);

        infoExecutionCourse.setNumberOfAttendingStudents(new Integer(executionCourse
                .getAttendingStudents() == null ? 0 : executionCourse.getAttendingStudents().size()));
        return infoExecutionCourse;
    }

    /**
     * @param room
     * @return ILesson
     */
    public static InfoRoom copyRoom2InfoRoom(IRoom room) {

        if (room != null) {
            InfoRoom infoRoom = new InfoRoom();

            copyObjectProperties(infoRoom, room);
            return infoRoom;
        }
        return null;
    }

    /**
     * Method copyInfoLesson2Lesson.
     * 
     * @param lessonExample
     * @return ILesson
     */
    public static InfoLesson copyILesson2InfoLesson(ILesson lesson) {
        if (lesson == null) {
            return null;
        }

        try {
            // Temporary bug fix.
            // Try to materialize the proxy... if it fails return null.
            // Should look into this further... but I don't have the time
            // at the moment. This situation occores constantly when
            // viewing Class 05102 of the execution period 80.
            // "\n\n");
            lesson.getIdInternal();
        } catch (Throwable nex) {

            return null;
        }

        InfoLesson infoLesson = new InfoLesson();
        /*
         * InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse)
         * Cloner.get(lesson .getDisciplinaExecucao());
         */
        InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(lesson.getSala());
        InfoRoomOccupation infoRoomOccupation = Cloner.copyIRoomOccupation2InfoRoomOccupation(lesson
                .getRoomOccupation());

        copyObjectProperties(infoLesson, lesson);

        infoLesson.setInfoSala(infoRoom);
        infoLesson.setInfoRoomOccupation(infoRoomOccupation);

        return infoLesson;
    }

    /**
     * Method copyInfoShift2Shift.
     * 
     * @param infoShift
     * @return IShift
     */
    public static InfoClass copyClass2InfoClass(ISchoolClass classD) {
        InfoClass infoClass = new InfoClass();
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(classD
                .getExecutionDegree());
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(classD
                .getExecutionPeriod());

        copyObjectProperties(infoClass, classD);

        infoClass.setInfoExecutionDegree(infoExecutionDegree);
        infoClass.setInfoExecutionPeriod(infoExecutionPeriod);
        return infoClass;
    }

    /**
     * Method copyIExecutionPeriod2InfoExecutionPeriod.
     * 
     * @param iExecutionPeriod
     * @return InfoExecutionPeriod
     */

    // DO NOT DELETE - this is used locally through introspection!!!
    private static InfoExecutionPeriod copy(IExecutionPeriod executionPeriod) {
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) Cloner.get(executionPeriod
                .getExecutionYear());

        copyObjectProperties(infoExecutionPeriod, executionPeriod);

        infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
        return infoExecutionPeriod;
    }

    private static void copyObjectProperties(Object destination, Object source) {
        if (source != null)
            try {

                CopyUtils.copyProperties(destination, source);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
    }

    /**
     * @param executionDegree
     * @return InfoExecutionDegree
     */

    // DO NOT DELETE - this is used locally through introspection!!!
    protected static InfoExecutionDegree copy(IExecutionDegree executionDegree) {

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = Cloner
                .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(executionDegree
                        .getDegreeCurricularPlan());
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) Cloner.get(executionDegree
                .getExecutionYear());
        try {
            BeanUtils.copyProperties(infoExecutionDegree, executionDegree);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoExecutionDegree.setTemporaryExamMap(executionDegree.getTemporaryExamMap());

        // added by Tânia Pousão
        InfoCampus infoCampus = Cloner.copyICampus2InfoCampus(executionDegree.getCampus());
        infoExecutionDegree.setInfoCampus(infoCampus);

        return infoExecutionDegree;

    }

    /**
     * Method copyInfoExecutionYear2IExecutionYear.
     * 
     * @param infoExecutionYear
     * @return IExecutionYear
     */

    // DO NOT DELETE - this is used locally through introspection!!!
    protected static InfoExecutionYear copy(IExecutionYear executionYear) {
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        copyObjectProperties(infoExecutionYear, executionYear);
        return infoExecutionYear;
    }

    /**
     * Method copyIDegree2InfoDegree.
     * 
     * @param iCurso
     * @return InfoDegree
     */
    public static InfoDegree copyIDegree2InfoDegree(IDegree degree) {
        InfoDegree infoDegree = new InfoDegree();
        copyObjectProperties(infoDegree, degree);
        return infoDegree;
    }

    /**
     * Method copyIStudent2InfoStudent.
     * 
     * @param elem
     * @return Object
     */
    public static InfoStudent copyIStudent2InfoStudent(IStudent student) {
        InfoStudent infoStudent = new InfoStudent();
        copyObjectProperties(infoStudent, student);

        infoStudent.setInfoPerson(Cloner.copyIPerson2InfoPerson(student.getPerson()));
        infoStudent
                .setInfoStudentKind(Cloner.copyIStudentKind2InfoStudentKind(student.getStudentKind()));

        // by gedl at august the 5th, 2003
        infoStudent.setIdInternal(student.getIdInternal());
        return infoStudent;
    }

    /**
     * Method copyIPerson2InfoPerson.
     * 
     * @param Person
     * @return InfoPerson
     */
    public static InfoPerson copyIPerson2InfoPerson(IPerson person) {

        try {
            // Temporary bug fix.
            // Try to materialize the proxy... if it fails return null.
            // Should look into this further... but I don't have the time
            // at the moment. This situation occores constantly when
            // viewing Class 05102 of the execution period 80.
            person.getIdInternal();
        } catch (Throwable nex) {
            return null;
        }

        InfoPerson infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPerson();
            InfoCountry infoCountry = null;
            if (person.getPais() != null) {
                infoCountry = Cloner.copyICountry2InfoCountry(person.getPais());
            }

            copyObjectProperties(infoPerson, person);
            // infoPerson.copyToDomain(infoPerson,person);
            infoPerson.setInfoPais(infoCountry);

            if (person.getAdvisories() != null) {
                infoPerson.setInfoAdvisories((List) CollectionUtils.collect(person.getAdvisories(),
                        new Transformer() {
                            public Object transform(Object arg0) {
                                return copyIAdvisory2InfoAdvisory((IAdvisory) arg0);
                            }
                        }));
            } else {
                infoPerson.setInfoAdvisories(new ArrayList());
            }
        }
        return infoPerson;
    }

    /**
     * @param advisory
     * @return
     */
    public static InfoAdvisory copyIAdvisory2InfoAdvisory(IAdvisory advisory) {
        InfoAdvisory infoAdvisory = new InfoAdvisory();
        copyObjectProperties(infoAdvisory, advisory);
        return infoAdvisory;
    }

    public static InfoCandidateSituation copyICandidateSituation2InfoCandidateSituation(
            ICandidateSituation candidateSituation) {
        InfoCandidateSituation infoCandidateSituation = new InfoCandidateSituation();
        copyObjectProperties(infoCandidateSituation, candidateSituation);
        return infoCandidateSituation;
    }

    /**
     * Method copyIMasterDegreeCandidate2InfoMasterDegreCandidate
     * 
     * @param masterDegreeCandidate
     * @return InfoMasterDegreeCandidate
     */
    public static InfoMasterDegreeCandidate copyIMasterDegreeCandidate2InfoMasterDegreCandidate(
            IMasterDegreeCandidate masterDegreeCandidate) {
        if (masterDegreeCandidate == null)
            throw new IllegalArgumentException("ERROR -----------------------------");

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(masterDegreeCandidate
                .getExecutionDegree());
        infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

        InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(masterDegreeCandidate.getPerson());
        infoMasterDegreeCandidate.setInfoPerson(infoPerson);
        if (masterDegreeCandidate.getActiveCandidateSituation() != null) {
            InfoCandidateSituation infoCandidateSituation = Cloner
                    .copyICandidateSituation2InfoCandidateSituation(masterDegreeCandidate
                            .getActiveCandidateSituation());
            infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }

        copyObjectProperties(infoMasterDegreeCandidate, masterDegreeCandidate);
        return infoMasterDegreeCandidate;
    }

    /**
     * Method copyICountry2InfoCountry
     * 
     * @param country
     * @return
     */
    public static InfoCountry copyICountry2InfoCountry(ICountry country) {
        InfoCountry infoCountry = new InfoCountry();
        copyObjectProperties(infoCountry, country);
        return infoCountry;
    }

    /**
     * @param role
     * @return InfoRole
     */
    public static InfoRole copyIRole2InfoRole(IRole role) {
        InfoRole infoRole = new InfoRole();
        copyObjectProperties(infoRole, role);
        return infoRole;
    }

    public static InfoBibliographicReference copyIBibliographicReference2InfoBibliographicReference(
            IBibliographicReference bibliographicReference) {
        InfoBibliographicReference infoBibliographicReference = new InfoBibliographicReference();
        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner
                .get(bibliographicReference.getExecutionCourse());
        copyObjectProperties(infoBibliographicReference, bibliographicReference);
        infoBibliographicReference.setInfoExecutionCourse(infoExecutionCourse);
        return infoBibliographicReference;
    }

    /**
     * Method copyISite2InfoSite.
     * 
     * @param site
     * @return InfoSite
     */

    public static InfoSite copyISite2InfoSite(ISite site) {
        InfoSite infoSite = new InfoSite();

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(site
                .getExecutionCourse());

        copyObjectProperties(infoSite, site);
        infoSite.setInfoExecutionCourse(infoExecutionCourse);

        return infoSite;
    }

    /**
     * Method copyISection2InfoSection.
     * 
     * @param section
     * @return InfoSection
     */

    public static InfoSection copyISection2InfoSection(ISection section) {

        InfoSection infoSection = new InfoSection();

        InfoSection fatherInfoSection = null;

        InfoSite infoSite = Cloner.copyISite2InfoSite(section.getSite());

        ISection superiorSection = section.getSuperiorSection();

        if (superiorSection != null) {
            fatherInfoSection = Cloner.copyISection2InfoSection(superiorSection);
        }

        copyObjectProperties(infoSection, section);

        infoSection.setSuperiorInfoSection(fatherInfoSection);
        infoSection.setInfoSite(infoSite);
        infoSection.setIdInternal(section.getIdInternal());

        return infoSection;

    }

    /**
     * Method copyIItem2InfoItem.
     * 
     * @param item
     * @return InfoItem
     */

    public static InfoItem copyIItem2InfoItem(IItem item) {

        InfoItem infoItem = new InfoItem();
        InfoSection infoSection = Cloner.copyISection2InfoSection(item.getSection());

        copyObjectProperties(infoItem, item);

        infoItem.setInfoSection(infoSection);

        return infoItem;

    }

    /**
     * Method copyIAnnouncement2InfoAnnouncement.
     * 
     * @param announcement
     * @return InfoAnnouncement
     */
    public static InfoAnnouncement copyIAnnouncement2InfoAnnouncement(IAnnouncement announcement) {
        InfoAnnouncement infoAnnouncement = new InfoAnnouncement();

        InfoSite infoSite = Cloner.copyISite2InfoSite(announcement.getSite());

        copyObjectProperties(infoAnnouncement, announcement);
        infoAnnouncement.setInfoSite(infoSite);

        return infoAnnouncement;
    }

    /**
     * @param curriculum
     * @return InfoCurriculum
     */
    public static InfoCurriculum copyICurriculum2InfoCurriculum(ICurriculum curriculum) {
        InfoCurriculum infoCurriculum = new InfoCurriculum();

        InfoCurricularCourse infoCurricularCourse = Cloner
                .copyCurricularCourse2InfoCurricularCourse(curriculum.getCurricularCourse());
        InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(curriculum.getPersonWhoAltered());

        copyObjectProperties(infoCurriculum, curriculum);
        infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);
        infoCurriculum.setInfoPersonWhoAltered(infoPerson);

        return infoCurriculum;
    }

    public static InfoUniversity copyIUniversity2InfoUniversity(IUniversity university) {
        InfoUniversity infoUniversity = new InfoUniversity();
        copyObjectProperties(infoUniversity, university);
        return infoUniversity;
    }

    /**
     * @param exam
     * @return InfoExam
     */
    public static InfoExam copyIExam2InfoExam(IExam exam) {
        InfoExam infoExam = new InfoExam();

        copyObjectProperties(infoExam, exam);
        List infoRooms = new ArrayList();
        List infoCurricularCourseScope = new ArrayList();
        List infoRoomOccupation = new ArrayList();
        List infoExecutionCourse = new ArrayList();

        List associatedRooms = exam.getAssociatedRooms();
        if (exam != null && associatedRooms != null && associatedRooms.size() > 0) {
            for (int i = 0; i < associatedRooms.size(); i++) {
                infoRooms.add(copyRoom2InfoRoom((IRoom) associatedRooms.get(i)));
            }
        }

        // associate curricularCourseScopes
        if (exam != null && exam.getAssociatedCurricularCourseScope() != null
                && exam.getAssociatedCurricularCourseScope().size() > 0) {
            for (int i = 0; i < exam.getAssociatedCurricularCourseScope().size(); i++) {
                ICurricularCourseScope scope = exam.getAssociatedCurricularCourseScope().get(i);
                infoCurricularCourseScope
                        .add(copyICurricularCourseScope2InfoCurricularCourseScope(scope));
            }
        }
        infoExam.setAssociatedCurricularCourseScope(infoCurricularCourseScope);

        // associate room occupation (new version)
        if (exam != null && exam.getAssociatedRoomOccupation() != null
                && exam.getAssociatedRoomOccupation().size() > 0) {

            for (int i = 0; i < exam.getAssociatedRoomOccupation().size(); i++) {
                infoRoomOccupation.add(copyIRoomOccupation2InfoRoomOccupation(exam
                        .getAssociatedRoomOccupation().get(i)));
            }
        }
        infoExam.setAssociatedRoomOccupation(infoRoomOccupation);

        // associate execution course (new version)
        if (exam != null && exam.getAssociatedExecutionCourses() != null
                && exam.getAssociatedExecutionCourses().size() > 0) {

            for (int i = 0; i < exam.getAssociatedExecutionCourses().size(); i++) {
                infoExecutionCourse.add(get(exam.getAssociatedExecutionCourses().get(i)));
            }
        }
        infoExam.setAssociatedExecutionCourse(infoExecutionCourse);

        return infoExam;
    }

    /**
     * @param IRoomOccupation
     * @return InfoRoomOccupation
     */
    public static InfoRoomOccupation copyIRoomOccupation2InfoRoomOccupation(
            IRoomOccupation roomOccupation) {

        InfoRoomOccupation infoRoomOccupation = new InfoRoomOccupation();

        InfoPeriod infoPeriod = Cloner.copyIPeriod2InfoPeriod(roomOccupation.getPeriod());
        InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(roomOccupation.getRoom());

        copyObjectProperties(infoRoomOccupation, roomOccupation);

        infoRoomOccupation.setInfoPeriod(infoPeriod);
        infoRoomOccupation.setInfoRoom(infoRoom);

        return infoRoomOccupation;
    }

    /**
     * @param IPeriod
     * @return InfoPeriod
     */
    public static InfoPeriod copyIPeriod2InfoPeriod(IPeriod period) {
        InfoPeriod infoPeriod = new InfoPeriod();

        copyObjectProperties(infoPeriod, period);

        return infoPeriod;
    }

    /**
     * @param teacher
     * @return
     */
    public static InfoTeacher copyITeacher2InfoTeacher(ITeacher teacher) {
        InfoTeacher infoTeacher = null;
        InfoPerson infoPerson = null;
        InfoCategory infoCategory = null;

        if (teacher != null) {
            infoTeacher = new InfoTeacher();
            infoPerson = new InfoPerson();
            infoCategory = new InfoCategory();
            infoPerson = copyIPerson2InfoPerson(teacher.getPerson());
            infoCategory = copyICategory2InfoCategory(teacher.getCategory());
            copyObjectProperties(infoTeacher, teacher);
            infoTeacher.setInfoPerson(infoPerson);
            infoTeacher.setInfoCategory(infoCategory);
        }
        return infoTeacher;
    }

    /**
     * @param category
     * @return
     */
    public static InfoCategory copyICategory2InfoCategory(ICategory category) {
        InfoCategory infoCategory = new InfoCategory();
        copyObjectProperties(infoCategory, category);

        return infoCategory;
    }

    /**
     * @author dcs-rjao
     * @param IDegreeCurricularPlan
     * @return InfoDegreeCurricularPlan
     */
    public static InfoDegreeCurricularPlan copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(
            IDegreeCurricularPlan degreeCurricularPlan) {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();

        InfoDegree infoDegree = Cloner.copyIDegree2InfoDegree(degreeCurricularPlan.getDegree());

        copyObjectProperties(infoDegreeCurricularPlan, degreeCurricularPlan);

        infoDegreeCurricularPlan.setInfoDegree(infoDegree);

        return infoDegreeCurricularPlan;
    }

    public static InfoCurricularCourse copyCurricularCourse2InfoCurricularCourseWithCurricularCourseScopes(
            ICurricularCourse curricularCourse) {

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(curricularCourse
                .getDegreeCurricularPlan());

        copyObjectProperties(infoCurricularCourse, curricularCourse);

        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        List infoCurricularCourseScopes = new ArrayList();
        Iterator iter = curricularCourse.getScopes().iterator();
        while (iter.hasNext()) {
            infoCurricularCourseScopes
                    .add(copyICurricularCourseScope2InfoCurricularCourseScope((ICurricularCourseScope) iter
                            .next()));

        }
        infoCurricularCourse.setInfoScopes(infoCurricularCourseScopes);

        return infoCurricularCourse;
    }

    /**
     * @author dcs-rjao
     * @param IBranch
     * @return InfoBranch
     */
    public static InfoBranch copyIBranch2InfoBranch(IBranch branch) {

        InfoBranch infoBranch = new InfoBranch();
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        // modified by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on
        // 25/Set/2003
        if (branch != null) {
            infoDegreeCurricularPlan = Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(branch
                    .getDegreeCurricularPlan());
            copyObjectProperties(infoBranch, branch);
        }
        infoBranch.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        return infoBranch;
    }

    /**
     * @author dcs-rjao
     * @param ICurricularCourse
     * @return InfoCurricularCourse
     */

    public static InfoCurricularCourse copyCurricularCourse2InfoCurricularCourse(
            ICurricularCourse curricularCourse) {

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        InfoUniversity infoUniversity = Cloner.copyIUniversity2InfoUniversity(curricularCourse
                .getUniversity());
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(curricularCourse
                .getDegreeCurricularPlan());

        copyObjectProperties(infoCurricularCourse, curricularCourse);

        if (curricularCourse.getScientificArea() != null) {
            InfoScientificArea infoScientificArea = copyIScientificArea2InfoScientificArea(curricularCourse
                    .getScientificArea());
            infoCurricularCourse.setInfoScientificArea(infoScientificArea);
        }

        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoCurricularCourse.setInfoUniversity(infoUniversity);
        return infoCurricularCourse;
    }

    /**
     * @author dcs-rjao
     * @param ICurricularCourseScope
     * @return InfoCurricularCourseScope
     */

    public static InfoCurricularCourseScope copyICurricularCourseScope2InfoCurricularCourseScope(
            ICurricularCourseScope curricularCourseScope) {

        InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();

        InfoCurricularCourse infoCurricularCourse = copyCurricularCourse2InfoCurricularCourse(curricularCourseScope
                .getCurricularCourse());
        InfoCurricularSemester infoCurricularSemester = copyCurricularSemester2InfoCurricularSemester(curricularCourseScope
                .getCurricularSemester());
        InfoBranch infoBranch = copyIBranch2InfoBranch(curricularCourseScope.getBranch());

        copyObjectProperties(infoCurricularCourseScope, curricularCourseScope);

        infoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);
        infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);
        infoCurricularCourseScope.setInfoBranch(infoBranch);

        return infoCurricularCourseScope;
    }

    /**
     * @author dcs-rjao
     * @param ICurricularSemester
     * @return InfoCurricularSemester
     */
    public static InfoCurricularSemester copyCurricularSemester2InfoCurricularSemester(
            ICurricularSemester curricularSemester) {

        InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();

        InfoCurricularYear infoCurricularYear = copyCurricularYear2InfoCurricularYear(curricularSemester
                .getCurricularYear());

        copyObjectProperties(infoCurricularSemester, curricularSemester);

        infoCurricularSemester.setInfoCurricularYear(infoCurricularYear);

        return infoCurricularSemester;
    }

    /**
     * @author dcs-rjao
     * @param ICurricularYear
     * @return InfoCurricularYear
     */
    public static InfoCurricularYear copyCurricularYear2InfoCurricularYear(ICurricularYear curricularYear) {
        InfoCurricularYear infoCurricularYear = new InfoCurricularYear();
        copyObjectProperties(infoCurricularYear, curricularYear);
        return infoCurricularYear;
    }

    /**
     * @author dcs-rjao
     * @param copyIStudentCurricularPlan2InfoStudentCurricularPlan
     * @return InfoStudentCurricularPlan
     */
    public static InfoStudentCurricularPlan copyIStudentCurricularPlan2InfoStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) {

        InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();

        InfoStudent infoStudent = null;
        if (studentCurricularPlan.getStudent() != null) {
            infoStudent = Cloner.copyIStudent2InfoStudent(studentCurricularPlan.getStudent());
        }
        InfoBranch infoBranch = null;
        if (studentCurricularPlan.getBranch() != null) {
            infoBranch = Cloner.copyIBranch2InfoBranch(studentCurricularPlan.getBranch());
        }
        InfoBranch infoSecundaryBranch = null;
        if (studentCurricularPlan.getSecundaryBranch() != null) {
            infoSecundaryBranch = Cloner.copyIBranch2InfoBranch(studentCurricularPlan
                    .getSecundaryBranch());
        }
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        if (studentCurricularPlan.getDegreeCurricularPlan() != null) {
            infoDegreeCurricularPlan = Cloner
                    .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(studentCurricularPlan
                            .getDegreeCurricularPlan());
        }
        try {
            BeanUtils.copyProperties(infoStudentCurricularPlan, studentCurricularPlan);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        infoStudentCurricularPlan.setInfoStudent(infoStudent);
        infoStudentCurricularPlan.setInfoBranch(infoBranch);
        infoStudentCurricularPlan.setInfoSecundaryBranch(infoSecundaryBranch);
        infoStudentCurricularPlan.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        return infoStudentCurricularPlan;
    }

    /**
     * @author dcs-rjao
     * @param IStudentKind
     * @return InfoStudentKind
     */
    public static InfoStudentKind copyIStudentKind2InfoStudentKind(IStudentKind studentGroupInfo) {
        InfoStudentKind infoStudentKind = new InfoStudentKind();
        copyObjectProperties(infoStudentKind, studentGroupInfo);
        return infoStudentKind;
    }

    /**
     * @author dcs-rjao
     * @param IEnrolmentEvaluation
     * @return InfoEnrolmentEvaluation
     */
    public static InfoEnrolmentEvaluation copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(
            IEnrolmentEvaluation enrolmentEvaluation) {
        // properties of infoEnrolment are not copied for not to get into loop
        InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
        InfoPerson infoPerson = copyIPerson2InfoPerson(enrolmentEvaluation
                .getPersonResponsibleForGrade());
        copyObjectProperties(infoEnrolmentEvaluation, enrolmentEvaluation);
        infoEnrolmentEvaluation.setInfoPersonResponsibleForGrade(infoPerson);
        return infoEnrolmentEvaluation;
    }

    public static InfoShiftProfessorship copyIShiftProfessorship2InfoShiftProfessorship(
            IShiftProfessorship teacherShiftPercentage) {
        InfoShiftProfessorship infoTeacherShiftPercentage = new InfoShiftProfessorship();

        InfoProfessorship infoProfessorShip = Cloner
                .copyIProfessorship2InfoProfessorship(teacherShiftPercentage.getProfessorship());
        InfoShift infoShift = (InfoShift) Cloner.get(teacherShiftPercentage.getShift());

        copyObjectProperties(infoTeacherShiftPercentage, teacherShiftPercentage);

        infoTeacherShiftPercentage.setInfoProfessorship(infoProfessorShip);
        infoTeacherShiftPercentage.setInfoShift(infoShift);

        return infoTeacherShiftPercentage;
    }

    public static InfoProfessorship copyIProfessorship2InfoProfessorship(IProfessorship professorship) {
        InfoProfessorship infoProfessorShip = new InfoProfessorship();

        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(professorship.getTeacher());

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(professorship
                .getExecutionCourse());

        copyObjectProperties(infoProfessorShip, professorship);

        infoProfessorShip.setInfoTeacher(infoTeacher);
        infoProfessorShip.setInfoExecutionCourse(infoExecutionCourse);

        return infoProfessorShip;
    }

    /**
     * @param IAttends
     * @return InfoFrequenta
     */
    public static InfoFrequenta copyIFrequenta2InfoFrequenta(IAttends frequenta) {
        if (frequenta == null) {
            return null;
        }

        InfoFrequenta infoFrequenta = new InfoFrequenta();

        InfoStudent infoStudent = new InfoStudent();
        infoStudent = Cloner.copyIStudent2InfoStudent(frequenta.getAluno());

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoExecutionCourse = (InfoExecutionCourse) Cloner.get(frequenta.getDisciplinaExecucao());

        InfoEnrolment infoEnrolment = null;
        if (frequenta.getEnrolment() != null) {
            infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                    .newInfoFromDomain(frequenta.getEnrolment());
        }

        infoFrequenta.setIdInternal(frequenta.getIdInternal());
        infoFrequenta.setAluno(infoStudent);
        infoFrequenta.setDisciplinaExecucao(infoExecutionCourse);
        infoFrequenta.setInfoEnrolment(infoEnrolment);

        return infoFrequenta;

    }

    /**
     * @param price
     * @return InfoPrice
     */
    public static InfoPrice copyIPrice2InfoPrice(IPrice price) {

        InfoPrice infoPrice = new InfoPrice();
        copyObjectProperties(infoPrice, price);
        return infoPrice;
    }

    /**
     * @param examStudentRoom
     * @return
     */
    public static InfoExamStudentRoom copyIExamStudentRoom2InfoExamStudentRoom(
            IExamStudentRoom examStudentRoom) {

        InfoExam infoExam = Cloner.copyIExam2InfoExam(examStudentRoom.getExam());
        InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(examStudentRoom.getStudent());
        InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(examStudentRoom.getRoom());

        InfoExamStudentRoom infoExamStudentRoom = new InfoExamStudentRoom();

        copyObjectProperties(infoExamStudentRoom, examStudentRoom);
        infoExamStudentRoom.setInfoExam(infoExam);
        infoExamStudentRoom.setInfoRoom(infoRoom);
        infoExamStudentRoom.setInfoStudent(infoStudent);
        return infoExamStudentRoom;
    }

    public static InfoDepartment copyIDepartment2InfoDepartment(IDepartment department) {
        InfoDepartment infoDeparment = new InfoDepartment();
        copyObjectProperties(infoDeparment, department);
        return infoDeparment;
    }

    /**
     * @param groupProperties
     * @return infoGroupProperties
     */

    public static InfoGroupProperties copyIGroupProperties2InfoGroupProperties(
            IGroupProperties groupProperties) {
        InfoGroupProperties infoGroupProperties = new InfoGroupProperties();

        infoGroupProperties.setEnrolmentBeginDay(groupProperties.getEnrolmentBeginDay());
        infoGroupProperties.setEnrolmentEndDay(groupProperties.getEnrolmentEndDay());
        infoGroupProperties.setMaximumCapacity(groupProperties.getMaximumCapacity());
        infoGroupProperties.setMinimumCapacity(groupProperties.getMinimumCapacity());
        infoGroupProperties.setIdealCapacity(groupProperties.getIdealCapacity());
        infoGroupProperties.setGroupMaximumNumber(groupProperties.getGroupMaximumNumber());
        infoGroupProperties.setEnrolmentPolicy(groupProperties.getEnrolmentPolicy());
        infoGroupProperties.setIdInternal(groupProperties.getIdInternal());
        infoGroupProperties.setName(groupProperties.getName());
        infoGroupProperties.setShiftType(groupProperties.getShiftType());
        infoGroupProperties.setProjectDescription(groupProperties.getProjectDescription());

        return infoGroupProperties;
    }

    /**
     * @param IGroupPropertiesExecutionCourse
     * @return infoGroupPropertiesExecutionCourse
     */

    public static InfoGroupPropertiesExecutionCourse copyIGroupPropertiesExecutionCourse2InfoGroupPropertiesExecutionCourse(
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        InfoGroupPropertiesExecutionCourse infoGroupPropertiesExecutionCourse = new InfoGroupPropertiesExecutionCourse();
        infoGroupPropertiesExecutionCourse.setIdInternal(groupPropertiesExecutionCourse.getIdInternal());
        InfoGroupProperties infoGroupProperties = copyIGroupProperties2InfoGroupProperties(groupPropertiesExecutionCourse
                .getGroupProperties());
        InfoExecutionCourse infoExecutionCourse = copy(groupPropertiesExecutionCourse
                .getExecutionCourse());
        infoGroupPropertiesExecutionCourse.setInfoExecutionCourse(infoExecutionCourse);
        infoGroupPropertiesExecutionCourse.setInfoGroupProperties(infoGroupProperties);
        return infoGroupPropertiesExecutionCourse;
    }

    /**
     * @param attendsSet
     * @return infoAttendsSet
     */

    public static InfoAttendsSet copyIAttendsSet2InfoAttendsSet(IAttendsSet attendsSet) {

        InfoAttendsSet infoAttendsSet = new InfoAttendsSet();
        infoAttendsSet.setIdInternal(attendsSet.getIdInternal());
        infoAttendsSet.setName(attendsSet.getName());

        return infoAttendsSet;
    }

    public static InfoCurricularYear copyICurricularYear2InfoCurricularYear(
            ICurricularYear curricularYear) {
        InfoCurricularYear infoCurricularYear = new InfoCurricularYear();
        copyObjectProperties(infoCurricularYear, curricularYear);
        return infoCurricularYear;
    }

    public static InfoWebSite copyIWebSite2InfoWebSite(IWebSite webSite) {

        InfoWebSite infoWebSite = null;

        if (webSite instanceof ISiteIST) {
            infoWebSite = new InfoSiteIST();
        }

        copyObjectProperties(infoWebSite, webSite);

        return infoWebSite;
    }

    public static InfoWebSiteSection copyIWebSiteSection2InfoWebSiteSection(IWebSiteSection section) {

        InfoWebSiteSection infoWebSiteSection = new InfoWebSiteSection();

        copyObjectProperties(infoWebSiteSection, section);

        InfoWebSite infoWebSite = Cloner.copyIWebSite2InfoWebSite(section.getWebSite());
        infoWebSiteSection.setInfoWebSite(infoWebSite);

        return infoWebSiteSection;

    }

    public static InfoWebSiteItem copyIWebSiteItem2InfoWebSiteItem(IWebSiteItem item) {
        InfoWebSiteItem infoWebSiteItem = new InfoWebSiteItem();

        copyObjectProperties(infoWebSiteItem, item);

        InfoWebSiteSection infoWebSiteSection = Cloner.copyIWebSiteSection2InfoWebSiteSection(item
                .getWebSiteSection());
        infoWebSiteItem.setInfoWebSiteSection(infoWebSiteSection);

        InfoPerson person = Cloner.copyIPerson2InfoPerson(item.getEditor());
        infoWebSiteItem.setInfoEditor(person);

        Calendar calendar = Calendar.getInstance();
        if (item.getItemBeginDay() != null) {
            calendar.clear();
            calendar.setTimeInMillis(item.getItemBeginDay().getTime());
            infoWebSiteItem.setItemBeginDayCalendar(calendar);
        }
        if (item.getItemEndDay() != null) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.getItemEndDay().getTime());
            infoWebSiteItem.setItemEndDayCalendar(calendar);
        }

        return infoWebSiteItem;
    }

    public static InfoMasterDegreeProofVersion copyIMasterDegreeProofVersion2InfoMasterDegreeProofVersion(
            IMasterDegreeProofVersion masterDegreeProofVersion) {
        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = new InfoMasterDegreeProofVersion();

        copyObjectProperties(infoMasterDegreeProofVersion, masterDegreeProofVersion);

        InfoMasterDegreeThesis infoMasterDegreeThesis = Cloner
                .copyIMasterDegreeThesis2InfoMasterDegreeThesis(masterDegreeProofVersion
                        .getMasterDegreeThesis());
        InfoEmployee infoEmployee = Cloner.copyIEmployee2InfoEmployee(masterDegreeProofVersion
                .getResponsibleEmployee());
        List infoJuries = Cloner.copyListITeacher2ListInfoTeacher(masterDegreeProofVersion.getJuries());
        List infoExternalPersonJuries = Cloner
                .copyListIExternalPerson2ListInfoExternalPerson(masterDegreeProofVersion
                        .getExternalJuries());

        infoMasterDegreeProofVersion.setInfoMasterDegreeThesis(infoMasterDegreeThesis);
        infoMasterDegreeProofVersion.setInfoResponsibleEmployee(infoEmployee);
        infoMasterDegreeProofVersion.setInfoJuries(infoJuries);
        infoMasterDegreeProofVersion.setInfoExternalJuries(infoExternalPersonJuries);

        return infoMasterDegreeProofVersion;

    }

    public static List copyListIMasterDegreeProofVersion2ListInfoMasterDegreeProofVersion(
            List listIMasterDegreeProofVersion) {
        List listInfoMasterDegreeProofVersion = new ArrayList();

        Iterator iterListIMasterDegreeProofVersion = listIMasterDegreeProofVersion.iterator();

        while (iterListIMasterDegreeProofVersion.hasNext()) {
            IMasterDegreeProofVersion masterDegreeProofVersion = (IMasterDegreeProofVersion) iterListIMasterDegreeProofVersion
                    .next();
            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = Cloner
                    .copyIMasterDegreeProofVersion2InfoMasterDegreeProofVersion(masterDegreeProofVersion);
            listInfoMasterDegreeProofVersion.add(infoMasterDegreeProofVersion);
        }

        return listInfoMasterDegreeProofVersion;
    }

    public static InfoMasterDegreeThesis copyIMasterDegreeThesis2InfoMasterDegreeThesis(
            IMasterDegreeThesis masterDegreeThesis) {
        InfoMasterDegreeThesis infoMasterDegreeThesis = new InfoMasterDegreeThesis();
        InfoStudentCurricularPlan infoStudentCurricularPlan = Cloner
                .copyIStudentCurricularPlan2InfoStudentCurricularPlan(masterDegreeThesis
                        .getStudentCurricularPlan());
        copyObjectProperties(infoMasterDegreeThesis, masterDegreeThesis);
        infoMasterDegreeThesis.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

        return infoMasterDegreeThesis;
    }

    public static InfoEmployee copyIEmployee2InfoEmployee(IEmployee employee) {
        InfoEmployee infoEmployee = new InfoEmployee();
        InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(employee.getPerson());
        infoEmployee.setIdInternal(employee.getIdInternal());
        infoEmployee.setPerson(infoPerson);

        return infoEmployee;
    }

    public static InfoExternalPerson copyIExternalPerson2InfoExternalPerson(
            IExternalPerson externalPerson) {
        InfoExternalPerson infoExternalPerson = new InfoExternalPerson();
        copyObjectProperties(infoExternalPerson, externalPerson);
        InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(externalPerson.getPerson());
        infoExternalPerson.setInfoPerson(infoPerson);
        InfoWorkLocation infoWorkLocation = Cloner.copyIWorkLocation2InfoWorkLocation(externalPerson
                .getWorkLocation());
        infoExternalPerson.setInfoWorkLocation(infoWorkLocation);

        return infoExternalPerson;
    }

    public static List copyListIExternalPerson2ListInfoExternalPerson(List listIExternalPerson) {
        List listInfoExternalPersons = new ArrayList();

        Iterator iterListIExternalPerson = listIExternalPerson.iterator();

        while (iterListIExternalPerson.hasNext()) {
            IExternalPerson externalPerson = (IExternalPerson) iterListIExternalPerson.next();
            InfoExternalPerson infoExternalPerson = Cloner
                    .copyIExternalPerson2InfoExternalPerson(externalPerson);
            listInfoExternalPersons.add(infoExternalPerson);
        }

        return listInfoExternalPersons;
    }

    public static List copyListInfoExternalPerson2ListIExternalPerson(List listInfoExternalPerson) {
        List listExternalPersons = new ArrayList();

        Iterator iterListInfoExternalPerson = listInfoExternalPerson.iterator();

        while (iterListInfoExternalPerson.hasNext()) {
            InfoExternalPerson infoExternalPerson = (InfoExternalPerson) iterListInfoExternalPerson
                    .next();
            
            IExternalPerson externalPerson = new ExternalPerson();
            copyObjectProperties(externalPerson, infoExternalPerson);

            IPerson person = new Person();
            copyObjectProperties(person, infoExternalPerson.getInfoPerson());
            ICountry country = new Country();
            copyObjectProperties(country, infoExternalPerson.getInfoPerson().getInfoPais());
            person.setPais(country);
            externalPerson.setPerson(person);

            IWorkLocation workLocation = new WorkLocation();
            copyObjectProperties(workLocation, infoExternalPerson.getInfoWorkLocation());
            externalPerson.setWorkLocation(workLocation);

            listExternalPersons.add(externalPerson);
        }

        return listExternalPersons;
    }

    public static List copyListITeacher2ListInfoTeacher(List listITeacher) {
        List listInfoTeacher = new ArrayList();

        Iterator iterListITeachers = listITeacher.iterator();

        while (iterListITeachers.hasNext()) {
            ITeacher teacher = (ITeacher) iterListITeachers.next();
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            listInfoTeacher.add(infoTeacher);
        }

        return listInfoTeacher;
    }

    public static List copyListInfoTeacher2ListITeacher(List listInfoTeacher) {
        List listITeacher = new ArrayList();

        Iterator iterListInfoTeacher = listInfoTeacher.iterator();

        while (iterListInfoTeacher.hasNext()) {
            InfoTeacher infoTeacher = (InfoTeacher) iterListInfoTeacher.next();

            ITeacher teacher = new Teacher();
            copyObjectProperties(teacher, infoTeacher);

            IPerson person = new Person();
            copyObjectProperties(person, infoTeacher.getInfoPerson());
            ICountry country = new Country();
            copyObjectProperties(country, infoTeacher.getInfoPerson().getInfoPais());
            person.setPais(country);
            teacher.setPerson(person);

            ICategory category = new Category();
            copyObjectProperties(category, infoTeacher.getInfoCategory());
            teacher.setCategory(category);

            listITeacher.add(teacher);
        }

        return listITeacher;
    }

    public static InfoCoordinator copyICoordinator2InfoCoordenator(ICoordinator coordinator) {
        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(coordinator.getTeacher());
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(coordinator
                .getExecutionDegree());
        InfoCoordinator infoCoordinator = new InfoCoordinator();
        copyObjectProperties(infoCoordinator, coordinator);
        infoCoordinator.setInfoExecutionDegree(infoExecutionDegree);
        infoCoordinator.setInfoTeacher(infoTeacher);
        return infoCoordinator;
    }

    /**
     * @author Tânia Pousão Created on 13/Nov/2003
     */
    public static InfoCampus copyICampus2InfoCampus(ICampus campus) {
        InfoCampus infoCampus = new InfoCampus();
        copyObjectProperties(infoCampus, campus);

        return infoCampus;
    }

    public static InfoCareer copyICareer2InfoCareer(ICareer career) {
        InfoCareer infoCareer = null;

        if (career instanceof Proxy) {
            career = (ICareer) ProxyHelper.getRealObject(career);
        }

        if (career instanceof IProfessionalCareer) {
            IProfessionalCareer professionalCareer = (IProfessionalCareer) career;
            infoCareer = copyIProfessionalCareer2InfoProfessionalCareer(professionalCareer);
        } else {
            ITeachingCareer teachingCareer = (ITeachingCareer) career;
            infoCareer = copyITeachingCareer2InfoTeachingCareer(teachingCareer);
        }

        return infoCareer;
    }

    private static InfoProfessionalCareer copyIProfessionalCareer2InfoProfessionalCareer(
            IProfessionalCareer professionalCareer) {
        InfoProfessionalCareer infoProfessionalCareer = new InfoProfessionalCareer();
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(professionalCareer.getTeacher());
        copyObjectProperties(infoProfessionalCareer, professionalCareer);

        infoProfessionalCareer.setInfoTeacher(infoTeacher);

        return infoProfessionalCareer;
    }

    private static InfoTeachingCareer copyITeachingCareer2InfoTeachingCareer(
            ITeachingCareer teachingCareer) {
        InfoTeachingCareer infoTeachingCareer = new InfoTeachingCareer();
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(teachingCareer.getTeacher());
        InfoCategory infoCategory = copyICategory2InfoCategory(teachingCareer.getCategory());
        copyObjectProperties(infoTeachingCareer, teachingCareer);

        infoTeachingCareer.setInfoTeacher(infoTeacher);
        infoTeachingCareer.setInfoCategory(infoCategory);

        return infoTeachingCareer;
    }

    /**
     * @param externalActivity
     * @return
     */
    public static InfoExternalActivity copyIExternalActivity2InfoExternalActivity(
            IExternalActivity externalActivity) {
        InfoExternalActivity infoExternalActivity = new InfoExternalActivity();
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(externalActivity.getTeacher());
        copyObjectProperties(infoExternalActivity, externalActivity);

        infoExternalActivity.setInfoTeacher(infoTeacher);

        return infoExternalActivity;
    }

    public static InfoOldPublication copyIOldPublication2InfoOldPublication(
            IOldPublication oldPublication) {
        InfoOldPublication infoOldPublication = new InfoOldPublication();
        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(oldPublication.getTeacher());
        copyObjectProperties(infoOldPublication, oldPublication);

        infoOldPublication.setInfoTeacher(infoTeacher);
        return infoOldPublication;
    }

    public static InfoGaugingTestResult copyIGaugingTestResult2IngoGaugingTestResult(
            IGaugingTestResult gaugingTestResult) {
        InfoStudent infoStudent = copyIStudent2InfoStudent(gaugingTestResult.getStudent());
        InfoGaugingTestResult infoGaugingTestResult = new InfoGaugingTestResult();
        copyObjectProperties(infoGaugingTestResult, gaugingTestResult);
        infoGaugingTestResult.setInfoStudent(infoStudent);
        return infoGaugingTestResult;
    }

    /**
     * @param supportLesson
     * @return
     */
    public static InfoSupportLesson copyISupportLesson2InfoSupportLesson(ISupportLesson supportLesson) {
        InfoSupportLesson infoSupportLesson = new InfoSupportLesson();
        InfoProfessorship infoProfessorship = Cloner.copyIProfessorship2InfoProfessorship(supportLesson
                .getProfessorship());

        copyObjectProperties(infoSupportLesson, supportLesson);

        infoSupportLesson.setInfoProfessorship(infoProfessorship);

        return infoSupportLesson;
    }

    /**
     * @param teacherDegreeFinalProjectStudent
     * @return
     */
    public static InfoTeacherDegreeFinalProjectStudent copyITeacherDegreeFinalProjectStudent2InfoTeacherDegreeFinalProjectStudent(
            ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent) {
        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacherDegreeFinalProjectStudent
                .getTeacher());
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner
                .get(teacherDegreeFinalProjectStudent.getExecutionPeriod());
        InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(teacherDegreeFinalProjectStudent
                .getStudent());

        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = new InfoTeacherDegreeFinalProjectStudent();

        copyObjectProperties(infoTeacherDegreeFinalProjectStudent, teacherDegreeFinalProjectStudent);

        infoTeacherDegreeFinalProjectStudent.setInfoExecutionPeriod(infoExecutionPeriod);
        infoTeacherDegreeFinalProjectStudent.setInfoStudent(infoStudent);
        infoTeacherDegreeFinalProjectStudent.setInfoTeacher(infoTeacher);

        return infoTeacherDegreeFinalProjectStudent;
    }

    public static InfoTeacherInstitutionWorkTime copyITeacherInstitutionWorkingTime2InfoTeacherInstitutionWorkTime(
            ITeacherInstitutionWorkTime teacherInstitutionWorkTime) {
        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacherInstitutionWorkTime
                .getTeacher());
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner
                .get(teacherInstitutionWorkTime.getExecutionPeriod());

        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = new InfoTeacherInstitutionWorkTime();

        copyObjectProperties(infoTeacherInstitutionWorkTime, teacherInstitutionWorkTime);

        infoTeacherInstitutionWorkTime.setInfoTeacher(infoTeacher);
        infoTeacherInstitutionWorkTime.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoTeacherInstitutionWorkTime;
    }

    public static InfoWorkLocation copyIWorkLocation2InfoWorkLocation(IWorkLocation workLocation) {
        InfoWorkLocation infoWorkLocation = new InfoWorkLocation();
        copyObjectProperties(infoWorkLocation, workLocation);

        return infoWorkLocation;
    }

    public static InfoScientificArea copyIScientificArea2InfoScientificArea(
            IScientificArea scientificArea) {
        InfoScientificArea infoScientificArea = new InfoScientificArea();
        copyObjectProperties(infoScientificArea, scientificArea);

        return infoScientificArea;
    }

    public static InfoGratuityValues copyIGratuityValues2InfoGratuityValues(
            IGratuityValues gratuityValues) {
        InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
        copyObjectProperties(infoGratuityValues, gratuityValues);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(gratuityValues
                .getExecutionDegree());
        InfoEmployee infoEmployee = Cloner.copyIEmployee2InfoEmployee(gratuityValues.getEmployee());

        infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);
        infoGratuityValues.setInfoEmployee(infoEmployee);

        return infoGratuityValues;
    }

    public static InfoPaymentPhase copyIPaymentPhase2InfoPaymentPhase(IPaymentPhase paymentPhase) {
        InfoPaymentPhase infoPaymentPhase = new InfoPaymentPhase();
        copyObjectProperties(infoPaymentPhase, paymentPhase);

        InfoGratuityValues infoGratuityValues = Cloner
                .copyIGratuityValues2InfoGratuityValues(paymentPhase.getGratuityValues());
        infoPaymentPhase.setInfoGratuityValues(infoGratuityValues);
        return infoPaymentPhase;

    }

    public static InfoCourseHistoric copyICourseHistoric2InfoCourseHistoric(
            ICourseHistoric courseHistoric) {
        InfoCourseHistoric infoCourseHistoric = new InfoCourseHistoric();
        copyObjectProperties(infoCourseHistoric, courseHistoric);
        InfoCurricularCourse infoCurricularCourse = Cloner
                .copyCurricularCourse2InfoCurricularCourse(courseHistoric.getCurricularCourse());
        infoCourseHistoric.setInfoCurricularCourse(infoCurricularCourse);

        return infoCourseHistoric;
    }

    public static InfoStudentCourseReport copyIStudentCourseReport2InfoStudentCourseReport(
            IStudentCourseReport studentCourseReport) {
        InfoStudentCourseReport infoStudentCourseReport = new InfoStudentCourseReport();
        copyObjectProperties(infoStudentCourseReport, studentCourseReport);
        InfoCurricularCourse infoCurricularCourse = Cloner
                .copyCurricularCourse2InfoCurricularCourse(studentCourseReport.getCurricularCourse());
        infoStudentCourseReport.setInfoCurricularCourse(infoCurricularCourse);

        return infoStudentCourseReport;
    }

    public static InfoOtherTypeCreditLine copyIOtherTypeCreditLine2InfoOtherCreditLine(
            IOtherTypeCreditLine creditLine) {
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(creditLine.getTeacher());
        InfoExecutionPeriod infoExecutionPeriod = copy(creditLine.getExecutionPeriod());

        InfoOtherTypeCreditLine infoOtherTypeCreditLine = new InfoOtherTypeCreditLine();

        copyObjectProperties(infoOtherTypeCreditLine, creditLine);

        infoOtherTypeCreditLine.setInfoTeacher(infoTeacher);
        infoOtherTypeCreditLine.setInfoExecutionPeriod(infoExecutionPeriod);
        return infoOtherTypeCreditLine;
    }

    public static InfoManagementPositionCreditLine copyIManagementPositionCreditLine2InfoManagementPositionCreditLine(
            IManagementPositionCreditLine creditLine) {
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(creditLine.getTeacher());

        InfoManagementPositionCreditLine infoCreditLine = new InfoManagementPositionCreditLine();

        copyObjectProperties(infoCreditLine, creditLine);

        infoCreditLine.setInfoTeacher(infoTeacher);
        return infoCreditLine;
    }

    public static InfoServiceExemptionCreditLine copyIServiceExemptionCreditLine2InfoServiceExemptionCreditLine(
            IServiceExemptionCreditLine creditLine) {
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(creditLine.getTeacher());

        InfoServiceExemptionCreditLine infoCreditLine = new InfoServiceExemptionCreditLine();

        copyObjectProperties(infoCreditLine, creditLine);

        infoCreditLine.setInfoTeacher(infoTeacher);
        return infoCreditLine;
    }

    /**
     * Method copyInfoExecutionCourse2ExecutionCourse.
     * 
     * @param infoExecutionCourse
     * @return IDisciplinaExecucao
     */
    public static IExecutionCourse copyInfoExecutionCourse2ExecutionCourse(
            InfoExecutionCourse infoExecutionCourse) {
        IExecutionCourse executionCourse = new ExecutionCourse();
        IExecutionPeriod executionPeriod = Cloner
                .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionCourse.getInfoExecutionPeriod());

        copyObjectProperties(executionCourse, infoExecutionCourse);

        executionCourse.setExecutionPeriod(executionPeriod);
        return executionCourse;
    }

    /**
     * Method copyInfoRoomOccupation2RoomOccupation.
     * 
     * @param infoRoomOccupation
     */
    public static IRoomOccupation copyInfoRoomOccupation2RoomOccupation(
            InfoRoomOccupation infoRoomOccupation) {
        IRoomOccupation roomOccupation = new RoomOccupation();
        copyObjectProperties(roomOccupation, infoRoomOccupation);
        return roomOccupation;
    }



    public static IExecutionPeriod copyInfoExecutionPeriod2IExecutionPeriod(
            InfoExecutionPeriod infoExecutionPeriod) {

        IExecutionPeriod executionPeriod = new ExecutionPeriod();
        InfoExecutionYear infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();
        IExecutionYear executionYear = null;
        if (infoExecutionYear != null) {
            executionYear = new ExecutionYear();
            try {
                BeanUtils.copyProperties(executionYear, infoExecutionYear);
            } catch (Exception e) {
                    throw new RuntimeException(e);
            }
        } else {
            copyObjectProperties(executionYear, infoExecutionYear);
        }

        copyObjectProperties(executionPeriod, infoExecutionPeriod);

        executionPeriod.setExecutionYear(executionYear);

        return executionPeriod;
    }

    public static IRoomOccupation copyInfoRoomOccupation2IRoomOccupation(
            InfoRoomOccupation infoRoomOccupation) {

        IRoomOccupation roomOccupation = new RoomOccupation();
        copyObjectProperties(roomOccupation, infoRoomOccupation);
        
        IPeriod period = Cloner.copyInfoPeriod2IPeriod(infoRoomOccupation.getInfoPeriod());
        roomOccupation.setPeriod(period);
        
        IRoom room = new Room();
        copyObjectProperties(room, infoRoomOccupation.getInfoRoom());
        roomOccupation.setRoom(room);

        return roomOccupation;
    }

    public static IPeriod copyInfoPeriod2IPeriod(InfoPeriod infoPeriod) {
        IPeriod period = new Period();

        copyObjectProperties(period, infoPeriod);

        return period;
    }

}
