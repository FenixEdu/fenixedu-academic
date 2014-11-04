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
/*
 * Created on Apr 3, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.renderers.ExamsMapContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.renderers.ExamsMapSlotContentRenderer;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class RenderExamsMapTag extends TagSupport {

    private static final Logger logger = LoggerFactory.getLogger(RenderExamsMapTag.class);

    // Name of atribute containing ExamMap
    private String name;

    private String user;

    private String mapType;

    private ExamsMapSlotContentRenderer examsMapSlotContentRenderer = new ExamsMapContentRenderer();

    @Override
    public int doStartTag() throws JspException {
        // Obtain InfoExamMap
        InfoExamsMap infoExamsMap = null;
        InfoRoomExamsMap infoRoomExamsMap = null;
        ExamsMap examsMap = null;
        IExamsMapRenderer renderer = null;
        String typeUser = "";
        String typeMapType = "";
        Locale locale = (Locale) pageContext.findAttribute(Globals.LOCALE_KEY);

        try {
            infoExamsMap = (InfoExamsMap) pageContext.findAttribute(name);
            typeUser = user;
            typeMapType = mapType;
            examsMap = new ExamsMap(infoExamsMap, locale);
            renderer = new ExamsMapRenderer(examsMap, this.examsMapSlotContentRenderer, typeUser, typeMapType, locale);
        } catch (ClassCastException e) {
            infoExamsMap = null;
        }
        try {
            infoRoomExamsMap = (InfoRoomExamsMap) pageContext.findAttribute(name);

            typeUser = user;
            examsMap = new ExamsMap(infoRoomExamsMap, locale);
            renderer = new ExamsMapForRoomRenderer(examsMap, this.examsMapSlotContentRenderer, typeUser);
        } catch (ClassCastException e) {
            infoRoomExamsMap = null;
        }
        if (infoExamsMap == null && infoRoomExamsMap == null) {
            throw new JspException(messages.getMessage("generateExamsMap.infoExamsMap.notFound", name));
        }

        // Generate Map from infoExamsMap
        JspWriter writer = pageContext.getOut();
        // ExamsMap examsMap = new ExamsMap(infoExamsMap);

        // ExamsMapRenderer renderer =
        // new ExamsMapRenderer(
        // examsMap,
        // this.examsMapSlotContentRenderer,
        // typeUser);

        try {
            writer.print(renderer.render(locale, pageContext));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new JspException(messages.getMessage("generateExamsMap.io", e.toString()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return (SKIP_BODY);
    }

    @Override
    public int doEndTag() {
        return (EVAL_PAGE);
    }

    @Override
    public void release() {
        super.release();
    }

    // Error Messages
    protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String string) {
        user = string;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String string) {
        mapType = string;
    }
}