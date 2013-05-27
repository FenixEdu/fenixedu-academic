/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseEditor;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author lmac1
 */
@Mapping(module = "manager", path = "/editCurricularCourse", input = "/editCurricularCourse.do?method=prepareEdit&page=0",
        attribute = "curricularCourseForm", formBean = "curricularCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "readDegreeCP", path = "/readDegreeCurricularPlan.do"),
        @Forward(name = "readCurricularCourse", path = "/readCurricularCourse.do"),
        @Forward(name = "editCurricularCourse", path = "/manager/editCurricularCourse_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/curricularCourseNavLocalManager.jsp")) })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
                        key = "resources.Action.exceptions.NonExistingActionException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
                        key = "resources.Action.exceptions.ExistingActionException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.manager.EditCurricularCourse.ExistingAcronymException.class,
                        key = "message.manager.existing.curricular.course.acronym",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request") })
public class EditCurricularCourseDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = UserView.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

        InfoCurricularCourse oldInfoCurricularCourse = null;

        try {
            oldInfoCurricularCourse = ReadCurricularCourse.runReadCurricularCourse(curricularCourseId);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping.findForward("readDegreeCP"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        dynaForm.set("name", oldInfoCurricularCourse.getName());
        dynaForm.set("nameEn", oldInfoCurricularCourse.getNameEn());
        dynaForm.set("code", oldInfoCurricularCourse.getCode());
        dynaForm.set("acronym", oldInfoCurricularCourse.getAcronym());

        dynaForm.set("type", oldInfoCurricularCourse.getType().toString());
        dynaForm.set("mandatory", oldInfoCurricularCourse.getMandatory().toString());
        dynaForm.set("basic", oldInfoCurricularCourse.getBasic().toString());

        dynaForm.set("credits", (oldInfoCurricularCourse.getCredits() == null) ? "" : oldInfoCurricularCourse.getCredits()
                .toString());
        dynaForm.set("ectsCredits", (oldInfoCurricularCourse.getEctsCredits() == null) ? "" : oldInfoCurricularCourse
                .getEctsCredits().toString());
        if (oldInfoCurricularCourse.getTheoreticalHours() != null) {
            dynaForm.set("theoreticalHours", oldInfoCurricularCourse.getTheoreticalHours().toString());
        }
        if (oldInfoCurricularCourse.getPraticalHours() != null) {
            dynaForm.set("praticalHours", oldInfoCurricularCourse.getPraticalHours().toString());
        }
        if (oldInfoCurricularCourse.getTheoPratHours() != null) {
            dynaForm.set("theoPratHours", oldInfoCurricularCourse.getTheoPratHours().toString());
        }
        if (oldInfoCurricularCourse.getLabHours() != null) {
            dynaForm.set("labHours", oldInfoCurricularCourse.getLabHours().toString());
        }
        dynaForm.set("maxIncrementNac", oldInfoCurricularCourse.getMaximumValueForAcumulatedEnrollments().toString());
        dynaForm.set("minIncrementNac", oldInfoCurricularCourse.getMinimumValueForAcumulatedEnrollments().toString());
        dynaForm.set("weight",
                oldInfoCurricularCourse.getWeigth() != null ? oldInfoCurricularCourse.getWeigth().toString() : StringUtils.EMPTY);

        dynaForm.set("mandatoryEnrollment", oldInfoCurricularCourse.getMandatoryEnrollment().toString());
        dynaForm.set("enrollmentAllowed", oldInfoCurricularCourse.getEnrollmentAllowed().toString());
        dynaForm.set("enrollmentWeigth", oldInfoCurricularCourse.getEnrollmentWeigth().toString());

        if (oldInfoCurricularCourse.getGradeScale() != null) {
            dynaForm.set("gradeType", oldInfoCurricularCourse.getGradeScale().toString());
        }

        dynaForm.set("regimeType", oldInfoCurricularCourse.getRegimeType().toString());

        return mapping.findForward("editCurricularCourse");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        IUserView userView = UserView.getUser();

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        Integer oldCurricularCourseId = new Integer(request.getParameter("curricularCourseId"));

        InfoCurricularCourseEditor newInfoCurricularCourse = new InfoCurricularCourseEditor();

        String name = (String) dynaForm.get("name");
        String nameEn = (String) dynaForm.get("nameEn");
        String code = (String) dynaForm.get("code");
        String acronym = (String) dynaForm.get("acronym");
        String typeString = (String) dynaForm.get("type");
        String mandatoryString = (String) dynaForm.get("mandatory");
        String basicString = (String) dynaForm.get("basic");

        newInfoCurricularCourse.setName(name);
        newInfoCurricularCourse.setNameEn(nameEn);
        newInfoCurricularCourse.setCode(code);
        newInfoCurricularCourse.setAcronym(acronym);
        newInfoCurricularCourse.setIdInternal(oldCurricularCourseId);

        CurricularCourseType type = CurricularCourseType.valueOf(typeString);
        newInfoCurricularCourse.setType(type);

        newInfoCurricularCourse.setMandatory(new Boolean(mandatoryString));
        newInfoCurricularCourse.setBasic(new Boolean(basicString));

        String praticalHours = (String) dynaForm.get("praticalHours");
        if (praticalHours.compareTo("") != 0) {
            newInfoCurricularCourse.setPraticalHours(new Double(praticalHours));
        }

        String theoPratHours = (String) dynaForm.get("theoPratHours");
        if (theoPratHours.compareTo("") != 0) {
            newInfoCurricularCourse.setTheoPratHours(new Double(theoPratHours));
        }

        String theoreticalHours = (String) dynaForm.get("theoreticalHours");
        if (theoreticalHours.compareTo("") != 0) {
            newInfoCurricularCourse.setTheoreticalHours(new Double(theoreticalHours));
        }
        String labHours = (String) dynaForm.get("labHours");
        if (labHours.compareTo("") != 0) {
            newInfoCurricularCourse.setLabHours(new Double(labHours));
        }

        newInfoCurricularCourse.setMaximumValueForAcumulatedEnrollments(new Integer((String) dynaForm.get("maxIncrementNac")));
        newInfoCurricularCourse.setMinimumValueForAcumulatedEnrollments(new Integer((String) dynaForm.get("minIncrementNac")));

        String credits = (String) dynaForm.get("credits");
        if (credits.compareTo("") != 0) {
            newInfoCurricularCourse.setCredits(new Double(credits));
        }

        String ectsCredits = (String) dynaForm.get("ectsCredits");
        if (ectsCredits != null && ectsCredits.length() > 0) {
            newInfoCurricularCourse.setEctsCredits(new Double(ectsCredits));
        }
        newInfoCurricularCourse.setWeigth(new Double((String) dynaForm.get("weight")));

        newInfoCurricularCourse.setMandatoryEnrollment(new Boolean((String) dynaForm.get("mandatoryEnrollment")));
        newInfoCurricularCourse.setEnrollmentAllowed(new Boolean((String) dynaForm.get("enrollmentAllowed")));
        newInfoCurricularCourse.setEnrollmentWeigth(new Integer((String) dynaForm.get("enrollmentWeigth")));

        String gradeTypeString = (String) dynaForm.get("gradeType");
        GradeScale gradeScale = null;
        if (gradeTypeString != null && gradeTypeString.length() > 0) {
            gradeScale = GradeScale.valueOf(gradeTypeString);
        }
        newInfoCurricularCourse.setGradeScale(gradeScale);

        String regimeTypeString = (String) dynaForm.get("regimeType");
        RegimeType regimeType = null;
        if (regimeTypeString != null && regimeTypeString.length() > 0) {
            regimeType = RegimeType.valueOf(regimeTypeString);
        }
        newInfoCurricularCourse.setRegimeType(regimeType);

        try {
            EditCurricularCourse.run(newInfoCurricularCourse);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping.findForward("readDegreeCP"));
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("message.manager.existing.curricular.course");
        }

        return mapping.findForward("readCurricularCourse");
    }
}