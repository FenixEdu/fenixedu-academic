package net.sourceforge.fenixedu.domain.teacher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class TeacherService extends TeacherService_Base {

    public TeacherService(ITeacher teacher, IExecutionPeriod executionPeriod) {
        super();
        if (teacher == null || executionPeriod == null) {
            throw new DomainException("arguments can't be null");
        }
        setTeacher(teacher);
        setExecutionPeriod(executionPeriod);
    }

    public void delete() {
        if (getServiceItems().isEmpty()) {
            removeTeacher();
            removeExecutionPeriod();
            deleteDomainObject();
        } else {
            throw new DomainException("There are service items associated to this Teacher Service");
        }
    }

    public Double getCredits() {
        double credits = getMasterDegreeServiceCredits();
        /*
         * credits += getStudentsFinalWorkDegreeCredits(); credits +=
         * getLessonCredits();
         */

        credits += getPastServiceCredits();
        credits += getOtherServiceCredits();
        return round(credits);
    }

    public Double getTeachingDegreeCredits() {
        double credits = 0;
        for (IDegreeTeachingService degreeTeachingService : getDegreeTeachingServices()) {
            credits += degreeTeachingService.getShift().hours()
                    * (degreeTeachingService.getPercentage().doubleValue() / 100);
        }
        return round(credits);
    }

    public Double getSupportLessonHours() {
        double hours = 0;
        for (ISupportLesson supportLesson : getSupportLessons()) {
            hours += supportLesson.hours();
        }
        return hours;
    }

    public Double getMasterDegreeServiceCredits() {
        double credits = 0;
        for (ITeacherMasterDegreeService teacherMasterDegreeService : getMasterDegreeServices()) {
            credits += teacherMasterDegreeService.getCredits();
        }
        return round(credits);
    }

    public Double getTeacherAdviseServiceCredits() {
        double credits = 0;
        for (ITeacherAdviseService teacherAdviseService : getTeacherAdviseServices()) {
            credits = credits + ((teacherAdviseService.getPercentage().doubleValue()/100) * (1.0/3));
        }
        return round(credits);
    }
    
    public Double getPastServiceCredits() {
        double credits = 0;
        ITeacherPastService teacherPastService = getPastService();
        if (teacherPastService != null) {
            credits = teacherPastService.getCredits();
        }
        return round(credits);
    }

    public Double getOtherServiceCredits() {
        double credits = 0;
        for (IOtherService otherService : getOtherServices()) {
            credits += otherService.getCredits();
        }
        return round(credits);
    }

    public Double getInstitutionWorkingHours() {
        double hours = 0;
        for (IInstitutionWorkTime institutionWorkTime : getInstitutionWorkTimes()) {
            hours += institutionWorkTime.getHours();
        }
        return hours;
    }

    public void verifyOverlappingWithInstitutionWorkingTime(Date startTime, Date endTime, WeekDay weekDay) {
        for (IInstitutionWorkTime teacherInstitutionWorkTime : getInstitutionWorkTimes()) {
            if (teacherInstitutionWorkTime.getWeekDay().equals(weekDay)) {
                Date startWorkTime = teacherInstitutionWorkTime.getStartTime();
                Date endWorkTime = teacherInstitutionWorkTime.getEndTime();
                if (CalendarUtil.intersectTimes(startTime, endTime, startWorkTime, endWorkTime)) {
                    throw new DomainException("message.overlapping.institution.working.period");
                }
            }
        }
    }

    public void verifyOverlappingWithSupportLesson(Date startTime, Date endTime, WeekDay weekDay) {
        for (ISupportLesson supportLesson : getSupportLessons()) {
            if (WeekDay.getWeekDay(supportLesson.getWeekDay()).equals(weekDay)) {
                Date supportLessonStart = supportLesson.getStartTime();
                Date supportLessonEnd = supportLesson.getEndTime();
                if (CalendarUtil
                        .intersectTimes(startTime, endTime, supportLessonStart, supportLessonEnd)) {
                    throw new DomainException("message.overlapping.support.lesson.period");
                }
            }
        }
    }

    public void verifyOverlappingWithTeachingService(Date startTime, Date endTime, WeekDay weekDay) {
        for (IDegreeTeachingService degreeTeachingService : getDegreeTeachingServices()) {
            for (ILesson lesson : degreeTeachingService.getShift().getAssociatedLessons()) {
                if (weekDay.equals(WeekDay.getWeekDay(lesson.getDiaSemana()))) {
                    Date lessonStartTime = lesson.getBegin();
                    Date lessonEndTime = lesson.getEnd();
                    if (CalendarUtil.intersectTimes(startTime, endTime, lessonStartTime, lessonEndTime)) {
                        throw new DomainException("message.overlapping.lesson.period");
                    }
                }
            }
        }
    }

    public IDegreeTeachingService getDegreeTeachingServiceByShiftAndExecutionCourse(final IShift shift,
            final IExecutionCourse executionCourse) {
        return (IDegreeTeachingService) CollectionUtils.find(getDegreeTeachingServices(),
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        IDegreeTeachingService degreeTeachingService = (IDegreeTeachingService) arg0;
                        return (degreeTeachingService.getShift() == shift)
                                && (degreeTeachingService.getProfessorship().getExecutionCourse() == executionCourse);
                    }
                });
    }

    public List<IDegreeTeachingService> getDegreeTeachingServices() {
        return (List<IDegreeTeachingService>) CollectionUtils.select(getServiceItems(), new Predicate() {
            public boolean evaluate(Object arg0) {
                return arg0 instanceof IDegreeTeachingService;
            }
        });
    }

    public List<ITeacherMasterDegreeService> getMasterDegreeServices() {
        return (List<ITeacherMasterDegreeService>) CollectionUtils.select(getServiceItems(),
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        return arg0 instanceof ITeacherMasterDegreeService;
                    }
                });
    }

    public ITeacherPastService getPastService() {
        return (ITeacherPastService) CollectionUtils.find(getServiceItems(), new Predicate() {
            public boolean evaluate(Object arg0) {
                return arg0 instanceof ITeacherPastService;
            }
        });
    }

    public List<IOtherService> getOtherServices() {
        return (List<IOtherService>) CollectionUtils.select(getServiceItems(), new Predicate() {
            public boolean evaluate(Object arg0) {
                return arg0 instanceof IOtherService;
            }
        });
    }

    public List<IInstitutionWorkTime> getInstitutionWorkTimes() {
        return (List<IInstitutionWorkTime>) CollectionUtils.select(getServiceItems(), new Predicate() {
            public boolean evaluate(Object arg0) {
                return arg0 instanceof IInstitutionWorkTime;
            }
        });
    }

    public List<ISupportLesson> getSupportLessons() {
        List<ISupportLesson> supportLessons = new ArrayList<ISupportLesson>();
        for (IProfessorship professorship : getTeacher().getProfessorships()) {
            IExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod() == getExecutionPeriod()) {
                if (!executionCourse.isMasterDegreeOnly()) {
                    supportLessons.addAll(professorship.getSupportLessons());
                }
            }
        }
        return supportLessons;
    }

    public List<ITeacherAdviseService> getTeacherAdviseServices() {
        return (List<ITeacherAdviseService>) CollectionUtils.select(getServiceItems(), new Predicate() {
            public boolean evaluate(Object arg0) {
                return arg0 instanceof ITeacherAdviseService;
            }
        });
    }

    private Double round(double n) {
        long rounded = Math.round(n * 100);
        return new Double(rounded / 100.0);
    }

}
