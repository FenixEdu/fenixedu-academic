package pt.ist.fenix.cgd;

import org.fenixedu.bennu.core.domain.User;

public class CgdCard extends CgdCard_Base {
    
    public CgdCard(final CgdCardCounter counter, final User user, final int count) {
        setCgdCardCounter(counter);
        setUser(user);
        setSerialNumber(count);
    }

    public String getSerialNumberForCard() {
        return Integer.toString(getCgdCardCounter().getYear() % 100) + fillLeftString(Integer.toString(getSerialNumber()), '0', 6);
    }

    protected static String fillLeftString(final String string, final char c, final int fillTo) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = string.length(); i < fillTo; i++) {
            stringBuilder.append(c);
        }
        stringBuilder.append(string);
        return stringBuilder.toString();
    }

}
