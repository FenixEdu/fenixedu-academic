<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<h2><bean:message key="title.masterDegree.administrativeOffice.marksManagement" /></h2>

<logic:present name="oneInfoEnrollment">
	<table width="100%">
		<tr>
			<td class="infoselected">
				<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>:</b>
				<bean:write name="oneInfoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome" />
				<br />
				<b><bean:message key="label.curricularPlan" />:</b>
				<bean:write name="oneInfoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.name" />
				<br />
				<b><bean:message key="label.curricularCourse"/>:</b>
				<bean:write name="oneInfoEnrollment" property="infoCurricularCourse.name" />
			</td>
		</tr>
	</table>
	<br />
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<bean:define id="courseId" name="oneInfoEnrollment" property="infoCurricularCourse.externalId"/>
	<ul>
	    <li>
			<html:link page="<%= "/showMarkDispatchAction.do?method=prepareShowMark&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;courseId=" + courseId + "&amp;objectCode=" + pageContext.findAttribute("objectCode")%>">
	    		<bean:message key="link.masterDegree.administrativeOffice.marksView" />    		
			</html:link>
		</li>
	    <li><html:link page="<%="/marksSubmission.do?method=prepare&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;courseId=" + courseId + "&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
	    		<bean:message key="link.masterDegree.administrativeOffice.marksSubmission" />
	    	</html:link>
	    </li>
	    <li>
			<html:link page="<%="/marksConfirmation.do?method=prepareMarksConfirmation&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;courseId=" + courseId + "&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
	    		<bean:message key="link.masterDegree.administrativeOffice.marksConfirmation" />
			</html:link>
		</li>
	     <li>
			<html:link page="<%= "/changeMarkDispatchAction.do?method=prepareChangeMark&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;courseId=" + courseId + "&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
	    		<bean:message key="link.masterDegree.administrativeOffice.changeMark" /> 		
			</html:link>
		</li>
	</ul>
</logic:present>
     