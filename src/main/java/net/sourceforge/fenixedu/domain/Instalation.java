package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class Instalation extends Instalation_Base {

    public Instalation() {
        setBennu(Bennu.getInstance());
    }

    public String getInstituitionalEmailAddress(String prefix) {
        return String.format("%s@%s", prefix, getInstituitionEmailDomain());
    }

    public static Instalation getInstance() {
        return Bennu.getInstance().getInstalation();
    }

    @Atomic
    public static void ensureInstalation() {
        if (Bennu.getInstance().getInstalation() == null) {
            new Instalation();
        }
    }
}
