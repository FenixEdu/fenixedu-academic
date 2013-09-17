<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2><bean:message key="link.gep.teachersInformation" bundle="GEP_RESOURCES"/></h2>
<span class="error"><!-- Error messages go here -->
	<html:errors bundle="GEP_RESOURCES"/>
</span>
<logic:equal name="showNextSelects" value="false">
	<html:form action="/searchTeachersInformation">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="searchForm"/>
		<table>
			<tr>
				<td>
					<b><bean:message key="title.gep.executionYear" bundle="GEP_RESOURCES"/>:</b>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYear" property="executionYear" onchange="this.form.submit();">
						<html:option value="[Escolha]"/>
						<html:optionsCollection name="executionYears"/>
					</html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
</logic:equal>
<logic:equal name="showNextSelects" value="true">
	<bean:define id="executionYear"><%=pageContext.findAttribute("executionYear")%></bean:define>
	<table width="90%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<p>
					<strong><bean:message key="title.gep.executionYear"
										  bundle="GEP_RESOURCES"/>:</strong>
					<bean:write name="executionYear"/>
				</p>			
			</td>
		</tr>
	</table>

	<html:form action="/searchTeachersInformation">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doSearch"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear" value="<%=executionYear%>"/>
		<table>
			<tr>
				<td>
					<b><bean:message key="label.gep.degree" bundle="GEP_RESOURCES"/>:</b>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeId" property="executionDegreeId">
						<html:option key="label.selectAll" bundle="GEP_RESOURCES" value="all"/>
						<html:options collection="executionDegrees" labelProperty="presentationName" property="externalId"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<br />
					<b><bean:message key="label.gep.courseInformation.type" bundle="GEP_RESOURCES"/>:</b>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.gep.basic" bundle="GEP_RESOURCES"/>
				</td>
				<td>
					<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.basic" property="basic" value="true"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.gep.non.basic" bundle="GEP_RESOURCES"/>
				</td>
				<td>
					<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.basic" property="basic" value="false"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.selectAll" bundle="GEP_RESOURCES"/>
				</td>
				<td>
					<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.basic" property="basic" value=""/>
				</td>
			</tr>
		</table>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.show"
					  bundle="GEP_RESOURCES"/>
	</html:submit>
	</html:form>
</logic:equal>
