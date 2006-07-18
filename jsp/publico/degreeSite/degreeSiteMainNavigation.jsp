<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%
Integer degreeID = (Integer) request.getAttribute("degreeID");
if (degreeID == null) {
	degreeID = Integer.valueOf(request.getParameter("degreeID"));
}

String aditionalParameter = "";
if (request.getAttribute("executionDegreeID") != null) {
    Integer executionDegreeID = (Integer) request.getAttribute("executionDegreeID");
	aditionalParameter = (executionDegreeID != null) ? "&amp;executionDegreeID=" + executionDegreeID : "";
}
%>

<ul class="treemenu">
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID="+ degreeID + aditionalParameter%>" >
	    	 <bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.description" /> 
	    </html:link>                
    </li>
    <li>
    	<html:link page="<%= "/showDegreeSite.do?method=showAccessRequirements&amp;degreeID="+ degreeID + aditionalParameter%>" >
        	<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.accessRequirements"/>
        </html:link>
	</li>
    <li>
    	<html:link page="<%= "/showDegreeSite.do?method=showProfessionalStatus&amp;degreeID="+ degreeID + aditionalParameter%>" >
        	<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.professionalStatus"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID="+ degreeID + aditionalParameter%>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularPlan"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showExecutionCourseSites.do?method=listSites&amp;degreeOID="+ degreeID + aditionalParameter+ "&amp;showTwoSemesters=true" %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.courseSites"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showClasses.do?method=listClasses&amp;degreeOID="+ degreeID + aditionalParameter%>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.schedulesByClass"/>
        </html:link>
	</li>
<%--
	<li>
    	<html:link page="<%= "/chooseExamsMapContextDA.do?method=choose&amp;degreeID="+ degreeID + aditionalParameter%>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.exams"/>
        </html:link>
	</li>
--%>
	<li>
    	<html:link page="<%= "/degreeSite/publicEvaluations.faces?degreeID="+ degreeID + aditionalParameter%>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.evaluations"/>
        </html:link>
	</li>
	
	<%--
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=viewDegreeEvaluation&amp;degreeID="+ degreeID + aditionalParameter%>" >
			<bean:message key="link.view.inquiries.results" bundle="INQUIRIES_RESOURCES"/>
        </html:link>
	</li>
	--%>
	
	<%--/viewAllExamsByDegreeAndCurricularYear.do?method=listClasses--%>
</ul>
