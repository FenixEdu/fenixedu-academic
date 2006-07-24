/*
 * Created on 21/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Price;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PrepareCreateGuide extends Service {

    public InfoGuide run(String graduationType, InfoExecutionDegree infoExecutionDegree, Integer number,
            String requesterType, Party contributorParty) throws FenixServiceException, ExcepcaoPersistencia {

        MasterDegreeCandidate masterDegreeCandidate = null;
        InfoGuide infoGuide = new InfoGuideWithPersonAndExecutionDegreeAndContributor();

        Integer year = null;
        Calendar calendar = Calendar.getInstance();
        year = new Integer(calendar.get(Calendar.YEAR));

        ExecutionDegree executionDegree = null;

        executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());

        // Check if the Requester is a Candidate
        if (requesterType.equals(GuideRequester.CANDIDATE.name())) {

            masterDegreeCandidate = executionDegree
                    .getMasterDegreeCandidateBySpecializationAndCandidateNumber(Specialization
                            .valueOf(graduationType), number);

            // Check if the Candidate Exists
            if (masterDegreeCandidate == null)
                throw new NonExistingServiceException("O Candidato", null);

            // Get the price for the Candidate Application
            Price price = null;
            // FIXME to be removed when the descriptions in the DB are
            // changed to keys to resource bundles
            String description = getDescription(graduationType);

            price = Price.readByGraduationTypeAndDocumentTypeAndDescription(
                    GraduationType.MASTER_DEGREE, DocumentType.APPLICATION_EMOLUMENT, description);

            if (price == null) {
                throw new FenixServiceException("Unkown Application Price");
            }

            infoGuide.setInfoContributor(InfoContributor.newInfoFromDomain(contributorParty));
            infoGuide.setInfoPerson(InfoPerson.newInfoFromDomain(masterDegreeCandidate.getPerson()));
            infoGuide.setYear(year);
            infoGuide.setTotal(price.getPrice());

            infoGuide.setCreationDate(calendar.getTime());
            infoGuide.setVersion(new Integer(1));
            infoGuide
                    .setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                            .newInfoFromDomain(executionDegree));

            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDescription(price.getDescription());
            infoGuideEntry.setDocumentType(price.getDocumentType());
            infoGuideEntry.setGraduationType(price.getGraduationType());
            infoGuideEntry.setInfoGuide(infoGuide);
            infoGuideEntry.setPrice(price.getPrice());
            infoGuideEntry.setQuantity(new Integer(1));

            List<InfoGuideEntry> infoGuideEntries = new ArrayList<InfoGuideEntry>();
            infoGuideEntries.add(infoGuideEntry);

            infoGuide.setInfoGuideEntries(infoGuideEntries);
            infoGuide.setGuideRequester(GuideRequester.CANDIDATE);
        }

        if (requesterType.equals(GuideRequester.STUDENT.name())) {

            Student student = null;
            student = Student.readStudentByNumberAndDegreeType(number, DegreeType.MASTER_DEGREE);
            if (student == null)
                throw new NonExistingServiceException("O Aluno", null);

            final Integer degreeCurricularPlanID = executionDegree.getDegreeCurricularPlan()
                    .getIdInternal();
            List studentCurricularPlanList = (List) CollectionUtils.select(student
                    .getStudentCurricularPlans(), new Predicate() {

                public boolean evaluate(Object arg0) {
                    StudentCurricularPlan scp = (StudentCurricularPlan) arg0;
                    return scp.getDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanID);
                }
            });

            // check if student curricular plan contains selected execution
            // degree
            if (studentCurricularPlanList.isEmpty()) {
                throw new NonExistingServiceException("O Aluno", null);
            }

            // Check if the Candidate Exists
            if (student == null)
                throw new NonExistingServiceException("O Aluno", null);

            infoGuide.setInfoContributor(InfoContributor.newInfoFromDomain(contributorParty));
            infoGuide.setInfoPerson(InfoPerson.newInfoFromDomain(student.getPerson()));
            infoGuide.setYear(year);

            infoGuide.setCreationDate(calendar.getTime());
            infoGuide.setVersion(new Integer(1));

            infoGuide
                    .setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                            .newInfoFromDomain(executionDegree));

            infoGuide.setInfoGuideEntries(new ArrayList());
            infoGuide.setGuideRequester(GuideRequester.STUDENT);
        }

        return infoGuide;
    }

    private String getDescription(String graduationType) {

        switch (Specialization.valueOf(graduationType)) {
        case MASTER_DEGREE:
            return "Mestrado";
        case INTEGRATED_MASTER_DEGREE:
            return "Integrado";
        case SPECIALIZATION:
            return "Especialização";
        }

        return null;

    }

}