package ServidorApresentacao.Action.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.PeriodState;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Fernanda Quitério 19/Dez/2003
 *  
 */
public class EditExecutionCourseDispatchAction extends FenixDispatchAction
{
    public ActionForward prepareEditECChooseExecutionPeriod(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

        List infoExecutionPeriods = null;
        try
        {
            infoExecutionPeriods =
                (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);
        }
        catch (FenixServiceException ex)
        {
            throw new FenixActionException();
        }

        if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty())
        {
            // exclude closed execution periods
            infoExecutionPeriods = (List) CollectionUtils.select(infoExecutionPeriods, new Predicate()
            {
                public boolean evaluate(Object input)
                {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;
                    if (!infoExecutionPeriod.getState().equals(PeriodState.CLOSED))
                    {
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
        }
        return mapping.findForward("prepareEditECChooseExecutionPeriod");
    }

    private List buildLabelValueBeanForJsp(List infoExecutionPeriods)
    {
        List executionPeriodLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionPeriods, new Transformer()
        {
            public Object transform(Object arg0)
            {
                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

                LabelValueBean executionPeriod =
                    new LabelValueBean(
                        infoExecutionPeriod.getName()
                            + " - "
                            + infoExecutionPeriod.getInfoExecutionYear().getYear(),
                        infoExecutionPeriod.getName()
                            + " - "
                            + infoExecutionPeriod.getInfoExecutionYear().getYear()
                            + "#"
                            + infoExecutionPeriod.getIdInternal().toString());
                return executionPeriod;
            }
        }, executionPeriodLabels);
        return executionPeriodLabels;
    }

    public ActionForward prepareEditECChooseExecDegreeAndCurYear(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

        buildCurricularYearLabelValueBean(request);

        Integer executionPeriodId =
            separateLabel(form, request, "executionPeriod", "executionPeriodId", "executionPeriodName");

        Object args[] = { executionPeriodId };
        List executionDegreeList = null;
        try
        {
            executionDegreeList =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadExecutionDegreesByExecutionPeriodId",
                    args);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        ArrayList courses = new ArrayList();
        courses.add(new LabelValueBean("escolher", ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        buildExecutionDegreeLabelValueBean(executionDegreeList, courses);

        request.setAttribute(SessionConstants.DEGREES, courses);
        return mapping.findForward("prepareEditECChooseExecDegreeAndCurYear");
    }

    private Integer separateLabel(
        ActionForm form,
        HttpServletRequest request,
        String property,
        String id,
        String name)
    {
        DynaActionForm executionCourseForm = (DynaActionForm) form;
        // the value returned to action is a string name#idInternal
        String object = (String) executionCourseForm.get(property);
        Integer objectId = Integer.valueOf(StringUtils.substringAfter(object, "#"));
        object = object.substring(0, object.indexOf("#"));
        request.setAttribute(name, object);
        request.setAttribute(id, objectId);
        return objectId;
    }

    private void buildExecutionDegreeLabelValueBean(List executionDegreeList, ArrayList courses)
    {
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext())
        {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name =
                infoExecutionDegree
                    .getInfoDegreeCurricularPlan()
                    .getInfoDegree()
                    .getTipoCurso()
                    .toString()
                    + " em "
                    + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
                ? "-" + infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
                : "";

            courses.add(
                new LabelValueBean(name, name + "#" + infoExecutionDegree.getIdInternal().toString()));
        }
    }

    private void buildCurricularYearLabelValueBean(HttpServletRequest request)
    {
        //		Create bean for curricular years
        ArrayList curricularYears = new ArrayList();
        curricularYears.add(new LabelValueBean("escolher", ""));
        curricularYears.add(new LabelValueBean("1 º", "1"));
        curricularYears.add(new LabelValueBean("2 º", "2"));
        curricularYears.add(new LabelValueBean("3 º", "3"));
        curricularYears.add(new LabelValueBean("4 º", "4"));
        curricularYears.add(new LabelValueBean("5 º", "5"));
        request.setAttribute(SessionConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);
    }

    private boolean duplicateInfoDegree(
        List executionDegreeList,
        InfoExecutionDegree infoExecutionDegree)
    {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext())
        {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }

    public ActionForward prepareEditExecutionCourse(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm executionCourseForm = (DynaValidatorForm) form;

        Integer executionPeriodId =
            Integer.valueOf((String) executionCourseForm.get("executionPeriodId"));
        request.setAttribute("executionPeriodId", executionPeriodId.toString());
       

        Boolean getNotLinked = (Boolean) executionCourseForm.get("executionCoursesNotLinked");
        Integer executionDegreeId = null;
        Integer curYear = null;
        if (getNotLinked == null || getNotLinked.equals(Boolean.FALSE))
        {
            executionDegreeId =
                separateLabel(
                    form,
                    request,
                    "executionDegreeId",
                    "executionDegreeId",
                    "executionDegreeName");
            curYear = Integer.valueOf((String) executionCourseForm.get("curYear"));
            request.setAttribute("executionDegreeId", executionDegreeId);
            request.setAttribute("curYear", curYear);
        }
        else
        {
            request.setAttribute("executionDegreeName", "");
            request.setAttribute("executionCoursesNotLinked", getNotLinked.toString());
        }
        Object args[] = { executionDegreeId, executionPeriodId, curYear };

        List infoExecutionCourses;
        try
        {
            infoExecutionCourses =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear",
                    args);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));

        request.setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, infoExecutionCourses);

        return mapping.findForward("prepareEditExecutionCourse");
    }

