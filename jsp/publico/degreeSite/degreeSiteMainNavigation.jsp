<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


<logic:notPresent name="inEnglish">
<ul class="treemenu">
	<li>
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID")%>" >
	    	<bean:message key="label.description"/>
	    </html:link>                
    </li>
    <li>
    	<html:link page="<%= "/showDegreeSite.do?method=showAccessRequirements&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
        	<bean:message key="label.accessRequirements"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" +  request.getAttribute("degreeID") %>" >
			<bean:message key="label.curricularPlan"/>
        </html:link>
	</li>
</ul>
</logic:notPresent>


<logic:present name="inEnglish">
<ul class="treemenu">
	<li>
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;inEnglish=true&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
	    	<bean:message key="label.description.en"/>
	    </html:link>                
    </li>
    <li>
    	<html:link page="<%= "/showDegreeSite.do?method=showAccessRequirements&amp;inEnglish=true&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
        	<bean:message key="label.accessRequirements.en"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;inEnglish=true&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
			<bean:message key="label.curricularPlan.en"/>
        </html:link>
	</li>
</ul>
</logic:present>