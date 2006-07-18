<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<jsp:include page="contextShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= SessionConstants.LESSON %>" scope="request">
	<bean:define id="lesson"
				 name="<%= SessionConstants.LESSON %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="lessonOID"
				 type="java.lang.Integer"
				 name="lesson"
				 property="idInternal"
				 toScope="request"
				 scope="request"/>
</logic:present>