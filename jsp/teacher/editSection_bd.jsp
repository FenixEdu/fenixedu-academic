<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<h2><bean:message key="button.editSection" /></h2>
<span class="error"><html:errors property="error.default"/></span>
<html:form action="/editSection">
<html:hidden property="page" value="1"/>
<table>
	<tr>
		<td>
			<bean:message key="message.sectionName"/>
		</td>
		<td>
			<html:text name="<%=SessionConstants.INFO_SECTION %>" property="name" />
			<span class="error"><html:errors property="name"/></span>
		</td>
	</tr>
	<tr>
	<logic:present name="<%= SessionConstants.CHILDREN_SECTIONS %>">
	<td>		
		<bean:message key="message.sectionOrder"/>		
	</td>
	<td>
		<html:select name="sectionForm" property="sectionOrder">
			<html:options collection="<%= SessionConstants.CHILDREN_SECTIONS %>" property="name"/>
			<html:option value="-1">(Fim)</html:option>
		</html:select>
			<span class="error"><html:errors property="sectionOrder"/></span>
	</td>
	</logic:present>
	<logic:notPresent name="<%= SessionConstants.CHILDREN_SECTIONS %>">
		<html:hidden property="sectionOrder" value="0"/>
	</logic:notPresent>
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
</table>
<br />
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>			
<html:hidden property="method" value="edit" />
</html:form>
