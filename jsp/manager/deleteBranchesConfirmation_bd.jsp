<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3><bean:message key="label.manager.branches.management"/></h3>

<bean:define id="branchesIds" name="branchesIds" scope="request"/>

<span class="error"><html:errors/><p><bean:message key="label.manager.delete.branches.warning"/></p></span>

	<html:form action="/manageBranches" method="get">
		<html:hidden property="degreeId" />
		<html:hidden property="degreeCurricularPlanId" />
		<html:hidden property="method" value="forceDelete"/>
		
		<logic:iterate id="branchesId" name="branchesIds">	
			<bean:define id="branch" name="branchesId" />
			<html:hidden property="internalIds" value="<%= branch.toString() %>" />
		</logic:iterate>

		<html:submit >
			<bean:message key="label.manager.delete.selected.branches"/>
		</html:submit>
	</html:form> 