package net.sourceforge.fenixedu.domain.contents;

import java.util.List;

import net.sourceforge.fenixedu.domain.Site;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ContentJump extends ContentJump_Base {

    public ContentJump(MultiLanguageString jumpName, Site site) {
        super();
        setName(jumpName);
        setJumpTo(site);
    }

    @Override
    public void setNormalizedName(final MultiLanguageString normalizedName) {
        // unable to optimize because we cannot track changes to name correctly.
        // don't call super.setNormalizedName() !
    }

    @Override
    public void addPathContentsForTrailingPath(final List<Content> contents, final String trailingPath) {
        final Container container = getJumpTo();
        contents.add(container);
        final int size = contents.size();
        String newTrailingPath = trailingPath;
        container.addPathContentsForTrailingPath(contents, newTrailingPath);

        if (contents.size() == size
                && !(newTrailingPath.length() == 0 || (newTrailingPath.length() == 1 && newTrailingPath.charAt(0) == '/'))) {
            throw new InvalidContentPathException(this, newTrailingPath);
        }
    }

    @Override
    public void addPathContents(List<Content> contents, String path) {
        super.addPathContents(contents, path);
        String subPath = getSubPathForSearch(path);
        if (matchesPath(subPath) && !(subPath.length() + 1 < path.length())) {
            contents.add(getJumpTo());
            Content initialContent = getJumpTo().getInitialContent();
            if (initialContent != null) {
                contents.add(initialContent);
            }
        }
    }

}
