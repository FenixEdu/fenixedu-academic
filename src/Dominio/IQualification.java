
package Dominio;

import java.util.Date;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IQualification extends IDomainObject {

	public void setMark(String mark);
	public void setSchool(String school);
	public void setTitle(String title);
    public void setDegree(String degree);
	public void setPerson(IPessoa person);
    public void setLastModificationDate(Date lastModificationDate);
    public void setBranch(String Branch);
    public void setDegreeRecognition(String degreeRecognition);    
    public void setEquivalenceDate(Date equivalenceDate);    
    public void setEquivalenceSchool(String equivalenceSchool);
    public void setSpecializationArea(String specializationArea);
    public void setDate(Date date);
    public void setCountry(ICountry country);
    
	public String getMark();
	public String getSchool();
	public String getTitle();
    public String getDegree();
	public IPessoa getPerson();
	public Date getLastModificationDate();
    public String getBranch();    
    public String getDegreeRecognition();
    public Date getEquivalenceDate();
    public String getEquivalenceSchool();
    public String getSpecializationArea();
    public Date getDate();
    public ICountry getCountry();
}




