/*
 * Created on 27/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

/**
 * @author asnr & scpo
 *  
 */
public class ViewEnroledExecutionCoursesAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Object[] args = { userView.getUtilizador() };
        List allInfoExecutionCourses = (List) ServiceUtils.executeService(userView,
                    "ReadEnroledExecutionCourses", args);

        List infoExecutionCourses = new ArrayList();
        if (allInfoExecutionCourses.size() != 0) {
            infoExecutionCourses.add(new LabelValueBean("[Escolha a Disciplina]", ""));
            Iterator iter = allInfoExecutionCourses.iterator();
            String label, value;
            while (iter.hasNext()) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) iter.next();
                value = infoExecutionCourse.getIdInternal().toString();
                label = infoExecutionCourse.getNome();

                infoExecutionCourses.add(new LabelValueBean(label, value));
            }

        }
        request.setAttribute("infoExecutionCourses", infoExecutionCourses);

        return mapping.findForward("sucess");

    }

}