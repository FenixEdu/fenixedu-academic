/*
 * Created on Mar 7, 2004
 *
 */
package DataBeans.finalDegreeWork;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Util.TipoCurso;

/**
 * @author Luis Cruz
 *
 */
public class InfoProposal extends InfoObject {

	private InfoDegreeCurricularPlan degreeCurricularPlan;
	private String title;
	private InfoTeacher orientator;
	private InfoTeacher coorientator;
	private Integer orientatorsCreditsPercentage;
	private Integer coorientatorsCreditsPercentage;
	private String objectives;
	private String description;
	private String requirements;
	private String url;
	private Integer numberOfGroupElements;
	private TipoCurso degreeType;
	private String partA;
	private String partB;
	private String observations;
	private String companyName;
	private String companyLink;
	private String companyContact;

	/* Construtores */
	public InfoProposal() {
		super();
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoProposal) {
			InfoProposal proposal = (InfoProposal) obj;

			result = getTitle().equals(proposal.getTitle())
			&& getInfoDegreeCurricularPlan().equals(proposal.getInfoDegreeCurricularPlan());
		}
		return result;
	}

	public String toString() {
		String result = "[Proposal";
		result += ", idInternal=" + getIdInternal();
		result += ", title=" + getTitle();
		result += ", InfoDegreeCurricularPlan=" + getInfoDegreeCurricularPlan();
		result += "]";
		return result;
	}

	/**
	 * @return Returns the companyContact.
	 */
	public String getCompanyContact()
	{
		return companyContact;
	}

	/**
	 * @param companyContact The companyContact to set.
	 */
	public void setCompanyContact(String companyContact)
	{
		this.companyContact = companyContact;
	}

	/**
	 * @return Returns the companyLink.
	 */
	public String getCompanyLink()
	{
		return companyLink;
	}

	/**
	 * @param companyLink The companyLink to set.
	 */
	public void setCompanyLink(String companyLink)
	{
		this.companyLink = companyLink;
	}

	/**
	 * @return Returns the companyName.
	 */
	public String getCompanyName()
	{
		return companyName;
	}

	/**
	 * @param companyName The companyName to set.
	 */
	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	/**
	 * @return Returns the coorientator.
	 */
	public InfoTeacher getCoorientator()
	{
		return coorientator;
	}

	/**
	 * @param coorientator The coorientator to set.
	 */
	public void setCoorientator(InfoTeacher coorientator)
	{
		this.coorientator = coorientator;
	}

	/**
	 * @return Returns the coorientatorsCreditsPercentage.
	 */
	public Integer getCoorientatorsCreditsPercentage()
	{
		return coorientatorsCreditsPercentage;
	}

	/**
	 * @param coorientatorsCreditsPercentage The coorientatorsCreditsPercentage to set.
	 */
	public void setCoorientatorsCreditsPercentage(Integer coorientatorsCreditsPercentage)
	{
		this.coorientatorsCreditsPercentage = coorientatorsCreditsPercentage;
	}

	/**
	 * @return Returns the degreeCurricularPlan.
	 */
	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan()
	{
		return degreeCurricularPlan;
	}

	/**
	 * @param degreeCurricularPlan The degreeCurricularPlan to set.
	 */
	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan degreeCurricularPlan)
	{
		this.degreeCurricularPlan = degreeCurricularPlan;
	}

	/**
	 * @return Returns the degreeType.
	 */
	public TipoCurso getDegreeType()
	{
		return degreeType;
	}

	/**
	 * @param degreeType The degreeType to set.
	 */
	public void setDegreeType(TipoCurso degreeType)
	{
		this.degreeType = degreeType;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return Returns the numberOfGroupElements.
	 */
	public Integer getNumberOfGroupElements()
	{
		return numberOfGroupElements;
	}

	/**
	 * @param numberOfGroupElements The numberOfGroupElements to set.
	 */
	public void setNumberOfGroupElements(Integer numberOfGroupElements)
	{
		this.numberOfGroupElements = numberOfGroupElements;
	}

	/**
	 * @return Returns the objectives.
	 */
	public String getObjectives()
	{
		return objectives;
	}

	/**
	 * @param objectives The objectives to set.
	 */
	public void setObjectives(String objectives)
	{
		this.objectives = objectives;
	}

	/**
	 * @return Returns the observations.
	 */
	public String getObservations()
	{
		return observations;
	}

	/**
	 * @param observations The observations to set.
	 */
	public void setObservations(String observations)
	{
		this.observations = observations;
	}

	/**
	 * @return Returns the orientator.
	 */
	public InfoTeacher getOrientator()
	{
		return orientator;
	}

	/**
	 * @param orientator The orientator to set.
	 */
	public void setOrientator(InfoTeacher orientator)
	{
		this.orientator = orientator;
	}

	/**
	 * @return Returns the orientatorsCreditsPercentage.
	 */
	public Integer getOrientatorsCreditsPercentage()
	{
		return orientatorsCreditsPercentage;
	}

	/**
	 * @param orientatorsCreditsPercentage The orientatorsCreditsPercentage to set.
	 */
	public void setOrientatorsCreditsPercentage(Integer orientatorsCreditsPercentage)
	{
		this.orientatorsCreditsPercentage = orientatorsCreditsPercentage;
	}

	/**
	 * @return Returns the partA.
	 */
	public String getPartA()
	{
		return partA;
	}

	/**
	 * @param partA The partA to set.
	 */
	public void setPartA(String partA)
	{
		this.partA = partA;
	}

	/**
	 * @return Returns the partB.
	 */
	public String getPartB()
	{
		return partB;
	}

	/**
	 * @param partB The partB to set.
	 */
	public void setPartB(String partB)
	{
		this.partB = partB;
	}

	/**
	 * @return Returns the requirements.
	 */
	public String getRequirements()
	{
		return requirements;
	}

	/**
	 * @param requirements The requirements to set.
	 */
	public void setRequirements(String requirements)
	{
		this.requirements = requirements;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

}