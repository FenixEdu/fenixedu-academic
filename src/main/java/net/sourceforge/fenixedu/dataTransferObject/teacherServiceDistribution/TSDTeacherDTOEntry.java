package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDRealTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;

import org.apache.commons.collections.Predicate;

public class TSDTeacherDTOEntry extends DataTranferObject {

    public static final Comparator<TSDTeacherDTOEntry> COMPARATOR_BY_NAME = new Comparator<TSDTeacherDTOEntry>() {

        @Override
        public int compare(TSDTeacherDTOEntry o1, TSDTeacherDTOEntry o2) {
            return o1.getName().compareTo(o2.getName());
        }

    };

    private List<TSDTeacher> tsdTeacherList;
    private List<ExecutionSemester> executionPeriodList = null;

    public TSDTeacherDTOEntry(TSDTeacher tsdTeacher, List<ExecutionSemester> executionPeriodList) {
        this.tsdTeacherList = new ArrayList<TSDTeacher>();
        this.tsdTeacherList.add(tsdTeacher);

        this.executionPeriodList = executionPeriodList;
    }

    public List<TSDProfessorshipDTOEntry> getTSDProfessorshipDTOEntries() {
        List<TSDProfessorshipDTOEntry> tsdProfessorshipDTOEntryList = new ArrayList<TSDProfessorshipDTOEntry>();

        for (TSDTeacher tsdTeacher : tsdTeacherList) {
            for (TSDCourse course : tsdTeacher.getAssociatedTSDCourses()) {
                if (executionPeriodList.contains(course.getExecutionPeriod())) {
                    tsdProfessorshipDTOEntryList.add(new TSDProfessorshipDTOEntry(
                            tsdTeacher.getTSDProfessorShipsByCourse(course), executionPeriodList));
                }
            }
        }

        return tsdProfessorshipDTOEntryList;
    }

    public Boolean getIsRealTeacher() {
        return tsdTeacherList.iterator().next() instanceof TSDRealTeacher;
    }

    public ProfessionalCategory getCategory() {
        return tsdTeacherList.iterator().next().getProfessionalCategory();
    }

    public String getName() {
        return tsdTeacherList.iterator().next().getName();
    }

    public List<ExecutionSemester> getExecutionPeriodList() {
        return executionPeriodList;
    }

    /*
     * public Double getTotalTheoreticalHoursLectured() { Double
     * totalTheoreticalHoursLectured = 0d;
     * 
     * for(TSDTeacher tsdTeacher : tsdTeacherList) {
     * totalTheoreticalHoursLectured +=
     * tsdTeacher.getTotalTheoreticalHoursLectured(executionPeriodList); }
     * 
     * return totalTheoreticalHoursLectured; }
     * 
     * public Double getTotalPraticalHoursLectured() { Double
     * totalPraticalHoursLectured = 0d;
     * 
     * for(TSDTeacher tsdTeacher : tsdTeacherList) { totalPraticalHoursLectured
     * += tsdTeacher.getTotalPraticalHoursLectured(executionPeriodList); }
     * 
     * return totalPraticalHoursLectured; }
     * 
     * public Double getTotalTheoPratHoursLectured() { Double
     * totalTheoPratHoursLectured = 0d;
     * 
     * for(TSDTeacher tsdTeacher : tsdTeacherList) { totalTheoPratHoursLectured
     * += tsdTeacher.getTotalTheoPratHoursLectured(executionPeriodList); }
     * 
     * return totalTheoPratHoursLectured; }
     * 
     * public Double getTotalLaboratorialHoursLectured() { Double
     * totalLaboratorialHoursLectured = 0d;
     * 
     * for(TSDTeacher tsdTeacher : tsdTeacherList) {
     * totalLaboratorialHoursLectured +=
     * tsdTeacher.getTotalLaboratorialHoursLectured(executionPeriodList); }
     * 
     * return totalLaboratorialHoursLectured; }
     */

    public Double getTotalHoursLectured() {
        Double totalHoursLectured = 0d;

        for (TSDTeacher tsdTeacher : tsdTeacherList) {
            totalHoursLectured += tsdTeacher.getTotalHoursLectured(executionPeriodList);
        }

        return totalHoursLectured;
    }

    public Double getTotalHoursLecturedByShiftType(ShiftType type) {
        Double totalHoursLectured = 0d;

        for (TSDTeacher tsdTeacher : tsdTeacherList) {
            totalHoursLectured += tsdTeacher.getTotalHoursLecturedByShiftType(type, executionPeriodList);
        }

        return totalHoursLectured;
    }

    public Double getRequiredHours() {
        return tsdTeacherList.iterator().next().getRequiredHours(executionPeriodList);
    }

    public Double getAvailability() {
        return getRequiredHours() - getTotalHoursLecturedPlusExtraCredits();
    }

    public Double getServiceExemptionCredits() {
        return tsdTeacherList.iterator().next().getServiceExemptionCredits(executionPeriodList);
    }

    public Double getManagementFunctionsCredits() {
        return tsdTeacherList.iterator().next().getManagementFunctionsCredits(executionPeriodList);
    }

    public Double getRequiredTeachingHours() {
        return getRequiredHours() - getExtraCreditsValue();
    }

    public void addExecutionPeriodList(List<ExecutionSemester> executionPeriodList) {
        for (ExecutionSemester executionSemester : executionPeriodList) {
            if (!this.executionPeriodList.contains(executionSemester)) {
                this.executionPeriodList.add(executionSemester);
            }
        }
    }

    public void addTSDTeacher(TSDTeacher tsdTeacher) {
        this.tsdTeacherList.add(tsdTeacher);
    }

    public List<TSDTeacher> getTSDTeachers() {
        return tsdTeacherList;
    }

    public List<PersonContractSituation> getServiceExemptions() {
        return tsdTeacherList.iterator().next().getServiceExemptions(executionPeriodList);
    }

    public List<PersonFunction> getManagementFunctions() {
        return tsdTeacherList.iterator().next().getManagementFunctions(executionPeriodList);
    }

    public TSDProfessorshipDTOEntry getTSDProfessorshipDTOEntryByTSDCourseDTOEntry(final TSDCourseDTOEntry tsdCourseDTOEntry) {
        return (TSDProfessorshipDTOEntry) CollectionUtils.find(getTSDProfessorshipDTOEntries(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                TSDProfessorshipDTOEntry tsdProfessorshipDTOEntry = (TSDProfessorshipDTOEntry) arg0;
                return tsdProfessorshipDTOEntry.getTSDCourseDTOEntry().getTSDCourse() == tsdCourseDTOEntry.getTSDCourse();
            }

        });
    }

    public String getAcronym() {
        return tsdTeacherList.iterator().next().getAcronym();
    }

    public Double getTotalHoursLecturedPlusExtraCredits() {
        return tsdTeacherList.iterator().next().getTotalHoursLecturedPlusExtraCredits(executionPeriodList);
    }

    public Boolean getUsingExtraCredits() {
        return tsdTeacherList.iterator().next().getUsingExtraCredits();
    }

    public String getExtraCreditsName() {
        return tsdTeacherList.iterator().next().getExtraCreditsName();
    }

    public Double getExtraCreditsValue() {
        return tsdTeacherList.iterator().next().getExtraCreditsValue();
    }

    public String getEmailUserId() {
        return tsdTeacherList.iterator().next().getEmailUserId();
    }

    public String getShortName() {
        return tsdTeacherList.iterator().next().getShortName();
    }

}
