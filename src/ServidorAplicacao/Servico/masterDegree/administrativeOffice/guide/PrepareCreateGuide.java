/*
 * Created on 21/Mar/2003
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.util.Cloner;
import Dominio.Contributor;
import Dominio.CursoExecucao;
import Dominio.Guide;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IGuide;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPrice;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingContributorServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DocumentType;
import Util.GraduationType;
import Util.GuideRequester;
import Util.Specialization;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PrepareCreateGuide implements IService {

    public InfoGuide run(String graduationType, InfoExecutionDegree infoExecutionDegree, Integer number,
            String requesterType, Integer contributorNumber, String contributorName,
            String contributorAddress) throws FenixServiceException {

        ISuportePersistente sp = null;
        IContributor contributor = null;
        IMasterDegreeCandidate masterDegreeCandidate = null;
        IGuide guide = new Guide();
        InfoGuide infoGuide = new InfoGuide();

        //	Read the Contributor
        try {
            sp = SuportePersistenteOJB.getInstance();
            contributor = sp.getIPersistentContributor().readByContributorNumber(contributorNumber);

            if ((contributor == null)
                    && ((contributorAddress == null) || (contributorAddress.length() == 0)
                            || (contributorName.length() == 0) || (contributorName == null))) {
                throw new NonExistingContributorServiceException();
            }

            if ((contributor == null) && (contributorAddress != null)
                    && (contributorAddress.length() != 0) && (contributorName.length() != 0)
                    && (contributorName != null)) {

                // Create the Contributor
                contributor = new Contributor();
                sp.getIPersistentContributor().simpleLockWrite(contributor);
                contributor.setContributorNumber(contributorNumber);
                contributor.setContributorAddress(contributorAddress);
                contributor.setContributorName(contributorName);
            }
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }

        Integer year = null;
        Calendar calendar = Calendar.getInstance();
        year = new Integer(calendar.get(Calendar.YEAR));

        ICursoExecucao executionDegree = null;
        try {
            executionDegree = (ICursoExecucao) sp.getIPersistentExecutionDegree().readByOID(
                    CursoExecucao.class, infoExecutionDegree.getIdInternal());
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }

        //	Check if the Requester is a Candidate
        if (requesterType.equals(GuideRequester.CANDIDATE_STRING)) {

            try {
                masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                        .readByNumberAndExecutionDegreeAndSpecialization(number, executionDegree,
                                new Specialization(graduationType));
            } catch (ExcepcaoPersistencia ex) {
                FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
                throw newEx;
            }

            // Check if the Candidate Exists
            if (masterDegreeCandidate == null)
                throw new NonExistingServiceException("O Candidato", null);

            // Get the price for the Candidate Application
            IPrice price = null;
            try {
                price = sp.getIPersistentPrice().readByGraduationTypeAndDocumentTypeAndDescription(
                        GraduationType.MASTER_DEGREE_TYPE, DocumentType.APPLICATION_EMOLUMENT_TYPE,
                        graduationType);
            } catch (ExcepcaoPersistencia ex) {
                FenixServiceException newEx = new FenixServiceException("Persistence layer error");
                newEx.fillInStackTrace();
                throw newEx;
            }

            if (price == null) {
                throw new FenixServiceException("Unkown Application Price");
            }

            guide.setContributor(contributor);
            guide.setPerson(masterDegreeCandidate.getPerson());
            guide.setYear(year);
            guide.setTotal(price.getPrice());

            guide.setCreationDate(calendar.getTime());
            guide.setVersion(new Integer(1));
            guide.setExecutionDegree(executionDegree);

            infoGuide = Cloner.copyIGuide2InfoGuide(guide);

            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDescription(price.getDescription());
            infoGuideEntry.setDocumentType(price.getDocumentType());
            infoGuideEntry.setGraduationType(price.getGraduationType());
            infoGuideEntry.setInfoGuide(infoGuide);
            infoGuideEntry.setPrice(price.getPrice());
            infoGuideEntry.setQuantity(new Integer(1));

            List infoGuideEntries = new ArrayList();
            infoGuideEntries.add(infoGuideEntry);

            infoGuide.setInfoGuideEntries(infoGuideEntries);
            infoGuide.setGuideRequester(GuideRequester.CANDIDATE_TYPE);

        }

        if (requesterType.equals(GuideRequester.STUDENT_STRING)) {
            IStudent student = null;

            try {
                student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                        TipoCurso.MESTRADO_OBJ);
                if (student == null)
                    throw new NonExistingServiceException("O Aluno", null);

                List studentCurricularPlanList = sp.getIStudentCurricularPlanPersistente()
                        .readAllByStudentAndDegreeCurricularPlan(student,
                                executionDegree.getCurricularPlan());

                // check if student curricular plan contains selected execution
                // degree
                if (studentCurricularPlanList.isEmpty()) {
                    throw new NonExistingServiceException("O Aluno", null);
                }
            } catch (ExcepcaoPersistencia ex) {

                FenixServiceException newEx = new FenixServiceException("Persistence layer error");
                newEx.fillInStackTrace();
                throw newEx;
            }

            // Check if the Candidate Exists
            if (student == null)
                throw new NonExistingServiceException("O Aluno", null);

            guide.setContributor(contributor);
            guide.setPerson(student.getPerson());
            guide.setYear(year);

            guide.setCreationDate(calendar.getTime());
            guide.setVersion(new Integer(1));

            guide.setExecutionDegree(executionDegree);

            infoGuide = Cloner.copyIGuide2InfoGuide(guide);

            infoGuide.setInfoGuideEntries(new ArrayList());
            infoGuide.setGuideRequester(GuideRequester.STUDENT_TYPE);
        }

        return infoGuide;
    }

}