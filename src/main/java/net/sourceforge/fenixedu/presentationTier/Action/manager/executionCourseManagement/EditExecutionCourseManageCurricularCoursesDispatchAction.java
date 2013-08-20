package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.AssociateCurricularCoursesToExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.DissociateCurricularCourseByExecutionCourseId;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

/*
 * 
 * @author Fernanda Quitério 23/Dez/2003
 *  
 */
public class EditExecutionCourseManageCurricularCoursesDispatchAction extends FenixDispatchAction {

    public ActionForward dissociateCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String curricularCourseId = RequestUtils.getAndSetStringToRequest(request, "curricularCourseId");
        ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
        String executionCourseName = executionCourse.getName() + " [" + executionCourse.getDegreePresentationString() + "]";

        try {
            DissociateCurricularCourseByExecutionCourseId.run(executionCourseId, curricularCourseId);
            CurricularCourse curricularCourse = AbstractDomainObject.fromExternalId(curricularCourseId);
            addActionMessage("success", request, "message.manager.executionCourseManagement.dissociate.success",
                    curricularCourse.getName(), curricularCourse.getDegreeCurricularPlan().getName());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Set<Degree> degrees = executionCourse.getDegreesSortedByDegreeName();
        // destination attributes
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        ExecutionDegree originExecutionDegree = AbstractDomainObject.fromExternalId(originExecutionDegreeId);
        request.setAttribute("originExecutionDegreeName", originExecutionDegree.getPresentationName());
        Boolean chooseNotLinked = Boolean.valueOf(RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked"));
        String curricularYearId = RequestUtils.getAndSetStringToRequest(request, "curricularYearId");

        if (!degrees.contains(originExecutionDegree.getDegree())) {
            ExecutionCourseBean sessionBean = new ExecutionCourseBean();
            sessionBean.setSourceExecutionCourse(executionCourse);
            sessionBean.setExecutionSemester(executionCourse.getExecutionPeriod());
            sessionBean.setChooseNotLinked(chooseNotLinked);
            CurricularYear curYear = AbstractDomainObject.fromExternalId(curricularYearId);
            sessionBean.setExecutionDegree(originExecutionDegree);
            sessionBean.setCurricularYear(curYear);

            request.setAttribute("sessionBean", sessionBean);
            addActionMessage("info", request, "message.manager.executionCourseManagement.dissociate.success.switchContext",
                    executionCourseName);
            return mapping.findForward("listExecutionCourseActions");
        }

        return mapping.findForward("manageCurricularSeparation");
    }

    public ActionForward prepareAssociateCurricularCourseChooseDegreeCurricularPlan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        //TODO: check & clean up attributes that are not needed
        //processing attributes
        String executionPeriodId = RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");
        List<InfoExecutionDegree> executionDegreeList = new ArrayList<InfoExecutionDegree>();
        try {
            executionDegreeList = ReadExecutionDegreesByExecutionPeriodId.runForAcademicAdmin(executionPeriodId);
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
            ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(originExecutionDegreeId);
            request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());

        }
        RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        RequestUtils.getAndSetStringToRequest(request, "executionCourseName");
        ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodId);
        request.setAttribute("executionPeriodName", executionSemester.getQualifiedName());
        return mapping.findForward("prepareAssociateCurricularCourseChooseDegreeCurricularPlan");
    }

    public ActionForward prepareAssociateCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        //TODO: check and clean up unneeded attributes
        //informative and destination attributes
        String executionCoursesNotLinked = RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked");
        if (StringUtils.isEmpty(executionCoursesNotLinked) || !Boolean.valueOf(executionCoursesNotLinked)) {
            RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
            String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
            ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(originExecutionDegreeId);
            request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());
        }
        String executionPeriodId = RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");
        ExecutionSemester executionPeriod = AbstractDomainObject.fromExternalId(executionPeriodId);
        request.setAttribute("executionPeriodName", executionPeriod.getQualifiedName());
        RequestUtils.getAndSetStringToRequest(request, "executionCourseName");

        //FIXME: executionPeriod might not be needed (present in exec course)
        //processing attributes
        String degreeCurricularPlanId = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanId");
        DegreeCurricularPlan degreeCurricularPlan = null;
        if (!StringUtils.isEmpty(degreeCurricularPlanId)) {
            degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanId);
        }
        try {
            if (degreeCurricularPlan == null) {
                throw new DomainException("error.selection.noDegree");
            }
            request.setAttribute("degreeCurricularPlanName",
                    degreeCurricularPlan.getPresentationName(executionPeriod.getExecutionYear()));
            request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
            String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
            final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
            for (final DegreeModule degreeModule : rootDomainObject.getDegreeModulesSet()) {
                if (degreeModule instanceof CurricularCourse) {
                    final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
                    if (!executionCourse.getAssociatedCurricularCoursesSet().contains(curricularCourse)
                            && !curricularCourse.hasAnyExecutionCourseIn(executionSemester)) {
                        if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(null, degreeCurricularPlan,
                                executionSemester)) {
                            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
                        }
                    }
                }
            }
            Collections.sort(infoCurricularCourses, new BeanComparator("name"));
            request.setAttribute("infoCurricularCourses", infoCurricularCourses);

            return mapping.findForward("associateCurricularCourse");

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            return prepareAssociateCurricularCourseChooseDegreeCurricularPlan(mapping, form, request, response);
        }
    }

    public ActionForward associateCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm executionCourseForm = (DynaValidatorForm) form;
        RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");
        RequestUtils.getAndSetStringToRequest(request, "executionPeriodName");
        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");

        Integer curricularCoursesListSize = (Integer) executionCourseForm.get("curricularCoursesListSize");

        List<String> curricularCourseIds =
                getInformationToDissociate(request, curricularCoursesListSize, "curricularCourse", "externalId", "chosen");

        try {
            AssociateCurricularCoursesToExecutionCourse.run(executionCourseId, curricularCourseIds);

            // avmc (ist150958): success messages: 1 line for each curricular course
            String degreeCurricularPlanId = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanId");
            DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanId);

            addActionMessage("success", request, "message.manager.executionCourseManagement.associateCourse.success",
                    degreeCurricularPlan.getName());
            for (final String curricularCourseId : curricularCourseIds) {
                final CurricularCourse curricularCourse = AbstractDomainObject.fromExternalId(curricularCourseId);
                addActionMessage("successCourse", request,
                        "message.manager.executionCourseManagement.associateCourse.success.line", curricularCourse.getName());
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            return prepareAssociateCurricularCourse(mapping, executionCourseForm, request, response);
        }

        RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
        return mapping.findForward("manageCurricularSeparation");
    }

    protected void buildExecutionDegreeLabelValueBean(List<InfoExecutionDegree> executionDegreeList, List<LabelValueBean> courses) {

        for (InfoExecutionDegree infoExecutionDegree : executionDegreeList) {
            String name =
                    infoExecutionDegree.getInfoDegreeCurricularPlan().getDegreeCurricularPlan()
                            .getPresentationName(infoExecutionDegree.getInfoExecutionYear().getExecutionYear());
            /*
            TODO: DUPLICATE check really needed?
            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().getLocalizedName() + " em " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-" + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";
            */
            // courses.add(new LabelValueBean(name, name + "~" + infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId().toString()));
            courses.add(new LabelValueBean(name, infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId()));
        }
    }

    private List<String> getInformationToDissociate(HttpServletRequest request, Integer curricularCoursesListSize, String what,
            String property, String formProperty) {

        List<String> informationToDeleteList = new ArrayList<String>();
        for (int i = 0; i < curricularCoursesListSize.intValue(); i++) {
            String informationToDelete = dataToDelete(request, i, what, property, formProperty);
            if (informationToDelete != null) {
                informationToDeleteList.add(informationToDelete);
            }
        }
        return informationToDeleteList;
    }

    private String dataToDelete(HttpServletRequest request, int index, String what, String property, String formProperty) {

        String itemToDelete = null;
        String checkbox = request.getParameter(what + "[" + index + "]." + formProperty);
        String toDelete = null;
        if (checkbox != null && (checkbox.equals("on") || checkbox.equals("yes") || checkbox.equals("true"))) {
            toDelete = request.getParameter(what + "[" + index + "]." + property);
        }
        if (toDelete != null) {
            itemToDelete = toDelete;
        }
        return itemToDelete;
    }

    //not used
    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {

        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2))) {
                return true;
            }

        }
        return false;
    }
}
