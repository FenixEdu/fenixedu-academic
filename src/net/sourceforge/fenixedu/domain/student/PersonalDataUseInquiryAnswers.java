package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IStudent;

/**
 * @author Nuno Correia & Ricardo Rodrigues
 * 
 * 12/07/2004
 */

public class PersonalDataUseInquiryAnswers extends DomainObject implements
        IPersonalDataUseInquiryAnswers {

    private Boolean answer1;

    private Boolean answer2;

    private Boolean answer3;

    private Boolean answer4;

    private Boolean answer5;

    private Boolean answer6;

    private Boolean answer7;

    private Integer keyStudent;

    private IStudent student;

    public Boolean getAnswer1() {
        return this.answer1;
    }

    public void setAnswer1(Boolean answer) {
        this.answer1 = answer;
    }

    public Boolean getAnswer2() {
        return answer2;
    }

    public void setAnswer2(Boolean answer2) {
        this.answer2 = answer2;
    }

    public Boolean getAnswer3() {
        return answer3;
    }

    public void setAnswer3(Boolean answer3) {
        this.answer3 = answer3;
    }

    public Boolean getAnswer4() {
        return answer4;
    }

    public void setAnswer4(Boolean answer4) {
        this.answer4 = answer4;
    }

    public Boolean getAnswer5() {
        return answer5;
    }

    public void setAnswer5(Boolean answer5) {
        this.answer5 = answer5;
    }

    public Boolean getAnswer6() {
        return answer6;
    }

    public void setAnswer6(Boolean answer6) {
        this.answer6 = answer6;
    }

    public Boolean getAnswer7() {
        return answer7;
    }

    public void setAnswer7(Boolean answer7) {
        this.answer7 = answer7;
    }

    public Integer getKeyStudent() {
        return this.keyStudent;
    }

    public void setKeyStudent(Integer keyStudent) {
        this.keyStudent = keyStudent;
    }

    public IStudent getStudent() {
        return student;
    }

    public void setStudent(IStudent student) {
        this.student = student;
    }

    public void setAnswer(Integer id, Boolean answer) {
        switch (id.intValue()) {
        case 1:
            setAnswer1(answer);
            break;
        case 2:
            setAnswer2(answer);
            break;
        case 3:
            setAnswer3(answer);
            break;
        case 4:
            setAnswer4(answer);
            break;
        case 5:
            setAnswer5(answer);
            break;
        case 6:
            setAnswer6(answer);
            break;
        case 7:
            setAnswer7(answer);
            break;
        }
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += " keyStudent " + keyStudent + "]\n ";
        result += "idInternal " + getIdInternal() + ";\n ";
        result += " answer1 " + answer1 + "\n ";
        result += " answer2 " + answer2 + "\n ";
        result += " answer3 " + answer3 + "\n ";
        result += " answer4 " + answer4 + "\n ";
        result += " answer5 " + answer5 + "\n ";
        result += " answer6 " + answer6 + "\n ";
        result += " answer7 " + answer7 + "\n ";
        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof PersonalDataUseInquiryAnswers) {
            PersonalDataUseInquiryAnswers inquiryanswer = (PersonalDataUseInquiryAnswers) obj;
            result = getKeyStudent().equals(inquiryanswer.getKeyStudent());
        }
        return result;
    }

}