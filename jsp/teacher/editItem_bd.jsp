<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<span class="error"><html:errors property="error.default" /></span>

<logic:present name="siteView">
<bean:define id="infoSiteItems" name="siteView" property="component"/>
<bean:define id="item" name="infoSiteItems" property="item"/>
<bean:define id="items" name="infoSiteItems" property="items"/>
	
<html:form action="/editItem">
	<html:hidden property="page" value="1"/>
<table align="center">
<tr>
	<td>
		<bean:message key="message.itemName"/>
	</td>
	<td>
		<html:text name="item" property="name"/> <span class="error"><html:errors property="name"/></span>
	</td>
</tr>

<tr>
	<td>
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<html:select property="itemOrder" size="1">
			<bean:define id="items" name="items" />
			<html:option value="-1"><bean:message key="label.end"/></html:option>	
			<html:options collection="items" labelProperty="name" property="itemOrder" />
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
				<html:option value="false"><bean:message key="label.no"/></html:option>
				<html:option value="true"><bean:message key="label.yes"/></html:option>
		</html:select>
		<span class="error"><html:errors property="urgent"/></span>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.itemInformation"/>
	</td>
	<td>
		<html:textarea name="item" property="information" rows="8" cols="40"/>
		<span class="error"><html:errors property="information"/></span>
	</td>
</tr>
</table>			

<html:hidden property="method" value="editItem" />
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<bean:define id="itemCode" name="item" property="idInternal"/>
<html:hidden property="itemCode" value="<%= itemCode.toString() %>" />
<bean:define id="currentSection" name="item" property="infoSection"/>
<bean:define id="currentSectionCode" name="currentSection" property="idInternal"/>
<html:hidden property="currentSectionCode" value="<%= currentSectionCode.toString() %>" />
	

<html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
</html:form>
</logic:present>