<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.create"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.convokes"/></h2>

<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/checkall.js"></script>

<fr:form action="/vigilancy/convokeManagement.do?method=generateConvokesSugestion">
<fr:edit 
		 id="selectEvaluation"
		 name="bean" 
		 scope="request" 
		 nested="true"
		 schema="selectWrittenEvaluation"> 
	<fr:destination name="cancel" path="/vigilancy/convokeManagement.do?method=prepareConvoke"/> 
</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<logic:present name="bean" property="selectedVigilantGroup">
<logic:present name="bean" property="writtenEvaluation">
<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean"/>
<bean:define id="groupName" name="bean" property="selectedVigilantGroup.name" type="java.lang.String"/>

<p class="mvert15 breadcumbs"><span class="actual"><bean:message key="label.vigilancy.firstStep" bundle="VIGILANCY_RESOURCES"/></span> > <span><bean:message key="label.vigilancy.secondStep" bundle="VIGILANCY_RESOURCES"/></span></p>

<div class="infoop2">
<bean:message key="label.vigilancy.convokeInstructions" bundle="VIGILANCY_RESOURCES" arg0="<%= groupName %>"/>
</div>
 
<div class="mtop2">

<p class="mtop2 mbottom05"><strong><bean:message key="label.vigilancy.associatedRooms" bundle="VIGILANCY_RESOURCES"/></strong>: 
<logic:notEmpty name="bean" property="writtenEvaluation.associatedRooms">  
<fr:view name="bean" property="writtenEvaluation.associatedRooms">
<fr:layout name="flowLayout">
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="presentRooms"/>
			<fr:property name="htmlSeparator" value=","/>
</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:empty name="bean" property="writtenEvaluation.associatedRooms">  
	<em><bean:message key="label.vigilancy.associatedRoomsUnavailable" bundle="VIGILANCY_RESOURCES"/></em>
</logic:empty>
</p>

<logic:notEmpty name="bean" property="writtenEvaluation.vigilancies"> 
<p class="mbottom05"><strong><bean:message key="label.vigilancy.alreadyConvoked" bundle="VIGILANCY_RESOURCES"/></strong>:
<fr:view name="bean" property="writtenEvaluation.vigilancies">
	<fr:layout name="flowLayout">
		<fr:property name="eachInline" value="false"/>
		<fr:property name="eachSchema" value="showVigilantsFromConvokes"/>
		<fr:property name="eachLayout" value="values"/>
	</fr:layout>
</fr:view>
</p>
</logic:notEmpty>

<fr:form id="addVigilantsForm" action="/vigilancy/convokeManagement.do?method=confirmConvokes#vigilantTable">

<p class="mtop15">
<html:submit><bean:message key="label.next" bundle="VIGILANCY_RESOURCES"/></html:submit>
<html:cancel><bean:message key="label.cancel" bundle="VIGILANCY_RESOURCES"/></html:cancel>
</p>

<p class="mbottom05"><strong><bean:message key="label.vigilancy.vigilantsThatTeachCourse" bundle="VIGILANCY_RESOURCES"/></strong>:</p>
<fr:edit id="selectVigilantsThatAreTeachers" name="bean" schema="selectVigilantsThatAreTeachers">
<fr:layout>
	<fr:property name="displayLabel" value="false"/>
	<fr:property name="classes" value="mtop0" />
</fr:layout>
</fr:edit>

<a name="vigilantTable"></a>
<logic:notEmpty name="incompatibleVigilants">
<strong><bean:message key="label.vigilancy.incompatibilityDetected" bundle="VIGILANCY_RESOURCES"/></strong>:
<fr:view name="incompatibleVigilants">
	<fr:layout>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="eachSchema" value="presentVigilantName"/>
		<fr:property name="classes" value="error1 list2"/>
	</fr:layout>
</fr:view> 
</logic:notEmpty>
<p class="mbottom05"><strong><bean:message key="label.vigilancy.vigilantsThatDoNotTeachCourse" bundle="VIGILANCY_RESOURCES"/></strong>:</p>
<fr:edit id="selectVigilants" name="bean" schema="selectVigilants">
<fr:layout>
	<fr:property name="displayLabel" value="false"/>
	<fr:property name="classes" value="mtop0" />
</fr:layout>
	<fr:destination name="postback" path="/vigilancy/convokeManagement.do?method=checkForIncompatibilities"/>
</fr:edit>


<p class="mbottom05"><strong><bean:message key="label.vigilancy.unavailableVigilants" bundle="VIGILANCY_RESOURCES"/></strong>:</p>
<fr:edit id="selectVigilantsThatAreUnavailable" name="bean" schema="selectVigilantsThatAreUnavailable">
<fr:layout>
	<fr:property name="displayLabel" value="false"/>
	<fr:property name="classes" value="mtop0" />
</fr:layout>
<fr:destination name="cancel" path="/vigilancy/convokeManagement.do?method=prepareConvoke"/>
</fr:edit>

<p>
	<span class="switchInline"><a href="javascript:checkall('addVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('addVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a></span>
</p>

<logic:notEmpty name="bean" property="unavailableInformation">
<p class="mbottom05"><strong><bean:message key="label.vigilancy.whyUnavailable" bundle="VIGILANCY_RESOURCES"/>:</strong></p>

<ul>
<logic:iterate id="information" name="bean" property="unavailableInformation" type="net.sourceforge.fenixedu.domain.vigilancy.strategies.UnavailableInformation"> 
	<li>
	<fr:view name="information" property="vigilant.person.name"/>: <fr:view name="information" property="reason"/>
	<logic:equal name="information" property="reason" value="UNAVAILABLE_PERIOD">
		<fr:view name="information" property="vigilant.unavailablePeriods">
		<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="unavailableShow"/>
			<fr:property name="classes" value="list3"/>
		</fr:layout>
		 </fr:view>
	</logic:equal>
	</li>
</logic:iterate>
</ul>
</logic:notEmpty>
</div>




<p class="mtop15">
<html:submit><bean:message key="label.next" bundle="VIGILANCY_RESOURCES"/></html:submit>
<html:cancel><bean:message key="label.cancel" bundle="VIGILANCY_RESOURCES"/></html:cancel>
</p>
</fr:form>

</logic:present>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>