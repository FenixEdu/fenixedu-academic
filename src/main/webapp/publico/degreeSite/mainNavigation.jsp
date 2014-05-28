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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<ul class="treemenu">
	<li><a href="/html/instituto/">Instituto</a></li>
	<li><a href="/html/estrutura/">Estrutura</a></li>
  	<li class="treenode"><a href="">Ensino</a>
		<ul class="expmenu">
			<li><html:link page="<%= "/showDegrees.do?method=nonMaster" %>" ><bean:message key="link.degree.nonMaster"/></html:link></li>
			<bean:define id="institutionUrlPostGraduation" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/ensino/pos_grad.html</bean:define>
			<li><a href="<%= institutionUrlPostGraduation %>">Pós-graduações</a></li>
			<li><html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) %>" ><bean:message key="link.degree.master"/></html:link></li>
			<bean:define id="institutionUrlPhd" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/ensino/doutoramentos.html</bean:define>
			<li><a href="<%= institutionUrlPhd %>">Doutoramentos</a></li>
		</ul>
	</li>
	<li><a href="/html/id/">I &amp; D</a></li>
	<li><a href="/html/viverist/">Viver no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a></li>
</ul>