    private String getAndSetStringToRequest(HttpServletRequest request, String name)
    {
        String parameter = request.getParameter(name);
        if (parameter == null)
        {
            parameter = (String) request.getAttribute(name);
        }
        request.setAttribute(name, parameter);
        return parameter;
    }

    public ActionForward editExecutionCourse(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

      
        String executionCourseId = getAndSetStringToRequest(request, "executionCourseId");
      

        Object args[] = { Integer.valueOf(executionCourseId)};

        InfoExecutionCourse infoExecutionCourse;
        try
        {
            infoExecutionCourse =
                (InfoExecutionCourse) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadInfoExecutionCourseByOID",
                    args);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        Collections.sort(
            infoExecutionCourse.getAssociatedInfoCurricularCourses(),
            new BeanComparator("name"));

        request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourse);
        return mapping.findForward("editExecutionCourse");
    }

    public ActionForward deleteExecutionCourse(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

        String executionPeriodId = getAndSetStringToRequest(request, "executionPeriodId");
        String executionCourseId = getAndSetStringToRequest(request, "executionCourseId");
        String executionDegreeName = getAndSetStringToRequest(request, "executionDegreeName");
        //		List internalIds = Arrays.asList((Integer[])
		// deleteForm.get("internalIds"));

        List internalIds = new ArrayList();
        internalIds.add(new Integer(executionCourseId));

        List errorCodes = new ArrayList();
        Object args[] = { internalIds };
        try
        {
            errorCodes = (List) ServiceUtils.executeService(userView, "DeleteExecutionCourses", args);
        }
        catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (!errorCodes.isEmpty())
        {
            String executionDegreeId = getAndSetStringToRequest(request, "executionDegreeId");
            String curYear = getAndSetStringToRequest(request, "curYear");
            String getNotLinked = getAndSetStringToRequest(request, "executionCoursesNotLinked");

            DynaActionForm executionCourseForm = (DynaValidatorForm) form;

            executionCourseForm.set("curYear", curYear);
            executionCourseForm.set("executionDegreeId", executionDegreeName + "#" + executionDegreeId);
            executionCourseForm.set("executionPeriodId", executionPeriodId);
            executionCourseForm.set("executionCoursesNotLinked", Boolean.valueOf(getNotLinked));

            ActionErrors actionErrors = new ActionErrors();
            Iterator codesIter = errorCodes.iterator();
            ActionError error = null;
            while (codesIter.hasNext())
            {
                error =
                    new ActionError(
                        "errors.invalid.delete.not.empty.execution.course",
                        codesIter.next());
                actionErrors.add("errors.invalid.delete.not.empty.execution.course", error);
            }
            saveErrors(request, actionErrors);
        }

        return prepareEditExecutionCourse(mapping, form, request, response);
    }
}