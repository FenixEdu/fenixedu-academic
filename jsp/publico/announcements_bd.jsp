<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="announcements">
	<h2><bean:message key="message.announcements.not.available" /></h2>
	</logic:empty>
<table border="0" style="text-align: left;">
        <tbody>
            <logic:iterate id="announcement" name="component" property="announcements" >
                <tr>
                    <td>
                        <br>
                        <h2><bean:write name="announcement" property="title"/></h2>
                    </td>
                </tr>
                <tr>
                    <td>
						<bean:write name="announcement" property="information" filter="false"/>
                        <br><br>
                    </td>
                </tr>
                <tr>
                	<td>
                		<bean:message key="label.creationDate" /><bean:write name="announcement" property="creationDateFormatted"/></BR>
                		<bean:message key="label.lastModificationDate" /><bean:write name="announcement" property="lastModifiedDateFormatted"/>
                	</td>
            	</tr>
            </logic:iterate>
        </tbody>
</table>
</logic:present>
<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.announcements.not.available" />
</h4>
</logic:notPresent>