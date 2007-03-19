<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.writtenEvaluationReport"/></h2>
<bean:define id="writtenEvaluationId" name="writtenEvaluation" property="idInternal"/>

<ul>
	<li><html:link page="/vigilancy/vigilantManagement.do?method=prepareMap"><bean:message key="label.vigilancy.back" bundle="VIGILANCY_RESOURCES"/></html:link>
</ul>
<p class="mtop2 mbottom05"><strong><bean:message key="label.vigilancy.course" bundle="VIGILANCY_RESOURCES"/>:</strong> <span class="highlight1"><fr:view name="writtenEvaluation" property="fullName"/></span></p>
<p class="mvert05"><strong><bean:message key="label.vigilancy.date" bundle="VIGILANCY_RESOURCES"/>:</strong> <fr:view name="writtenEvaluation" property="beginningDateTime"/></p>
<p class="mvert05"><strong><bean:message key="label.vigilancy.associatedRooms" bundle="VIGILANCY_RESOURCES"/></strong>: 
	<logic:notEmpty name="writtenEvaluation" property="associatedRooms">  
	<fr:view name="writtenEvaluation" property="associatedRooms">
	<fr:layout name="flowLayout">
				<fr:property name="eachLayout" value="values"/>
				<fr:property name="eachSchema" value="presentRooms"/>
				<fr:property name="htmlSeparator" value=","/>
	</fr:layout>
	</fr:view>
	</logic:notEmpty>
	<logic:empty name="writtenEvaluation" property="associatedRooms">  
		<em><bean:message key="label.vigilancy.associatedRoomsUnavailable" bundle="VIGILANCY_RESOURCES"/></em>
	</logic:empty>
</p>

<logic:notEmpty name="writtenEvaluation" property="teachersVigilancies">
<strong><bean:message key="label.vigilancy.vigilantsThatTeachCourse" bundle="VIGILANCY_RESOURCES"/>:</strong><br/>
<table class="tstyle1">
	<tr>
		<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>
	<logic:iterate id="vigilancy" name="writtenEvaluation" property="teachersVigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
	<bean:define id="vigilancy" name="vigilancy" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy"/>
		
	<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
		<td><fr:view name="vigilancy" property="vigilant.teacherCategoryCode"/></td>
			<td><fr:view name="vigilancy" property="vigilant.person.username"/></td>
			<td><fr:view name="vigilancy" property="vigilant.person.name"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="active"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="attended"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="points"/></td>
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:notEmpty name="writtenEvaluation" property="othersVigilancies">
<strong><bean:message key="label.vigilancy.vigilants" bundle="VIGILANCY_RESOURCES"/>:</strong><br/>
<table class="tstyle1">
	<tr>
		<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></th>

	</tr>
<logic:iterate id="vigilancy" name="writtenEvaluation" property="othersVigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
		
	<bean:define id="vigilancy" name="vigilancy" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy"/>
		
	<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
		<td><fr:view name="vigilancy" property="vigilant.teacherCategoryCode"/></td>
			<td><fr:view name="vigilancy" property="vigilant.person.username"/></td>
			<td><fr:view name="vigilancy" property="vigilant.person.name"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="active"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="confirmed"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="attended"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="points"/></td>
	</tr>
</logic:iterate>
</table>
</logic:notEmpty>