package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoGuideEntry;
import DataBeans.util.Cloner;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DocumentType;
import Util.SituationOfGuide;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadGratuityInformationByStudentCurricularPlanID implements
        IServico {

    private static ReadGratuityInformationByStudentCurricularPlanID servico = new ReadGratuityInformationByStudentCurricularPlanID();

    /**
     * The singleton access method of this class.
     */
    public static ReadGratuityInformationByStudentCurricularPlanID getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadGratuityInformationByStudentCurricularPlanID() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "ReadGratuityInformationByStudentCurricularPlanID";
    }

    public List run(Integer studentCurricularPlanID)
            throws FenixServiceException {

        List guides = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) sp
                    .getIStudentCurricularPlanPersistente().readByOID(
                            StudentCurricularPlan.class, studentCurricularPlanID);

            guides = sp.getIPersistentGuide().readByPerson(
                    studentCurricularPlan.getStudent().getPerson()
                            .getNumeroDocumentoIdentificacao(),
                    studentCurricularPlan.getStudent().getPerson()
                            .getTipoDocumentoIdentificacao());

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (guides == null) {
            throw new NonExistingServiceException();
        }

        List result = new ArrayList();
        Iterator guidesIterator = guides.iterator();
        while (guidesIterator.hasNext()) {
            IGuide guide = (IGuide) guidesIterator.next();
            if (guide.getActiveSituation().getSituation().equals(
                    SituationOfGuide.PAYED_TYPE)) {
                Iterator guideEntryIterator = guide.getGuideEntries()
                        .iterator();
                while (guideEntryIterator.hasNext()) {
                    IGuideEntry guideEntry = (IGuideEntry) guideEntryIterator
                            .next();
                    if (guideEntry.getDocumentType().equals(
                            DocumentType.GRATUITY_TYPE)) {
                        InfoGuideEntry infoGuideEntry = Cloner
                                .copyIGuideEntry2InfoGuideEntry(guideEntry);
                        infoGuideEntry.setInfoGuide(Cloner
                                .copyIGuide2InfoGuide(guide));
                        result.add(infoGuideEntry);

                    }
                }
            }
        }

        if (result.size() == 0) {
            throw new NonExistingServiceException();
        }

        return result;
    }
}