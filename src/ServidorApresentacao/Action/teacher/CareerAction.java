/*
 * Created on 17/Nov/2003
 *  
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.framework.CRUDActionByOID;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CareerAction extends CRUDActionByOID {
    protected void prepareFormConstants(ActionMapping mapping, ActionForm form,
            HttpServletRequest request) throws FenixServiceException {
        IUserView userView = SessionUtils.getUserView(request);
        List categories = (List) ServiceUtils
                .executeService(userView, "ReadCategories", new Object[] {});

        request.setAttribute("categories", categories);
    }
}