package net.sourceforge.fenixedu.domain.accounting;

import java.util.Comparator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGeneratorFactory;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public abstract class PaymentCode extends PaymentCode_Base {

    private static final String ENTITY_CODE = PropertiesManager.getProperty("sibs.entityCode");
    public static final String SIBS_IGNORE_MAX_AMOUNT = "99999999.99";

    public static Comparator<PaymentCode> COMPARATOR_BY_CODE = new Comparator<PaymentCode>() {
        @Override
        public int compare(PaymentCode leftPaymentCode, PaymentCode rightPaymentCode) {
            int comparationResult = leftPaymentCode.getCode().compareTo(rightPaymentCode.getCode());
            if (comparationResult == 0) {
                throw new DomainException("error.accounting.PaymentCode.data.is.corrupted.because.found.duplicate.codes");
            }
            return comparationResult;
        }
    };

    public static Comparator<PaymentCode> COMPARATOR_BY_END_DATE = new Comparator<PaymentCode>() {
        @Override
        public int compare(PaymentCode leftPaymentCode, PaymentCode rightPaymentCode) {
            int comparationResult = leftPaymentCode.getEndDate().compareTo(rightPaymentCode.getEndDate());
            return (comparationResult == 0) ? leftPaymentCode.getExternalId().compareTo(rightPaymentCode.getExternalId()) : comparationResult;
        }
    };

    protected PaymentCode() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setWhenCreated(new DateTime());
        super.setWhenUpdated(new DateTime());
        super.setState(PaymentCodeState.NEW);
        super.setEntityCode(ENTITY_CODE);
    }

    protected void init(final PaymentCodeType paymentCodeType, final YearMonthDay startDate, final YearMonthDay endDate,
            final Money minAmount, final Money maxAmount, final Person person) {

        checkParameters(paymentCodeType, startDate, endDate, minAmount, maxAmount, person);

        super.setCode(getPaymentCodeGenerator(paymentCodeType).generateNewCodeFor(paymentCodeType, person));

        super.setType(paymentCodeType);
        super.setStartDate(startDate);
        super.setEndDate(endDate);
        super.setMinAmount(minAmount);
        super.setMaxAmount(maxAmount != null ? maxAmount : new Money(SIBS_IGNORE_MAX_AMOUNT));
        super.setPerson(person);
    }

    private void checkParameters(PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate, Money minAmount,
            Money maxAmount, final Person person) {

        if (paymentCodeType == null) {
            throw new DomainException("error.accounting.PaymentCode.paymentCodeType.cannot.be.null");
        }

        checkParameters(startDate, endDate, minAmount, maxAmount);
    }

    private void checkParameters(YearMonthDay startDate, YearMonthDay endDate, Money minAmount, Money maxAmount) {
        if (startDate == null) {
            throw new DomainException("error.accounting.PaymentCode.startDate.cannot.be.null");
        }

        if (endDate == null) {
            throw new DomainException("error.accounting.PaymentCode.endDate.cannot.be.null");
        }

        if (minAmount == null) {
            throw new DomainException("error.accounting.PaymentCode.minAmount.cannot.be.null");
        }
    }

    public String getFormattedCode() {
        final StringBuilder result = new StringBuilder();
        int i = 1;
        for (char character : getCode().toCharArray()) {
            result.append(character);
            if (i % 3 == 0) {
                result.append(" ");
            }
            i++;
        }

        return result.charAt(result.length() - 1) == ' ' ? result.deleteCharAt(result.length() - 1).toString() : result
                .toString();
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
        throw new DomainException("error.accounting.PaymentCode.cannot.modify.whenCreated");
    }

    @Override
    public void setCode(String code) {
        throw new DomainException("error.accounting.PaymentCode.cannot.modify.code");
    }

    @Override
    public void setStartDate(YearMonthDay startDate) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.startDate");
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.endDate");
    }

    @Override
    public void setMinAmount(Money minAmount) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.minAmount");
    }

    @Override
    public void setMaxAmount(Money maxAmount) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.maxAmount");
    }

    @Override
    public void setWhenUpdated(DateTime whenUpdated) {
        throw new DomainException("error.accounting.PaymentCode.cannot.modify.whenUpdated");
    }

    @Override
    @Service
    public void setState(PaymentCodeState state) {
        super.setWhenUpdated(new DateTime());
        super.setState(state);
    }

    @Override
    public void setEntityCode(String entityCode) {
        throw new DomainException("error.accounting.PaymentCode.cannot.modify.entityCode");
    }

    public boolean isNew() {
        return getState() == PaymentCodeState.NEW;
    }

    protected void reuseCode() {
        setState(PaymentCodeState.NEW);
    }

    public boolean isProcessed() {
        return getState() == PaymentCodeState.PROCESSED;
    }

    public boolean isCancelled() {
        return getState() == PaymentCodeState.CANCELLED;
    }

    public boolean isInvalid() {
        return getState() == PaymentCodeState.INVALID;
    }

    public void cancel() {
        setState(PaymentCodeState.CANCELLED);
    }

    public boolean isAvailableForReuse() {
        return !isNew();
    }

    public void update(final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount, final Money maxAmount) {
        checkParameters(startDate, endDate, maxAmount, maxAmount);
        super.setStartDate(startDate);
        super.setEndDate(endDate);
        super.setMinAmount(minAmount);
        super.setMaxAmount(maxAmount != null ? maxAmount : new Money(SIBS_IGNORE_MAX_AMOUNT));
        super.setWhenUpdated(new DateTime());
    }

    @Service
    public void process(Person responsiblePerson, Money amount, DateTime whenRegistered, String sibsTransactionId, String comments) {

        if (isProcessed()) {
            return;
        }

        if (isInvalid()) {
            throw new DomainException("error.accounting.PaymentCode.cannot.process.invalid.codes");
        }

        internalProcess(responsiblePerson, amount, whenRegistered, sibsTransactionId, comments);
        if (!getType().isReusable()) {
            setState(PaymentCodeState.PROCESSED);
        }
    }

    public void delete() {
        super.setPerson(null);
        for (PaymentCodeMapping mapping : getOldPaymentCodeMappingsSet()) {
            mapping.delete();
        }
        for (PaymentCodeMapping mapping : getNewPaymentCodeMappingsSet()) {
            removeNewPaymentCodeMappings(mapping);
        }
        removeStudentCandidacy();

        removeRootDomainObject();
        deleteDomainObject();
    }

    public String getDescription() {
        final ResourceBundle enumerationResources =
                ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
        return enumerationResources.getString(getType().getQualifiedName());
    }

    abstract protected void internalProcess(final Person person, final Money amount, final DateTime whenRegistered,
            final String sibsTransactionId, final String comments);

    public PaymentCodeMapping getOldPaymentCodeMapping(final ExecutionYear executionYear) {
        for (final PaymentCodeMapping mapping : getOldPaymentCodeMappingsSet()) {
            if (mapping.has(executionYear)) {
                return mapping;
            }
        }
        return null;
    }

    protected static PaymentCodeGenerator getPaymentCodeGenerator(PaymentCodeType paymentCodeType) {
        return PaymentCodeGeneratorFactory.getGenerator(paymentCodeType);
    }

    static public PaymentCode readByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (final PaymentCode paymentCode : RootDomainObject.getInstance().getPaymentCodesSet()) {
            if (paymentCode.getCode().equals(code)) {
                return paymentCode;
            }
        }
        return null;
    }

    public static boolean canGenerateNewCode(Class<? extends PaymentCode> paymentCodeClass, PaymentCodeType paymentCodeType,
            final Person person) {
        return getPaymentCodeGenerator(paymentCodeType).canGenerateNewCode(paymentCodeType, person);
    }

    public boolean isInstallmentPaymentCode() {
        return false;
    }

    public boolean isAccountingEventPaymentCode() {
        return false;
    }

    public boolean isForRectorate() {
        return false;
    }
}
