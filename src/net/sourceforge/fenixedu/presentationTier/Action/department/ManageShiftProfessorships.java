/*
 * Created on Nov 22, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftPercentage;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.TeacherExecutionCourseProfessorshipShiftsDTO;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * @author jpvl
 */
public class ManageShiftProfessorships extends DispatchAction {

    /**
     * @param teacherPercentageMap
     * @return
     */
    private List buildShiftProfessorshipList(InfoTeacher infoTeacher, HashMap teacherPercentageMap) {
        List infoShiftProfessorshipList = new ArrayList();

        InfoProfessorship infoProfessorShip = new InfoProfessorship();
        infoProfessorShip.setInfoTeacher(infoTeacher);

        Iterator entryInterator = teacherPercentageMap.entrySet().iterator();
        while (entryInterator.hasNext()) {
            Map.Entry entry = (Map.Entry) entryInterator.next();
            Integer shiftIdInternal = Integer.valueOf((String) entry.getKey());
            String percentage = (String) entry.getValue();
            if ((percentage != null) && (!percentage.equals(""))) {

                percentage = percentage.replace(',', '.');

                InfoShiftProfessorship infoShiftProfessorship = new InfoShiftProfessorship();
                infoShiftProfessorship.setInfoShift(new InfoShift(shiftIdInternal));
                infoShiftProfessorship.setInfoProfessorship(infoProfessorShip);
                infoShiftProfessorship.setPercentage(Double.valueOf(percentage));
                infoShiftProfessorshipList.add(infoShiftProfessorship);
            }
        }
        return infoShiftProfessorshipList;
    }

    //    private ActionForward getForward(ActionMapping mapping, Integer
    // teacherOID)
    //    {
    //        ActionForward forward = new ActionForward();
    //        ActionForward acceptSuccess = mapping.findForward("acceptSuccess");
    //        String path = acceptSuccess.getPath();
    //
    //        if (path.indexOf('?') == -1)
    //        {
    //            path += "?";
    //        }
    //        else
    //        {
    //            path += "&amp;";
    //        }
    //        forward.setPath(path + "teacherOID=" + teacherOID);
    //        forward.setContextRelative(acceptSuccess.getContextRelative());
    //        forward.setName(acceptSuccess.getName());
    //
    //        return forward;
    //    }
    /**
     * @param teacherExecutionCourseProfessorshipShiftsDTO
     * @param executionCourseProfessorshipShiftForm
     * @param request
     */
    private void populateForm(
            TeacherExecutionCourseProfessorshipShiftsDTO teacherExecutionCourseProfessorshipShiftsDTO,
            DynaActionForm teacherExecutionCourseProfessorshipShiftForm, HttpServletRequest request) {
        teacherExecutionCourseProfessorshipShiftForm.set("teacherId",
                teacherExecutionCourseProfessorshipShiftsDTO.getInfoTeacher().getIdInternal());
        teacherExecutionCourseProfessorshipShiftForm.set("executionCourseId",
                teacherExecutionCourseProfessorshipShiftsDTO.getInfoExecutionCourse().getIdInternal());

        List infoShiftPercentageList = teacherExecutionCourseProfessorshipShiftsDTO
                .getInfoShiftPercentageList();
        Iterator iterator = infoShiftPercentageList.iterator();
        HashMap teacherPercentageMap = new HashMap();
        while (iterator.hasNext()) {
            InfoShiftPercentage infoShiftPercentage = (InfoShiftPercentage) iterator.next();
            String idInternalString = String.valueOf(infoShiftPercentage.getShift().getIdInternal());

            List infoShiftProfessorshipList = infoShiftPercentage.getInfoShiftProfessorshipList();
            for (int i = 0; i < infoShiftProfessorshipList.size(); i++) {
                InfoShiftProfessorship infoShiftProfessorship = (InfoShiftProfessorship) infoShiftProfessorshipList
                        .get(i);
                InfoTeacher infoTeacher = infoShiftProfessorship.getInfoProfessorship().getInfoTeacher();

                if (infoTeacher.equals(teacherExecutionCourseProfessorshipShiftsDTO.getInfoTeacher())) {
                    teacherPercentageMap.put(idInternalString, String.valueOf(infoShiftProfessorship
                            .getPercentage()));
                }
            }
        }
        teacherExecutionCourseProfessorshipShiftForm.set("teacherPercentageMap", teacherPercentageMap);

    }

    public ActionForward processForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm teacherExecutionCourseShiftProfessorshipForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);
        HashMap teacherPercentageMap = (HashMap) teacherExecutionCourseShiftProfessorshipForm
                .get("teacherPercentageMap");

        Integer teacherId = (Integer) teacherExecutionCourseShiftProfessorshipForm.get("teacherId");
        Integer executionCourseId = (Integer) teacherExecutionCourseShiftProfessorshipForm
                .get("executionCourseId");

        InfoTeacher infoTeacher = new InfoTeacher(teacherId);
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(executionCourseId);

        List shiftProfessorshipList = buildShiftProfessorshipList(infoTeacher, teacherPercentageMap);
        Object args[] = { infoTeacher, infoExecutionCourse, shiftProfessorshipList };
        ServiceUtils.executeService(userView, "AcceptTeacherExecutionCourseShiftPercentage", args);
        return mapping.findForward("successfull-edit");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward showForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm teacherExecutionCourseShiftProfessorshipForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer teacherId = (Integer) teacherExecutionCourseShiftProfessorshipForm.get("teacherId");
        Integer executionCourseId = (Integer) teacherExecutionCourseShiftProfessorshipForm
                .get("executionCourseId");

        TeacherExecutionCourseProfessorshipShiftsDTO teacherExecutionCourseProfessorshipShiftsDTO = (TeacherExecutionCourseProfessorshipShiftsDTO) ServiceUtils
                .executeService(userView, "ReadTeacherExecutionCourseProfessorshipShifts", new Object[] {
                        new InfoTeacher(teacherId), new InfoExecutionCourse(executionCourseId) });

        Collections.sort(teacherExecutionCourseProfessorshipShiftsDTO.getInfoShiftPercentageList(),
                new BeanComparator("shift.tipo.tipo"));
        if (!hasErrors(request)) {
            populateForm(teacherExecutionCourseProfessorshipShiftsDTO,
                    teacherExecutionCourseShiftProfessorshipForm, request);
        }

        request.setAttribute("teacherExecutionCourseProfessorshipShifts",
                teacherExecutionCourseProfessorshipShiftsDTO);
        return mapping.findForward("show-form");
    }

    /**
     * @param request
     * @return
     */
    private boolean hasErrors(HttpServletRequest request) {
        return request.getAttribute(Globals.ERROR_KEY) != null;
    }

}