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
package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class IrsDeclarationLink extends IrsDeclarationLink_Base {

    public IrsDeclarationLink() {
        super();
        final Bennu rootDomainObject = Bennu.getInstance();
        if (rootDomainObject.getIrsDeclarationLink() == null) {
            setRootDomainObject(rootDomainObject);
        } else {
            throw new Error("there.can.only.be.one!");
        }
        setAvailable(Boolean.FALSE);
        setTitle(new MultiLanguageString(MultiLanguageString.pt, "IRS"));
        setIrsLink("");
    }

    @Atomic
    public static IrsDeclarationLink getInstance() {
        final Bennu rootDomainObject = Bennu.getInstance();
        final IrsDeclarationLink irsDeclarationLink = rootDomainObject.getIrsDeclarationLink();
        return irsDeclarationLink == null ? new IrsDeclarationLink() : irsDeclarationLink;
    }

    @Atomic
    public static void set(MultiLanguageString title, Boolean available, String irsLink) {
        final Bennu rootDomainObject = Bennu.getInstance();
        final IrsDeclarationLink irsDeclarationLink = rootDomainObject.getIrsDeclarationLink();
        irsDeclarationLink.setTitle(title);
        irsDeclarationLink.setAvailable(available);
        irsDeclarationLink.setIrsLink(irsLink);
    }

    @Deprecated
    public boolean hasAvailable() {
        return getAvailable() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasIrsLink() {
        return getIrsLink() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

}
