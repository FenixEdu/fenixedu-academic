/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author lmac1
 */
public class ReadTeacherInChargeAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));

        Object args[] = { executionCourseId };

        InfoExecutionCourse infoExecutionCourse = null;

        try {
            infoExecutionCourse = (InfoExecutionCourse) ServiceUtils.executeService(userView,
                    "ReadExecutionCourse", args);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        List infoTeachersList = null;

        try {
            infoTeachersList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionCourseTeachers", args);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (infoTeachersList != null) {
            List teachersIds = new ArrayList();
            Integer teacherId;
            Iterator iter = infoTeachersList.iterator();
            while (iter.hasNext()) {
                teacherId = ((InfoTeacher) iter.next()).getIdInternal();
                teachersIds.add(teacherId);
            }

            Integer[] professorShipTeachersIds = (Integer[]) teachersIds.toArray(new Integer[] {});
            DynaActionForm newForm = (DynaActionForm) form;
            newForm.set("professorShipTeachersIds", professorShipTeachersIds);

            List responsiblesIds = null;

            try {
                responsiblesIds = (List) ServiceUtils.executeService(userView,
                        "ReadExecutionCourseResponsiblesIds", args);

            } catch (FenixServiceException fenixServiceException) {
                throw new FenixActionException(fenixServiceException.getMessage());
            }

            Integer[] responsibleTeachersIds = (Integer[]) responsiblesIds.toArray(new Integer[] {});
            newForm.set("responsibleTeachersIds", responsibleTeachersIds);
            request.setAttribute("infoTeachersList", infoTeachersList);

        }
        request.setAttribute("executionCourseName", infoExecutionCourse.getNome());
        return mapping.findForward("readExecutionCourseTeachers");
    }

}