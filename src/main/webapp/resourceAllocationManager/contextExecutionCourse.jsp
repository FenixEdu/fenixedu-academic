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
<html:xhtml/>

<logic:present name="executionCourse">
	
	<logic:equal name="executionCourse" property="class.name" value="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
		<h3>
			<bean:write name="executionCourse" property="nome"/>
			(<bean:write name="executionCourse" property="sigla"/>)
		</h3>
		<p>
			<bean:write name="executionCourse" property="academicInterval.pathName"/>
		</p>
	</logic:equal>
	
	<logic:equal name="executionCourse" property="class.name" value="net.sourceforge.fenixedu.domain.ExecutionCourse">
		<h3>
			<bean:write name="executionCourse" property="nome"/>
			(<bean:write name="executionCourse" property="sigla"/>)
		</h3>
		<p>
			<bean:write name="executionCourse" property="academicInterval.pathName"/>
		</p>		
	</logic:equal>
	
</logic:present>

<%--
<logic:notPresent name="executionCourse">	
	<logic:present name="executionPeriod">
		<p>
			<bean:write name="executionPeriod" property="infoExecutionYear.year"/> - 
			<bean:write name="executionPeriod" property="name"/>
		</p>
	</logic:present>	
</logic:notPresent>
--%>