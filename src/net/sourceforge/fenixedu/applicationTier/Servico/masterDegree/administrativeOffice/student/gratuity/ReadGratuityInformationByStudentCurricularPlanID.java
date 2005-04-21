package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.DocumentType;
import net.sourceforge.fenixedu.util.SituationOfGuide;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadGratuityInformationByStudentCurricularPlanID implements IService {

    public List run(Integer studentCurricularPlanID) throws FenixServiceException {

        List guides = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) sp
                    .getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class,
                            studentCurricularPlanID);

            guides = sp.getIPersistentGuide().readByPerson(
                    studentCurricularPlan.getStudent().getPerson().getNumeroDocumentoIdentificacao(),
                    studentCurricularPlan.getStudent().getPerson().getIdDocumentType());

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
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
            if (guide.getActiveSituation().getSituation().equals(SituationOfGuide.PAYED_TYPE)) {
                Iterator guideEntryIterator = guide.getGuideEntries().iterator();
                while (guideEntryIterator.hasNext()) {
                    IGuideEntry guideEntry = (IGuideEntry) guideEntryIterator.next();
                    if (guideEntry.getDocumentType().equals(DocumentType.GRATUITY_TYPE)) {
                        InfoGuideEntry infoGuideEntry = Cloner
                                .copyIGuideEntry2InfoGuideEntry(guideEntry);
                        infoGuideEntry.setInfoGuide(InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide));
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