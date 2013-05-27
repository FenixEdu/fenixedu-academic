package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAllMasterDegrees {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List run(DegreeType degreeType) throws FenixServiceException {
        List<Degree> result = Degree.readAllByDegreeType(degreeType);

        if (result == null || result.size() == 0) {
            throw new NonExistingServiceException();
        }

        List degrees = new ArrayList();
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            degrees.add(InfoDegree.newInfoFromDomain((Degree) iterator.next()));
        }
        return degrees;

    }
}