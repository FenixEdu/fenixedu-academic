<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<!-- NOTA: Não foram incluidas tags do beans tipo <bean:message key="title.listClasses"/> -->
<p><strong>&raquo; Gest&atilde;o de Turmas</strong></p>
<ul>
  <li>
  	<html:link page="<%= "/ClassManagerDA.do?method=createClass&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ SessionConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ SessionConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Criar turma
  	</html:link>
  </li>
  <li>
  	<html:link page="<%= "/ClassesManagerDA.do?method=listClasses&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ SessionConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ SessionConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Listar turmas
  	</html:link>
  </li>
</ul>

<p><strong>&raquo; 
  	<html:link page="<%= "/manageShifts.do?method=listShifts&amp;page=0&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ SessionConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ SessionConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
		Gest&atilde;o de Turnos
  	</html:link>
</strong></p>
<ul>
  <li>
  	<html:link page="<%= "/prepararCriarTurno.do?"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ SessionConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ SessionConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Criar turno
  	</html:link>
  </li>
  <li>
  	<html:link page="<%= "/escolherDisciplinaETipoForm.do?"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ SessionConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ SessionConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Listar turnos por disciplina
  	</html:link>
  </li>
</ul>

<p><strong>&raquo; Gest&atilde;o de Aulas</strong></p>
<ul>
  <li>
  	<html:link page="<%= "/prepararAula.do?"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ SessionConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ SessionConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Criar aulas
  	</html:link>
  </li>
  <li>
  	<html:link page="<%= "/escolherDisciplinaExecucaoForm.do?"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ SessionConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ SessionConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Alterar aulas
  	</html:link>
  </li>
</ul>
