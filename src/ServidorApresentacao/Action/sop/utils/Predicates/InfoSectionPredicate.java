/*
 * Created on 4/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.sop.utils.Predicates;

import DataBeans.gesdis.InfoSection;
import ServidorApresentacao.Action.sop.utils.FenixPredicate;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InfoSectionPredicate extends FenixPredicate {
	InfoSection supSection;

	/**
	 * 
	 */
	public InfoSectionPredicate(InfoSection supSection) {
		this.supSection=supSection;
	}
	
	public boolean evaluate(Object arg0) {
		return (arg0 instanceof InfoSection )&&(((InfoSection)arg0).getSuperiorInfoSection().equals(this.supSection));
			
		}
}
