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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h3><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForuns.title" /></h3>

<logic:present name="foruns">
	<bean:define id="prefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="contextPrefix" value="<%= prefix + (prefix.contains("?") ? "&amp;" : "?") %>" type="java.lang.String"/>
	<fr:view name="foruns" layout="tabular" schema="forum.view-with-name-description-and-creation-date">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
      		<fr:property name="columnClasses" value="listClasses,"/>
			<fr:property name="link(view)" value="<%= contextPrefix + "method=viewForum" %>"/>
			<fr:property name="param(view)" value="externalId/forumId"/>
			<fr:property name="key(view)" value="messaging.viewForum.link"/>
			<fr:property name="bundle(view)" value="MESSAGING_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:present>