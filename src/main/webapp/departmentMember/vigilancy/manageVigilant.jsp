<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />
<em><bean:message bundle="VIGILANCY_RESOURCES"
	key="label.vigilancy.department" /></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES"
	key="label.navheader.person.vigilant" /></h2>

<logic:present name="bean" property="executionYear">
	<ul class="mtop05">
		<logic:present name="vigilant">
			<logic:equal name="bean" property="executionYear.current"
				value="true">
				<logic:equal name="vigilant"
					property="person.allowedToSpecifyUnavailablePeriod" value="true">
					<bean:define id="externalId" name="vigilant" property="externalId" />
					<logic:present name="bean" property="selectedVigilantGroup.externalId">
						<bean:define id="groupId" name="bean" property="selectedVigilantGroup.externalId"/>
							
						<li><html:link
							page="<%= "/vigilancy/unavailablePeriodManagement.do?method=addUnavailablePeriod&vid=" + externalId + "&gid=" + groupId.toString()%>">
							<bean:message bundle="VIGILANCY_RESOURCES"
								key="label.vigilancy.addUnavailablePeriod" />
						</html:link>
					</logic:present>
					</li>
				</logic:equal>
			</logic:equal>
		</logic:present>
	</ul>
</logic:present>



<fr:form action="/vigilancy/vigilantManagement.do?method=displayMap">
	<fr:edit id="selectGroup" name="bean" schema="selectGroupsToDisplayMap"
		nested="true">
		<fr:destination name="postback"
			path="/vigilancy/vigilantManagement.do?method=displayMap" />
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright mbottom05" />
			<fr:property name="columnClasses" value=",,tdclear" />
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		styleClass="switchNone">
		<bean:message key="label.submit" bundle="VIGILANCY_RESOURCES" />
	</html:submit>
</fr:form>



<logic:empty name="bean" property="vigilantGroups">
	<logic:notEmpty name="bean" property="executionYear">
		<p><em><bean:message bundle="VIGILANCY_RESOURCES"
			key="label.vigilancy.noVigilantGroupsToDisplay" /></em></p>
	</logic:notEmpty>
</logic:empty>

<logic:present name="bean" property="selectedVigilantGroup">
	<div class="infoop5 mvert1" style="width: 550px;">
	<p class="mvert025"><bean:message
		key="label.vigilancy.contactEmail" bundle="VIGILANCY_RESOURCES" />: <logic:present
		name="bean" property="selectedVigilantGroup.contactEmail">
		<a
			href="mailto:<bean:write name="bean" property="selectedVigilantGroup.contactEmail"/>"><fr:view
			name="bean" property="selectedVigilantGroup.contactEmail" /></a>
	</logic:present> <logic:notPresent name="bean"
		property="selectedVigilantGroup.contactEmail">
		<em><bean:message key="label.vigilancy.noContactEmailDefined" /></em>
	</logic:notPresent></p>
	<p class="mvert025"><bean:message key="label.vigilancy.rulesLink"
		bundle="VIGILANCY_RESOURCES" />: <logic:present name="bean"
		property="selectedVigilantGroup.rulesLink">
		<a
			href="<bean:write name="bean" property="selectedVigilantGroup.rulesLink"/>"><fr:view
			name="bean" property="selectedVigilantGroup.rulesLink" /></a>
	</logic:present> <logic:notPresent name="bean"
		property="selectedVigilantGroup.rulesLink">
		<em><bean:message key="label.vigilancy.noRulesLinkDefined" /></em>
	</logic:notPresent></p>
	</div>
</logic:present>

