<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>



<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="institutionUrlStudents" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>pt/alunos/</bean:define>
<bean:define id="institutionUrlTeachers" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>pt/docentes/</bean:define>
<bean:define id="institutionUrlEmployees" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>pt/pessoal/</bean:define>
<bean:define id="institutionUrlCandidates" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>pt/candidatos/</bean:define>
<bean:define id="institutionUrlInternational" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>en/</bean:define>
<bean:define id="institutionUrlAlumni" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>pt/alumni/</bean:define>

<ul>
	<li><a href="<%= institutionUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.home"/></a></li>
  	<li><a href="<%= institutionUrlStudents %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.student"/></a></li>
  	<li><a href="<%= institutionUrlTeachers %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.teacher"/></a></li>
	<li><a href="<%= institutionUrlEmployees %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.staff"/></a></li>
	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= institutionUrlCandidates %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.candidade"/></a></li>	
  	<li><a href="<%= institutionUrlInternational %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.international"/></a></li>
  	<li><a href="<%= institutionUrlAlumni %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.alumni"/></a></li>
</ul>
