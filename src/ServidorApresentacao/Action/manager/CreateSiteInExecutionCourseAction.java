/*
 * Created on 27/Set/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */
public class CreateSiteInExecutionCourseAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);

        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
        Object args[] = { executionCourseId };
        try {

            ServiceUtils.executeService(userView, "CreateSiteInExecutionCourse", args);
        } catch (NonExistingServiceException exception) {
            throw new NonExistingActionException(exception.getMessage());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        //TODO:ver qd nao exeiste curricularcourse
        return mapping.findForward("readCurricularCourse");
    }

}

