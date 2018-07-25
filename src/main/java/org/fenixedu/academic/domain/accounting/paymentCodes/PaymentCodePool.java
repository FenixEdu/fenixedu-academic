package org.fenixedu.academic.domain.accounting.paymentCodes;

import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class PaymentCodePool extends PaymentCodePool_Base {
    
    protected PaymentCodePool() {
        super();
    }

    public PaymentCodePool(int minSize, int prefix, int numberOfMonths, Money minAmount, Money maxAmount) {
        if (Bennu.getInstance().getPaymentCodePool() != null) {
            throw new DomainException("Already exists");
        }
        setBennu(Bennu.getInstance());
        setMinSize(minSize);
        setPrefix(prefix);
        setMinAmount(minAmount);
        setMaxAmount(maxAmount);
        setNumberOfMonths(numberOfMonths);
    }

    public static PaymentCodePool getInstance() {
        return Bennu.getInstance().getPaymentCodePool();
    }


    private Predicate<EventPaymentCode> paymentCodeHasOpenEvent = code -> code.getEvent().map(Event::isOpen).orElse(false);
    private Predicate<EventPaymentCode> paymentCodeIsUnused = EventPaymentCode::isInUsed;

    public void setup(User responsible, LocalDate start) {
        getPaymentCodeStream().filter(code -> paymentCodeHasOpenEvent.or(paymentCodeIsUnused.negate()).test(code)).forEach(
                code -> code.edit(start, start.plusMonths(getNumberOfMonths())));
        enforceMinSize(responsible, start);
        if (getMinSize() != getPaymentCodeSet().size()) {
            throw new DomainException("Can't proceed");
        }
    }

    private Stream<EventPaymentCode> getPaymentCodeStream() {
        return getPaymentCodeSet().stream();
    }

    private void enforceMinSize(User responsible, LocalDate start) {
        int numberOfCodesInUsed = (int) getPaymentCodeStream().filter(EventPaymentCode::isInUsed).count();
        if (numberOfCodesInUsed < getMinSize()) {
            int numberOfCodesToBeCreated = getMinSize() - numberOfCodesInUsed;
            int CHUNK_SIZE = 10000;

            int chunks = numberOfCodesToBeCreated / CHUNK_SIZE;
            int remainder = numberOfCodesToBeCreated % CHUNK_SIZE;
            
            IntStream.range(0, chunks).forEach(i -> {
                FenixFramework.atomic(() -> {
                    IntStream.range(0, CHUNK_SIZE).forEach(j -> {
                        createNewCode(responsible.getPerson(), start);
                    });
                });
            });
            
            FenixFramework.atomic(() -> {
                IntStream.range(0, remainder).forEach(i -> {
                    createNewCode(responsible.getPerson(), start);
                });
            });
        }
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private EventPaymentCode createNewCode(Person person, LocalDate start) {
        final EventPaymentCode eventPaymentCode =
                new EventPaymentCode(person, start, start.plusMonths(getNumberOfMonths()), getMinAmount(), getMaxAmount());
        addPaymentCode(eventPaymentCode);
        return eventPaymentCode;
    }

    public String generateNewCode() {
        setSequenceCode(getSequenceCode() + 1);
        String baseCode = StringUtils.leftPad(Integer.toString(getSequenceCode()), 6, "0");
        return getPrefix() + baseCode + calculateCheckDigitMod97(baseCode);
    }

    /**
     * https://usersite.datalab.eu/printclass.aspx?type=wiki&id=91772
     * https://stackoverflow.com/questions/277931/algorithm-to-add-two-digits-to-the-end-of-a-number-to-calculate-a-specific-modul
     * @param code
     * @return
     */

    public String calculateCheckDigitMod97(String code) throws RuntimeException {
        // reference code should have length 6
        if (code == null || code.length() != 6) {
            throw new RuntimeException("Invalid Code length=" + (code == null ? 0 : code.length()));
        }
        final int MOD97 = 97;
        int modulusResult = Integer.parseInt(code) * 100 % MOD97;
        int charValue = (MOD97 + 1) - modulusResult;
        String checkDigit = Integer.toString(charValue);
        return (charValue > 9 ? checkDigit : "0" + checkDigit);
    }

    public boolean validateCheckDigitMod97(String code) throws RuntimeException {
        // reference code should have length 8
        if (code == null || code.length() != 8) {
            throw new RuntimeException("Invalid Code length=" + (code == null ? 0 : code.length()));
        }
        int i = Integer.parseInt(code);
        return i % 97 == 1;
    }

}
