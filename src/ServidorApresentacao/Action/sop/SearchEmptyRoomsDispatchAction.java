package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.comparators.RoomAlphabeticComparator;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.sop.ReadEmptyRoomsService.InvalidTimeInterval;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidTimeIntervalActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class SearchEmptyRoomsDispatchAction extends FenixContextDispatchAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		request.setAttribute("minutes", Util.getMinutes());
		request.setAttribute("hours", Util.getHours());
		request.setAttribute("weekDays", Util.getDaysOfWeek());

		// execution period selection		
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos gestor = GestorServicos.manager();

		Object argsReadExecutionPeriods[] = {
		};
		ArrayList executionPeriods =
			(ArrayList) gestor.executar(
				userView,
				"ReadNotClosedExecutionPeriods",
				argsReadExecutionPeriods);

		// if executionPeriod was previously selected,form has that
		// value as default
		InfoExecutionPeriod selectedExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);
		if (selectedExecutionPeriod != null) {
			DynaActionForm searchForm = (DynaActionForm) form;
			searchForm.set(
				"executionPeriodIndex",
				new Integer(executionPeriods.indexOf(selectedExecutionPeriod)));
		}
		//----------------------------------------------


		ArrayList executionPeriodsLabelValueList = new ArrayList();
		for (int i = 0; i < executionPeriods.size(); i++) {
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) executionPeriods.get(i);
			executionPeriodsLabelValueList.add(
				new LabelValueBean(
					infoExecutionPeriod.getName()
						+ " - "
						+ infoExecutionPeriod.getInfoExecutionYear().getYear(),
					"" + i));
		}

//		request.setAttribute(
//			SessionConstants.LIST_INFOEXECUTIONPERIOD,
//			executionPeriods);

		request.setAttribute(
			SessionConstants.LABELLIST_EXECUTIONPERIOD,
			executionPeriodsLabelValueList);

		return mapping.findForward("searchPage");
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public ActionForward doSearch(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		try {
			DynaValidatorForm searchForm = (DynaValidatorForm) form;
			Integer normalCapacity = new Integer(0);
			try {
				normalCapacity =
					new Integer((String) searchForm.get("normalCapacity"));
			} catch (NumberFormatException e) {
				//ignored
			}

			Integer weekDay = new Integer((String) searchForm.get("weekDay"));

			Calendar start = Calendar.getInstance();
			start.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) searchForm.get("startHour")));
			start.set(
				Calendar.MINUTE,
				Integer.parseInt((String) searchForm.get("startMinutes")));
			start.set(Calendar.SECOND, 0);
			Calendar end = Calendar.getInstance();
			end.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) searchForm.get("endHour")));
			end.set(
				Calendar.MINUTE,
				Integer.parseInt((String) searchForm.get("endMinutes")));
			end.set(Calendar.SECOND, 0);

			InfoRoom infoRoom = new InfoRoom();
			infoRoom.setCapacidadeNormal(normalCapacity);

			InfoLesson infoLesson = new InfoLesson();

			infoLesson.setDiaSemana(new DiaSemana(weekDay));
			infoLesson.setInicio(start);
			infoLesson.setFim(end);

			Object argsReadExecutionPeriods[] = {
			};
			ArrayList infoExecutionPeriodList;
			try {
				infoExecutionPeriodList =
					(ArrayList) ServiceUtils.executeService(
						null,
						"ReadExecutionPeriods",
						argsReadExecutionPeriods);
			} catch (FenixServiceException e) {
				throw new FenixActionException();
			}

			Integer index = (Integer) searchForm.get("executionPeriodIndex");

			InfoExecutionPeriod infoExecutionPeriod = null;
			if (infoExecutionPeriodList != null && index != null) {
				infoExecutionPeriod =
					(InfoExecutionPeriod) infoExecutionPeriodList.get(
						index.intValue());
			}
			// Set selected executionPeriod in request
			request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
			request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());			
			
			// --------------------------------	

			Object args[] = { infoRoom, infoLesson, infoExecutionPeriod };

			List emptyRoomsList =
				(List) ServiceUtils.executeService(
					SessionUtils.getUserView(request),
					"ReadEmptyRoomsService",
					args);
			Collections.sort(emptyRoomsList,new RoomAlphabeticComparator());
			if (emptyRoomsList == null || emptyRoomsList.isEmpty()) {
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"search.empty.rooms.no.rooms",
					new ActionError("search.empty.rooms.no.rooms"));

				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
			// keep search criteria in request 
			// (executionPeriod is already in session...)
			request.setAttribute("weekDay", infoLesson.getDiaSemana());
			request.setAttribute("intervalStart",timeToString(infoLesson.getInicio()));
			request.setAttribute("intervalEnd",timeToString(infoLesson.getFim()));
			request.setAttribute("minimumCapacity", normalCapacity);
			//--------------------------------------------

			request.setAttribute("roomList", emptyRoomsList);

			return mapping.findForward("showSearchResult");

		} catch (InvalidTimeInterval ex) {
			throw new InvalidTimeIntervalActionException(ex);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}
	}

	private String timeToString(Calendar time) {
		String hourStr = "" + time.get(Calendar.HOUR_OF_DAY);
		String minutesStr = "" + time.get(Calendar.MINUTE);

		if (time.get(Calendar.HOUR_OF_DAY) < 10)
			hourStr = "0" + hourStr;
		if (time.get(Calendar.MINUTE) < 10)
			minutesStr = "0" + minutesStr;

		return hourStr + ":" + minutesStr;

	}

}
