<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3><bean:message key="label.manager.branches.management"/></h3>

<ul style="list-style-type: square;">
	<li><html:link page="<%="/manageBranches.do?method=prepareInsert&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>"><bean:message key="label.manager.insert.branch"/></html:link></li>
</ul>

<span class="error"><html:errors/></span>

<logic:notPresent name="infoBranchesList">
	<bean:message key="label.manager.no.branches"/>
</logic:notPresent>

<logic:present name="infoBranchesList">

	<html:form action="/manageBranches" method="get">
		<html:hidden property="degreeId" />
		<html:hidden property="degreeCurricularPlanId" />
		<html:hidden property="method" value="delete"/>

 		<bean:define id="onclick">
			return confirm('<bean:message key="message.confirm.delete.branches"/>')
		</bean:define>

		<table width="80%" cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header">
				</td>
				<td class="listClasses-header"><bean:message key="label.name" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.code" />
				</td>
				<td class="listClasses-header">
				</td>
			</tr> 
			<logic:iterate id="infoBranch" name="infoBranchesList">			
				<tr>	
					<td class="listClasses">
						<html:multibox property="internalIds">
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
						<html:link page="<%="/manageBranches.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") %>" paramId="branchId" paramName="infoBranch" paramProperty="idInternal">
							<bean:message key="link.edit"/>
						</html:link>
					</td>
	 			</tr>	
	 		</logic:iterate>
		</table>
		
		<br>
			
  		<html:submit onclick='<%=onclick.toString() %>'>
   			<bean:message key="label.manager.delete.selected.branches"/>
  		</html:submit>
	</html:form> 
</logic:present> 	