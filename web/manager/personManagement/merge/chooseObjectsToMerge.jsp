<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.personManagement.merge" /></h2>

<p class="breadcumbs">
	<span class="actual"><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 1</strong>: <bean:message key="label.personManagement.merge.choose.persons" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 2</strong>: <bean:message key="label.personManagement.merge.transfer.personal.data" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 3</strong>: <bean:message key="label.personManagement.merge.transfer.student.data" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 4</strong>: <bean:message key="label.personManagement.merge.transfer.registrations" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 5</strong>: <bean:message key="label.personManagement.merge.delete.student" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 6</strong>: <bean:message key="label.personManagement.merge.delete.person" bundle="MANAGER_RESOURCES" /> </span>
</p>


<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>



<fr:form action="/mergePersons.do?method=chooseObjects">

	<fr:edit id="mergePersonsBean" name="mergePersonsBean" visible="false"/>
	
	<fr:edit id="chooseObjects" 
			name="mergePersonsBean"
			schema="personManagement.merge.chooseObjects"/>
		
	<html:submit><bean:message key="label.continue" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>

