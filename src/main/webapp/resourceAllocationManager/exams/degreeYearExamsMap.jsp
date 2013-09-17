<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<bean:define id="infoExamsMaps" name="<%=PresentationConstants.INFO_EXAMS_MAP%>"/>
<logic:iterate id ="infoExamsMap" name="infoExamsMaps">
	<app:generateNewExamsMap name="infoExamsMap" user="sop" mapType="DegreeAndYear"/>
</logic:iterate>