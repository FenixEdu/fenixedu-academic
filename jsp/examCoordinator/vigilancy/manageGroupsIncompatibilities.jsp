<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinatior"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.person.vigilancy.displayIncompatibleInformation"/></h2>

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
			<a href="<%= "/vigilancy/vigilantGroupManagement.do?method=deleteIncompatibility&oid=" + vigilant.getIdInternal() %>">
				<bean:message key="label.vigilancy.delete" bundle="VIGILANCY_RESOURCES"/>
			</a>
		</li>
	</logic:iterate>
</ul>
</logic:notEmpty>

<p>
	<html:link page="/vigilancy/vigilantGroupManagement.do?method=prepareAddIncompatiblePersonToVigilant">
	<bean:message key="label.vigilancy.addIncompatibilityToVigilant" bundle="VIGILANCY_RESOURCES"/>
	</html:link>
</p>