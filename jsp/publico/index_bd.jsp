<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoExecutionPeriod" %>
<span class="error"><html:errors/></span>
<%--	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop">
    			<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" ><h2 style="display: inline;"><bean:message key="link.degree.consult"/></h2></html:link>
    		</td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.degree.consult"/>
</p>
<br />--%>
	<br />
	<strong><font color="#FF0000">Aviso:</font></strong>
	<br />
	<strong>Se pretende consultar informação relativa a disciplinas de 4º ou 5º em 2003/2004 ou 5ª em 2004/2005 do curso de Informática - Alameda, deve seleccionar o plano curricular "Licenciatura em Engenharia Informática e de Computadores - LEIC - Currículo Antigo"</strong>
	<br />
	
<&--	<strong><font color="#FF0000">Aviso:</font></strong>
	<br />
	<strong>Já estão disponíveis os <font color="#FF0000">horários provisórios</font> para o 1º semestre de 2004/2005 </strong>
	<br />
--%>	
	<br />
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td class="infoop">
    			<html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" > <h2 style="display: inline;"><bean:message key="link.classes.consult"/></h2></html:link>
    		</td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.timetable.consult"/>
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>"><h2 style="display: inline;"><bean:message key="link.executionCourse.consult"/></h2></html:link></td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.course.consult"/>
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="<%= "/prepareConsultRooms.do?executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" ><h2 style="display: inline;"><bean:message key="link.rooms.consult"/></h2></html:link></td>
  		</tr>
	</table>
<br />

<p>
<bean:message key="message.public.index.room.consult"/>
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop">
    			<html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" ><h2 style="display: inline;"><bean:message key="link.exams.consult"/></h2></html:link>
    		</td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.exam.consult"/>
</p>
<br />
<br />  
<table width="100%">
	<tr>
		<td><img src="<%= request.getContextPath() %>/images/dotist_info.gif" alt="" />
		</td>
		<td class="px9">
		<bean:message key="message.gesdis.info" />
		</td>
	</tr>
</table>