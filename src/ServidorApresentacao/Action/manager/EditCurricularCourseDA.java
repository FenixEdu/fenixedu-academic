/*
 * Created on 18/Ago/2003
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
        dynaForm.set("code", oldInfoCurricularCourse.getCode());
        dynaForm.set("acronym", oldInfoCurricularCourse.getAcronym());

        dynaForm.set("type", oldInfoCurricularCourse.getType().getCurricularCourseType().toString());
        dynaForm.set("mandatory", oldInfoCurricularCourse.getMandatory().toString());
        dynaForm.set("basic", oldInfoCurricularCourse.getBasic().toString());

        dynaForm.set("credits", oldInfoCurricularCourse.getCredits().toString());
        dynaForm.set("ectsCredits", oldInfoCurricularCourse.getEctsCredits().toString());
        dynaForm.set("theoreticalHours", oldInfoCurricularCourse.getTheoreticalHours().toString());
        dynaForm.set("praticalHours", oldInfoCurricularCourse.getPraticalHours().toString());
        dynaForm.set("theoPratHours", oldInfoCurricularCourse.getTheoPratHours().toString());
        dynaForm.set("labHours", oldInfoCurricularCourse.getLabHours().toString());
        dynaForm.set("maxIncrementNac", oldInfoCurricularCourse
                .getMaximumValueForAcumulatedEnrollments().toString());
        dynaForm.set("minIncrementNac", oldInfoCurricularCourse
                .getMinimumValueForAcumulatedEnrollments().toString());
        dynaForm.set("weight", oldInfoCurricularCourse.getWeigth().toString());

        dynaForm.set("mandatoryEnrollment", oldInfoCurricularCourse.getMandatoryEnrollment().toString());
        dynaForm.set("enrollmentAllowed", oldInfoCurricularCourse.getEnrollmentAllowed().toString());
        dynaForm.set("enrollmentWeigth", oldInfoCurricularCourse.getEnrollmentWeigth().toString());

        return mapping.findForward("editCurricularCourse");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        Integer oldCurricularCourseId = new Integer(request.getParameter("curricularCourseId"));

        InfoCurricularCourse newInfoCurricularCourse = new InfoCurricularCourse();

        String name = (String) dynaForm.get("name");
        String code = (String) dynaForm.get("code");
        String acronym = (String) dynaForm.get("acronym");
        String typeString = (String) dynaForm.get("type");
        String mandatoryString = (String) dynaForm.get("mandatory");
        String basicString = (String) dynaForm.get("basic");

        newInfoCurricularCourse.setName(name);
        newInfoCurricularCourse.setCode(code);
        newInfoCurricularCourse.setAcronym(acronym);
        newInfoCurricularCourse.setIdInternal(oldCurricularCourseId);

        CurricularCourseType type = new CurricularCourseType(new Integer(typeString));
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