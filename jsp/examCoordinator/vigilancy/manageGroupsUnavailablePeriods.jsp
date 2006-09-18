<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinatior"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.person.vigilancy.displayUnavailableInformation"/></h2>

<ul>
	<li>
	<html:link page="/vigilancy/unavailablePeriodManagement.do?method=prepareAddPeriodToVigilant">
		<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.addAnUnavailablePeriodOfVigilant"/>
	</html:link>
	</li>
</ul>

<logic:empty name="unavailablePeriods"> 
	<p>
		<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noUnavailablePeriodsToManage"/>.</em>
	</p>
</logic:empty>

<logic:notEmpty name="unavailablePeriods">
	<fr:view 
		name="unavailablePeriods"
		schema="unavailableShowForCoordinator"
    >
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
		<fr:property name="columnClasses" value=",width100px acenter,width100px acenter,,," />
		<fr:property name="link(edit)" value="/vigilancy/unavailablePeriodManagement.do?method=editUnavailablePeriodOfVigilant" />
		<fr:property name="param(edit)" value="idInternal/oid" />
		<fr:property name="link(delete)" value="/vigilancy/unavailablePeriodManagement.do?method=deleteUnavailablePeriodOfVigilant" />
		<fr:property name="param(delete)" value="idInternal/oid" />
	</fr:layout>
	</fr:view>
   
</logic:notEmpty>
