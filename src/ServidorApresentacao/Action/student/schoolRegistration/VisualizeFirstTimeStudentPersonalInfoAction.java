/*
 * Created on Jul 21, 2004
 *
 */
package ServidorApresentacao.Action.student.schoolRegistration;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoCountry;
import DataBeans.InfoPerson;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import Util.EstadoCivil;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */

public class VisualizeFirstTimeStudentPersonalInfoAction// extends TransactionalDispatchAction {
extends ServidorApresentacao.Action.base.FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoPerson infoPerson = null;

		Object args[] = new Object[1];
		args[0] = userView;

		infoPerson =
			(InfoPerson) ServiceManagerServiceFactory.executeService(
				userView,
				"ReadPersonByUsername",
				args);

		request.removeAttribute("personalInfo");

		request.setAttribute("personalInfo", infoPerson);
		session.setAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY, new EstadoCivil().toArrayList());
        
//		Get List of available Countries
        Object result = null;
        result = ServiceManagerServiceFactory.executeService(userView, "ReadAllCountries", null);
        ArrayList country = (ArrayList) result;

        //			Build List of Countries for the Form
        Iterator iterador = country.iterator();

        ArrayList nationalityList = new ArrayList();
        while (iterador.hasNext())
        {
            InfoCountry countryTemp = (InfoCountry) iterador.next();
            nationalityList.add(
                new LabelValueBean(countryTemp.getNationality(), countryTemp.getNationality()));
        }

        session.setAttribute(SessionConstants.NATIONALITY_LIST_KEY, nationalityList);

		return mapping.findForward("Success");
	}

}

