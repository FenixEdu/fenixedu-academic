package Dominio.student;

import Dominio.DomainObject;

/**
 * @author Nuno Correia & Ricardo Rodrigues
 *
 * 12/07/2004
 */

public class SchoolRegistrationInquiryAnswer extends DomainObject implements ISchoolRegistrationInquiryAnswer {

    private Boolean answer1;
    private Boolean answer2;
    private Boolean answer3;
    private Boolean answer4;
    private Boolean answer5;
    private Integer keyStudent;

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

    public Integer getKeyStudent() {
        return this.keyStudent;
    }

    public void setKeyStudent(Integer keyStudent) {
        this.keyStudent = keyStudent;
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
        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof SchoolRegistrationInquiryAnswer) {
            SchoolRegistrationInquiryAnswer inquiryanswer = (SchoolRegistrationInquiryAnswer) obj;
            result = getKeyStudent().equals(inquiryanswer.getKeyStudent());
        }
        return result;
    }

}