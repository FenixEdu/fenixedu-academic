<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
	
<logic:present name="<%= PresentationConstants.EXAM_DATEANDTIME %>">
	<dt:format pattern="yyyy/MM/dd - HH:mm">
		<bean:write name="examDateAndTime"/>
	</dt:format>
</logic:present>
