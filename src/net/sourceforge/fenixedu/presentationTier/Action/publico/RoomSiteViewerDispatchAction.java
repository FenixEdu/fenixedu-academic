package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteFirstPage;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.DateMidnight;

public class RoomSiteViewerDispatchAction extends FenixContextDispatchAction {

  
    public ActionForward curricularCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        String curricularCourseIdString = request.getParameter("ccCode");
        if (curricularCourseIdString == null) {
            curricularCourseIdString = (String) request.getAttribute("ccCode");
        }
        Integer curricularCourseId = new Integer(curricularCourseIdString);
        ISiteComponent curricularCourseComponent = new InfoSiteCurricularCourse();
        readSiteView(request, curricularCourseComponent, null, null, curricularCourseId);

        return returnSuccessMappingForward(mapping, form, request);

    }


    private SiteView readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent,
            Integer infoExecutionCourseCode, Integer sectionIndex, Integer curricularCourseId)
            throws FenixActionException, FenixFilterException, FenixFilterException {
        Integer objectCode = null;
        if (infoExecutionCourseCode == null) {
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null) {
                objectCodeString = (String) request.getAttribute("objectCode");

            }
            objectCode = new Integer(objectCodeString);
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        Object[] args = { commonComponent, firstPageComponent, objectCode, infoExecutionCourseCode,
                sectionIndex, curricularCourseId };

        try {
            ExecutionCourseSiteView siteView = (ExecutionCourseSiteView) ServiceUtils.executeService(
                    null, "ExecutionCourseSiteComponentService", args);

            List curricularCoursesByDegree = ((InfoSiteCommon) siteView.getCommonComponent())
                    .getAssociatedDegreesByDegree();
            Collections.sort(curricularCoursesByDegree, new BeanComparator(
                    "infoDegreeCurricularPlan.infoDegree.sigla"));

            if (siteView != null) {
                if (infoExecutionCourseCode != null) {
                    request.setAttribute("objectCode", ((InfoSiteFirstPage) siteView.getComponent())
                            .getSiteIdInternal());
                } else {
                    request.setAttribute("objectCode", objectCode);
                }

                request.setAttribute("siteView", siteView);
                request.setAttribute("executionCourseCode", ((InfoSiteCommon) siteView
                        .getCommonComponent()).getExecutionCourse().getIdInternal());
                request.setAttribute("sigla", ((InfoSiteCommon) siteView.getCommonComponent())
                        .getExecutionCourse().getSigla());
                request.setAttribute("executionPeriodCode", ((InfoSiteCommon) siteView
                        .getCommonComponent()).getExecutionCourse().getInfoExecutionPeriod()
                        .getIdInternal());

                if (siteView.getComponent() instanceof InfoSiteSection) {
                    request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent())
                            .getSection());
                }
            }
            return siteView;
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A disciplina", e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
    }

    public ActionForward roomViewer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        String roomName = request.getParameter("roomName");
        if (roomName == null) {
            roomName = (String) request.getAttribute("roomName");
        }
        request.setAttribute("roomName", roomName);
        RoomKey roomKey = null;

        if (roomName != null) {
            roomKey = new RoomKey(roomName);

            ISiteComponent bodyComponent = new InfoSiteRoomTimeTable();
            DynaActionForm indexForm = (DynaActionForm) form;
            Integer indexWeek = (Integer) indexForm.get("indexWeek");
            // Integer executionPeriodID = (Integer)
            // indexForm.get("selectedExecutionPeriodID");
            String executionPeriodIDString = request.getParameter("selectedExecutionPeriodID");
            if (executionPeriodIDString == null) {
                executionPeriodIDString = (String) request.getAttribute("selectedExecutionPeriodID");
            }
            Integer executionPeriodID = (executionPeriodIDString != null) ? Integer
                    .valueOf(executionPeriodIDString) : null;
            if (executionPeriodID == null) {
                try {
                    // executionPeriodID = (Integer)
                    // indexForm.get("selectedExecutionPeriodID");
                    executionPeriodID = Integer.valueOf((String) indexForm
                            .get("selectedExecutionPeriodID"));
                } catch (IllegalArgumentException ex) {
                }
            }
            Calendar today = new DateMidnight().toCalendar(null);
            ArrayList weeks = new ArrayList();

            InfoExecutionPeriod executionPeriod;
            if (executionPeriodID == null) {
                executionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView,
                        "ReadCurrentExecutionPeriod", new Object[] {});
                executionPeriodID = executionPeriod.getIdInternal();
                try {
                    indexForm.set("selectedExecutionPeriodID", executionPeriod.getIdInternal()
                            .toString());
                } catch (IllegalArgumentException ex) {
                }
            } else {
                executionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView,
                        "ReadExecutionPeriodByOID", new Object[] { executionPeriodID });
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
                weeksLabelValueList.add(new LabelValueBean(beginWeekString + " - " + endWeekString,
                        new Integer(i).toString()));
                begin.add(Calendar.DATE, 2);
                if (!selectedWeek && indexWeek == null && Calendar.getInstance().before(begin)) {
                    indexForm.set("indexWeek", new Integer(i));
                    selectedWeek = true;
                }
                i++;
            }

            final Collection<ExecutionPeriod> executionPeriods = rootDomainObject
                    .getExecutionPeriodsSet();
            final List<LabelValueBean> executionPeriodLabelValueBeans = new ArrayList<LabelValueBean>();
            for (final ExecutionPeriod ep : executionPeriods) {
                if (ep.getState() == PeriodState.OPEN || ep.getState() == PeriodState.CURRENT) {
                    executionPeriodLabelValueBeans.add(new LabelValueBean(ep.getName() + " "
                            + ep.getExecutionYear().getYear(), ep.getIdInternal().toString()));
                }
            }
            request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD,
                    executionPeriodLabelValueBeans);

            request.setAttribute(SessionConstants.LABELLIST_WEEKS, weeksLabelValueList);
            if (indexWeek != null) {
                today = (Calendar) weeks.get(indexWeek.intValue());
            }
            Object[] args = { bodyComponent, roomKey, today, executionPeriodID };

            try {
                SiteView siteView = (SiteView) ServiceUtils.executeService(null,
                        "RoomSiteComponentServiceByExecutionPeriodID", args);

                request.setAttribute("siteView", siteView);

            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException(e);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            return mapping.findForward("roomViewer");
        }
        throw new FenixActionException();

    }

    private void setFromRequest(HttpServletRequest request) {

        String shift = request.getParameter("shift");
        if (shift == null)
            shift = (String) request.getAttribute("shift");
        if (shift == null)
            shift = new String("false");

        request.setAttribute("shift", shift);

    }

    private ActionForward returnSuccessMappingForward(ActionMapping mapping, ActionForm form,
            HttpServletRequest request) {
        return mapping.findForward("sucess");
    }

}