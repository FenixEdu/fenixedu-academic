/*
 * Created on 9/Mai/2003 by jpvl
 *
 */
package Util;


/**
 * This class tells in which step of the enrolment this precedence is to be applied.
  * 	TO_APPLY_DURING_ENROLMENT  
 * @author jpvl
 */
public class PrecedenceScopeToApply extends FenixUtil{
	public static final int TO_APPLY_TO_SPAN_INT = 1;
	public static final int TO_APLLY_DURING_ENROLMENT_INT = 2;
	public static final int TO_APLLY_TO_OPTION_LIST_INT = 3;
	
	public static final String TO_APPLY_TO_SPAN_STR = "SP";
	public static final String TO_APLLY_DURING_ENROLMENT_STR = "ENR";
	public static final String TO_APLLY_TO_OPTION_LIST_STR = "OP";
	
	/**
	 * Precedence of this type is to be applied in calculation of initial span of curricular courses
	 */
	public static final PrecedenceScopeToApply TO_APPLY_TO_SPAN = new PrecedenceScopeToApply(PrecedenceScopeToApply.TO_APPLY_TO_SPAN_INT);

	/**
	 * Precedence of this type is means that precedence is to be applied during student enrolment
	 */
	public static final PrecedenceScopeToApply TO_APPLY_DURING_ENROLMENT = new PrecedenceScopeToApply(PrecedenceScopeToApply.TO_APLLY_DURING_ENROLMENT_INT);

	public static final PrecedenceScopeToApply TO_APLLY_TO_OPTION_LIST = new PrecedenceScopeToApply(PrecedenceScopeToApply.TO_APLLY_TO_OPTION_LIST_INT);
	
	private int scope;
	
	
	private PrecedenceScopeToApply(int scope){
		this.scope = scope;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof PrecedenceScopeToApply){
			PrecedenceScopeToApply scopeObj = (PrecedenceScopeToApply) obj;
			return this.scope == scopeObj.getScope();
		}
		return false;
	}

	/**
	 * @return
	 */
	public int getScope() {
		return this.scope;
	}

}
