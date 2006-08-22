/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.teacher.Career;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCareer extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        InfoCareer infoCarrerWithInfoTeacher = InfoCareer.newInfoFromDomain((Career) domainObject); 
    	infoCarrerWithInfoTeacher.setInfoTeacher(InfoTeacher.newInfoFromDomain(((Career) domainObject).getTeacher()));
        return infoCarrerWithInfoTeacher;
    }

	@Override
	protected DomainObject readDomainObject(final Integer idInternal) {
		return rootDomainObject.readCareerByOID(idInternal);
	}

}
