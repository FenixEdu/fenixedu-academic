/*
 * Created on 4/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.sop.utils;

import org.apache.commons.collections.Predicate;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class  FenixPredicate implements Predicate {

	/**
	 * 
	 */
	public FenixPredicate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	public abstract boolean evaluate(Object arg0);

}
