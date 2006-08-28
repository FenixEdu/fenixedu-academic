/*
 * Created on Jan 18, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author mrsp and jdnf
 */
public class CopySiteExecutionCourseAction extends FenixDispatchAction {

    public ActionForward prepareChooseExecutionPeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        List infoExecutionPeriods = null;

        try {
            infoExecutionPeriods = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods",
                    null);

        } catch (FenixServiceException ex) {
            throw new FenixActionException();
        }
        if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
            // exclude closed execution periods
            infoExecutionPeriods = (List) CollectionUtils.select(infoExecutionPeriods, new Predicate() {

                public boolean evaluate(Object input) {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;
                    if (!infoExecutionPeriod.getState().equals(PeriodState.CLOSED)) {
                        return true;
                    }
                    return false;
                }
            });

            ComparatorChain comparator = new ComparatorChain();
            comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
            comparator.addComparator(new BeanComparator("name"), true);
            Collections.sort(infoExecutionPeriods, comparator);

            List executionPeriodLabels = buildLabelValueBeanForJsp(infoExecutionPeriods);

            request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, executionPeriodLabels);

            readSiteView(request, null, null, null);
        }
        return mapping.findForward("chooseExecutionPeriod");
    }

    private List buildLabelValueBeanForJsp(List infoExecutionPeriods) {

        List executionPeriodLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionPeriods, new Transformer() {

            public Object transform(Object arg0) {

                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;
                String labelPeriod = new String();
                if (infoExecutionPeriod.getName() != null && infoExecutionPeriod.getName().length() > 0) {
                    labelPeriod = labelPeriod.concat(infoExecutionPeriod.getName() + " - ");
                }
                LabelValueBean executionPeriod = new LabelValueBean(labelPeriod
                        .concat(infoExecutionPeriod.getInfoExecutionYear().getYear()),
                        infoExecutionPeriod.getName() + " - "
                                + infoExecutionPeriod.getInfoExecutionYear().getYear() + "~"
                                + infoExecutionPeriod.getIdInternal().toString());
                return executionPeriod;
            }
        }, executionPeriodLabels);
        return executionPeriodLabels;
    }

    public ActionForward prepareChooseExecDegreeAndCurYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        buildCurricularYearLabelValueBean(request);

        Integer executionPeriodId = separateLabel(form, request, "executionPeriod", "executionPeriodId",
                "executionPeriodName");

        if (executionPeriodId == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("error.no.executionPeriod"));
            saveErrors(request, errors);
            return prepareChooseExecutionPeriod(mapping, form, request, response);
        }

        Object args[] = { executionPeriodId };
        List executionDegreeList = null;
        try {
            executionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionPeriodId", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        ArrayList courses = new ArrayList();
        courses.add(new LabelValueBean("escolher", ""));
        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        buildExecutionDegreeLabelValueBean(executionDegreeList, courses);
        request.setAttribute(SessionConstants.DEGREES, courses);

        readSiteView(request, null, null, null);

        return mapping.findForward("chooseExecDegreeAndCurYear");
    }

    private void buildExecutionDegreeLabelValueBean(List executionDegreeList, ArrayList courses) {

        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();
            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " em " + name;
            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";
            courses.add(new LabelValueBean(name, name + "~"
                    + infoExecutionDegree.getIdInternal().toString()));
        }
    }

    private void buildCurricularYearLabelValueBean(HttpServletRequest request) {

        ArrayList curricularYears = new ArrayList();
        curricularYears.add(new LabelValueBean("escolher", "0"));
        curricularYears.add(new LabelValueBean("1 ", "1"));
        curricularYears.add(new LabelValueBean("2 ", "2"));
        curricularYears.add(new LabelValueBean("3 ", "3"));
        curricularYears.add(new LabelValueBean("4 ", "4"));
        curricularYears.add(new LabelValueBean("5 ", "5"));
        request.setAttribute(SessionConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);
    }

    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {

        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;
        }
        return false;
    }

    public ActionForward showExecutionCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer executionPeriodId = separateLabel(form, request, "executionPeriod", "executionPeriodId",
                "executionPeriodName");

        DynaActionForm copySiteForm = (DynaValidatorForm) form;
        Integer executionDegreeId = null;
        Integer curYear = null;
        Integer objectCode = null;
        executionDegreeId = separateLabel(form, request, "executionDegree", "executionDegreeId",
                "executionDegreeName");
        if (executionDegreeId == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("error.no.executionDegree"));
            saveErrors(request, errors);
            return prepareChooseExecDegreeAndCurYear(mapping, form, request, response);
        }
        curYear = Integer.valueOf((String) copySiteForm.get("curYear"));
        if (curYear.equals(new Integer(0))) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("error.no.curYear"));
            saveErrors(request, errors);
            return prepareChooseExecDegreeAndCurYear(mapping, form, request, response);
        }
        request.setAttribute("curYear", curYear);
        objectCode = Integer.valueOf((String) copySiteForm.get("objectCode"));
        request.setAttribute("objectCode", objectCode);

        Object args[] = { executionDegreeId, executionPeriodId, curYear };
        List infoExecutionCourses;
        try {
            infoExecutionCourses = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
        request.setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, infoExecutionCourses);

        readSiteView(request, null, null, null);

        return mapping.findForward("viewExecutionCourses");
    }

    private Integer separateLabel(ActionForm form, HttpServletRequest request, String property,
            String id, String name) {

        DynaActionForm copySiteForm = (DynaActionForm) form;

        // the value returned to action is a string name~idInternal
        String object = (String) copySiteForm.get(property);
        if (object == null || object.length() <= 0) {
            object = (String) request.getAttribute(property);
            if (object == null) {
                object = request.getParameter(property);
            }
        }

        Integer objectId = null;
        String objectName = null;
        if (object != null && object.length() > 0 && object.indexOf("~") > 0) {
            copySiteForm.set(property, object);
            request.setAttribute(property, object);

            objectId = Integer.valueOf(StringUtils.substringAfter(object, "~"));
            objectName = object.substring(0, object.indexOf("~"));

            request.setAttribute(name, objectName);
            request.setAttribute(id, objectId);
        }

        return objectId;
    }

    public ActionForward chooseFeaturesToCopy(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixActionException {

        DynaActionForm copySiteForm = (DynaActionForm) form;
        copySiteForm.set("objectCode", request.getParameter("objectCode")); // executionCourseIDTo
        copySiteForm.set("executionCourseID", new Integer(request.getParameter("executionCourseId"))); // executionCourseIDFrom

        readSiteView(request, null, null, null);

        return mapping.findForward("chooseFeaturesToCopy");
    }

    public ActionForward copySite(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm copySiteForm = (DynaActionForm) form;

        Integer executionCourseIDFrom = (Integer) copySiteForm.get("executionCourseID");
        Integer executionCourseIDTo = Integer.valueOf((String) copySiteForm.get("objectCode"));
        Boolean sectionsAndItems = (Boolean) copySiteForm.get("sectionsAndItems");
        Boolean bibliographicReference = (Boolean) copySiteForm.get("bibliographicReference");
        Boolean evaluationMethod = (Boolean) copySiteForm.get("evaluationMethod");

        Object[] args = { executionCourseIDFrom, executionCourseIDTo, sectionsAndItems,
                bibliographicReference, evaluationMethod };
        try {
            ServiceManagerServiceFactory.executeService(userView, "CopySiteExecutionCourse", args);

        } catch (Exception exception) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("impossible.copy.site", new ActionError("error.copySite.impossible.copy"));

            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }

        return mapping.findForward("manageExecutionCourses");
    }

    private SiteView readSiteView(HttpServletRequest request,
            Integer infoExecutionCourseCode, Object obj1, Object obj2) throws FenixActionException,
            FenixFilterException {

        HttpSession session = getSession(request);

        IUserView userView = getUserView(request);

        Integer objectCode = null;
        if (infoExecutionCourseCode == null) {
            objectCode = getObjectCode(request);
            infoExecutionCourseCode = objectCode;
        }

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourseCode);
        final InfoSite infoSite = InfoSite.newInfoFromDomain(executionCourse.getSite());

        ISiteComponent commonComponent = new InfoSiteCommon();
        Object[] args = { infoExecutionCourseCode, commonComponent, infoSite, objectCode,
                obj1, obj2 };

        try {
            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceUtils
                    .executeService(userView, "TeacherAdministrationSiteComponentService", args);
            request.setAttribute("siteView", siteView);
            request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse().getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection) {
                request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent())
                        .getSection());
            }

            return siteView;

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

    }

    private Integer getObjectCode(HttpServletRequest request) {
        Integer objectCode = null;
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        if (objectCodeString != null) {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }
}
