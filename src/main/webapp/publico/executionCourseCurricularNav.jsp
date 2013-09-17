<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="curricularCourses" name="component" property="associatedDegrees" />

<h2 class="brown"><bean:message key="label.curricular.information"/></h2>
<ul>
	<logic:iterate id="curricularCourse" name="curricularCourses">
		<bean:define id="curricularCourseId" name="curricularCourse" property="externalId" />
		<bean:define id="degreeID" name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.externalId" />
		<bean:define id="degreeCurricularPlanID" name="curricularCourse" property="infoDegreeCurricularPlan.externalId" />

		<bean:define id="initialDate" name="curricularCourse" property="infoDegreeCurricularPlan.initialDate" />
		
		<li><html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseId") + "&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  pageContext.getAttribute("degreeID") %>"> 	
			<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>	
			<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
		</html:link></li>
	</logic:iterate>
</ul>