<logic:present name="bean">
	<p class="mbottom05"><strong><bean:message
		key="vigilancy.yourGroups" bundle="VIGILANCY_RESOURCES" /></strong>: <fr:view
		name="bean" property="userViewVigilantGroups">
		<fr:layout name="flowLayout">
			<fr:property name="eachLayout" value="values" />
			<fr:property name="eachSchema" value="presentVigilantGroupName" />
			<fr:property name="htmlSeparator" value="," />

		</fr:layout>
	</fr:view></p>
	<p class="mbottom05"><strong><bean:message
		key="vigilancy.myConvokes" bundle="VIGILANCY_RESOURCES" /></strong>:</p>
	<logic:empty name="vigilant" property="activeVigilancies">
		<p class="mtop05"><em><bean:message
			key="label.vigilancy.noConvokesToDisplay"
			bundle="VIGILANCY_RESOURCES" /></em></p>
	</logic:empty>

	<logic:present name="bean" property="activeOtherCourseVigilancies">
		<logic:equal name="bean" property="executionYear.current" value="true">
			<fr:view name="bean" property="activeOtherCourseVigilancies"
				schema="present.convokes" layout="tabular">
				<fr:layout>
					<fr:property name="classes" value="tstyle1 mtop05" />
					<fr:property name="key(confirmar)" value="label.confirm" />
					<fr:property name="bundle(confirmar)" value="VIGILANCY_RESOURCES" />
					<fr:property name="link(confirmar)"
						value="/vigilancy/vigilantManagement.do?method=vigilantAcceptsConvoke" />
					<fr:property name="param(confirmar)" value="externalId/oid" />
					<fr:property name="visibleIfNot(confirmar)" value="confirmed" />
				</fr:layout>
			</fr:view>
		</logic:equal>
		<logic:equal name="bean" property="executionYear.current"
			value="false">
			<fr:view name="vigilant" property="activeOtherCourseVigilancies"
				schema="present.convokes" layout="tabular">
				<fr:layout>
					<fr:property name="classes" value="tstyle1 mtop05" />
				</fr:layout>
			</fr:view>
		</logic:equal>
	</logic:present>

	<logic:equal name="bean" property="showUnavailables" value="true">
		<p class="mbottom05"><strong><bean:message
			key="vigilancy.myUnavailablePeriods" bundle="VIGILANCY_RESOURCES" /></strong>:</p>
		<logic:messagesPresent message="true">
			<p>
			<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
				<span class="error0"><bean:write name="messages"/></span>
			</html:messages>
			</p>
		</logic:messagesPresent>
		<logic:notEmpty name="vigilant" property="unavailablePeriods">
			<logic:equal name="vigilant"
				property="person.allowedToSpecifyUnavailablePeriod" value="true">
				<fr:view name="vigilant" property="unavailablePeriods"
					schema="unavailableShow">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 mtop05" />
						<fr:property name="key(edit)" value="label.edit" />
						<fr:property name="bundle(edit)" value="VIGILANCY_RESOURCES" />
						<fr:property name="link(edit)"
							value="/vigilancy/unavailablePeriodManagement.do?method=editUnavailablePeriod" />
						<fr:property name="param(edit)" value="externalId/oid" />
						<fr:property name="key(delete)" value="label.delete" />
						<fr:property name="bundle(delete)" value="VIGILANCY_RESOURCES" />
						<fr:property name="link(delete)"
							value="/vigilancy/unavailablePeriodManagement.do?method=deleteUnavailablePeriod" />
						<fr:property name="param(delete)" value="externalId/oid" />
					</fr:layout>
				</fr:view>
			</logic:equal>

			<logic:equal name="vigilant"
				property="person.allowedToSpecifyUnavailablePeriod" value="false">

				<fr:view name="vigilant" property="unavailablePeriods"
					schema="unavailableShow">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 mtop05" />
					</fr:layout>
				</fr:view>
			</logic:equal>
		</logic:notEmpty>

		<logic:empty name="vigilant" property="unavailablePeriods">
			<p class="mtop05"><em><bean:message
				key="label.vigilancy.youHaveNoUnavailablePeriods"
				bundle="VIGILANCY_RESOURCES" /></em></p>
		</logic:empty>
	</logic:equal>

	<logic:equal name="bean" property="showIncompatibilities" value="true">
		<p class="mbottom05"><strong><bean:message
			key="vigilancy.myIncompatibility" bundle="VIGILANCY_RESOURCES" /></strong>:</p>
		<logic:notEmpty name="vigilant" property="person.incompatibleVigilant">
			<fr:view name="vigilant" property="incompatiblePersonName">
				<fr:layout>
					<fr:property name="classes" value="mtop05" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		<logic:empty name="vigilant" property="person.incompatibleVigilant">
			<p class="mtop05"><em><bean:message
				bundle="VIGILANCY_RESOURCES"
				key="label.vigilancy.youHaveNoIncompatibilities" /></em></p>
		</logic:empty>
	</logic:equal>
