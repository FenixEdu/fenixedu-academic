<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<div class="page-header">
	<h1>
		<bean:message key="label.candidacy.move.candidate" bundle="APPLICATION_RESOURCES"/>
	</h1>
</div>
<bean:define id="individualProcess" name="individualProcess" />
<bean:define id="processName" value='<%= individualProcess.getClass().getSimpleName() %>' />
<bean:define id="individualProcessId" name="individualProcess" property="externalId"/>
<bean:define id="backLink" value='<%= "/academicAdministration/caseHandling" + processName + ".do?method=listProcessAllowedActivities&amp;processId="+ individualProcessId  %>'/>
<a href="${backLink}"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></a>

<h4>
<bean:message key="label.candidacy.choose.new.candidacy" bundle="APPLICATION_RESOURCES"/>
</h4>


<bean:define id="action" value='<%= "/academicAdministration/caseHandling" + processName + ".do?method=executeMoveCandidacy&amp;processId="+ individualProcessId  %>'/>

<form role="form" method="post" action="${action}" enctype="multipart/form-data">
<input type="hidden" class="form-control" id="process" name="process" value="${process.externalId}"/>
<select class="form-control" id="newParentProcess" name="newParentProcess" >
 	<logic:iterate name="processes" id="process"> 
		<option value="${process.externalId}">
			<bean:write name="process" property="presentationName"/>
		</option>
	</logic:iterate>
</select>
<p>
<button type="submit" class="btn btn-primary"><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></button>
</p>
</form>