<%@ page language="java" %>
<%@ page import="DataBeans.gesdis.InfoAnnouncement" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<br />
<table width="100%">
<tr>
<td class="infoop"><bean:message key="label.announcement.explanation" /></td>
</tr>
</table>
<br />
<table width="100%">
    <html:form action="/announcementManagementAction">
            <tr>
                <td>
					<div class="gen-button"><html:link page="/announcementManagementAction.do?method=prepareCreateAnnouncement">
						<bean:message key="label.insertAnnouncement" />
					</html:link></div>
					<br />
					<br />
                </td>
            </tr>
            <% int index = 0; %>
            <logic:iterate id="announcement" name="<%= SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST %>" scope="session" >
                <tr>
                    <td>
                        <strong><bean:write name="announcement" property="title"/></strong>
                    </td>
                </tr>
               	<tr>
                    <td>
						<span class="px9"><bean:write name="announcement" property="creationDate"/> | <bean:message key="label.lastModificationDate" /><bean:write name="announcement" property="lastModifiedDate"/></span>
                    </td>
                </tr>
                <tr>
                    <td>
						<bean:write name="announcement" property="information" filter="false"/>
                        <br />
                        <br />
                    </td>
                </tr>
                <tr>
                    <td>
						<div class="gen-button"><html:link page="/announcementManagementAction.do?method=prepareEditAnnouncement" indexId="index" indexed="true"><bean:message key="button.edit" />
						</html:link></div>
						<div class="gen-button"><html:link page="/announcementManagementAction.do?method=deleteAnnouncement" indexId="index" indexed="true"><bean:message key="button.delete" />
						</html:link></div>
    	                <br />
    	                <br />
                    </td>
                </tr>
             <% index++; %>
            </logic:iterate>
    </html:form>
</table>