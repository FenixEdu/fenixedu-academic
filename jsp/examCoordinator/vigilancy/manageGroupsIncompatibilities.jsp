<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.displayIncompatibleInformation"/></h2>


<fr:form action="/vigilancy/vigilantGroupManagement.do?method=manageIncompatiblesOfVigilants">
<fr:edit id="selectVigilantGroup" name="bean" schema="vigilantGroup.selectToEdit">
</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<logic:present name="bean" property="selectedVigilantGroup">
<bean:define id="vigilantGroup" name="bean" property="selectedVigilantGroup" type="net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup"/>

<logic:empty name="vigilants"> 
<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noIncompatibilitiesToManage"/> 
</logic:empty>

<logic:notEmpty name="vigilants">
<ul class="mtop15">
	<logic:iterate id="vigilantIterator" name="vigilants">
	<bean:define id="vigilant" name="vigilantIterator" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilant"/>
		<li>
			<fr:view name="vigilant" property="person.name"/> 
			<span class="greytxt2"><bean:message key="label.vigilancy.incompatibleWith" bundle="VIGILANCY_RESOURCES"/> </span>
			<fr:view name="vigilant" property="incompatiblePerson.name"/> 
			<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=deleteIncompatibility&oid=" + vigilant.getIdInternal() + "&gid=" + vigilantGroup.getIdInternal() %>">
				<bean:message key="label.vigilancy.delete" bundle="VIGILANCY_RESOURCES"/>
			</a>
		</li>
	</logic:iterate>
</ul>
</logic:notEmpty>

<p>
	<html:link page="<%= "/vigilancy/vigilantGroupManagement.do?method=prepareAddIncompatiblePersonToVigilant&gid=" + vigilantGroup.getIdInternal() %>">
	<bean:message key="label.vigilancy.addIncompatibilityToVigilant" bundle="VIGILANCY_RESOURCES"/>
	</html:link>
</p>
</logic:present>


<script type="text/javascript" language="javascript">
switchGlobal();
</script>

