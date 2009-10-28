package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.MasterDegreeAdministrativeOfficeGroup;
import net.sourceforge.fenixedu.domain.phd.InternalGuiding;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuiding;
import net.sourceforge.fenixedu.domain.util.email.Message;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AlertService {

    static public void alertStudent(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {
	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
	alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
	alertBean.setBody(getMessageFromResource(bodyKey));
	alertBean.setFireDate(new LocalDate());
	alertBean.setTargetGroup(new FixedSetGroup(process.getPerson()));

	new PhdCustomAlert(alertBean);

    }

    private static String getSubjectPrefixed(PhdIndividualProgramProcess process, String subjectKey) {
	return getProcessNumberPrefix(process) + getMessageFromResource(subjectKey);
    }

    private static String getProcessNumberPrefix(PhdIndividualProgramProcess process) {
	return "[" + getMessageFromResource("label.phds") + " - " + process.getProcessNumber() + "] ";
    }

    static private String getMessageFromResource(String key) {
	return ResourceBundle.getBundle("resources.PhdResources", Language.getLocale()).getString(key);

    }

    static public void alertGuiders(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {

	final Set<Person> toNotify = new HashSet<Person>();

	for (final PhdProgramGuiding guiding : process.getGuidingsAndAssistantGuidings()) {
	    if (guiding.isInternal()) {
		toNotify.add(((InternalGuiding) guiding).getPerson());
	    } else {
		new Message(RootDomainObject.getInstance().getSystemSender(), Collections.EMPTY_LIST, Collections.EMPTY_LIST,
			subjectKey, bodyKey, Collections.singleton(guiding.getEmail()));
	    }
	}

	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
	alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
	alertBean.setBody(getMessageFromResource(bodyKey));
	alertBean.setFireDate(new LocalDate());
	alertBean.setTargetGroup(new FixedSetGroup(toNotify));

	new PhdCustomAlert(alertBean);

    }

    static public void alertAcademicOffice(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {
	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, true);
	alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
	alertBean.setBody(getMessageFromResource(bodyKey));
	alertBean.setFireDate(new LocalDate());
	alertBean.setTargetGroup(new MasterDegreeAdministrativeOfficeGroup());

	new PhdCustomAlert(alertBean);
    }

    static public void alertCoordinator(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {
	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
	alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
	alertBean.setBody(getMessageFromResource(bodyKey));
	alertBean.setFireDate(new LocalDate());
	alertBean.setTargetGroup(new FixedSetGroup(process.getPhdProgram().getCoordinatorsFor(
		ExecutionYear.readCurrentExecutionYear())));

	new PhdCustomAlert(alertBean);
    }

}
