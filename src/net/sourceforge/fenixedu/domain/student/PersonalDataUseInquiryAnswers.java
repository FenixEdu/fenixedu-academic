package net.sourceforge.fenixedu.domain.student;

/**
 * @author Nuno Correia & Ricardo Rodrigues
 * 
 * 12/07/2004
 */

public class PersonalDataUseInquiryAnswers extends PersonalDataUseInquiryAnswers_Base {

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += " keyStudent " + this.getKeyStudent() + "]\n ";
        result += "idInternal " + this.getIdInternal() + ";\n ";
        result += " answer1 " + this.getAnswer1() + "\n ";
        result += " answer2 " + this.getAnswer2() + "\n ";
        result += " answer3 " + this.getAnswer3() + "\n ";
        result += " answer4 " + this.getAnswer4() + "\n ";
        result += " answer5 " + this.getAnswer5() + "\n ";
        result += " answer6 " + this.getAnswer6() + "\n ";
        result += " answer7 " + this.getAnswer7() + "\n ";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IPersonalDataUseInquiryAnswers) {
            final IPersonalDataUseInquiryAnswers personalDataUseInquiryAnswers = (IPersonalDataUseInquiryAnswers) obj;
            return this.getIdInternal().equals(personalDataUseInquiryAnswers.getIdInternal());
        }
        return false;
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

}
