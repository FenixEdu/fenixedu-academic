package net.sourceforge.fenixedu.domain.alumni;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.Atomic;

public class CerimonyInquiryAnswer extends CerimonyInquiryAnswer_Base implements Comparable<CerimonyInquiryAnswer> {

    @Override
    public int compareTo(final CerimonyInquiryAnswer cerimonyInquiryAnswer) {
        final int c = getOrder().compareTo(cerimonyInquiryAnswer.getOrder());
        return c == 0 ? getExternalId().compareTo(cerimonyInquiryAnswer.getExternalId()) : c;
    }

    public CerimonyInquiryAnswer(final CerimonyInquiry cerimonyInquiry) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCerimonyInquiry(cerimonyInquiry);
    }

    @Override
    public void setCerimonyInquiry(final CerimonyInquiry cerimonyInquiry) {
        super.setCerimonyInquiry(cerimonyInquiry);
        if (cerimonyInquiry == null) {
            setOrder(Integer.valueOf(0));
        } else {
            setOrder(Integer.valueOf(cerimonyInquiry.getCerimonyInquiryAnswerCount()));
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

}
