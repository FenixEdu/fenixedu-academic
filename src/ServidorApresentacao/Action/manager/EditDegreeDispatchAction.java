/*
 * Created on 28/Jul/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoCurso;

/**
 * @author lmac1
 */

public class EditDegreeDispatchAction extends FenixDispatchAction
{

    public ActionForward prepareEdit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm readDegreeForm = (DynaActionForm) form;

        Integer degreeId = new Integer(request.getParameter("degreeId"));

        InfoDegree oldInfoDegree = null;

        Object args[] = { degreeId };

        try
        {
            oldInfoDegree = (InfoDegree) ServiceUtils.executeService(userView, "ReadDegree", args);

        } catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(
                "message.nonExistingDegree",
                mapping.findForward("readDegrees"));
        } catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        TipoCurso degreeType = oldInfoDegree.getTipoCurso();

        readDegreeForm.set("name", oldInfoDegree.getNome());
        readDegreeForm.set("code", oldInfoDegree.getSigla());
        readDegreeForm.set("degreeType", degreeType.getTipoCurso());
        return mapping.findForward("editDegree");
    }

    public ActionForward edit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm editDegreeForm = (DynaActionForm) form;
        Integer oldDegreeId = new Integer(request.getParameter("degreeId"));
        String code = (String) editDegreeForm.get("code");
        String name = (String) editDegreeForm.get("name");
        Integer degreeTypeInt = (Integer) editDegreeForm.get("degreeType");

        TipoCurso degreeType = new TipoCurso(degreeTypeInt);
        InfoDegree newInfoDegree = new InfoDegree(code, name, degreeType);
        newInfoDegree.setIdInternal(oldDegreeId);

        Object args[] = { newInfoDegree };

        try
        {
            ServiceUtils.executeService(userView, "EditDegree", args);

        } catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(
                "message.nonExistingDegree",
                mapping.findForward("readDegrees"));
        } catch (ExistingServiceException e)
        {
            throw new ExistingActionException("message.manager.existing.degree");
        } catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return mapping.findForward("readDegree");
    }
}
