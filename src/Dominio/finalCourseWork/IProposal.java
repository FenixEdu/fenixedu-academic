/*
 * Created on Mar 7, 2004
 *
 */
package Dominio.finalCourseWork;

import Dominio.DegreeCurricularPlan;
import Dominio.IDomainObject;
import Dominio.Teacher;
import Util.TipoCurso;

/**
 * @author Luis Cruz
 *
 */
public interface IProposal extends IDomainObject
{
	public String getCompanyContact();

	public void setCompanyContact(String companyContact);

	public String getCompanyLink();

	public void setCompanyLink(String companyLink);

	public String getCompanyName();

	public void setCompanyName(String companyName);

	public Teacher getCoorientator();

	public void setCoorientator(Teacher coorientator);

	public Integer getCoorientatorsCreditsPercentage();

	public void setCoorientatorsCreditsPercentage(Integer coorientatorsCreditsPercentage);

	public DegreeCurricularPlan getDegreeCurricularPlan();

	public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan);

	public TipoCurso getDegreeType();

	public void setDegreeType(TipoCurso degreeType);

	public String getDescription();

	public void setDescription(String description);

	public Integer getNumberOfGroupElements();

	public void setNumberOfGroupElements(Integer numberOfGroupElements);

	public String getObjectives();

	public void setObjectives(String objectives);

	public String getObservations();

	public void setObservations(String observations);

	public Teacher getOrientator();

	public void setOrientator(Teacher orientator);

	public Integer getOrientatorsCreditsPercentage();

	public void setOrientatorsCreditsPercentage(Integer orientatorsCreditsPercentage);

	public String getPartA();

	public void setPartA(String partA);

	public String getPartB();

	public void setPartB(String partB);

	public String getRequirements();

	public void setRequirements(String requirements);

	public String getTitle();

	public void setTitle(String title);

	public String getUrl();

	public void setUrl(String url);

}
