<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<logic:present name="siteView">
<bean:define id="sections" name="siteView" property="component"/>
<bean:define id="section" name="sections" property="section"/>

<h2><bean:message key="button.editSection" /></h2>
<span class="error"><html:errors property="error.default"/></span>
<html:form action="/editSection">
<html:hidden property="page" value="1"/>
<table>
	<tr>
		<td>
			<br />
			<br />
			<bean:message key="message.sectionName"/>
		</td>
		<td>
			<br />
			<br />
			<html:text name="section" property="name" />
			<span class="error"><html:errors property="name"/></span>
		</td>
	</tr>
	<tr>
	<logic:present name="sections" property="sections">
	<td>		
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<bean:define id="sectionsList" name="sections" property="sections"/>
		<html:select name="sectionForm" property="sectionOrder">
			<html:options collection="sectionsList" property="sectionOrder" labelProperty="name"/>
			<html:option value="-1"><bean:message key="label.end"/></html:option>
		</html:select>
			<span class="error"><html:errors property="sectionOrder"/></span>
	</td>
	</logic:present>
	<logic:notPresent name="sections">
		<html:hidden property="sectionOrder" value="0"/>
	</logic:notPresent>
</tr>
</table>
<br />

<html:hidden property="method" value="editSection" />
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<bean:define id="currentSectionCode" name="section" property="idInternal"/>
<html:hidden property="currentSectionCode" value="<%= currentSectionCode.toString() %>" />

<html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit>
<html:reset styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
</html:form>
</logic:present>