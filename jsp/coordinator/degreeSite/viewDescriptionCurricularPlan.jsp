<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeCurricularPlanID" >

<html:form action="/degreeSiteManagement">

	<h2><bean:message key="title.coordinator.degreeSite.edit"/></h2>

	<bean:define id="infoExecutionDegreeID" name="infoExecutionDegreeID"/>
	<html:hidden property="infoExecutionDegreeID" value="<%=  infoExecutionDegreeID.toString() %>"/>
	
	<bean:define id="infoDegreeCurricularPlanID" name="infoDegreeCurricularPlanID"/>
	<html:hidden property="infoDegreeCurricularPlanID" value="<%=  infoDegreeCurricularPlanID.toString() %>"/>

	<html:hidden property="method" value="editDescriptionDegreeCurricularPlan" />
	
	<html:hidden property="page" value="1" />
	
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