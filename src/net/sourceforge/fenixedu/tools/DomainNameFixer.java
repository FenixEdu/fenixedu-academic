package net.sourceforge.fenixedu.tools;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;

public class DomainNameFixer {

	private static final Logger logger = Logger.getLogger(DomainNameFixer.class);

	public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        final Map descriptorTable = getDescriptorTable();

        logger.info("Repository contains " + descriptorTable.size() + " mapped classes.");

		StringBuffer stringBuffer = new StringBuffer();

        for (final Iterator iterator = descriptorTable.entrySet().iterator(); iterator.hasNext();)
        {
            final Entry entry = (Entry) iterator.next();
            final String className = (String) entry.getKey();
            final ClassDescriptor classDescriptor = (ClassDescriptor) entry.getValue();

			final FieldDescriptor concreteClassField = classDescriptor.getOjbConcreteClassField();
			constructUpdateQuery(stringBuffer, classDescriptor, concreteClassField);

			final FieldDescriptor concreteClassForStudentCurricularPlans = classDescriptor.getFieldDescriptorByName("concreteClassForStudentCurricularPlans");
			constructUpdateQuery(stringBuffer, classDescriptor, concreteClassForStudentCurricularPlans);

			final FieldDescriptor concreteClassForDegreeCurricularPlans = classDescriptor.getFieldDescriptorByName("concreteClassForDegreeCurricularPlans");
			constructUpdateQuery(stringBuffer, classDescriptor, concreteClassForDegreeCurricularPlans);
		}

		stringBuffer.append("");

		logger.warn(stringBuffer);

        final long stopTime = System.currentTimeMillis();

        logger.info("Generation complete. Took: " + (stopTime - startTime) + "ms.");
        System.exit(0);

	}

	private static void constructUpdateQuery(StringBuffer stringBuffer, final ClassDescriptor classDescriptor, final FieldDescriptor concreteClassField) {
		if (concreteClassField != null) {
			final String tableName = classDescriptor.getFullTableName();
			final String columnName = concreteClassField.getColumnName();
			stringBuffer.append("select distinct(concat('update ");
			stringBuffer.append(tableName);
			stringBuffer.append(" set ");
			stringBuffer.append(columnName);
			stringBuffer.append(" = \"net.sourceforge.fenixedu.', 'domain.', right(");
			stringBuffer.append(columnName);
			stringBuffer.append(", LENGTH(");
			stringBuffer.append(columnName);
			stringBuffer.append(") - 8), '\" where ");
			stringBuffer.append(columnName);
			stringBuffer.append("= \"', ");
			stringBuffer.append(columnName);
			stringBuffer.append(", '\";')) as \"\" from ");
			stringBuffer.append(tableName);
			stringBuffer.append(";\n");
		}
	}

    protected static Map getDescriptorTable()
    {
        final MetadataManager metadataManager = MetadataManager.getInstance();
        final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
        return descriptorRepository.getDescriptorTable();
    }

}
