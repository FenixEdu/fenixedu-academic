/*
 * Created on 5/Ago/2003, 15:46:40 By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT]
 * utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IStudent;
import Dominio.Student;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import Dominio.Seminaries.ICaseStudyChoice;
import Dominio.Seminaries.IModality;
import Dominio.Seminaries.ISeminary;
import Dominio.Seminaries.ITheme;
import Dominio.Seminaries.Seminary;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminary;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudyChoice;
import ServidorPersistente.Seminaries.IPersistentSeminaryModality;
import ServidorPersistente.Seminaries.IPersistentSeminaryTheme;

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
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
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
                ITheme readTheme = (ITheme) persistentTheme.readByOID(Dominio.Seminaries.Theme.class,
                        candidacy.getTheme().getIdInternal());
                candidacy.setTheme(readTheme);
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