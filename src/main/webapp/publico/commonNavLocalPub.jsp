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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<ul>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/instituto/"><bean:message key="label.institution" bundle="GLOBAL_RESOURCES"/></a></li>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/estrutura/"><bean:message key="label.structure" bundle="GLOBAL_RESOURCES"/></a></li>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/ensino/"><bean:message key="label.education" bundle="GLOBAL_RESOURCES"/></a></li>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/id/"><bean:message key="label.research.and.development.acronyms" bundle="GLOBAL_RESOURCES"/></a></li>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/viverist/"><bean:message key="label.life.at.ist" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="GLOBAL_RESOURCES"/></a></li>
</ul>