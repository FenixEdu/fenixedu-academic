package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.SetTSDCourse;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularLoad;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class TSDCourseValuationAction extends FenixDispatchAction {

    public ActionForward prepareForTSDCourseValuation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        IUserView userView = UserView.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        getFromRequestAndSetOnForm(request, dynaForm);
        TSDCourse tsdCourse = getSelectedTSDCourse(dynaForm);

        initializeVariables(userView.getPerson(), dynaForm, tsdCourse);
        return loadTSDCourses(mapping, form, request, response, request.getParameter("forward"));
    }

    private void initializeVariables(Person person, DynaActionForm dynaForm, TSDCourse course) {

        dynaForm.set("tsdCourseType", getTSDCourseType(course).toString());
    }

    private TSDCourseType getTSDCourseType(TSDCourse course) {
        if (course instanceof TSDCompetenceCourse) {
            return TSDCourseType.COMPETENCE_COURSE_VALUATION;
        } else if (course instanceof TSDCurricularCourse) {
            return TSDCourseType.CURRICULAR_COURSE_VALUATION;
        } else if (course instanceof TSDCurricularCourseGroup) {
            return TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP;
        }

        return TSDCourseType.NOT_DETERMINED;
    }

    private void getFromRequestAndSetOnForm(HttpServletRequest request, DynaActionForm dynaForm) {
        dynaForm.set("tsdCourse", request.getParameter("tsdCourse"));
        dynaForm.set("tsd", request.getParameter("tsd"));

        String typeStr = (String) request.getAttribute("shiftType");

        if (typeStr != null) {
            dynaForm.set("shiftType", typeStr);
        }
    }

    private TSDCourse getSelectedTSDCourse(DynaActionForm dynaForm) throws FenixServiceException {
        return AbstractDomainObject.fromExternalId((String) dynaForm.get("tsdCourse"));
    }

    private TeacherServiceDistribution getSelectedTSD(DynaActionForm dynaForm) throws FenixServiceException {
        return AbstractDomainObject.fromExternalId((String) dynaForm.get("tsd"));
    }

    public ActionForward loadTSDCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return loadTSDCourses(mapping, form, request, response, (String) ((DynaActionForm) form).get("forward"));
    }

    @SuppressWarnings("unchecked")
    private ActionForward loadTSDCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String forward) throws FenixServiceException {
        DynaActionForm dynaForm = (DynaActionForm) form;

        TSDCourse selectedTSDCourse = getSelectedTSDCourse(dynaForm);
        ShiftType shiftType = getSelectedShiftType(dynaForm, selectedTSDCourse);

        if (request.getParameter("fromValidation") == null) {
            fillFormWithTSDCourseParameters(dynaForm, selectedTSDCourse, shiftType);
        }

        List<ShiftType> shiftsList =
                (List<ShiftType>) CollectionUtils.collect(selectedTSDCourse.getTSDCurricularLoads(), new Transformer() {
                    @Override
                    final public Object transform(Object arg0) {
                        TSDCurricularLoad tsdLoad = (TSDCurricularLoad) arg0;
                        return tsdLoad.getType();
                    }
                });

        request.setAttribute("selectedTSDCourse", selectedTSDCourse);
        request.setAttribute("selectedTSDCourseType", getTSDCourseType(selectedTSDCourse).toString());
        request.setAttribute("selectedTSDLoad", selectedTSDCourse.getTSDCurricularLoadByShiftType(shiftType));
        request.setAttribute("selectedTSD", getSelectedTSD(dynaForm));
        request.setAttribute("tsdProcess", selectedTSDCourse.getTSDProcessPhase().getTSDProcess());
        request.setAttribute("shiftType", shiftType);
        request.setAttribute("shiftsList", shiftsList);

        return mapping.findForward(forward);
    }

    private void fillFormWithTSDCourseParameters(DynaActionForm dynaForm, TSDCourse selectedTSDCourse, ShiftType shiftType) {

        Integer firstTimeEnrolledManualValue = selectedTSDCourse.getFirstTimeEnrolledStudents();
        if (firstTimeEnrolledManualValue == null || firstTimeEnrolledManualValue == 0) {
            firstTimeEnrolledManualValue = selectedTSDCourse.getRealFirstTimeEnrolledStudentsNumberLastYear();
        }
        Integer secondTimeEnrolledManualValue = selectedTSDCourse.getSecondTimeEnrolledStudents();
        if (secondTimeEnrolledManualValue == null || secondTimeEnrolledManualValue == 0) {
            secondTimeEnrolledManualValue = selectedTSDCourse.getRealSecondTimeEnrolledStudentsNumberLastYear();
        }

        dynaForm.set("firstTimeEnrolledStudentsManual", firstTimeEnrolledManualValue.toString());
        dynaForm.set("firstTimeEnrolledStudentsType", selectedTSDCourse.getFirstTimeEnrolledStudentsType().toString());
        dynaForm.set("secondTimeEnrolledStudentsManual", secondTimeEnrolledManualValue.toString());
        dynaForm.set("secondTimeEnrolledStudentsType", selectedTSDCourse.getSecondTimeEnrolledStudentsType().toString());

        if (shiftType != null) {
            TSDCurricularLoad tsdLoad = selectedTSDCourse.getTSDCurricularLoadByShiftType(shiftType);

            dynaForm.set("studentsPerShiftManual", tsdLoad.getStudentsPerShift().toString());
            dynaForm.set("studentsPerShiftType", tsdLoad.getStudentsPerShiftType().toString());
            dynaForm.set("shiftFrequency", tsdLoad.getFrequency().toString());

            dynaForm.set("weightFirstTimeEnrolledStudentsPerShiftManual", tsdLoad.getWeightFirstTimeEnrolledStudentsPerShift()
                    .toString());
            dynaForm.set("weightFirstTimeEnrolledStudentsPerShiftType", tsdLoad.getWeightFirstTimeEnrolledStudentsPerShiftType()
                    .toString());
            dynaForm.set("weightSecondTimeEnrolledStudentsPerShiftManual", tsdLoad.getWeightSecondTimeEnrolledStudentsPerShift()
                    .toString());
            dynaForm.set("weightSecondTimeEnrolledStudentsPerShiftType", tsdLoad
                    .getWeightSecondTimeEnrolledStudentsPerShiftType().toString());

            Double manualHours = tsdLoad.getHours();
            if (manualHours == null || manualHours == 0) {
                manualHours = selectedTSDCourse.getHoursCalculated(shiftType);
            }
            dynaForm.set("hoursManual", manualHours.toString());
            dynaForm.set("hoursType", tsdLoad.getHoursType().toString());
            dynaForm.set("hoursPerShift", tsdLoad.getHoursPerShift().toString());
            dynaForm.set("timeTableSlots", tsdLoad.getTimeTableSlotsNumber().toString());
        }
    }

    private ShiftType getSelectedShiftType(DynaActionForm dynaForm, TSDCourse course) {
        if (dynaForm.get("shiftType") == null || dynaForm.get("shiftType").equals("")) {
            if (course.getTSDCurricularLoadsCount() > 0) {
                return course.getTSDCurricularLoads().get(0).getType();
            } else {
                return null;
            }
        }

        return ShiftType.valueOf((String) dynaForm.get("shiftType"));
    }

    public ActionForward setTSDCourseStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        IUserView userView = UserView.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TSDCourse tsdCourse = getSelectedTSDCourse(dynaForm);
        Map<String, Object> tsdCourseParameters = obtainStudentsParametersFromForm(dynaForm);

        SetTSDCourse.runSetTSDCourse(tsdCourse.getExternalId(), tsdCourseParameters);

        return loadTSDCourses(mapping, form, request, response, "courseValuationStudents");
    }

    private Map<String, Object> obtainStudentsParametersFromForm(DynaActionForm dynaForm) {
        Map<String, Object> tsdParameters = new HashMap<String, Object>();

        tsdParameters.put("firstTimeEnrolledStudentsManual",
                Integer.parseInt((String) dynaForm.get("firstTimeEnrolledStudentsManual")));
        tsdParameters.put("firstTimeEnrolledStudentsType", dynaForm.get("firstTimeEnrolledStudentsType"));
        tsdParameters.put("secondTimeEnrolledStudentsManual",
                Integer.parseInt((String) dynaForm.get("secondTimeEnrolledStudentsManual")));
        tsdParameters.put("secondTimeEnrolledStudentsType", dynaForm.get("secondTimeEnrolledStudentsType"));

        return tsdParameters;
    }

    /*
     * public ActionForward removeTSDCourseLoad( ActionMapping mapping,
     * ActionForm form, HttpServletRequest request, HttpServletResponse
     * response) throws  FenixServiceException { IUserView
     * userView = UserView.getUser(); DynaActionForm dynaForm = (DynaActionForm)
     * form;
     * 
     * TSDCourse tsdCourse = getSelectedTSDCourse(dynaForm); Map<String, Object>
     * tsdCourseParameters = obtainRemoveParametersFromForm(dynaForm);
     * 
     * Object[] parameters = new Object[] { tsdCourse.getExternalId(),
     * tsdCourseParameters }; ServiceUtils.executeService( "SetTSDCourse",
     * parameters);
     * 
     * dynaForm.set("shiftType", "");
     * 
     * return loadTSDCourses(mapping, form, request, response,
     * "courseValuationShifts"); }
     * 
     * private Map<String, Object> obtainRemoveParametersFromForm(DynaActionForm
     * dynaForm) { Map<String, Object> tsdParameters = new HashMap<String,
     * Object>();
     * 
     * tsdParameters.put("removeTSDLoad", true); tsdParameters.put("shiftType",
     * dynaForm.get("shiftType"));
     * 
     * return tsdParameters; }
     */

    public ActionForward setTSDCourseWeights(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        IUserView userView = UserView.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TSDCourse tsdCourse = getSelectedTSDCourse(dynaForm);
        Map<String, Object> tsdCourseParameters = obtainWeightsParametersFromForm(dynaForm);

        SetTSDCourse.runSetTSDCourse(tsdCourse.getExternalId(), tsdCourseParameters);

        return loadTSDCourses(mapping, form, request, response, "courseValuationWeights");
    }

    private Map<String, Object> obtainWeightsParametersFromForm(DynaActionForm dynaForm) {
        Map<String, Object> tsdParameters = new HashMap<String, Object>();

        tsdParameters.put("weightFirstTimeEnrolledStudentsPerShiftManual",
                Double.parseDouble((String) dynaForm.get("weightFirstTimeEnrolledStudentsPerShiftManual")));
        tsdParameters.put("weightFirstTimeEnrolledStudentsPerShiftType",
                dynaForm.get("weightFirstTimeEnrolledStudentsPerShiftType"));
        tsdParameters.put("weightSecondTimeEnrolledStudentsPerShiftManual",
                Double.parseDouble((String) dynaForm.get("weightSecondTimeEnrolledStudentsPerShiftManual")));
        tsdParameters.put("weightSecondTimeEnrolledStudentsPerShiftType",
                dynaForm.get("weightSecondTimeEnrolledStudentsPerShiftType"));
        tsdParameters.put("shiftType", dynaForm.get("shiftType"));

        return tsdParameters;
    }

    public ActionForward setTSDCourseHours(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        IUserView userView = UserView.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TSDCourse tsdCourse = getSelectedTSDCourse(dynaForm);
        Map<String, Object> tsdCourseParameters = obtainHoursParametersFromForm(dynaForm);

        SetTSDCourse.runSetTSDCourse(tsdCourse.getExternalId(), tsdCourseParameters);

        return loadTSDCourses(mapping, form, request, response, "courseValuationHours");
    }

    private Map<String, Object> obtainHoursParametersFromForm(DynaActionForm dynaForm) {
        Map<String, Object> tsdParameters = new HashMap<String, Object>();
        tsdParameters.put("hoursManual", Double.parseDouble((String) dynaForm.get("hoursManual")));
        tsdParameters.put("hoursType", dynaForm.get("hoursType"));
        tsdParameters.put("shiftType", dynaForm.get("shiftType"));
        tsdParameters.put("hoursPerShift", Double.parseDouble((String) dynaForm.get("hoursPerShift")));
        // tsdParameters.put("timeTableSlots", Integer.parseInt((String)
        // dynaForm.get("timeTableSlots")));
        tsdParameters.put("studentsPerShiftManual", Integer.parseInt((String) dynaForm.get("studentsPerShiftManual")));
        tsdParameters.put("studentsPerShiftType", dynaForm.get("studentsPerShiftType"));
        tsdParameters.put("shiftFrequency", Double.parseDouble((String) dynaForm.get("shiftFrequency")));

        return tsdParameters;
    }

    public ActionForward loadTSDCurricularLoadForShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        return loadTSDCourses(mapping, form, request, response, "courseValuationShifts");
    }

    public ActionForward loadTSDCurricularLoadForWeights(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        return loadTSDCourses(mapping, form, request, response, "courseValuationWeights");
    }

    public ActionForward loadTSDCurricularLoadForHours(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        return loadTSDCourses(mapping, form, request, response, "courseValuationHours");
    }

    public ActionForward defineSchoolClassCalculationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        TeacherServiceDistribution tsd = AbstractDomainObject.fromExternalId(request.getParameter("tsd"));
        TSDCourse tsdCourse = AbstractDomainObject.fromExternalId(request.getParameter("tsdCourse"));
        TSDProcess process = AbstractDomainObject.fromExternalId(request.getParameter("tsdProcessId"));
        ShiftType type = ShiftType.valueOf(request.getParameter("shiftType"));

        request.setAttribute("curricularLoad", tsdCourse.getTSDCurricularLoadByShiftType(type));
        request.setAttribute("tsd", tsd);
        request.setAttribute("tsdCourse", tsdCourse);
        request.setAttribute("tsdProcess", process);

        return mapping.findForward("defineSchoolClassCalculationMethod");
    }

}
