package net.sourceforge.fenixedu.domain.dissertation;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;

public class Comment extends Comment_Base {
    
    public  Comment() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    } 
}
