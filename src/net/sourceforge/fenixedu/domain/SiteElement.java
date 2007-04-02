package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * Base class for all the accessible elements of a {@link net.sourceforge.fenixedu.domain.Site Site}.
 * 
 * @author cfgi
 */
public abstract class SiteElement extends SiteElement_Base {
    
    public SiteElement() {
        super();
    }
 
    public Group getPermittedGroup() {
        GroupAvailability groupAvailability = (GroupAvailability) getAvailabilityPolicy();
        
        Group result;
        if (groupAvailability == null) {
            result = null;
        }
        else {
            result = groupAvailability.getTargetGroup();
        }
        
        return result == null ? new EveryoneGroup() : result;
    }
    
    public void setPermittedGroup(Group group) {
        if (group == null) {
            setAvailabilityPolicy(null);
        }
        else {
            new GroupAvailability(this, group);
        }
    }
 
    protected boolean isNameUnique(List<? extends SiteElement> siblings, MultiLanguageString name) {
        Map<Language, Set<String>> names = new Hashtable<Language, Set<String>>();
        
        for (SiteElement sibling : siblings) {
            if (sibling == this) {
                continue;
            }
            
            MultiLanguageString siblingName = sibling.getName();
            for (Language language : siblingName.getAllLanguages()) {
                Set<String> languageNames = names.get(language);
                
                if (languageNames == null) {
                    languageNames = new HashSet<String>();
                    names.put(language, languageNames);
                }
                
                String localName = siblingName.getContent(language).toLowerCase();
                languageNames.add(localName);
            }
        }
        
        for (Language language : name.getAllLanguages()) {
            Set<String> languageNames = names.get(language);
            
            if (languageNames == null) {
                continue;
            }
            
            String localName = name.getContent(language).toLowerCase();
            if (languageNames.contains(localName)) {
                return false;
            }
        }
        
        return true;
    }

    public boolean isAllowedToEditPermissions() {
        return true;
    }
}
