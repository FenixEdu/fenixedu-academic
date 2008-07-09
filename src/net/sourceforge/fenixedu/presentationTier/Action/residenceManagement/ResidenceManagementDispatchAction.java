package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
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

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/residenceManagement", module = "residenceManagement")
@Forwards( { @Forward(name = "importData", path = "residenceManagement-importData") })
public class ResidenceManagementDispatchAction extends FenixDispatchAction {

    public ActionForward importData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ImportResidenceEventBean bean = (ImportResidenceEventBean) getRenderedObject("importFile");
	if (bean == null) {
	    bean = new ImportResidenceEventBean();
	} else {
	    process(bean);
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

    private void process(SimpleFileBean bean) throws IOException {
	POIFSFileSystem fs = new POIFSFileSystem(bean.getFile());
	HSSFWorkbook wb = new HSSFWorkbook(fs);

	HSSFSheet sheet = wb.getSheet("NOVEMBRO_07");

	int i = 3;
	HSSFRow row;
	while ((row = sheet.getRow(i)) != null) {
	    String room = row.getCell((short) 0).getStringCellValue();
	    if (StringUtils.isEmpty(room))
		break;
	    System.out.println(new Double(row.getCell((short) 1).getNumericCellValue()).intValue());
	    System.out.println(row.getCell((short) 2).getStringCellValue());
	    System.out.println(getValueFromColumn(row, 6));
	    System.out.println(row.getCell((short) 8).getNumericCellValue());
	    i++;
	}
    }

    private String getValueFromColumn(HSSFRow row, int i) {
	try {
	    return new Integer(new Double(row.getCell((short) 6).getNumericCellValue()).intValue()).toString();
	} catch (NumberFormatException e) {
	    return row.getCell((short) 6).getStringCellValue();
	}
    }
}
