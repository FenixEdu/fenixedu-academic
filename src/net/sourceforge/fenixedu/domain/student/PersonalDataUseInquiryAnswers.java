package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Nuno Correia & Ricardo Rodrigues
 * 
 * 12/07/2004
 */

public class PersonalDataUseInquiryAnswers extends PersonalDataUseInquiryAnswers_Base {

    public PersonalDataUseInquiryAnswers() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
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
