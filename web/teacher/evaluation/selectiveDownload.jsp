<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="message.evaluationElements" bundle="APPLICATION_RESOURCES" /></em>
<h2><bean:message key="label.teacher.executionCourseManagement.evaluation.project.partsDownload" bundle="APPLICATION_RESOURCES"/></h2>
 
<bean:define id="executionCourseID" value="<%= request.getParameter("executionCourseID")%>"/>
<bean:define id="projectOID" name="project" property="externalId"/>
<ul>
<li>
<html:link page="<%= "/projectSubmissionsManagement.do?method=viewLastProjectSubmissionForEachGroup&executionCourseID=" + executionCourseID + "&projectOID=" + projectOID%>">
			<bean:message key="label.return" bundle="APPLICATION_RESOURCES"/>
</html:link>
</li>
</ul>

<div class="infoop2">
<bean:message key="label.teacher.executionCourseManagement.evaluation.project.partsDownloadExplanation" bundle="APPLICATION_RESOURCES"/>
</div>

<fr:hasMessages>
<div class="error2">
<fr:messages>
<fr:message/>
</fr:messages>
</div>
</fr:hasMessages>

<table>
<tr>
<td>
<strong><bean:message key="label.teacher.executionCourseManagement.evaluation.project.projectNumber" bundle="APPLICATION_RESOURCES"/></strong>:
</td>
<td>
<fr:form action="<%= "/projectSubmissionsManagement.do?method=prepareSelectiveDownload&amp;executionCourseID=" + executionCourseID + "&amp;projectOID=" + projectOID %>">
<fr:edit id="selectiveDownload" name="bean" slot="integer" type="java.lang.Integer">
	<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
	<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.NumberRangeValidator">
		<fr:property name="lowerBound" value="1"/>
	</fr:validator>
</fr:edit>
<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
</fr:form>
</td>
</tr>
</table>
 
<logic:notEmpty name="projectSubmissions">
<ul>
  <bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.VariantBean"/>
	<bean:size id="size" name="projectSubmissions"/>
  <bean:define id="lastShown" value="0"/>
	<logic:iterate id="project" name="projectSubmissions" indexId="index">
		<bean:define id="naturalDivision" value="<%= String.valueOf(index % bean.getInteger()) %>"/>	
		<logic:equal  name="naturalDivision" value="0">
			<bean:define id="lastShown" value="<%= String.valueOf(index + bean.getInteger()) %>"/>
			<li>
			<html:link page="<%= "/projectSubmissionsManagement.do?method=selectiveDownload&amp;executionCourseID=" + executionCourseID + "&amp;projectOID=" + projectOID + "&amp;startIndex=" + index + "&amp;size=" + bean.getInteger() %>">
					<bean:message key="label.teacher.executionCourseManagement.evaluation.project.projectsFromTo" arg0="<%= String.valueOf(index +1) %>" arg1="<%=  String.valueOf(Math.min(bean.getInteger()+index,size)) %>" bundle="APPLICATION_RESOURCES"/><br/>
			</html:link>
			</li>
		</logic:equal> 
	</logic:iterate>


	<logic:greaterThan name="size" value="<%= lastShown %>">
		<bean:define id="numberOfResults" value="<%= String.valueOf(size - Integer.valueOf(lastShown))  %>"/>
		<li>
		<html:link page="<%= "/projectSubmissionsManagement.do?method=selectiveDownload&amp;executionCourseID=" + executionCourseID + "&amp;projectOID=" + projectOID + "&amp;startIndex=" + lastShown + "&amp;size=" + numberOfResults %>">
					<bean:message key="label.teacher.executionCourseManagement.evaluation.project.projectsFromTo" arg0="<%= String.valueOf(lastShown) %>" arg1="<%=  String.valueOf(size) %>" bundle="APPLICATION_RESOURCES"/><br/>
		</html:link>		
		</li>
	</logic:greaterThan>
</ul>
</logic:notEmpty>
