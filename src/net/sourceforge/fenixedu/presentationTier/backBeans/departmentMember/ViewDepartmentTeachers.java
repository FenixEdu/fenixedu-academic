package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
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

    private HtmlInputHidden selectedExecutionYearIdHidden;

    private List<IExecutionCourse> lecturedDegreeExecutionCourses;

    private Map<Integer, String> lecturedDegreeExecutionCourseDegreeNames;

    private List<IExecutionCourse> lecturedMasterDegreeExecutionCourses;

    private Map<Integer, String> lecturedMasterDegreeExecutionCourseDegreeNames;

    private List<IMasterDegreeThesisDataVersion> guidedMasterDegreeThesisList;

    private List<SelectItem> executionYearItems;
    
    //private List<IProposal> finalDegreeWorks;

    private ResourceBundle bundle;

    private static final String BUNDLE_NAME = "ServidorApresentacao/DepartmentMemberResources";

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

    public void setSelectedTeacherID(Integer selectedTeacherID) {
        this.getViewState().setAttribute("selectedTeacherID", selectedTeacherID);
    }

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

    public Integer getSelectedExecutionYearID() throws FenixFilterException, FenixServiceException {

        if (this.selectedExecutionYearID == null && this.getExecutionYears().size() != 0) {
            this.selectedExecutionYearID = (Integer) this.getExecutionYears().get(
                    this.getExecutionYears().size() - 1).getValue();

        }
        return selectedExecutionYearID;
    }

    public void setSelectedExecutionYearID(Integer selectedExecutionYearID) {
        this.selectedExecutionYearID = selectedExecutionYearID;
    }

    public List getDepartmentTeachers() throws FenixFilterException, FenixServiceException {               
        List<ITeacher> result = new ArrayList<ITeacher>(getUserView().getPerson().getTeacher().getLastWorkingDepartment().getTeachers());

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("teacherNumber"));

        Collections.sort(result, comparatorChain);

        return result;
    }

    public void selectTeacher(ActionEvent event) throws NumberFormatException, FenixFilterException,
            FenixServiceException {

        Integer teacherID = Integer.valueOf(getRequestParameter("teacherID"));

        setSelectedTeacherID(teacherID);
    }

    public InfoTeacher getSelectedTeacher() throws FenixFilterException, FenixServiceException {

        if (this.selectedTeacher == null) {
            this.selectedTeacher = (InfoTeacher) ServiceUtils.executeService(getUserView(),
                    "ReadTeacherByOID", new Object[] { getSelectedTeacherID() });
        }

        return this.selectedTeacher;
    }

    public List<SelectItem> getExecutionYears() throws FenixFilterException, FenixServiceException {

        if (this.executionYearItems == null) {

            List<InfoExecutionYear> executionYears = (List<InfoExecutionYear>) ServiceUtils
                    .executeService(getUserView(), "ReadNotClosedExecutionYears", null);

            List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
            for (InfoExecutionYear executionYear : executionYears) {
                result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
            }

            result.add(0, new SelectItem(0, getBundle().getString(ALL_EXECUTION_YEARS_KEY)));

            /*
             * if (this.getSelectedExecutionYearID() == null) {
             * this.setSelectedExecutionYearID((Integer)
             * result.get(result.size() - 1).getValue()); }
             */
            this.executionYearItems = result;
        }

        return this.executionYearItems;

    }

    public List<IExecutionCourse> getLecturedDegreeExecutionCourses() throws FenixFilterException,
            FenixServiceException {

        if (this.lecturedDegreeExecutionCourses == null && this.getSelectedExecutionYearID() != null) {
            this.lecturedDegreeExecutionCourses = readLecturedExecutionCourses(DegreeType.DEGREE);
            this.lecturedDegreeExecutionCourseDegreeNames = computeExecutionCoursesDegreeAcronyms(this.lecturedDegreeExecutionCourses);

        }

        return this.lecturedDegreeExecutionCourses;
    }

    public List<IExecutionCourse> getLecturedMasterDegreeExecutionCourses() throws FenixFilterException,
            FenixServiceException {

        if (this.lecturedMasterDegreeExecutionCourses == null
                && this.getSelectedExecutionYearID() != null) {
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

        Integer executionYearID = getSelectedExecutionYearID();

        if (executionYearID == 0) {
            executionYearID = null;
        }

        List<IExecutionCourse> lecturedExecutionCourses = (List<IExecutionCourse>) ServiceUtils
                .executeService(getUserView(),
                        "ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType",
                        new Object[] { getSelectedTeacherID(), executionYearID, degreeType });

        List<IExecutionCourse> result = new ArrayList<IExecutionCourse>();

        result.addAll(lecturedExecutionCourses);

        BeanComparator comparator = new BeanComparator("executionPeriod.executionYear.year");
        Collections.sort(result, comparator);
        Collections.reverse(result);

        return result;
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

        /*
         * if (this.finalDegreeWorks == null &&
         * this.getSelectedExecutionYearID() != null) { this.finalDegreeWorks =
         * (List<IProposal>) ServiceUtils.executeService(getUserView(),
         * "ReadFinalDegreeWorksByTeacherIDAndExecutionYearID", new Object[] {
         * this.getSelectedTeacherID(), getSelectedExecutionYearID() }); }
         * 
         * return this.finalDegreeWorks;
         */

        // TODO: waiting for ricardo code
        return new ArrayList<IProposal>();
    }

    public List<IMasterDegreeThesisDataVersion> getGuidedMasterDegreeThesisList()
            throws FenixFilterException, FenixServiceException {
        if (this.guidedMasterDegreeThesisList == null && this.getSelectedExecutionYearID() != null) {
            Integer executionYearID = this.getSelectedExecutionYearID();

            if (executionYearID == 0) {
                executionYearID = null;
            }

            this.guidedMasterDegreeThesisList = (List<IMasterDegreeThesisDataVersion>) ServiceUtils
                    .executeService(getUserView(),
                            "ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID", new Object[] {
                                    getSelectedTeacherID(), executionYearID });
        }

        return this.guidedMasterDegreeThesisList;
    }

    public void onSelectedExecutionYearChanged(ValueChangeEvent valueChangeEvent) {
        setSelectedExecutionYearID((Integer) valueChangeEvent.getNewValue());
        this.lecturedDegreeExecutionCourses = null;
        this.lecturedMasterDegreeExecutionCourses = null;
        //this.finalDegreeWorks = null;
        this.guidedMasterDegreeThesisList = null;
    }

}
