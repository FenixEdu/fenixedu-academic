package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.LerAulasDeTurno;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadShiftByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.RemoverAula;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Luis Cruz & SaraRibeiro
 * 
 */
public class RemoverAulaDeTurnoFormAction extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		super.execute(mapping, form, request, response);

		DynaActionForm editarAulasDeTurnoForm = (DynaActionForm) request.getAttribute("editarAulasDeTurnoForm");

		IUserView userView = UserView.getUser();

		Integer shiftOID = new Integer(request.getParameter(PresentationConstants.SHIFT_OID));

		InfoShift infoTurno = ReadShiftByOID.run(shiftOID);

		InfoExecutionCourse infoExecutionCourse =
				(InfoExecutionCourse) request.getAttribute(PresentationConstants.EXECUTION_COURSE);

		Integer indexAula = (Integer) editarAulasDeTurnoForm.get("indexAula");

		List infoAulas = LerAulasDeTurno.run(new ShiftKey(infoTurno.getNome(), infoExecutionCourse));

		InfoLesson infoLesson = (InfoLesson) infoAulas.get(indexAula.intValue());

		RemoverAula.run(infoLesson, infoTurno);

		return mapping.findForward("Sucesso");
	}
}