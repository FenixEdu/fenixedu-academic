<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<html:form action="/editSection">

<table align="center">
	<tr>
		<td>
			<bean:message key="message.sectionName"/>
		</td>
		<td>
			<html:textarea rows="2" cols="50" name="<%=SessionConstants.INFO_SECTION %>" property="name" />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="message.sectionOrder"/>
		</td>
		<td>
			<html:text size="5" name="<%=SessionConstants.INFO_SECTION %>" property="sectionOrder"/>
		</td>
	</tr>
<%--
	<tr>
		<td>
			<bean:message key="message.parentSection"/>
		</td>
		<td>
			<html:select property="parentSection" >
				<html:options collection="ALL_SECTIONS" property="name" />
			</html:select>
		</td>
	</tr>

	<tr>
		<td>
			<app:generateSectionMenu name="ALL_SECTIONS" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="<%= SessionConstants.INFO_SECTION %>" renderer="sectionChooser" />
		</td>
	</tr>	

	<% int index = 0; %>
	<logic:iterate id="section" name="ALL_SECTIONS" />
		<tr>
			<td>
			  	<bean:define id="depth" name="section" property="sectionDepth"/>
					<% 
						int i = -1; 
						while(i< ((Integer) pageContext.findAttribute("depth")).intValue()){
					%>
					&nbsp
					<% 
						}
						i++;
					%>
					<a href="editSection.do?method=changeParent&amp;index=<%= index %>" >
						<bean:write name="section" property="name" />
					</a>
			</td>
		</tr>	
	<% index++; %>					
	</logic:iterate>
	--%>
	<tr>
		<td>
			<html:reset  styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>			
		</td>
		<td>
			<html:submit styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
		</td>
	</tr>
</table>

<html:hidden property="method" value="edit" />

</html:form>
