package net.sourceforge.fenixedu.presentationTier.docs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.JasperPrintProcessor;
import net.sourceforge.fenixedu.util.LanguageUtils;

abstract public class FenixReport implements Serializable {

    private final Collection dataSource;
    private final Map<String, Object> parameters = new HashMap<String, Object>();

    protected ResourceBundle resourceBundle;
    static protected final ResourceBundle enumerationBundle = ResourceBundle.getBundle("resources.EnumerationResources",
	    LanguageUtils.getLocale());
    
    protected FenixReport() {
	this.dataSource = new ArrayList();
    }
    
    protected FenixReport(final Collection dataSource) {
	this.dataSource = (dataSource == null) ? new ArrayList() : dataSource;
    }

    public final Map<String, Object> getParameters() {
	return parameters;
    }

    public final ResourceBundle getResourceBundle() {
	return resourceBundle;
    }

    public final Collection getDataSource() {
	return dataSource;
    }

    public String getReportTemplateKey() {
	return getClass().getName();
    }

    public JasperPrintProcessor getPreProcessor() {
	return null;
    }

    protected void addParameter(final String key, final Object value) {
	this.parameters.put(key, value);
    }
    
    protected void addDataSourceElement(final Object object) {
	this.dataSource.add(object);
    }
    
    protected void addDataSourceElements(final Collection objects) {
	this.dataSource.addAll(objects);
    }

    abstract public String getReportFileName();
    abstract protected void fillReport();
}
