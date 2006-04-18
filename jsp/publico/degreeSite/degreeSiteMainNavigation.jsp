<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%
Integer degreeID = (Integer) request.getAttribute("degreeID");
if (degreeID == null) {
	degreeID = Integer.valueOf(request.getParameter("degreeID"));
}
%>

<ul class="treemenu">
	<li>
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" +  degreeID %>" >	 
	    	 <bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.description" /> 
	    </html:link>                
    </li>
    <li>
    	<html:link page="<%= "/showDegreeSite.do?method=showAccessRequirements&amp;degreeID=" + degreeID %>" >
        	<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.accessRequirements"/>
        </html:link>
	</li>
    <li>
    	<html:link page="<%= "/showDegreeSite.do?method=showProfessionalStatus&amp;degreeID=" + degreeID %>" >
        	<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.professionalStatus"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + degreeID %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularPlan"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showExecutionCourseSites.do?method=listSites&amp;degreeOID=" + degreeID + "&amp;showTwoSemesters=true" %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.courseSites"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showClasses.do?method=listClasses&amp;degreeOID=" + degreeID %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.schedulesByClass"/>
        </html:link>
	</li>
<%--
	<li>
    	<html:link page="<%= "/chooseExamsMapContextDA.do?method=choose&amp;degreeID=" + degreeID %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.exams"/>
        </html:link>
	</li>
--%>
	<li>
    	<html:link page="<%= "/degreeSite/publicEvaluations.faces?degreeID=" + degreeID %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.evaluations"/>
        </html:link>
	</li>
	
	<%--
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=viewDegreeEvaluation&amp;degreeID=" + degreeID %>" >
			<bean:message key="link.view.inquiries.results" bundle="INQUIRIES_RESOURCES"/>
        </html:link>
	</li>
	--%>
	
	<%--/viewAllExamsByDegreeAndCurricularYear.do?method=listClasses--%>
</ul>
