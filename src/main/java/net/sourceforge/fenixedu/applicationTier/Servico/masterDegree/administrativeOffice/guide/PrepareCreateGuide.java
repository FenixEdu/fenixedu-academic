/*
 * Created on 21/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Price;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PrepareCreateGuide {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoGuide run(String graduationType, InfoExecutionDegree infoExecutionDegree, Integer number,
            String requesterType, Party contributorParty) throws FenixServiceException {

        MasterDegreeCandidate masterDegreeCandidate = null;
        InfoGuide infoGuide = new InfoGuideWithPersonAndExecutionDegreeAndContributor();

        Integer year = null;
        Calendar calendar = Calendar.getInstance();
        year = new Integer(calendar.get(Calendar.YEAR));

        ExecutionDegree executionDegree = null;

        executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());

        // Check if the Requester is a Candidate
        if (requesterType.equals(GuideRequester.CANDIDATE.name())) {

            masterDegreeCandidate =
                    executionDegree.getMasterDegreeCandidateBySpecializationAndCandidateNumber(
                            Specialization.valueOf(graduationType), number);

            // Check if the Candidate Exists
            if (masterDegreeCandidate == null) {
                throw new NonExistingServiceException("O Candidato", null);
            }

            // Get the price for the Candidate Application
            Price price = null;
            // FIXME to be removed when the descriptions in the DB are
            // changed to keys to resource bundles
            String description = getDescription(graduationType);

            price =
                    Price.readByGraduationTypeAndDocumentTypeAndDescription(GraduationType.MASTER_DEGREE,
                            DocumentType.APPLICATION_EMOLUMENT, description);

            if (price == null) {
                throw new FenixServiceException("Unkown Application Price");
            }

            infoGuide.setInfoContributor(InfoContributor.newInfoFromDomain(contributorParty));
            infoGuide.setInfoPerson(InfoPerson.newInfoFromDomain(masterDegreeCandidate.getPerson()));
            infoGuide.setYear(year);
            infoGuide.setTotal(price.getPrice());

            infoGuide.setCreationDate(calendar.getTime());
            infoGuide.setVersion(Integer.valueOf(1));
            infoGuide.setInfoExecutionDegree(InfoExecutionDegree.newInfoFromDomain(executionDegree));

            InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.setDescription(price.getDescription());
            infoGuideEntry.setDocumentType(price.getDocumentType());
            infoGuideEntry.setGraduationType(price.getGraduationType());
            infoGuideEntry.setInfoGuide(infoGuide);
            infoGuideEntry.setPrice(price.getPrice());
            infoGuideEntry.setQuantity(Integer.valueOf(1));

            List<InfoGuideEntry> infoGuideEntries = new ArrayList<InfoGuideEntry>();
            infoGuideEntries.add(infoGuideEntry);

            infoGuide.setInfoGuideEntries(infoGuideEntries);
            infoGuide.setGuideRequester(GuideRequester.CANDIDATE);
        }

        if (requesterType.equals(GuideRequester.STUDENT.name())) {

            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            Registration registration = Registration.readByNumberAndDegreeCurricularPlan(number, degreeCurricularPlan);
            if (registration == null) {
                throw new NonExistingServiceException("O Aluno", null);
            }

            final Integer degreeCurricularPlanID = degreeCurricularPlan.getIdInternal();
            List studentCurricularPlanList =
                    (List) CollectionUtils.select(registration.getStudentCurricularPlans(), new Predicate() {

                        @Override
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
            if (registration == null) {
                throw new NonExistingServiceException("O Aluno", null);
            }

            infoGuide.setInfoContributor(InfoContributor.newInfoFromDomain(contributorParty));
            infoGuide.setInfoPerson(InfoPerson.newInfoFromDomain(registration.getPerson()));
            infoGuide.setYear(year);

            infoGuide.setCreationDate(calendar.getTime());
            infoGuide.setVersion(Integer.valueOf(1));

            infoGuide.setInfoExecutionDegree(InfoExecutionDegree.newInfoFromDomain(executionDegree));

            infoGuide.setInfoGuideEntries(new ArrayList());
            infoGuide.setGuideRequester(GuideRequester.STUDENT);
        }

        return infoGuide;
    }

    private static String getDescription(String graduationType) {
        switch (Specialization.valueOf(graduationType)) {
        case STUDENT_CURRICULAR_PLAN_MASTER_DEGREE:
            return "Mestrado";
        case STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE:
            return "Integrado";
        case STUDENT_CURRICULAR_PLAN_SPECIALIZATION:
            return "Especialização";
        }

        return null;
    }

}