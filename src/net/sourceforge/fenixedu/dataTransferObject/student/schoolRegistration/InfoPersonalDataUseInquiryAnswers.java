/*
 * Created on Aug 15, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.IPersonalDataUseInquiryAnswers;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InfoPersonalDataUseInquiryAnswers extends InfoObject {

    private Boolean answer1;

    private Boolean answer2;

    private Boolean answer3;

    private Boolean answer4;

    private Boolean answer5;

    private Boolean answer6;

    private Boolean answer7;

    private InfoStudent infoStudent;

    public InfoPersonalDataUseInquiryAnswers() {
    }

    public InfoPersonalDataUseInquiryAnswers(InfoStudent infoStudent, Boolean answer1, Boolean answer2,
            Boolean answer3, Boolean answer4, Boolean answer5, Boolean answer6, Boolean answer7) {
        setInfoStudent(infoStudent);
        setAnswer1(answer1);
        setAnswer2(answer2);
        setAnswer3(answer3);
        setAnswer4(answer4);
        setAnswer5(answer5);
        setAnswer6(answer6);
        setAnswer7(answer7);

    }

    /**
     * @return Returns the answer1.
     */
    public Boolean getAnswer1() {
        return answer1;
    }

    /**
     * @return Returns the answer2.
     */
    public Boolean getAnswer2() {
        return answer2;
    }

    /**
     * @return Returns the answer3.
     */
    public Boolean getAnswer3() {
        return answer3;
    }

    /**
     * @return Returns the answer4.
     */
    public Boolean getAnswer4() {
        return answer4;
    }

    /**
     * @return Returns the answer5.
     */
    public Boolean getAnswer5() {
        return answer5;
    }

    /**
     * @return Returns the answer6.
     */
    public Boolean getAnswer6() {
        return answer6;
    }

    /**
     * @return Returns the answer7.
     */
    public Boolean getAnswer7() {
        return answer7;
    }

    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    /**
     * @param answer1
     *            The answer1 to set.
     */
    public void setAnswer1(Boolean answer1) {
        this.answer1 = answer1;
    }

    /**
     * @param answer2
     *            The answer2 to set.
     */
    public void setAnswer2(Boolean answer2) {
        this.answer2 = answer2;
    }

    /**
     * @param answer3
     *            The answer3 to set.
     */
    public void setAnswer3(Boolean answer3) {
        this.answer3 = answer3;
    }

    /**
     * @param answer4
     *            The answer4 to set.
     */
    public void setAnswer4(Boolean answer4) {
        this.answer4 = answer4;
    }

    /**
     * @param answer5
     *            The answer5 to set.
     */
    public void setAnswer5(Boolean answer5) {
        this.answer5 = answer5;
    }

    /**
     * @param answer6
     *            The answer6 to set.
     */
    public void setAnswer6(Boolean answer6) {
        this.answer6 = answer6;
    }

    /**
     * @param answer7
     *            The answer7 to set.
     */
    public void setAnswer7(Boolean answer7) {
        this.answer7 = answer7;
    }

    /**
     * @param infoStudent
     *            The infoStudent to set.
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoPersonalDataUseInquiryAnswers) {
            InfoPersonalDataUseInquiryAnswers infoPersonalDataUseInquiryAnswers = (InfoPersonalDataUseInquiryAnswers) obj;
            result = this.getInfoStudent().equals(infoPersonalDataUseInquiryAnswers.getInfoStudent());
        }
        return result;
    }

    public void copyFromDomain(IPersonalDataUseInquiryAnswers personalDataUseInquiryAnswers) {
        super.copyFromDomain(personalDataUseInquiryAnswers);

        if (personalDataUseInquiryAnswers != null) {
            setAnswer1(getAnswer1());
            setAnswer2(getAnswer2());
            setAnswer3(getAnswer3());
            setAnswer4(getAnswer4());
            setAnswer5(getAnswer5());
            setAnswer6(getAnswer6());
            setAnswer7(getAnswer7());
        }
    }

    public static InfoPersonalDataUseInquiryAnswers newInfoFromDomain(
            IPersonalDataUseInquiryAnswers personalDataUseInquiryAnswers) {
        InfoPersonalDataUseInquiryAnswers infoPersonalDataUseInquiryAnswers = null;
        if (personalDataUseInquiryAnswers != null) {
            infoPersonalDataUseInquiryAnswers = new InfoPersonalDataUseInquiryAnswers();
            infoPersonalDataUseInquiryAnswers.copyFromDomain(personalDataUseInquiryAnswers);
        }
        return infoPersonalDataUseInquiryAnswers;
    }
}