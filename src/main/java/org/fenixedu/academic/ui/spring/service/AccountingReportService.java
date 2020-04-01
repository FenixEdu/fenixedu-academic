/**
 * Copyright © 2018 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.academic.ui.spring.service;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.accounting.AccountingTransactionDetail;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.domain.accounting.accountingTransactions.detail.SibsTransactionDetail;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.springframework.stereotype.Service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */

@Service
public class AccountingReportService {

    private final String DATE_PATTERN = "dd/MM/yyyy";
    private final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

    private boolean isFor(AccountingTransactionDetail atd, DateTime start, DateTime end) {
        final DateTime whenRegistered = atd.getWhenRegistered();
        return new Interval(start, end.plusDays(1)).contains(whenRegistered);
    }

    private void fill(Spreadsheet dateSheet, List<AccountingTransactionDetail> value) {
        value.stream().sorted(Comparator.comparing(AccountingTransactionDetail::getWhenRegistered)).forEach(atd -> {
            fillPayment(dateSheet, atd);
        });
    }

	private void fillPayment(Spreadsheet dateSheet, AccountingTransactionDetail atd) {
		Event event = atd.getEvent();
		Spreadsheet.Row row = dateSheet.addRow();

		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.whenProcessed"), atd.getWhenProcessed().toString(DATE_TIME_PATTERN));
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.whenRegistered"), atd.getWhenRegistered().toString(DATE_TIME_PATTERN));
		YearMonthDay sibsDate = null;
		if (atd instanceof SibsTransactionDetail) {
		    sibsDate = ((SibsTransactionDetail)atd).getSibsLine().getHeader().getWhenProcessedBySibs();
		}
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.sibsDate"), sibsDate != null ? sibsDate.toString(DATE_PATTERN) : "-");
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.client"), event.getParty().getPartyPresentationName());
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.event.id"), event.getExternalId());
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.event.description"), event.getDescription().toString());
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.event.type"), BundleUtil.getString(Bundle.ENUMERATION, event.getEventType().getQualifiedName()));
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.payment.method"), atd.getPaymentMethod().getLocalizedName());
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.payment.reference"), atd.getPaymentReference());
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.value"), atd.getTransaction().getAmountWithAdjustment().toPlainString());
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.creator"),atd.getTransaction().getResponsibleUser()==null? "-": 
		        atd.getTransaction().getResponsibleUser().getDisplayName());
		row.setCell(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.creator.username"),atd.getTransaction().getResponsibleUser()==null? "-": 
		        atd.getTransaction().getResponsibleUser().getUsername());
	}
    
    public Spreadsheet reportList(LocalDate start, LocalDate end) {
        DateTime startDateTime = start.toDateTimeAtStartOfDay();
        DateTime endDateTime = end.toDateTimeAtStartOfDay();
    	Spreadsheet payments = new Spreadsheet("Pagamentos detalhados");
		Bennu.getInstance().getAccountingTransactionDetailsSet().stream()
			.filter(atd -> !atd.getTransaction().isAdjustingTransaction())	
			.filter(atd -> isFor(atd, startDateTime, endDateTime))
        	.forEach(atd -> fillPayment(payments, atd));
		
		return payments;
    }
    
    public Spreadsheet report(LocalDate start, LocalDate end) {
        DateTime startDateTime = start.toDateTimeAtStartOfDay();
        DateTime endDateTime = end.toDateTimeAtStartOfDay();

        final Map<LocalDate, List<AccountingTransactionDetail>> dateTransactionsMap =
                Bennu.getInstance().getAccountingTransactionDetailsSet().stream()
                        .filter(atd -> isFor(atd, startDateTime, endDateTime))
                        .filter(atd -> !atd.getTransaction().isAdjustingTransaction())
                        .collect(Collectors.groupingBy(atd -> atd.getWhenRegistered().toLocalDate()));

        final Table<LocalDate, PaymentMethod, Money> table = HashBasedTable.create();

        dateTransactionsMap.forEach((localDate,accountingTransactionDetails) -> {
            Map<PaymentMethod, Money> paymentMethodMoneyMap = accountingTransactionDetails.stream().collect(Collectors
                    .toMap(AccountingTransactionDetail::getPaymentMethod, atd -> atd.getTransaction().getAmountWithAdjustment(),
                            Money::add));
            paymentMethodMoneyMap.forEach((paymentMethod, totalMoney) -> table.put(localDate, paymentMethod, totalMoney));
        });

        final Spreadsheet spreadsheet = new Spreadsheet(BundleUtil.getString(Bundle.ACCOUNTING,"label.transaction.report.summary"));
        final List<PaymentMethod> sortedPaymentMethods =
                Bennu.getInstance().getPaymentMethodSet().stream().sorted(Comparator.comparing(PaymentMethod::getName)).collect
                        (Collectors.toList());

        spreadsheet.setHeader(0, BundleUtil.getString(Bundle.ACCOUNTING, "label.transaction.report.whenRegistered"));

        for (int i = 0; i < sortedPaymentMethods.size(); i++) {
            spreadsheet.setHeader(i + 1, sortedPaymentMethods.get(i).getLocalizedName());
        }

        table.rowMap().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(entry -> {
            final LocalDate localDate = entry.getKey();
            final Map<PaymentMethod, Money> paymentMethodMoneyMap = entry.getValue();
            Spreadsheet.Row sheetRow = spreadsheet.addRow();
            sheetRow.setCell(0, localDate.toString(DATE_PATTERN));
            paymentMethodMoneyMap.forEach((paymentMethod, money) -> {
                sheetRow.setCell(sortedPaymentMethods.indexOf(paymentMethod) + 1, money.toPlainString());
            });
        });

        final Spreadsheet[] holder = new Spreadsheet[1];
        holder[0] = spreadsheet;

        dateTransactionsMap.keySet().stream().sorted(Comparator.reverseOrder()).forEach(date -> {
            Spreadsheet dateSheet = holder[0].addSpreadsheet(date.toString(DATE_PATTERN).replaceAll("/", "_"));
            fill(dateSheet, dateTransactionsMap.get(date));
            holder[0] = dateSheet;
        });

        return spreadsheet;
    }

}
