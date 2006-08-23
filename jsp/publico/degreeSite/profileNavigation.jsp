<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="institutionUrlStudents" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/aluno.shtml</bean:define>
<bean:define id="institutionUrlTeachers" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/docente.shtml</bean:define>
<bean:define id="institutionUrlEmployees" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/funcionario.shtml</bean:define>
<bean:define id="institutionUrlCandidates" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/candidato.shtml</bean:define>
<bean:define id="institutionUrlInternational" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>html/perfil/international.shtml</bean:define>
<ul>
	<li><a href="<%= institutionUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.home"/></a></li>
  	<li><a href="<%= institutionUrlStudents %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.student"/></a></li>
  	<li><a href="<%= institutionUrlTeachers %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.teacher"/></a></li>
	<li><a href="<%= institutionUrlEmployees %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.staff"/></a></li>
	<li><a href="<%= institutionUrlCandidates %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.candidade"/></a></li>	
  	<li><a href="<%= institutionUrlInternational %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.international"/></a></li>
</ul>