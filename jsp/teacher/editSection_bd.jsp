<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<logic:present name="siteView">
<bean:define id="sections" name="siteView" property="component"/>
<bean:define id="section" name="sections" property="section"/>

<h2><bean:message key="button.editSection" /></h2>
<span class="error"><!-- Error messages go here --><html:errors property="error.default"/></span>
<html:form action="/editSection">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
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
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" name="section" property="name" />
			<span class="error"><!-- Error messages go here --><html:errors property="name"/></span>
		</td>
	</tr>
	<tr>
	<logic:present name="sections" property="sections">
	<td>		
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<bean:define id="sectionOrder" name="section" property="sectionOrder" type="java.lang.Integer"/>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.sectionOrder" name="section" property="sectionOrder" size="1">
			<logic:iterate id="otherSection" name="sections" property="sections" type="net.sourceforge.fenixedu.dataTransferObject.InfoSection">
				<bean:define id="otherSectionOrder" type="java.lang.String"><%= String.valueOf(otherSection.getSectionOrder().intValue()) %></bean:define>
				<bean:define id="selected" value=""></bean:define>
				<logic:equal name="otherSectionOrder" value="<%= String.valueOf(sectionOrder.intValue() + 1) %>">
					<bean:define id="selected">selected="selected"</bean:define>
					<bean:define id="isSelected">yes</bean:define>
				</logic:equal>
				<option value="<bean:write name='otherSection' property='sectionOrder'/>" <bean:write name="selected"/>><bean:write name="otherSection" property="name"/></option>
			</logic:iterate>
			<bean:define id="selected" value=""></bean:define>
			<logic:notPresent name="isSelected">
					<bean:define id="selected">selected="selected"</bean:define>
			</logic:notPresent>
				<option value="-1" <bean:write name="selected"/>><bean:message key="label.end" /></option>
		</html:select>
		<span class="error"><!-- Error messages go here --><html:errors property="sectionOrder"/></span>
	</td>
	</logic:present>
	<logic:notPresent name="sections">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.sectionOrder" property="sectionOrder" value="0"/>
	</logic:notPresent>
</tr>
</table>
<br />

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editSection" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<bean:define id="currentSectionCode" name="section" property="idInternal"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.currentSectionCode" property="currentSectionCode" value="<%= currentSectionCode.toString() %>" />

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
</html:form>
</logic:present>