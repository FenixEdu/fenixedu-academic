package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;

public class DefaultPathProcessor extends AbstractPathProcessor {

    protected String getTemplatedIdString(final String path) {
	final int indexOfSlash = path.indexOf('/');
	return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;
    }

    protected Integer getTemplatedId(final String path) {
	final String templatedIdString = getTemplatedIdString(path);
	return templatedIdString == null ? null : Integer.valueOf(templatedIdString);
    }

    protected Container getTemplatedContent(final String path) {
	final Integer id = getTemplatedId(path);
	return id == null ? null : (Container) RootDomainObject.getInstance().readContentByOID(id);
    }

    public Content processPath(String path) {
	return getTemplatedContent(path);
    }

}
