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
package net.sourceforge.fenixedu.presentationTier.TagLib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyTag extends TagSupport {

    private static final Logger logger = LoggerFactory.getLogger(PropertyTag.class);

    private String name = null;

    private String value = null;

    public PropertyTag() {
    }

    @Override
    public void release() {
        super.release();

        this.name = null;
        this.value = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int doStartTag() throws JspException {
        PropertyContainerTag parent = (PropertyContainerTag) findAncestorWithClass(this, PropertyContainerTag.class);

        if (parent != null) {
            parent.addProperty(getName(), getValue());
        } else {
            logger.warn("property tag was using inside an invalid containerCOULD NOT SET PROPERTY");
            logger.warn("could not set property: " + getName() + "=" + getValue());
        }

        return SKIP_BODY;
    }
}
