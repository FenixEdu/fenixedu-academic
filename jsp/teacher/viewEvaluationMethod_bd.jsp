<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.evaluationMethod.explanation" />
		</td>
	</tr>
</table>

<logic:present name="siteView">
	<bean:define id="component" name="siteView" property="component"/>
	
	<blockquote>
			
		<h2><bean:message key="title.evaluationMethod"/></h2>				
		<logic:notEmpty name="component" property="evaluationElements">
		<table>
			<tr>
				<td> 
					<bean:write name="component" property="evaluationElements" filter="false"/>
	 			</td>
			</tr>
		</table>
		</logic:notEmpty>		
		<br />
		
		
		<h2><bean:message key="title.evaluationMethod.eng"/></h2>
		<logic:notEmpty name="component" property="evaluationElementsEn">	
		<table>	
			<tr>
				<td> 
						<bean:write name="component" property="evaluationElementsEn" filter="false"/>
					</td>
				</tr>
			</table>
			<br/>	
		</logic:notEmpty>
	
		<div class="gen-button">
			<html:link page="<%= "/editEvaluationMethod.do?method=prepareEditEvaluationMethod&amp;objectCode=" + pageContext.findAttribute("objectCode")%>">
				<bean:message key="button.edit"/>
			</html:link>
		</div>
		
		</blockquote>				
		<br />
		<br />			
		<br />		

</logic:present> 





<%-- BEFORE
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.evaluationMethod.explanation" />
		</td>
	</tr>
</table>

<logic:present name="siteView">
	<bean:define id="component" name="siteView" property="component"/>

	<bean:define id="infoEvaluations" name="component" property="infoEvaluations"/>
	<bean:define id="infoCurricularCourses" name="component" property="infoCurricularCourses"/>
	<logic:iterate id="infoCurricularCourse" name="infoCurricularCourses">
			<h3><bean:write name="infoCurricularCourse" property="name"/> -- <bean:write name="infoCurricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/></h3>
			<blockquote>
			<bean:define id="curricularCourseId" name="infoCurricularCourse" property="idInternal"/>
			
			<logic:iterate id="infoCurriculum" name="infoEvaluations">
			
				<bean:define id="infoCurriculumCurricularCourse" name="infoCurriculum" property="infoCurricularCourse"/>
				
				<logic:equal name="infoCurriculumCurricularCourse" property="idInternal" value="<%= curricularCourseId.toString() %>">
				<h2><bean:message key="title.evaluationMethod"/></h2>				
					<table>
						<tr>
							<td> 
								<bean:write name="infoCurriculum" property="evaluationElements" filter="false"/>
	 						</td>
						</tr>
					</table>
					<br />
					<logic:notEmpty name="infoCurriculum" property="evaluationElementsEn">	
						<h2><bean:message key="title.evaluationMethod.eng"/></h2>
						<table>	
							<tr>
								<td> 
									<bean:write name="infoCurriculum" property="evaluationElementsEn" filter="false"/>
	 							</td>
							</tr>
						</table>
						<br/>	
					</logic:notEmpty>
				</logic:equal>

			</logic:iterate>

			<logic:equal name="infoCurricularCourse" property="basic" value="false">
				<div class="gen-button">
					<html:link page="<%= "/editEvaluationMethod.do?method=prepareEditEvaluationMethod&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;curricularCourseCode="+ pageContext.findAttribute("curricularCourseId")%>">
						<bean:message key="button.edit"/>
					</html:link>
				</div>
			</logic:equal>
			</blockquote>				
			<br />
			<br />			
			<br />			
		</logic:iterate>

</logic:present> --%>