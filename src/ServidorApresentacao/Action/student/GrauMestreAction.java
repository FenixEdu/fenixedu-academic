package ServidorApresentacao.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegree;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.DataView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

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

        String strTipo = (String) session.getAttribute("Tipo");

        int Tipo = Integer.parseInt(strTipo);
        System.out.println(Tipo);
        // Temporario

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