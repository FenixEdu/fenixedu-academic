/*
 * Created on Feb 20, 2004
 *  
 */
package ServidorAplicacao.Servico.manager.gratuity;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Guide;
import Dominio.GuideEntry;
import Dominio.GuideSituation;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IGuideSituation;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.gratuity.masterDegree.ISibsPaymentFile;
import Dominio.gratuity.masterDegree.ISibsPaymentFileEntry;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingContributorServiceException;
import ServidorAplicacao.Servico.exceptions.gratuity.masterDegree.DuplicateSibsPaymentFileProcessingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.IPersistentGuideEntry;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gratuity.masterDegree.IPersistentSibsPaymentFile;
import ServidorPersistente.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import Util.DocumentType;
import Util.GraduationType;
import Util.GuideRequester;
import Util.PaymentType;
import Util.SituationOfGuide;
import Util.Specialization;
import Util.State;
import Util.TipoCurso;
import Util.gratuity.SibsPaymentStatus;
import Util.gratuity.SibsPaymentType;
import Util.gratuity.fileParsers.sibs.SibsPaymentFileUtils;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 */
public class ProcessSibsPaymentFile implements IService {

    public ProcessSibsPaymentFile() {

    }

    /**
     *  Process sibs payment files and creates corresponding payment transactions
     * @param filename
     * @param fileEntries
     * @throws FenixServiceException
     */
    public void run(String filename, List fileEntries)
            throws FenixServiceException {

        try {
        	
        	
        	if (filename.trim().length() == 0) {
        		throw new DuplicateSibsPaymentFileProcessingServiceException(
        			"error.exception.duplicateSibsPaymentFileProcessing");
        		
        	}
        	
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            
            ISibsPaymentFile storedPaymentFile = sp.getIPersistentSibsPaymentFile()
                    .readByFilename(filename);

            if (storedPaymentFile != null) {
                throw new DuplicateSibsPaymentFileProcessingServiceException(
                        "error.exception.duplicateSibsPaymentFileProcessing");
            }

            ISibsPaymentFile sibsPaymentFile = SibsPaymentFileUtils
                    .buildPaymentFile(filename, fileEntries);

            buildTransactionsAndStoreFile(sp, sibsPaymentFile);

        } catch (ExcepcaoPersistencia e) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }

