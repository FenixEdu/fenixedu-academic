<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<ul class="treemenu">
	<li>
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeId=" +  request.getAttribute("degreeId") %>" >
	    	<bean:message key="label.description"/>
	    </html:link>                
    </li>
    <li>
    	<html:link page="<%= "/showDegreeSite.do?method=showAccessRequirements&amp;executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeId=" +  request.getAttribute("degreeId") %>" >
        	<bean:message key="label.accessRequirements"/>
        </html:link>
	</li>
	<li>
    	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeId=" +  request.getAttribute("degreeId") %>" >
			<bean:message key="label.curricularPlan"/>
        </html:link>
	</li>
</ul>