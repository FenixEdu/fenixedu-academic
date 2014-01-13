package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadBuildings;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadRoomByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.ReadExamsMapByRooms;
import net.sourceforge.fenixedu.dataTransferObject.InfoBuilding;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Ana & Ricardo
 */
@Mapping(module = "resourceAllocationManager", path = "/roomExamSearch", input = "/roomExamSearch.do?method=prepare&page=0",
        attribute = "roomExamSearchForm", formBean = "roomExamSearchForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "roomChoose", path = "df.page.selectRoom"),
        @Forward(name = "roomSearch", path = "df.page.selectRooms"),
        @Forward(name = "showExamsMap", path = "df.page.examSearchByRoom") })
public class RoomExamSearchDA extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();

        List infoBuildings = ReadBuildings.run();
        List buildingsStrings = (List) CollectionUtils.collect(infoBuildings, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                final InfoBuilding infoBuilding = (InfoBuilding) arg0;
                return infoBuilding.getName();
            }
        });
        List types = Util.readTypesOfRooms("", null);
        List buildings = new ArrayList();
        Iterator iter = buildingsStrings.iterator();
        while (iter.hasNext()) {
            String building = (String) iter.next();
            buildings.add(new LabelValueBean(building, building));
        }

        request.setAttribute("public.buildings", buildings);
        request.setAttribute("public.types", types);

        final String executionPeriodString = request.getParameter(PresentationConstants.EXECUTION_PERIOD_OID);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, executionPeriodString);

        return mapping.findForward("roomSearch");
    }

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User userView = Authenticate.getUser();
        DynaValidatorForm roomExamForm = (DynaValidatorForm) form;

        String rooms[] = (String[]) roomExamForm.get("roomsId");
        List infoRooms = new ArrayList();
        for (String roomId : rooms) {
            InfoRoom infoRoom = ReadRoomByOID.run(roomId);
            infoRooms.add(infoRoom);
        }

        List infoExamsMap = getExamsMap(request, infoRooms);

        request.setAttribute(PresentationConstants.INFO_EXAMS_MAP, infoExamsMap);

        return mapping.findForward("showExamsMap");
    }

    private List getExamsMap(HttpServletRequest request, List infoRooms) throws FenixActionException {

        User userView = getUserView(request);

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        List infoRoomExamsMaps;

        try {
            infoRoomExamsMaps = ReadExamsMapByRooms.run(infoExecutionPeriod, infoRooms);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        } catch (Exception e) {
            throw new FenixActionException(e);
        }

        return infoRoomExamsMaps;
    }
}