package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.DataView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Ricardo Nortadas & Rui Figueiredo
 */

public class ShowDisciplineListAction extends Action {

    public static String INFO_STUDENT_KEY = "infoStudent";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        HttpSession session = request.getSession(false);

        InfoStudent infoStudent = (InfoStudent) session.getAttribute(INFO_STUDENT_KEY);

        Object[] argsReadDisciplinesByStudent = { infoStudent.getNumber(), infoStudent.getDegreeType() };
        Object[] argsReadCourseByStudent = { infoStudent.getNumber(), infoStudent.getDegreeType() };

        List DisciplinesList = new ArrayList();
        InfoDegree degree = null;

        try {
            DisciplinesList = (ArrayList) ServiceUtils.executeService(userView,
                    "ReadDisciplinesByStudent", argsReadDisciplinesByStudent);
            if (!DisciplinesList.isEmpty()) {
                session.setAttribute("disciplinesList", DisciplinesList);
            }

            degree = (InfoDegree) ServiceUtils.executeService(userView, "ReadCourseByStudent",
                    argsReadCourseByStudent);
            if (degree != null) {

                // TODO : This session attribute name should be infoDegree
                session.setAttribute("infoCourse", degree);
            }
        } catch (Exception e) {
            return mapping.getInputForward();
        }

        Object argumentos[] = {};

        DataView data = (DataView) ServiceManagerServiceFactory.executeService(null, "ObterData",
                argumentos);
        //request.setAttribute("Data", data);
        session.setAttribute("Data", data);

        return mapping.findForward("viewDisciplinesList");

    }

}