    /**
     * NOTE: This method is temporarily creating guides instead of transactions
     * @param sibsPaymentFile
     */
    private void buildTransactionsAndStoreFile(ISuportePersistente sp,
            ISibsPaymentFile sibsPaymentFile) throws ExcepcaoPersistencia,
            NonExistingContributorServiceException {

        IPersistentSibsPaymentFile sibsPaymentFileDAO = sp
                .getIPersistentSibsPaymentFile();
        sibsPaymentFileDAO.simpleLockWrite(sibsPaymentFile);

        List sibsPaymentFileEntries = sibsPaymentFile
                .getSibsPaymentFileEntries();

        int totalPaymentEntries = sibsPaymentFileEntries.size();

        // find duplicates and mark them
        for (int i = 0; i < totalPaymentEntries; i++) {

            ISibsPaymentFileEntry sibsPaymentFileEntry = (ISibsPaymentFileEntry) sibsPaymentFileEntries
                    .get(i);

            if (sibsPaymentFileEntry.getPaymentStatus().equals(
                    SibsPaymentStatus.NOT_PROCESSED_PAYMENT) == true) {

                sibsPaymentFileEntry
                        .setPaymentStatus(SibsPaymentStatus.PROCESSED_PAYMENT);

                // Exception cases should be inserted here
                // e.g. SMS credit payments (i.e. cases where duplicate entry checking is not required)
                // assuming the form:
                //if (isSmsPayment())) {
                // do specific code if any 
                //}
                // else { do specific code to insurance and gratuities }

                markDuplicateGratuityAndInsurancePayments(sp,
                        sibsPaymentFileEntry, sibsPaymentFileEntries,
                        totalPaymentEntries, i);
            }

            // write the file entries
            IPersistentSibsPaymentFileEntry sibsPaymentFileEntryDAO = sp
                    .getIPersistentSibsPaymentFileEntry();

            sibsPaymentFileEntryDAO.simpleLockWrite(sibsPaymentFileEntry);
        }

        Calendar calendar = Calendar.getInstance();
        IPersistentGuide guideDAO = sp.getIPersistentGuide();
        Integer guideYear = new Integer(calendar.get(Calendar.YEAR));
        int nextGuideNumber = guideDAO.generateGuideNumber(guideYear).intValue();
        
        // lets build guides/transactions for the entries in file
        for (int i = 0; i < totalPaymentEntries; i++) {

            ISibsPaymentFileEntry sibsPaymentFileEntry = (ISibsPaymentFileEntry) sibsPaymentFileEntries
                    .get(i);

            if (sibsPaymentFileEntry.getPaymentStatus().equals(
                    SibsPaymentStatus.PROCESSED_PAYMENT) == false) {
                continue;
            }

            // Exception cases should be inserted here
            // e.g. SMS credit payments (i.e. cases where duplicate entry checking is not required)
            // assuming the form:
            //if (isSmsPayment())) {
            // do specific code if any 
            //}
            // else { do specific code to insurance and gratuities }

            //TipoCurso should be changed in future to support Degree Student gratuity
            IStudent student = sp.getIPersistentStudent()
                    .readStudentByNumberAndDegreeType(
                            sibsPaymentFileEntry.getStudentNumber(),
                            TipoCurso.MESTRADO_OBJ);
           
            // to remove in future
            List guideList = sp.getIPersistentGuide().readByPerson(
                    student.getPerson().getNumeroDocumentoIdentificacao(),
                    student.getPerson().getTipoDocumentoIdentificacao());

            if (guideList.size() == 0) {
                throw new NonExistingContributorServiceException();
            }

            IContributor gratuityGuideContributor = ((IGuide) guideList.get(0))
                    .getContributor();

            int year = sibsPaymentFileEntry.getYear().intValue();
            String executionYearName = year + "/" + (year + 1);
            IExecutionYear executionYear = sp.getIPersistentExecutionYear()
                    .readExecutionYearByName(executionYearName);

            if (executionYear == null) {

                //Change status to be solved manually because we could not find execution year
                sibsPaymentFileEntry
                        .setPaymentStatus(SibsPaymentStatus.INVALID_EXECUTION_YEAR);
                continue;
            }

            Specialization specialization = determineSpecialization(sibsPaymentFileEntry);

            //TipoCurso is hardcoded, it should be changed in future to meet Degree gratuity requirements
            List studentCurricularPlanList = sp
                    .getIStudentCurricularPlanPersistente()
                    .readByStudentNumberAndDegreeType(student.getNumber(),
                            TipoCurso.MESTRADO_OBJ);

            ICursoExecucao executionDegree = null;

            for (Iterator iter = studentCurricularPlanList.iterator(); iter
                    .hasNext();) {

                IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iter
                        .next();

                if ((sibsPaymentFileEntry.getPaymentType().equals(
                        SibsPaymentType.INSURANCE) == false)
                        && (studentCurricularPlan.getSpecialization().equals(
                                specialization) == false)) {
                    continue;
                }

                executionDegree = sp
                        .getICursoExecucaoPersistente()
                        .readByDegreeCurricularPlanAndExecutionYear(
                                studentCurricularPlan.getDegreeCurricularPlan(),
                                executionYear);

                if (executionDegree != null) {
                    break;
                }
            }

            if (executionDegree == null) {

                //Change status to be solved manually because we could not find execution degree
                sibsPaymentFileEntry
                        .setPaymentStatus(SibsPaymentStatus.INVALID_EXECUTION_DEGREE);
                continue;
            }

            IGuide guide = new Guide();
            Date currentDate = calendar.getTime();

            guide.setGuideRequester(GuideRequester.STUDENT_TYPE);
            guide.setPaymentType(PaymentType.ATM_TYPE);
            guide.setContributor(gratuityGuideContributor);
            guide.setNumber(new Integer(nextGuideNumber));
            guide.setYear(guideYear);
            guide.setPerson(student.getPerson());
            guide.setVersion(new Integer(1));
            guide.setCreationDate(currentDate);
            guide.setPaymentDate(currentDate);
            guide.setExecutionDegree(executionDegree);

            guideDAO.simpleLockWrite(guide);
            nextGuideNumber++;

            IGuideSituation guideSituation = new GuideSituation(
                    SituationOfGuide.PAYED_TYPE, null, currentDate, guide,
                    new State(State.ACTIVE));

            sp.getIPersistentGuideSituation().simpleLockWrite(guideSituation);

            IPersistentGuideEntry guideEntryDAO = sp.getIPersistentGuideEntry();

            double payedValue = sibsPaymentFileEntry.getPayedValue()
                    .doubleValue();

            IGuideEntry guideEntry = new GuideEntry();

            //GraduationType should be changed in future to support Degree gratuities
            guideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
            guideEntry.setPrice(new Double(payedValue));
            guideEntry.setGuide(guide);
            guideEntry.setQuantity(new Integer(1));

            SibsPaymentType sibsPaymentType = sibsPaymentFileEntry
                    .getPaymentType();

            if (sibsPaymentType.equals(SibsPaymentType.INSURANCE)) {
                guideEntry.setDocumentType(DocumentType.INSURANCE_TYPE);

            } else {
                guideEntry.setDocumentType(DocumentType.GRATUITY_TYPE);
            }

            guideEntryDAO.simpleLockWrite(guideEntry);

            guide.setTotal(new Double(payedValue));

        }

    }

