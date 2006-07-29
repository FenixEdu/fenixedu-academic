<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.branches.management"/></h3>

<ul style="list-style-type: square;">
	<li><html:link module="/manager" page="<%="/manageBranches.do?method=prepareInsert&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.branch"/></html:link></li>
</ul>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:notPresent name="infoBranchesList">
	<bean:message bundle="MANAGER_RESOURCES" key="label.manager.no.branches"/>
</logic:notPresent>

<logic:present name="infoBranchesList">

	<html:form action="/manageBranches" method="get">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="delete"/>

 		<bean:define id="onclick">
			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.confirm.delete.branches"/>')
		</bean:define>

		<table width="80%" cellpadding="0" border="0">
			<tr>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.name" />
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.code" />
				</th>
				<th class="listClasses-header">
				</th>
			</tr> 
			<logic:iterate id="infoBranch" name="infoBranchesList">			
				<tr>	
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.internalIds" property="internalIds">
							<bean:write name="infoBranch" property="idInternal"/>
						</html:multibox>
					</td>	
					<td class="listClasses">
						<bean:write name="infoBranch" property="name"/>
					</td>			
					<td class="listClasses">
						<bean:write name="infoBranch" property="code"/>
					</td>
					<td class="listClasses">
						<html:link module="/manager" page="<%="/manageBranches.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") %>" paramId="branchId" paramName="infoBranch" paramProperty="idInternal">
							<bean:message bundle="MANAGER_RESOURCES" key="link.edit"/>
						</html:link>
					</td>
	 			</tr>	
	 		</logic:iterate>
		</table>
		
		<br/>
			
  		<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='<%=onclick.toString() %>'>
   			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.delete.selected.branches"/>
  		</html:submit>
	</html:form> 
</logic:present> 	