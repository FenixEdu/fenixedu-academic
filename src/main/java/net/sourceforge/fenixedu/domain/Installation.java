package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.Installation_Base;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class Installation extends Installation_Base {

    public Installation() {
        setBennu(Bennu.getInstance());
    }

    public String getInstituitionalEmailAddress(String prefix) {
        return String.format("%s@%s", prefix, getInstituitionEmailDomain());
    }

    public static Installation getInstance() {
        return Bennu.getInstance().getInstallation();
    }

    @Atomic
    public static void ensureInstallation() {
        if (Bennu.getInstance().getInstallation() == null) {
            new Installation();
        }
    }

}
