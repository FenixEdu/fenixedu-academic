package pt.utl.ist.domain;

import java.util.Iterator;
import java.util.Map;

import net.sourceforge.fenixedu.domain.IDomainObject;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;

public class DomainDMLOJBVerifier {

    private static final Logger logger = Logger.getLogger(DomainDMLOJBVerifier.class);

    public static void main(final String[] args) {
        final Map ojbMetadata = getDescriptorTable();
        verifyDMLMappingFromOJBMetadata(ojbMetadata);

        logger.warn("Verification complete.");
        System.exit(0);
    }

    protected static void verifyDMLMappingFromOJBMetadata(final Map ojbMetadata) {
        for (final Iterator iterator = ojbMetadata.values().iterator(); iterator.hasNext(); ) {
            final ClassDescriptor classDescriptor = (ClassDescriptor) iterator.next();
            final Class mappedClass = classDescriptor.getClassOfObject();

            if (isDomainClass(mappedClass)) {
                
            } else {
                System.out.println("Non-domain class: " + mappedClass.getName() + " included in OJB mapping.");
            }
        }
    }

    protected static boolean isDomainClass(final Class mappedClass) {
        return false;
    }

    protected static Map getDescriptorTable()
    {
        final MetadataManager metadataManager = MetadataManager.getInstance();
        final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
        return descriptorRepository.getDescriptorTable();
    }

}
