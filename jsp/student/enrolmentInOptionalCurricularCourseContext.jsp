<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<jsp:include page="enrolmentContext.jsp" flush="true"/>
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />
<br/>
<bean:define id="optionalInfoCurricularCourse" name="infoEnrolmentContext" property="infoChosenOptionalCurricularCourseScope.infoCurricularCourse" />
<bean:message key="label.option" /><bean:write name="optionalInfoCurricularCourse" property="name"/> <br />
<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
	<logic:equal name="optionalEnrolment" property="infoCurricularCourse" value="<%= pageContext.findAttribute("optionalInfoCurricularCourse").toString() %>">
			&nbsp;&nbsp;<bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
	</logic:equal> 
</logic:iterate>