/*
 * ISite.java
 * Mar 10, 2003
 */
package Dominio;


/**
 * @author Ivo Brandão
 */
public interface ISite extends IDomainObject {

	public IExecutionCourse getExecutionCourse();
	public void setExecutionCourse(IExecutionCourse executionCourse);
	public String getAlternativeSite();
	public void setAlternativeSite(String alternativeSite);
	public String getMail();
	public void setMail(String mail);
	public String getInitialStatement(); 
	public String getIntroduction();
	public void setInitialStatement(String initialStatement);
	public void setIntroduction(String introduction);
	public String getStyle();
	public void setStyle(String style);
	
}
