<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>



<logic:present name="coordinators">
<h3>Equipa de Coordenação</h3>
<html:form action="/viewCoordinationTeam">
<html:hidden property="method" value="removeCoordinators" />
<table>
<tr><td class="listClasses-header">Nome</td>
   <logic:equal name="isResponsible" value="true">
	<td class="listClasses-header">Remover</td>
   </logic:equal>
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
<br/>
<html:submit/>
<br/>
<br/>
<bean:define id="infoExecutionDegreeId" name="infoExecutionDegreeId"/>
<html:link 
	page="<%= "/viewCoordinationTeam.do?method=prepareAddCoordinator&infoExecutionDegreeId="+ 
	infoExecutionDegreeId.toString()  %>" >
	Adicionar Docente à Equipa de Coordenação
	</html:link>
</html:form>
</logic:present>



