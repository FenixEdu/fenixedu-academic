<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="link.manageRequestsPeriods" /></h2>

<p class="mtop15 mbottom05">	
	<span class="error0">
		<html:messages id="message" message="true" bundle="PARKING_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>
	
<logic:present name="parkingRequestPeriodToEdit">
	<p class="mbottom025"><strong><bean:message key="label.editPeriod"/></strong></p>
	<fr:edit id="parkingRequestPeriodToEdit" name="parkingRequestPeriodToEdit" schema="show.parkingRequestPeriod" action="/manageParkingPeriods.do?method=prepareManageRequestsPeriods" type="net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop025" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
	</fr:edit>
</logic:present>
<logic:notPresent name="parkingRequestPeriodToEdit">
	<p class="mbottom025"><strong><bean:message key="label.insertNewPeriod"/></strong></p>
	<fr:form action="/manageParkingPeriods.do?method=prepareManageRequestsPeriods">
		<fr:create id="aa" schema="show.parkingRequestPeriod" type="net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mvert025" />
				<fr:property name="columnClasses" value=",,tderror1 tdclear" />
			</fr:layout>
		</fr:create>
		<p class="mtop05">
			<html:submit>
				<bean:message key="button.submit" />
			</html:submit>
		</p>
	</fr:form>
</logic:notPresent>




<logic:present name="parkingRequestPeriods">
	<bean:size id="size" name="parkingRequestPeriods"/>
	<logic:equal name="size" value="0">
		<p class="mtop15">
			<em><bean:message key="label.periodNotSpecified"/>.</em>
		</p>
	</logic:equal>
	<logic:notEqual name="size" value="0">
		<fr:view name="parkingRequestPeriods" schema="show.parkingRequestPeriod">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="columnClasses" value="bgcolor3 acenter,acenter,aleft" />
				<fr:property name="headerClasses" value="acenter" />
				
				<fr:property name="link(edit)" value="/manageParkingPeriods.do?method=editRequestPeriod" />
				<fr:property name="key(edit)" value="label.edit" />
				<fr:property name="param(edit)" value="idInternal" />
				<fr:property name="bundle(edit)" value="PARKING_RESOURCES" />
				<fr:property name="link(delete)" value="/manageParkingPeriods.do?method=deleteRequestPeriod" />
				<fr:property name="key(delete)" value="label.delete" />
				<fr:property name="param(delete)" value="idInternal" />
				<fr:property name="bundle(delete)" value="PARKING_RESOURCES" />
			</fr:layout>
		</fr:view>	
	</logic:notEqual>
</logic:present>
