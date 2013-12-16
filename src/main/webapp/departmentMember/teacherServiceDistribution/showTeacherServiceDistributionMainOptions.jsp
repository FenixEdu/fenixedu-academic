<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<h2>
	<bean:message key="link.teacherServiceDistribution"/>
</h2>

<ul>
	<li>
	  	<html:link page="/globalTSDProcessValuation.do?method=prepareForGlobalTSDProcessValuation">
	  		<bean:message key="link.teacherServiceDistribution.tsdProcessVisualization"/>
	  	</html:link>
	</li>
<logic:present role="role(DEPARTMENT_ADMINISTRATIVE_OFFICE)">
	<li>
	  	<html:link page="/tsdProcess.do?method=prepareForTSDProcessCreation">
	  		<bean:message key="link.teacherServiceDistribution.tsdProcessCreation"/>
	  	</html:link>
	</li>
</logic:present>
	<li>
	  	<html:link page="/tsdProcess.do?method=prepareForTSDProcessEdition">
	  		<bean:message key="link.teacherServiceDistribution.tsdProcessEdition"/>
	  	</html:link>
	</li>	
</ul>
