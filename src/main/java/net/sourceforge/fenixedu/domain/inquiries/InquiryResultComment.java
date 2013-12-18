package net.sourceforge.fenixedu.domain.inquiries;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.Person;

public class InquiryResultComment extends InquiryResultComment_Base {

    public InquiryResultComment(InquiryResult questionResult, Person person, ResultPersonCategory personCategory,
            Integer resultOrder) {
        super();
        setGeneralAttributes(person, personCategory, resultOrder);
        setInquiryResult(questionResult);
    }

    public InquiryResultComment(InquiryGlobalComment globalComment, Person person, ResultPersonCategory personCategory,
            Integer resultOrder, String comment) {
        super();
        setGeneralAttributes(person, personCategory, resultOrder);
        setInquiryGlobalComment(globalComment);
        setComment(comment);
    }

    private void setGeneralAttributes(Person person, ResultPersonCategory personCategory, Integer resultOrder) {
        setRootDomainObject(Bennu.getInstance());
        setPerson(person);
        setPersonCategory(personCategory);
        setResultOrder(resultOrder);
    }

    public void delete() {
        setInquiryGlobalComment(null);
        setInquiryResult(null);
        setPerson(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }
    @Deprecated
    public boolean hasInquiryResult() {
        return getInquiryResult() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasResultOrder() {
        return getResultOrder() != null;
    }

    @Deprecated
    public boolean hasAllowedToView() {
        return getAllowedToView() != null;
    }

    @Deprecated
    public boolean hasComment() {
        return getComment() != null;
    }

    @Deprecated
    public boolean hasInquiryGlobalComment() {
        return getInquiryGlobalComment() != null;
    }

    @Deprecated
    public boolean hasPersonCategory() {
        return getPersonCategory() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
