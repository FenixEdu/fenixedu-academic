<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>



<ul class="treemenu">
	<li>
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID")%>" >	 
	    	 <bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.description" /> 
	    </html:link>                
    </li>
    <li>
    	<html:link page="<%= "/showDegreeSite.do?method=showAccessRequirements&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
        	<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.accessRequirements"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" +  request.getAttribute("degreeID") %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularPlan"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showExecutionCourseSites.do?method=listSites&amp;degreeOID=" +  request.getAttribute("degreeID") + "&amp;showTwoSemesters=true" %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.courseSites"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showClasses.do?method=listClasses&amp;degreeOID=" +  request.getAttribute("degreeID") %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.schedulesByClass"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/chooseExamsMapContextDA.do?method=choose&amp;degreeID=" +  request.getAttribute("degreeID") %>" >
			<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.exams"/>
        </html:link>
	</li>
	
	<%--
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=viewDegreeEvaluation&amp;degreeID=" +  request.getAttribute("degreeID") %>" >
			<bean:message key="link.view.degree.evaluation" bundle="INQUIRIES_RESOURCES"/>
        </html:link>
	</li>
	--%>
	
	<%--/viewAllExamsByDegreeAndCurricularYear.do?method=listClasses--%>
</ul>