</logic:present>


<logic:present name="bean" property="selectedVigilantGroup">

	<logic:notEmpty name="bean" property="selectedVigilantGroup.vigilants">

		<h3 class="mtop15 mbottom05"><fr:view name="bean"
			property="selectedVigilantGroup.name" /></h3>
		<bean:define id="bean" name="bean"
			type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantBean" />

		<div class="mbottom2">
			<p class="mtop2 mbottom0"><em><bean:message key="label.vigilancy.label" bundle="VIGILANCY_RESOURCES"/>:</em></p>
			<ul class="list2 mtop025 liinline mbottom0">
				<li><em><strong><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.category" bundle="VIGILANCY_RESOURCES"/></span></em></li>
				<li><em><strong><bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.totalPoints" bundle="VIGILANCY_RESOURCES"/></span></em></li>
				<li><em><strong><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></span></em></li>
				<li><em><strong><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></span></em></li>
				<li><em><strong><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></span></em></li>
				<li><em><strong><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></span></em></li>
				<li><em><strong><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.startPoints" bundle="VIGILANCY_RESOURCES"/></span></em></li>
			</ul>
			
		<table>
			<tr class="color888">
				<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #00427b; background: #9ebcd7; float:left; margin-right: 0.5em;"></div> <bean:message key="label.vigilancy.convokes" bundle="VIGILANCY_RESOURCES"/></td>
				<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #007b4d; background: #9ed7c2; float:left; margin-right: 0.5em;"></div> <bean:message key="label.teachersVigilants" bundle="VIGILANCY_RESOURCES"/></td>
				<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #888; background: #ccc; float:left; margin-right: 0.5em;"></div> <bean:message key="label.cancelledConvokes" bundle="VIGILANCY_RESOURCES"/></td>
				<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #d2d292; background: #ffffe5; float:left; margin-right: 0.5em;"></div> <bean:message key="label.problematicConvoke" bundle="VIGILANCY_RESOURCES"/></td>
			</tr>
		</table>
		
		</div>

		<fr:view name="bean" property="selectedVigilantGroup"
			layout="vigilant-table">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 mtop05 acenter" />
				<fr:property name="rowClasses" value="vigilancyHeaderColumns" />
				<fr:property name="emptyMessageKey"
					value="label.vigilancy.noConvokes" />
				<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES" />

				<fr:property name="showIncompatibilities"
					value="<%= String.valueOf(bean.isShowIncompatibilities()) %>" />
				<fr:property name="showUnavailables"
					value="<%= String.valueOf(bean.isShowUnavailables()) %>" />
				<fr:property name="showBoundsJustification"
					value="<%= String.valueOf(bean.isShowBoundsJustification()) %>" />
				<fr:property name="showStartPoints"
					value="<%= String.valueOf(bean.isShowStartPoints())%>" />
				<fr:property name="showNotActiveConvokes"
					value="<%= String.valueOf(bean.isShowNotActiveConvokes()) %>" />
				<fr:property name="showPointsWeight"
					value="<%= String.valueOf(bean.isShowPointsWeight()) %>" />
				<fr:property name="showOwnVigilancies" 
					value="<%= String.valueOf(bean.isShowOwnVigilancies()) %>"/>
				<fr:property name="columnClasses"
					value=",,width250px aleft,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," />

				<logic:equal name="bean" property="showAllVigilancyInfo"
					value="true">
					<fr:property name="convokeSchema"
						value="presentConvokesAllInfoForVigilant" />
				</logic:equal>
				<logic:equal name="bean" property="showAllVigilancyInfo"
					value="false">
					<fr:property name="convokeSchema"
						value="presentSimpleConvokesForVigilant" />
				</logic:equal>

			</fr:layout>
		</fr:view>

	</logic:notEmpty>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
