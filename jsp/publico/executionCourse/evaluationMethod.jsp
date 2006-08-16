<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2>
	<bean:message key="title.evaluationMethod"/>
</h2>
<logic:empty name="executionCourse" property="evaluationMethod">
	<h3>
		<bean:message key="message.evaluation.not.available"/>
	</h3>
</logic:empty>
<logic:notEmpty name="executionCourse" property="evaluationMethod">
	<p>
		<bean:write name="executionCourse" property="evaluationMethod.evaluationElements"/>
	</p>
	<logic:notEmpty name="executionCourse" property="evaluationMethod.evaluationElementsEn">
		<h2>
			<bean:message key="title.evaluationMethod.eng"/>
		</h2>
		<p>
			<bean:write name="executionCourse" property="evaluationMethod.evaluationElementsEn"/>
		</p>
	</logic:notEmpty>	
</logic:notEmpty>
