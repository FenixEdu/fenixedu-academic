package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCourseWithShiftsAndCurricularCoursesByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.SeperateExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
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
            HttpServletResponse response) throws FenixServiceException, FenixFilterException {

        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
        Integer executionDegreeID = getIntegerFromRequest(request, "originExecutionDegreeID");
        Integer curricularYearID = getIntegerFromRequest(request, "curricularYearId");

        InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseWithShiftsAndCurricularCoursesByOID.run(executionCourseId);
        request.setAttribute("infoExecutionCourse", infoExecutionCourse);

        List<InfoExecutionDegree> executionDegrees =
                ReadExecutionDegreesByExecutionPeriodId.run(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());
        transformExecutionDegreesIntoLabelValueBean(executionDegrees);
        request.setAttribute("executionDegrees", executionDegrees);

        List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);
        request.setAttribute("originExecutionDegreeID", executionDegreeID);
        request.setAttribute("curricularYearId", curricularYearID);
        return mapping.findForward("showSeperationPage");
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

                return new LabelValueBean(label, infoExecutionDegree.getIdInternal().toString());
            }

        });

        Collections.sort(executionDegreeList, new BeanComparator("label"));
        executionDegreeList.add(
                0,
                new LabelValueBean(BundleUtil.getStringFromResourceBundle("resources.RendererResources",
                        "renderers.menu.default.title"), ""));
    }

    public ActionForward changeDestinationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixFilterException {

        prepareTransfer(mapping, form, request, response);

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        String destinationExecutionDegreeId = (String) dynaActionForm.get("destinationExecutionDegreeId");
        String destinationCurricularYear = (String) dynaActionForm.get("destinationCurricularYear");

        if (isSet(destinationExecutionDegreeId) && isSet(destinationCurricularYear)) {

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request.getAttribute("infoExecutionCourse");

            List<InfoExecutionCourse> executionCourses =
                    ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear.run(new Integer(
                            destinationExecutionDegreeId), infoExecutionCourse.getInfoExecutionPeriod().getIdInternal(),
                            new Integer(destinationCurricularYear));
            executionCourses.remove(infoExecutionCourse);
            Collections.sort(executionCourses, new BeanComparator("nome"));
            request.setAttribute("executionCourses", executionCourses);
        }

        return mapping.findForward("showSeperationPage");
    }

    public ActionForward transfer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        /**/
        ExecutionCourseBean bean = new ExecutionCourseBean();

        /**/
        DynaActionForm dynaActionForm = (DynaActionForm) form;

        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
        String destinationExecutionCourseIDString = (String) dynaActionForm.get("destinationExecutionCourseID");
        Integer originExecutionDegreeID = (Integer) dynaActionForm.get("originExecutionDegreeID");
        Integer curricularYearID = (Integer) dynaActionForm.get("curricularYearId");
        String[] shiftIdsToTransfer = (String[]) dynaActionForm.get("shiftIdsToTransfer");
        String[] curricularCourseIdsToTransfer = (String[]) dynaActionForm.get("curricularCourseIdsToTransfer");

        Integer destinationExecutionCourseID = null;
        if (!StringUtils.isEmpty(destinationExecutionCourseIDString) && StringUtils.isNumeric(destinationExecutionCourseIDString)) {
            destinationExecutionCourseID = new Integer(destinationExecutionCourseIDString);
        }

        try {

            ExecutionCourse destinationExecutionCourse =
                    SeperateExecutionCourse.run(executionCourseId, destinationExecutionCourseID,
                            makeIntegerArray(shiftIdsToTransfer), makeIntegerArray(curricularCourseIdsToTransfer));

            String destinationExecutionCourseName = destinationExecutionCourse.getNameI18N().getContent();
            if (StringUtils.isEmpty(destinationExecutionCourseName)) {
                destinationExecutionCourseName = destinationExecutionCourse.getName();
            }
            String destinationExexcutionCourseCode = destinationExecutionCourse.getSigla();
            String destinationDegreeName = destinationExecutionCourse.getDegreePresentationString();
            String transferedCurricularCourses = makeObjectStringFromArray(curricularCourseIdsToTransfer, CurricularCourse.class);
            String transferedShifts = makeObjectStringFromArray(shiftIdsToTransfer, Shift.class);

            if (destinationExecutionCourseID == null && destinationExecutionCourse != null) {
                // newly created execution course
                addActionMessage("success", request, "message.manager.executionCourseManagement.separate.success.create",
                        destinationExecutionCourseName, destinationDegreeName, destinationExexcutionCourseCode,
                        transferedCurricularCourses, transferedShifts);
            } else if (destinationExecutionCourseID != null && destinationExecutionCourse != null) {
                // existing execution course
                addActionMessage("success", request, "message.manager.executionCourseManagement.separate.success.transfer",
                        transferedCurricularCourses, transferedShifts, destinationExecutionCourseName, destinationDegreeName,
                        destinationExexcutionCourseCode);
            } else {
                // This should never happen
                addActionMessage("error", request, "Transfer ocurred but something went wrong");
            }

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            return prepareTransfer(mapping, dynaActionForm, request, response);
        } finally {

            ExecutionDegree originExecutionDegree = rootDomainObject.readExecutionDegreeByOID(originExecutionDegreeID);
            final ExecutionCourse originExecutionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
            final CurricularYear curricularYear = rootDomainObject.readCurricularYearByOID(curricularYearID);

            bean.setExecutionDegree(originExecutionDegree);
            bean.setChooseNotLinked(false);
            bean.setSourceExecutionCourse(originExecutionCourse);
            bean.setExecutionSemester(originExecutionCourse.getExecutionPeriod());
            bean.setCurricularYear(curricularYear);
            request.setAttribute("originExecutionDegree", originExecutionDegree);
            request.setAttribute("sessionBean", bean);
        }

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