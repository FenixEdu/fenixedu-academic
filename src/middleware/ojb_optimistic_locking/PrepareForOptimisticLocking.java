/*
 * Created on Dec 26, 2003
 *  
 */
package middleware.ojb_optimistic_locking;

import java.util.Iterator;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class PrepareForOptimisticLocking extends ObjectFenixOJB
{

	private static String ALTER_TABLE_STRING = "alter table ";
	private static String ADD_ACKOPTLOCK_COLUMN = " add ACKOPTLOCK int(11);";
	private static String NEWLINE = "\n";

	private static SuportePersistenteOJB persistentSupport;
	private static String outputBuffer;

	static {
		try
		{
			persistentSupport = SuportePersistenteOJB.getInstance();
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			System.out.println("Unable to initialize persistent support.");
		}
	}

	/**
	 *  
	 */
	public PrepareForOptimisticLocking()
	{
		super();
	}

	public static void main(String[] args)
	{
		MetadataManager metadataManager = MetadataManager.getInstance();
		DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
		Iterator iteratorDescriptorTable = descriptorRepository.getDescriptorTable().values().iterator();
		while (iteratorDescriptorTable.hasNext())
		{
			ClassDescriptor classDescriptor = (ClassDescriptor) iteratorDescriptorTable.next();
			if ((classDescriptor != null)
				&& (classDescriptor.getFullTableName() != null)
				&& (!classDescriptor.getFullTableName().startsWith("OJB")))
			{
				//System.out.println(classDescriptor.getFullTableName());
//				outputBuffer += ALTER_TABLE_STRING
//					+ classDescriptor.getFullTableName()
//					+ ADD_ACKOPTLOCK_COLUMN
//					+ NEWLINE;
				boolean containsACK_OPT_LOCK = false;
				FieldDescriptor[] fieldDescriptors = classDescriptor.getFieldDescriptions();
				for (int i = 0; i < fieldDescriptors.length; i++)
				{
					FieldDescriptor fieldDescriptor = fieldDescriptors[i];
					if (fieldDescriptor != null
							&& fieldDescriptor.getColumnName() != null
							&& fieldDescriptor.getColumnName().equals("ACK_OPT_LOCK"))
					{
						containsACK_OPT_LOCK = true;
					}
				}
				
				if (!containsACK_OPT_LOCK) {
					System.out.println("Mapping of class ["
							+ classDescriptor.getClassNameOfObject() +
							"] does not contain ACK_OPT_LOCK column." );
				}
			}
		}

		//		try {
		//			FileWriter fileWriter = new FileWriter("alterTables.sql");
		//			fileWriter.write(outputBuffer);
		//			fileWriter.close();
		//		} catch (IOException e1) {
		//			e1.printStackTrace();
		//		}
	}
}