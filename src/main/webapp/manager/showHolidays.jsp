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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2>
	<bean:message bundle="MANAGER_RESOURCES" key="label.manage.holidays"/>
</h2>
<bean:message bundle="MANAGER_RESOURCES" key="message.manage.holidays"/>
<br/>
<br/>
<fr:create type="net.sourceforge.fenixedu.domain.Holiday$HolidayFactoryCreator"
		schema="HolidayFactoryCreatory"
		action="/manageHolidays.do?method=create&amp;page=0">
</fr:create>
<br/>
<br/>
<logic:present name="holidays">
	<fr:view name="holidays" schema="Holiday" layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
     		<fr:property name="columnClasses" value="listClasses"/>
			<fr:property name="link(delete)" value="/manageHolidays.do?method=delete"/>
			<fr:property name="param(delete)" value="externalId/holidayID"/>
			<fr:property name="key(delete)" value="link.delete"/>
			<fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:present>
