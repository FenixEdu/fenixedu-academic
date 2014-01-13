package pt.ist.fenix.user.management;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class IstUsernameCounter extends IstUsernameCounter_Base {

    public IstUsernameCounter() {
        super();
        setBennu(Bennu.getInstance());
        setLastValue(4_00_000);
    }

    public long getNext() {
        long next = getLastValue() + 1;
        setLastValue(next);
        return next;
    }

    @Atomic
    public static IstUsernameCounter ensureSingleton() {
        if (Bennu.getInstance().getIstUsernameCounter() == null) {
            new IstUsernameCounter();
        }
        return Bennu.getInstance().getIstUsernameCounter();
    }

}
