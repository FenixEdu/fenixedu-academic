/*
 * Created on 24/Set/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
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

/**
 * @author lmac1
 */
public class InsertExecutionCourseDA extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("insertExecutionCourse");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

        String name = (String) dynaForm.get("name");
        infoExecutionCourse.setNome(name);

        String code = (String) dynaForm.get("code");
        infoExecutionCourse.setSigla(code);

        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setIdInternal(new Integer(request.getParameter("executionPeriodId")));
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        String labHours = (String) dynaForm.get("labHours");
        if (labHours.compareTo("") != 0)
            infoExecutionCourse.setLabHours(new Double(labHours));

        String praticalHours = (String) dynaForm.get("praticalHours");
        if (praticalHours.compareTo("") != 0)
            infoExecutionCourse.setPraticalHours(new Double(praticalHours));

        String theoPratHours = (String) dynaForm.get("theoPratHours");
        if (theoPratHours.compareTo("") != 0)
            infoExecutionCourse.setTheoPratHours(new Double(theoPratHours));

        String theoreticalHours = (String) dynaForm.get("theoreticalHours");
        if (theoreticalHours.compareTo("") != 0)
            infoExecutionCourse.setTheoreticalHours(new Double(theoreticalHours));

        infoExecutionCourse.setComment((String) dynaForm.get("comment"));

        Object args[] = { infoExecutionCourse };

        try {
            ServiceUtils.executeService(userView, "InsertExecutionCourseAtExecutionPeriod", args);

        } catch (ExistingServiceException ex) {
            throw new ExistingActionException(ex.getMessage(), ex);
        } catch (NonExistingServiceException exception) {
            throw new NonExistingActionException(exception.getMessage(), mapping
                    .findForward("readExecutionPeriods"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("readExecutionCourses");
    }
}