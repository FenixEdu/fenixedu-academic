/*
 * Created 2004/10/24
 * 
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularCourseByID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionCourseByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.TransferCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Luis Cruz
 * @version 1.1, Oct 24, 2004
 * @since 1.1
 * 
 */
@Mapping(module = "manager", path = "/editExecutionCourseTransferCurricularCourses", input = "...",
        attribute = "executionCourseTransferCurricularCourseForm", formBean = "executionCourseTransferCurricularCourseForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "completedTransfer", path = "/editExecutionCourse.do?method=editExecutionCourse"),
        @Forward(name = "showPage", path = "/manager/executionCourseManagement/transferCurricularCourse.jsp") })
@Deprecated
public class EditExecutionCourseTransferCurricularCoursesDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTransferCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        Integer executionCourseId = Integer.valueOf(RequestUtils.getAndSetStringToRequest(request, "executionCourseId"));
        Integer curricularCourseId = Integer.valueOf(RequestUtils.getAndSetStringToRequest(request, "curricularCourseId"));
        Integer executionPeriodId =
                Integer.valueOf(separateLabel(request, "executionPeriod", "executionPeriodId", "executionPeriodName"));

        InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseByOID.run(executionCourseId);
        request.setAttribute("infoExecutionCourse", infoExecutionCourse);

        InfoCurricularCourse infoCurricularCourse = ReadCurricularCourseByID.run(curricularCourseId);
        request.setAttribute("infoCurricularCourse", infoCurricularCourse);

        List<InfoExecutionDegree> executionDegrees = ReadExecutionDegreesByExecutionPeriodId.run(executionPeriodId);
        Collection executionDegreesLabelValueList = RequestUtils.buildExecutionDegreeLabelValueBean(executionDegrees);
        request.setAttribute("executionDegrees", executionDegreesLabelValueList);

        List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
        request.setAttribute("curricularYears", curricularYears);

        prepareReturnSessionBean(request,
                Boolean.valueOf(RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked")),
                executionCourseId.toString());

        return mapping.findForward("showPage");
    }

    public ActionForward selectExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        RequestUtils.getAndSetStringToRequest(request, "curricularCourseId");
        Integer executionPeriodId =
                Integer.valueOf(separateLabel(request, "executionPeriod", "executionPeriodId", "executionPeriodName"));

        String destinationExecutionDegreeIdString = (String) dynaActionForm.get("destinationExecutionDegreeId");
        String curricularYearString = (String) dynaActionForm.get("curricularYear");

        if (!StringUtils.isEmpty(destinationExecutionDegreeIdString) && !StringUtils.isEmpty(curricularYearString)
                && StringUtils.isNumeric(destinationExecutionDegreeIdString) && StringUtils.isNumeric(curricularYearString)) {
            Integer destinationExecutionDegreeId = Integer.valueOf(destinationExecutionDegreeIdString);
            Integer curricularYear = Integer.valueOf(curricularYearString);

            List executionCourses =
                    ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear.run(destinationExecutionDegreeId,
                            executionPeriodId, curricularYear);
            request.setAttribute("executionCourses", executionCourses);
        }

        return prepareTransferCurricularCourse(mapping, form, request, response);
    }

    public ActionForward transferCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String executionCourseIdString = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String curricularCourseIdString = RequestUtils.getAndSetStringToRequest(request, "curricularCourseId");

        String destinationExecutionDegreeIdString = (String) dynaActionForm.get("destinationExecutionDegreeId");
        String curricularYearString = (String) dynaActionForm.get("curricularYear");
        String destinationExecutionCourseIdString = (String) dynaActionForm.get("destinationExecutionCourseId");

        try {

            if (StringUtils.isEmpty(destinationExecutionDegreeIdString)
                    || !StringUtils.isNumeric(destinationExecutionDegreeIdString)) {
                throw new DomainException("error.selection.noDegree");
            }

            if (StringUtils.isEmpty(curricularYearString) || !StringUtils.isNumeric(curricularYearString)) {
                throw new DomainException("error.selection.noCurricularYear");
            }

            TransferCurricularCourse.run(executionCourseIdString, curricularCourseIdString, destinationExecutionCourseIdString);

            CurricularCourse curricularCourseForMessage =
                    (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(
                            Integer.valueOf(curricularCourseIdString));
            ExecutionCourse executionCourseForMessage =
                    RootDomainObject.getInstance().readExecutionCourseByOID(Integer.valueOf(destinationExecutionCourseIdString));
            String curricularCourseName = curricularCourseForMessage.getNameI18N().getContent();
            if (StringUtils.isEmpty(curricularCourseName)) {
                curricularCourseName = curricularCourseForMessage.getName();
            }
            String executionCourseName = executionCourseForMessage.getNameI18N().getContent();
            if (StringUtils.isEmpty(executionCourseName)) {
                executionCourseName = executionCourseForMessage.getName();
            }

            addActionMessage("success", request, "message.manager.executionCourseManagement.transferCourse.success",
                    curricularCourseName, curricularCourseForMessage.getDegree().getSigla(), executionCourseName,
                    executionCourseForMessage.getDegreePresentationString());
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            if (!e.getKey().equals("error.manager.executionCourseManagement.transferCurricularCourse.gone")) {
                return selectExecutionDegree(mapping, form, request, response);
            }
        }
        separateLabel(request, "executionPeriod", "executionPeriodId", "executionPeriodName");
        prepareReturnSessionBean(request,
                Boolean.valueOf(RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked")),
                executionCourseIdString);

        return mapping.findForward("completedTransfer");
    }

    private void prepareReturnSessionBean(HttpServletRequest request, Boolean chooseNotLinked, String executionCourseId) {
        // Preparing sessionBean for the EditExecutionCourseDA.list...Actions... (needed on the editExecutionCourse page)
        Integer executionPeriodId = Integer.valueOf((String) request.getAttribute("executionPeriodId"));

        ExecutionCourse executionCourse = null;
        if (!StringUtils.isEmpty(executionCourseId)) {
            executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(Integer.valueOf(executionCourseId));
        }
        ExecutionSemester executionPeriod = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);

        ExecutionCourseBean sessionBean = new ExecutionCourseBean();

        sessionBean.setSourceExecutionCourse(executionCourse);
        sessionBean.setExecutionSemester(executionPeriod);
        sessionBean.setChooseNotLinked(chooseNotLinked);

        if (!chooseNotLinked) {
            separateLabel(request, "executionDegree", "executionDegreeId", "executionDegreeName");
            separateLabel(request, "curYear", "curYearId", "curYearName");

            Integer executionDegreeId = Integer.valueOf((String) request.getAttribute("executionDegreeId"));
            Integer curricularYearId = Integer.valueOf((String) request.getAttribute("curYearId"));

            ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);
            CurricularYear curYear = RootDomainObject.getInstance().readCurricularYearByOID(curricularYearId);

            sessionBean.setExecutionDegree(executionDegree);
            sessionBean.setCurricularYear(curYear);
        }

        request.setAttribute("sessionBean", sessionBean);
    }

    private String separateLabel(HttpServletRequest request, String property, String id, String name) {

        // the value returned to action is a string name~externalId
        String object = (String) request.getAttribute(property);
        if (StringUtils.isEmpty(object)) {
            object = request.getParameter(property);
        }

        String objectId = null;
        String objectName = null;
        if (!StringUtils.isEmpty(object) && object.indexOf("~") > 0) {
            request.setAttribute(property, object);

            objectId = org.apache.commons.lang.StringUtils.substringAfter(object, "~");
            objectName = object.substring(0, object.indexOf("~"));

            request.setAttribute(name, objectName);
            request.setAttribute(id, objectId);
        }

        return objectId;
    }
}
