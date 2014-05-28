<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>



<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>
<bean:define id="institutionUrlStudents" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/alunos/</bean:define>
<bean:define id="institutionUrlTeachers" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/docentes/</bean:define>
<bean:define id="institutionUrlEmployees" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/pessoal/</bean:define>
<bean:define id="institutionUrlCandidates" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/</bean:define>
<bean:define id="institutionUrlInternational" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/</bean:define>
<bean:define id="institutionUrlAlumni" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/alumni/</bean:define>

<ul>
	<li><a href="<%= institutionUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.home"/></a></li>
  	<li><a href="<%= institutionUrlStudents %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.student"/></a></li>
  	<li><a href="<%= institutionUrlTeachers %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.teacher"/></a></li>
	<li><a href="<%= institutionUrlEmployees %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.staff"/></a></li>
	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= institutionUrlCandidates %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.candidade"/></a></li>	
  	<li><a href="<%= institutionUrlInternational %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.international"/></a></li>
  	<li><a href="<%= institutionUrlAlumni %>"><bean:message bundle="GLOBAL_RESOURCES" key="link.alumni"/></a></li>
</ul>
