<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page="/teacherServiceDistribution.do?method=prepareForTeacherServiceDistributionCreation">
		<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionCreation"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherEmptyServiceDistributionCreation"/>
</h3>

<ul>
<li><html:link page="/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution">
	<bean:message key="link.back"/>
</html:link>
</li>
</ul>


<fr:edit id="createDistribution" name="bean" schema="createTeacherDistribution" action="/teacherServiceDistribution.do?&method=createTeacherServiceDistributionNew">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
	<fr:destination name="cancel" path="/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution"/>
</fr:edit>
