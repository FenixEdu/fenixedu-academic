/*
 * Created on 28/Ago/2003, 7:57:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 28/Ago/2003, 7:57:10
 * 
 */
public class ReadStudentById {

    @Service
    public static Object run(Integer id) throws FenixServiceException {
        InfoStudent infoStudent = null;

        Registration registration = RootDomainObject.getInstance().readRegistrationByOID(id);
        if (registration != null) {
            infoStudent = InfoStudent.newInfoFromDomain(registration);
        }

        return infoStudent;
    }
}