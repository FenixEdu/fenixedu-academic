<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="curricularCourses" name="component" property="associatedDegrees" />

<h2 class="brown"><bean:message key="label.curricular.information"/></h2>
<ul>
	<logic:iterate id="curricularCourse" name="curricularCourses">
		<bean:define id="curricularCourseId" name="curricularCourse" property="idInternal" />
		<bean:define id="degreeID" name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.idInternal" />
		<bean:define id="degreeCurricularPlanID" name="curricularCourse" property="infoDegreeCurricularPlan.idInternal" />

		<bean:define id="initialDate" name="curricularCourse" property="infoDegreeCurricularPlan.initialDate" />
		
		<li><html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseId") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  pageContext.getAttribute("degreeID") %>"> 	
			<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>	
			<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
		</html:link></li>
	</logic:iterate>
</ul>

