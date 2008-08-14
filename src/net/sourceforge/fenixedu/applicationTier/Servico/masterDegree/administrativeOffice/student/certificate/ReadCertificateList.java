package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPrice;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.Price;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCertificateList extends Service {

    public List run(GraduationType graduationType, List<DocumentType> documentTypes) throws FenixServiceException {
	final List<InfoPrice> result = new ArrayList<InfoPrice>();
	for (final Price price : Price.readByGraduationTypeAndDocumentTypes(graduationType, documentTypes)) {
	    result.add(InfoPrice.newInfoFromDoaim(price));
	}
	return result;
    }
}