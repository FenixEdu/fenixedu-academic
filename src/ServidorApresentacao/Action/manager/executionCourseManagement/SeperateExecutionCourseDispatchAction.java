package ServidorApresentacao.Action.manager.executionCourseManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.utils.RequestUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class SeperateExecutionCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTransfer(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) ServiceManagerServiceFactory
                .executeService(userView, "ReadExecutionCourseWithShiftsAndCurricularCoursesByOID",
                        new Object[] { executionCourseId });
        request.setAttribute("infoExecutionCourse", infoExecutionCourse);

        List executionDegrees = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadExecutionDegreesByExecutionPeriodId", new Object[] { infoExecutionCourse
                        .getInfoExecutionPeriod().getIdInternal() });
        transformExecutionDegreesIntoLabelValueBean(executionDegrees);
        request.setAttribute("executionDegrees", executionDegrees);

        List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
        request.setAttribute(SessionConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);

        return mapping.findForward("showSeperationPage");
    }

    private void transformExecutionDegreesIntoLabelValueBean(List executionDegreeList) {
        CollectionUtils.transform(executionDegreeList, new Transformer() {

            public Object transform(Object arg0) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
                StringBuffer label = new StringBuffer(infoExecutionDegree.getInfoDegreeCurricularPlan()
                        .getInfoDegree().getTipoCurso().toString());
                label.append(" em ");
                label
                        .append(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                                .getNome());

                return new LabelValueBean(label.toString(), infoExecutionDegree.getIdInternal()
                        .toString());
            }

        });

        Collections.sort(executionDegreeList, new BeanComparator("label"));
        executionDegreeList.add(0, new LabelValueBean("escolher", ""));
    }

    public ActionForward changeDestinationContext(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException {

        prepareTransfer(mapping, form, request, response);

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String destinationExecutionDegreeId = (String) dynaActionForm
                .get("destinationExecutionDegreeId");
        String destinationCurricularYear = (String) dynaActionForm.get("destinationCurricularYear");

        if (isSet(destinationExecutionDegreeId) && isSet(destinationCurricularYear)) {
            IUserView userView = SessionUtils.getUserView(request);

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                    .getAttribute("infoExecutionCourse");

            Object args[] = { new Integer(destinationExecutionDegreeId),
                    infoExecutionCourse.getInfoExecutionPeriod().getIdInternal(),
                    new Integer(destinationCurricularYear) };
            List executionCourses = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear", args);
            Collections.sort(executionCourses, new BeanComparator("nome"));
            request.setAttribute("executionCourses", executionCourses);
        }

        return mapping.findForward("showSeperationPage");
    }

    public ActionForward transfer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixFilterException {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
        String destinationExecutionCourseIDString = (String) dynaActionForm
                .get("destinationExecutionCourseID");
        String[] shiftIdsToTransfer = (String[]) dynaActionForm.get("shiftIdsToTransfer");
        String[] curricularCourseIdsToTransfer = (String[]) dynaActionForm
                .get("curricularCourseIdsToTransfer");

        IUserView userView = SessionUtils.getUserView(request);

        Integer destinationExecutionCourseID = null;
        if (destinationExecutionCourseIDString != null
                && destinationExecutionCourseIDString.length() > 0
                && StringUtils.isNumeric(destinationExecutionCourseIDString)) {
            destinationExecutionCourseID = new Integer(destinationExecutionCourseIDString);
        }

        ServiceManagerServiceFactory.executeService(userView, "SeperateExecutionCourse", new Object[] {
                executionCourseId, destinationExecutionCourseID, makeIntegerArray(shiftIdsToTransfer),
                makeIntegerArray(curricularCourseIdsToTransfer) });

        return mapping.findForward("returnFromTransfer");
    }

    private Integer[] makeIntegerArray(String[] stringArray) {
        Integer[] integerArray = new Integer[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {
            integerArray[i] = new Integer(stringArray[i]);
        }

        return integerArray;
    }

    private boolean isSet(String parameter) {
        return parameter != null && parameter.length() > 0 && StringUtils.isNumeric(parameter);
    }

}