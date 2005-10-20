package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

/**
 * 
 * @author naat
 * 
 */
public class ViewDepartmentTeachers extends FenixBackingBean {

    private Integer selectedExecutionYearID;

    private InfoTeacher selectedTeacher;

    private HtmlInputHidden selectedExecutionYearIdHidden;

    private List<IExecutionCourse> lecturedDegreeExecutionCourses;

    private Map<Integer, String> lecturedDegreeExecutionCourseDegreeNames;

    private List<IExecutionCourse> lecturedMasterDegreeExecutionCourses;

    private Map<Integer, String> lecturedMasterDegreeExecutionCourseDegreeNames;

    private List<IProposal> finalDegreeWorks;

    private List<IMasterDegreeThesisDataVersion> guidedMasterDegreeThesisList;

    public HtmlInputHidden getSelectedExecutionYearIdHidden() {
        if (this.selectedExecutionYearIdHidden == null) {
            this.selectedExecutionYearIdHidden = new HtmlInputHidden();
            this.selectedExecutionYearIdHidden.setValue(this.selectedExecutionYearID);
        }

        return selectedExecutionYearIdHidden;
    }

    public void setSelectedExecutionYearIdHidden(HtmlInputHidden selectedExecutionYearIdHidden) {
        if (selectedExecutionYearIdHidden != null) {
            this.selectedExecutionYearID = Integer.valueOf(selectedExecutionYearIdHidden.getValue()
                    .toString());
        }

        this.selectedExecutionYearIdHidden = selectedExecutionYearIdHidden;
    }

    public Integer getSelectedExecutionYearID() {
        return selectedExecutionYearID;
    }

    public void setSelectedExecutionYearID(Integer selectedExecutionYearID) {
        this.selectedExecutionYearID = selectedExecutionYearID;
    }

    public List getDepartmentTeachers() throws FenixFilterException, FenixServiceException {

        InfoTeacher infoTeacher = (InfoTeacher) ServiceUtils.executeService(getUserView(),
                "ReadTeacherByUsername", new Object[] { this.getUserView().getUtilizador() });

        InfoDepartment infoDepartment = (InfoDepartment) ServiceUtils.executeService(getUserView(),
                "ReadDepartmentByTeacher", new Object[] { infoTeacher });

        List<InfoTeacher> result = (List<InfoTeacher>) ServiceUtils.executeService(getUserView(),
                "ReadDepartmentTeachersByDepartmentID", new Object[] { infoDepartment.getIdInternal() });

        return result;
    }

    public void selectTeacher(ActionEvent event) throws NumberFormatException, FenixFilterException,
            FenixServiceException {
        UIParameter parameter = (UIParameter) event.getComponent().findComponent("teacherId");

        this.selectedTeacher = (InfoTeacher) ServiceUtils.executeService(getUserView(),
                "ReadTeacherByOID", new Object[] { Integer.valueOf(parameter.getValue().toString()) });
    }

    public InfoTeacher getSelectedTeacher() throws FenixFilterException, FenixServiceException {
        return this.selectedTeacher;
    }

    public List<SelectItem> getExecutionYears() throws FenixFilterException, FenixServiceException {

        List<InfoExecutionYear> executionYears = (List<InfoExecutionYear>) ServiceUtils.executeService(
                getUserView(), "ReadNotClosedExecutionYears", null);

        List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
        for (InfoExecutionYear executionYear : executionYears) {
            result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
        }

        if (this.getSelectedExecutionYearID() == null) {
            this.setSelectedExecutionYearID((Integer) result.get(result.size() - 1).getValue());
        }

        return result;

    }

    public List<IExecutionCourse> getLecturedDegreeExecutionCourses() throws FenixFilterException,
            FenixServiceException {

        if (this.lecturedDegreeExecutionCourses == null) {
            this.lecturedDegreeExecutionCourses = readLecturedExecutionCourses(DegreeType.DEGREE);
            this.lecturedDegreeExecutionCourseDegreeNames = computeExecutionCoursesDegreeAcronyms(this.lecturedDegreeExecutionCourses);

        }

        return this.lecturedDegreeExecutionCourses;
    }

    public List<IExecutionCourse> getLecturedMasterDegreeExecutionCourses() throws FenixFilterException,
            FenixServiceException {

        if (this.lecturedMasterDegreeExecutionCourses == null) {
            this.lecturedMasterDegreeExecutionCourses = readLecturedExecutionCourses(DegreeType.MASTER_DEGREE);
            this.lecturedMasterDegreeExecutionCourseDegreeNames = computeExecutionCoursesDegreeAcronyms(this.lecturedMasterDegreeExecutionCourses);

        }

        return this.lecturedMasterDegreeExecutionCourses;
    }

    public Map<Integer, String> getLecturedDegreeExecutionCourseDegreeNames() {
        return lecturedDegreeExecutionCourseDegreeNames;
    }

    public Map<Integer, String> getLecturedMasterDegreeExecutionCourseDegreeNames() {
        return lecturedMasterDegreeExecutionCourseDegreeNames;
    }

    private List<IExecutionCourse> readLecturedExecutionCourses(DegreeType degreeType)
            throws FenixFilterException, FenixServiceException {

        List<IExecutionCourse> lecturedExecutionCourses = (List<IExecutionCourse>) ServiceUtils
                .executeService(getUserView(),
                        "ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType",
                        new Object[] { this.selectedTeacher.getIdInternal(),
                                this.selectedExecutionYearID, degreeType });

        return lecturedExecutionCourses;
    }

    private Map<Integer, String> computeExecutionCoursesDegreeAcronyms(
            List<IExecutionCourse> executionCourses) {
        Map<Integer, String> result = new HashMap<Integer, String>();

        for (IExecutionCourse executionCourse : executionCourses) {
            String degreeAcronyns = computeDegreeAcronyms(executionCourse);
            result.put(executionCourse.getIdInternal(), degreeAcronyns);
        }

        return result;
    }

    private String computeDegreeAcronyms(IExecutionCourse executionCourse) {
        StringBuilder degreeAcronyms = new StringBuilder();

        List<ICurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        Set<String> processedAcronyns = new HashSet<String>();

        for (ICurricularCourse curricularCourse : curricularCourses) {
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

    public List<IProposal> getFinalDegreeWorks() throws FenixFilterException, FenixServiceException {

        if (this.finalDegreeWorks == null) {
            this.finalDegreeWorks = (List<IProposal>) ServiceUtils.executeService(getUserView(),
                    "ReadFinalDegreeWorksByTeacherIDAndExecutionYearID", new Object[] {
                            this.selectedTeacher.getIdInternal(), this.selectedExecutionYearID });
        }

        return this.finalDegreeWorks;
    }

    public List<IMasterDegreeThesisDataVersion> getGuidedMasterDegreeThesisList()
            throws FenixFilterException, FenixServiceException {
        if (this.guidedMasterDegreeThesisList == null) {
            this.guidedMasterDegreeThesisList = (List<IMasterDegreeThesisDataVersion>) ServiceUtils
                    .executeService(getUserView(),
                            "ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID", new Object[] {
                                    this.selectedTeacher.getIdInternal(), this.selectedExecutionYearID });
        }

        return this.guidedMasterDegreeThesisList;
    }

}
