<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<table width="100%" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
       	</td>
    </tr>
</table>
<br />
<logic:present name="<%= SessionConstants.CLASS_VIEW %>" scope="request">
	<table width="100%" cellspacing="0">
		<tr>   		
			<td nowrap="nowrap">
				<%-- Create class form --%>
				<h2><bean:message key="label.class.edit"/></h2>    		
				<jsp:include page="classForm.jsp"/>
				<%-- ***************** --%>
			</td>
	   	</tr>
	</table>
	<br />
	<h2>Hor�rio da Turma</h2>
	<div align="center">
		<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>"
						  type="<%= TimeTableType.SOP_CLASS_TIMETABLE %>"/>
	</div>
	<br />
</logic:present>
<logic:notPresent name="<%= SessionConstants.CLASS_VIEW %>" scope="request">
	<h2><bean:message key="label.class.create"/></h2>
	<table cellspacing="0">
		<tr>   		
			<td nowrap="nowrap" class="formTD">
				<%-- Create class form --%>		
				<jsp:include page="classForm.jsp"/>
				<%-- ***************** --%>
		   	</td>
	   	</tr>
	</table>
</logic:notPresent>