
package Dominio;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IQualification extends IDomainObject {


	public void setYear(Integer year);
	public void setMark(String mark);
	public void setSchool(String school);
	public void setTitle(String title);
    public void setDegree(String degree);
	public void setPerson(IPessoa person);
	
	public Integer getYear();
	public String getMark();
	public String getSchool();
	public String getTitle();
    public String getDegree();
	public IPessoa getPerson();
	
}
