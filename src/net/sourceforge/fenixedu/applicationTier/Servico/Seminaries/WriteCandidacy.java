/*
 * Created on 5/Ago/2003, 15:46:40 By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT]
 * utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.IModality;
import net.sourceforge.fenixedu.domain.Seminaries.ISeminary;
import net.sourceforge.fenixedu.domain.Seminaries.ITheme;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudyChoice;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryModality;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryTheme;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at
 *         5/Ago/2003, 15:46:40
 */
public class WriteCandidacy implements IService {

    /**
     * The actor of this class.
     */
    public WriteCandidacy() {
    }

    public void run(InfoCandidacy infoCandidacy) throws BDException {
        try {
            ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
                    .getIPersistentSeminaryCandidacy();
            IPersistentSeminaryModality persistentModality = persistenceSupport
                    .getIPersistentSeminaryModality();
            IPersistentSeminaryCaseStudyChoice persistentChoice = persistenceSupport
                    .getIPersistentSeminaryCaseStudyChoice();

            IPersistentStudent persistentStudent = persistenceSupport.getIPersistentStudent();
            IPersistentSeminary persistentSeminary = persistenceSupport.getIPersistentSeminary();
            IPersistentCurricularCourse persistentCurricularCourse = persistenceSupport
                    .getIPersistentCurricularCourse();
            IPersistentSeminaryTheme persistentTheme = persistenceSupport.getIPersistentSeminaryTheme();

            ICandidacy candidacy = Cloner.copyInfoCandicacy2ICandidacy(infoCandidacy);

            IStudent readStudent = (IStudent) persistentStudent.readByOID(Student.class, candidacy
                    .getStudent().getIdInternal());
            ISeminary readSeminary = (ISeminary) persistentSeminary.readByOID(Seminary.class, candidacy
                    .getSeminary().getIdInternal());
            ICurricularCourse readCurricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, candidacy.getCurricularCourse().getIdInternal());

            IModality modality = persistentModality.readByName("Completa");
            persistentCandidacy.simpleLockWrite(candidacy);
            candidacy.setModality(modality);
            candidacy.setStudent(readStudent);
            candidacy.setSeminary(readSeminary);
            candidacy.setCurricularCourse(readCurricularCourse);

            if (modality.getIdInternal().equals(infoCandidacy.getInfoModality().getIdInternal())) {
                ((Candidacy) candidacy).setThemeIdInternal(null);
                candidacy.setTheme(null);
            } else {
                ITheme readTheme = (ITheme) persistentTheme.readByOID(Theme.class,
                        candidacy.getTheme().getIdInternal());
                candidacy.setTheme(readTheme);
            }
            if (!infoCandidacy.getInfoSeminary().getHasThemes().booleanValue())
            {
                ((Candidacy) candidacy).setThemeIdInternal(null);
                candidacy.setTheme(null);                
            }
            for (Iterator iter = candidacy.getCaseStudyChoices().iterator(); iter.hasNext();) {
                ICaseStudyChoice element = (ICaseStudyChoice) iter.next();
                persistentChoice.simpleLockWrite(element);
                element.setCandidacy(candidacy);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException("Got an error while trying to write a candidacy to the database", ex);
        }
    }
}