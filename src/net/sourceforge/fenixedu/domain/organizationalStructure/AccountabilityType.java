package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.apache.commons.lang.StringUtils;

public class AccountabilityType extends AccountabilityType_Base {
    
    protected AccountabilityType() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public AccountabilityType(AccountabilityTypeEnum accountabilityTypeEnum, MultiLanguageString name) {
        this();
        setType(accountabilityTypeEnum);
        setTypeName(name);
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
	return getType() != null && getTypeName() != null && !getTypeName().isEmpty(); 	
    }
    
    public boolean isFunction() {
    	return false;
    }

	public String getName() {
	return getTypeName().getPreferedContent();
    }

    public void setName(String name) {	
	
	if(name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.accountabilityType.empty.name");
	}
	
	MultiLanguageString typeName = getTypeName();
	typeName = typeName == null ? new MultiLanguageString() : typeName;
	typeName.setContent(Language.getDefaultLanguage(), name);
	
	setTypeName(typeName);
    }
}   