/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 26, 2006,10:56:35 AM
 */
package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.FormatRenderer;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

import org.joda.time.DateTime;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br><br>
 * Created on Jul 26, 2006,10:56:35 AM
 *
 */
public class DateTimeDataDependentRenderer extends FormatRenderer {

    private String formatWithTime;
    private String formatWithoutTime;

    public String getFormatWithoutTime() {
        return formatWithoutTime;
    }

    public void setFormatWithoutTime(String formatWithoutTime) {
        this.formatWithoutTime = formatWithoutTime;
    }

    public String getFormatWithTime() {
        return formatWithTime;
    }

    public void setFormatWithTime(String formatWithTime) {
        this.formatWithTime = formatWithTime;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        if (object == null) {
            return super.getLayout(object, type);
        }
        
        DateTime dateTime = (DateTime) object;
        if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0) {
            setFormat(getFormatWithoutTime());
        }
        else {
            setFormat(getFormatWithTime());
        }
        
        return super.getLayout(object, type);
    }
    
    
}
