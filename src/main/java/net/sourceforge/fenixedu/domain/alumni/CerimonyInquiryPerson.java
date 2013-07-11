package net.sourceforge.fenixedu.domain.alumni;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class CerimonyInquiryPerson extends CerimonyInquiryPerson_Base {

    public CerimonyInquiryPerson(final CerimonyInquiry cerimonyInquiry, final Person person) {
        setRootDomainObject(RootDomainObject.getInstance());
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

}
