<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<html:messages id="messages" message="true">
	<span class="error"><bean:write name="messages" /></span>
</html:messages>

<html:form action="/executionDegreesManagement">
	<html:hidden property="method" value="insertCoordinator"/>
	<bean:define id="executionDegreeID" name="executionDegree" property="idInternal" />
	<html:hidden property="executionDegreeID" value="<%= executionDegreeID.toString() %>"/>
	
	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.associate.teachers.in.charge"/></h3>
	<h3><b><bean:write name="executionDegree" property="degreeCurricularPlan.degree.sigla"/>-<bean:write name="executionDegree" property="degreeCurricularPlan.degree.nome"/> (<bean:write name="executionDegree" property="executionYear.year"/>)</b></h3>
	
	<div class='simpleblock4'>
		<p>	
		<bean:message bundle="MANAGER_RESOURCES" key="label.coordinator.number"/>:
		<html:text size="5" property="coordinatorNumber" />
		</p>
	</div>
	
	<html:submit styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="button.save"/></html:submit>
</html:form>