/*
 * Created on 8/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.CurricularCourseType;

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

        DynaActionForm dynaForm = (DynaValidatorForm) form;
        String type = (String) dynaForm.get("type");
        String mandatory = (String) dynaForm.get("mandatory");
        String basic = (String) dynaForm.get("basic");
        
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setIdInternal(degreeCurricularPlanId);

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setBasic(new Boolean(basic));
        infoCurricularCourse.setCode((String) dynaForm.get("code"));
        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoCurricularCourse.setMandatory(new Boolean(mandatory));
        infoCurricularCourse.setName((String) dynaForm.get("name"));
        infoCurricularCourse.setType(new CurricularCourseType(new Integer(type)));

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