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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml/>


<bean:define id="dotTitle" type="java.lang.String"><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/></bean:define>

<h2><bean:message key="title.resourceAllocationManager.management"/></h2>

<bean:message key="introduction.message" arg0="<%= dotTitle %>"/>


<table class="mtop15" width="98%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img height="15" src="<%= request.getContextPath() %>/images/info.gif" alt="<bean:message key="info" bundle="IMAGE_RESOURCES" />" width="15" /> 
    </td>
    <td class="infoop"><strong><html:link page="/chooseExecutionPeriod.do?method=prepare"><bean:message key="link.schedules.management" bundle="SOP_RESOURCES"/></html:link></strong></td>
  </tr>
</table>
<p><bean:message key="introduction.schedules"/></p>

<br />

<table width="98%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img height="15" src="<%= request.getContextPath() %>/images/info.gif" alt="<bean:message key="info" bundle="IMAGE_RESOURCES" />" width="15" /> 
    </td>
    <td class="infoop"><strong><html:link page="/mainExams.do?method=prepare"><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></html:link></strong></td>
  </tr>
</table>
<p><bean:message key="introduction.writtenEvaluation.management"/></p>

<br />

<table width="98%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img height="15" src="<%= request.getContextPath() %>/images/info.gif" alt="<bean:message key="info" bundle="IMAGE_RESOURCES" />" width="15" /> 
    </td>
    <td class="infoop"><strong><html:link page="/manageExecutionCourses.do?method=prepareSearch&amp;page=0"><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></html:link></strong></td>
  </tr>
</table>
<p><bean:message key="introduction.disciplinesExecution"/></p>

<br/>

<table width="98%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img height="15" src="<%= request.getContextPath() %>/images/info.gif" alt="<bean:message key="info" bundle="IMAGE_RESOURCES" />" width="15" /> 
    </td>
    <td class="infoop"><strong><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></html:link></strong></td>
  </tr>
</table>

<p><bean:message key="message.info.sop" bundle="CURRICULUM_HISTORIC_RESOURCES"/></p>

<br/>

		
