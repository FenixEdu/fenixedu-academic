package ServidorApresentacao.TagLib.assiduousness;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

public class ListagemTag extends TagSupport {

    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String scope = null;

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    /* Table */

    protected String border = "0";

    public String getBorder() {
        return (this.border);
    }

    public void setBorder(String border) {
        this.border = border;
    }

    protected String tabAlign = "center";

    public String getTabAlign() {
        return (this.tabAlign);
    }

    public void setTabAlign(String tabAlign) {
        this.tabAlign = tabAlign;
    }

    protected String tabWidth = null;

    public String getTabWidth() {
        return (this.tabWidth);
    }

    public void setTabWidth(String tabWidth) {
        this.tabWidth = tabWidth;
    }

    protected String tabHeight = null;

    public String getTabHeight() {
        return (this.tabHeight);
    }

    public void setTabHeight(String tabHeight) {
        this.tabHeight = tabHeight;
    }

    protected String fontSize = "3";

    public String getFontSize() {
        return (this.fontSize);
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    /* Header */

    protected String headers = null;

    public String getHeaders() {
        return (this.headers);
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    protected String headBGColor = "DarkBlue";//"#B1BBC5";

    public String getHeadBGColor() {
        return (this.headBGColor);
    }

    public void setHeadBGColor(String headBGColor) {
        this.headBGColor = headBGColor;
    }

    protected String headAlign = "center";

    public String getHeadAlign() {
        return (this.headAlign);
    }

    public void setHeadAlign(String headAlign) {
        this.headAlign = headAlign;
    }

    protected String headFontColor = "#ffffff";//"DarkBlue";

    public String getHeadFontColor() {
        return (this.headFontColor);
    }

    public void setHeadFontColor(String headFontColor) {
        this.headFontColor = headFontColor;
    }

    /* Body */

    protected String body = null;

    public String getBody() {
        return (this.body);
    }

    public void setBody(String body) {
        this.body = body;
    }

    protected String bodyBGColor1 = "#ffffff";//"#ffddaa";

    public String getBodyBGColor1() {
        return (this.bodyBGColor1);
    }

    public void setBodyBGColor1(String bodyBGColor1) {
        this.bodyBGColor1 = bodyBGColor1;
    }

    protected String bodyBGColor2 = "#B1BBD6";//"#ffffcc";

    public String getBodyBGColor2() {
        return (this.bodyBGColor2);
    }

    public void setBodyBGColor2(String bodyBGColor2) {
        this.bodyBGColor2 = bodyBGColor2;
    }

    protected String bodyAlign = "left";

    public String getBodyAlign() {
        return (this.bodyAlign);
    }

    public void setBodyAlign(String bodyAlign) {
        this.bodyAlign = bodyAlign;
    }

    protected String bodyFontColor = "DarkBlue";

    public String getBodyFontColor() {
        return (this.bodyFontColor);
    }

    public void setBodyFontColor(String bodyFontColor) {
        this.bodyFontColor = bodyFontColor;
    }

    public int doStartTag() throws JspException {

        List h = (ArrayList) RequestUtils.lookup(pageContext, name, headers, scope);
        List c = (ArrayList) RequestUtils.lookup(pageContext, name, body, scope);

        StringBuffer results = new StringBuffer("<table ");
        results.append(" border=\"");
        results.append(border);
        results.append("\"");
        results.append(" align=\"");
        results.append(tabAlign);
        results.append("\"");

        if (tabWidth != null) {
            results.append(" width=\"");
            results.append(tabWidth);
            results.append("\"");
        }

        if (tabHeight != null) {
            results.append(" height=\"");
            results.append(tabHeight);
            results.append("\"");
        }

        results.append(">");

        results.append("<tr>");
        int i = 0;
        while (i < h.size()) {
            results.append("<th nowrap");
            results.append(" bgcolor=\"");
            results.append(headBGColor);

            results.append("\" align=\"");
            results.append(headAlign);
            results.append("\">");

            results.append("<font color=\"");
            results.append(headFontColor);
            results.append("\"");
            results.append(" size=\"");
            results.append(fontSize);
            results.append("\">");

            results.append((String) h.get(i));
            results.append("</font>");

            results.append("</th>");
            i++;
        }
        results.append("</tr>");

        i = 0;
        int j = 0;
        String contents;
        String corFundo = bodyBGColor2;

        while (i < c.size()) {

            results.append("<tr");
            results.append(" bgcolor=\"");
            if (corFundo == bodyBGColor2)
                corFundo = bodyBGColor1;
            else
                corFundo = bodyBGColor2;

            results.append(corFundo);
            results.append("\">");

            while (j < h.size()) {
                contents = (String) c.get(i);

                results.append("<td nowrap");
                results.append(" align=\"");
                results.append(bodyAlign);
                results.append("\">");
                results.append("<font color=\"");
                results.append(bodyFontColor);
                results.append("\"");
                results.append(" size=\"");
                results.append(fontSize);
                results.append("\">");
                //results.append((contents.get(j)).toString());
                results.append(contents);
                results.append("</font>");

                results.append("</td>");
                i++;
                j++;
            }
            results.append("</tr>");
            //i=j;
            j = 0;
        }
        //   results.append("</tr>");
        ResponseUtils.write(pageContext, results.toString());

        return (SKIP_BODY);
    }

    public int doEndTag() throws JspException {
        StringBuffer results = new StringBuffer("</table>");

        ResponseUtils.write(pageContext, results.toString());

        return (EVAL_PAGE);

    }

    public void release() {
        super.release();
        name = null;
        scope = null;
        tabAlign = "center";
        tabWidth = null;
        tabHeight = null;
        headers = null;
        headBGColor = "DarkBlue";
        headAlign = "center";
        headFontColor = "#ffffff";
        body = null;
        bodyBGColor1 = "#ffffff";
        bodyBGColor2 = "#B1BBD6";
        bodyAlign = "center";
        bodyFontColor = "DarkBlue";
    }
}