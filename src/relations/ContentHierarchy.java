package relations;

import net.sourceforge.fenixedu.stm.VBox;
import net.sourceforge.fenixedu.stm.RelationList;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;
public class ContentHierarchy extends ContentHierarchy_Base {
    
	 public static void add(net.sourceforge.fenixedu.domain.cms.IContent children, net.sourceforge.fenixedu.domain.cms.IBin parents) {
		 ContentHierarchy_Base.add(children,parents);
		 ContentOwnership.add(parents.getOwner(),children);
	     
	    }
	    
	    public static void remove(net.sourceforge.fenixedu.domain.cms.IContent children, net.sourceforge.fenixedu.domain.cms.IBin parents) {
	        ContentHierarchy_Base.remove(children,parents);
	        ContentOwnership.remove(children.getOwner(),children);
	        
	    }
}
