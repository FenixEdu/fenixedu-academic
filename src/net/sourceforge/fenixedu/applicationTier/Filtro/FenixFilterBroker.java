package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterBroker;
import pt.utl.ist.berserk.logic.filterManager.FilteringResult;
import pt.utl.ist.berserk.logic.filterManager.exceptions.ClassNotIFilterException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.IncompatibleFilterException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.InvalidFilterException;
import pt.utl.ist.berserk.logic.filterManager.exceptions.InvalidFilterExpressionException;
import pt.utl.ist.berserk.util.Dictionary;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class FenixFilterBroker extends FilterBroker {
    
    private Set<String> servicesToMonitor;
    
    private Set<String> usersToMonitor;

    public FenixFilterBroker(List serviceChainAssociations, Dictionary avaliableFiltersDefinitions)
            throws InvalidFilterExpressionException, InvalidFilterException, ClassNotIFilterException,
            IncompatibleFilterException {
        
        super(serviceChainAssociations, avaliableFiltersDefinitions);
        
        servicesToMonitor = readSubjectsToMonitor("servicesToMonitor");       
        usersToMonitor = readSubjectsToMonitor("usersToMonitor");
                                
    }

    private Set<String> readSubjectsToMonitor(String fileName) {
        
        ResourceBundle rb = ResourceBundle.getBundle(fileName);
        
        Set valuesSet = new HashSet<String>();
        for (Enumeration keys = rb.getKeys(); keys.hasMoreElements(); ) {
            valuesSet.add(keys.nextElement());
        }
        
        return valuesSet;        
    }

    @Override
    public FilteringResult executePostFiltering(ServiceRequest request, ServiceResponse response) throws Exception {
        FilteringResult result = super.executePostFiltering(request, response);

        if(result.isSuccess()){
            
            String serviceName = request.getService().getClass().getName();

            String username = null;            
            IUserView requester = (IUserView) request.getRequester(); 
            if(requester != null ){                
                username = requester.getUtilizador();
            }
            
            if(this.usersToMonitor.contains(username) || this.servicesToMonitor.contains(serviceName)){
                logService(username, serviceName, request.getArguments());
            }
                        
        }
        
        return result;
    }

    private void logService(String username, String serviceName, Object[] arguments) {
        
        StringBuilder argumentsInString = new StringBuilder();
        
        for (Object argument : arguments) {
            if(argument != null){
                argumentsInString.append(argument.toString());
                argumentsInString.append("; ");
            }
        }
        
        DomainFactory.makeServiceLog(username, serviceName, argumentsInString.toString());
        
    }
    
}
