<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdProcessCreation"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.tsdProcessCreation"/>
	</em>
</p>


<ul>
	<li>
	  	<html:link page="/tsdProcess.do?method=prepareForEmptyTSDProcessCreation">
	  		<bean:message key="link.teacherServiceDistribution.teacherEmptyServiceDistributionCreation"/>
	  	</html:link>
	</li>
	<li>
	  	<html:link page="/tsdProcessCopy.do?method=prepareForTSDProcessCopy">
	  		<bean:message key="link.teacherServiceDistribution.tsdProcessCopy"/>
	  	</html:link>
	</li>
</ul>
<br/>
<br/>
<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
	<bean:message key="link.back"/>
</html:link>
