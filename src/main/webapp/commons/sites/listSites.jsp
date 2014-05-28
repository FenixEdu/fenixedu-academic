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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<table class="tstyle1 mtop05">
    <tr>
        <th><bean:message key="label.site.type" bundle="SITE_RESOURCES"/></th>
        <th>&nbsp;</th>
    </tr>
    <logic:iterate id="type" type="java.lang.Class" name="types">
        <tr>
            <td>
                <fr:view name="type" layout="label">
                    <fr:layout>
                        <fr:property name="bundle" value="SITE_RESOURCES"/>
                    </fr:layout>
                </fr:view>
            </td>
            <td>
                <html:link page="/manageSites.do?method=manageSite" paramId="type" paramName="type" paramProperty="name">
                    <bean:message key="link.site.manage" bundle="SITE_RESOURCES"/>
                </html:link>
            </td>
        </tr>
    </logic:iterate>
</table>