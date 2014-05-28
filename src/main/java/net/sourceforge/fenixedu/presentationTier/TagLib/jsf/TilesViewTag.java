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
package net.sourceforge.fenixedu.presentationTier.TagLib.jsf;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.taglib.tiles.InsertTag;
import org.apache.struts.taglib.tiles.PutTag;

import com.sun.faces.taglib.jsf_core.ViewTag;

public class TilesViewTag extends ViewTag {

    // This is the order in which the methods are called.
    // It's just a usefull reference.
    // setPageContext
    // setParent
    // doStartTag
    // getRendererType
    // getParent
    // setBodyContent
    // doInitBody
    // getComponentInstance
    // getComponentInstance
    // doAfterBody
    // getPreviousOut
    // getBodyContent
    // getPreviousOut
    // doEndTag

    private String definition = null;
    private String attributeName = null;

    public void setDefinition(final String definition) {
        this.definition = definition;
    }

    public void setAttributeName(final String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public int doAfterBody() throws JspException {
        final BodyContent bodyContent = getBodyContent();
        final String bodyContentString = bodyContent.getString();

        try {
            // pageContext.getOut().flush();
            pageContext.getOut().clearBuffer();
            doInsertTag(pageContext, this, definition, bodyContentString);
        } catch (IOException e) {
            throw new JspException(e);
        }

        return super.doAfterBody();
    }

    private void doInsertTag(PageContext pageContext, Tag parent, String definition, String body) throws JspException {
        final InsertTag insertTag = new InsertTag();
        insertTag.setPageContext(pageContext);
        insertTag.setParent(parent);
        insertTag.setDefinition(definition);
        insertTag.setFlush(false);

        int _jspx_eval_tiles_insert_0 = insertTag.doStartTag();
        if (_jspx_eval_tiles_insert_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                /* ---- tiles:put ---- */
                final PutTag putTag = new PutTag();
                putTag.setPageContext(pageContext);
                putTag.setParent(insertTag);
                putTag.setName(attributeName);
                putTag.setValue(body);
                putTag.doStartTag();
                if (putTag.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    return;
                }
                int evalDoAfterBody = insertTag.doAfterBody();
                if (evalDoAfterBody != IterationTag.EVAL_BODY_AGAIN) {
                    break;
                }
            } while (true);
        }
        if (insertTag.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            return;
        }
    }

}
