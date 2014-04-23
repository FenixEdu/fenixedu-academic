package net.sourceforge.fenixedu.domain.phd.alert;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdCustomAlert extends PhdCustomAlert_Base {

    protected PhdCustomAlert() {
        super();
    }

    public PhdCustomAlert(PhdIndividualProgramProcess process, Group targetGroup, MultiLanguageString subject,
            MultiLanguageString body, Boolean sendMail, LocalDate fireDate, Boolean userDefined, Boolean shared) {
        this();
        init(process, targetGroup, subject, body, sendMail, fireDate, userDefined, shared);
    }

    public PhdCustomAlert(PhdCustomAlertBean bean) {
        this(bean.getProcess(), bean.calculateTargetGroup(), new MultiLanguageString(Language.getDefaultLanguage(),
                bean.getSubject()), new MultiLanguageString(Language.getDefaultLanguage(), bean.getBody()), bean.isToSendEmail(),
                bean.getFireDate(), bean.getUserDefined(), bean.getShared());
    }

    protected void init(PhdIndividualProgramProcess process, Group targetGroup, MultiLanguageString subject,
            MultiLanguageString body, Boolean sendEmail, LocalDate whenToFire, Boolean userDefined, Boolean shared) {

        super.init(process, subject, body);
        String[] args = {};

        if (whenToFire == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert.whenToFire.cannot.be.null",
                    args);
        }
        String[] args1 = {};
        if (targetGroup == null) {
            throw new DomainException("error.phd.alert.PhdAlert.targetGroup.cannot.be.null", args1);
        }
        String[] args2 = {};
        if (sendEmail == null) {
            throw new DomainException("error.phd.alert.PhdAlert.sendEmail.cannot.be.null", args2);
        }
        String[] args3 = {};
        if (userDefined == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert.userDefined.cannot.be.null", args3);
        }
        String[] args4 = {};
        if (shared == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert.shared.cannot.be.null",
                    args4);
        }

        super.setWhenToFire(whenToFire);
        super.setSendEmail(sendEmail);
        super.setTargetGroup(targetGroup);
        super.setUserDefined(userDefined);
        super.setShared(shared);
    }

    @Override
    public boolean isToFire() {
        return !new LocalDate().isBefore(getWhenToFire());
    }

    @Override
    protected boolean isToDiscard() {
        return getFireDate() != null;
    }

    @Override
    public boolean isCustomAlert() {
        return true;
    }

    @Override
    public String getDescription() {
        final ResourceBundle bundle = getResourceBundle();
        return MessageFormat.format(bundle.getString("message.phd.alert.custom.description"), getTargetGroup().getName(),
                getWhenToFire().toString(DateFormatUtil.DEFAULT_DATE_FORMAT), getFormattedSubject(), getFormattedBody());
    }

    @Override
    protected void generateMessage() {

        if (getShared().booleanValue()) {
            new PhdAlertMessage(getProcess(), getTargetGroup().getElements(), getFormattedSubject(), getFormattedBody());
        } else {
            for (final Person person : getTargetGroup().getElements()) {
                new PhdAlertMessage(getProcess(), person, getFormattedSubject(), getFormattedBody());
            }
        }

        if (isToSendMail()) {
            final Recipient recipient = new Recipient(getTargetGroup().getElements());
            new Message(getSender(), recipient, buildMailSubject(), buildMailBody());

        }

    }

    public String getTargetGroupInText() {
        Group targetGroup = getTargetGroup();

        Set<Person> elements = targetGroup.getElements();

        StringBuilder builder = new StringBuilder();

        for (Person person : elements) {
            builder.append(person.getName()).append(" (").append(person.getEmailForSendingEmails()).append(")\n");
        }

        return builder.toString();
    }

    @Override
    public void setTargetGroup(Group targetGroup) {
        throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.targetGroup");
    }

    /**
     * Remove once groups are converted
     * 
     * @param targetGroup
     */
    @Deprecated
    public void setTargetGroupWithoutCheckingPermissions(Group targetGroup) {
        super.setTargetGroup(targetGroup);
    }

    @Override
    public void setSendEmail(Boolean sendEmail) {
        throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.sendEmail");
    }

    @Override
    public void setUserDefined(Boolean userDefined) {
        throw new DomainException("error.phd.alert.PhdCustomAlert.cannot.modify.userDefined");
    }

    @Override
    public void setShared(Boolean shared) {
        throw new DomainException("error.phd.alert.PhdCustomAlert.cannot.modify.shared");
    }

    @Override
    public boolean isToSendMail() {
        return getSendEmail().booleanValue();
    }

    @Override
    public boolean isSystemAlert() {
        return !getUserDefined().booleanValue();
    }

    @Deprecated
    public boolean hasSendEmail() {
        return getSendEmail() != null;
    }

    @Deprecated
    public boolean hasShared() {
        return getShared() != null;
    }

    @Deprecated
    public boolean hasUserDefined() {
        return getUserDefined() != null;
    }

    @Deprecated
    public boolean hasTargetGroup() {
        return getTargetGroup() != null;
    }

    @Deprecated
    public boolean hasWhenToFire() {
        return getWhenToFire() != null;
    }

}
