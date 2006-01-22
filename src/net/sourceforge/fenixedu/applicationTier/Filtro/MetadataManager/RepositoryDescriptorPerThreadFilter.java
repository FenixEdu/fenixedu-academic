package net.sourceforge.fenixedu.applicationTier.Filtro.MetadataManager;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.Filtro.Filter;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;

public class RepositoryDescriptorPerThreadFilter extends Filter {

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest arg0, ServiceResponse arg1, FilterParameters filterParameters) throws Exception {

        MetadataManager mm = MetadataManager.getInstance();
        // tell the manager to use per thread mode
        mm.setEnablePerThreadChanges(true);
        DescriptorRepository descriptorRepository = ((SuportePersistenteOJB) persistentSupport)
                .getDescriptor("lightVersion");
        if (descriptorRepository == null) {
            descriptorRepository = mm.readDescriptorRepository("OJB/lightVersion/repository.xml");
            ((SuportePersistenteOJB) persistentSupport).setDescriptor(descriptorRepository, "lightVersion");
        }

        mm.setDescriptor(descriptorRepository);

    }


}