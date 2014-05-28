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
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:html xhtml="true"/>

<h2>
	<bean:message key="label.person.edit.irs.declaration.link"  />
</h2>

<fr:edit id="declarationBean" name="declarationBean" action="/irsDeclaration.do?method=editBean">
	<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.person.IRSDeclarationAction$IRSDeclarationBean" bundle="APPLICATION_RESOURCES">
		<fr:slot name="title" key="label.person.title" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
		<fr:slot name="irsLink" key="label.person.link" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
		<fr:slot name="available" key="label.person.available" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>

    <%
    	org.fenixedu.bennu.core.domain.Bennu rootDomainObject = org.fenixedu.bennu.core.domain.Bennu.getInstance();
    	if (rootDomainObject.getIrsDeclarationLink() != null) {
    	    %>
    				<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= rootDomainObject.getIrsDeclarationLink().getIrsLink() %>">
    					<%= rootDomainObject.getIrsDeclarationLink().getTitle().getContent() %>
    				</a>
<%
    	}
%>