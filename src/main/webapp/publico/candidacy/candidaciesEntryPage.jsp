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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml/>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1>Licenciaturas</h1>

<p>O ingresso nas licenciaturas do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> poderá ser efectuado através dos seguintes mecanismos de acesso:</p>

<ul>
	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/lic/concurso_nacional_acesso" %>">Concurso Nacional de Acesso</a></li>
	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/perfil/candidato/lic/mud-transf-curso.shtml">Mudanças de Curso e Transferências</a></li>
	<li>Concurso Especial de Acesso
		<ul style="margin-top: 0.5em;">
			<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/perfil/candidato/lic/curso-medio.shtml">Titulares de Cursos Médios e Superiores</a></li>
			<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= request.getContextPath() + "/candidaturas/lic/maiores_vinte_tres" %>'>Maiores de 23 anos</a></li>
		</ul>
	</li>
</ul>
