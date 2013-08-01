package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Comparator;
import java.util.Random;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.codec.digest.DigestUtils;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.util.ConfigurationManager;

public class GenericApplication extends GenericApplication_Base {

    public static final Comparator<GenericApplication> COMPARATOR_BY_APPLICATION_NUMBER = new Comparator<GenericApplication>() {
        @Override
        public int compare(final GenericApplication o1, final GenericApplication o2) {
            final int n = compareAppNumber(o1.getApplicationNumber(), o2.getApplicationNumber());
            return n == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : n;
        }

        private int compareAppNumber(final String an1, final String an2) {
            int i1 = an1.lastIndexOf('/');
            int i2 = an2.lastIndexOf('/');

            final int c = an1.substring(0, i1).compareTo(an2.substring(0, i2));
            return c == 0 ? Integer.valueOf(an1.substring(i1 + 1)).compareTo(Integer.valueOf(an2.substring(i2 + 1))) : c;
        }

    };

    public GenericApplication(final GenericApplicationPeriod period, final String email) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGenericApplicationPeriod(period);
        setApplicationNumber(period.generateApplicationNumber());
        if (email == null || email.isEmpty()) {
            throw new NullPointerException("error.email.cannot.be.null");
        }
        setEmail(email);
        sendEmailForApplication();
        setIdDocumentType(IDDocumentType.IDENTITY_CARD);
        final Unit institutionUnit = Bennu.getInstance().getInstitutionUnit();
        if (institutionUnit != null) {
            setNationality(institutionUnit.getCountry());
        }
    }

    public void sendEmailForApplication() {
        final String subject =
                BundleUtil.getStringFromResourceBundle("resources.CandidateResources", "label.application.email.subject",
                        getGenericApplicationPeriod().getTitle().getContent());
        final String body =
                BundleUtil
                        .getStringFromResourceBundle("resources.CandidateResources", "label.application.email.body",
                                getApplicationNumber(), generateConfirmationLink(), getGenericApplicationPeriod().getTitle()
                                        .getContent());
        new Message(getRootDomainObject().getSystemSender(), getEmail(), subject, body);
    }

    private String generateConfirmationLink() {
        final String confirmationCode =
                DigestUtils.sha512Hex(getEmail() + System.currentTimeMillis() + hashCode()
                        + new Random(System.currentTimeMillis()).nextGaussian());
        setConfirmationCode(confirmationCode);
        return ConfigurationManager.getProperty("generic.application.email.confirmation.link") + confirmationCode
                + "&applicationExternalId=" + getExternalId();
    }

    public boolean isAllPersonalInformationFilled() {
        return getGender() != null && getDateOfBirthYearMonthDay() != null && getDocumentIdNumber() != null
                && !getDocumentIdNumber().isEmpty() && getIdDocumentType() != null && getNationality() != null
                /* && getFiscalCode() != null && !getFiscalCode().isEmpty() */&& getAddress() != null && !getAddress().isEmpty()
                && getAreaCode() != null && !getAreaCode().isEmpty() && getArea() != null && !getArea().isEmpty()
                && getTelephoneContact() != null && !getTelephoneContact().isEmpty();
    }

    public int getAvailableGenericApplicationRecomentationCount() {
        int result = 0;
        for (final GenericApplicationRecomentation recomentation : getGenericApplicationRecomentationSet()) {
            if (recomentation.getLetterOfRecomentation() != null) {
                result++;
            }
        }
        return result;
    }

    @Deprecated
    public boolean hasNationality() {
        return getNationality() != null;
    }

    @Deprecated
    public boolean hasGenericApplicationPeriod() {
        return getGenericApplicationPeriod() != null;
    }

    @Deprecated
    public boolean hasAnyGenericApplicationComment() {
        return !getGenericApplicationCommentSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyGenericApplicationRecomentation() {
        return !getGenericApplicationRecomentationSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyGenericApplicationFileSet() {
        return !getGenericApplicationFileSet().isEmpty();
    }
}
