<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2><bean:message key="title.coordinator.main"/></h2>

<p><span class="emphasis">${degrees.size()}</span> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/></p>
<p class="mbottom05"><bean:message key="label.masterDegree.chooseOne"/></p>

<table class="tstyle4 thlight mtop05">
	<tr>
		<th><bean:message key="label.name" bundle="APPLICATION_RESOURCES" /></th>
		<th><bean:message key="label.curricularPlan" bundle="APPLICATION_RESOURCES" /></th>
	</tr>
	
	<logic:iterate id="degreeCurricularPlan" name="degrees">
		<tr>
		   <td>
				<logic:notEmpty name="degreeCurricularPlan" property="degree.phdProgram">
					<bean:write name="degreeCurricularPlan" property="degree.phdProgram.presentationName"/>
				</logic:notEmpty>
				<logic:empty name="degreeCurricularPlan" property="degree.phdProgram">
					<bean:write name="degreeCurricularPlan" property="degree.presentationName"/>
				</logic:empty>
		   </td>
		   <td class="acenter">
				<a href="${pageContext.request.contextPath}/coordinator/coordinatorIndex.do?degreeCurricularPlanID=${degreeCurricularPlan.externalId}">${degreeCurricularPlan.name}</a>
		   </td>	   
		</tr>
	</logic:iterate>

</table>
