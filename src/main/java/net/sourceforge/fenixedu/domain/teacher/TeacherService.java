/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class TeacherService extends TeacherService_Base {

    public TeacherService(Teacher teacher, ExecutionSemester executionSemester) {
        super();
        if (teacher == null || executionSemester == null) {
            throw new DomainException("arguments can't be null");
        }
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
        if (teacherService != null) {
            throw new DomainException("error.teacherService.already.exists.one.teacherService.in.executionPeriod");
        }
        setRootDomainObject(Bennu.getInstance());
        setTeacher(teacher);
        setExecutionPeriod(executionSemester);
    }

    public void delete() {
        if (getServiceItems().isEmpty()) {
            setTeacher(null);
            setExecutionPeriod(null);
            setRootDomainObject(null);
            deleteDomainObject();
        } else {
            throw new DomainException("There are service items associated to this Teacher Service");
        }
    }

    @Atomic
    public static TeacherService getTeacherService(Teacher teacher, ExecutionSemester executionPeriod) {
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionPeriod);
        }
        return teacherService;
    }

    public DegreeTeachingService getDegreeTeachingServiceByShiftAndProfessorship(final Shift shift,
            final Professorship professorship) {
        return (DegreeTeachingService) CollectionUtils.find(getDegreeTeachingServices(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                DegreeTeachingService degreeTeachingService = (DegreeTeachingService) arg0;
                return (degreeTeachingService.getShift() == shift) && (degreeTeachingService.getProfessorship() == professorship);
            }
        });
    }

    public List<DegreeTeachingService> getDegreeTeachingServiceByProfessorship(final Professorship professorship) {
        return (List<DegreeTeachingService>) CollectionUtils.select(getDegreeTeachingServices(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                DegreeTeachingService degreeTeachingService = (DegreeTeachingService) arg0;
                return degreeTeachingService.getProfessorship() == professorship;
            }
        });
    }

    public TeacherMasterDegreeService getMasterDegreeServiceByProfessorship(Professorship professorship) {
        for (TeacherMasterDegreeService masterDegreeService : getMasterDegreeServices()) {
            if (masterDegreeService.getProfessorship() == professorship) {
                return masterDegreeService;
            }
        }
        return null;
    }

    public Double getCredits() throws ParseException {
        double credits = getMasterDegreeServiceCredits();
        credits += getTeachingDegreeCredits();
        credits += getOtherServiceCredits();
        credits += getTeacherAdviseServiceCredits();
        return round(credits);
    }

    public Double getTeachingDegreeHours() {
        double hours = 0;
        for (DegreeTeachingService degreeTeachingService : getDegreeTeachingServices()) {
            if ((!degreeTeachingService.getProfessorship().getExecutionCourse().isDissertation())
                    && (!degreeTeachingService.getProfessorship().getExecutionCourse().getProjectTutorialCourse())) {
                hours += degreeTeachingService.getEfectiveLoad();
            }
        }
        return round(hours);
    }

    public Double getTeachingDegreeCorrections() {
        double hours = 0;
        for (OtherService otherService : getOtherServices()) {
            if (otherService instanceof DegreeTeachingServiceCorrection) {
                DegreeTeachingServiceCorrection degreeTeachingServiceCorrection = (DegreeTeachingServiceCorrection) otherService;
                if ((!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse().isDissertation())
                        && (!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse().getProjectTutorialCourse())) {
                    hours += degreeTeachingServiceCorrection.getCorrection().doubleValue();
                }
            }
        }
        return round(hours);
    }

    public Double getTeachingDegreeCredits() {
        double credits = 0;
        for (DegreeTeachingService degreeTeachingService : getDegreeTeachingServices()) {
            if ((!degreeTeachingService.getProfessorship().getExecutionCourse().isDissertation())
                    && (!degreeTeachingService.getProfessorship().getExecutionCourse().getProjectTutorialCourse())) {
                credits += degreeTeachingService.calculateCredits();
            }
        }
        return round(credits);
    }

    public Double getSupportLessonHours() {
        double hours = 0;
        for (SupportLesson supportLesson : getSupportLessons()) {
            hours += supportLesson.hours();
        }
        return round(hours);
    }

    public Double getMasterDegreeServiceCredits() {
        double credits = 0;
        for (TeacherMasterDegreeService teacherMasterDegreeService : getMasterDegreeServices()) {
            if (teacherMasterDegreeService.getCredits() != null) {
                credits += teacherMasterDegreeService.getCredits();
            }
        }
        return round(credits);
    }

    public Double getTeacherAdviseServiceCredits() {
        double credits = 0;
        for (TeacherAdviseService teacherAdviseService : getTeacherAdviseServices()) {
            credits = credits + ((teacherAdviseService.getPercentage().doubleValue() / 100) * (1.0 / 3));
        }
        return round(credits);
    }

    public Double getPastServiceCredits() {
        double credits = 0;
        TeacherPastService teacherPastService = getPastService();
        if (teacherPastService != null) {
            credits = teacherPastService.getCredits();
        }
        return round(credits);
    }

    public Double getOtherServiceCredits() {
        double credits = 0;
        for (OtherService otherService : getOtherServices()) {
            credits += otherService.getCredits();
        }
        return round(credits);
    }

    public Double getInstitutionWorkingHours() {
        double hours = 0;
        for (InstitutionWorkTime institutionWorkTime : getInstitutionWorkTimes()) {
            hours += institutionWorkTime.getHours();
        }
        return round(hours);
    }

    public List<DegreeTeachingService> getDegreeTeachingServices() {
        return (List<DegreeTeachingService>) CollectionUtils.select(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof DegreeTeachingService;
            }
        });
    }

    public List<DegreeProjectTutorialService> getDegreeProjectTutorialServices() {
        return (List<DegreeProjectTutorialService>) CollectionUtils.select(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof DegreeProjectTutorialService;
            }
        });
    }

    public List<TeacherMasterDegreeService> getMasterDegreeServices() {
        return (List<TeacherMasterDegreeService>) CollectionUtils.select(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof TeacherMasterDegreeService;
            }
        });
    }

    public TeacherPastService getPastService() {
        return (TeacherPastService) CollectionUtils.find(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof TeacherPastService;
            }
        });
    }

    public List<OtherService> getOtherServices() {
        return (List<OtherService>) CollectionUtils.select(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof OtherService;
            }
        });
    }

    public List<InstitutionWorkTime> getInstitutionWorkTimes() {
        return (List<InstitutionWorkTime>) CollectionUtils.select(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof InstitutionWorkTime;
            }
        });
    }

    public ReductionService getReductionService() {
        return (ReductionService) CollectionUtils.find(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof ReductionService;
            }
        });
    }

    public TeacherServiceNotes getTeacherServiceNotes() {
        return (TeacherServiceNotes) CollectionUtils.find(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof TeacherServiceNotes;
            }
        });
    }

    public List<SupportLesson> getSupportLessons() {
        List<SupportLesson> supportLessons = new ArrayList<SupportLesson>();
        for (Professorship professorship : getTeacher().getProfessorships()) {
            ExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod() == getExecutionPeriod()) {
                if (!executionCourse.isMasterDegreeDFAOrDEAOnly()) {
                    supportLessons.addAll(professorship.getSupportLessons());
                }
            }
        }
        return supportLessons;
    }

    public List<TeacherAdviseService> getTeacherAdviseServices() {
        return (List<TeacherAdviseService>) CollectionUtils.select(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof TeacherAdviseService;
            }
        });
    }

    public List<TeacherServiceComment> getTeacherServiceComments() {
        return (List<TeacherServiceComment>) CollectionUtils.select(getServiceItems(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof TeacherServiceComment;
            }
        });
    }

    private Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }

    public BigDecimal getReductionServiceCredits() {
        return getReductionService() == null || getReductionService().getCreditsReductionAttributed() == null ? BigDecimal.ZERO : getReductionService()
                .getCreditsReductionAttributed();
    }

    public SortedSet<TeacherServiceLog> getSortedLogs() {
        return new TreeSet<TeacherServiceLog>(getTeacherServiceLogSet());
    }

    @Atomic
    public void lockTeacherCredits() {
        setTeacherServiceLock(new DateTime());
        new TeacherServiceLog(this, BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.lockTeacherCredits", getExecutionPeriod().getQualifiedName()));
    }

    @Atomic
    public void unlockTeacherCredits() {
        setTeacherServiceLock(null);
        new TeacherServiceLog(this, BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.unlockTeacherCredits", getExecutionPeriod().getQualifiedName()));
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem> getServiceItems() {
        return getServiceItemsSet();
    }

    @Deprecated
    public boolean hasAnyServiceItems() {
        return !getServiceItemsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog> getTeacherServiceLog() {
        return getTeacherServiceLogSet();
    }

    @Deprecated
    public boolean hasAnyTeacherServiceLog() {
        return !getTeacherServiceLogSet().isEmpty();
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasTeacherServiceLock() {
        return getTeacherServiceLock() != null;
    }

}
