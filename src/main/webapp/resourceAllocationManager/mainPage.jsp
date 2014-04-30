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

		
