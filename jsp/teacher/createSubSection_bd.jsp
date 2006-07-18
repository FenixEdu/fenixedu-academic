<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<span class="error"><html:errors property="error.default" /></span>
<h2>Inserir Sub-Secção</h2>
<br />

<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<logic:present name="component" property="rootSections">
	<bean:define id="sections" name="component" property="rootSections"/>
</logic:present>
<logic:present name="component" property="regularSections">
	<bean:define id="sections" name="component" property="regularSections"/>
</logic:present>

<html:form action="/createSection">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
<table>
<tr>
	<td>
		<bean:message key="message.sectionName"/>
	</td>
	<td>
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" />
			<span class="error"><html:errors property="name"/></span>
	</td>
</tr>
<tr>
	<logic:present name="sections">
	<td>		
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.sectionOrder" name="sectionForm" property="sectionOrder">
			<html:option value="-1"><bean:message key="label.end"/></html:option>
			<html:options collection="sections" labelProperty="name" property="sectionOrder"/>
			
		</html:select>
		<span class="error"><html:errors property="sectionOrder"/></span>
	</td>
	</logic:present>
	<logic:notPresent name="sections">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.sectionOrder" property="sectionOrder" value="0"/>
	</logic:notPresent>
</tr>
</table>
<br />

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createSection"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

<logic:present name="component" property="regularSections">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.currentSectionCode" property="currentSectionCode" value="<%= pageContext.findAttribute("currentSectionCode").toString() %>" />
</logic:present>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
</html:form>
</logic:present>