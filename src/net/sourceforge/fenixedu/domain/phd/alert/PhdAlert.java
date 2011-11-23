package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.util.email.UnitBasedSender;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

abstract public class PhdAlert extends PhdAlert_Base {

    protected PhdAlert() {
	super();
    }

    protected void init(PhdIndividualProgramProcess process, MultiLanguageString subject, MultiLanguageString body) {
	check(process, "error.phd.alert.PhdAlert.process.cannot.be.null");
	super.setProcess(process);
	super.init(subject, body);
    }

    @Override
    public void setProcess(PhdIndividualProgramProcess process) {
	throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.process");
    }

    protected String buildMailBody() {
	final StringBuilder result = new StringBuilder();

	for (final String eachContent : getFormattedBody().getAllContents()) {
	    result.append(eachContent).append("\n").append(" ------------------------- ");
	}

	result.delete(result.lastIndexOf("\n") + 1, result.length());

	return result.toString();

    }

    protected String buildMailSubject() {
	final StringBuilder result = new StringBuilder();

	for (final String eachContent : getFormattedSubject().getAllContents()) {
	    result.append(eachContent).append(" / ");
	}

	if (result.toString().endsWith(" / ")) {
	    result.delete(result.length() - 3, result.length());
	}

	return result.toString();
    }

    public boolean isSystemAlert() {
	return false;
    }

    public boolean isCustomAlert() {
	return false;
    }

    protected void disconnect() {
	super.setProcess(null);
	removeRootDomainObjectForActiveAlerts();
	removeRootDomainObject();
    }

    public void delete() {
	disconnect();
	super.deleteDomainObject();
    }

    public boolean isActive() {
	return getActive().booleanValue();
    }

    public boolean hasFireDate() {
	return getFireDate() != null;
    }

    final protected ResourceBundle getResourceBundle() {
	return getResourceBundle(Language.getDefaultLocale());
    }

    final protected ResourceBundle getResourceBundle(final Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale);
    }

    protected UnitBasedSender getSender() {
	AdministrativeOffice administrativeOffice = AdministrativeOffice.readMasterDegreeAdministrativeOffice();
	return administrativeOffice.getUnit().getUnitBasedSenderSet().iterator().next();
    }
}
