package net.sourceforge.fenixedu.domain.cms;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.groups.AnyoneGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.StringNormalizer;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class CmsContent extends CmsContent_Base implements Comparable<CmsContent> {

    public CmsContent() {
        super();
        setOrder(Integer.MAX_VALUE);
    }

    public Group getPermittedGroup() {
        return new EveryoneGroup();
    }

    public void setPermittedGroup(Group group) {
        // Do nothing...
    }

    @Override
    public int compareTo(CmsContent o) {
        int order = this.getOrder().compareTo(o.getOrder());
        if (order == 0) {
            order = this.getName().compareTo(o.getName());
            if (order == 0) {
                order = this.getExternalId().compareTo(o.getExternalId());
            }
        }
        return order;
    }

    public boolean isAvailable() {
        if (getAccessGroup() == null) {
            return true;
        }
        return getAccessGroup().isMember(Authenticate.getUser());
    }

    public Site getOwnerSite() {
        return getParent().getOwnerSite();
    }

    @Override
    public Section getParent() {
        return super.getParent();
    }

    public void delete() {
        for (FileContent file : getFileContentSet()) {
            file.delete();
        }
        setParent(null);
        setAccessGroup(null);
        deleteDomainObject();
    }

    public String getFullPath() {
        StringBuilder builder = new StringBuilder();
        builder.append(getOwnerSite().getFullPath());
        appendFullPath(builder);
        return builder.toString();
    }

    public Collection<FileContent> getVisibleFiles() {
        return Collections2.filter(getFileContentSet(), new Predicate<FileContent>() {
            @Override
            public boolean apply(FileContent input) {
                return input.getVisible();
            }
        });
    }

    protected void appendFullPath(StringBuilder builder) {
        if (getParent() != null) {
            getParent().appendFullPath(builder);
        }
        builder.append('/');
        builder.append(getNormalizedName().getContent());
    }

    public void applyStructureModifications(Section newParent, int order) {
        setParent(Objects.requireNonNull(newParent, "newParent"));
        setOrder(order);
    }

    @Override
    public void setName(final MultiLanguageString name) {
        checkInvalidCharacters(name);
        super.setName(name);
        setNormalizedName(normalize(name));
    }

    private void checkInvalidCharacters(MultiLanguageString name) {
        String validChars = "_\\- .,:;!()*$&'=@";
        for (String content : name.getAllContents()) {
            // if the accepted character list is changed, consider changing the 'File.java' list as well
            if (!Pattern.matches("[\\p{IsLatin}0-9" + validChars + "]+", content)) {
                throw new DomainException("label.error.content.invalid.name", validChars.replace("\\", ""));
            }
        }
    }

    @Override
    public MultiLanguageString getNormalizedName() {
        final MultiLanguageString normalizedName = super.getNormalizedName();
        return normalizedName == null ? normalize(getName()) : normalizedName;
    }

    public boolean matchesPath(final String path) {
        for (final String name : getNormalizedName().getAllContents()) {
            if (name.equalsIgnoreCase(path)) {
                return true;
            }
        }
        return false;
    }

    public static MultiLanguageString normalize(final MultiLanguageString multiLanguageString) {
        if (multiLanguageString == null) {
            return null;
        }
        MultiLanguageString result = new MultiLanguageString();
        for (final Language language : multiLanguageString.getAllLanguages()) {
            result = result.with(language, normalize(multiLanguageString.getContent(language)));
        }
        return result;
    }

    public static String normalize(final String string) {
        return string == null ? null : StringNormalizer.normalize(string).replace(' ', '-');
    }

    public boolean isPublicAvailable() {
        if (getAccessGroup() == null) {
            if (getParent() != null) {
                return getParent().isPublicAvailable();
            } else {
                return true;
            }
        } else {
            return getAccessGroup() == AnyoneGroup.getInstance();
        }

    }

}
