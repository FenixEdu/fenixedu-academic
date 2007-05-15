<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h1 class="mbottom03 cnone">
	<fr:view name="researchUnit" property="nameWithAcronym"/>
</h1>

<h2 class="mtop15"><bean:message key="label.members" bundle="SITE_RESOURCES"/></h2>

<logic:notEmpty name="researchUnit" property="permanentResearchContracts">
<em><bean:message key="label.permanentResearchers" bundle="SITE_RESOURCES"/></em>
	<fr:view name="researchUnit" property="permanentResearchContracts">
		<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="person.in.contract"/>
			<fr:property name="sortBy" value="person.nickname"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="researchUnit" property="invitedResearchContracts">
<em><bean:message key="label.invitedResearchers" bundle="SITE_RESOURCES"/></em>
	<fr:view name="researchUnit" property="invitedResearchContracts">
	<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="person.in.contract"/>
			<fr:property name="sortBy" value="person.nickname"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="researchUnit" property="otherStaffContracts">
<em><bean:message key="label.otherStaff" bundle="SITE_RESOURCES"/></em>
	<fr:view name="researchUnit" property="otherStaffContracts">
	<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="person.in.contract"/>
			<fr:property name="sortBy" value="person.nickname"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="researchUnit" property="technicalStaffContracts">
<em><bean:message key="label.technicalStaff" bundle="SITE_RESOURCES"/></em>
	<fr:view name="researchUnit" property="technicalStaffContracts">
	<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="person.in.contract"/>
			<fr:property name="sortBy" value="person.nickname"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="researchUnit" property="collaboratorsContracts">
<em><bean:message key="label.colaborators" bundle="SITE_RESOURCES"/></em>
	<fr:view name="researchUnit" property="collaboratorsContracts">
	<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="person.in.contract"/>
			<fr:property name="sortBy" value="person.nickname"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="researchUnit" property="msCStudentsContracts">
<em><bean:message key="label.mscStudents" bundle="SITE_RESOURCES"/></em>
	<fr:view name="researchUnit" property="msCStudentsContracts">
	<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="person.in.contract"/>
			<fr:property name="sortBy" value="person.nickname"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="researchUnit" property="phDStudentsContracts">
<em><bean:message key="label.phdStudents" bundle="SITE_RESOURCES"/></em>
	<fr:view name="researchUnit" property="phDStudentsContracts">
	<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="person.in.contract"/>
			<fr:property name="sortBy" value="person.nickname"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


