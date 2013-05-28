package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ReadDepartmentTeachersByDepartmentIDAndExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.function.ReadPersonFunctionsByPersonIDAndExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadTeacherByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.advise.ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 
 * @author naat
 * 
 */
public class ViewDepartmentTeachers extends FenixBackingBean {

    private Integer selectedExecutionYearID;

    private InfoTeacher selectedTeacher;

    private List<ExecutionCourse> lecturedDegreeExecutionCourses;

    private Map<Integer, String> lecturedDegreeExecutionCourseDegreeNames;

    private List<ExecutionCourse> lecturedMasterDegreeExecutionCourses;

    private Map<Integer, String> lecturedMasterDegreeExecutionCourseDegreeNames;

    private List<MasterDegreeThesisDataVersion> guidedMasterDegreeThesisList;

    private List<SelectItem> executionYearItems;

    private List<Advise> finalDegreeWorkAdvises;

    private List<PersonFunction> teacherFunctions;

    private ResourceBundle bundle;

    private static final String BUNDLE_NAME = "resources/DepartmentMemberResources";

    private static final String ALL_EXECUTION_YEARS_KEY = "label.common.allExecutionYears";

    public ResourceBundle getBundle() {

        if (this.bundle == null) {
            this.bundle = getResourceBundle(BUNDLE_NAME);
        }

        return this.bundle;
    }

    public Integer getSelectedTeacherID() {

        return (Integer) this.getViewState().getAttribute("selectedTeacherID");

    }

    public void setSelectedTeacherID(String selectedTeacherId) {
        this.getViewState().setAttribute("selectedTeacherId", selectedTeacherId);
    }

    public Integer getSelectedExecutionYearID() throws  FenixServiceException {

        if (this.selectedExecutionYearID == null) {

            InfoExecutionYear infoExecutionYear = ReadCurrentExecutionYear.run();

            if (infoExecutionYear != null) {
                this.selectedExecutionYearID = infoExecutionYear.getExternalId();
            } else {
                this.selectedExecutionYearID = 0;
            }
        }

        return this.selectedExecutionYearID;
    }

    public void setSelectedExecutionYearID(Integer selectedExecutionYearID) {
        this.selectedExecutionYearID = selectedExecutionYearID;
    }

    public List<Teacher> getDepartmentTeachers() throws  FenixServiceException {
        Integer executionYearID = getSelectedExecutionYearID();

        if (executionYearID == 0) {
            executionYearID = null;
        }

        List<Teacher> result =
                new ArrayList<Teacher>(
                        ReadDepartmentTeachersByDepartmentIDAndExecutionYearID
                                .runReadDepartmentTeachersByDepartmentIDAndExecutionYearID(getDepartment().getExternalId(),
                                        executionYearID));

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("teacherId"));

        Collections.sort(result, comparatorChain);

