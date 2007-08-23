package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.List;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class AccountabilityType extends AccountabilityType_Base {
    
    protected AccountabilityType() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public AccountabilityType(AccountabilityTypeEnum accountabilityTypeEnum) {
        this();
        setType(accountabilityTypeEnum);
    }

    @Override
    public void setType(AccountabilityTypeEnum type) {
	if(type == null) {
	    throw new DomainException("error.accountabilityType.empty.type");
	}
	super.setType(type);
    }  
    
    public static AccountabilityType readAccountabilityTypeByType(AccountabilityTypeEnum typeEnum) {
        List<AccountabilityType> allAccountabilityTypes = RootDomainObject.getInstance().getAccountabilityTypes();
        for (AccountabilityType accountabilityType : allAccountabilityTypes) {
            if (accountabilityType.getType().equals(typeEnum)) {
                return accountabilityType;
            }
        }
        return null;
    }
    
    @Override
    public void setTypeName(MultiLanguageString typeName) {
	if(typeName == null || typeName.isEmpty()) {
	    throw new DomainException("error.accountabilityType.empty.typeName");
	}
	super.setTypeName(typeName);
    }
    
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return getType() != null; 	
    }
    
    public String getName() {
	return getTypeName().getContent(Language.pt);
    }
    
    public void setName(String name) {	
	MultiLanguageString typeName = getTypeName();
	if(typeName == null) {
	    typeName = new MultiLanguageString();
	}
	typeName.setContent(Language.pt, name);
	setTypeName(typeName);
    }
}   
