package relations;

import net.sourceforge.fenixedu.stm.VBox;
import net.sourceforge.fenixedu.stm.RelationList;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;
public class CmsContents extends CmsContents_Base {
	 public static void add(net.sourceforge.fenixedu.domain.cms.ICms CMS, net.sourceforge.fenixedu.domain.cms.IContent contents) {
	       CmsContents_Base.add(CMS,contents);
	        
	    }
	    
	    public static void remove(net.sourceforge.fenixedu.domain.cms.ICms CMS, net.sourceforge.fenixedu.domain.cms.IContent contents) {
	        CmsContents_Base.remove(CMS,contents);
	    }
}
