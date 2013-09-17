<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<!-- NOTA: Não foram incluidas tags do beans tipo <bean:message key="title.listClasses"/> -->

<ul>
  <li class="navheader">Gest&atilde;o de Turmas</li>
  <li>
  	<html:link page="<%= "/ClassManagerDA.do?method=createClass&amp;"
  							+ PresentationConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ PresentationConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ PresentationConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Criar turma
  	</html:link>
  </li>
  <li>
  	<html:link page="<%= "/ClassesManagerDA.do?method=listClasses&amp;"
  							+ PresentationConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ PresentationConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ PresentationConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Listar turmas
  	</html:link>
  </li>
  <li class="navheader">Gest&atilde;o de Turnos</li>
  <li>
  	<html:link page="<%= "/prepararCriarTurno.do?"
  							+ PresentationConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ PresentationConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ PresentationConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Criar turno
  	</html:link>
  </li>
  <li>
  	<html:link page="<%= "/escolherDisciplinaETipoForm.do?"
  							+ PresentationConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ PresentationConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ PresentationConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Listar turnos por disciplina
  	</html:link>
  </li>
  <li class="navheader">Gest&atilde;o de Aulas</li>
  <li>
  	<html:link page="<%= "/prepararAula.do?"
  							+ PresentationConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ PresentationConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ PresentationConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Criar aulas
  	</html:link>
  </li>
  <li>
  	<html:link page="<%= "/escolherDisciplinaExecucaoForm.do?"
  							+ PresentationConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
  							+ "&amp;"
  							+ PresentationConstants.CURRICULAR_YEAR_OID
  							+ "="
  							+ pageContext.findAttribute("curricularYearOID")
  							+ "&amp;"
  							+ PresentationConstants.EXECUTION_DEGREE_OID
  							+ "="
  							+ pageContext.findAttribute("executionDegreeOID") %>">
  		Alterar aulas
  	</html:link>
  </li>
</ul>
