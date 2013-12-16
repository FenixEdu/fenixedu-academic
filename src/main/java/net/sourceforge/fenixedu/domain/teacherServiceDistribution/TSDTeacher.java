package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;

import org.apache.commons.collections.Predicate;

public abstract class TSDTeacher extends TSDTeacher_Base {

    protected TSDTeacher() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Set<TSDCourse> getAssociatedTSDCourses() {
        Set<TSDCourse> coursesSet = new HashSet<TSDCourse>();

        for (TSDProfessorship professorship : getActiveTSDProfessorships()) {
            coursesSet.add(professorship.getTSDCourse());
        }

        return coursesSet;
    }

    public abstract String getName();

    public abstract Double getRealHoursByShiftTypeAndExecutionCourses(ShiftType shiftType,
            List<ExecutionCourse> executionCourseList);

    public abstract Double getRequiredHours(final List<ExecutionSemester> executionPeriodList);

    public abstract Double getServiceExemptionCredits(List<ExecutionSemester> executionPeriodList);

    public abstract Double getManagementFunctionsCredits(List<ExecutionSemester> executionPeriodList);

    public abstract List<PersonContractSituation> getServiceExemptions(List<ExecutionSemester> executionPeriodList);

    public abstract List<PersonFunction> getManagementFunctions(List<ExecutionSemester> executionPeriodList);

    public abstract String getEmailUserId();

    public abstract String getShortName();

    public abstract String getDistinctName();

    public abstract String getTeacherId();

    public Set<ShiftType> getAssociatedShiftTypes() {
        Set<ShiftType> typesSet = new HashSet<ShiftType>();

        for (TSDProfessorship professorship : getActiveTSDProfessorships()) {
            typesSet.add(professorship.getType());
        }

        return typesSet;
    }

    public List<TSDProfessorship> getTSDProfessorShipsByCourse(TSDCourse course) {
        List<TSDProfessorship> professorshipList = new ArrayList<TSDProfessorship>();

        for (TSDProfessorship tsdProfessorship : getActiveTSDProfessorships()) {
            if (tsdProfessorship.getTSDCourse().equals(course)) {
                professorshipList.add(tsdProfessorship);
            }
        }

        return professorshipList;
    }

