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
package net.sourceforge.fenixedu.util.sibs.incomming;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class SibsIncommingPaymentFile {

    private static final String HEADER_REGISTER_TYPE = "0";

    private static final String DETAIL_REGISTER_TYPE = "2";

    private static final String FOOTER_REGISTER_TYPE = "9";

    private SibsIncommingPaymentFileHeader header;

    private List<SibsIncommingPaymentFileDetailLine> detailLines;

    private SibsIncommingPaymentFileFooter footer;

    private String filename;

    private SibsIncommingPaymentFile(String filename, SibsIncommingPaymentFileHeader header,
            SibsIncommingPaymentFileFooter footer, List<SibsIncommingPaymentFileDetailLine> detailLines) {
        this.filename = filename;
        this.header = header;
        this.footer = footer;
        this.detailLines = detailLines;

        checkIfDetailLinesTotalAmountMatchesFooterTotalAmount();
    }

    private void checkIfDetailLinesTotalAmountMatchesFooterTotalAmount() {
        Money totalEntriesAmount = Money.ZERO;
        for (final SibsIncommingPaymentFileDetailLine detailLine : getDetailLines()) {
            totalEntriesAmount = totalEntriesAmount.add(detailLine.getAmount());
        }

        if (!totalEntriesAmount.equals(footer.getTransactionsTotalAmount())) {
            throw new RuntimeException("Footer total amount does not match detail lines total amount");
        }

    }

    public static SibsIncommingPaymentFile parse(final File file) {
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            return parse(pt.utl.ist.fenix.tools.util.FileUtils.getFilenameOnly(file.getName()), inputStream);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static SibsIncommingPaymentFile parse(String filename, InputStream stream) {

        SibsIncommingPaymentFileHeader header = null;
        SibsIncommingPaymentFileFooter footer = null;
        final List<SibsIncommingPaymentFileDetailLine> detailLines = new ArrayList<SibsIncommingPaymentFileDetailLine>();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try {
            String line = reader.readLine();
            while (line != null) {

                if (isHeader(line)) {
                    header = SibsIncommingPaymentFileHeader.buildFrom(line);
                } else if (isDetail(line)) {
                    detailLines.add(SibsIncommingPaymentFileDetailLine.buildFrom(line));
                } else if (isFooter(line)) {
                    footer = SibsIncommingPaymentFileFooter.buildFrom(line);
                } else {
                    throw new RuntimeException("Unknown sibs incomming payment file line type");
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new SibsIncommingPaymentFile(filename, header, footer, detailLines);

    }

    private static boolean isFooter(String line) {
        return line.startsWith(FOOTER_REGISTER_TYPE);
    }

    private static boolean isHeader(String line) {
        return line.startsWith(HEADER_REGISTER_TYPE);
    }

    private static boolean isDetail(String line) {
        return line.startsWith(DETAIL_REGISTER_TYPE);
    }

    public List<SibsIncommingPaymentFileDetailLine> getDetailLines() {
        return Collections.unmodifiableList(detailLines);
    }

    public SibsIncommingPaymentFileFooter getFooter() {
        return footer;
    }

    public SibsIncommingPaymentFileHeader getHeader() {
        return header;
    }

    public String getFilename() {
        return filename;
    }

    public YearMonthDay getWhenProcessedBySibs() {
        return getHeader().getWhenProcessedBySibs();
    }

    public Integer getVersion() {
        return getHeader().getVersion();
    }

}
