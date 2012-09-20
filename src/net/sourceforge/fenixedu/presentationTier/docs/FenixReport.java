package net.sourceforge.fenixedu.presentationTier.docs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.DateI18NUtil;
import net.sourceforge.fenixedu.util.JasperPrintProcessor;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

abstract public class FenixReport implements Serializable {

    private static String realPath = null;

    @SuppressWarnings("unchecked")
    final private Collection dataSource;

    final private Map<String, Object> parameters = new HashMap<String, Object>();

    private ResourceBundle resourceBundle;

    private final ResourceBundle applicationBundle;

    private final ResourceBundle enumerationBundle;

    final private Locale locale;

    final private Language language;

    static final public Locale[] suportedLocales = { Language.getDefaultLocale(), new Locale("en") };

    static final protected String EMPTY_STR = StringUtils.EMPTY;

    static final protected String SINGLE_SPACE = StringUtils.SINGLE_SPACE;

    static final protected String DD_MMMM_YYYY = "dd MMMM yyyy";

    static final protected String DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";

    static final protected String YYYYMMMDD = "yyyyMMdd";

    static final protected String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    {
	addParameter("path", realPath);
    }

    protected FenixReport() {
	this(null, Language.getLocale());
    }

    protected FenixReport(final Locale locale) {
	this(null, locale);
    }

    @SuppressWarnings("unchecked")
    private FenixReport(final Collection dataSource, final Locale locale) {
	this.dataSource = (dataSource == null) ? new ArrayList() : dataSource;
	this.enumerationBundle = ResourceBundle.getBundle("resources.EnumerationResources", locale);
	this.applicationBundle = ResourceBundle.getBundle("resources.ApplicationResources", locale);
	this.locale = locale;
	this.language = Language.valueOf(locale.getLanguage());
    }

    @SuppressWarnings("unchecked")
    public final Collection getDataSource() {
	return dataSource;
    }

    public final Map<String, Object> getParameters() {
	return parameters;
    }

    public final ResourceBundle getResourceBundle() {
	return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
	this.resourceBundle = resourceBundle;
    }

    public ResourceBundle getApplicationBundle() {
	return applicationBundle;
    }

    public ResourceBundle getEnumerationBundle() {
	return enumerationBundle;
    }

    public Locale getLocale() {
	return locale;
    }

    public Language getLanguage() {
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
    public void addDataSourceElements(final Collection objects) {
	this.dataSource.addAll(objects);
    }

    abstract public String getReportFileName();

    abstract protected void fillReport();

    public synchronized static void setRealPath(String realPath) {
	FenixReport.realPath = realPath;
    }

    protected void printParameters() {
	for (Map.Entry<String, Object> entry : parameters.entrySet()) {
	    System.out.println(String.format("%s - %s", entry.getKey(), entry.getValue()));
	}

    }

    protected String verboseDate(LocalDate date) {
	return "dia " + DateI18NUtil.verboseNumber(date.getDayOfMonth(), getEnumerationBundle()) + " do mês de "
		+ date.toString("MMMM", new Locale("pt")) + " de "
		+ DateI18NUtil.verboseNumber(date.getYear(), getEnumerationBundle());
    }
}
