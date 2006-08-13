/*
 * Created on 8/Ago/2003
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
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
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

public class InsertCurricularCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("insertCurricularCourse");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);

        DynaActionForm dynaForm = (DynaValidatorForm) form;
        String type = (String) dynaForm.get("type");
        String mandatory = (String) dynaForm.get("mandatory");
        String basic = (String) dynaForm.get("basic");
        
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan(degreeCurricularPlan);

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setBasic(new Boolean(basic));
        infoCurricularCourse.setCode((String) dynaForm.get("code"));
        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoCurricularCourse.setMandatory(new Boolean(mandatory));
        infoCurricularCourse.setName((String) dynaForm.get("name"));
        infoCurricularCourse.setNameEn((String) dynaForm.get("nameEn"));
        infoCurricularCourse.setType(CurricularCourseType.valueOf(type));

        String credits = (String) dynaForm.get("credits");
        if (credits.compareTo("") != 0) {
            infoCurricularCourse.setCredits(new Double(credits));
        }
        String ectsCredits = (String) dynaForm.get("ectsCredits");
        if (ectsCredits != null && ectsCredits.length() > 0) {
            infoCurricularCourse.setEctsCredits(new Double(ectsCredits));
        }

        String labHours = (String) dynaForm.get("labHours");
        if (labHours.compareTo("") != 0) {
            infoCurricularCourse.setLabHours(new Double(labHours));
        }
        infoCurricularCourse.setMaximumValueForAcumulatedEnrollments(new Integer((String) dynaForm
                .get("maxIncrementNac")));
        infoCurricularCourse.setMinimumValueForAcumulatedEnrollments(new Integer((String) dynaForm
                .get("minIncrementNac")));

        String praticalHours = (String) dynaForm.get("praticalHours");
        if (praticalHours.compareTo("") != 0) {
            infoCurricularCourse.setPraticalHours(new Double(praticalHours));
        }

        String theoPratHours = (String) dynaForm.get("theoPratHours");
        if (theoPratHours.compareTo("") != 0) {
            infoCurricularCourse.setTheoPratHours(new Double(theoPratHours));
        }
        String theoreticalHours = (String) dynaForm.get("theoreticalHours");
        if (theoreticalHours.compareTo("") != 0) {
            infoCurricularCourse.setTheoreticalHours(new Double(theoreticalHours));
        }
        infoCurricularCourse.setWeigth(new Double((String) dynaForm.get("weight")));

        infoCurricularCourse.setMandatoryEnrollment(new Boolean((String) dynaForm
                .get("mandatoryEnrollment")));
        infoCurricularCourse
                .setEnrollmentAllowed(new Boolean((String) dynaForm.get("enrollmentAllowed")));
        infoCurricularCourse.setEnrollmentWeigth(new Integer((String) dynaForm.get("enrollmentWeigth")));
        
        infoCurricularCourse.setAcronym((String) dynaForm.get("acronym"));
        
        String gradeTypeString = (String) dynaForm.get("gradeType");
        GradeScale gradeScale = null;
        if(gradeTypeString != null && gradeTypeString.length() > 0) {
        	gradeScale = GradeScale.valueOf(gradeTypeString);
        }
        infoCurricularCourse.setGradeScale(gradeScale);

        Object args[] = { infoCurricularCourse };

        try {
            ServiceUtils.executeService(userView, "InsertCurricularCourseAtDegreeCurricularPlan", args);

        } catch (ExistingServiceException ex) {
            throw new ExistingActionException(ex.getMessage(), ex);
        } catch (NonExistingServiceException exception) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping
                    .findForward("readDegree"));
        }

        return mapping.findForward("readDegreeCurricularPlan");
    }
}