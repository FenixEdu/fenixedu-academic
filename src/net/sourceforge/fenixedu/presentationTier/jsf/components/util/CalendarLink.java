package net.sourceforge.fenixedu.presentationTier.jsf.components.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CalendarLink {
    private Calendar objectOccurrence;
    private String objectLinkLabel;
    private Map<String,String> linkParameters;

    public void setObjectOccurrence(Calendar objectOccurrence) {
        this.objectOccurrence = objectOccurrence;
    }
    
    public void setObjectOccurrence(Date objectOccurrence) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(objectOccurrence);
        this.objectOccurrence = calendar;
    }

    public Calendar getObjectOccurrence() {
        return this.objectOccurrence;
    }

    public void setObjectLinkLabel(String objectLinkLabel) {
        this.objectLinkLabel = objectLinkLabel;
    }

    public String getObjectLinkLabel() {
        return this.objectLinkLabel;
    }
    
    public void setLinkParameters(Map<String,String> linkParameters) {
        this.linkParameters = linkParameters;
    }

    public String giveLink(String editLinkPage) {
        final StringBuilder linkParameters = new StringBuilder();
        linkParameters.append(editLinkPage);

        if (this.linkParameters != null && !this.linkParameters.isEmpty()) {
            linkParameters.append('?');
            for (final Iterator<Entry<String, String>> iterator = this.linkParameters.entrySet().iterator();
                    iterator.hasNext(); ) {
                final Entry<String, String> entry = iterator.next();
                linkParameters.append(entry.getKey());
                linkParameters.append('=');
                linkParameters.append(entry.getValue());

                if (iterator.hasNext()) {
                    linkParameters.append('&');
                }
            }
        }
        return linkParameters.toString();
    }

}
