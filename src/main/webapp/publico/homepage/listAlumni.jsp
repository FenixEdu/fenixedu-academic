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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<logic:present name="homepages">
	<logic:notPresent name="selectedPage">
		<logic:iterate id="entry" name="homepages">
			<html:link page="/viewHomepage.do?method=listAlumni" paramId="selectedPage" paramName="entry" paramProperty="key.externalId">
				<bean:message name="entry" property="key.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
				<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
				<bean:write name="entry" property="key.name"/>
			</html:link>
			<bean:size id="numberHomepages" name="entry" property="value"/>
			<bean:write name="numberHomepages"/>
			<br/>
		</logic:iterate>
	</logic:notPresent>
	<logic:present name="selectedPage">
		<bean:define id="selectedPage" type="java.lang.String" name="selectedPage"/>
		<br/>
		<logic:iterate id="entry" name="homepages">
			<logic:equal name="entry" property="key.externalId" value="<%= selectedPage %>">
				<bean:message name="entry" property="key.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
				<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
				<bean:write name="entry" property="key.name"/>
				<br/>
				<br/>
				<logic:iterate id="homepage" name="entry" property="value">
					<bean:write name="homepage" property="person.user.username"/>
					<html:link action="/viewHomepage.do?method=show" paramId="homepageID" paramName="homepage" paramProperty="externalId">
						<bean:write name="homepage" property="name"/>
					</html:link>
					<br/>
				</logic:iterate>
			</logic:equal>
		</logic:iterate>
	</logic:present>
</logic:present>