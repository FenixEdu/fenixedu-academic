<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinatior"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.person.vigilancy.displayGroupHistory"/></h2>


<fr:form action="/vigilancy/vigilantGroupManagement.do?method=displayGroupHistory">
	<fr:edit id="selectGroupAndYear" name="bean" schema="selectGroupsToDisplayMap" nested="true">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright"/>
	</fr:layout>
	</fr:edit>
</fr:form>


<logic:empty name="bean" property="vigilantGroups">
 <logic:notEmpty name="bean" property="executionYear">
  <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noVigilantGroupsToDisplay"/>
 </logic:notEmpty>
</logic:empty>

<logic:notEmpty name="bean" property="selectedVigilantGroup">

	<h3 class="mbottom05"><fr:view name="bean" property="selectedVigilantGroup.name"/></h3>
	
	<fr:view name="bean" property="selectedVigilantGroup.vigilants" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="sortBy" value="person.username"/>
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="classes" value="tstyle1a mtop05" />
			<logic:equal name="bean" property="showIncompatibilities" value="true">
			<logic:equal name="bean" property="showUnavailables" value="true">
				<fr:property name="vigilantSchema" value="vigilantsWithAllInformation"/>	
			</logic:equal>
		
			<logic:equal name="bean" property="showUnavailables" value="false">
				<fr:property name="vigilantSchema" value="vigilantsWithOutUnavailables"/>	
			</logic:equal>
	    </logic:equal>
		<logic:equal name="bean" property="showIncompatibilities" value="false">
					<logic:equal name="bean" property="showUnavailables" value="true">
				<fr:property name="vigilantSchema" value="vigilantsWithoutIncompatibilities"/>	
			</logic:equal>
		
			<logic:equal name="bean" property="showUnavailables" value="false">
				<fr:property name="vigilantSchema" value="simpleVigilants"/>	
			</logic:equal>
		</logic:equal>

		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="presentConvokesWithoutLink"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokes"/>	
		</logic:equal>
		
		<fr:property name="columnClasses" value=",width250px,,,,,,,,,,,,,,,,,,,,,,"/>
		<fr:property name="convokesToShow" value="all"/>
	</fr:layout>

	</fr:view>
</logic:notEmpty>

