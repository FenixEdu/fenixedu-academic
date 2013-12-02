package net.sourceforge.fenixedu.domain;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.Atomic;

public class Instalation extends Instalation_Base {

    private static final String DEFAULT_INSTALATION_NAME = ".IST";
    private static final String DEFAULT_INSTITUTION_URL = "http://www.ist.utl.pt/";
    private static final String DEFAULT_INSTITUTION_EMAIL_DOMAIN = "ist.utl.pt";

    public Instalation() {
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public String getInstituitionalEmailAddress(String prefix) {
        return String.format("%s@%s", prefix, getInstituitionEmailDomain());
    }

    @Override
    public String getInstituitionURL() {
        return StringUtils.isEmpty(super.getInstituitionURL()) ? DEFAULT_INSTITUTION_URL : super.getInstituitionURL();
    }

    @Override
    public String getInstituitionEmailDomain() {
        return StringUtils.isEmpty(super.getInstituitionEmailDomain()) ? DEFAULT_INSTITUTION_EMAIL_DOMAIN : super
                .getInstituitionEmailDomain();
    }

    @Override
    public String getInstalationName() {
        return StringUtils.isEmpty(super.getInstalationName()) ? DEFAULT_INSTALATION_NAME : super.getInstalationName();
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
