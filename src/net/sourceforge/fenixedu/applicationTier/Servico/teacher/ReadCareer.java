/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCareer extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return Career.class;
    }

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        InfoCareer infoCarrerWithInfoTeacher = InfoCareer.newInfoFromDomain((Career) domainObject); 
    	infoCarrerWithInfoTeacher.setInfoTeacher(InfoTeacherWithPerson.newInfoFromDomain(((Career) domainObject).getTeacher()));
        return infoCarrerWithInfoTeacher;
    }

}
