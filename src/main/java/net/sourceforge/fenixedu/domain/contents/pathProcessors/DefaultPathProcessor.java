package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.FenixFramework;

public class DefaultPathProcessor extends AbstractPathProcessor {

    protected String getTemplatedIdString(final String path) {
        final int indexOfSlash = path.indexOf('/');
        return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;
    }

    protected String getTemplatedId(final String path) {
        final String templatedIdString = getTemplatedIdString(path);
        return StringUtils.isEmpty(templatedIdString) ? null : templatedIdString;
    }

    protected Container getTemplatedContent(final String path) {
        final String id = getTemplatedId(path);
        return id == null ? null : (Container) FenixFramework.getDomainObject(id);
    }

    @Override
    public Content processPath(String path) {
        return getTemplatedContent(path);
    }

}
