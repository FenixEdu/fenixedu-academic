<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
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
<app:generateExamsMap name="<%= SessionConstants.INFO_EXAMS_MAP %>" user="sop"/>