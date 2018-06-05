package pt.ist.fenix.webapp;
	

import com.google.common.base.Joiner;
import org.fenixedu.academic.util.ConnectionManager;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.messaging.core.domain.MessagingSystem;
import pt.ist.fenixframework.Atomic;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SetMessageOptInGroupTask extends CustomTask {
    @Override public Atomic.TxMode getTxMode() {
        return Atomic.TxMode.WRITE;
    }


    @Override public void runTask() throws Exception {
        // Set default OptOutAvailableGroup
        MessagingSystem.getInstance().setOptOutAvailable(Group.parse("! (activeResearchers | activeGrantOwner | activeEmployees | activeStudents | activeTeachers)"));


        List<String> users = new ArrayList<>();
        Connection currentSQLConnection = ConnectionManager.getCurrentSQLConnection();
        Statement statement = currentSQLConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select U.USERNAME from PARTY P INNER JOIN USER U ON P.OID_USER = U.OID WHERE P.DISABLE_SEND_EMAILS = 1;");
        while (resultSet.next()){
            users.add(resultSet.getString(1));
        }


        // Migrate already opted out users from previous version
        Group parse = Group.parse("U(" + Joiner.on(",").join(users) + ")");
        MessagingSystem.getInstance().setOptedOutGroup(MessagingSystem.getInstance().getOptedOutGroup().or(parse));
    }
}