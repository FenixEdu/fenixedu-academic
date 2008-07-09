package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidenceEventBean;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidentListsHolderBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.SimpleFileBean;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/residenceManagement", module = "residenceManagement")
@Forwards( { @Forward(name = "importData", path = "residenceManagement-importData"),
	 @Forward(name="paymentLimits", path="residenceManagement-paymentLimits") })
public class ResidenceManagementDispatchAction extends FenixDispatchAction {

    public ActionForward importData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ImportResidenceEventBean bean = (ImportResidenceEventBean) getRenderedObject("importFile");
	if (bean == null) {
	    bean = new ImportResidenceEventBean();
	} else {

	    List<ResidenceEventBean> sucessful = new ArrayList<ResidenceEventBean>();
	    List<ResidenceEventBean> unsucessful = new ArrayList<ResidenceEventBean>();

	    for (ResidenceEventBean eventBean : process(bean)) {
		if (eventBean.getStatus()) {
		    sucessful.add(eventBean);
		} else {
		    unsucessful.add(eventBean);
		}
	    }

	    ResidentListsHolderBean listHolder = new ResidentListsHolderBean(sucessful, unsucessful);
	    request.setAttribute("importList", listHolder);
	}

	RenderUtils.invalidateViewState();
	request.setAttribute("importFileBean", bean);
	return mapping.findForward("importData");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ImportResidenceEventBean bean = (ImportResidenceEventBean) getRenderedObject("editBean");
	RenderUtils.invalidateViewState();
	request.setAttribute("importFileBean", bean);
	return mapping.findForward("importData");
    }

    public ActionForward createYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	executeService("CreateNewResidenceYear", new Object[] {});
	return importData(mapping, actionForm, request, response);
    }

    public ActionForward configurePaymentLimits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IViewState viewState = RenderUtils.getViewState("paymentLimits");
	ImportResidenceEventBean bean = viewState != null ?  (ImportResidenceEventBean) viewState.getMetaObject().getObject() : new ImportResidenceEventBean();
	RenderUtils.invalidateViewState();
	request.setAttribute("paymentLimits", bean);
	return mapping.findForward("paymentLimits");
    }
    
    private List<ResidenceEventBean> process(SimpleFileBean bean) throws IOException {
	List<ResidenceEventBean> beans = new ArrayList<ResidenceEventBean>();

	POIFSFileSystem fs = new POIFSFileSystem(bean.getFile());
	HSSFWorkbook wb = new HSSFWorkbook(fs);

	HSSFSheet sheet = wb.getSheet("NOVEMBRO_07");

	int i = 3;
	HSSFRow row;
	while ((row = sheet.getRow(i)) != null) {
	    String room = row.getCell((short) 0).getStringCellValue();
	    if (StringUtils.isEmpty(room))
		break;

	    String userName = String.valueOf(new Double(row.getCell((short) 1).getNumericCellValue()).intValue());
	    String name = row.getCell((short) 2).getStringCellValue();
	    String fiscalNumber = getValueFromColumn(row, 6);
	    Double roomValue = new Double(row.getCell((short) 8).getNumericCellValue());
	    beans.add(new ResidenceEventBean(userName, fiscalNumber, name, roomValue));

	    i++;
	}
	return beans;
    }

    private String getValueFromColumn(HSSFRow row, int i) {
	try {
	    return new Integer(new Double(row.getCell((short) 6).getNumericCellValue()).intValue()).toString();
	} catch (NumberFormatException e) {
	    return row.getCell((short) 6).getStringCellValue();
	}
    }
}
