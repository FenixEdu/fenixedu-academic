package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.IUserView;
/**
 * @author tfc130
 */
public class PrepararEscolherDisciplinaETipoFormAction extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
		
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
        IUserView userView = (IUserView) sessao.getAttribute("UserView");
        
		// Ler Disciplinas em Execucao
        InfoExecutionDegree iLE = (InfoExecutionDegree) request.getAttribute("infoLicenciaturaExecucao");
        Integer semestre = (Integer) request.getAttribute("semestre");
        Integer anoCurricular = (Integer) request.getAttribute("anoCurricular");

        Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] = { new CurricularYearAndSemesterAndInfoExecutionDegree(anoCurricular, semestre, iLE) };
		ArrayList infoDisciplinasExecucao = (ArrayList) ServiceManagerServiceFactory.executeService(userView, "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);

        //Collections.sort(infoDisciplinasExecucao);

        ArrayList disciplinasExecucao = new ArrayList();
        disciplinasExecucao.add(new LabelValueBean("escolher", ""));
        Iterator iterator = infoDisciplinasExecucao.iterator();
        while(iterator.hasNext()) {
            InfoExecutionCourse elem = (InfoExecutionCourse)iterator.next();
            disciplinasExecucao.add(new LabelValueBean(elem.getNome(), (new Integer( infoDisciplinasExecucao.indexOf(elem) + 1 )).toString()));
        }

        request.setAttribute("disciplinasExecucao", disciplinasExecucao);
        request.setAttribute("infoDisciplinasExecucao", infoDisciplinasExecucao);    


      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}