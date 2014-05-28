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
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<html:xhtml/>

<bean:define id="component" name="siteView" property="component" />
<bean:define id="lessonList" name="component" property="lessons" />
<bean:define id="executionPeriod" name="component" property="infoExecutionPeriod" />
<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear" />
	
<h2 class="greytxt">
	<bean:write name="executionYear" property="year" />,
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr" />
	<bean:write name="executionPeriod" property="semester" />
</h2>

<h2><bean:message key="property.class" />: <bean:write name="className" /></h2>
<app:gerarHorario name="lessonList"/>
