/*
 * Created on 18/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecutedException;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.RandomStringGenerator;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GenerateUsername {

    public static String getUsername() throws NotExecutedException {

        ISuportePersistente sp = null;

        // Generate Username
        String username = RandomStringGenerator.getRandomStringGenerator(6);

        // Verify if the Username already Exists
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            while ((sp.getIPessoaPersistente().lerPessoaPorUsername(username)) != null)
                username = RandomStringGenerator.getRandomStringGenerator(6);
        } catch (ExcepcaoPersistencia ex) {
            NotExecutedException newEx = new NotExecutedException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        return username;
    }

    public static String getCandidateUsername(IMasterDegreeCandidate newMasterDegreeCandidate)
            throws NotExecutedException {
        // FIXME : temporary solution: in the future create username by name
        ISuportePersistente sp = null;

        int start = 0;
        int end = 3;
        char buf[] = new char[end - start];
        newMasterDegreeCandidate.getSpecialization().toString().getChars(start, end, buf, 0);

        // Generate Username
        String username = newMasterDegreeCandidate.getCandidateNumber()
                + String.valueOf(buf)
                + newMasterDegreeCandidate.getExecutionDegree().getCurricularPlan().getDegree()
                        .getSigla();

        // Verify if the Username already Exists
        String username2Test = username;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            int i = 1;
            while ((sp.getIPessoaPersistente().lerPessoaPorUsername(username2Test)) != null) {
                //while ((person =
                // sp.getIPessoaPersistente().lerPessoaPorUsername(username)) !=
                // null){
                username2Test = new String(username + i++);
            }

        } catch (ExcepcaoPersistencia ex) {
            NotExecutedException newEx = new NotExecutedException("Persistence layer error");
            throw newEx;
        }
        return username2Test;
    }

    public static void main(String args[]) {
        String s = "abcdefghij";
        int start = 3;
        int end = 5;
        char buf[] = new char[end - start];

        s.getChars(start, end, buf, 0);
    }

}