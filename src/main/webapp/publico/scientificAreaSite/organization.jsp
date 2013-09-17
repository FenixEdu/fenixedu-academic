<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>


<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>
<h2><bean:message key="label.organization" bundle="SITE_RESOURCES"/> </h2>

<div>
	<fr:view name="site" property="unit" layout="organigram-with-card">
		<fr:layout>
			<fr:property name="classes" value="ostructure1"/>
			<fr:property name="rootUnitClasses" value="osrootunit"/>
			<fr:property name="unitClasses" value="osunit"/>
			<fr:property name="levelClasses" value="oslevel"/>
			<fr:property name="employeesSectionClasses" value="osemployees"/>
			<fr:property name="employeesClasses" value="osperson osworker"/>
			<fr:property name="functionsClasses" value="osfunction"/>
			<fr:property name="implicitFunctionsClasses" value="osfunction osimplicit"/>
			<fr:property name="membersClasses" value="osperson osmember nobullet"/>
			<fr:property name="showOnlyPeopleWithFunctions" value="true"/>
			<fr:property name="showEmptyFunctions" value="false"/>
			
			<fr:property name="showDates" value="false"/>
			<fr:property name="unitShown" value="false"/>
			<fr:property name="cardProperty(subLayout)" value="values-as-list"/>
			<fr:property name="cardProperty(subSchema)" value="view.person"/>
		</fr:layout>	
	</fr:view>
</div>