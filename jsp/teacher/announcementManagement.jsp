<%@ page language="java" %>
<%@ page import="DataBeans.gesdis.InfoAnnouncement" %>
<%@ page import="java.lang.String" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table border="0" style="text-align: left;">
    <html:form action="/announcementManagementAction">
        <tbody>
            <tr>
                <td>
                    <br>
                    <html:submit property="method" value="createAnnouncement" styleClass="inputbutton"/>
	                <bean:message key="label.insertAnnouncement" />
                    &nbsp;&nbsp;
                    <br><br><hr>
                </td>
            </tr>
            <logic:iterate id="announcement" name="Announcements" scope="session" >
                <tr>
                    <td>
                        <br>
                        <h2><bean:write name="announcement" property="title"/></h2>
						<bean:write name="announcement" property="creationDate"/>
                    </td>
                </tr>
                <tr>
                    <td>
                         <% String title =  ((InfoAnnouncement) announcement).getInformation();
	                        title = title.replaceAll("\n","<br>");
                            out.println(title);
                          %>
                        <br><br>
                    </td>
                </tr>
                <tr>
                	<td>
                		<bean:message key="label.lastModificationDate" /><bean:write name="announcement" property="lastModifiedDate"/>
						<br><br>
                	</td>
            	</tr>
                <tr>
                    <td>
                        <html:submit indexed="true" property="method" value="prepareEditAnnouncement" styleClass="inputbutton">
                        	<bean:message key="button.edit"/>
                        </html:submit>
                        <html:submit indexed="true" property="method" value="deleteAnnouncement" styleClass="inputbutton">
                        	<bean:message key="button.delete"/>
                        </html:submit>
                    </td>
                </tr>
            </logic:iterate>
        </tbody>
    </html:form>
</table>