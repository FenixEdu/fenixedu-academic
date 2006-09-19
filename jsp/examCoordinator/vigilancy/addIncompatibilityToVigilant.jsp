<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinatior"/></em>
<h2><bean:message key="label.vigilancy.addIncompatibilityToVigilant.title" bundle="VIGILANCY_RESOURCES"/></h2>

<ul>
	<li>
	<html:link page="/vigilancy/vigilantGroupManagement.do?method=prepareManageIncompatiblesOfVigilants"><bean:message key="label.vigilancy.back" bundle="VIGILANCY_RESOURCES"/></html:link>
	</li>
</ul>

<fr:form action="/vigilancy/vigilantGroupManagement.do?method=vigilantSelectedInIncompatibilityScreen">
<fr:edit id="selectVigilant" name="bean" schema="selectVigilantsOfVigilantGroup" 
action="/vigilancy/vigilantGroupManagement.do?method=vigilantSelectedInIncompatibilityScreen"/>
	<html:submit styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>


<logic:present name="bean" property="selectedVigilant">

	<logic:present name="bean" property="selectedVigilant.incompatiblePerson">
		<ul>
			<li>
			<fr:view name="bean" property="selectedVigilant" schema="presentVigilantWithIncompatiblePerson" layout="values"/>
			</li>
		</ul>
	</logic:present>	
	<logic:notPresent name="bean" property="selectedVigilant.incompatiblePerson">
		<p><em><bean:message key="label.vigilancy.noIncompatiblePerson" bundle="VIGILANCY_RESOURCES"/></em></p>
	</logic:notPresent>  

	<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean"/>
	<fr:view 
		name="bean"
		property="vigilants"
		schema="presentVigilantName"
    >
    <fr:layout name="tabular">
 		<fr:property name="link(Adicionar)" value="<%= "/vigilancy/vigilantGroupManagement.do?method=addIncompatibilityToVigilant&oid=" + bean.getSelectedVigilant().getIdInternal() + "&gid=" + bean.getSelectedVigilantGroup().getIdInternal() %>"/>
		<fr:property name="param(Adicionar)" value="person.idInternal/pid" />
		<fr:property name="displayHeaders" value="false"/>
	</fr:layout>
	</fr:view>
	
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>