<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinatior"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.newVigilantGroup.title"/></h2>

<fr:form action="/vigilancy/vigilantGroupManagement.do?method=createVigilantGroup">
<fr:edit 
		   id="createVigilantGroup.block1"
		   type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
		   name="bean"
           schema="vigilantGroup.block1" >
	<fr:layout>
		<fr:property name="classes" value="thlight mtop1"/>		
	</fr:layout>
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"/>
</fr:edit>

<p class="mtop15 mbottom025"><strong><bean:message key="label.vigilancy.firstUnavailablePeriod" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:edit 
		   id="createVigilantGroup.block2"
		   type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
		   name="bean"
           schema="vigilantGroup.block2" 
>
	<fr:layout>
		<fr:property name="classes" value="thlight"/>
	</fr:layout>
</fr:edit>

<p class="mtop15"><strong><bean:message key="label.vigilancy.secondUnavailablePeriod" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:edit 
		   id="createVigilantGroup.block3"
		   type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
		   name="bean"
           schema="vigilantGroup.block3" 
>
	<fr:layout>
		<fr:property name="classes" value="thlight"/>
	</fr:layout>
</fr:edit>

<p class="mtop15"><html:submit><bean:message key="label.create" bundle="VIGILANCY_RESOURCES"/></html:submit>
<html:cancel>
	<bean:message key="label.cancel" bundle="VIGILANCY_RESOURCES"/>
</html:cancel>
</p>

</fr:form>
           
