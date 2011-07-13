package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.GiafInterfaceBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.GiafInterfaceDocument;
import net.sourceforge.fenixedu.domain.exceptions.InvalidGiafCodeException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.GiafInterface;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/giafInterface", module = "personnelSection")
@Forwards( { @Forward(name = "employee-hour-value", path = "/managementAssiduousness/giafInterface/showEmployeeHourValue.jsp"),
	@Forward(name = "insert-A17-vacations", path = "/managementAssiduousness/giafInterface/insertA17Vacations.jsp") })
public class GIAFInterfaceDispatchAction extends FenixDispatchAction {

    public ActionForward showEmployeeHourValue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	GiafInterfaceBean giafInterfaceBean = getRenderedObject("giafInterfaceBean");
	if (giafInterfaceBean == null) {
	    giafInterfaceBean = new GiafInterfaceBean();
	} else {
	    Employee employee = giafInterfaceBean.getEmployee();
	    if (employee == null) {
		addErrorMessage(request, "message", "error.invalidEmployee");
	    } else {
		try {
		    BigDecimal hourValue = new GiafInterface().getEmployeeHourValue(employee, giafInterfaceBean.getLocalDate());
		    request.setAttribute("hourValue", hourValue);
		} catch (ExcepcaoPersistencia e) {
		    e.printStackTrace();
		    addErrorMessage(request, "message", "error.connectionError");
		}
	    }
	}
	request.setAttribute("giafInterfaceBean", giafInterfaceBean);
	return mapping.findForward("employee-hour-value");
    }

    public ActionForward insertA17Vacations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	GiafInterfaceBean giafInterfaceBean = getRenderedObject("giafInterfaceBean");
	if (giafInterfaceBean == null) {
	    giafInterfaceBean = new GiafInterfaceBean();
	} else {
	    giafInterfaceBean.consume();
	    try {
		GiafInterfaceDocument.createGiafInterfaceDocument(giafInterfaceBean);
		new GiafInterface().exportVacationsToGIAF(new String(giafInterfaceBean.getFileByteArray()));
	    } catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		addErrorMessage(request, "message", "error.connectionError");
	    } catch (SQLException e) {
		e.printStackTrace();
		addErrorMessage(request, "message", "error.connectionError");
	    } catch (InvalidGiafCodeException e) {
		addErrorMessage(request, "message", e.getMessage());
	    }
	}
	List<GiafInterfaceDocument> giafInterfaceDocuments = new ArrayList<GiafInterfaceDocument>(rootDomainObject
		.getGiafInterfaceDocuments());
	Collections.sort(giafInterfaceDocuments, new BeanComparator("createdWhen"));
	Collections.reverse(giafInterfaceDocuments);
	request.setAttribute("giafInterfaceDocuments", giafInterfaceDocuments);
	request.setAttribute("giafInterfaceBean", giafInterfaceBean);
	return mapping.findForward("insert-A17-vacations");
    }

}