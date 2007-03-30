<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />


<logic:present name="coordinators">
<h3><bean:message key="title.coordinationTeam"/></h3>
<html:form action="/viewCoordinationTeam">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeCoordinators" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
<table>
<tr>
<th class="listClasses-header"><bean:message key="label.coordinator.name"/></th>
	<th class="listClasses-header">
   <logic:equal name="isResponsible" value="true">
   <bean:message key="label.remove"/>
   </logic:equal>
   &nbsp;</th>
</tr>
<logic:iterate name="coordinators" id="coordinator">
<tr>
	<td class="listClasses"><bean:write name="coordinator" property="infoPerson.nome" /> 
	
	</td>
    
	<td class="listClasses">
  	
   		<logic:equal name="coordinator" property="responsible" value="true">
		<bean:message key="label.responsible"/>
		</logic:equal> 
		<logic:notEqual name="coordinator" property="responsible" value="true">
			<logic:equal name="isResponsible" value="true">	
			 <html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.coordinatorsIds" property="coordinatorsIds">
			 	<bean:write name="coordinator" property="idInternal"/> 
			 </html:multibox >
			 </logic:equal>&nbsp;
			 
		</logic:notEqual>
	</td>
</tr>
</logic:iterate>
</table>
<br/>
<br/><bean:define id="infoExecutionDegreeId" name="infoExecutionDegreeId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoExecutionDegreeId" property="infoExecutionDegreeId" value="<%= infoExecutionDegreeId.toString() %>"/>
<logic:equal name="isResponsible" value="true">	
<html:submit><bean:message key="label.remove"/></html:submit>

<br/>
<br/>

<p>
<html:link 
	page="<%= "/viewCoordinationTeam.do?method=prepareAddCoordinator&infoExecutionDegreeId="+ 
	infoExecutionDegreeId.toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>" >
	<bean:message key="title.addCoordinator"/>
	</html:link>
</p>
</logic:equal>	
</html:form>
</logic:present>