    public TSDProfessorship getTSDProfessorShipByCourseAndShiftType(TSDCourse course, ShiftType type) {
        for (TSDProfessorship tsdProfessorship : getTSDProfessorShipsByCourse(course)) {
            if (tsdProfessorship.getType().equals(type)) {
                return tsdProfessorship;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public List<TSDProfessorship> getTSDProfessorshipsByShiftType(final ShiftType type) {
        return (List<TSDProfessorship>) CollectionUtils.select(getTSDProfessorships(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                TSDProfessorship tsdProfessorship = (TSDProfessorship) arg0;
                return tsdProfessorship.getType().equals(type) && tsdProfessorship.getIsActive();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<TSDProfessorship> getActiveTSDProfessorships() {
        return (List<TSDProfessorship>) CollectionUtils.select(getTSDProfessorships(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                TSDProfessorship tsdProfessorship = (TSDProfessorship) arg0;
                return tsdProfessorship.getIsActive();
            }
        });
    }

    public Double getTotalHoursLecturedByShiftType(ShiftType shiftType, List<ExecutionSemester> executionPeriodList) {
        Double totalHours = 0d;

        if (executionPeriodList == null) {
            for (TSDProfessorship tsdProfessorship : getTSDProfessorshipsByShiftType(shiftType)) {
                totalHours += tsdProfessorship.getHours();
            }
        } else {
            for (ExecutionSemester executionSemester : executionPeriodList) {
                for (TSDProfessorship tsdProfessorship : getTSDProfessorshipsByShiftType(shiftType)) {
                    totalHours += (tsdProfessorship.getExecutionPeriod() == executionSemester) ? tsdProfessorship.getHours() : 0d;
                }
            }
        }

        return totalHours;
    }

    public Double getTotalHoursLecturedOnTSDCourseByShiftType(TSDCourse course, ShiftType shiftType) {
        TSDProfessorship tsdProfessorship = getTSDProfessorShipByCourseAndShiftType(course, shiftType);

        return tsdProfessorship == null ? 0d : tsdProfessorship.getHours();
    }

    public Double getTotalHoursLecturedOnTSDCourse(TSDCourse course) {
        Double totalHours = 0d;

        for (TSDProfessorship tsdProfessorship : getTSDProfessorShipsByCourse(course)) {
            totalHours += tsdProfessorship.getHours();
        }

        return totalHours;
    }

    public Department getDepartment() {
        return getTeacherServiceDistributions().iterator().next().getTSDProcessPhase().getTSDProcess().getDepartment();
    }

    public Double getTotalHoursLectured(List<ExecutionSemester> executionPeriodList) {
        Double totalHours = 0d;

        if (executionPeriodList == null) {
            for (TSDProfessorship tsdProfessorship : getActiveTSDProfessorships()) {
                totalHours += tsdProfessorship.getHours();
            }
        } else {
            for (ExecutionSemester executionSemester : executionPeriodList) {
                for (TSDProfessorship tsdProfessorship : getActiveTSDProfessorships()) {
                    totalHours += (tsdProfessorship.getExecutionPeriod() == executionSemester) ? tsdProfessorship.getHours() : 0d;
                }
            }
        }

        return totalHours;
    }

    public Double getAvailability(List<ExecutionSemester> executionPeriodList) {
        return getRequiredHours(executionPeriodList) - getTotalHoursLectured(executionPeriodList)
                - (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0.0);
    }

    public void delete() {
        for (TeacherServiceDistribution grouping : getTeacherServiceDistributions()) {
            removeTeacherServiceDistributions(grouping);
        }
        for (TSDProfessorship tsdProfessorship : getTSDProfessorships()) {
            tsdProfessorship.delete();
        }

        setProfessionalCategory(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public String getAcronym() {
        StringBuilder sb = new StringBuilder();
        String name = getName();
        String[] nameCompounds = name.split(" ");

        for (String compound : nameCompounds) {
            compound = compound.trim();

            if (compound.length() > 0 && Character.isUpperCase(compound.charAt(0))) {
                sb.append(compound.charAt(0));
            }
        }

        return sb.toString();
    }

    public Double getRequiredTeachingHours(List<ExecutionSemester> executionPeriodList) {
        return getRequiredHours(executionPeriodList) - (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0.0);
    }

    public Double getExtraCreditsValue(List<ExecutionSemester> executionPeriodList) {
        return getExtraCreditsValue()
                * (executionPeriodList.size() / getTeacherServiceDistributions().iterator().next().getTSDProcessPhase()
                        .getTSDProcess().getExecutionPeriodsSet().size());
    }

    public Double getTotalHoursLecturedPlusExtraCredits(List<ExecutionSemester> executionPeriodList) {
        return getTotalHoursLectured(executionPeriodList)
                + (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0d);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship> getTSDProfessorships() {
        return getTSDProfessorshipsSet();
    }

    @Deprecated
    public boolean hasAnyTSDProfessorships() {
        return !getTSDProfessorshipsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution> getTeacherServiceDistributions() {
        return getTeacherServiceDistributionsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherServiceDistributions() {
        return !getTeacherServiceDistributionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUsingExtraCredits() {
        return getUsingExtraCredits() != null;
    }

    @Deprecated
    public boolean hasProfessionalCategory() {
        return getProfessionalCategory() != null;
    }

    @Deprecated
    public boolean hasExtraCreditsName() {
        return getExtraCreditsName() != null;
    }

    @Deprecated
    public boolean hasExtraCreditsValue() {
        return getExtraCreditsValue() != null;
    }

}
