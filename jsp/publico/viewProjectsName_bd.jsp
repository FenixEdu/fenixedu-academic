<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

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
                        <h2>
							<li><html:link page="<%= "/viewSite.do" + "?method=viewProjectStudentGroupsAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" paramId="groupProperties" paramName="infoGroupProperties" paramProperty="idInternal">
								<bean:write name="infoGroupProperties" property="name"/></h2>
							</html:link></li>
							<br>
							<BLOCKQUOTE><bean:write name="infoGroupProperties" property="projectDescription"/></BLOCKQUOTE>
							<br>
                    </td>
                </tr>

            </logic:iterate>
        </tbody>
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.infoGroupPropertiesList.not.available" />
</h4>
</logic:notPresent>