<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= PresentationConstants.EXECUTION_COURSE %>" scope="request">
	<bean:define id="executionCourse"
				 name="<%= PresentationConstants.EXECUTION_COURSE %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="executionCourseOID"
				 type="java.lang.String"
				 name="executionCourse"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>