/*
 * Created on Feb 27, 2005
 *
 */
package Tools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.MultiHashMap;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;

/**
 * @author Luis Cruz
 * 
 */
public class DMLGenerator
{

    private static final Logger logger = Logger.getLogger(DMLGenerator.class);

    public static void main(String[] args)
    {
        final long startTime = System.currentTimeMillis();

        final Map descriptorTable = getDescriptorTable();
        final Map dmlDescriptorTable = new HashMap(descriptorTable.size());
        final Map relationsTable = new MultiHashMap();

        logger.info("Repository contains " + descriptorTable.size() + " mapped classes.");

        for (final Iterator iterator = descriptorTable.entrySet().iterator(); iterator.hasNext();)
        {
            final Entry entry = (Entry) iterator.next();
            final String className = (String) entry.getKey();
            final ClassDescriptor classDescriptor = (ClassDescriptor) entry.getValue();

            final DMLClassDescriptor dmlClassDescriptor = generateDMLForClassDescriptor(className,
                    classDescriptor);
            dmlDescriptorTable.put(className, dmlClassDescriptor);
        }

        for (final Iterator iterator = dmlDescriptorTable.values().iterator(); iterator.hasNext(); ) {
            final DMLClassDescriptor dmlClassDescriptor = (DMLClassDescriptor) iterator.next();
            final Set clasDescriptorRelations = dmlClassDescriptor.getRelations();
            for (final Iterator relationsInterator = clasDescriptorRelations.iterator(); relationsInterator.hasNext(); ) {
                final DMLRelationDescriptor dMLRelationDescriptor = (DMLRelationDescriptor) relationsInterator.next();
                final String key = dMLRelationDescriptor.getKey();
                if (key != null) {
                    if (!relationsTable.containsKey(key)) {
                        relationsTable.put(key, dMLRelationDescriptor);
                    } else {
                        System.out.println("Relations table already contains value for key: " + key);
                    }
                } else {
                    //System.out.println("Key is null.");
                }
            }
        }

        System.out.println("Found a total of " + relationsTable.size() + " relations.");

        try
        {
            generateDMLFile("dml.file", dmlDescriptorTable);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        final long stopTime = System.currentTimeMillis();

        logger.info("DML generation complete. Took: " + (stopTime - startTime) + "ms.");
        System.exit(0);
    }

    protected static Map getDescriptorTable()
    {
        final MetadataManager metadataManager = MetadataManager.getInstance();
        final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
        return descriptorRepository.getDescriptorTable();
    }

    protected static DMLClassDescriptor generateDMLForClassDescriptor(final String className,
            final ClassDescriptor classDescriptor)
    {
        final DMLClassDescriptor dmlClassDescriptor = new DMLClassDescriptor(className, classDescriptor);
        return dmlClassDescriptor;
    }

    protected static void generateDMLFile(final String filename, final Map dmlDescriptorTable)
            throws IOException
    {
        final StringBuffer stringBuffer = new StringBuffer(dmlDescriptorTable.size() * 5 * 80);
        for (final Iterator iterator = dmlDescriptorTable.values().iterator(); iterator.hasNext();)
        {
            final DMLClassDescriptor dmlClassDescriptor = (DMLClassDescriptor) iterator.next();
            dmlClassDescriptor.appendDMLDescription(stringBuffer);
        }
        writeFile(filename, stringBuffer.toString(), false);
    }

    public static void writeFile(final String filename, final String fileContents, final boolean append)
            throws IOException
    {
        final FileWriter fileWriter = new FileWriter(filename, append);
        fileWriter.write(fileContents);
        fileWriter.close();
    }

}
