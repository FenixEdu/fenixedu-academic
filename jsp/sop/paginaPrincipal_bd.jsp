<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="dotTitle"type="java.lang.String"><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:message key="introduction.message" arg0="<%= dotTitle %>"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img height="15" src="<%= request.getContextPath() %>/images/info.gif" alt="<bean:message key="info" bundle="IMAGE_RESOURCES" />" width="15" /> 
    </td>
    <td class="infoop"><strong><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0"><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></html:link></strong></td>
  </tr>
</table>
<p>Na &aacute;rea da Gest&atilde;o de Disciplinas pode efectuar algumas opera&ccedil;&otilde;es sobre <strong>disciplinas de execu&ccedil;&atilde;o</strong> 
	(agrupar disciplinas de execu&ccedil;&atilde;o e gerir cargas horárias).</p>

<br />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img height="15" src="<%= request.getContextPath() %>/images/info.gif" alt="<bean:message key="info" bundle="IMAGE_RESOURCES" />" width="15" /> 
    </td>
    <td class="infoop"><strong><html:link page="/chooseExecutionPeriod.do?method=prepare"><bean:message key="link.schedules.management" bundle="SOP_RESOURCES"/></html:link></strong></td>
  </tr>
</table>
<p>Na &aacute;rea da Gest&atilde;o de Hor&aacute;rios pode proceder &agrave; gest&atilde;o
  de <strong>turmas</strong> (criar e alterar turmas), gest&atilde;o de <strong>turnos</strong> (criar,
  alterar e apagar turnos), assim como proceder &agrave; gest&atilde;o de <strong>aulas</strong> (criar
e alterar aulas).</p>

<br />

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img height="15" src="<%= request.getContextPath() %>/images/info.gif" alt="<bean:message key="info" bundle="IMAGE_RESOURCES" />" width="15" /> 
    </td>
    <td class="infoop"><strong><html:link page="/principalSalas.do"><bean:message key="link.rooms.management" bundle="SOP_RESOURCES"/></html:link></strong></td>
  </tr>
</table>
<p>Este m&oacute;dulo visa uma gest&atilde;o eficiente das <strong>salas de aulas</strong> dispon&iacute;veis
  e a sua calendariza&ccedil;&atilde;o.</p>

<br />

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img height="15" src="<%= request.getContextPath() %>/images/info.gif" alt="<bean:message key="info" bundle="IMAGE_RESOURCES" />" width="15" /> 
    </td>
    <td class="infoop"><strong><html:link page="/mainExamsNew.do?method=prepare"><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></html:link></strong></td>
  </tr>
</table>
<p><bean:message key="introduction.writtenEvaluation.management"/></p>

<br />

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img height="15" src="<%= request.getContextPath() %>/images/info.gif" alt="<bean:message key="info" bundle="IMAGE_RESOURCES" />" width="15" /> 
    </td>
    <td class="infoop"><strong><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></html:link></strong></td>
  </tr>
</table>
<p><bean:message key="message.info.sop" bundle="CURRICULUM_HISTORIC_RESOURCES"/></p>
