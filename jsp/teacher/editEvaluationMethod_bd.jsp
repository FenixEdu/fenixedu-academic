<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<span class="error"><html:errors /></span> 

<logic:present name="siteView">
	<html:form action="/editEvaluationMethod">

	<logic:present name="objectCode">
		<html:hidden property="objectCode" value="<%= request.getAttribute("objectCode").toString() %>"/>
	</logic:present>

	<logic:present name="siteView" property="component">
		<bean:define id="infoEvaluationMethod" name="siteView" property="component"/>
		<logic:notEmpty name="infoEvaluationMethod" property="idInternal">
			<bean:define id="evaluationMethodCode" name="infoEvaluationMethod" property="idInternal"/>
			<html:hidden property="evaluationMethodCode" value="<%= evaluationMethodCode.toString() %>"/>	
		</logic:notEmpty>
		<logic:notEmpty name="infoEvaluationMethod" property="infoExecutionCourse">
			<bean:define id="infoExecutionCourse" name="infoEvaluationMethod" property="infoExecutionCourse" />
			<logic:notEmpty name="infoExecutionCourse" property="idInternal">			
				<bean:define id="executionCourseCode" name="infoExecutionCourse" property="idInternal"/>
				<html:hidden property="objectCode" value="<%= pageContext.findAttribute("executionCourseCode").toString() %>"/>
			</logic:notEmpty>								
		</logic:notEmpty>	
	</logic:present>
		
	<logic:notPresent name="siteView" property="component">
		<logic:notEmpty name="infoEvaluationMethod" property="idInternal">
			<bean:define id="evaluationMethodCode" name="infoEvaluationMethod" property="idInternal"/>
			<html:hidden property="evaluationMethodCode" value="<%= pageContext.findAttribute("evaluationMethodCode").toString() %>"/>			
		</logic:notEmpty>
	</logic:notPresent> 
	
	<h2><bean:message key="label.evaluation"/></h2> 
		
	<table>	
		<tr>
			<td><strong><bean:message key="label.evaluation"/></strong></td>
		</tr>
		<tr>
			<td><html:textarea  property="evaluationElements" cols="50" rows="8"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="label.evaluation.eng"/></strong></td>
		</tr>	
		<tr>
			<td><html:textarea  property="evaluationElementsEn" cols="50" rows="8"/></td>
		</tr>
		<tr>
			<td>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
				<html:reset  styleClass="inputbutton">
					<bean:message key="label.clear"/>
				</html:reset>
			</td>
		</tr>
	</table>   
	
	<html:hidden property="method" value="editEvaluationMethod" />
	<html:hidden property="page" value="1"/>	
	
	</html:form>
</logic:present>


<%--
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<span class="error"><html:errors /></span> 

	<logic:present name="siteView">
		<html:form action="/editEvaluationMethod">
		
		<logic:present name="siteView" property="component">
			<bean:define id="curriculum" name="siteView" property="component"/>
			<bean:define id="curricularCourseCode" name="curriculum" property="infoCurricularCourse.idInternal"/>
			<html:hidden property="curricularCourseCode" value="<%= curricularCourseCode.toString() %>"/>
			<h3><bean:write name="curriculum" property="infoCurricularCourse.name"/> -- <bean:write name="curriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></h3>
		</logic:present>
		
		<logic:notPresent name="siteView" property="component">
			<bean:define id="curricularCourseCode" name="curricularCourseCode"/>
			<html:hidden property="curricularCourseCode" value="<%= curricularCourseCode.toString() %>"/>
			
		</logic:notPresent> 
	
		<h2><bean:message key="label.evaluation"/></h2> 
	
		<html:hidden property="page" value="1"/>
		<table>	
			<tr>
				<td><strong><bean:message key="label.evaluation"/></strong></td>
			</tr>
			<tr>
				<td><html:textarea  property="evaluationElements" cols="50" rows="8"/></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.evaluation.eng"/></strong></td>
			</tr>	
			<tr>
				<td><html:textarea  property="evaluationElementsEn" cols="50" rows="8"/></td>
			</tr>
			<tr>
				<td>
					<html:submit styleClass="inputbutton">
						<bean:message key="button.save"/>
					</html:submit>
					<html:reset  styleClass="inputbutton">
						<bean:message key="label.clear"/>
					</html:reset>
				</td>
			</tr>
		</table>   
		<html:hidden property="method" value="editEvaluationMethod" />
		<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	
	</html:form>
</logic:present> --%>