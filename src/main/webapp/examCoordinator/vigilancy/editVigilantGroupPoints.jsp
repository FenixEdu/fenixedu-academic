<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilancyPoints"/></h2>

<bean:define id="groupID" name="group" property="externalId"/>
<ul>
	<li>
		<html:link page="/vigilancy/vigilantGroupManagement.do?method=selectPreviousPointsSchema" paramId="oid" paramName="groupID">
			<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.selectPreviousPointsSchema" />
		</html:link>
	</li>
</ul>

<fr:edit id="group" name="group" schema="edit.vigilantGroup.points">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="success" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"/>
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"/>
</fr:edit>