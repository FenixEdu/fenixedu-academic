package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixframework.Atomic;

public class Instalation extends Instalation_Base {

    public Instalation() {
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public String getInstituitionalEmailAddress(String prefix) {
        return String.format("%s@%s", prefix, getInstituitionEmailDomain());
    }

    @Override
    public String getInstituitionURL() {
        return super.getInstituitionURL() != null ? super.getInstituitionURL() : BundleUtil.getStringFromResourceBundle(
                "resources/GlobalResources", "institution.url");
    }

    @Override
    public String getInstituitionEmailDomain() {
        return super.getInstituitionEmailDomain() != null ? super.getInstituitionEmailDomain() : BundleUtil
                .getStringFromResourceBundle("resources/GlobalResources", "institution.email.domain");
    }

    public static Instalation getInstance() {
        return RootDomainObject.getInstance().getInstalation();
    }

    @Atomic
    public static void ensureInstalation() {
        if (RootDomainObject.getInstance().getInstalation() == null) {
            new Instalation();
        }
    }
}
