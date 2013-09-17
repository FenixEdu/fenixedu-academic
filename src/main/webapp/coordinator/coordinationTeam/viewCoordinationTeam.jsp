<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />


<logic:present name="coordinators">

<h2><bean:message key="title.coordinationTeam"/></h2>

<html:form action="/viewCoordinationTeam">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeCoordinators" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>



<table class="tstyle1">
<tr>
<th><bean:message key="label.coordinator.name"/></th>
	<th>
   <logic:equal name="isResponsible" value="true">
   <bean:message key="label.remove"/>
   </logic:equal>
   &nbsp;</th>
</tr>
<logic:iterate name="coordinators" id="coordinator">
<tr>
	<td>
		<bean:write name="coordinator" property="infoPerson.nome" /> 
	</td>
	<td class="acenter">
   		<logic:equal name="coordinator" property="responsible" value="true">
			<bean:message key="label.responsible"/>
		</logic:equal> 
		
		<logic:notEqual name="coordinator" property="responsible" value="true">
			<logic:equal name="isResponsible" value="true">	
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.coordinatorsIds" property="coordinatorsIds">
					<bean:write name="coordinator" property="externalId"/>
				</html:multibox >
			</logic:equal>&nbsp;
		</logic:notEqual>
	</td>
</tr>
</logic:iterate>
</table>

<bean:define id="infoExecutionDegreeId" name="infoExecutionDegreeId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoExecutionDegreeId" property="infoExecutionDegreeId" value="<%= infoExecutionDegreeId.toString() %>"/>
<logic:equal name="isResponsible" value="true">	

<p>
	<html:submit><bean:message key="label.remove"/></html:submit>
</p>


<ul class="mtop2">
	<li>
		<html:link 
		page="<%= "/viewCoordinationTeam.do?method=prepareAddCoordinator&infoExecutionDegreeId="+ 
		infoExecutionDegreeId.toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>" >
		<bean:message key="title.addCoordinator"/>
		</html:link>
	</li>
</ul>


</logic:equal>	
</html:form>
</logic:present>



