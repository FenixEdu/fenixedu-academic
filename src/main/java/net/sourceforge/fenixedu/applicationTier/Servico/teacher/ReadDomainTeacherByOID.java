/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDomainTeacherByOID {

    @Service
    public static Teacher run(Integer teacherID) {
        return RootDomainObject.getInstance().readTeacherByOID(teacherID);
    }
}