<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<jsp:include page="contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= SessionConstants.CURRICULAR_YEARS_LIST %>" scope="request">
	<logic:iterate id="year" name="<%= SessionConstants.CURRICULAR_YEARS_LIST %>" scope="request">
		<logic:equal name="year" value="1">
			<bean:define id="curricularYears_1"
						 value="1"
						 toScope="request"/>
		</logic:equal>
		<logic:equal name="year" value="2">
			<bean:define id="curricularYears_2"
						 value="2"
						 toScope="request"/>
		</logic:equal>
		<logic:equal name="year" value="3">
			<bean:define id="curricularYears_3"
						 value="3"
						 toScope="request"/>
		</logic:equal>
		<logic:equal name="year" value="4">
			<bean:define id="curricularYears_4"
						 value="4"
						 toScope="request"/>
		</logic:equal>
		<logic:equal name="year" value="5">
			<bean:define id="curricularYears_5"
						 value="5"
						 toScope="request"/>
		</logic:equal>
	</logic:iterate>
</logic:present>