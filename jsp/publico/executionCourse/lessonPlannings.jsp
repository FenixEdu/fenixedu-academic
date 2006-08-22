<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:present name="executionCourse">

	<H2><bean:message key="link.lessonPlannings"/></H2>
	<logic:empty name="lessonPlanningsMap">
		<h3><bean:message key="label.lessonPlannings.not.available"/></h3>
	</logic:empty>			
	<logic:iterate id="lessonPlannings" name="lessonPlanningsMap">		
		<logic:notEmpty name="lessonPlannings" property="value">
			<H3><bean:message key="label.lessons"/> <bean:message name="lessonPlannings" property="key.name" bundle="DEFAULT"/></H3>
		</logic:notEmpty>
		<br/>
		<logic:iterate id="lessonPlanning" name="lessonPlannings" property="value" indexId="index">
			<i><bean:message key="label.lessonPlanning.order"/></i>&nbsp;<i><bean:write name="lessonPlanning" property="orderOfPlanning"/></i><br/>
			<b><bean:write name="lessonPlanning" property="title"/></b><br/>
			<bean:write name="lessonPlanning" property="planning" filter="false"/><br/>													
			<br/>
		</logic:iterate>				
	</logic:iterate>

</logic:present>