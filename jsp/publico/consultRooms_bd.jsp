<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.search.rooms"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<br />
<table width="100%">
	<tr>
		<td class="infoop"><bean:message key="message.search.rooms" /></td>
	</tr>
</table>
<html:form action="/chooseRoomsForm" method="get">
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>"  property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />

    <table border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td class="tall-td"><bean:message key="property.room.name"/>:</td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="11" maxlength="20"/></td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.building"/>:</td>
            <td>
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.building" property="building" size="1">
                    <html:options collection="publico.buildings" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.floor"/>:</td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.floor" property="floor" size="2" maxlength="2"/></td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.type"/>:</td>
            <td>
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.type" property="type" size="1">
                	<html:options collection="publico.types" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.capacity.normal"/>:</td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityNormal" property="capacityNormal" size="3" maxlength="4"/></td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.capacity.exame"/>:</td>
            <td height="0"><html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityExame" property="capacityExame" size="3" maxlength="4"/></td>
        </tr>
    </table>
    <br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.submit"/></html:submit>
    <html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form>