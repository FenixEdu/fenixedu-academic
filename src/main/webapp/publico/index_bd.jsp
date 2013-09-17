<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<%--	<table width="98%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop">
    			<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) %>" ><h2 style="display: inline;"><bean:message key="link.degree.consult"/></h2></html:link>
    		</td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.degree.consult"/>
</p>
<br />--%>
	<p><strong><font color="#FF0000">Atenï¿?ï¿?o&nbsp;-&nbsp;</font>Se pretende consultar informação relativa a disciplinas de 4ï¿? ou 5ï¿? em 2003/2004 ou 5ï¿? em 2004/2005 do curso de Informï¿?tica - Alameda, deve seleccionar o plano curricular "Licenciatura em Engenharia Informï¿?tica e de Computadores - LEIC - Currï¿?culo Antigo"</strong></p>
	<p>	<strong><font color="#FF0000">Atenï¿?ï¿?o&nbsp;-&nbsp;</font>Devido ï¿? alteraï¿?ï¿?o do calendï¿?rio das Licenciaturas em Engenharia Civil, em Engenharia do Territï¿?rio, e em Arquitectura (despacho do Conselho Directivo do passado dia 29 de Julho), as salas de aula dessas trï¿?s Licenciaturas foram alteradas</strong></p>
	
<%--	<strong><font color="#FF0000">Aviso:</font></strong>
	<br />
	<strong>Jï¿? estï¿?o disponï¿?veis os <font color="#FF0000">horï¿?rios provisï¿?rios</font> para o 1ï¿? semestre de 2004/2005 </strong>
	<br />
--%>	
	<br />
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td class="infoop">
    			<html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" > <h2 style="display: inline;"><bean:message key="link.classes.consult"/></h2></html:link>
    		</td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.timetable.consult"/>
</p>
<br />
	<table width="98%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>"><h2 style="display: inline;"><bean:message key="link.executionCourse.consult"/></h2></html:link></td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.course.consult"/>
</p>
<br />
	<table width="98%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="<%= "/prepareConsultRooms.do?executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) %>" ><h2 style="display: inline;"><bean:message key="link.rooms.consult"/></h2></html:link></td>
  		</tr>
	</table>
<br />

<p>
<bean:message key="message.public.index.room.consult"/>
</p>
<br />
	<table width="98%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop">
    			<html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" ><h2 style="display: inline;"><bean:message key="link.exams.consult"/></h2></html:link>
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
		<td><img src="<%= request.getContextPath() %>/images/dotist_info.gif" alt="<bean:message key="dotist_info" bundle="IMAGE_RESOURCES" />" />
		</td>
		<td class="px9">
		<bean:message key="message.gesdis.info" />
		</td>
	</tr>
</table>