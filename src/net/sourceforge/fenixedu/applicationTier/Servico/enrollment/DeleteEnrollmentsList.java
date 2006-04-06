/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.CantDeleteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão
 * 
 */
public class DeleteEnrollmentsList extends Service {

    // some of these arguments may be null. they are only needed for filter
    public void run(InfoStudent infoStudent, DegreeType degreeType, List enrolmentIDList)
            throws FenixServiceException, ExcepcaoPersistencia {
        if (enrolmentIDList != null && enrolmentIDList.size() > 0) {
            ListIterator iterator = enrolmentIDList.listIterator();
            while (iterator.hasNext()) {
                Integer enrolmentID = (Integer) iterator.next();

                final Enrolment enrollment = (Enrolment) rootDomainObject.readCurriculumModuleByOID(enrolmentID);
                if (enrollment != null) {
                    try {
                        enrollment.unEnroll();
                    } catch (DomainException e) {
                        throw new CantDeleteServiceException();
                    }
                }
            }
        }
    }
    
}
