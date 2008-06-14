<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em><br>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editExamCoordinator"/></h2><br>


<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean"/>

<fr:edit id="editCoordinators" name="bean" property="examCoordinators" schema="editCoordinatorPreviledges">
	<fr:destination name="cancel" path="<%= "/vigilancy/examCoordinatorManagement.do?method=prepareAddExamCoordinatorWithState&unitId=" + bean.getSelectedUnit().getIdInternal() + "&deparmentId=" + bean.getSelectedDepartment().getIdInternal() %>"/>
	<fr:destination name="success" path="<%= "/vigilancy/examCoordinatorManagement.do?method=prepareAddExamCoordinatorWithState&unitId=" + bean.getSelectedUnit().getIdInternal() + "&deparmentId=" + bean.getSelectedDepartment().getIdInternal() %>"/>
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5"/>
		<fr:property name="rowClasses" value="trhighlight1, "/>
	</fr:layout>
</fr:edit>