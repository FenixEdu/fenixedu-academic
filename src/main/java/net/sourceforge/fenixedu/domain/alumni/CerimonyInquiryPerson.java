package net.sourceforge.fenixedu.domain.alumni;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;

public class CerimonyInquiryPerson extends CerimonyInquiryPerson_Base {

    public CerimonyInquiryPerson(final CerimonyInquiry cerimonyInquiry, final Person person) {
        setRootDomainObject(Bennu.getInstance());
        setCerimonyInquiry(cerimonyInquiry);
        setPerson(person);
    }

    public void delete() {
        if (!hasCerimonyInquiryAnswer()) {
            setCerimonyInquiry(null);
            setPerson(null);
            setRootDomainObject(null);
            deleteDomainObject();
        }
    }

    public boolean isPendingResponse() {
        return !hasCerimonyInquiryAnswer() && getCerimonyInquiry().isOpen();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasComment() {
        return getComment() != null;
    }

    @Deprecated
    public boolean hasCerimonyInquiry() {
        return getCerimonyInquiry() != null;
    }

    @Deprecated
    public boolean hasCerimonyInquiryAnswer() {
        return getCerimonyInquiryAnswer() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
