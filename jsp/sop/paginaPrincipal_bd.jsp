<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<p><img height="36" src="<%= request.getContextPath() %>/images/intranetSOP.gif" width="239" /></p>

  A aplica&ccedil;&atilde;o online SOP, exclusivamente dispon&iacute;vel para
  utilizadores credenciados, integra um conjunto de servi&ccedil;os denominado
  .IST cujos objectivos residem na utiliza&ccedil;&atilde;o de tecnologias web
  para criar, modificar, armazenar e aceder a informa&ccedil;&atilde;o privilegiada
  de forma forma flex&iacute;vel, funcional e descentralizada, contribuindo -
dessa forma - para din&acirc;micas de trabalho mais produtivas e eficientes.
<p>Descrevem-se, de seguida, as funcionalidades existentes nas duas grandes &aacute;reas
dispon&iacute;veis.</p>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img alt="" height="15" src="<%= request.getContextPath() %>/images/info.gif" width="15"> 
    </td>
    <td class="infoop"><html:link page="/chooseExecutionPeriod.do?method=prepare"><strong>Gest&atilde;o de Hor&aacute;rios</strong></html:link></td>
  </tr>
</table>
<p>Na &aacute;rea da Gest&atilde;o de Hor&aacute;rios pode proceder &agrave; gest&atilde;o
  de <strong>turmas</strong> (criar e alterar turmas), gest&atilde;o de <strong>turnos</strong> (criar,
  alterar e apagar turnos), assim como proceder &agrave; gest&atilde;o de <strong>aulas</strong> (criar
e alterar aulas).</p>
<br />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img alt="" height="15" src="<%= request.getContextPath() %>/images/info.gif" width="15"> 
    </td>
    <td class="infoop"><html:link page="/principalSalas.do"><strong>Gest&atilde;o de Salas</strong></html:link></td>
  </tr>
</table>
<p>Este m&oacute;dulo visa uma gest&atilde;o eficiente das <strong>salas de aulas</strong> dispon&iacute;veis
  e a sua calendariza&ccedil;&atilde;o.</p>
<br />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img alt="" height="15" src="<%= request.getContextPath() %>/images/info.gif" width="15"> 
    </td>
    <td class="infoop">
		<html:link page="/mainExamsNew.do?method=prepare"><strong>Gest&atilde;o de Exames </strong></html:link>
    </td>
  </tr>
</table>
<p>Este m&oacute;dulo visa uma gest&atilde;o eficiente da calendariza&ccedil;&atilde;o dos <strong>exames</strong>.</p>