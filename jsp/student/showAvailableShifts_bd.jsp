<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="DataBeans.InfoLesson" %>
<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />

<table cellpadding="0" cellspacing="0" border="0">
 <tr>
  <td>
    <bean:write name="infoStudentShiftEnrolment" property="infoStudent.infoPerson.nome"/> 
  </td>
 </tr>
 <tr>
  <td>
   <logic:present name="infoStudentShiftEnrolment"> <!-- aqui falta scope ? -->
    Turnos onde estás inscrito:<br>
    <logic:iterate name="infoStudentShiftEnrolment"  id="enroledShift" property="currentEnrolment">
      Nome: <bean:write name="enroledShift" property="nome"/><br>
      Tipo: <bean:write name="enroledShift" property="tipo.siglaTipoAula"/><br>
    </logic:iterate>
    <br>
    Turnos onde te podes inscrever:<br>
    <html:form action="studentShiftEnrolmentManager">
     <html:hidden property="method" value="validateAndConfirmShiftEnrolment"/>
     <bean:define id="index" value="0"/> 
     <logic:iterate name="infoStudentShiftEnrolment"  id="list" property="dividedList" indexId="courseIndex">
	  <br>
       Cadeira: <bean:write name="list" property="type"/><br>
       ----------
		<logic:iterate name="list" id="sublist" property="list" indexId="groupIndex">
	       <bean:define id="index" value="<%=  (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
			&nbsp <br>Tipo: <bean:write name="sublist" property="type"/><br>
            <logic:iterate name="sublist" id="shiftWithLessons" property="list" >
            <bean:define id="shift" name="shiftWithLessons" property="infoShift" />
             &nbsp <html:radio property='<%= "shifts[" + index + "]" %>' idName="shift" value="idInternal" /><bean:write name="shiftWithLessons" property="infoShift.nome"/> : 
             (Capacidade <bean:write name="shiftWithLessons" property="infoShift.availabilityFinal"/>)<br> 

             
             
             <logic:iterate id="lesson" name="shiftWithLessons" property="infoLessons">
              &nbsp &nbsp  &nbsp &nbsp <bean:write name="lesson" property="diaSemana"/> das <%= ((InfoLesson)lesson).getInicio().get(Calendar.HOUR_OF_DAY) +":"+((InfoLesson)lesson).getInicio().get(Calendar.MINUTE)  %> até as 
              <%= ((InfoLesson)lesson).getFim().get(Calendar.HOUR_OF_DAY) +":"+((InfoLesson)lesson).getFim().get(Calendar.MINUTE)  %> <br>
             </logic:iterate>
            </logic:iterate>

		</logic:iterate>
     </logic:iterate>
      <html:submit value="Inscrever"/>
    </html:form>
   </logic:present>
  </td>
 </tr>
</table>