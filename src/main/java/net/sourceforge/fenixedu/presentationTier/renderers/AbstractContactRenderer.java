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
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class AbstractContactRenderer extends OutputRenderer {
    private boolean publicSpace = false;

    // If this is true and there is at least one contact being shown the
    // defaultLabel will be suffixed to the mark the contact as default.
    private boolean showDefaultSuffix = true;

    // If this is true and there is at least one contact being shown, and the
    // types are more than one, a type label will be suffixed.
    private boolean showTypeSuffix = true;

    private String schema;

    private String bundle;

    private String defaultLabel;

    private String types = "PERSONAL, WORK, INSTITUTIONAL";

    // locals
    private boolean showDefault;

    private boolean showType;

    protected List<MetaObject> getFilteredContacts(Collection<PartyContact> unfiltered) {
        String[] parts = getTypes().split(",");
        ArrayList<PartyContactType> typeEnums = new ArrayList<PartyContactType>();
        for (String part : parts) {
            typeEnums.add(PartyContactType.valueOf(part.trim()));
        }

        List<MetaObject> contacts = new ArrayList<MetaObject>();
        for (PartyContact contact : unfiltered) {
            if (isVisible(contact, publicSpace) && typeEnums.contains(contact.getType())) {
                contacts.add(MetaObjectFactory.createObject(contact, RenderKit.getInstance().findSchema(getSchema())));
            }
        }
        showType = isShowTypeSuffix() && contacts.size() > 1 && typeEnums.size() > 1;
        showDefault = isShowDefaultSuffix() && contacts.size() > 1;
        Collections.sort(contacts, new Comparator<MetaObject>() {
            @Override
            public int compare(MetaObject o1, MetaObject o2) {
                PartyContact contact1 = (PartyContact) o1.getObject();
                PartyContact contact2 = (PartyContact) o2.getObject();
                if (contact1.getType().ordinal() > contact2.getType().ordinal()) {
                    return -1;
                } else if (contact1.getType().ordinal() < contact2.getType().ordinal()) {
                    return 1;
                } else if (contact1.getDefaultContact().booleanValue()) {
                    return -1;
                } else if (contact2.getDefaultContact().booleanValue()) {
                    return 1;
                } else {
                    return contact1.getPresentationValue().compareTo(contact2.getPresentationValue());
                }
            }
        });
        return contacts;
    }

    private boolean isVisible(PartyContact contact, boolean publicSpace) {
        if (!Authenticate.isLogged() && publicSpace && contact.getVisibleToPublic().booleanValue()) {
            return true;
        }
        if (Authenticate.isLogged()) {
            User user = Authenticate.getUser();
            Person reader = user.getPerson();
            if (reader.hasRole(RoleType.CONTACT_ADMIN).booleanValue() || reader.hasRole(RoleType.MANAGER).booleanValue()
                    || reader.hasRole(RoleType.DIRECTIVE_COUNCIL).booleanValue()) {
                return true;
            }
            if (reader.hasRole(RoleType.EMPLOYEE).booleanValue() && contact.getVisibleToEmployees().booleanValue()) {
                return true;
            }
            if (reader.hasRole(RoleType.TEACHER).booleanValue() && contact.getVisibleToTeachers().booleanValue()) {
                return true;
            }
            if (reader.hasRole(RoleType.STUDENT).booleanValue() && contact.getVisibleToStudents().booleanValue()) {
                return true;
            }
            if (reader.hasRole(RoleType.ALUMNI).booleanValue() && contact.getVisibleToAlumni().booleanValue()) {
                return true;
            }
            if (contact.getVisibleToPublic()) {
                return true;
            }
        }
        return false;
    }

    protected HtmlComponent getValue(PartyContact contact) {
        HtmlInlineContainer span = new HtmlInlineContainer();
        if (contact instanceof Phone) {
            span.addChild(new HtmlText(((Phone) contact).getNumber()));
        } else if (contact instanceof MobilePhone) {
            span.addChild(new HtmlText(((MobilePhone) contact).getNumber()));
        } else if (contact instanceof EmailAddress) {
            EmailAddress email = (EmailAddress) contact;
            if (isPublicSpace()) {
                HtmlImage img = new HtmlImage();
                img.setSource(RenderUtils.getContextRelativePath("") + "/publico/viewHomepage.do?method=emailPng&amp;email="
                        + email.getExternalId());
                span.addChild(img);
            } else {
                HtmlLink link = new HtmlLink();
                link.setModuleRelative(false);
                link.setContextRelative(false);
                link.setUrl("mailto:" + email.getValue());
                link.setBody(new HtmlText(email.getValue()));
                span.addChild(link);
            }
        } else if (contact instanceof WebAddress) {
            HtmlLink link = new HtmlLink();
            link.setModuleRelative(false);
            link.setContextRelative(false);
            link.setUrl(((WebAddress) contact).getPresentationValue());
            link.setBody(new HtmlText(((WebAddress) contact).getPresentationValue()));
            span.addChild(link);
        }
        if (showType || (showDefault && contact.isDefault())) {
            StringBuilder suffix = new StringBuilder();
            suffix.append(" (");
            if (showType) {
                suffix.append(RenderUtils.getEnumString(contact.getType()));
            }
            if (showDefault && contact.isDefault()) {
                if (showType) {
                    suffix.append(", ");
                }
                suffix.append(RenderUtils.getResourceString(getBundle(), getDefaultLabel()));
            }
            suffix.append(")");
            span.addChild(new HtmlText(suffix.toString()));
        }
        return span;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public boolean isPublicSpace() {
        return publicSpace;
    }

    public void setPublicSpace(boolean publicSpace) {
        this.publicSpace = publicSpace;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public boolean isShowDefaultSuffix() {
        return showDefaultSuffix;
    }

    public void setShowDefaultSuffix(boolean showDefaultSuffix) {
        this.showDefaultSuffix = showDefaultSuffix;
    }

    public boolean isShowTypeSuffix() {
        return showTypeSuffix;
    }

    public void setShowTypeSuffix(boolean showTypeSuffix) {
        this.showTypeSuffix = showTypeSuffix;
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }
}
