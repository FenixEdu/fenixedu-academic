/*
 * Created on 5/Ago/2003, 17:12:20
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

import DataBeans.InfoObject;
import Dominio.Seminaries.ICaseStudyChoice;


/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 5/Ago/2003, 17:12:20
 * 
 */
public class InfoCaseStudyChoice extends /*DataTranferObject*/ InfoObject
{
    private Integer order;
    
    private Integer caseStudyIdInternal;
    private Integer candidacyIdInternal;
	/**
	 * @return
	 */
	public Integer getCandidacyIdInternal()
	{
		return candidacyIdInternal;
	}

	/**
	 * @return
	 */
	public Integer getCaseStudyIdInternal()
	{
		return caseStudyIdInternal;
	}

	/**
	 * @return
	 */
	public Integer getOrder()
	{
		return order;
	}

	/**
	 * @param integer
	 */
	public void setCandidacyIdInternal(Integer integer)
	{
		candidacyIdInternal= integer;
	}

	/**
	 * @param integer
	 */
	public void setCaseStudyIdInternal(Integer integer)
	{
		caseStudyIdInternal= integer;
	}

	/**
	 * @param integer
	 */
	public void setOrder(Integer integer)
	{
		order= integer;
	}

    public void copyFromDomain(ICaseStudyChoice caseStudyChoice) {
        super.copyFromDomain(caseStudyChoice);
        if (caseStudyChoice != null) {
        	setCandidacyIdInternal(caseStudyChoice.getCandidacyIdInternal());
        	setCaseStudyIdInternal(caseStudyChoice.getCaseStudyIdInternal());
        	setOrder(caseStudyChoice.getOrder());
        }
    }

    public static InfoCaseStudyChoice newInfoFromDomain(ICaseStudyChoice caseStudyChoice) {
    	InfoCaseStudyChoice infoCaseStudyChoice = null;
        if (caseStudyChoice != null) {
        	infoCaseStudyChoice = new InfoCaseStudyChoice();
        	infoCaseStudyChoice.copyFromDomain(caseStudyChoice);
        }
        return infoCaseStudyChoice;
    }
}
