package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidenceEventBean;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidentListsHolderBean;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/residenceManagement", module = "residenceManagement")
@Forwards( { @Forward(name = "importData", path = "residenceManagement-importData"),
	@Forward(name = "yearConfiguration", path = "residenceManagement-yearConfiguration"),
	@Forward(name = "editPaymentLimitDay", path = "/residenceManagement/editPaymentLimitDay.jsp"),
	@Forward(name = "editRoomValues", path = "/residenceManagement/editRoomValues.jsp") })
public class ResidenceManagementDispatchAction extends FenixDispatchAction {

    public ActionForward importData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ImportResidenceEventBean bean = (ImportResidenceEventBean) getRenderedObject("importFile");
	if (bean == null) {
	    ResidenceMonth month = getResidenceMonth(request);
	    bean = month != null ? new ImportResidenceEventBean(month) : new ImportResidenceEventBean();
	} else {

	    List<ResidenceEventBean> sucessful = new ArrayList<ResidenceEventBean>();
	    List<ResidenceEventBean> unsucessful = new ArrayList<ResidenceEventBean>();

	    List<ResidenceEventBean> process = null;
	    try {
		process = process(bean);
	    } catch (InvalidSpreadSheetName exception) {
		addActionMessage(request, "label.error.invalid.spreadsheetname", exception.getRequestedSheet());
		request.setAttribute("availableSpreadsheets", exception.getAvailableSpreadSheets());
		RenderUtils.invalidateViewState();
		request.setAttribute("importFileBean", bean);
		return mapping.findForward("importData");
	    }

	    for (ResidenceEventBean eventBean : process) {
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

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ImportResidenceEventBean bean = (ImportResidenceEventBean) getRenderedObject("importFile");
	request.setAttribute("importFileBean", bean);
	return mapping.findForward("importData");
    }

    public ActionForward createYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	executeService("CreateNewResidenceYear", new Object[] {});
	return importData(mapping, actionForm, request, response);
    }

    public ActionForward editPaymentLimitDay(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return editResidenceYearProperty(mapping, actionForm, request, response, "editPaymentLimitDay");
    }

    public ActionForward editRoomValues(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return editResidenceYearProperty(mapping, actionForm, request, response, "editRoomValues");
    }

    private ActionForward editResidenceYearProperty(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, String forwardName) throws Exception {

	ResidenceMonth month = getResidenceMonth(request);
	request.setAttribute("residenceMonth", month);

	return mapping.findForward(forwardName);
    }

    private ResidenceMonth getResidenceMonth(HttpServletRequest request) {
	String oid = request.getParameter("monthOID");
	return oid == null ? null : (ResidenceMonth) DomainObject.fromOID(Long.valueOf(oid));
    }

    public ActionForward generateDebts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ResidentListsHolderBean listHolder = (ResidentListsHolderBean) getRenderedObject("importList");
	ImportResidenceEventBean eventBean = (ImportResidenceEventBean) getRenderedObject("dateBean");
	try {
	    executeService("CreateResidenceEvents", new Object[] { listHolder.getSuccessfulEvents(),
		    eventBean.getResidenceMonth() });
	} catch (Exception e) {
	    addActionMessage(request, e.getMessage());
	    return importData(mapping, actionForm, request, response);
	}
	request.setAttribute("createdDebts", true);
	return importData(mapping, actionForm, request, response);
    }

    private List<ResidenceEventBean> process(ImportResidenceEventBean bean) throws IOException, InvalidSpreadSheetName {
	List<ResidenceEventBean> beans = new ArrayList<ResidenceEventBean>();

	POIFSFileSystem fs = new POIFSFileSystem(bean.getFile());
	HSSFWorkbook wb = new HSSFWorkbook(fs);

	HSSFSheet sheet = wb.getSheet(bean.getSpreadsheetName());

	if (sheet == null) {
	    throw new InvalidSpreadSheetName(bean.getSpreadsheetName(), getAllSpreadsheets(wb));
	}

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

    private String[] getAllSpreadsheets(HSSFWorkbook wb) {
	String[] spreadsheets = new String[wb.getNumberOfSheets()];
	for (int i = 0; i < wb.getNumberOfSheets(); i++) {
	    spreadsheets[i] = wb.getSheetName(i);
	}
	return spreadsheets;
    }

    private String getValueFromColumn(HSSFRow row, int i) {
	try {
	    return new Integer(new Double(row.getCell((short) 6).getNumericCellValue()).intValue()).toString();
	} catch (NumberFormatException e) {
	    return row.getCell((short) 6).getStringCellValue();
	}
    }

    private class InvalidSpreadSheetName extends Exception {
	private final String requestedSheet;
	private final String[] availableSpreadSheets;

	public InvalidSpreadSheetName(String requestedSheet, String[] availableSpreadSheets) {
	    this.requestedSheet = requestedSheet;
	    this.availableSpreadSheets = availableSpreadSheets;
	}

	public String[] getAvailableSpreadSheets() {
	    return availableSpreadSheets;
	}

	public String getRequestedSheet() {
	    return requestedSheet;
	}

    }
}
