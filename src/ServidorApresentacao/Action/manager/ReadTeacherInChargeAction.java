/*
 * Created on 16/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

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