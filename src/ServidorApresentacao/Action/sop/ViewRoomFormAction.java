package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends FenixContextAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession();
		DynaActionForm indexForm = (DynaActionForm) form;
		request.removeAttribute(SessionConstants.INFO_SECTION);
		if (session != null) {
			IUserView userView = (IUserView) session.getAttribute("UserView");
			GestorServicos gestor = GestorServicos.manager();
			
			List infoRooms = (List) request.getAttribute("publico.infoRooms");
			InfoRoom infoRoom =
				(InfoRoom) infoRooms.get(
					((Integer) indexForm.get("index")).intValue());
			request.removeAttribute("publico.infoRoom");
			request.setAttribute("publico.infoRoom", infoRoom);

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) this
					.servlet
					.getServletContext()
					.getAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD_KEY);

			Object argsReadLessons[] = { infoExecutionPeriod, infoRoom };

			try {
				List lessons;
				lessons =
					(List) ServiceUtils.executeService(
						null,
						"LerAulasDeSalaEmSemestre",
						argsReadLessons);

				if (lessons != null) {
					request.setAttribute(
						SessionConstants.LESSON_LIST_ATT,
						lessons);
				}

			} catch (FenixServiceException e) {
				throw new FenixActionException();
			}

			// Escolha de periodo execucao
			Object argsReadExecutionPeriods[] = {
			};
			ArrayList executionPeriods;
			try {
				executionPeriods = (ArrayList) gestor.executar(
					userView,
					"ReadExecutionPeriods",
					argsReadExecutionPeriods);
			} catch (FenixServiceException e1) {
				throw new FenixActionException();
			}

//			// if executionPeriod was previously selected,form has that
//			// value as default
//			InfoExecutionPeriod selectedExecutionPeriod =
//				(InfoExecutionPeriod) session.getAttribute(
//					SessionConstants.INFO_EXECUTION_PERIOD_KEY);
//			DynaActionForm indexFormForExecutionPeriod = new DynaActionForm();
//			if (selectedExecutionPeriod != null) {
//				indexFormForExecutionPeriod.set(
//					"index",
//					new Integer(
//						executionPeriods.indexOf(selectedExecutionPeriod)));
//			}
//			request.setAttribute("pagedIndexForm", indexFormForExecutionPeriod);
			//----------------------------------------------

			ArrayList executionPeriodsLabelValueList = new ArrayList();
			for (int i = 0; i < executionPeriods.size(); i++) {
				infoExecutionPeriod =
					(InfoExecutionPeriod) executionPeriods.get(i);
				executionPeriodsLabelValueList.add(
					new LabelValueBean(
						infoExecutionPeriod.getName()
							+ " - "
							+ infoExecutionPeriod
								.getInfoExecutionYear()
								.getYear(),
						"" + i));
			}

			request.setAttribute(
				SessionConstants.LIST_INFOEXECUTIONPERIOD,
				executionPeriods);

			request.setAttribute(
				SessionConstants.LABELLIST_EXECUTIONPERIOD,
				executionPeriodsLabelValueList);
			//--------------------

			return mapping.findForward("Sucess");
		} else {
			throw new FenixActionException();
		}

	}
}