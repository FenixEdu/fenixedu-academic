/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement.CreateResidenceEvents;
import net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement.PayResidenceEvent;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidenceDebtEventBean;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidenceEventBean;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ResidentListsHolderBean;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/residenceManagement", module = "residenceManagement",
        functionality = ResidenceEventManagementDispatchAction.class)
@Forwards({ @Forward(name = "importData", path = "/residenceManagement/importData.jsp"),
        @Forward(name = "yearConfiguration", path = "residenceManagement-yearConfiguration"),
        @Forward(name = "editPaymentLimitDay", path = "/residenceManagement/editPaymentLimitDay.jsp"),
        @Forward(name = "importCurrentDebt", path = "/residenceManagement/importCurrentDebts.jsp"),
        @Forward(name = "editRoomValues", path = "/residenceManagement/editRoomValues.jsp"),
        @Forward(name = "missingPayments", path = "/residenceManagement/missingPayment.jsp") })
public class ResidenceManagementDispatchAction extends FenixDispatchAction {

    public ActionForward missingPayments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<ResidenceEvent> results = new ArrayList<ResidenceEvent>();
        for (ResidenceMonth month : rootDomainObject.getResidenceMonths0Set()) {
            for (ResidenceEvent residenceEvent : month.getEventsSet()) {
                if (residenceEvent.isInDebt()) {
                    results.add(residenceEvent);
                }
            }
        }
        Collections.sort(results, new Comparator<ResidenceEvent>() {
            @Override
            public int compare(ResidenceEvent o1, ResidenceEvent o2) {
                return o1.getEventStateDate().compareTo(o2.getEventStateDate());
            }
        });
        request.setAttribute("list", results);
        return mapping.findForward("missingPayments");
    }

    public ActionForward importCurrentDebts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ImportResidenceEventBean bean = getRenderedObject("importFile");
        if (bean == null) {
            ResidenceMonth month = getResidenceMonth(request);
            bean = month != null ? new ImportResidenceEventBean(month) : new ImportResidenceEventBean();
        } else {

            List<ResidenceEventBean> sucessful = new ArrayList<ResidenceEventBean>();
            List<ResidenceEventBean> unsucessful = new ArrayList<ResidenceEventBean>();

            List<ResidenceEventBean> process = null;
            try {
                process = processCurrentDebts(bean);
            } catch (InvalidSpreadSheetName exception) {
                addActionMessage(request, "label.error.invalid.spreadsheetname", exception.getRequestedSheet());
                request.setAttribute("availableSpreadsheets", exception.getAvailableSpreadSheets());
                RenderUtils.invalidateViewState();
                request.setAttribute("importFileBean", bean);
                return mapping.findForward("importCurrentDebt");
            } catch (Exception exception) {
                addActionMessage(request, "label.error.invalid.table");
                RenderUtils.invalidateViewState();
                request.setAttribute("importFileBean", bean);
                return mapping.findForward("importCurrentDebt");
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
        return mapping.findForward("importCurrentDebt");

    }

    public ActionForward importData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ImportResidenceEventBean bean = getRenderedObject("importFile");
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
            } catch (Exception exception) {
                addActionMessage(request, "label.error.invalid.table");
                RenderUtils.invalidateViewState();
                request.setAttribute("importFileBean", bean);
                return mapping.findForward("importCurrentDebt");
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
        ImportResidenceEventBean bean = getRenderedObject("importFile");
        request.setAttribute("importFileBean", bean);
        return mapping.findForward("importData");
    }

    public ActionForward editPaymentLimitDay(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return editResidencePriceTableProperty(mapping, actionForm, request, response, "editPaymentLimitDay");
    }

    public ActionForward editRoomValues(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return editResidencePriceTableProperty(mapping, actionForm, request, response, "editRoomValues");
    }

    private ActionForward editResidencePriceTableProperty(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, String forwardName) throws Exception {

        ResidenceManagementUnit unit = getManagementUnit(request);
        ResidenceMonth month = getResidenceMonth(request);
        request.setAttribute("residenceMonth", month);
        request.setAttribute("priceTable", unit.getResidencePriceTable());

        return mapping.findForward(forwardName);
    }

    private ResidenceManagementUnit getManagementUnit(HttpServletRequest request) {
        return Bennu.getInstance().getResidenceManagementUnit();
    }

    private ResidenceMonth getResidenceMonth(HttpServletRequest request) {
        String oid = request.getParameter("monthOID");
        return oid == null ? null : FenixFramework.<ResidenceMonth> getDomainObject(oid);
    }

    public ActionForward generateDebts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResidentListsHolderBean listHolder = getRenderedObject("importList");
        ImportResidenceEventBean eventBean = getRenderedObject("dateBean");
        try {
            CreateResidenceEvents.run(listHolder.getSuccessfulEvents(), eventBean.getResidenceMonth());
        } catch (Exception e) {
            addActionMessage(request, e.getMessage());
            return importData(mapping, actionForm, request, response);
        }
        request.setAttribute("createdDebts", true);
        return importData(mapping, actionForm, request, response);
    }

    @Atomic
    public void makePayments(List<ResidenceEventBean> events, HttpServletRequest request) throws Exception {
        for (ResidenceEventBean event : events) {
            ResidenceDebtEventBean debtEvent = (ResidenceDebtEventBean) event;
            PayResidenceEvent.run(getLoggedPerson(request).getUser(), debtEvent.getEventObject(), debtEvent.getPaidDateObject());
        }
    }

    public ActionForward generatePayments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResidentListsHolderBean listHolder = getRenderedObject("importList");
        ImportResidenceEventBean eventBean = getRenderedObject("dateBean");

        try {
            makePayments(listHolder.getSuccessfulEvents(), request);
        } catch (Exception e) {
            addActionMessage(request, e.getMessage());
            return importData(mapping, actionForm, request, response);
        }

        request.setAttribute("createdDebts", true);
        return importCurrentDebts(mapping, actionForm, request, response);
    }

    private List<ResidenceEventBean> process(ImportResidenceEventBean bean) throws IOException, InvalidSpreadSheetName {
        List<ResidenceEventBean> beans = new ArrayList<ResidenceEventBean>();

        POIFSFileSystem fs = new POIFSFileSystem(bean.getFile());
        HSSFWorkbook wb = new HSSFWorkbook(fs);

        HSSFSheet sheet = wb.getSheetAt(0);

        if (sheet == null) {
            throw new InvalidSpreadSheetName(bean.getSpreadsheetName(), getAllSpreadsheets(wb));
        }

        int i = 2;
        HSSFRow row;
        while ((row = sheet.getRow(i)) != null) {
            String room = row.getCell((short) 0).getStringCellValue();
            if (StringUtils.isEmpty(room)) {
                break;
            }

            String userName = getValueFromColumnMayBeNull(row, 1);
            String fiscalNumber = getValueFromColumnMayBeNull(row, 2);
            String name = getValueFromColumnMayBeNull(row, 3);
            Double roomValue = new Double(row.getCell((short) 4).getNumericCellValue());
            beans.add(new ResidenceEventBean(userName, fiscalNumber, name, roomValue, room));

            i++;
        }
        return beans;
    }

    private List<ResidenceEventBean> processCurrentDebts(ImportResidenceEventBean bean) throws IOException,
            InvalidSpreadSheetName {
        List<ResidenceEventBean> beans = new ArrayList<ResidenceEventBean>();

        POIFSFileSystem fs = new POIFSFileSystem(bean.getFile());
        HSSFWorkbook wb = new HSSFWorkbook(fs);

        HSSFSheet sheet = wb.getSheetAt(0);

        if (sheet == null) {
            throw new InvalidSpreadSheetName(bean.getSpreadsheetName(), getAllSpreadsheets(wb));
        }

        int i = 2;
        HSSFRow row;
        while ((row = sheet.getRow(i)) != null) {
            String room = row.getCell((short) 0).getStringCellValue();
            if (StringUtils.isEmpty(room)) {
                break;
            }

            String userName = getValueFromColumnMayBeNull(row, 1);
            String fiscalNumber = getValueFromColumnMayBeNull(row, 2);
            String name = getValueFromColumnMayBeNull(row, 3);
            Double roomValue = new Double(row.getCell((short) 4).getNumericCellValue());
            String paidDate = getDateFromColumn(row, 5);
            Double roomValuePaid = new Double(row.getCell((short) 6).getNumericCellValue());
            ResidenceDebtEventBean residenceDebtEventBean =
                    new ResidenceDebtEventBean(userName, fiscalNumber, name, roomValue, room, paidDate, roomValuePaid);
            residenceDebtEventBean.setMonth(bean.getResidenceMonth());
            beans.add(residenceDebtEventBean);

            i++;
        }
        return beans;
    }

    private String getValueFromColumnMayBeNull(HSSFRow row, int i) {
        HSSFCell cell = row.getCell((short) i);
        if (cell == null) {
            return StringUtils.EMPTY;
        }
        return getValueFromColumn(row, i);
    }

    private String[] getAllSpreadsheets(HSSFWorkbook wb) {
        String[] spreadsheets = new String[wb.getNumberOfSheets()];
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            spreadsheets[i] = wb.getSheetName(i);
        }
        return spreadsheets;
    }

    private String getDateFromColumn(HSSFRow row, int i) {
        try {
            return new SimpleDateFormat("dd-MM-yy").format(row.getCell((short) i).getDateCellValue());
        } catch (Exception e) {
            return row.getCell((short) i).getStringCellValue();
        }
    }

    private String getValueFromColumn(HSSFRow row, int i) {
        try {
            return new Integer(new Double(row.getCell((short) i).getNumericCellValue()).intValue()).toString();
        } catch (NumberFormatException e) {
            return row.getCell((short) i).getStringCellValue();
        } catch (IllegalStateException e) {
            return row.getCell((short) i).getStringCellValue();
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