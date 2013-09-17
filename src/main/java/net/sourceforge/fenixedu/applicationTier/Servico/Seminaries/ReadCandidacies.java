/*
 * Created on 1/Set/2003, 14:47:35
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.Seminaries.CandidaciesAccessFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyDetails;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoClassification;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoModality;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 1/Set/2003, 14:47:35
 * 
 */
public class ReadCandidacies {

    protected List run(String modalityID, String seminaryID, String themeID, String case1Id, String case2Id, String case3Id,
            String case4Id, String case5Id, String curricularCourseID, String degreeCurricularPlanID, Boolean approved)
            throws BDException {
        // IDs == -1 => not selected
        // approved == nulll => not selected
        //
        // case[1-5]Id => case study ids in the desired order

        Modality modality = FenixFramework.getDomainObject(modalityID);
        Seminary seminary = FenixFramework.getDomainObject(seminaryID);
        Theme theme = FenixFramework.getDomainObject(themeID);

        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
        CurricularCourse curricularCourse = FenixFramework.getDomainObject(curricularCourseID);

        CaseStudy caseStudy1 = FenixFramework.getDomainObject(case1Id);
        CaseStudy caseStudy2 = FenixFramework.getDomainObject(case2Id);
        CaseStudy caseStudy3 = FenixFramework.getDomainObject(case3Id);
        CaseStudy caseStudy4 = FenixFramework.getDomainObject(case4Id);
        CaseStudy caseStudy5 = FenixFramework.getDomainObject(case5Id);

        List<SeminaryCandidacy> filteredCandidacies = new ArrayList<SeminaryCandidacy>();

        outter: for (SeminaryCandidacy candidacy : SeminaryCandidacy.getAllCandidacies()) {
            if (modality != null && !candidacy.getModality().equals(modality)) {
                continue;
            }

            if (seminary != null && !candidacy.getSeminary().equals(seminary)) {
                continue;
            }

            if (curricularCourse != null && !candidacy.getCurricularCourse().equals(curricularCourse)) {
                continue;
            }

            // TODO: converte Modality into a enumeration
            if (theme != null) {
                if (!candidacy.getTheme().equals(theme) /*&& !(candidacy.getModality().getExternalId().intValue() == 1) */) {
                    continue;
                }
            }

            if (approved != null && !candidacy.getApproved().equals(approved)) {
                continue;
            }

            if (degreeCurricularPlan != null
                    && !degreeCurricularPlan.getCurricularCourses().contains(candidacy.getCurricularCourse())) {
                continue;
            }

            CaseStudy choices[] = { caseStudy1, caseStudy2, caseStudy3, caseStudy4, caseStudy5 };
            for (int i = 0; i < choices.length; i++) {
                CaseStudy caseStudy = choices[i];

                if (caseStudy == null) {
                    continue;
                }

                for (CaseStudyChoice choice : candidacy.getCaseStudyChoices()) {
                    if (choice.getOrder() != null) {
                        if (choice.getOrder() == i && !choice.getCaseStudy().equals(caseStudy)) {
                            continue outter; // the case study in that order
                            // is not what the user
                            // requested
                        }
                    }
                }
            }

            filteredCandidacies.add(candidacy);
        }

        List infoCandidacies = new LinkedList();

        for (SeminaryCandidacy candidacy : filteredCandidacies) {
            Registration registration = candidacy.getStudent();
            StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
            if (studentCurricularPlan != null) {
                Collection enrollments = studentCurricularPlan.getEnrolmentsSet();

                InfoCandidacyDetails candidacyDTO = new InfoCandidacyDetails();
                candidacyDTO.setCurricularCourse(InfoCurricularCourse.newInfoFromDomain(candidacy.getCurricularCourse()));
                candidacyDTO.setExternalId(candidacy.getExternalId());
                candidacyDTO.setInfoClassification(getInfoClassification(enrollments));
                candidacyDTO.setModality(InfoModality.newInfoFromDomain(candidacy.getModality()));
                candidacyDTO.setMotivation(candidacy.getMotivation());
                candidacyDTO.setSeminary(InfoSeminaryWithEquivalencies.newInfoFromDomain(candidacy.getSeminary()));
                candidacyDTO.setStudent(InfoStudent.newInfoFromDomain(registration));
                candidacyDTO.setTheme(InfoTheme.newInfoFromDomain(candidacy.getTheme()));
                List<InfoCaseStudyChoice> infos = new ArrayList<InfoCaseStudyChoice>();
                for (Object element2 : candidacy.getCaseStudyChoices()) {
                    CaseStudyChoice element = (CaseStudyChoice) element2;
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
        }

        return infoCandidacies;
    }

    /**
     * @param enrolments
     * @param infoClassification
     */
    private InfoClassification getInfoClassification(Collection<Enrolment> enrolments) {
        InfoClassification infoClassification = new InfoClassification();
        int auxInt = 0;
        float acc = 0;
        for (final Enrolment enrolment : enrolments) {
            final Grade grade = enrolment.getGrade();
            if (grade.isApproved() && grade.isNumeric()) {
                acc += Float.valueOf(grade.getValue()).floatValue();
                auxInt++;
            }
        }
        if (auxInt != 0) {
            String value = new DecimalFormat("#0.0").format(acc / auxInt);
            infoClassification.setAritmeticClassification(value);
        }
        infoClassification.setCompletedCourses(Integer.valueOf(auxInt).toString());
        return infoClassification;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCandidacies serviceInstance = new ReadCandidacies();

    @Atomic
    public static List runReadCandidacies(String modalityID, String seminaryID, String themeID, String case1Id, String case2Id,
            String case3Id, String case4Id, String case5Id, String curricularCourseID, String degreeCurricularPlanID,
            Boolean approved) throws NotAuthorizedException, BDException {
        CandidaciesAccessFilter.instance.execute();
        return serviceInstance.run(modalityID, seminaryID, themeID, case1Id, case2Id, case3Id, case4Id, case5Id,
                curricularCourseID, degreeCurricularPlanID, approved);
    }

}