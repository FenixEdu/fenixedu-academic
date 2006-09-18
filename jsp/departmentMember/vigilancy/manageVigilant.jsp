<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.department"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></h2>

<logic:present name="bean" property="executionYear">
<ul class="mtop05">
<logic:present name="vigilant">
<logic:equal name="bean" property="executionYear.current" value="true">
	<logic:equal name="vigilant" property="allowedToSpecifyUnavailablePeriod" value="true">
		<li><html:link  page="/vigilancy/unavailablePeriodManagement.do?method=addUnavailablePeriod"><bean:message bundle="VIGILANCY_RESOURCES" key="label.person.vigilancy.addUnavailablePeriod"/></html:link></li>
	</logic:equal>
	<li><html:link  page="/vigilancy/vigilantManagement.do?method=prepareEditIncompatiblePerson"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.addIncompatiblePerson"/></html:link></li>
</logic:equal>
</logic:present>
</ul>
</logic:present>



<fr:form action="/vigilancy/vigilantManagement.do?method=displayMap">
	<fr:edit id="selectGroup" 
		name="bean" schema="selectGroupsToDisplayMap"
		nested="true">
	<fr:destination name="postback" path="/vigilancy/vigilantManagement.do?method=displayMap"/>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright mbottom05"/>
	</fr:layout>
	</fr:edit>
</fr:form>



<logic:empty name="bean" property="vigilantGroups">
<logic:notEmpty name="bean" property="executionYear">
	<p><em><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noVigilantGroupsToDisplay"/></em></p>
</logic:notEmpty>
</logic:empty>


<logic:present name="vigilant">
	<p class="mbottom05"><strong><bean:message key="vigilancy.myConvokes" bundle="VIGILANCY_RESOURCES"/></strong>:</p>
	<logic:empty name="vigilant" property="convokes">
		<p class="mtop05"><em><bean:message key="label.person.vigilancy.noConvokesToDisplay" bundle="VIGILANCY_RESOURCES"/></em></p>
	</logic:empty>

	<logic:notEmpty name="vigilant" property="convokes">
	<logic:equal name="bean" property="executionYear.current" value="true">
	<fr:view name="vigilant" property="convokes" schema="present.convokes" layout="tabular">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 mtop05" />
		<fr:property name="link(confirmar)" value="/vigilancy/vigilantManagement.do?method=vigilantAcceptsConvoke" />
		<fr:property name="param(confirmar)" value="idInternal/oid" />
		<fr:property name="visibleIf(confirmar)" value="notConfirmed" />
	</fr:layout>
	</fr:view>
	</logic:equal>	
	<logic:equal name="bean" property="executionYear.current" value="false">
	<fr:view name="vigilant" property="convokes" schema="present.convokes" layout="tabular">
		<fr:layout>
		<fr:property name="classes" value="tstyle1 mtop05" />
		</fr:layout>
	</fr:view>
	</logic:equal>
	</logic:notEmpty>

<logic:equal name="bean" property="showUnavailables" value="true">
	<p class="mbottom05"><strong><bean:message key="vigilancy.myUnavailablePeriods" bundle="VIGILANCY_RESOURCES"/></strong>:</p>
<logic:notEmpty name="vigilant" property="unavailablePeriods">
	<logic:equal name="vigilant" property="allowedToSpecifyUnavailablePeriod" value="true">
	<fr:view name="vigilant" property="unavailablePeriods" schema="unavailableShow">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 mtop05" />
		<fr:property name="link(edit)" value="/vigilancy/unavailablePeriodManagement.do?method=editUnavailablePeriod" />
		<fr:property name="param(edit)" value="idInternal/oid" />
		<fr:property name="link(delete)" value="/vigilancy/unavailablePeriodManagement.do?method=deleteUnavailablePeriod" />
		<fr:property name="param(delete)" value="idInternal/oid" />
	</fr:layout>
	</fr:view>
	</logic:equal>    

	<logic:equal name="vigilant" property="allowedToSpecifyUnavailablePeriod" value="false">		

<fr:view name="vigilant" property="unavailablePeriods">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 mtop05" />
	</fr:layout>
	</fr:view>    
	</logic:equal>
</logic:notEmpty>

<logic:empty name="vigilant" property="unavailablePeriods">
		<p class="mtop05"><em><bean:message key="label.vigilancy.youHaveNoUnavailablePeriods" bundle="VIGILANCY_RESOURCES"/></em></p>
</logic:empty>
</logic:equal>

<logic:equal name="bean" property="showIncompatibilities" value="true">
	<p class="mbottom05"><strong><bean:message key="vigilancy.myIncompatibility" bundle="VIGILANCY_RESOURCES"/></strong>:</p>
	<logic:notEmpty name="vigilant" property="incompatiblePerson">
	<fr:view name="vigilant" property="incompatiblePerson.name">
		<fr:layout>
			<fr:property name="classes" value="mtop05"/>
		</fr:layout>
	</fr:view>
	</logic:notEmpty>
	<logic:empty name="vigilant" property="incompatiblePerson">
	<p class="mtop05"><em><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.youHaveNoIncompatibilities"/></em></p>
	</logic:empty>
	</logic:equal>
</logic:present>


<logic:present name="bean" property="selectedVigilantGroup">
<logic:notEmpty name="bean" property="selectedVigilantGroup.vigilants">
	

<h3 class="mtop15 mbottom05"><fr:view name="bean" property="selectedVigilantGroup.name"/></h3>

<fr:view name="bean" property="selectedVigilantGroup.vigilants" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="classes" value="tstyle1 mtop05 acenter" />
		<fr:property name="sortBy" value="person.username"/>
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<logic:equal name="bean" property="showIncompatibilities" value="true">
			<logic:equal name="bean" property="showUnavailables" value="true">
				<fr:property name="vigilantSchema" value="vigilantsWithAllInformation"/>	
				<fr:property name="columnClasses" value=",width250px aleft,,aleft,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
			</logic:equal>
		
			<logic:equal name="bean" property="showUnavailables" value="false">
				<fr:property name="vigilantSchema" value="vigilantsWithOutUnavailables"/>	
				<fr:property name="columnClasses" value=",width250px aleft,aleft,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
			</logic:equal>
	    </logic:equal>
		<logic:equal name="bean" property="showIncompatibilities" value="false">
					<logic:equal name="bean" property="showUnavailables" value="true">
				<fr:property name="vigilantSchema" value="vigilantsWithoutIncompatibilities"/>	
			</logic:equal>
		
			<logic:equal name="bean" property="showUnavailables" value="false">
						<fr:property name="columnClasses" value=",width250px aleft,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
				<fr:property name="vigilantSchema" value="simpleVigilants"/>	
			</logic:equal>
		</logic:equal>


		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="presentConvokesWithoutLink"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokes"/>	
		</logic:equal>


		<fr:property name="convokesToShow" value="all"/>
	</fr:layout>
	</fr:view>

</logic:notEmpty>
</logic:present>

