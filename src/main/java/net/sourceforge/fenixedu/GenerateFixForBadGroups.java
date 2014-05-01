package net.sourceforge.fenixedu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sourceforge.fenixedu.util.ConnectionManager;

import org.fenixedu.bennu.core.domain.groups.Group;
import org.fenixedu.bennu.core.domain.groups.NobodyGroup;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

import pt.ist.fenixframework.FenixFramework;

public class GenerateFixForBadGroups extends CustomTask {
    @Override
    public void runTask() throws Exception {
        Connection connection = ConnectionManager.getCurrentSQLConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query =
                    "select OID, PERMITTED_GROUP from GENERIC_FILE where concat('', PERMITTED_GROUP * 1) <> PERMITTED_GROUP";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                String oid = rs.getString("OID");
                String expression = rs.getString("PERMITTED_GROUP");
                taskLog("-- %s\n", expression);
                Group group = NobodyGroup.getInstance();
                for (String string : expression.trim().split("\\s*\\|\\|\\s*")) {
                    group = group.or(FenixFramework.<Group> getDomainObject(string));
                }
                taskLog("update GENERIC_FILE set PERMITTED_GROUP = %s where OID = %s\n", group.getExternalId(), oid);
            }
        } finally {
        }
    }
}
