<%@ page language="java" %>
<%@ page import="DataBeans.gesdis.InfoAnnouncement" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.lang.String" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table border="0" style="text-align: left;">
        <tbody>
            <logic:iterate id="announcement" name="<%= SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST %>" scope="session" >
                <tr>
                    <td>
                        <br>
                        <h2><bean:write name="announcement" property="title"/></h2>
                    </td>
                </tr>
                <tr>
                    <td>
						<bean:write name="announcement" property="information"/>
                        <br><br>
                    </td>
                </tr>
                <tr>
                	<td>
                		<bean:message key="label.creationDate" /><bean:write name="announcement" property="creationDate"/></BR>
                		<bean:message key="label.lastModificationDate" /><bean:write name="announcement" property="lastModifiedDate"/>
                	</td>
            	</tr>
            </logic:iterate>
        </tbody>
</table>