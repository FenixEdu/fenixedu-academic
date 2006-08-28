<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2>
	<bean:message key="link.teacherServiceDistribution"/>
</h2>

<ul>
	<li>
	  	<html:link page="/globalTeacherServiceDistributionValuation.do?method=prepareForGlobalTeacherServiceDistributionValuation">
	  		<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionVisualization"/>
	  	</html:link>
	</li>
<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">
	<li>
	  	<html:link page="/teacherServiceDistribution.do?method=prepareForTeacherServiceDistributionCreation">
	  		<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionCreation"/>
	  	</html:link>
	</li>
</logic:present>
	<li>
	  	<html:link page="/teacherServiceDistribution.do?method=prepareForTeacherServiceDistributionEdition">
	  		<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionEdition"/>
	  	</html:link>
	</li>	
</ul>
