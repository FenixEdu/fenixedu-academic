<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors property="error.default" /></span>
<h2><bean:message key="message.insertItem" /></h2>
<br />

<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="itemsList" name="component" property="items"/>
	
<html:form action="/insertItem">
<html:hidden property="page" value="1"/>
<table>
	<tr>
		<td>
			<bean:message key="message.itemName"/>
		</td>
		<td>
			<html:text property="name" /><span class="error"><html:errors property="name"/></span>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="message.itemInformation"/>
		</td>
		<td>
			<html:textarea rows="15" cols="45" property="information"/>
			<span class="error"><html:errors property="information"/></span>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="message.sectionOrder"/>	
		</td>
		<td>
			<html:select property="itemOrder" size="1">
				<html:option value="-1"><bean:message key="label.end"/></html:option>
				<html:options collection="itemsList" labelProperty="name" property="itemOrder" />
			
			</html:select>
			<span class="error"><html:errors property="itemOrder"/></span>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="message.itemUrgent"/>
		</td>
		<td>
			<html:select property="urgent" size="1" >	
				<html:option key="label.no" value="false"></html:option>
				<html:option key="label.yes" value="true"></html:option>
			</html:select>
			<span class="error"><html:errors property="urgent"/></span>
		</td>
	</tr>
</table>
<br />

<html:hidden property="method" value="insertItem" />
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<bean:define id="section" name="component" property="section"/>	
<bean:define id="sectionCode" name="section" property="idInternal"/>	
<html:hidden property="currentSectionCode" value="<%= sectionCode.toString() %>" />

<html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
</html:form>
</logic:present>