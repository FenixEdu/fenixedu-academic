<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:xhtml />

<em><bean:message key="title.student.portalTitle" /></em>
<h2><bean:message key="label.careerWorkshopApplication.title" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<h3 class="mbottom025"><bean:message key="label.careerWorkshop.openApplicationEvents" bundle="STUDENT_RESOURCES"/></h3>

<logic:empty name="openApplicationEvents" >
    <p><em><bean:message key="label.careerWorkshop.noOpenApplicationEvents" bundle="STUDENT_RESOURCES"/></em></p>
</logic:empty>

<logic:notEmpty name="openApplicationEvents">
    <fr:view name="openApplicationEvents">
        <fr:layout name="tabular">
        	<fr:property name="linkFormat(apply)" value="<%="/careerWorkshopApplication.do?method=presentApplication&eventId=${externalId}"%>"/>
			<fr:property name="order(apply)" value="1" />
			<fr:property name="key(apply)" value="label.manageCareerWorkshop.presentApplication" />
			<fr:property name="bundle(apply)" value="STUDENT_RESOURCES" />
        
        	<fr:property name="sortBy" value="beginDate=desc"/>
            <fr:property name="classes" value="tstyle1 thleft thlight mvert05" />
			<fr:property name="columnClasses" value=",,,,tdclear tderror1" />	    
        </fr:layout>
        <fr:schema type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent" bundle="STUDENT_RESOURCES">
			<fr:slot name="formattedBeginDate" key="label.manageCareerWorkshop.startDate" />
			<fr:slot name="formattedEndDate" key="label.manageCareerWorkshop.endDate"/>
			<fr:slot name="relatedInformation" key="label.manageCareerWorkshop.relatedInformation"/>
		</fr:schema>
    </fr:view>
</logic:notEmpty>


<h3 class="mbottom025"><bean:message key="label.careerWorkshop.pendingForConfirmation" bundle="STUDENT_RESOURCES"/></h3>

<logic:empty name="pendingForConfirmation">
    <p><em><bean:message key="label.careerWorkshop.noPendingForConfirmation" bundle="STUDENT_RESOURCES"/></em></p>
</logic:empty>

<logic:notEmpty name="pendingForConfirmation">
    <fr:view name="pendingForConfirmation">
        <fr:layout name="tabular">
        	<fr:property name="linkFormat(confirm)" value="<%="/careerWorkshopApplication.do?method=presentConfirmation&eventId=${externalId}"%>"/>
			<fr:property name="order(confirm)" value="1" />
			<fr:property name="key(confirm)" value="label.manageCareerWorkshop.presentConfirmation" />
			<fr:property name="bundle(confirm)" value="STUDENT_RESOURCES" />
        
        	<fr:property name="sortBy" value="careerWorkshopApplicationEvent.beginDate=desc"/>
            <fr:property name="classes" value="tstyle1 thleft thlight mvert05" />
			<fr:property name="columnClasses" value=",,,tdclear tderror1" />	    
        </fr:layout>
         <fr:schema type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmationEvent" bundle="STUDENT_RESOURCES">
			<fr:slot name="beginDate" key="label.manageCareerWorkshop.confirmationStartDate"/>
			<fr:slot name="endDate" key="label.manageCareerWorkshop.confirmationEndDate"/>
		</fr:schema>
    </fr:view>
</logic:notEmpty>