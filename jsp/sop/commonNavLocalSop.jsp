<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<!-- NOTA: Não foram incluidas tags do beans tipo <bean:message key="title.listClasses"/> -->

<ul>
  <li class="navheader">Gest&atilde;o de Turmas</li>
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
  <li class="navheader">Gest&atilde;o de Turnos</li>
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
  <li class="navheader">Gest&atilde;o de Aulas</li>
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
