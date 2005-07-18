package pt.utl.ist.codeGenerator.database;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;

public class DatabaseDescriptorFactory {

    private DatabaseDescriptorFactory() {
    }

    public static Map<String, SqlTable> getSqlTables() {
        final Map<String, SqlTable> sqlTables = newSqlTableMap();

        final Map<String, ClassDescriptor> classDescriptorMap = getDescriptorTable();
        for (final ClassDescriptor classDescriptor : classDescriptorMap.values()) {
            addSqlTableDescription(sqlTables, classDescriptor);
        }

        return sqlTables;
    }

    private static final Map<String, SqlTable> newSqlTableMap() {
        return new TreeMap<String, SqlTable>(new Comparator() {
            public int compare(Object o1, Object o2) {
                final String tablename1 = (String) o1;
                final String tablename2 = (String) o2;
                return tablename1.compareTo(tablename2);
            }
        });
    }

    private static Map<String, ClassDescriptor> getDescriptorTable() {
        final MetadataManager metadataManager = MetadataManager.getInstance();
        final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
        return descriptorRepository.getDescriptorTable();
    }

    private static void addSqlTableDescription(final Map<String, SqlTable> sqlTables,
            final ClassDescriptor classDescriptor) {
        final String tablename = classDescriptor.getFullTableName();
        final String classname = classDescriptor.getClassNameOfObject();
        if (!classname.startsWith("pt.utl.ist.berserk") && tablename != null && !tablename.startsWith("OJB")) {
            final SqlTable sqlTable = obtainSQLTable(sqlTables, tablename);

            addColumns(sqlTable, classDescriptor.getFieldDescriptions());
            setPrimaryKey(sqlTable, classDescriptor.getPkFields());
            addIndexes(sqlTable, classDescriptor);

            addSqlIndirectionTableDescription(sqlTables, classDescriptor);
        }
    }

    private static SqlTable obtainSQLTable(final Map<String, SqlTable> sqlTables, final String tablename) {
        final SqlTable sqlTable;

        if (sqlTables.containsKey(tablename)) {
            sqlTable = sqlTables.get(tablename);
        } else {
            sqlTable = new SqlTable(tablename);
            sqlTables.put(tablename, sqlTable);
        }

        return sqlTable;
    }

    private static void addColumns(final SqlTable sqlTable, final FieldDescriptor[] fieldDescriptions) {
        if (fieldDescriptions != null) {
            for (final FieldDescriptor fieldDescriptor : fieldDescriptions) {
                sqlTable.addColumn(fieldDescriptor.getColumnName(), fieldDescriptor.getColumnType());
            }
        }
    }

    private static void setPrimaryKey(final SqlTable sqlTable, final FieldDescriptor[] pkFields) {
        final String[] primaryKey = new String[pkFields.length];
        for (int i = 0; i < pkFields.length; i++) {
            primaryKey[i] = pkFields[i].getColumnName();
        }
        sqlTable.primaryKey(primaryKey);
    }

    private static void addIndexes(final SqlTable sqlTable, final ClassDescriptor classDescriptor) {
        for (final Iterator iterator = classDescriptor.getObjectReferenceDescriptors().iterator(); iterator.hasNext();) {
            final ObjectReferenceDescriptor objectReferenceDescriptor = (ObjectReferenceDescriptor) iterator.next();
            final String foreignKeyField = (String) objectReferenceDescriptor.getForeignKeyFields().get(0);
            final FieldDescriptor fieldDescriptor = classDescriptor.getFieldDescriptorByName(foreignKeyField);

            sqlTable.index(fieldDescriptor.getColumnName());
        }
    }

    private static void addSqlIndirectionTableDescription(final Map<String, SqlTable> sqlTables,
            final ClassDescriptor classDescriptor) {
        for (final Iterator iterator = classDescriptor.getCollectionDescriptors().iterator(); iterator.hasNext();) {
            final CollectionDescriptor collectionDescriptor = (CollectionDescriptor) iterator.next();
            final String indirectionTablename = collectionDescriptor.getIndirectionTable();
            if (indirectionTablename != null) {
                final SqlTable indirectionSqlTable = obtainSQLTable(sqlTables, indirectionTablename);

                final String foreignKeyToThis = collectionDescriptor.getFksToThisClass()[0];
                final String foreignKeyToOther = collectionDescriptor.getFksToItemClass()[0];

                indirectionSqlTable.addColumn(foreignKeyToThis, "INTEGER");
                indirectionSqlTable.addColumn(foreignKeyToOther, "INTEGER");

                indirectionSqlTable.primaryKey(new String[] { foreignKeyToThis, foreignKeyToOther });
            }
        }
    }

}
