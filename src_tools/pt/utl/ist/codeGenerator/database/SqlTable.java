package pt.utl.ist.codeGenerator.database;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SqlTable {

    private static final Map<String, String> mySqlTypeTranslation = new HashMap<String, String>();
    static {
        mySqlTypeTranslation.put("BIT", "tinyint(1)");
        mySqlTypeTranslation.put("CHAR", "varchar(20)");
        mySqlTypeTranslation.put("DATE", "date");
        mySqlTypeTranslation.put("DOUBLE", "double");
        mySqlTypeTranslation.put("FLOAT", "float(10,2)");
        mySqlTypeTranslation.put("INTEGER", "int(11)");
        mySqlTypeTranslation.put("LONGVARCHAR", "text");
        mySqlTypeTranslation.put("TIME", "time");
        mySqlTypeTranslation.put("TIMESTAMP", "timestamp NULL default NULL");
        mySqlTypeTranslation.put("VARCHAR", "text");
        mySqlTypeTranslation.put("BLOB", "blob");
        mySqlTypeTranslation.put("BIGINT", "bigint(20)");

        mySqlTypeTranslation.put(null, "tinyint(1)");
    }


    public class Column {
        final String name;
        final String type;

        private Column(final String name, final String type) {
            this.name = name;
            this.type = type;
        }

        public void appendCreateTableMySql(final StringBuilder stringBuilder) {
            stringBuilder.append(name);
            stringBuilder.append(" ");
            String typeTranslated=mySqlTypeTranslation.get(type);
            if(typeTranslated==null)
            {
            	System.out.println("No mapping defined for generic type "+type+" for the current database! Assuming that the db type will be the same as the generic type... Please review the resulting sql file for the table "+SqlTable.this.tablename+" and for field "+name);
            	typeTranslated=type;
            }
            stringBuilder.append(typeTranslated);
            if (name.equals("ID_INTERNAL")) {
        	stringBuilder.append(" NOT NULL auto_increment");
            }
        }

        public boolean equals(Object obj) {
            if (obj != null && obj instanceof Column) {
                final Column column = (Column) obj;
                return name.equals(column.name);
            }
            return false;
        }

        public int hashCode() {
            return name.hashCode();
        }
    }


    final String tablename;

    final Set<Column> columns = new TreeSet<Column>(new Comparator() {
        public int compare(Object o1, Object o2) {
            final Column column1 = (Column) o1;
            final Column column2 = (Column) o2;
            return column1.name.compareTo(column2.name);
        }
    });

    final Set<String> indexes = new TreeSet<String>();

    String[] primaryKey = null;

    public SqlTable(final String tablename) {
        this.tablename = tablename;
    }

    public void addColumn(final String name, final String type) {
        columns.add(new Column(name, type));
    }

    public void index(final String columnName) {
        indexes.add(columnName);
    }

    public void primaryKey(final String[] primaryKey) {
        this.primaryKey = primaryKey;
    }


    public void appendCreateTableMySql(final StringBuilder stringBuilder) {
        stringBuilder.append("create table ");
        stringBuilder.append(tablename);
        stringBuilder.append(" (\n");

        for (final Iterator iterator = columns.iterator(); iterator.hasNext();) {
            final Column column = (Column) iterator.next();
            stringBuilder.append("  ");
            column.appendCreateTableMySql(stringBuilder);
            if (iterator.hasNext()) {
                stringBuilder.append(",");
                stringBuilder.append("\n");
            }
        }

        if (primaryKey != null) {
            stringBuilder.append(",\n  primary key (");
            for (int i = 0; i < primaryKey.length; i++) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(primaryKey[i]);
            }
            stringBuilder.append(")");
        } else {
            System.out.println("No primary key for table " + tablename);
        }

        for (final String columnName : indexes) {
            stringBuilder.append(",\n  index (");
            stringBuilder.append(columnName);
            stringBuilder.append(")");
        }
        stringBuilder.append("\n");

        stringBuilder.append(") type=InnoDB ;");
    }

}
