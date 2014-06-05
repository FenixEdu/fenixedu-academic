/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.cms;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.StringNormalizer;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class CmsContent extends CmsContent_Base implements Comparable<CmsContent> {

    public CmsContent() {
        super();
        setOrder(Integer.MAX_VALUE);
    }

    public Group getPermittedGroup() {
        if (getGroup() == null) {
            return AnyoneGroup.get();
        } else {
            return getGroup().toGroup();
        }
    }

    public void setPermittedGroup(Group group) {
        setGroup(group.toPersistentGroup());
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
        if (getParent() != null && !getParent().isAvailable()) {
            return false;
        }

        if (getGroup() == null) {
            return true;
        }
        return getGroup().isMember(Authenticate.getUser());
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
        setGroup(null);
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
        for (final Locale language : multiLanguageString.getAllLocales()) {
            result = result.with(language, normalize(multiLanguageString.getContent(language)));
        }
        return result;
    }

    public static String normalize(final String string) {
        return string == null ? null : StringNormalizer.normalize(string).replace(' ', '-');
    }

    public boolean isPublicAvailable() {
        if (getPermittedGroup() == null) {
            if (getParent() != null) {
                return getParent().isPublicAvailable();
            } else {
                return true;
            }
        } else {
            return getPermittedGroup().isMember(null);
        }
    }

}
