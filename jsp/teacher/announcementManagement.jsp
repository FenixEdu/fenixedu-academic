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
                    <html:submit property="option" value="Insert"/>&nbsp;&nbsp;<bean:message key="label.insertAnnouncement" />
                    <br><br><hr>
                </td>
            </tr>
            <logic:iterate id="announcement" name="Announcements">
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
                		<bean:message key="label.lastModificationDate" /> <bean:write name="announcement" property="lastModificationDate"/>
						<br><br>
                	</td>
            	</tr>
                <tr>
                    <td>
                        <html:submit indexed="true" property="method">
                        	<bean:message key="button.edit"/>                    		     
                        </html:submit>
                        <html:submit indexed="true" property="method">
                        	<bean:message key="button.delete"/>                    		     
                        </html:submit>
                    </td>
                </tr>
            </logic:iterate>
        </tbody>
    </html:form>
</table>