    /**
     * @param sibsPaymentFileEntry
     * @param sibsPaymentFileEntries
     * @param totalPaymentEntries
     * @param currentIndex
     */
    private void markDuplicateGratuityAndInsurancePayments(
            ISuportePersistente sp, ISibsPaymentFileEntry sibsPaymentFileEntry,
            List sibsPaymentFileEntries, int totalPaymentEntries,
            int currentIndex) throws ExcepcaoPersistencia {

        // first check if the gratuity or insurance payment is repeated inside the file
        for (int j = currentIndex + 1; j < totalPaymentEntries; j++) {
            ISibsPaymentFileEntry duplicatePaymentCandidate = (ISibsPaymentFileEntry) sibsPaymentFileEntries
                    .get(j);

            if (sibsPaymentFileEntry.getYear().equals(
                    duplicatePaymentCandidate.getYear())
                    && sibsPaymentFileEntry.getStudentNumber().equals(
                            duplicatePaymentCandidate.getStudentNumber())
                    && sibsPaymentFileEntry.getPaymentType().equals(
                            duplicatePaymentCandidate.getPaymentType())) {

                if (sibsPaymentFileEntry.getPaymentType().equals(
                        SibsPaymentType.INSURANCE)) {

                    duplicatePaymentCandidate
                            .setPaymentStatus(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT);
                } else {
                    duplicatePaymentCandidate
                            .setPaymentStatus(SibsPaymentStatus.DUPLICATE_GRATUITY_PAYMENT);
                }

            }
        }

        // next check if the gratuity or insurance payment is repeated in database
        IPersistentSibsPaymentFileEntry sibsPaymentFileEntryDAO = sp
                .getIPersistentSibsPaymentFileEntry();

        List sibsPaymentFileEntryList = sibsPaymentFileEntryDAO
                .readByYearAndStudentNumberAndPaymentType(sibsPaymentFileEntry
                        .getYear(), sibsPaymentFileEntry.getStudentNumber(),
                        sibsPaymentFileEntry.getPaymentType());

        if (sibsPaymentFileEntryList.size() > 0) {
            if (sibsPaymentFileEntry.getPaymentType().equals(
                    SibsPaymentType.INSURANCE)) {

                sibsPaymentFileEntry
                        .setPaymentStatus(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT);
            } else {

                sibsPaymentFileEntry
                        .setPaymentStatus(SibsPaymentStatus.DUPLICATE_GRATUITY_PAYMENT);
            }

        }
    }

    private Specialization determineSpecialization(
            ISibsPaymentFileEntry sibsPaymentFileEntry) {

        //if sibs payment codes change to much in future
        //this logic should be moved to a config file

        SibsPaymentType sibsPaymentType = sibsPaymentFileEntry.getPaymentType();
        if (sibsPaymentType
                .equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_TOTAL)
                || sibsPaymentType
                        .equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_FIRST_PHASE)
                || sibsPaymentType
                        .equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_SECOND_PHASE)) {
            return Specialization.ESPECIALIZACAO_TYPE;
        } 
            return Specialization.MESTRADO_TYPE;
        

        //degree code goes here

    }

}