<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<h2><bean:message key="title.exams.list"/></h2>
<br/><br/>
<table width="100%" cellspacing="0">
	<tr>
		<td class="infoselected"><p>O curso seleccionado
        	&eacute;:</p>
			<strong><jsp:include page="examsMapContext.jsp"/></strong>
         </td>
    </tr>
</table>
<br />
<br />
<app:generateExamsMap name="<%= PresentationConstants.INFO_EXAMS_MAP %>" user="sop"/>