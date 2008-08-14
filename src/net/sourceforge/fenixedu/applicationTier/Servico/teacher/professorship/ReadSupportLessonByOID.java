/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.SupportLesson;

/**
 * @author jpvl
 */
public class ReadSupportLessonByOID extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
	return InfoSupportLesson.newInfoFromDomain((SupportLesson) domainObject);
    }

    @Override
    protected DomainObject readDomainObject(Integer idInternal) {
	return rootDomainObject.readSupportLessonByOID(idInternal);
    }

}
