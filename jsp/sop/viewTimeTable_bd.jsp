<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
	<table width="100%" cellpadding="0" cellspacing="0">
          <tr>
            <td  class="infoselected"><p>A licenciatura seleccionada &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
	</table>
     <br />
	<logic:present name="<%= SessionConstants.CLASS_VIEW %>" scope="session">
   		<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>   		
			<td nowrap="nowrap" class="formTD">
				<%-- Create class form --%>
				<h2><bean:message key="label.class.edit"/></h2>    		
					<jsp:include page="classForm.jsp"/>
				<%-- ***************** --%>
	   		</td>
   		</tr>
		</table>
		<br/>
		<h2>Horário da Turma</h2>
		<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>" type="<%= TimeTableType.SOP_CLASS_TIMETABLE %>"/>		
		<br />
	</logic:present>
	<logic:notPresent name="<%= SessionConstants.CLASS_VIEW %>" scope="session">
    	<h2><bean:message key="label.class.create"/></h2>
   		<table align="left" cellpading="0" cellspacing="0" border="0">
		<tr>   		
			<td nowrap="nowrap" class="formTD">
				<%-- Create class form --%>		
					<jsp:include page="classForm.jsp"/>
				<%-- ***************** --%>
	   		</td>
   		</tr>
   		</table>
	</logic:notPresent>

