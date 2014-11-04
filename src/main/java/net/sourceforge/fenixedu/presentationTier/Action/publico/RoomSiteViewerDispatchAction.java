/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Factory.RoomSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.publico.spaces.FindSpacesDA;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/viewRoom", module = "publico", formBean = "indexForm", functionality = FindSpacesDA.class)
@Forwards(@Forward(name = "roomViewer", path = "/publico/viewRoom_bd.jsp"))
public class RoomSiteViewerDispatchAction extends FenixContextDispatchAction {

    public ActionForward roomViewer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String roomName = request.getParameter("roomName");
        if (roomName == null) {
            roomName = (String) request.getAttribute("roomName");
        }
        request.setAttribute("roomName", roomName);
        RoomKey roomKey = null;

        if (roomName != null) {
            roomKey = new RoomKey(roomName);

            InfoSiteRoomTimeTable bodyComponent = new InfoSiteRoomTimeTable();
            DynaActionForm indexForm = (DynaActionForm) form;
            Integer indexWeek = (Integer) indexForm.get("indexWeek");
            // Integer executionPeriodID = (Integer)
            // indexForm.get("selectedExecutionPeriodID");
            String executionPeriodIDString = request.getParameter("selectedExecutionPeriodID");
            if (executionPeriodIDString == null) {
                executionPeriodIDString = (String) request.getAttribute("selectedExecutionPeriodID");
            }
            String executionPeriodID = (executionPeriodIDString != null) ? executionPeriodIDString : null;
            if (executionPeriodID == null) {
                try {
                    // executionPeriodID = (Integer)
                    // indexForm.get("selectedExecutionPeriodID");
                    executionPeriodID =
                            indexForm.get("selectedExecutionPeriodID").equals("") ? null : (String) indexForm
                                    .get("selectedExecutionPeriodID");
                } catch (IllegalArgumentException ex) {
                }
            }
            Calendar today = new DateMidnight().toCalendar(null);
            ArrayList weeks = new ArrayList();

            InfoExecutionPeriod executionPeriod;
            if (executionPeriodID == null) {
                executionPeriod = ReadCurrentExecutionPeriod.run();
                executionPeriodID = executionPeriod.getExternalId();
                try {
                    indexForm.set("selectedExecutionPeriodID", executionPeriod.getExternalId().toString());
                } catch (IllegalArgumentException ex) {
                }
            } else {
                executionPeriod = ReadExecutionPeriodByOID.run(executionPeriodID);
            }

            // weeks
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
                weeksLabelValueList.add(new LabelValueBean(beginWeekString + " - " + endWeekString, new Integer(i).toString()));
                begin.add(Calendar.DATE, 2);
                if (!selectedWeek && indexWeek == null && Calendar.getInstance().before(begin)) {
                    indexForm.set("indexWeek", new Integer(i));
                    selectedWeek = true;
                }
                i++;
            }

            final Collection<ExecutionSemester> executionSemesters = rootDomainObject.getExecutionPeriodsSet();
            final List<LabelValueBean> executionPeriodLabelValueBeans = new ArrayList<LabelValueBean>();
            for (final ExecutionSemester ep : executionSemesters) {
                if (ep.getState().equals(PeriodState.OPEN) || ep.getState().equals(PeriodState.CURRENT)) {
                    executionPeriodLabelValueBeans.add(new LabelValueBean(ep.getName() + " " + ep.getExecutionYear().getYear(),
                            ep.getExternalId().toString()));
                }
            }
            request.setAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodLabelValueBeans);

            request.setAttribute(PresentationConstants.LABELLIST_WEEKS, weeksLabelValueList);
            if (indexWeek != null) {
                final int xpto = indexWeek.intValue();
                if (xpto < weeks.size()) {
                    today = (Calendar) weeks.get(xpto);
                } else {
                    today = (Calendar) weeks.iterator().next();
                    indexForm.set("indexWeek", new Integer(0));
                }
            }

            try {
                InfoSiteRoomTimeTable component = run(roomKey, today, executionPeriodID);
                request.setAttribute("component", component);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException(e);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            return mapping.findForward("roomViewer");
        }
        throw new FenixActionException();

    }

    private static InfoSiteRoomTimeTable run(RoomKey roomKey, Calendar someDay, String executionPeriodID) throws Exception {
        final Calendar day = new DateTime(someDay.getTimeInMillis()).withField(DateTimeFieldType.dayOfWeek(), 1).toCalendar(null);
        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodID);
        Space room = SpaceUtils.findAllocatableSpaceForEducationByName(roomKey.getNomeSala());
        return RoomSiteComponentBuilder.getInfoSiteRoomTimeTable(day, room,
                executionSemester != null ? executionSemester : ExecutionSemester.readActualExecutionSemester());
    }

}