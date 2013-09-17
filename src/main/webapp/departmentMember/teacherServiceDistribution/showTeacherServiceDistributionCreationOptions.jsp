<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

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
