<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionCreation"/>
</h3>

<ul>
	<li>
	  	<html:link page="/teacherServiceDistribution.do?method=prepareForEmptyTeacherServiceDistributionCreation">
	  		<bean:message key="link.teacherServiceDistribution.teacherEmptyServiceDistributionCreation"/>
	  	</html:link>
	</li>
	<li>
	  	<html:link page="/teacherServiceDistributionCopy.do?method=prepareForTeacherServiceDistributionCopy">
	  		<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionCopy"/>
	  	</html:link>
	</li>
</ul>
<br/>
<br/>
<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
	<bean:message key="link.back"/>
</html:link>
