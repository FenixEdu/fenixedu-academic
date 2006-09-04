/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
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
public class InsertExecutionCourseDA extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("insertExecutionCourse");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        InfoExecutionCourseEditor infoExecutionCourse = new InfoExecutionCourseEditor();

        String name = (String) dynaForm.get("name");
        infoExecutionCourse.setNome(name);

        String code = (String) dynaForm.get("code");
        infoExecutionCourse.setSigla(code);

        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod(rootDomainObject.readExecutionPeriodByOID(new Integer(request.getParameter("executionPeriodId"))));
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