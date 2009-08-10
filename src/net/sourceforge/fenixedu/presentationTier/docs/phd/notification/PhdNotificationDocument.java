package net.sourceforge.fenixedu.presentationTier.docs.phd.notification;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotification;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.joda.time.DateTime;

public class PhdNotificationDocument extends FenixReport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DomainReference<PhdNotification> notification;

    public PhdNotificationDocument(PhdNotification notification) {
	setNotification(notification);
    }

    private PhdNotification getNotification() {
	return (this.notification != null) ? this.notification.getObject() : null;
    }

    private void setNotification(PhdNotification notification) {
	this.notification = (notification != null) ? new DomainReference<PhdNotification>(notification) : null;
    }

    @Override
    protected void fillReport() {

    }

    @Override
    public String getReportFileName() {
	return "Notification-" + getNotification().getNotificationNumber().replace("/", "-") + "-"
		+ new DateTime().toString(YYYYMMDDHHMMSS);
    }

    @Override
    public String getReportTemplateKey() {
	return getClass().getName() + "." + getNotification().getType().name();
    }

}
