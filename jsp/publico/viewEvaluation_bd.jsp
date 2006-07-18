<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<logic:present name="siteView" property="component" > 
	<bean:define id="component" name="siteView" property="component"/>
	<logic:iterate id="infoEvaluationMethod" name="component" property="infoEvaluations">
	
		<h4><bean:write name="infoEvaluationMethod" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></h4>	
		<h4><bean:write name="infoEvaluationMethod" property="infoCurricularCourse.name"/></h4>	

		<logic:notEmpty name="infoEvaluationMethod" property="evaluationElements">
		<h2><bean:message key="label.evaluation" /></h2>	
		<p>
			<bean:write name="infoEvaluationMethod" property="evaluationElements" filter="false"/>
		</p>
		</logic:notEmpty>
		<logic:empty name="infoEvaluationMethod" property="evaluationElements">
			<h2><bean:message key="message.evaluation.not.available"/></h2>
		</logic:empty>	
		
	</logic:iterate>	

</logic:present>

<logic:notPresent name="siteView" property="component">
	<h2><bean:message key="message.evaluation.not.available"/></h2>
</logic:notPresent>