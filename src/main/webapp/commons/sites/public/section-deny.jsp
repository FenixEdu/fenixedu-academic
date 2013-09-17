<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<logic:present name="section">
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
    
    
    <h2>
        <fr:view name="section" property="name" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
        <span class="permalink1">(<app:contentLink name="section"><bean:message key="label.link" bundle="SITE_RESOURCES"/></app:contentLink>)</span>
    </h2>
    
    
    <p>
       <em><bean:message key="message.section.view.notAllowed" bundle="SITE_RESOURCES"/></em>
    </p>
    
    <bean:message key="label.item.file.availableFor" bundle="SITE_RESOURCES"/>

	<logic:present name="section" property="availabilityPolicy">
		<logic:present name="section" property="availabilityPolicy.targetGroup">
			<logic:present name="section" property="availabilityPolicy.targetGroup.name">
				<fr:view name="section" property="availabilityPolicy.targetGroup.name">
				</fr:view>
			</logic:present>
		</logic:present>
	</logic:present>
    
</logic:present>
