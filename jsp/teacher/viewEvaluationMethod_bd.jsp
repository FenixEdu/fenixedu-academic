<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="evaluationMethods" name="component" property="infoEvaluations"/>
<logic:notEqual name="component" property="size" value="0">
<logic:iterate id="evaluation" name="evaluationMethods">
<bean:define id="infoCurricularCourse" name="evaluation" property="infoCurricularCourse"/>
<bean:define id="infoDegreeCurricularPlan" name="infoCurricularCourse" property="infoDegreeCurricularPlan"/>
<bean:define id="infoDegree" name="infoDegreeCurricularPlan" property="infoDegree"/>
<h2><bean:write name="infoDegree" property="sigla"/>-<bean:write name="infoCurricularCourse" property="name"/></h2>

<h2><bean:message key="title.evaluationMethod"/></h2>
<table>
	<tr>
		<td> 
			<bean:write name="evaluation" property="evaluationElements" filter="false"/>
	 	</td>
	</tr>
</table>
<br />
<logic:notEmpty name="evaluation" property="evaluationElementsEn">	
<h2><bean:message key="title.evaluationMethod.eng"/></h2>
<table>	
	<tr>
		<td> 
			<bean:write name="evaluation" property="evaluationElementsEn" filter="false"/>
	 	</td>
	</tr>
</table>
<br/>	
</logic:notEmpty>

<bean:define id="curricularCourseCode" name="infoCurricularCourse" property="idInternal"/>
<logic:equal name="infoCurricularCourse" property="basic" value="false">
<div class="gen-button">
	<html:link page="<%= "/editEvaluationMethod.do?method=prepareEditEvaluationMethod&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;curricularCourseCode=" +curricularCourseCode.toString() %>">
		<bean:message key="button.edit"/>
	</html:link>
</div>
</logic:equal>
</logic:iterate>
</logic:notEqual>

<logic:equal name="component" property="size" value="0">

<bean:define id="infoCurricularCourses" name="component" property="infoCurricularCourses"/>
<logic:iterate id="infoCurricularCourse" name="infoCurricularCourses">



<h2><bean:message key="title.evaluationMethod"/></h2>
<h3><bean:write name="infoCurricularCourse" property="name"/></h3>
<h3><bean:write name="infoCurricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/></h3>

<br />	
<bean:define id="curricularCourseId" name="infoCurricularCourse" property="idInternal"/>
<logic:equal name="infoCurricularCourse" property="basic" value="false">
<div class="gen-button">
	<html:link page="<%= "/editEvaluationMethod.do?method=prepareEditEvaluationMethod&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;curricularCourseCode="+ pageContext.findAttribute("curricularCourseId")%>">
		<bean:message key="button.edit"/>
	</html:link>
</div>
</logic:equal>
</logic:iterate>



</logic:equal>


</logic:present>