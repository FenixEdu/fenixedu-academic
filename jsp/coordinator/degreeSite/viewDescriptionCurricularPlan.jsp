<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<span class="error"><html:errors/></span>

<logic:present name="infoDegreeCurricularPlanId" >

<html:form action="/degreeSiteManagement">

	<h2><bean:message key="title.coordinator.degreeSite.edit"/></h2>

	<bean:define id="infoExecutionDegreeId" name="infoExecutionDegreeId"/>
	<html:hidden property="infoExecutionDegreeId" value="<%=  infoExecutionDegreeId.toString() %>"/>
	
	<bean:define id="infoDegreeCurricularPlanId" name="infoDegreeCurricularPlanId"/>
	<html:hidden property="infoDegreeCurricularPlanId" value="<%=  infoDegreeCurricularPlanId.toString() %>"/>

	<html:hidden property="method" value="editDescriptionDegreeCurricularPlan" />
	
	<table>	
		<tr>
			<td><strong><bean:message key="label.coordinator.degreeSite.description"/>&nbsp;<bean:message key="label.curricularPlan" /></strong></td>
		</tr>
		<tr>
			<td><html:textarea property="descriptionDegreeCurricularPlan" cols="80" rows="8"/></td>
		</tr>
		
		<tr>
			<td><strong><bean:message key="label.coordinator.degreeSite.description"/>&nbsp;<bean:message key="label.curricularPlan" />&nbsp;<bean:message key="label.inEnglish" /></strong></td>
		</tr>
		<tr>
			<td><html:textarea property="descriptionDegreeCurricularPlanEn" cols="80" rows="8"/></td>
		</tr>		
	</table>
		
	</br></br>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>                    		         	
	</html:submit>       
	<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>  
	</br></br>

</html:form>

</logic:present>