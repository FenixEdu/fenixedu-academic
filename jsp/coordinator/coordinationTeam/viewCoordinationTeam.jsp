<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />


<logic:present name="coordinators">
<h3><bean:message key="title.coordinationTeam"/></h3>
<html:form action="/viewCoordinationTeam">
<html:hidden property="method" value="removeCoordinators" />
<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
<table>
<tr><td class="listClasses-header">Nome</td>
	<td class="listClasses-header">
   <logic:equal name="isResponsible" value="true">
   <bean:message key="label.remove"/>
   </logic:equal>
   &nbsp;</td>
</tr>
<logic:iterate name="coordinators" id="coordinator">
<tr>
	<td class="listClasses"><bean:write name="coordinator" property="infoTeacher.infoPerson.nome" /> 
	
	</td>
   
	<td class="listClasses">
  	
   		<logic:equal name="coordinator" property="responsible" value="true">
		<bean:message key="label.responsible"/>
		</logic:equal> 
		<logic:notEqual name="coordinator" property="responsible" value="true">
			<logic:equal name="isResponsible" value="true">	
			 <html:multibox property="coordinatorsIds">
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
<html:hidden property="infoExecutionDegreeId" value="<%= infoExecutionDegreeId.toString() %>"/>
<logic:equal name="isResponsible" value="true">	
<html:submit><bean:message key="label.remove"/></html:submit>

<br/>
<br/>

<html:link 
	page="<%= "/viewCoordinationTeam.do?method=prepareAddCoordinator&infoExecutionDegreeId="+ 
	infoExecutionDegreeId.toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>" >
	<bean:message key="title.addCoordinator"/>
	</html:link>
</logic:equal>	
</html:form>
</logic:present>



