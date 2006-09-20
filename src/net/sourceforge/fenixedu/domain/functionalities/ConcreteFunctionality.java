package net.sourceforge.fenixedu.domain.functionalities;

import java.util.UUID;

import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * A concrete functionality is the point were the decision of how to 
 * start the functionality for the user is made.
 * 
 * @author cfgi
 */
public class ConcreteFunctionality extends ConcreteFunctionality_Base {
    
    protected ConcreteFunctionality() {
        super();
    }
    
    public ConcreteFunctionality(MultiLanguageString name) {
        this();
        
        setName(name);
        changeUuid(Functionality.generateUuid());
    }

    public ConcreteFunctionality(UUID uuid, MultiLanguageString name) {
        this();
        
        setName(name);
        changeUuid(uuid);
    }

    @Override
    public boolean isConcrete() {
        return true;
    }
    
    @Override
    protected void checkDeletion() {
        // can be deleted
    }

    @Override
    protected void disconnect() {
        super.disconnect();
        
        removeModule();
    }

}
