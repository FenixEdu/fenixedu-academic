/*
 * Created on 5/Ago/2003, 15:46:40 By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT]
 * utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;


import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at
 *         5/Ago/2003, 15:46:40
 */
public class WriteCandidacy {

    protected void run(InfoCandidacy infoCandidacy) {
        SeminaryCandidacy candidacy = new SeminaryCandidacy();
        candidacy.setApproved(infoCandidacy.getApproved());
        candidacy.setMotivation(infoCandidacy.getMotivation());

        // Modality
        final Modality modality = RootDomainObject.getInstance().readModalityByOID(infoCandidacy.getInfoModality().getIdInternal());
        candidacy.setModality(modality);

        // Registration
        final Registration readStudent = RootDomainObject.getInstance().readRegistrationByOID(infoCandidacy.getInfoStudent().getIdInternal());
        candidacy.setStudent(readStudent);

        // Seminary
        final Seminary readSeminary = RootDomainObject.getInstance().readSeminaryByOID(infoCandidacy.getInfoSeminary().getIdInternal());
        candidacy.setSeminary(readSeminary);

        // Curricular Course
        final CurricularCourse readCurricularCourse =
                (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(infoCandidacy.getCurricularCourse().getIdInternal());
        candidacy.setCurricularCourse(readCurricularCourse);

        // Theme
        if (modality.getIdInternal().equals(infoCandidacy.getInfoModality().getIdInternal())) {
            candidacy.setTheme(null);
        } else {
            final Theme readTheme = RootDomainObject.getInstance().readThemeByOID(infoCandidacy.getTheme().getIdInternal());
            candidacy.setTheme(readTheme);
        }
        if (!infoCandidacy.getInfoSeminary().getHasThemes().booleanValue()) {
            candidacy.setTheme(null);
        }

        // Seminary Case Study Choices
        for (InfoCaseStudyChoice infoCaseStudyChoice : infoCandidacy.getCaseStudyChoices()) {
            final CaseStudyChoice caseStudyChoice = new CaseStudyChoice();

            caseStudyChoice.setOrder(infoCaseStudyChoice.getOrder());

            final CaseStudy caseStudy = RootDomainObject.getInstance().readCaseStudyByOID(infoCaseStudyChoice.getCaseStudy().getIdInternal());
            caseStudyChoice.setCaseStudy(caseStudy);

            caseStudyChoice.setCandidacy(candidacy);
            candidacy.addCaseStudyChoices(caseStudyChoice);
        }
    }

    // Service Invokers migrated from Berserk

    private static final WriteCandidacy serviceInstance = new WriteCandidacy();

    @Service
    public static void runWriteCandidacy(InfoCandidacy infoCandidacy) throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        serviceInstance.run(infoCandidacy);
    }

}