/*
 * Created on Jul 12, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IStudent;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public interface IPersonalDataUseInquiryAnswers extends IDomainObject {

    public Boolean getAnswer1();

    public Boolean getAnswer2();

    public Boolean getAnswer3();

    public Boolean getAnswer4();

    public Boolean getAnswer5();

    public Boolean getAnswer6();

    public Boolean getAnswer7();

    public void setAnswer(Integer id, Boolean answer);

    public void setStudent(IStudent student);

    public IStudent getStudent();

}