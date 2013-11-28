package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

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
        return StringUtils.isEmpty(super.getInstituitionURL()) ? BundleUtil.getStringFromResourceBundle(
                "resources/GlobalResources", "institution.url") : super.getInstituitionURL();
    }

    @Override
    public String getInstituitionEmailDomain() {
        return StringUtils.isEmpty(super.getInstituitionEmailDomain()) ? BundleUtil.getStringFromResourceBundle(
                "resources/GlobalResources", "institution.email.domain") : super.getInstituitionEmailDomain();
    }

    @Override
    public String getInstalationName() {
        return StringUtils.isEmpty(super.getInstalationName()) ? BundleUtil.getStringFromResourceBundle(
                "resources/GlobalResources", "application.name") : super.getInstalationName();
    }

    public String getNmciUrl() {
        return "http://ncmi.ist.utl.pt/";
    }

    public String getAcademicDirectionEmailAddress() {
        return getInstituitionalEmailAddress("da");
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
