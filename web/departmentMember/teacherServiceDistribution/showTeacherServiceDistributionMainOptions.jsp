<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

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
<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">
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
