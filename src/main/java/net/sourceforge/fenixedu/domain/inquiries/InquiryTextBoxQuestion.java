package net.sourceforge.fenixedu.domain.inquiries;

public class InquiryTextBoxQuestion extends InquiryTextBoxQuestion_Base {

    public InquiryTextBoxQuestion() {
        super();
        setTextArea(false);
    }

    @Deprecated
    public boolean hasTextArea() {
        return getTextArea() != null;
    }

}
