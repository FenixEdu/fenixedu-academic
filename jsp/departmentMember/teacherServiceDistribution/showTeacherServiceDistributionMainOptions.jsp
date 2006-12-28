<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
	<bean:message key="link.teacherServiceDistribution"/>
</h2>

<fr:edit name="bean" id="selectYearAndPeriod" schema="selectPeriod" action="/teacherServiceDistribution.do?method=showTeacherServiceDistributions">
	<fr:destination name="cancel" path="/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution"/>
</fr:edit>

<logic:notEmpty name="bean" property="teacherServiceDistribution"> 
<fr:view name="bean" property="teacherServiceDistribution" schema="presentTeacherServiceDistribution">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
		<fr:property name="key(visualizar)" value="label.visualize"/>
		<fr:property name="link(visualizar)" value="/teacherServiceDistributionValuation.do?method=prepareForTeacherServiceDistributionValuation"/>
		<fr:property name="param(visualizar)" value="idInternal/teacherServiceDistribution"/>
		<fr:property name="order(visualizar)" value="1"/>
		<fr:property name="key(edit)" value="label.edit"/>
		<fr:property name="link(edit)" value="/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices"/>
		<fr:property name="param(edit)" value="idInternal/teacherServiceDistribution"/>
		<fr:property name="order(edit)" value="2"/>
		<fr:property name="key(copy)" value="label.copy"/>
		<fr:property name="link(copy)" value="/teacherServiceDistribution.do?method=prepareCopyTeacherServiceDistributionNew"/>
		<fr:property name="param(copy)" value="idInternal/did"/>		
		<fr:property name="order(copy)" value="3"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">
<ul>
	<li>
	  	<html:link page="/teacherServiceDistribution.do?method=prepareForEmptyTeacherServiceDistributionCreationNew">
	  		<bean:message key="link.teacherServiceDistribution.teacherEmptyServiceDistributionCreation"/>
	  	</html:link>
	</li>
</ul>
</logic:present>

