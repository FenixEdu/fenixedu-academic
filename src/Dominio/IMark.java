package Dominio;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public interface IMark extends IDomainObject{

	public String getMark();
	public String getPublishedMark();
	public IFrequenta getAttend();	
	public IExam getExam();

	public void setMark(String mark);
	public void setPublishedMark(String publishedMark);
	public void setAttend(IFrequenta attend);
	public void setExam(IExam exam);	

}