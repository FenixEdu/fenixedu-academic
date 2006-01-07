package relations;

public class CmsContents extends CmsContents_Base {
	 public static void add(net.sourceforge.fenixedu.domain.cms.Cms CMS, net.sourceforge.fenixedu.domain.cms.Content contents) {
	       CmsContents_Base.add(CMS,contents);
	        
	    }
	    
	    public static void remove(net.sourceforge.fenixedu.domain.cms.Cms CMS, net.sourceforge.fenixedu.domain.cms.Content contents) {
	        CmsContents_Base.remove(CMS,contents);
	    }
}
