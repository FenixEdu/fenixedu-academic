package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.applicationTier.Servico.candidacy.LogFirstTimeCandidacyTimestamp;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySummaryFile;
import net.sourceforge.fenixedu.domain.candidacy.FirstTimeCandidacyStage;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;
import org.fenixedu.bennu.core.domain.User;

public class RetrieveCandidacySummaryFile implements IRetrieveCandidacySummaryFile {
    private static final String storedPassword;
    private static final String storedUsername;

    static {
        storedUsername =
                FenixConfigurationManager.getConfiguration().getWebServicesPersonManagementGetPersonInformationUsername();
        storedPassword =
                FenixConfigurationManager.getConfiguration().getWebServicesPersonManagementGetPersonInformationPassword();
    }

    @Override
    public byte[] getCandidacySummaryFile(String username, String password, String userUID, MessageContext context)
            throws NotAuthorizedException, SummaryFileNotAvailableException {

        checkPermissions(username, password, context);

        final User foundUser = User.findByUsername(userUID);
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