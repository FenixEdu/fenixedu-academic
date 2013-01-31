/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DeleteNonAffiliatedTeacher extends FenixService {

	public void run(NonAffiliatedTeacher nonAffiliatedTeacher) {
		nonAffiliatedTeacher.delete();
	}

}
