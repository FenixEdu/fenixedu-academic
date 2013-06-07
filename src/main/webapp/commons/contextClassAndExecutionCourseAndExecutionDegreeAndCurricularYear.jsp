<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= PresentationConstants.CLASS_VIEW %>" scope="request">
	<bean:define id="school_class"
				 name="<%= PresentationConstants.CLASS_VIEW %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="classOID"
				 type="java.lang.String"
				 name="school_class"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>