package pt.ist.fenix.cgd;

import java.time.Year;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

public class CgdCardCounter extends CgdCardCounter_Base {

    private CgdCardCounter(final int year) {
        setBennu(Bennu.getInstance());
        setYear(year);
        setCount(0);
    }

    @Atomic
    public static String getNextSerialNumber(final User user) {
        final int year = Year.now().getValue();
        final CgdCardCounter counter = getCounterForYear(year);
        return counter.nextSerialNumber(user);
    }

    private String nextSerialNumber(final User user) {
        return user.getCgdCardSet().stream().filter(c -> c.getCgdCardCounter() == this).findAny().orElse(createNewSerialNumber(user)).getSerialNumberForCard();
    }

    private CgdCard createNewSerialNumber(User user) {
        final int count = getCount() + 1;
        setCount(count);
        return new CgdCard(this, user, count);
    }

    private static CgdCardCounter getCounterForYear(final int year) {
        return Bennu.getInstance().getCgdCardCounterSet().stream().filter(c -> c.getYear() == year).findAny()
                .orElse(new CgdCardCounter(year));
    }

}
