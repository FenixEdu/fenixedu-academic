package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCourseWithShiftsAndCurricularCoursesByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadInfoExecutionCourseByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.SeperateExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class SeperateExecutionCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTransfer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
        RequestUtils.getAndSetStringToRequest(request, "executionPeriodId"); // maybe not needed (have EC id)

        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(originExecutionDegreeId));
        request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());

        InfoExecutionCourse infoExecutionCourse =
                ReadExecutionCourseWithShiftsAndCurricularCoursesByOID.run(Integer.valueOf(executionCourseId));
        request.setAttribute("infoExecutionCourse", infoExecutionCourse);

        List executionDegrees =
                ReadExecutionDegreesByExecutionPeriodId.runForAcademicAdminAdv(infoExecutionCourse.getInfoExecutionPeriod()
                        .getIdInternal());
        transformExecutionDegreesIntoLabelValueBean(executionDegrees);
        request.setAttribute("executionDegrees", executionDegrees);

        List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);
        return mapping.findForward("showTransferPage");
    }

    public ActionForward prepareSeparate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
        RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");

        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(originExecutionDegreeId));
        request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());

        InfoExecutionCourse infoExecutionCourse =
                ReadExecutionCourseWithShiftsAndCurricularCoursesByOID.run(Integer.valueOf(executionCourseId));
        request.setAttribute("infoExecutionCourse", infoExecutionCourse);

        return mapping.findForward("showSeparationPage");
    }

    public ActionForward manageCurricularSeparation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixActionException {

        // FIXME:  ugly code to get attribute before parameter (parameter needs to be changed when coming from separate)
        String executionCourseId = (String) request.getAttribute("executionCourseId");
        if (executionCourseId == null) {
            executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        }

        InfoExecutionCourse infoExecutionCourse;

        try {
            infoExecutionCourse = ReadInfoExecutionCourseByOID.run(Integer.valueOf(executionCourseId));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoExecutionCourse.getAssociatedInfoCurricularCourses() != null) {
            Collections.sort(infoExecutionCourse.getAssociatedInfoCurricularCourses(), new BeanComparator("name"));
        }

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);

        // Setting bean for return to listExecutionCourseActions
        String executionCoursesNotLinked = RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked");
        Boolean chooseNotLinked = false;
        if (!StringUtils.isEmpty(executionCoursesNotLinked) && Boolean.valueOf(executionCoursesNotLinked)) {
            chooseNotLinked = true;
        }

        String executionPeriodId = RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");
        ExecutionCourse executionCourse =
                RootDomainObject.getInstance().readExecutionCourseByOID(Integer.valueOf(executionCourseId));
        ExecutionSemester executionPeriod =
                RootDomainObject.getInstance().readExecutionSemesterByOID(Integer.valueOf(executionPeriodId));

        ExecutionCourseBean sessionBean = new ExecutionCourseBean();
        sessionBean.setSourceExecutionCourse(executionCourse);
        sessionBean.setExecutionSemester(executionPeriod);
        sessionBean.setChooseNotLinked(chooseNotLinked);

        if (!chooseNotLinked) {
            String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
            String curricularYearId = RequestUtils.getAndSetStringToRequest(request, "curricularYearId");
            ExecutionDegree executionDegree =
                    RootDomainObject.getInstance().readExecutionDegreeByOID(Integer.valueOf(originExecutionDegreeId));
            CurricularYear curYear = RootDomainObject.getInstance().readCurricularYearByOID(Integer.valueOf(curricularYearId));
            sessionBean.setExecutionDegree(executionDegree);
            sessionBean.setCurricularYear(curYear);
            request.setAttribute("originExecutionDegreeName", executionDegree.getPresentationName());
        }

        request.setAttribute("sessionBean", sessionBean);

        return mapping.findForward("manageCurricularSeparation");
    }

    private void transformExecutionDegreesIntoLabelValueBean(List executionDegreeList) {
        CollectionUtils.transform(executionDegreeList, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
                /*
                TODO: DUPLICATE check really needed?
                StringBuilder label =
                        new StringBuilder(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType()
                                .getLocalizedName());
                label.append(" em ");
                label.append(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
                */

                String label =
                        infoExecutionDegree.getInfoDegreeCurricularPlan().getDegreeCurricularPlan()
                                .getPresentationName(infoExecutionDegree.getInfoExecutionYear().getExecutionYear());

                return new LabelValueBean(label, infoExecutionDegree.getExternalId().toString());
            }

        });

        Collections.sort(executionDegreeList, new BeanComparator("label"));
        executionDegreeList.add(
                0,
                new LabelValueBean(BundleUtil.getStringFromResourceBundle("resources.RendererResources",
                        "renderers.menu.default.title"), ""));
    }

    public ActionForward changeDestinationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        prepareTransfer(mapping, form, request, response);

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String destinationExecutionDegreeId = (String) dynaActionForm.get("destinationExecutionDegreeId");
        String destinationCurricularYear = (String) dynaActionForm.get("destinationCurricularYear");

        if (isSet(destinationExecutionDegreeId) && isSet(destinationCurricularYear)) {

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request.getAttribute("infoExecutionCourse");

            List executionCourses =
                    ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear.run(new Integer(
                            destinationExecutionDegreeId), infoExecutionCourse.getInfoExecutionPeriod().getExternalId(),
                            new Integer(destinationCurricularYear));
            executionCourses.remove(infoExecutionCourse);
            Collections.sort(executionCourses, new BeanComparator("nome"));
            request.setAttribute("executionCourses", executionCourses);
        }

        return mapping.findForward("showTransferPage");
    }

    public ActionForward transfer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixActionException {
        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String destinationExecutionCourseIdString = (String) dynaActionForm.get("destinationExecutionCourseId");
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        Integer curricularYearId = (Integer) dynaActionForm.get("curricularYearId");
        String[] shiftIdsToTransfer = (String[]) dynaActionForm.get("shiftIdsToTransfer");
        String[] curricularCourseIdsToTransfer = (String[]) dynaActionForm.get("curricularCourseIdsToTransfer");
        ExecutionDegree originExecutionDegree =
                rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(originExecutionDegreeId));
        ExecutionCourse originExecutionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(executionCourseId));
        String originExecutionDegreesString = originExecutionCourse.getDegreePresentationString();
        Integer destinationExecutionCourseId = null;

        try {

            if (!StringUtils.isEmpty(destinationExecutionCourseIdString)
                    && StringUtils.isNumeric(destinationExecutionCourseIdString)) {
                destinationExecutionCourseId = new Integer(destinationExecutionCourseIdString);
            } else {
                throw new DomainException("error.selection.noDestinationExecutionCourse");
            }

            ExecutionCourse destinationExecutionCourse =
                    SeperateExecutionCourse.run(Integer.valueOf(executionCourseId), destinationExecutionCourseId,
                            makeIntegerArray(shiftIdsToTransfer), makeIntegerArray(curricularCourseIdsToTransfer));

            String destinationExecutionCourseName = destinationExecutionCourse.getNameI18N().getContent();
            if (StringUtils.isEmpty(destinationExecutionCourseName)) {
                destinationExecutionCourseName = destinationExecutionCourse.getName();
            }
            String destinationExecutionCourseCode = destinationExecutionCourse.getSigla();
            String destinationDegreeName = destinationExecutionCourse.getDegreePresentationString();
            String transferedCurricularCourses = makeObjectStringFromArray(curricularCourseIdsToTransfer, CurricularCourse.class);
            String transferedShifts;

            String successKey;
            if (shiftIdsToTransfer.length == 0) {
                successKey = "message.manager.executionCourseManagement.transferCourse.success.many.noShifts";
                transferedShifts = "";
            } else {
                successKey = "message.manager.executionCourseManagement.transferCourse.success.many";
                transferedShifts = makeObjectStringFromArray(shiftIdsToTransfer, Shift.class);
            }
            addActionMessage("success", request, successKey, transferedCurricularCourses, transferedShifts,
                    destinationExecutionCourseName, destinationDegreeName, destinationExecutionCourseCode);

            // check if degree context has changed
            if (!originExecutionCourse.getExecutionDegrees().contains(originExecutionDegree)) {
                // origin execution course degree has changed (no longer on original degree)
                String originCourseName = originExecutionCourse.getNameI18N().getContent();
                if (StringUtils.isEmpty(originCourseName)) {
                    originCourseName = originExecutionCourse.getName();
                }
                addActionMessage("info", request,
                        "message.manager.executionCourseManagement.transferCourse.success.switchContext", originCourseName,
                        originExecutionDegreesString, originExecutionCourse.getDegreePresentationString(),
                        destinationExecutionCourseName, destinationExecutionCourse.getDegreePresentationString(),
                        originExecutionDegree.getDegree().getSigla());
                request.setAttribute("executionCourseId", destinationExecutionCourse.getExternalId().toString());
            }

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            if (request.getAttribute("destinationExecutionDegreeId") != null) {
                request.setAttribute("destinationExecutionDegreeId", request.getAttribute("destinationExecutionDegreeId"));
            }
            if (curricularYearId != null) {
                request.setAttribute("destinationCurricularYear", curricularYearId.toString());
            }
            if (request.getAttribute("executionCourses") != null) {
                request.setAttribute("executionCourses", request.getAttribute("executionCourses"));
            }
            if (destinationExecutionCourseId != null) {
                request.setAttribute("destinationExecutionCourseId", destinationExecutionCourseId.toString());
            }
            return changeDestinationContext(mapping, dynaActionForm, request, response);
        }

        return manageCurricularSeparation(mapping, dynaActionForm, request, response);
    }

    public ActionForward separate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixActionException {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        String originExecutionDegreeId = RequestUtils.getAndSetStringToRequest(request, "originExecutionDegreeId");
        String[] shiftIdsToTransfer = (String[]) dynaActionForm.get("shiftIdsToTransfer");
        String[] curricularCourseIdsToTransfer = (String[]) dynaActionForm.get("curricularCourseIdsToTransfer");
        ExecutionDegree originExecutionDegree =
                rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(originExecutionDegreeId));
        ExecutionCourse originExecutionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(executionCourseId));
        String originExecutionDegreesString = originExecutionCourse.getDegreePresentationString();

        try {

            ExecutionCourse destinationExecutionCourse =
                    SeperateExecutionCourse.run(Integer.valueOf(executionCourseId), null, makeIntegerArray(shiftIdsToTransfer),
                            makeIntegerArray(curricularCourseIdsToTransfer));

            String destinationExecutionCourseName = destinationExecutionCourse.getNameI18N().getContent();
            if (StringUtils.isEmpty(destinationExecutionCourseName)) {
                destinationExecutionCourseName = destinationExecutionCourse.getName();
            }
            String destinationExecutionCourseCode = destinationExecutionCourse.getSigla();
            String destinationDegreeName = destinationExecutionCourse.getDegreePresentationString();
            String transferedCurricularCourses = makeObjectStringFromArray(curricularCourseIdsToTransfer, CurricularCourse.class);
            String transferedShifts;

            String successKey;
            if (shiftIdsToTransfer.length == 0) {
                successKey = "message.manager.executionCourseManagement.separate.success.create.noShifts";
                transferedShifts = "";
            } else {
                successKey = "message.manager.executionCourseManagement.separate.success.create";
                transferedShifts = makeObjectStringFromArray(shiftIdsToTransfer, Shift.class);
            }
            addActionMessage("success", request, successKey, destinationExecutionCourseName, destinationDegreeName,
                    destinationExecutionCourseCode, transferedCurricularCourses, transferedShifts);

            // check if degree context has changed
            if (!originExecutionCourse.getExecutionDegrees().contains(originExecutionDegree)) {
                // origin execution course degree has changed (no longer on original degree)
                String originCourseName = originExecutionCourse.getNameI18N().getContent();
                if (StringUtils.isEmpty(originCourseName)) {
                    originCourseName = originExecutionCourse.getName();
                }
                addActionMessage("info", request, "message.manager.executionCourseManagement.separate.success.switchContext",
                        originCourseName, originExecutionDegreesString, originExecutionCourse.getDegreePresentationString(),
                        destinationExecutionCourseName, destinationExecutionCourse.getDegreePresentationString(),
                        originExecutionDegree.getDegree().getSigla());
                request.setAttribute("executionCourseId", destinationExecutionCourse.getExternalId().toString());
            }

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            return prepareSeparate(mapping, dynaActionForm, request, response);
        }

        return manageCurricularSeparation(mapping, dynaActionForm, request, response); //mapping.findForward("manageCurricularSeparation");
    }

    private Integer[] makeIntegerArray(String[] stringArray) {
        Integer[] integerArray = new Integer[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {
            integerArray[i] = new Integer(stringArray[i]);
        }

        return integerArray;
    }

    private boolean isSet(String parameter) {
        return !StringUtils.isEmpty(parameter) && StringUtils.isNumeric(parameter);
    }

    private String makeObjectStringFromArray(String[] ids, Class objectType) {

        StringBuilder sb = new StringBuilder();

        if (objectType.equals(CurricularCourse.class)) {
            for (String id : ids) {
                sb.append(curricularCourseToString(id));
                sb.append(", ");
            }
        } else if (objectType.equals(Shift.class)) {
            for (String id : ids) {
                sb.append(shiftToString(id));
                sb.append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // trim ", "
        } else {
            sb.append(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.empty"));
        }
        return sb.toString();
    }

    private String curricularCourseToString(String id) {
        CurricularCourse curricularCourse =
                (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(Integer.valueOf(id));
        String name = curricularCourse.getNameI18N().getContent();
        if (StringUtils.isEmpty(name)) {
            name = curricularCourse.getName();
        }
        return name + " [" + curricularCourse.getDegree().getSigla() + "]";
    }

    private String shiftToString(String id) {
        Shift shift = RootDomainObject.getInstance().readShiftByOID(Integer.valueOf(id));
        return shift.getPresentationName();
    }
}
