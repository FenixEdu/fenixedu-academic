<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<p>
	<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.ENROLMENT_LIST%>" />
		<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">		
		<br/>
 		<str:upperCase><bean:write name="itr" property="infoCurricularCourse.name" /></str:upperCase>
 		<bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 		com
 		<bean:write name="itr" property="infoEnrolmentEvaluation.grade" />
 		valores 		
	</logic:iterate>
</p>