package pt.utl.ist.codeGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;

public class SQLGenerator {

	private class SQLTable {

		private class Column implements Comparable {
			final String name;
			final String type;

			private Column(final String name, final String type) {
				this.name = name;
				this.type = type;
			}

			public void append(final StringBuilder stringBuilder) {
				stringBuilder.append(name);
				stringBuilder.append(" ");
				stringBuilder.append(type);
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

			public int compareTo(Object obj) {
				if (obj != null && obj instanceof Column) {
					final Column column = (Column) obj;
					return name.compareTo(column.name);
				}
				return -1;
			}
		}

		final String tablename;
		final Set<Column> columns = new TreeSet();

		private SQLTable(final String tablename) {
			this.tablename = tablename;
		}

		public void addColumn(final String name, final String type) {
			columns.add(new Column(name, type));
		}

		public void append(final StringBuilder stringBuilder) {
			stringBuilder.append("CREATE TABLE ");
			stringBuilder.append(tablename);
			stringBuilder.append(" (\n");

			for (final Iterator iterator = columns.iterator(); iterator.hasNext(); ) {
				final Column column = (Column) iterator.next();
				stringBuilder.append("\t");
				column.append(stringBuilder);
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
				stringBuilder.append("\n");
			}

			stringBuilder.append(") TYPE=InnoDB;");
		}

		public String toString() {
			final StringBuilder stringBuilder = new StringBuilder();
			append(stringBuilder);
			return stringBuilder.toString();
		}

	}

	private static final SQLGenerator instance = new SQLGenerator();

	private static final Map<String, SQLTable> sqlTables = new HashMap<String, SQLTable>();

	public static void main(String[] args) {
		try {
			generate("/tmp/fenix.sql");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("Generation Complete.");
		System.exit(0);
	}

	private static void generate(final String destinationFilename) {
		final Map<String, ClassDescriptor> classDescriptorMap = getDescriptorTable();
		for (final ClassDescriptor classDescriptor : classDescriptorMap.values()) {
			final String tablename = classDescriptor.getFullTableName();
			if (tablename != null) {
				final SQLTable sqlTable;
				if (sqlTables.containsKey(tablename)) {
					sqlTable = sqlTables.get(tablename);
				} else {
					sqlTable = instance.new SQLTable(tablename);
					sqlTables.put(tablename, sqlTable);
				}

				System.out.println("tablename: " + tablename);
				System.out.println("tablename: " + classDescriptor.getClassNameOfObject());

				for (final FieldDescriptor fieldDescriptor : classDescriptor.getFieldDescriptions()) {
					sqlTable.addColumn(fieldDescriptor.getColumnName(), fieldDescriptor.getColumnType());
				}

				System.out.println(sqlTable.toString());
			}
		}
	}

	protected static Map<String, ClassDescriptor> getDescriptorTable() {
		final MetadataManager metadataManager = MetadataManager.getInstance();
		final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
		return descriptorRepository.getDescriptorTable();
	}

}