package net.sourceforge.fenixedu.util.report;

import java.util.Collection;
import java.util.Map;

public interface ReportPrinter {

    public byte[] printReports(ReportDescription... reports) throws Exception;

    public default byte[] printReport(String key, Map<String, Object> parameters, Collection<?> dataSource) throws Exception {
        return printReports(new ReportDescription() {

            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Map<String, Object> getParameters() {
                return parameters;
            }

            @Override
            public Collection<?> getDataSource() {
                return dataSource;
            }
        });
    }

    public static interface ReportDescription {

        public String getKey();

        public Map<String, Object> getParameters();

        public Collection<?> getDataSource();

    }

}
