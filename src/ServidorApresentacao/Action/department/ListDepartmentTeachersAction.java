/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.department;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDepartment;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.framework.SearchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ListDepartmentTeachersAction extends SearchAction
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.framework.SearchAction#prepareFormConstants(org.apache.struts.action.ActionMapping,
	 *          javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
	 */
    protected void prepareFormConstants(ActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);
        InfoDepartment infoDepartment = (InfoDepartment) ServiceUtils.executeService(userView,
                "ReadDepartmentByUser", new Object[]{userView.getUtilizador()});
        request.setAttribute("infoDepartment", infoDepartment);
        super.prepareFormConstants(mapping, request, form);
    }
}