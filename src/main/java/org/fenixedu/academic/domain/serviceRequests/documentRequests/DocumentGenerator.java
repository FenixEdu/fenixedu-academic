package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.List;

import org.fenixedu.academic.report.FenixReport;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public interface DocumentGenerator {
    byte[] generateReport(List<? extends FenixReport> documents);
}
