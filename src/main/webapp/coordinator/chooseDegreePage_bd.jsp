<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List" %>
<html:xhtml/>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<bean:define id="coordinatedInfoDegreeCurricularPlans" name="master_degree_list" />
<bean:define id="link">/chooseDegree.do?degreeCurricularPlanID=</bean:define>

<h2><bean:message key="title.coordinator.main"/></h2>

<p><span class="emphasis"><%= ((List) coordinatedInfoDegreeCurricularPlans).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/></p>
<p class="mbottom05"><bean:message key="label.masterDegree.chooseOne"/></p>

<table class="tstyle4 thlight mtop05">
	<tr>
		<th>Nome</th>
		<th>Plano Curricular</th>
	</tr>
	
	<logic:iterate id="infoDegreeCurricularPlan" name="coordinatedInfoDegreeCurricularPlans">
		<bean:define id="degreeLink">
			<bean:write name="link"/><bean:write name="infoDegreeCurricularPlan" property="externalId"/>
		</bean:define>            	

		<tr>
		   <td>
				<logic:notEmpty name="infoDegreeCurricularPlan" property="degreeCurricularPlan.degree.phdProgram">
					<bean:write name="infoDegreeCurricularPlan" property="degreeCurricularPlan.degree.phdProgram.presentationName"/>
				</logic:notEmpty>
				<logic:empty name="infoDegreeCurricularPlan" property="degreeCurricularPlan.degree.phdProgram">
					<bean:write name="infoDegreeCurricularPlan" property="degreeCurricularPlan.degree.presentationName"/>
				</logic:empty>
		   </td>
		   <td class="acenter">
				<html:link page='<%= pageContext.findAttribute("degreeLink").toString() %>'>
					<bean:write name="infoDegreeCurricularPlan" property="name" /> 
				</html:link>
		   </td>	   
		</tr>
	</logic:iterate>

</table>
