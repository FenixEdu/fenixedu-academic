<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em><br>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editExamCoordinator"/></h2><br>


<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean"/>

<fr:edit id="editCoordinators" name="bean" property="examCoordinators" schema="editCoordinatorPreviledges">
	<fr:destination name="cancel" path="<%= "/vigilancy/examCoordinatorManagement.do?method=prepareAddExamCoordinatorWithState&unitId=" + bean.getSelectedUnit().getExternalId() + "&deparmentId=" + bean.getSelectedDepartment().getExternalId() %>"/>
	<fr:destination name="success" path="<%= "/vigilancy/examCoordinatorManagement.do?method=prepareAddExamCoordinatorWithState&unitId=" + bean.getSelectedUnit().getExternalId() + "&deparmentId=" + bean.getSelectedDepartment().getExternalId() %>"/>
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5"/>
		<fr:property name="rowClasses" value="trhighlight1, "/>
	</fr:layout>
</fr:edit>