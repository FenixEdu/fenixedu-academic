/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 */
public class Precedence extends DomainObject implements IPrecedence {
	private Integer keyCurricularCourse;
	private ICurricularCourse curricularCourse;
	private List restrictions;
	private PrecedenceScopeToApply precedenceScopeToApply;
	/**
	 * 
	 */
	public Precedence() {
		super();
	}

	/**
	 * @return
	 */
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	/**
	 * @return
	 */
	public Integer getKeyCurricularCourse() {
		return keyCurricularCourse;
	}

	/**
	 * @return
	 */
	public List getRestrictions() {
		return restrictions;
	}

	/**
	 * @param course
	 */
	public void setCurricularCourse(ICurricularCourse course) {
		curricularCourse = course;
	}

	/**
	 * @param integer
	 */
	public void setKeyCurricularCourse(Integer keyCurricularCourse) {
		this.keyCurricularCourse = keyCurricularCourse;
	}

	/**
	 * @param list
	 */
	public void setRestrictions(List restrictionList) {
		restrictions = restrictionList;
	}

    /* (non-Javadoc)
	 * @see Dominio.IPrecedence#evaluate(ServidorAplicacao.strategy.enrolment.EnrolmentContext)
	 */
	public boolean evaluate(EnrolmentContext enrolmentContext) {
		List restrictions = getRestrictions();
		boolean evaluate = true;
		// made and of all restrictions
		for (int i = 0; i < restrictions.size() && evaluate; i++){
			IRestriction restriction = (IRestriction) restrictions.get(i);
			evaluate = restriction.evaluate(enrolmentContext);
		}
		return evaluate;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean result = false;
		if ((obj != null) && (this.getClass().equals(obj.getClass()))){
			IPrecedence precedence = (IPrecedence) obj;
			result = this.getCurricularCourse().equals(precedence.getCurricularCourse());
			if (result){
				List precedenceRestrictions = precedence.getRestrictions();
				if (precedenceRestrictions != null){
					for (int i = 0; i < precedenceRestrictions.size() && result; i++){
						IRestriction restriction = (IRestriction) precedenceRestrictions.get(i);
						result = this.getRestrictions().contains(restriction);
					}
				}else{
					result = this.getRestrictions() == null;
				}
				
			}
		}
		return result;
	}

	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Precedence:\n");
		stringBuffer.append(this.getCurricularCourse()).append("\n");
		List restrictions = this.getRestrictions();
		for (int i = 0; i < restrictions.size(); i++) {
			IRestriction restriction = (IRestriction) restrictions.get(i);
			stringBuffer.append(restriction).append("\n");			
		}
		stringBuffer.append("---------\n");
		return stringBuffer.toString();
	}
	/**
	 * @return
	 */
	public PrecedenceScopeToApply getPrecedenceScopeToApply() {
		return precedenceScopeToApply;
	}

	/**
	 * @param apply
	 */
	public void setPrecedenceScopeToApply(PrecedenceScopeToApply apply) {
		precedenceScopeToApply = apply;
	}

}
