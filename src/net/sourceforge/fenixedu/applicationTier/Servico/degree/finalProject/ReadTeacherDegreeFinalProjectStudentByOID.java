/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
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

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentTeacherDegreeFinalProjectStudent();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return Cloner
                .copyITeacherDegreeFinalProjectStudent2InfoTeacherDegreeFinalProjectStudent((ITeacherDegreeFinalProjectStudent) domainObject);
    }

}
