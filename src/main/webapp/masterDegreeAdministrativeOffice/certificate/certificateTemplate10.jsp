<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<p>
	<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= PresentationConstants.ENROLMENT_LIST%>" />
	<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">		
		<br/>
 		<str:upperCase><bean:write name="itr" property="infoCurricularCourse.name" /></str:upperCase>
 		<bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 		com
 		<bean:write name="itr" property="infoEnrolmentEvaluation.gradeValue" />
 		valores 		
	</logic:iterate>
	<bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN%>"/>
	<logic:notEmpty name="infoStudentCurricularPlan"  property="givenCredits" >
		<br/>
		Atribuição de Créditos:  <bean:write name="infoStudentCurricularPlan"  property="givenCredits" /> Créditos
	</logic:notEmpty>
</p>