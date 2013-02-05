package net.sourceforge.fenixedu.util;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * This interface can be used to make changes to the jasper report in it's
 * intermediary form.
 * 
 * @author cfgi
 */
public interface JasperPrintProcessor {

    public JasperPrint process(JasperPrint jasperPrint);

}
