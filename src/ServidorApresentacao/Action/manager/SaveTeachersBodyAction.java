/*
 * Created on 23/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */

public class SaveTeachersBodyAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException,
            FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
        DynaActionForm actionForm = (DynaActionForm) form;

        Integer[] responsibleTeachersIds = (Integer[]) actionForm.get("responsibleTeachersIds");
        Integer[] professorShipTeachersIds = (Integer[]) actionForm.get("professorShipTeachersIds");
        List respTeachersIds = Arrays.asList(responsibleTeachersIds);
        List profTeachersIds = Arrays.asList(professorShipTeachersIds);
        // TODO: Collections.sort(profTeachersIds, new BeanComparator("name"));
        Object args[] = { respTeachersIds, profTeachersIds, executionCourseId };
        Boolean result;

        try {
            result = (Boolean) ServiceUtils.executeService(userView, "SaveTeachersBody", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping
                    .findForward("readCurricularCourse"));
        }

        if (!result.booleanValue())
            throw new InvalidArgumentsActionException("message.non.existing.teachers");

        return mapping.findForward("readCurricularCourse");
    }
}