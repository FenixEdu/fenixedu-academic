<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<table width="50%" border="0" align="center" cellpadding="0" cellspacing="2">
  <tr>
    <td width="20%" nowrap class="navopgeral-td"><html:link page="/home.do">Home</html:link></td>
	<td width="20%" nowrap class="navopgeralSelected-td"><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0">Gestão de Disciplinas</html:link></td>
    <td width="20%" nowrap class="navopgeral-td"><html:link page="/chooseExecutionPeriod.do?method=prepare">Gestão de Horários</html:link></td>
    <td width="20%" nowrap class="navopgeral-td"><html:link page="/principalSalas.do">Gestão de Salas</html:link></td>
    <td width="20%" nowrap class="navopgeral-td"><html:link page="/mainExams.do?method=prepare">Gestão de Exames</html:link></td>
	<td width="20%" nowrap class="centerContent"><html:link forward="logoff"><img alt="" border="0" src="<%= request.getContextPath() %>/images/logout.gif"></html:link></td>
  </tr>
</table>