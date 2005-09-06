/*
 * Created on 1/Set/2003, 14:47:35
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyDetails;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoClassification;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoModality;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.ICandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudyChoice;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 1/Set/2003, 14:47:35
 *  
 */
public class ReadCandidacies implements IService {

    /**
     * The actor of this class.
     */
    public ReadCandidacies() {
    }

    public List run(Integer modalityID, Integer seminaryID, Integer themeID, Integer case1Id,
            Integer case2Id, Integer case3Id, Integer case4Id, Integer case5Id,
            Integer curricularCourseID, Integer degreeID, Boolean approved) throws BDException {
        List infoCandidacies = new LinkedList();
        try {
            ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
                    .getIPersistentSeminaryCandidacy();
            List candidacies = persistentCandidacy.readByUserInput(modalityID, seminaryID, themeID,
                    case1Id, case2Id, case3Id, case4Id, case5Id, curricularCourseID, degreeID, approved);
            for (Iterator iterator = candidacies.iterator(); iterator.hasNext();) {
                ICandidacy candidacy = (ICandidacy) iterator.next();
                IStudent student = candidacy.getStudent();
                IStudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();
                List enrollments = studentCurricularPlan.getEnrolments();

                InfoCandidacyDetails candidacyDTO = new InfoCandidacyDetails();
                candidacyDTO.setCurricularCourse(InfoCurricularCourseWithInfoDegree
                        .newInfoFromDomain(candidacy.getCurricularCourse()));
                candidacyDTO.setIdInternal(candidacy.getIdInternal());
                candidacyDTO.setInfoClassification(getInfoClassification(enrollments));
                candidacyDTO.setModality(InfoModality.newInfoFromDomain(candidacy.getModality()));
                candidacyDTO.setMotivation(candidacy.getMotivation());
                candidacyDTO.setSeminary(InfoSeminaryWithEquivalencies.newInfoFromDomain(candidacy.getSeminary()));
                candidacyDTO.setStudent(InfoStudentWithInfoPerson.newInfoFromDomain(student));
                candidacyDTO.setTheme(InfoTheme.newInfoFromDomain(candidacy.getTheme()));
                List<InfoCaseStudyChoice> infos = new ArrayList<InfoCaseStudyChoice>();
                Collection c = candidacy.getCaseStudyChoices();
                int count = c.size();
                Iterator i = c.iterator();
                boolean b = i.hasNext();
                for (Iterator iter = candidacy.getCaseStudyChoices().iterator(); iter.hasNext();)
				{
					ICaseStudyChoice element = (ICaseStudyChoice) iter.next();
					infos.add(InfoCaseStudyChoice.newInfoFromDomain(element));
					
				}                 
                candidacyDTO.setCases(infos);
                if (candidacy.getApproved() != null) {
                    candidacyDTO.setApproved(candidacy.getApproved());
                } else {
                    candidacyDTO.setApproved(Boolean.FALSE);
                }
                infoCandidacies.add(candidacyDTO);

            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve mutiple candidacies from the database", ex);
        }
        return infoCandidacies;
    }

    /**
     * @param enrollments
     * @param infoClassification
     */
    private InfoClassification getInfoClassification(List enrollments) {
        InfoClassification infoClassification = new InfoClassification();
        int auxInt = 0;
        float acc = 0;
        float grade = 0;
        for (Iterator iter1 = enrollments.iterator(); iter1.hasNext();) {
            IEnrolment enrollment = (IEnrolment) iter1.next();
            List enrollmentEvaluations = enrollment.getEvaluations();
            IEnrolmentEvaluation enrollmentEvaluation = null;
            if (enrollmentEvaluations != null && !enrollmentEvaluations.isEmpty()) {
                enrollmentEvaluation = (IEnrolmentEvaluation) Collections.max(enrollmentEvaluations);
            }

            String stringGrade;
            if (enrollmentEvaluation != null) {

                stringGrade = enrollmentEvaluation.getGrade();
            } else {
                stringGrade = "NA";
            }

            if (stringGrade != null && !stringGrade.equals("") && !stringGrade.equals("RE") && !stringGrade.equals("NA") && !stringGrade.equals("AP")) {
                Float gradeObject = new Float(stringGrade);
                grade = gradeObject.floatValue();
                acc += grade;
                auxInt++;
            }

        }
        if (auxInt != 0) {
            String value = new DecimalFormat("#0.0").format(acc / auxInt);
            infoClassification.setAritmeticClassification(value);
        }
        infoClassification.setCompletedCourses(new Integer(auxInt).toString());
        return infoClassification;
    }


}