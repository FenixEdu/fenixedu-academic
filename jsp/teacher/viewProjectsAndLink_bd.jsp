<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoGroupPropertiesList">
	<h2><bean:message key="message.infoGroupPropertiesList.not.available" /></h2>
	</logic:empty>
<table border="0" style="text-align: left;">
        <tbody>
            <logic:iterate id="infoGroupProperties" name="component" property="infoGroupPropertiesList" >
                <tr>
                    <td>
                        <br>
                        <h2><li><html:link page="<%= "/viewProjectStudentGroups.do?method=viewProjectStudentGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="groupProperties" paramName="infoGroupProperties" paramProperty="idInternal">
							<bean:write name="infoGroupProperties" property="name"/></html:link></li></h2>
                    </td>
                </tr>

            </logic:iterate>
            
            </tbody>
</table>
<h2><li><html:link page="/createGroupProperties.do?method=prepareCreateGroupProperties" paramId="objectCode" paramName="objectCode" ><bean:message key="link.groupPropertiesDefintion"/></html:link></li></h2>
       
</logic:present>

<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.infoGroupPropertiesList.not.available" />
</h4>
</logic:notPresent>