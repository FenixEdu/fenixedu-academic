package ServidorApresentacao.Action.sop.exams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;

/**
 * @author Ana & Ricardo
 */
public class RoomExamSearchDA extends FenixContextDispatchAction
{

    public ActionForward prepare(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);
        Object args[] = {
        };
        List buildingsStrings = (List) ServiceUtils.executeService(userView, "ReadAllBuildings", args);
        List types = Util.readTypesOfRooms("", null);
        List buildings = new ArrayList();
        Iterator iter = buildingsStrings.iterator();
        while (iter.hasNext())
        {
            String building = (String) iter.next();
            buildings.add(new LabelValueBean(building, building));
        }

        request.setAttribute("public.buildings", buildings);
        request.setAttribute("public.types", types);

        return mapping.findForward("roomSearch");
    }

    public ActionForward search(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
    	
        IUserView userView = SessionUtils.getUserView(request);
        DynaValidatorForm roomExamForm = (DynaValidatorForm) form;

        String name = (String) roomExamForm.get("name");
        if (name.equals(""))
        {
            name = null;
        }
        String building = (String) roomExamForm.get("building");
        if (building.equals(""))
        {
            building = null;
        }
        Integer floor = null;
        Integer type = null;
        Integer normal = null;
        Integer exam = null;
        try
        {
            floor = new Integer((String) roomExamForm.get("floor"));
        }
        catch (NumberFormatException ex)
        {
        }
        try
        {
            type = new Integer((String) roomExamForm.get("type"));
        }
        catch (NumberFormatException ex)
        {
        }
        try
        {
            normal = new Integer((String) roomExamForm.get("normal"));
        }
        catch (NumberFormatException ex)
        {
        }
        try
        {
            exam = new Integer((String) roomExamForm.get("exam"));
        }
        catch (NumberFormatException ex)
        {
        }

        Object args[] = { name, building, floor, type, normal, exam };
        List rooms = (List) ServiceUtils.executeService(userView, "SearchRooms", args);
        if (rooms != null && !rooms.isEmpty())
        {
            request.setAttribute(SessionConstants.ROOMS_LIST, rooms);
        }
        return mapping.findForward("roomChoose");
    }

    public ActionForward show(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);
        DynaValidatorForm roomExamForm = (DynaValidatorForm) form;

        String rooms[] = (String[]) roomExamForm.get("roomsId");
        List infoRooms = new ArrayList();
        for (int i = 0; i < rooms.length; i++)
        {
            String roomId = rooms[i];
            Object args[] = { new Integer(roomId)};
            InfoRoom infoRoom = (InfoRoom) ServiceUtils.executeService(userView, "ReadRoomByOID", args);
			infoRooms.add(infoRoom);
        }

        List infoExamsMap = getExamsMap(request,infoRooms);
        	
        request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);

        return mapping.findForward("showExamsMap");
    }

    private List getExamsMap(HttpServletRequest request, List infoRooms) throws FenixActionException
    {    
    	
        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        InfoExecutionPeriod infoExecutionPeriod =
            (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
			
        Object[] args = { infoExecutionPeriod, infoRooms };
		List infoRoomExamsMaps;
		
        try
        {
			infoRoomExamsMaps =
                (ArrayList) ServiceUtils.executeService(userView, "ReadExamsMapByRooms", args);
                
        }
        catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(e);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        		
        return infoRoomExamsMaps;
    }
}