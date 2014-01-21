package pt.ist.fenix.dspace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.util.ConnectionManager;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.io.domain.GenericFile;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.bennu.scheduler.domain.SchedulerSystem;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

@Task(englishTitle = "Imports files from DSpace", readOnly = true)
public class ImportFilesFromDSpace extends CronTask {

    private final File errorsFile = new File(SchedulerSystem.getLogsPath() + "/dspaceErrors.json");

    @Override
    public void runTask() throws Exception {
        JsonArray ignores = loadIgnores();
        Method method = getMigrateMethod();
        taskLog("Begin processing. Ignored files: %s\n", ignores);
        Set<String> files = loadOIDs(ignores);
        taskLog("Processing %s files: %s\n", files.size(), files);
        for (String id : files) {
            GenericFile file = FenixFramework.getDomainObject(id);
            if (!migrateIt(method, file)) {
                ignores.add(new JsonPrimitive(id));
            }
        }
        saveIgnores(ignores);
    }

    private void saveIgnores(JsonArray ignores) throws IOException {
        if (ignores.size() > 0) {
            Files.write(ignores.toString(), errorsFile, Charset.defaultCharset());
        }
    }

    private JsonArray loadIgnores() throws FileNotFoundException {
        if (errorsFile.exists()) {
            return new JsonParser().parse(new FileReader(errorsFile)).getAsJsonArray();
        }
        return new JsonArray();
    }

    private boolean migrateIt(Method method, GenericFile file) {
        try {
            method.invoke(file);
            return true;
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            taskLog("Could not migrate %s due to an exception: %s\n", file.getExternalId(), e.getCause());
            return false;
        }
    }

    private Method getMigrateMethod() throws NoSuchMethodException {
        Method method = GenericFile.class.getDeclaredMethod("updateFileStorage", new Class[] {});
        method.setAccessible(true);
        return method;
    }

    private Set<String> loadOIDs(JsonArray ignores) throws SQLException {
        String notIn = Joiner.on(',').join(ignores);
        String notInStatement = ignores.size() == 0 ? "" : "AND OID NOT IN (" + notIn + ")";
        Connection connection = ConnectionManager.getCurrentSQLConnection();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs =
                    stmt.executeQuery("SELECT OID FROM GENERIC_FILE where OID_STORAGE = "
                            + Bennu.getInstance().getDSpaceFileStorage().getExternalId() + " " + notInStatement + " limit 100");
            Set<String> oids = new HashSet<>();
            while (rs.next()) {
                oids.add(rs.getString(1));
            }
            rs.close();
            return oids;
        }
    }
}
