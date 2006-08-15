package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

/*
 * 
 * @author Fernanda Quitério 23/Dez/2003
 *  
 */
public class EditExecutionCourseManageCurricularCoursesDispatchAction extends FenixDispatchAction {

    public ActionForward dissociateCurricularCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer executionCourseId = new Integer(getAndSetStringToRequest(request, "executionCourseId"));
        Integer curricularCourseId = new Integer(getAndSetStringToRequest(request, "curricularCourseId"));

        separateLabel(form, request, "executionPeriod", "executionPeriodId", "executionPeriodName");

        String executionCoursesNotLinked = getAndSetStringToRequest(request, "executionCoursesNotLinked");
        if (executionCoursesNotLinked == null || executionCoursesNotLinked.equals("null")
                || executionCoursesNotLinked.equals(Boolean.FALSE.toString())) {
            separateLabel(form, request, "executionDegree", "executionDegreeId", "executionDegreeName");
        }

        Object[] args = { executionCourseId, curricularCourseId };
        try {
            ServiceUtils.executeService(userView, "DissociateCurricularCourseByExecutionCourseId", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("editExecutionCourse");
    }

    public ActionForward prepareAssociateCurricularCourseChooseDegreeCurricularPlan(
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        getAndSetStringToRequest(request, "executionCourseId");
        getAndSetStringToRequest(request, "executionCourseName");

        Integer executionPeriodId = separateLabel(form, request, "executionPeriod", "executionPeriodId",
                "executionPeriodName");

        String executionCoursesNotLinked = getAndSetStringToRequest(request, "executionCoursesNotLinked");
        if (executionCoursesNotLinked == null || executionCoursesNotLinked.equals("null")
                || executionCoursesNotLinked.equals(Boolean.FALSE.toString())) {
            separateLabel(form, request, "executionDegree", "executionDegreeId", "executionDegreeName");
            getAndSetStringToRequest(request, "curYear");
        }

        Object args[] = { executionPeriodId };
        List executionDegreeList = null;
        try {
            executionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionPeriodId", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List courses = new ArrayList();
        courses.add(new LabelValueBean("escolher", ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        buildExecutionDegreeLabelValueBean(executionDegreeList, courses);

        request.setAttribute(SessionConstants.DEGREES, courses);

        return mapping.findForward("prepareAssociateCurricularCourseChooseDegreeCurricularPlan");
    }

    private void buildExecutionDegreeLabelValueBean(List executionDegreeList, List courses) {

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
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal().toString()));
        }
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

    public ActionForward prepareAssociateCurricularCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        getAndSetStringToRequest(request, "executionCourseId");
        getAndSetStringToRequest(request, "executionCourseName");
        separateLabel(form, request, "executionPeriod", "executionPeriodId", "executionPeriodName");

        String executionCoursesNotLinked = getAndSetStringToRequest(request, "executionCoursesNotLinked");
        if (executionCoursesNotLinked == null || executionCoursesNotLinked.equals("null")
                || executionCoursesNotLinked.equals(Boolean.FALSE.toString())) {
            separateLabel(form, request, "executionDegree", "executionDegreeId", "executionDegreeName");
            getAndSetStringToRequest(request, "curYear");
        }

        Integer degreeCurricularPlanId = separateLabel(form, request, "degreeCurricularPlan", "degreeCurricularPlanId", "degreeCurricularPlanName");
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);

        final Integer executionCourseID = Integer.valueOf((String) request.getAttribute("executionCourseId"));
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();

        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        for (final DegreeModule degreeModule : rootDomainObject.getDegreeModulesSet()) {
        	if (degreeModule instanceof CurricularCourse) {
        		final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
        		if (!executionCourse.getAssociatedCurricularCoursesSet().contains(curricularCourse)) { 
        			if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(null, degreeCurricularPlan, executionPeriod)) {
        				infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        			}
        		}
        	}
        }
        Collections.sort(infoCurricularCourses, new BeanComparator("name"));

        request.setAttribute("infoCurricularCourses", infoCurricularCourses);

        return mapping.findForward("associateCurricularCourse");
    }

    public ActionForward associateCurricularCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm executionCourseForm = (DynaValidatorForm) form;
        getAndSetStringToRequest(request, "executionPeriodId");
        getAndSetStringToRequest(request, "executionPeriodName");
        String executionCourseId = getAndSetStringToRequest(request, "executionCourseId");

        Integer curricularCoursesListSize = (Integer) executionCourseForm
                .get("curricularCoursesListSize");

        List curricularCourseIds = getInformationToDissociate(request, curricularCoursesListSize,
                "curricularCourse", "idInternal", "chosen");

        Object args[] = { Integer.valueOf(executionCourseId), curricularCourseIds };
        try {
            ServiceUtils.executeService(userView, "AssociateCurricularCoursesToExecutionCourse", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return mapping.findForward("editExecutionCourse");
    }

    private List getInformationToDissociate(HttpServletRequest request,
            Integer curricularCoursesListSize, String what, String property, String formProperty) {

        List informationToDeleteList = new ArrayList();
        for (int i = 0; i < curricularCoursesListSize.intValue(); i++) {
            Integer informationToDelete = dataToDelete(request, i, what, property, formProperty);
            if (informationToDelete != null) {
                informationToDeleteList.add(informationToDelete);
            }
        }
        return informationToDeleteList;
    }

    private Integer dataToDelete(HttpServletRequest request, int index, String what, String property,
            String formProperty) {

        Integer itemToDelete = null;
        String checkbox = request.getParameter(what + "[" + index + "]." + formProperty);
        String toDelete = null;
        if (checkbox != null
                && (checkbox.equals("on") || checkbox.equals("yes") || checkbox.equals("true"))) {
            toDelete = request.getParameter(what + "[" + index + "]." + property);
        }
        if (toDelete != null) {
            itemToDelete = new Integer(toDelete);
        }
        return itemToDelete;
    }

    private Integer separateLabel(ActionForm form, HttpServletRequest request, String property,
            String id, String name) {

        DynaActionForm executionCourseForm = (DynaActionForm) form;

        // the value returned to action is a string name~idInternal
        String object = (String) executionCourseForm.get(property);
        if (object == null || object.length() <= 0) {
            object = (String) request.getAttribute(property);
            if (object == null) {
                object = request.getParameter(property);
            }
        }

        Integer objectId = null;
        String objectName = null;
        if (object != null && object.length() > 0 && object.indexOf("~") > 0) {
            executionCourseForm.set(property, object);
            request.setAttribute(property, object);

            objectId = Integer.valueOf(StringUtils.substringAfter(object, "~"));
            request.setAttribute(id, objectId);

            objectName = object.substring(0, object.indexOf("~"));
            request.setAttribute(name, objectName);
        }

        return objectId;
    }

    private String getAndSetStringToRequest(HttpServletRequest request, String name) {
        return RequestUtils.getAndSetStringToRequest(request, name);
    }

}