        return result;
    }

    public Department getDepartment() {
        return getUserView().getPerson().getTeacher().getLastWorkingDepartment();

    }

    public void selectTeacher(ActionEvent event) throws NumberFormatException,  FenixServiceException {

        String teacherId = getRequestParameter("teacherId");

        setSelectedTeacherID(teacherId);
    }

    public InfoTeacher getSelectedTeacher() throws  FenixServiceException {

        if (this.selectedTeacher == null) {
            this.selectedTeacher = (InfoTeacher) ReadTeacherByOID.runReadTeacherByOID(getSelectedTeacherID());
        }

        return this.selectedTeacher;
    }

    public List<SelectItem> getExecutionYears() throws  FenixServiceException {

        if (this.executionYearItems == null) {

            List<InfoExecutionYear> executionYears = ReadNotClosedExecutionYears.run();

            List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
            for (InfoExecutionYear executionYear : executionYears) {
                result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
            }

            result.add(0, new SelectItem(0, getBundle().getString(ALL_EXECUTION_YEARS_KEY)));

            this.executionYearItems = result;
        }

        return this.executionYearItems;

    }

    public List<ExecutionCourse> getLecturedDegreeExecutionCourses() throws  FenixServiceException {

        if (this.lecturedDegreeExecutionCourses == null && this.getSelectedExecutionYearID() != null) {
            this.lecturedDegreeExecutionCourses = readLecturedExecutionCourses(DegreeType.DEGREE);
            this.lecturedDegreeExecutionCourseDegreeNames =
                    computeExecutionCoursesDegreeAcronyms(this.lecturedDegreeExecutionCourses);

        }

        return this.lecturedDegreeExecutionCourses;
    }

    public List<ExecutionCourse> getLecturedMasterDegreeExecutionCourses() throws  FenixServiceException {

        if (this.lecturedMasterDegreeExecutionCourses == null && this.getSelectedExecutionYearID() != null) {
            this.lecturedMasterDegreeExecutionCourses = readLecturedExecutionCourses(DegreeType.MASTER_DEGREE);
            this.lecturedMasterDegreeExecutionCourseDegreeNames =
                    computeExecutionCoursesDegreeAcronyms(this.lecturedMasterDegreeExecutionCourses);

        }

        return this.lecturedMasterDegreeExecutionCourses;
    }

    public Map<Integer, String> getLecturedDegreeExecutionCourseDegreeNames() {
        return lecturedDegreeExecutionCourseDegreeNames;
    }

    public Map<Integer, String> getLecturedMasterDegreeExecutionCourseDegreeNames() {
        return lecturedMasterDegreeExecutionCourseDegreeNames;
    }

    private List<ExecutionCourse> readLecturedExecutionCourses(DegreeType degreeType) throws 
            FenixServiceException {

        Integer executionYearID = getSelectedExecutionYearID();

        if (executionYearID == 0) {
            executionYearID = null;
        }

        List<ExecutionCourse> lecturedExecutionCourses =
                ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType
                        .runReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType(getSelectedTeacherID(),
                                executionYearID, degreeType);

        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();

        result.addAll(lecturedExecutionCourses);

        ComparatorChain comparatorChain = new ComparatorChain();
        BeanComparator executionYearComparator = new BeanComparator("executionPeriod.executionYear.year");
        BeanComparator semesterComparator = new BeanComparator("executionPeriod.semester");

        comparatorChain.addComparator(executionYearComparator);
        comparatorChain.addComparator(semesterComparator);

        Collections.sort(result, comparatorChain);

        return result;
    }

    private Map<Integer, String> computeExecutionCoursesDegreeAcronyms(List<ExecutionCourse> executionCourses) {
        Map<Integer, String> result = new HashMap<Integer, String>();

        for (ExecutionCourse executionCourse : executionCourses) {
            String degreeAcronyns = computeDegreeAcronyms(executionCourse);
            result.put(executionCourse.getExternalId(), degreeAcronyns);
        }

        return result;
    }

    private String computeDegreeAcronyms(ExecutionCourse executionCourse) {
        StringBuilder degreeAcronyms = new StringBuilder();

        List<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        Set<String> processedAcronyns = new HashSet<String>();

        for (CurricularCourse curricularCourse : curricularCourses) {
            String degreeAcronym = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();

            if (!processedAcronyns.contains(degreeAcronym)) {
                degreeAcronyms.append(degreeAcronym).append(",");
                processedAcronyns.add(degreeAcronym);

            }
        }

        if (degreeAcronyms.toString().endsWith(",")) {
            degreeAcronyms.deleteCharAt(degreeAcronyms.length() - 1);
        }

        return degreeAcronyms.toString();

    }

    public List<Advise> getFinalDegreeWorkAdvises() throws  FenixServiceException {

        if (this.finalDegreeWorkAdvises == null && this.getSelectedExecutionYearID() != null) {
            Integer executionYearID = this.getSelectedExecutionYearID();

            if (executionYearID == 0) {
                executionYearID = null;
            }

            List<Advise> result =
                    new ArrayList<Advise>(
                            ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID
                                    .runReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID(
                                            AdviseType.FINAL_WORK_DEGREE, getSelectedTeacherID(), executionYearID));

            ComparatorChain comparatorChain = new ComparatorChain();
            BeanComparator executionYearComparator = new BeanComparator("student.number");

            comparatorChain.addComparator(executionYearComparator);

            Collections.sort(result, comparatorChain);

            this.finalDegreeWorkAdvises = result;

        }
        return this.finalDegreeWorkAdvises;
    }

    public List<MasterDegreeThesisDataVersion> getGuidedMasterDegreeThesisList() throws 
            FenixServiceException {
        if (this.guidedMasterDegreeThesisList == null && this.getSelectedExecutionYearID() != null) {
            Integer executionYearID = this.getSelectedExecutionYearID();

            if (executionYearID == 0) {
                executionYearID = null;
            }

            this.guidedMasterDegreeThesisList =
                    ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID
                            .runReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID(getSelectedTeacherID(), executionYearID);
        }

        return this.guidedMasterDegreeThesisList;
    }

    public List<PersonFunction> getTeacherFunctions() throws  FenixServiceException {
        if (this.teacherFunctions == null && this.getSelectedExecutionYearID() != null) {
            Integer executionYearID = this.getSelectedExecutionYearID();

            if (executionYearID == 0) {
                executionYearID = null;
            }

            Teacher teacher = rootDomainObject.readTeacherByOID(getSelectedTeacherID());

            List<PersonFunction> result =
                    new ArrayList<PersonFunction>(ReadPersonFunctionsByPersonIDAndExecutionYearID.run(teacher.getPerson()
                            .getExternalId(), executionYearID));

            ComparatorChain comparatorChain = new ComparatorChain();
            BeanComparator beginDateComparator = new BeanComparator("beginDate");

            comparatorChain.addComparator(beginDateComparator);

            Collections.sort(result, comparatorChain);

            this.teacherFunctions = result;
        }

        return this.teacherFunctions;

    }

    public void onSelectedExecutionYearChanged(ValueChangeEvent valueChangeEvent) {
        setSelectedExecutionYearID((Integer) valueChangeEvent.getNewValue());
        this.lecturedDegreeExecutionCourses = null;
        this.lecturedMasterDegreeExecutionCourses = null;
        this.finalDegreeWorkAdvises = null;
        this.guidedMasterDegreeThesisList = null;
        this.teacherFunctions = null;
    }

}