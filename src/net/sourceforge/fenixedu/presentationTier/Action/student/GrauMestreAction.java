package net.sourceforge.fenixedu.presentationTier.Action.student;

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

public class GrauMestreAction extends Action {

    // TODO : Not used at this time, and needs corrections

    public static String INFO_STUDENT_KEY = "infoStudent";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        HttpSession session = request.getSession(false);

        InfoStudent infoStudent = (InfoStudent) session.getAttribute(INFO_STUDENT_KEY);

        // Tipo de certidao que o aluno quer obter

        Object[] argsReadCourseByStudent = { infoStudent.getNumber(), infoStudent.getDegreeType() };

        InfoDegree infoDegree = null;
        String result = "Inexistente";

        // deve faltar remover antes de entrar se existir

        try {

            // obter titulo da dissertação

            // obter o curso de mestrado

            infoDegree = (InfoDegree) ServiceUtils.executeService(userView, "ReadCourseByStudent",
                    argsReadCourseByStudent);
            if (infoDegree != null) {

                session.setAttribute("infoCourse", infoDegree);
            }
        } catch (Exception e) {

            return mapping.getInputForward();
        }

        // verificar se conclui o mestrado ou prestou provas

        // temporario
        //result="GrauMestre";
        result = "PrestouProvas";

        Object argumentos[] = {};

        DataView data = (DataView) ServiceManagerServiceFactory.executeService(null, "ObterData",
                argumentos);
        session.setAttribute("Data", data);

        return mapping.findForward(result);

    }

}