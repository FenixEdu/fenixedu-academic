package ServidorApresentacao.Action.sop;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.comparators.RoomAlphabeticComparator;
import Dominio.Period;
import Dominio.RoomOccupation;
import ServidorAplicacao.Servico.sop.ReadEmptyRoomsService.InvalidTimeInterval;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.InvalidTimeIntervalActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
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

//		HttpSession session = request.getSession(false);
		request.setAttribute("minutes", Util.getMinutes());
		request.setAttribute("hours", Util.getHours());
		request.setAttribute("weekDays", Util.getDaysOfWeek());

/*		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Object argsReadExecutionPeriods[] = {
		};
		ArrayList executionPeriods =
			(ArrayList) ServiceManagerServiceFactory.executeService(
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
*/
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

			Integer weekDayInteger = new Integer((String) searchForm.get("weekDay"));
			DiaSemana weekDay = new DiaSemana(weekDayInteger); 
			
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

			if (start.after(end))
			{
				ActionError actionError = new ActionError("error.timeSwitched");
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add("error.timeSwitched", actionError);
				saveErrors(request, actionErrors);
				return prepare(mapping, form, request, response);
			}
								
			Calendar startDate = Calendar.getInstance();
			Calendar endDate = Calendar.getInstance();
			int startDay = Integer.parseInt((String) searchForm.get("startDay"));
			int startMonth = Integer.parseInt((String) searchForm.get("startMonth")) - 1;
			int startYear = Integer.parseInt((String) searchForm.get("startYear"));
			int endDay = Integer.parseInt((String) searchForm.get("endDay"));
			int endMonth = Integer.parseInt((String) searchForm.get("endMonth")) - 1;
			int endYear = Integer.parseInt((String) searchForm.get("endYear"));
			 
			startDate.set(Calendar.DAY_OF_MONTH, startDay);
			startDate.set(Calendar.MONTH, startMonth);
			startDate.set(Calendar.YEAR, startYear);
			endDate.set(Calendar.DAY_OF_MONTH, endDay);
			endDate.set(Calendar.MONTH, endMonth);
			endDate.set(Calendar.YEAR, endYear);
					
			if (startDate.after(endDate))
			{
				ActionError actionError = new ActionError("error.dateSwitched");
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add("error.dateSwitched", actionError);
				saveErrors(request, actionErrors);
				return prepare(mapping, form, request, response);
			}

												
			Object args[] = {new Period(startDate, endDate), start, end, weekDay, null, normalCapacity, new Integer(RoomOccupation.SEMANAL), null, new Boolean(true)};

			List emptyRoomsList =
				(List) ServiceUtils.executeService(
					SessionUtils.getUserView(request),
					"ReadAvailableRoomsForExam",
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
			request.setAttribute("weekDay", weekDay);
			request.setAttribute("intervalStart",timeToString(start));
			request.setAttribute("intervalEnd",timeToString(end));
			request.setAttribute("minimumCapacity", normalCapacity);
			request.setAttribute("startDate",dateToString(startDate));
			request.setAttribute("endDate",dateToString(endDate));
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

    private String dateToString(Calendar date)
    {
		String dayStr = "";
		String monthStr = "";
		String yearStr = "";
		
		if (date.get(Calendar.DAY_OF_MONTH) < 10)
			dayStr = "0";
		if (date.get(Calendar.MONTH) + 1 < 10)
			monthStr = "0";

		dayStr += date.get(Calendar.DAY_OF_MONTH);
		monthStr += date.get(Calendar.MONTH) + 1;
		yearStr += date.get(Calendar.YEAR);
      
        return dayStr + "/" + monthStr + "/" + yearStr;
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
