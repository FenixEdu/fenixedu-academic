package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * @author tfc130
 */
public class PrepararEscolherDisciplinaExecucaoFormAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            IUserView userView = (IUserView) sessao.getAttribute("UserView");

            // Ler Disciplinas em Execucao
            InfoExecutionDegree iLE = (InfoExecutionDegree) request
                    .getAttribute("infoLicenciaturaExecucao");
            Integer semestre = (Integer) request.getAttribute("semestre");
            Integer anoCurricular = (Integer) request.getAttribute("anoCurricular");

            Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] = { new CurricularYearAndSemesterAndInfoExecutionDegree(
                    anoCurricular, semestre, iLE) };
            List infoDisciplinasExecucao = (ArrayList) ServiceManagerServiceFactory.executeService(
                    userView, "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
                    argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);

            //Collections.sort(infoDisciplinasExecucao);

            List disciplinasExecucao = new ArrayList();
            disciplinasExecucao.add(new LabelValueBean("escolher", ""));
            for (int i = 0; i < infoDisciplinasExecucao.size(); i++) {
                InfoExecutionCourse elem = (InfoExecutionCourse) infoDisciplinasExecucao.get(i);
                disciplinasExecucao.add(new LabelValueBean(elem.getNome(), (new Integer(
                        infoDisciplinasExecucao.indexOf(elem) + 1)).toString()));
            }

            request.setAttribute("disciplinasExecucao", disciplinasExecucao);
            request.setAttribute("infoDisciplinasExecucao", infoDisciplinasExecucao);

            return mapping.findForward("Sucesso");
        }
        throw new Exception();
    }
}