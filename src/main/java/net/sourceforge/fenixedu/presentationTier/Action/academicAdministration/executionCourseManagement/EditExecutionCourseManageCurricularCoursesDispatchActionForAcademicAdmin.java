package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodIdAcademicAdmin;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.EditExecutionCourseManageCurricularCoursesDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/*
 * 
 * @author Fernanda Quitério 23/Dez/2003
 *  
 */
@Mapping(module = "academicAdministration", path = "/editExecutionCourseManageCurricularCourses",
        input = "/editExecutionCourse.do?method=prepareEditExecutionCourse&page=0", attribute = "executionCourseForm",
        formBean = "executionCourseForm", scope = "request", parameter = "method")
@Forwards(
        value = {
                @Forward(name = "editExecutionCourse", path = "/editExecutionCourse.do?method=editExecutionCourse&page=0"),
                @Forward(name = "manageCurricularSeparation",
                        path = "/seperateExecutionCourse.do?method=manageCurricularSeparation"),
                @Forward(name = "associateCurricularCourse",
                        path = "/academicAdministration/executionCourseManagement/associateCurricularCourse.jsp"),
                @Forward(
                        name = "prepareAssociateCurricularCourseChooseDegreeCurricularPlan",
                        path = "/academicAdministration/executionCourseManagement/prepareAssociateCurricularCourseChooseDegreeCurricularPlan.jsp") })
public class EditExecutionCourseManageCurricularCoursesDispatchActionForAcademicAdmin extends
        EditExecutionCourseManageCurricularCoursesDispatchAction {

    @Override
    public ActionForward prepareAssociateCurricularCourseChooseDegreeCurricularPlan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        //TODO: check & clean up attributes that are not needed
        //processing attributes
        String executionPeriodId = RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");
        List<InfoExecutionDegree> executionDegreeList = null;
        try {
            executionDegreeList = ReadExecutionDegreesByExecutionPeriodIdAcademicAdmin.run(Integer.valueOf(executionPeriodId));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List<LabelValueBean> degrees = new ArrayList<LabelValueBean>();
        degrees.add(new LabelValueBean(BundleUtil.getStringFromResourceBundle("resources.RendererResources",
                "renderers.menu.default.title"), ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
        buildExecutionDegreeLabelValueBean(executionDegreeList, degrees);
        request.setAttribute(PresentationConstants.DEGREES, degrees);

        //destination attributes
        String executionCoursesNotLinked = RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked");
        if (StringUtils.isEmpty(executionCoursesNotLinked) || !Boolean.valueOf(executionCoursesNotLinked)) {
            RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
            String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
            ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(originExecutionDegreeId));
            request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());

        }
        RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        RequestUtils.getAndSetStringToRequest(request, "executionCourseName");
        ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(Integer.valueOf(executionPeriodId));
        request.setAttribute("executionPeriodName", executionSemester.getQualifiedName());
        return mapping.findForward("prepareAssociateCurricularCourseChooseDegreeCurricularPlan");
    }

}
