/*
 * Created on Jul 12, 2004
 *
 */
package Dominio.student;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public interface ISchoolRegistrationInquiryAnswer {

    public Boolean getAnswer1();
    public Boolean getAnswer2();
    public Boolean getAnswer3();
    public Boolean getAnswer4();
    public Boolean getAnswer5();
    
    public void setAnswer1(Boolean answer);
    public void setAnswer2(Boolean answer);
    public void setAnswer3(Boolean answer);
    public void setAnswer4(Boolean answer);
    public void setAnswer5(Boolean answer);
    
    public Integer getKeyStudent();
    public void setKeyStudent(Integer keyStudent);
    
}
