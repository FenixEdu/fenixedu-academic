package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IPrice;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCertificateList implements IService {

    public List run(GraduationType graduationType, List types) throws FenixServiceException {

        ISuportePersistente sp = null;
        List certificates = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the certificates

            certificates = sp.getIPersistentPrice().readByGraduationTypeAndDocumentType(graduationType,
                    types);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (certificates == null)
            throw new ExcepcaoInexistente("No Certificates Found !!");

        List result = new ArrayList();
        Iterator iterator = certificates.iterator();

        while (iterator.hasNext()) {
            IPrice price = (IPrice) iterator.next();
            result.add(Cloner.copyIPrice2InfoPrice(price));
        }
        return result;
    }

}