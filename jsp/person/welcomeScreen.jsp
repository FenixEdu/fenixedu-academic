<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>

<logic:notEmpty name="userView" property="person.user.lastLoginHost">
	<bean:define id="lastLoginDateTime" type="java.util.Date" name="userView" property="person.user.lastLoginDateTime"/>
	<bean:define id="timestamp" ><%= lastLoginDateTime.getTime() %></bean:define>
	<bean:define id="lastLoginHost" name="userView" property="person.user.lastLoginHost"/>
    <p class="mtop0 mbottom2"><span style="background-color: #eee; padding: 0.25em;"><bean:message key="last.login.dateTime"/>&nbsp;<b><date:format pattern="dd-MM-yyyy HH:mm"><bean:write name="timestamp"/></date:format></b> (<bean:write name="lastLoginHost"/>)</span></p>	
</logic:notEmpty>

<logic:present name="<%= SessionConstants.LIST_ADVISORY %>" scope="request">
	<bean:size id="numberAdvisories" name="<%= SessionConstants.LIST_ADVISORY %>" scope="request"/>
	<p><bean:message key="label.have"/>
	<span class="emphasis"><bean:write name="numberAdvisories"/></span>
	<bean:message key="label.advisories"/></p>
	<logic:notEqual name="numberAdvisories" value="0">
	<table width="90%" cellspacing="0" cellpadding="0" style="border: 1px solid #333;">
		<tr>
			<td colspan="3" style="background: #333; color:#fff; padding: 5px 0 5px 10px; border: 1px solid #333;"><strong>AVISOS</strong></td>
		</tr>
		<logic:iterate id="advisory" name="<%= SessionConstants.LIST_ADVISORY %>" scope="request">
			<bean:define id="activeAdvisory" name="activeAdvisory"/>
			<logic:equal name="advisory" property="idInternal" value="<%= activeAdvisory.toString() %>">
				<tr>
					<td colspan="3" style="background: #EBECED; padding: 10px 0 0 10px;"><strong><bean:message key="label.from"/>:</strong> <bean:write name="advisory" property="sender"/></td>
				</tr>
				<tr>
					<td colspan="3" style="background: #EBECED; padding: 5px 0 0 10px"><strong><bean:message key="label.sendDate"/>:</strong> <date:format pattern="yyyy.MM.dd"><bean:write name="advisory" property="created.time"/></date:format></td
				</tr>
				<tr>
					<td colspan="3" style="background: #EBECED; padding: 5px 0 0 10px"><strong><bean:message key="label.subject"/>:</strong> <bean:write name="advisory" property="subject" filter="false"/><td>
				</tr>
				<tr>
					<td colspan="3" style="background: #EBECED; padding: 10px; border-bottom: 1px solid #333;"><bean:write name="advisory" property="message" filter="false" /></td>
				</tr>
			</logic:equal>
			<logic:notEqual name="advisory" property="idInternal" value="<%= activeAdvisory.toString() %>">
				<tr>
					<td width="25%" style="background: #ccc; border-bottom: 1px solid #333; padding: 5px 0 5px 10px"><bean:write name="advisory" property="sender"/></td>
					<td width="60%" style="background: #fff; border-bottom: 1px solid #333; padding: 5px 0 5px 10px"><html:link page="/index.do" paramId="activeAdvisory" paramName="advisory" paramProperty="idInternal"><bean:message key="label.subject"/> <bean:write name="advisory" property="subject"/> </html:link></td>
					<td width="15%" style="background: #ccc; border-bottom: 1px solid #333; padding: 5px 0 5px 10px"><%--<bean:message key="label.sendDate"/>--%> <date:format pattern="yyyy.MM.dd"><bean:write name="advisory" property="created.time"/></date:format></td>
				</tr>	
			</logic:notEqual>
		</logic:iterate>
	</table>
	</logic:notEqual>
</logic:present>
<p>Bem-vindo ï¿? sua ï¿?rea pessoal.<br /> Poderï¿? visualizar e alterar a sua informação pessoal, proceder ï¿? alteraï¿?ï¿?o da sua password ler os avisos que lhe sï¿?o enviados pelos orgï¿?os de gestï¿?o da escola.</p>
