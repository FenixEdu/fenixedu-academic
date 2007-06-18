<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editVigilantGroup"/></h2>
<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean"/>


<p class="mtop1 mbottom025"><strong><fr:view name="bean" property="selectedVigilantGroup.name"/>:</strong></p>

<fr:form action="/vigilancy/vigilantGroupManagement.do?method=applyChangesToVigilantGroup">
<fr:edit 
		   id="editVigilantGroup.block1"
		   type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
		   name="bean"
           schema="vigilantGroup.block1" 
>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thright thlight mtop025"/>		
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"/>
</fr:edit>

<p class="mtop1 mbottom025"><strong><bean:message key="label.vigilancy.firstUnavailablePeriod" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:edit 
		   id="editVigilantGroup.block2"
		   type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
		   name="bean"
           schema="vigilantGroup.block2" 
>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop025"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<p class="mtop1"><strong><bean:message key="label.vigilancy.secondUnavailablePeriod" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:edit 
		   id="editVigilantGroup.block3"
		   type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
		   name="bean"
           schema="vigilantGroup.block3" 
>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop025"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<p class="mtop1">
	<html:submit><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="VIGILANCY_RESOURCES"/></html:cancel>
</p>

</fr:form>
