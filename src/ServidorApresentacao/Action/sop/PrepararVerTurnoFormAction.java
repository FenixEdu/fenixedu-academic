package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import DataBeans.comparators.InfoShiftComparatorByLessonType;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 */
public class PrepararVerTurnoFormAction extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

	super.execute(mapping, form, request, response);
		
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
		DynaActionForm manipularTurnosForm = (DynaActionForm) request.getAttribute("manipularTurnosForm");
        IUserView userView = (IUserView) sessao.getAttribute("UserView");
        
		Integer indexTurno = (Integer) manipularTurnosForm.get("indexTurno");

        //ArrayList infoTurnos = (ArrayList) request.getAttribute("infoTurnosDeDisciplinaExecucao");
		InfoExecutionCourse iDE = (InfoExecutionCourse) request.getAttribute(SessionConstants.EXECUTION_COURSE);
		Object argsLerTurnosDeDisciplinaExecucao[] = { iDE };
		List infoTurnos= (List) ServiceManagerServiceFactory.executeService(userView, "LerTurnosDeDisciplinaExecucao", argsLerTurnosDeDisciplinaExecucao);
		Collections.sort(infoTurnos, new InfoShiftComparatorByLessonType());

        InfoShift infoTurno = (InfoShift) infoTurnos.get(indexTurno.intValue());
        request.removeAttribute("infoTurno");
        request.setAttribute("infoTurno", infoTurno);
		request.setAttribute(SessionConstants.SHIFT, infoTurno);

	    Object argsLerAulasDeTurno[] = { new ShiftKey(infoTurno.getNome(), infoTurno.getInfoDisciplinaExecucao())};
        ArrayList infoAulasDeTurno = (ArrayList) ServiceManagerServiceFactory.executeService(userView, "LerAulasDeTurno", argsLerAulasDeTurno);

		request.removeAttribute("infoAulasDeTurno");
		if (!infoAulasDeTurno.isEmpty())
	        request.setAttribute("infoAulasDeTurno", infoAulasDeTurno);

      return mapping.findForward("Sucesso");
    } 
      throw new Exception();  
  }
}