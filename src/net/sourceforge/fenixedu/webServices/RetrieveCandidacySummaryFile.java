package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.candidacy.LogFirstTimeCandidacyTimestamp;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySummaryFile;
import net.sourceforge.fenixedu.domain.candidacy.FirstTimeCandidacyStage;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public class RetrieveCandidacySummaryFile implements IRetrieveCandidacySummaryFile {
    private static final String storedPassword;
    private static final String storedUsername;

    static {
        storedUsername = PropertiesManager.getProperty("webServices.PersonManagement.getPersonInformation.username");
        storedPassword = PropertiesManager.getProperty("webServices.PersonManagement.getPersonInformation.password");
    }

    @Override
    public byte[] getCandidacySummaryFile(String username, String password, String userUID, MessageContext context)
            throws NotAuthorizedException, SummaryFileNotAvailableException {

        checkPermissions(username, password, context);

        final User foundUser = User.readUserByUserUId(userUID);
        final StudentCandidacy candidacy =
                foundUser.getPerson().getStudent().getRegistrations().iterator().next().getStudentCandidacy();
        final CandidacySummaryFile file = candidacy.getSummaryFile();

        if (file == null) {
            throw new SummaryFileNotAvailableException(userUID);
        }

        LogFirstTimeCandidacyTimestamp.logTimestamp(candidacy, FirstTimeCandidacyStage.RETRIEVED_SUMMARY_PDF);
        return file.getContents();
    }

    public class SummaryFileNotAvailableException extends Exception {
        public SummaryFileNotAvailableException(String userUID) {
            super("Summary file of the candidacy of student " + userUID + " (userUID) does not exist");
        }
    }

    private void checkPermissions(String username, String password, MessageContext context) throws NotAuthorizedException {
        // check user/pass
        if (!storedUsername.equals(username) || !storedPassword.equals(password)) {
            throw new NotAuthorizedException();
        }
    }
}