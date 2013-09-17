<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.branches.management"/></h3>

<bean:define id="branchesIds" name="branchesIds" scope="request"/>

<span class="error"><!-- Error messages go here --><html:errors /><p><bean:message bundle="MANAGER_RESOURCES" key="label.manager.delete.branches.warning"/></p></span>

	<html:form action="/manageBranches" >
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="forceDelete"/>
		
		<logic:iterate id="branchesId" name="branchesIds">	
			<bean:define id="branch" name="branchesId" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.internalIds" property="internalIds" value="<%= branch.toString() %>" />
		</logic:iterate>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" >
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.delete.selected.branches"/>
		</html:submit>
	</html:form> 