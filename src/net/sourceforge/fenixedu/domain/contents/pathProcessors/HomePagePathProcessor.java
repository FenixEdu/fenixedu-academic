package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

public class HomePagePathProcessor extends AbstractPathProcessor {

    private String getIstUserName(String path) {
	final int indexOfSlash = path.indexOf('/');
	return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;
    }
    
    public Content processPath(String path) {
	String istUserName = getIstUserName(path);
	Person person = Person.readPersonByIstUsername(istUserName);
	Homepage homepage = person != null ? person.getHomepage() : null;
	return homepage != null && homepage.isAvailable() ? homepage : null;
    }

}
