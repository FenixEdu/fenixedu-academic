/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseEditor;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author lmac1
 */
public class EditCurricularCourseDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

        InfoCurricularCourse oldInfoCurricularCourse = null;

        Object args[] = { curricularCourseId };

        try {
            oldInfoCurricularCourse = (InfoCurricularCourse) ServiceUtils.executeService(userView,
                    "ReadCurricularCourse", args);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping
                    .findForward("readDegreeCP"));
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

        dynaForm.set("credits", (oldInfoCurricularCourse.getCredits() == null) ? ""
                : oldInfoCurricularCourse.getCredits().toString());
        dynaForm.set("ectsCredits", (oldInfoCurricularCourse.getEctsCredits() == null) ? ""
                : oldInfoCurricularCourse.getEctsCredits().toString());
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
        dynaForm.set("maxIncrementNac", oldInfoCurricularCourse
                .getMaximumValueForAcumulatedEnrollments().toString());
        dynaForm.set("minIncrementNac", oldInfoCurricularCourse
                .getMinimumValueForAcumulatedEnrollments().toString());
        dynaForm.set("weight", oldInfoCurricularCourse.getWeigth().toString());

        dynaForm.set("mandatoryEnrollment", oldInfoCurricularCourse.getMandatoryEnrollment().toString());
        dynaForm.set("enrollmentAllowed", oldInfoCurricularCourse.getEnrollmentAllowed().toString());
        dynaForm.set("enrollmentWeigth", oldInfoCurricularCourse.getEnrollmentWeigth().toString());

        if (oldInfoCurricularCourse.getGradeScale() != null) {
            dynaForm.set("gradeType", oldInfoCurricularCourse.getGradeScale().toString());
        }

        return mapping.findForward("editCurricularCourse");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException,
            FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

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

        newInfoCurricularCourse.setMaximumValueForAcumulatedEnrollments(new Integer((String) dynaForm
                .get("maxIncrementNac")));
        newInfoCurricularCourse.setMinimumValueForAcumulatedEnrollments(new Integer((String) dynaForm
                .get("minIncrementNac")));

        String credits = (String) dynaForm.get("credits");
        if (credits.compareTo("") != 0) {
            newInfoCurricularCourse.setCredits(new Double(credits));
        }

        String ectsCredits = (String) dynaForm.get("ectsCredits");
        if (ectsCredits != null && ectsCredits.length() > 0) {
            newInfoCurricularCourse.setEctsCredits(new Double(ectsCredits));
        }
        newInfoCurricularCourse.setWeigth(new Double((String) dynaForm.get("weight")));

        newInfoCurricularCourse.setMandatoryEnrollment(new Boolean((String) dynaForm
                .get("mandatoryEnrollment")));
        newInfoCurricularCourse.setEnrollmentAllowed(new Boolean((String) dynaForm
                .get("enrollmentAllowed")));
        newInfoCurricularCourse.setEnrollmentWeigth(new Integer((String) dynaForm
                .get("enrollmentWeigth")));

        String gradeTypeString = (String) dynaForm.get("gradeType");
        GradeScale gradeScale = null;
        if (gradeTypeString != null && gradeTypeString.length() > 0) {
            gradeScale = GradeScale.valueOf(gradeTypeString);
        }
        newInfoCurricularCourse.setGradeScale(gradeScale);

        Object args[] = { newInfoCurricularCourse };

        try {
            ServiceUtils.executeService(userView, "EditCurricularCourse", args);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping
                    .findForward("readDegreeCP"));
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("message.manager.existing.curricular.course");
        }

        return mapping.findForward("readCurricularCourse");
    }
}