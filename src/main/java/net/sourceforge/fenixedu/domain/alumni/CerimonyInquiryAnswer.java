package net.sourceforge.fenixedu.domain.alumni;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class CerimonyInquiryAnswer extends CerimonyInquiryAnswer_Base implements Comparable<CerimonyInquiryAnswer> {

    @Override
    public int compareTo(final CerimonyInquiryAnswer cerimonyInquiryAnswer) {
        final int c = getOrder().compareTo(cerimonyInquiryAnswer.getOrder());
        return c == 0 ? getExternalId().compareTo(cerimonyInquiryAnswer.getExternalId()) : c;
    }

    public CerimonyInquiryAnswer(final CerimonyInquiry cerimonyInquiry) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCerimonyInquiry(cerimonyInquiry);
    }

    @Override
    public void setCerimonyInquiry(final CerimonyInquiry cerimonyInquiry) {
        super.setCerimonyInquiry(cerimonyInquiry);
        if (cerimonyInquiry == null) {
            setOrder(Integer.valueOf(0));
        } else {
            setOrder(Integer.valueOf(cerimonyInquiry.getCerimonyInquiryAnswerSet().size()));
        }
    }

    public Integer getOrder() {
        return getAnswerOrder();
    }

    public void setOrder(Integer order) {
        setAnswerOrder(order);
    }

    @Atomic
    public void delete() {
        if (!hasAnyCerimonyInquiryPerson()) {
            setCerimonyInquiry(null);
            setRootDomainObject(null);
            deleteDomainObject();
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson> getCerimonyInquiryPerson() {
        return getCerimonyInquiryPersonSet();
    }

    @Deprecated
    public boolean hasAnyCerimonyInquiryPerson() {
        return !getCerimonyInquiryPersonSet().isEmpty();
    }

    @Deprecated
    public boolean hasText() {
        return getText() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCerimonyInquiry() {
        return getCerimonyInquiry() != null;
    }

    @Deprecated
    public boolean hasAnswerOrder() {
        return getAnswerOrder() != null;
    }

}
