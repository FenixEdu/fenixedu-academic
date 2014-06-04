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
package net.sourceforge.fenixedu.presentationTier.docs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.fenixedu.util.DateI18NUtil;
import net.sourceforge.fenixedu.util.JasperPrintProcessor;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class FenixReport implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FenixReport.class);

    @SuppressWarnings("rawtypes")
    final private Collection dataSource;

    final private Map<String, Object> parameters = new HashMap<String, Object>();

    private final Locale locale;

    private final Locale language;

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
        this.dataSource = dataSource == null ? new ArrayList<Object>() : dataSource;
        this.locale = locale;
        this.language = locale;
    }

    public final Collection<?> getDataSource() {
        return dataSource;
    }

    public final Map<String, Object> getParameters() {
        return parameters;
    }

    public Locale getLocale() {
        return locale;
    }

    public Locale getLanguage() {
        return language;
    }

    public String getReportTemplateKey() {
        return getClass().getName();
    }

    public JasperPrintProcessor getPreProcessor() {
        return null;
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

    protected String verboseDate(LocalDate date) {
        return "dia " + DateI18NUtil.verboseNumber(date.getDayOfMonth()) + " do mês de "
                + date.toString("MMMM", new Locale("pt")) + " de " + DateI18NUtil.verboseNumber(date.getYear());
    }
}
