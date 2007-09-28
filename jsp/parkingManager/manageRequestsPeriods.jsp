<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="link.manageRequestsPeriods" /></h2>

<p>	
	<span class="error0">
		<html:messages id="message" message="true" bundle="PARKING_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
	</p>
	
<logic:present name="parkingRequestPeriodToEdit">
	<fr:edit id="parkingRequestPeriodToEdit" name="parkingRequestPeriodToEdit" schema="show.parkingRequestPeriod" action="/parking.do?method=prepareManageRequestsPeriods" type="net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod"/>
</logic:present>
<logic:notPresent name="parkingRequestPeriodToEdit">
	<fr:create id="aa" schema="show.parkingRequestPeriod" action="/parking.do?method=prepareManageRequestsPeriods" type="net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod"/>
</logic:notPresent>



<logic:present name="parkingRequestPeriods">
	<fr:view name="parkingRequestPeriods" schema="show.parkingRequestPeriod">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder" />
			<fr:property name="columnClasses" value="bgcolor3 acenter,acenter,aleft" />
			<fr:property name="headerClasses" value="acenter" />
			
			<fr:property name="link(edit)" value="/parking.do?method=editRequestPeriod" />
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="param(edit)" value="idInternal" />
			<fr:property name="bundle(edit)" value="PARKING_RESOURCES" />
			<fr:property name="link(delete)" value="/parking.do?method=deleteRequestPeriod" />
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="param(delete)" value="idInternal" />
			<fr:property name="bundle(delete)" value="PARKING_RESOURCES" />
		</fr:layout>
	</fr:view>	
</logic:present>
