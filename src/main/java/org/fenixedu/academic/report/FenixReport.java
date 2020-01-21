/**
 * Copyright © 2002 Instituto Superior Técnico
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
package org.fenixedu.academic.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.util.report.ReportPrinter.ReportDescription;
import org.fenixedu.commons.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

abstract public class FenixReport implements Serializable, ReportDescription {

    private static final Logger logger = LoggerFactory.getLogger(FenixReport.class);

    @SuppressWarnings("rawtypes")
    final private Collection dataSource;

    final private Map<String, Object> parameters = new HashMap<>();

    private final Locale locale;

    private final Locale language;

    private final JsonObject payload;

    static final protected String EMPTY_STR = StringUtils.EMPTY;

    static final protected String SINGLE_SPACE = " ";

    static final protected String DD_MMMM_YYYY = "dd MMMM yyyy";

    static final protected String DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";

    static final protected String YYYYMMMDD = "yyyyMMdd";

    static final protected String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    protected FenixReport() {
        this(null, I18N.getLocale());
    }

    protected FenixReport(final Locale locale) {
        this(null, locale);
    }

    private FenixReport(final Collection<?> dataSource, final Locale locale) {
        this.payload = new JsonObject();
        this.dataSource = dataSource == null ? new ArrayList<>() : dataSource;
        this.locale = locale;
        this.language = locale;
    }

    @Override
    public final Collection<?> getDataSource() {
        return dataSource;
    }

    @Override
    public final Map<String, Object> getParameters() {
        return parameters;
    }

    public Locale getLocale() {
        return locale;
    }

    public Locale getLanguage() {
        return language;
    }

    @Override
    public String getKey() {
        return getReportTemplateKey();
    }

    public String getReportTemplateKey() {
        return getClass().getName();
    }

    public void addParameter(final String key, final Object value) {
        this.parameters.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public void addDataSourceElement(final Object object) {
        this.dataSource.add(object);
    }

    @SuppressWarnings("unchecked")
    public void addDataSourceElements(final Collection<?> objects) {
        this.dataSource.addAll(objects);
    }

    abstract public String getReportFileName();

    abstract protected void fillReport();

    protected void printParameters() {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            logger.info(String.format("%s - %s", entry.getKey(), entry.getValue()));
        }

    }

    public JsonObject getPayload() {
        return payload;
    }
}
