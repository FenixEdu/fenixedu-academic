/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadTeacherDegreeFinalProjectStudentByOID extends ReadDomainObjectService {
    private static ReadTeacherDegreeFinalProjectStudentByOID service = new ReadTeacherDegreeFinalProjectStudentByOID();

    /**
     * The singleton access method of this class.
     */
    public static ReadTeacherDegreeFinalProjectStudentByOID getService() {
        return service;
    }

    protected Class getDomainObjectClass() {
        return TeacherDegreeFinalProjectStudent.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {
        return persistentSupport.getIPersistentTeacherDegreeFinalProjectStudent();
    }

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson.newInfoFromDomain((TeacherDegreeFinalProjectStudent) domainObject);
    }

}
