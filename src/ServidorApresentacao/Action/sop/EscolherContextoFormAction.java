package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.DegreeKey;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 */
public class EscolherContextoFormAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
    DynaActionForm escolherContextoForm = (DynaActionForm) form;

    HttpSession session = request.getSession(false);
	   
    SessionUtils.removeAttributtes(session, SessionConstants.CONTEXT_PREFIX);
    
    if (session != null) {
        Integer semestre = (Integer) escolherContextoForm.get("semestre");
        Integer anoCurricular = (Integer) escolherContextoForm.get("anoCurricular");
        String sigla = (String) escolherContextoForm.get("sigla");

        IUserView userView = (IUserView) session.getAttribute("UserView");
        GestorServicos gestor = GestorServicos.manager();

      	session.setAttribute("anoCurricular", anoCurricular);
      	session.setAttribute("semestre", semestre);
      	
        Object argsLerLicenciaturaExecucao[] = { new DegreeKey(sigla) };
		InfoExecutionDegree iLE = (InfoExecutionDegree) gestor.executar(userView, "LerLicenciaturaExecucaoDeLicenciatura", argsLerLicenciaturaExecucao);

		if (iLE != null) {
        	session.setAttribute(SessionConstants.INFO_LIC_EXEC_KEY, iLE);
	    	CurricularYearAndSemesterAndInfoExecutionDegree cYSiED = new CurricularYearAndSemesterAndInfoExecutionDegree(anoCurricular, semestre, iLE);
	    	session.setAttribute(SessionConstants.CONTEXT_KEY, cYSiED);
	
			Object argsLerLicenciatura[] = { new String(sigla) };

			InfoDegree iL = (InfoDegree) gestor.executar(userView, "LerLicenciatura", argsLerLicenciatura);


	        session.setAttribute("infoLicenciatura", iL);

			Object argsLerTurmas[] = { cYSiED };
			List listaInfoTurmas = (List) gestor.executar(userView, "LerTurmas", argsLerTurmas);

	        session.removeAttribute("listaTurmasBean"); 
	   	    if(!listaInfoTurmas.isEmpty()) {
    	        //Collections.sort(listaInfoTurmas);
        	    session.setAttribute("listaInfoTurmas", listaInfoTurmas);
	        }
	        session.removeAttribute("licenciaturasExecucao");		
		}
		else {
			return mapping.findForward("Licenciatura execucao inexistente");
		}

      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}
