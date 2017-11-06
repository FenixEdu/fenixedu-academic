package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.List;

import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.academic.util.report.ReportsUtils;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class DefaultDocumentGenerator implements DocumentGenerator {

    private static DocumentGenerator documentGenerator;

    static {
        setGenerator(new DefaultDocumentGenerator());
    }

    @Override
    public byte[] generateReport(List<? extends FenixReport> documents) {
        FenixReport[] reports = documents.toArray(new FenixReport[] {});
        return ReportsUtils.generateReport(reports).getData();
    }

    public static DocumentGenerator getGenerator() {
        return documentGenerator;
    }

    public static void setGenerator(DocumentGenerator documentGenerator) {
        DefaultDocumentGenerator.documentGenerator = documentGenerator;
    }
}
