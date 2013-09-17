<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message key="label.vigilancy.addIncompatibilityToVigilant.title" bundle="VIGILANCY_RESOURCES"/></h2>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean"/>

<ul>
	<li>
	<html:link page="<%= "/vigilancy/vigilantGroupManagement.do?method=prepareManageIncompatiblesOfVigilants&gid=" + bean.getSelectedVigilantGroup().getExternalId() %>"><bean:message key="label.vigilancy.back" bundle="VIGILANCY_RESOURCES"/></html:link>
	</li>
</ul>

<fr:form action="/vigilancy/vigilantGroupManagement.do?method=vigilantSelectedInIncompatibilityScreen">
<fr:edit id="selectVigilantWrapper" name="bean" schema="selectVigilantsOfVigilantGroup" 
action="/vigilancy/vigilantGroupManagement.do?method=vigilantSelectedInIncompatibilityScreen">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thmiddle thlight mbottom05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>


<logic:present name="bean" property="selectedVigilantWrapper">

	<logic:notEmpty name="bean" property="selectedVigilantWrapper.person.incompatibleVigilant">
		<ul class="mvert15">
			<li>
				<fr:view name="bean" property="selectedVigilantWrapper" schema="presentVigilantWithIncompatiblePerson" layout="values"/>
			</li>
		</ul>
	</logic:notEmpty>	
	<logic:empty name="bean" property="selectedVigilantWrapper.person.incompatibleVigilant">
		<p class="mtop05"><em><bean:message key="label.vigilancy.noIncompatiblePerson" bundle="VIGILANCY_RESOURCES"/></em></p>
	</logic:empty>  

	<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean"/>
	<fr:view 
		name="bean"
		property="vigilants"
		schema="presentVigilantWrapperName"
    >
    <fr:layout name="tabular">
		<fr:property name="key(Adicionar)" value="label.add"/>
		<fr:property name="bundle(Adicionar)" value="VIGILANCY_RESOURCES"/>
 		<fr:property name="link(Adicionar)" value="<%= "/vigilancy/vigilantGroupManagement.do?method=addIncompatibilityToVigilant&oid=" + bean.getSelectedVigilantWrapper().getExternalId() + "&gid=" + bean.getSelectedVigilantGroup().getExternalId() %>"/>
		<fr:property name="param(Adicionar)" value="person.externalId/pid" />
		<fr:property name="displayHeaders" value="false"/>
		<fr:property name="sortBy" value="person.name"/>
	</fr:layout>
	</fr:view>
	
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>