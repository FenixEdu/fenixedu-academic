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
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.base.FenixSelectedRoomsAndSelectedRoomIndexContextAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends
        FenixSelectedRoomsAndSelectedRoomIndexContextAction
{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException
    {
        try
        {
            super.execute(mapping, form, request, response);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
        }

        HttpSession session = request.getSession();
        DynaActionForm indexForm = (DynaActionForm) form;
        request.removeAttribute(SessionConstants.INFO_SECTION);
        if (session != null)
        {
            IUserView userView = (IUserView) session
                    .getAttribute(SessionConstants.U_VIEW);

            List infoRooms = (List) request
                    .getAttribute(SessionConstants.SELECTED_ROOMS);
            InfoRoom infoRoom = (InfoRoom) infoRooms.get(((Integer) indexForm
                    .get("index")).intValue());

            request.setAttribute(SessionConstants.ROOM, infoRoom);
            request.setAttribute(SessionConstants.ROOM_OID, infoRoom
                    .getIdInternal());

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
            Integer executionPeriodId=null;
            try
            {
                executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
            }
            catch (NumberFormatException e3)
            {
                // ignore                
            }
            Object argsReadLessons[] = {infoExecutionPeriod, infoRoom,executionPeriodId};

            try
            {
                List lessons;
                lessons = (List) ServiceUtils.executeService(null,
                        "LerAulasDeSalaEmSemestre", argsReadLessons);

                if (lessons != null)
                {
                    request.setAttribute(SessionConstants.LESSON_LIST_ATT,
                            lessons);
                }

            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException();
            }

            // Escolha de periodo execucao
            Object argsReadExecutionPeriods[] = {};
            ArrayList executionPeriods;
            try
            {
                executionPeriods = (ArrayList) ServiceManagerServiceFactory
                        .executeService(userView,
                                "ReadNotClosedExecutionPeriods",
                                argsReadExecutionPeriods);
            }
            catch (FenixServiceException e1)
            {
                throw new FenixActionException();
            }

            ArrayList executionPeriodsLabelValueList = new ArrayList();
            for (int i = 0; i < executionPeriods.size(); i++)
            {
                infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods
                        .get(i);
                executionPeriodsLabelValueList.add(new LabelValueBean(
                        infoExecutionPeriod.getName()
                                + " - "
                                + infoExecutionPeriod.getInfoExecutionYear()
                                        .getYear(), "" + i));
            }

            request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD,
                    executionPeriodsLabelValueList);
            //--------------------

            return mapping.findForward("Sucess");

        }
        else
        {
            throw new FenixActionException();
        }

    }
}