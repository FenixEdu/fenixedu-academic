package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixSelectedRoomsAndSelectedRoomIndexContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends FenixSelectedRoomsAndSelectedRoomIndexContextAction
{

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException,
            FenixFilterException
    {
        try
        {
            super.execute(mapping, form, request, response);
        } catch (Exception e2)
        {
            e2.printStackTrace();
        }

        DynaActionForm indexForm = (DynaActionForm) form;
        Integer indexWeek = (Integer) indexForm.get("indexWeek");
        String selectedExecutionPeriodOIDString = (String) indexForm.get("selectedExecutionPeriodOID");
        Integer selectedExecutionPeriodOID = (selectedExecutionPeriodOIDString != null && selectedExecutionPeriodOIDString
                .length() > 0) ? new Integer(selectedExecutionPeriodOIDString) : null;
        request.removeAttribute(SessionConstants.INFO_SECTION);

        IUserView userView = SessionUtils.getUserView(request);

        setListOfExecutionPeriods(request, userView);

        List infoRooms = (List) request.getAttribute(SessionConstants.SELECTED_ROOMS);
        InfoRoom infoRoom = (InfoRoom) infoRooms.get(((Integer) indexForm.get("index")).intValue());

        if (indexWeek != null)
        {
            String roomOidString = request.getParameter(SessionConstants.ROOM_OID);
            if (roomOidString != null)
            {
                Integer roomOid = new Integer(Integer.parseInt(roomOidString));
                infoRoom = (InfoRoom) ServiceUtils.executeService(userView, "ReadRoomByOID",
                        new Object[] { roomOid });

            } else
            {
                indexWeek = null;
            }

        }

        request.setAttribute(SessionConstants.ROOM, infoRoom);
        request.setAttribute(SessionConstants.ROOM_OID, infoRoom.getIdInternal());

        ArrayList weeks = new ArrayList();

        // weeks
        InfoExecutionPeriod executionPeriod = getExecutionPeriod(userView, selectedExecutionPeriodOID);
        indexForm.set("selectedExecutionPeriodOID", executionPeriod.getIdInternal().toString());

        Calendar begin = Calendar.getInstance();
        begin.setTime(executionPeriod.getBeginDate());
        Calendar end = Calendar.getInstance();
        end.setTime(executionPeriod.getEndDate());
        ArrayList weeksLabelValueList = new ArrayList();
        begin.add(Calendar.DATE, Calendar.MONDAY - begin.get(Calendar.DAY_OF_WEEK));
        int i = 0;
        boolean selectedWeek = false;
        while (begin.before(end) || begin.before(Calendar.getInstance()))
        {
            Calendar day = Calendar.getInstance();
            day.setTimeInMillis(begin.getTimeInMillis());
            weeks.add(day);
            String beginWeekString = DateFormatUtils.format(begin.getTime(), "dd/MM/yyyy");
            begin.add(Calendar.DATE, 5);
            String endWeekString = DateFormatUtils.format(begin.getTime(), "dd/MM/yyyy");
            weeksLabelValueList.add(new LabelValueBean(beginWeekString + " - " + endWeekString,
                    new Integer(i).toString()));
            begin.add(Calendar.DATE, 2);
            if (!selectedWeek && indexWeek == null && Calendar.getInstance().before(begin))
            {
                indexForm.set("indexWeek", new Integer(i));
                selectedWeek = true;
            }
            i++;
        }

        Calendar today = null;
        if (indexWeek == null)
        {
            today = Calendar.getInstance();
            if (!executionPeriod.getState().equals(PeriodState.CURRENT))
            {
                // today.setTime(executionPeriod.getBeginDate());
                today = (Calendar) weeks.get(0);
                indexForm.set("indexWeek", new Integer(0));
            }
        } else
        {
            today = (Calendar) weeks.get(indexWeek.intValue());
        }

        Object argsReadLessonsAndExams[] = { infoRoom, today, executionPeriod };

        List showOccupations = (List) ServiceUtils.executeService(userView,
                "ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom", argsReadLessonsAndExams);

        if (showOccupations != null)
        {
            request.setAttribute(SessionConstants.LESSON_LIST_ATT, showOccupations);
        }

        request.setAttribute(SessionConstants.LABELLIST_WEEKS, weeksLabelValueList);

        return mapping.findForward("Sucess");
    }

    /**
     * @param userView
     * @param selectedExecutionPeriodOID
     * @return
     * @throws FenixServiceException
     * @throws FenixFilterException
     */
    private InfoExecutionPeriod getExecutionPeriod(IUserView userView, Integer selectedExecutionPeriodOID)
            throws FenixFilterException, FenixServiceException
    {
        if (selectedExecutionPeriodOID == null)
        {
            return (InfoExecutionPeriod) ServiceUtils.executeService(userView,
                    "ReadCurrentExecutionPeriod", new Object[] {});
        }
        Object[] args = { selectedExecutionPeriodOID };
        return (InfoExecutionPeriod) ServiceUtils.executeService(null, "ReadExecutionPeriodByOID", args);

    }

    protected void setListOfExecutionPeriods(HttpServletRequest request, IUserView userView)
            throws FenixServiceException, FenixFilterException
    {
        Object argsReadExecutionPeriods[] = {};
        List executionPeriods;
        executionPeriods = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                "ReadNotClosedExecutionPeriods", argsReadExecutionPeriods);

        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++)
        {
            InfoExecutionPeriod infoExecutionPeriod2 = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod2.getName() + " - "
                    + infoExecutionPeriod2.getInfoExecutionYear().getYear(), ""
                    + infoExecutionPeriod2.getIdInternal()));
        }

        request.setAttribute(SessionConstants.LIST_INFOEXECUTIONPERIOD, executionPeriods);

        request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);
    }

}