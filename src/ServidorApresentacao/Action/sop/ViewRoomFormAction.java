package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.base.FenixSelectedRoomsAndSelectedRoomIndexContextAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends FenixSelectedRoomsAndSelectedRoomIndexContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        HttpSession session = request.getSession();
        DynaActionForm indexForm = (DynaActionForm) form;
        Integer indexWeek = (Integer) indexForm.get("indexWeek");
        request.removeAttribute(SessionConstants.INFO_SECTION);
        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            List infoRooms = (List) request.getAttribute(SessionConstants.SELECTED_ROOMS);
            InfoRoom infoRoom = (InfoRoom) infoRooms.get(((Integer) indexForm.get("index")).intValue());

            if (indexWeek != null) {
                String roomOidString = request.getParameter(SessionConstants.ROOM_OID);
                if (roomOidString != null) {
                    Integer roomOid = new Integer(Integer.parseInt(roomOidString));
                    try {
                        infoRoom = (InfoRoom) ServiceUtils.executeService(userView, "ReadRoomByOID",
                                new Object[] { roomOid });
                    } catch (FenixServiceException e) {
                        throw new FenixActionException();
                    }

                } else {
                    indexWeek = null;
                }

            }

            request.setAttribute(SessionConstants.ROOM, infoRoom);
            request.setAttribute(SessionConstants.ROOM_OID, infoRoom.getIdInternal());

            //            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod)
            // request
            //                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
            //            Integer executionPeriodId=null;
            //            try
            //            {
            //                executionPeriodId = new
            // Integer(request.getParameter("executionPeriodId"));
            //            }
            //            catch (NumberFormatException e3)
            //            {
            //                // ignore
            //            }
            //            Object argsReadLessons[] = {infoExecutionPeriod,
            // infoRoom,executionPeriodId};
            ArrayList weeks = new ArrayList();

            try {
                //              weeks
                InfoExecutionPeriod executionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(
                        userView, "ReadCurrentExecutionPeriod", new Object[] {});
                Calendar begin = Calendar.getInstance();
                begin.setTime(executionPeriod.getBeginDate());
                Calendar end = Calendar.getInstance();
                end.setTime(executionPeriod.getEndDate());
                ArrayList weeksLabelValueList = new ArrayList();
                begin.add(Calendar.DATE, Calendar.MONDAY - begin.get(Calendar.DAY_OF_WEEK));
                int i = 0;
                boolean selectedWeek = false;
                while (begin.before(end) || begin.before(Calendar.getInstance())) {
                    Calendar day = Calendar.getInstance();
                    day.setTimeInMillis(begin.getTimeInMillis());
                    weeks.add(day);
                    String beginWeekString = DateFormatUtils.format(begin.getTime(), "dd/MM/yyyy");
                    begin.add(Calendar.DATE, 5);
                    String endWeekString = DateFormatUtils.format(begin.getTime(), "dd/MM/yyyy");
                    weeksLabelValueList.add(new LabelValueBean(beginWeekString + " - " + endWeekString,
                            new Integer(i).toString()));
                    begin.add(Calendar.DATE, 2);
                    if (!selectedWeek && indexWeek == null && Calendar.getInstance().before(begin)) {
                        indexForm.set("indexWeek", new Integer(i));
                        selectedWeek = true;
                    }
                    i++;
                }

                request.setAttribute(SessionConstants.LABELLIST_WEEKS, weeksLabelValueList);
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }
            Calendar today = null;
            if (indexWeek == null) {
                today = Calendar.getInstance();
            } else {
                today = (Calendar) weeks.get(indexWeek.intValue());
            }

            Object argsReadLessonsAndExams[] = { infoRoom, today };

            try {
                /*
                 * List lessons; lessons = (List)
                 * ServiceUtils.executeService(null, "LerAulasDeSalaEmSemestre",
                 * argsReadLessons);
                 */

                List showOccupations = (List) ServiceUtils.executeService(userView,
                        "ReadLessonsAndExamsInWeekAndRoom", argsReadLessonsAndExams);

                /*
                 * if (lessons != null) {
                 * request.setAttribute(SessionConstants.LESSON_LIST_ATT,
                 * lessons); }
                 */
                if (showOccupations != null) {
                    request.setAttribute(SessionConstants.LESSON_LIST_ATT, showOccupations);
                }

            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }

            // Escolha de periodo execucao
            //            Object argsReadExecutionPeriods[] = {};
            //            ArrayList executionPeriods;
            /*
             * try { executionPeriods = (ArrayList) ServiceManagerServiceFactory
             * .executeService(userView, "ReadNotClosedExecutionPeriods",
             * argsReadExecutionPeriods); } catch (FenixServiceException e1) {
             * throw new FenixActionException(); }
             */
            /*
             * ArrayList executionPeriodsLabelValueList = new ArrayList(); for
             * (int i = 0; i < executionPeriods.size(); i++) {
             * infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods
             * .get(i); executionPeriodsLabelValueList.add(new LabelValueBean(
             * infoExecutionPeriod.getName() + " - " +
             * infoExecutionPeriod.getInfoExecutionYear() .getYear(), "" + i)); }
             * 
             * request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD,
             * executionPeriodsLabelValueList);
             *///--------------------
            return mapping.findForward("Sucess");

        }
        throw new FenixActionException();
    }

}