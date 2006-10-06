<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.displayGroupHistory"/></h2>


<fr:form action="/vigilancy/vigilantGroupManagement.do?method=displayGroupHistory">
	<fr:edit id="selectGroupAndYear" name="bean" schema="selectGroupsToDisplayMap" nested="true">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear"/>
	</fr:layout>
	</fr:edit>
		<html:submit styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>


<logic:empty name="bean" property="vigilantGroups">
 <logic:notEmpty name="bean" property="executionYear">
  <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noVigilantGroupsToDisplay"/>
 </logic:notEmpty>
</logic:empty>

<logic:notEmpty name="bean" property="selectedVigilantGroup">

	<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantBean"/>
	<h3 class="mbottom05"><fr:view name="bean" property="selectedVigilantGroup.name"/></h3>
	
	<fr:view name="bean" property="selectedVigilantGroup.vigilants" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="classes" value="tstyle1 mtop05" />
		<fr:property name="showIncompatibilities" value="<%= String.valueOf(bean.isShowIncompatibilities()) %>"/>
		<fr:property name="showUnavailables" value="<%= String.valueOf(bean.isShowUnavailables()) %>"/>
		<fr:property name="showBoundsJustification" value="<%= String.valueOf(bean.isShowBoundsJustification()) %>"/>
		<fr:property name="showStartPoints" value="<%= String.valueOf(bean.isShowStartPoints())%>"/>
		<fr:property name="showNotActiveConvokes" value="<%= String.valueOf(bean.isShowNotActiveConvokes()) %>"/>
		<fr:property name="showPointsWeight" value="<%= String.valueOf(bean.isShowPointsWeight()) %>"/>
		<fr:property name="columnClasses" value=",,width250px aleft,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="presentConvokesWithoutLink"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokes"/>	
		</logic:equal>
		<fr:property name="convokesToShow" value="all"/>
	</fr:layout>

	</fr:view>
	<ul class="list2">
<li>
<em><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.category" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.totalPoints" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.startPoints" bundle="VIGILANCY_RESOURCES"/></em>
</li>
</ul>
</logic:notEmpty>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>