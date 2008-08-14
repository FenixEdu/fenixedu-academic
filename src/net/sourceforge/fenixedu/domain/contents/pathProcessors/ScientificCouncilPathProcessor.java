package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.contents.Content;

public class ScientificCouncilPathProcessor extends AbstractPathProcessor {

    @Override
    public Content processPath(String path) {
	return ScientificCouncilSite.getSite();
    }

    @Override
    public String getTrailingPath(String path) {
	return path;
    }

    @Override
    public Content getInitialContent() {
	return ScientificCouncilSite.getSite();
    }

    public boolean keepPortalInContentsPath() {
	return false;
    }
}
