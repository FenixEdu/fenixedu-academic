package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu._development.LogLevel;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;

public class SequenceUtil {

    public static int findMaxID() {
        int maxID = -1;
        try {
            final SuportePersistenteOJB persistenceSupport = SuportePersistenteOJB.getInstance();
            persistenceSupport.iniciarTransaccao();
            final PersistenceBroker persistenceBroker = persistenceSupport.currentBroker();
            maxID = findMaxIDinRepository(persistenceBroker.serviceConnectionManager().getConnection());
            persistenceSupport.commitTransaction();
        } catch (Exception ex) {
            throw new Error("Couldn't determine max id", ex);
        }
        return maxID;
    }

    private static int findMaxIDinRepository(final Connection connection) throws SQLException {
        int maxID = -1;

        final Set<String> processedTables = new HashSet<String>();
        for (final ClassDescriptor classDescriptor : getDescriptorTable().values()) {
            if (classDescriptor.getFullTableName() != null && !processedTables.contains(classDescriptor.getFullTableName())) { 
                processedTables.add(classDescriptor.getFullTableName());
                try {
                    final ResultSet resultSet = retrieveResultSet(connection, classDescriptor);
                    if (resultSet.next()) {
                        final int maxObjectID = resultSet.getInt(1);
                        if (maxObjectID > maxID) {
                            maxID = maxObjectID;
                        }
                    }
                    resultSet.close();
                } catch (Exception ex) {
                    if (LogLevel.WARN) {
                        System.out.println("Skipping: " + classDescriptor.getClassNameOfObject() + " due to exception.");
                    }
                }
            }
        }

        return maxID;
    }

    private static ResultSet retrieveResultSet(final Connection connection, final ClassDescriptor classDescriptor) throws SQLException {
        final FieldDescriptor fieldDescriptor = classDescriptor.getPrimaryKey();
        final String sql = constructSQLQuery(classDescriptor.getFullTableName(), fieldDescriptor.getColumnName());
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    private static String constructSQLQuery(final String tablename, final String columnname) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select max(");
        stringBuilder.append(columnname);
        stringBuilder.append(") from ");
        stringBuilder.append(tablename);
        stringBuilder.append(";");
        return stringBuilder.toString();        
    }

    public static Map<String, ClassDescriptor> getDescriptorTable() {
        final MetadataManager metadataManager = MetadataManager.getInstance();
        final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
        return descriptorRepository.getDescriptorTable();
    }

    public static void main(String[] args) {
        System.out.println("maxID= " + findMaxID());
        System.exit(0);
    }

}
