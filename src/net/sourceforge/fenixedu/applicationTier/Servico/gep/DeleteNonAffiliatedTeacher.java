/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DeleteNonAffiliatedTeacher extends Service {

    public void run(NonAffiliatedTeacher nonAffiliatedTeacher) {
	nonAffiliatedTeacher.delete();
    }

}
