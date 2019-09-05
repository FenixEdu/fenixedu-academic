package org.fenixedu.academic.domain.accounting.paymentCodes;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeState;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;

import com.google.common.collect.Lists;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class PaymentCodePool extends PaymentCodePool_Base {

    private static final int CHUNK_SIZE = 10000;

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


    private static Predicate<EventPaymentCode> paymentCodeHasOpenEvent = code -> code.getEvent().map(Event::isOpen).orElse(false);
    private static Predicate<EventPaymentCode> paymentCodeIsUsed = EventPaymentCode::isInUsed;

    public void refreshPaymentCodes(LocalDate start) {
        final List<EventPaymentCode> paymentCodesToRefresh =
                getPaymentCodeStream()
                        .filter(paymentCodeHasOpenEvent.or(paymentCodeIsUsed.negate()))
                        .filter(p -> !p.getStartDate().toLocalDate().equals(start))
                        .collect(Collectors.toList());
        
        Lists.partition(paymentCodesToRefresh, CHUNK_SIZE).forEach(codes -> {
            FenixFramework.atomic(() -> {
                codes.forEach(code -> code.edit(start, start.plusMonths(getNumberOfMonths())));
            });
        });
    }

    public void invalidatePaymentCodes(LocalDate today) {
        final List<EventPaymentCode> paymentCodesToCancel =
                getPaymentCodeStream()
                        .filter(EventPaymentCode::isNew)
                        .filter(paymentCodeHasOpenEvent.negate())
                        .filter(pc -> pc.getEndDate().toLocalDate().isBefore(today))
                        .collect(Collectors.toList());

        Lists.partition(paymentCodesToCancel, CHUNK_SIZE).forEach(codes -> {
            FenixFramework.atomic(() -> {
                codes.forEach(code -> code.setState(PaymentCodeState.CANCELLED));
            });
        });
    }

    private Stream<EventPaymentCode> getPaymentCodeStream() {
        return getPaymentCodeSet().stream();
    }

    public EventPaymentCode getAvailablePaymentCode() {
        return getPaymentCodeStream().filter(paymentCodeIsUsed.negate())
                .min(Comparator.comparing(PaymentCode::getWhenCreated))
                .orElseThrow(() -> new DomainException("no.available.payment.codes.in.pool"));
    }

    public void enforceMinSize(LocalDate start) {
        int numberOfAvailableCodes = (int) getPaymentCodeStream().filter(paymentCodeIsUsed.negate()).count();
        if (numberOfAvailableCodes < getMinSize()) {
            int numberOfCodesToBeCreated = getMinSize() - numberOfAvailableCodes;

            int chunks = numberOfCodesToBeCreated / CHUNK_SIZE;
            int remainder = numberOfCodesToBeCreated % CHUNK_SIZE;
            
            IntStream.range(0, chunks).forEach(i -> {
                FenixFramework.atomic(() -> {
                    IntStream.range(0, CHUNK_SIZE).forEach(j -> {
                        createNewCode(null,  start);
                    });
                });
            });
            
            FenixFramework.atomic(() -> {
                IntStream.range(0, remainder).forEach(i -> {
                    createNewCode(null, start);
                });
            });
        }
        if (getMinSize() != (int) getPaymentCodeStream().filter(paymentCodeIsUsed.negate()).count()) {
            throw new DomainException("The number of available payment codes is not correct. Do not proceed!");
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
