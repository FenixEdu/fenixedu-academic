package org.fenixedu.academic.ui.spring.service;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.residence.ResidenceMonth;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.residenceManagement.ResidenceEventBean;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.fenixframework.Atomic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class ResidenceEventManagementService {

    @Atomic
    public void createResidenceEvents(List<ResidenceEventBean> beans, ResidenceMonth month) {
        beans.stream()
                .filter(bean -> !month.isEventPresent(bean.getStudent().getPerson()))
                .forEach(bean -> new ResidenceEvent(month, bean.getStudent().getPerson(), bean.getRoomValue(), bean.getRoom()));
    }

    @Atomic
    public void cancelResidenceEvent(final ResidenceEvent event, final Person responsible) {
        event.cancel(responsible);
    }

    @Atomic
    public void payResidenceEvent(final ResidenceEvent event, final User responsible) {
        event.process(responsible, event.calculateEntries(), new AccountingTransactionDetailDTO(DateTime.now(),
                PaymentMethod.getCashPaymentMethod(),"", null));
    }

    public List<ResidenceEventBean> process(final ResidenceMonth residenceMonth, MultipartFile file) throws IOException, InvalidSpreadSheetName {
        List<ResidenceEventBean> beans = new ArrayList<>();

        POIFSFileSystem fs = new POIFSFileSystem(file.getInputStream());
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);

        if (sheet == null) {
            throw new InvalidSpreadSheetName(file.getOriginalFilename(), getAllSpreadsheets(wb));
        }

        for (int i = 2; sheet.getRow(i) != null; i++) {
            HSSFRow row = sheet.getRow(i);

            String room = row.getCell(0).getStringCellValue();
            if (StringUtils.isEmpty(room)) {
                break;
            }

            String userName = getValueFromColumnMayBeNull(row, 1);
            String fiscalNumber = getValueFromColumnMayBeNull(row, 2);
            String name = getValueFromColumnMayBeNull(row, 3);
            Double roomValue = row.getCell(4).getNumericCellValue();

            beans.add(new ResidenceEventBean(residenceMonth, userName, fiscalNumber, name, roomValue, room));
        }
        return beans;
    }

    private String getValueFromColumnMayBeNull(HSSFRow row, int i) {
        return Optional.ofNullable(row.getCell(i)).map(cell -> getValueFromColumn(row, i)).orElse(StringUtils.EMPTY);
    }

    private String[] getAllSpreadsheets(HSSFWorkbook wb) {
        return IntStream.range(0, wb.getNumberOfSheets()).mapToObj(wb::getSheetName).toArray(String[]::new);
    }

    private String getValueFromColumn(HSSFRow row, int i) {
        try {
            return Integer.toString(new Double(row.getCell(i).getNumericCellValue()).intValue());
        } catch (NumberFormatException | IllegalStateException e) {
            return row.getCell(i).getStringCellValue();
        }
    }

    public static class InvalidSpreadSheetName extends Exception {
        private static final long serialVersionUID = 5837631244624763747L;
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
