<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h1 class="mbottom03 cnone">
	<fr:view name="researchUnit" property="nameWithAcronym"/>
</h1>

<h2 class="mtop15"><bean:message key="label.members" bundle="SITE_RESOURCES"/></h2>
	<bean:define id="researchUnit" name="researchUnit" toScope="request"/>
	<jsp:include flush="true" page="viewMembersFromUnit.jsp"></jsp:include>


<logic:notEmpty name="researchUnit" property="allCurrentActiveSubUnits"> 
<bean:define id="unitName" name="researchUnit" property="name" type="java.lang.String"/>
	<logic:iterate id="unit" name="researchUnit" property="allCurrentActiveSubUnits"> 


		<fr:view name="unit">
			<fr:layout name="unit-link">
				<fr:property name="style" value="font-weight: bold; font-size: 12px; color: #333;"/>
				<fr:property name="unitLayout" value="values"/>
				<fr:property name="unitSchema" value="unit.name"/>
				<fr:property name="targetBlank" value="true"/>
				<fr:property name="parenteShown" value="false"/>
			</fr:layout>
		</fr:view>

		<logic:equal name="unit" property="siteAvailable" value="true">
			<bean:define id="researchUnit" name="unit" toScope="request"/>
			<jsp:include flush="true" page="viewMembersFromUnit.jsp"></jsp:include>
		</logic:equal>

		<logic:equal name="unit" property="siteAvailable" value="false">
			<p><em><bean:message key="label.noMembersDefined" bundle="SITE_RESOURCES"/></em></p>
		</logic:equal>	

	</logic:iterate>

</logic:notEmpty>


