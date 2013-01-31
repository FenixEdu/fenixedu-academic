/*
 * Created on 11/Ago/2005 - 19:07:51
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Qualification;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteQualification extends FenixService {

	@Service
	public static void run(Integer qualificationId) {
		Qualification qualification = rootDomainObject.readQualificationByOID(qualificationId);
		qualification.delete();
	}

}