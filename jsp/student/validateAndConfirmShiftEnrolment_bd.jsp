<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />

<table cellpadding="0" cellspacing="0" border="0">
<td>
  <tr>
   <logic:present name="infoStudentShiftEnrolment">
	   <logic:present name="infoStudentShiftEnrolment" property="currentEnrolment">
			Estes são os turnos onde estavas inscrito até agora:<br>
   	 		<logic:iterate name="infoStudentShiftEnrolment" id="shift" property="currentEnrolment">
   	 			Turno:<bean:write name="shift" property="nome"/><br>
	   	 	</logic:iterate>
	   	 </logic:present>
    <logic:present name="infoStudentShiftEnrolment" property="errors">
   	 	<!-- show errors here -->
   	 </logic:present>
     <logic:present name="infoStudentShiftEnrolment" property="newShifts">
	 	Turnos em que deixaste de estar inscrito:<br>
	     <logic:iterate name="infoStudentShiftEnrolment" id="shiftPairs" property="newShifts">
		     <logic:iterate name="shiftPairs" id="shift"offset="0" length="1">
		     	<logic:present name="shift">
				 	Nome: <bean:write name="shift" property="nome"/><br>
					Tipo: <bean:write name="shift" property="tipo.siglaTipoAula"/><br>
				</logic:present>
		     </logic:iterate>
    	 </logic:iterate>
		<br>Turnos em que te inscreveste:<br>
    	 <logic:iterate name="infoStudentShiftEnrolment" id="shiftPairs" property="newShifts">
	    	 <logic:iterate name="shiftPairs" id="shift"offset="1" length="1">
	     		<logic:present name="shift">
				 	Nome: <bean:write name="shift" property="nome"/><br>
					Tipo: <bean:write name="shift" property="tipo.siglaTipoAula"/><br>
				</logic:present>
		     </logic:iterate>
    	 </logic:iterate>
   	 </logic:present>
   	 <br><br>
   	 <logic:present name="infoStudentShiftEnrolment" property="errors">
   	 	Foram encontrados os seguintes problemas:<br>
   	 	<logic:iterate name="infoStudentShiftEnrolment" id="error" property="errors">
   	 		Turno:<bean:write name="error" property="shift.nome"/><br>
   	 		Problema:<bean:write name="error" property="msg"/><br>
   	 	</logic:iterate>
   	 </logic:present>
    </logic:present>
  </tr>
</td>
</table>