package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.PedagogicalCouncilSite;
import net.sourceforge.fenixedu.domain.contents.Content;

public class PedagogicalCouncilPathProcessor extends AbstractPathProcessor {

    @Override
    public Content processPath(String path) {
	return PedagogicalCouncilSite.getSite();
    }

    @Override
    public String getTrailingPath(String path) {
       return path;
    }
}
