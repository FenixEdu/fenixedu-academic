<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">	
	<em><bean:message key="label.researchPortal" bundle="RESEARCHER_RESOURCES"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="title.insert.new.interest"/></h2><br/>
	
	<fr:create type="net.sourceforge.fenixedu.domain.research.ResearchInterest" schema="researchInterest.simpleCreate"
	           action="/interests/interestsManagement.do?method=prepare">
		<fr:hidden slot="order" name="lastOrder"/>
		<fr:hidden slot="party" name="party" />
		<fr:destination name="cancel" path="/interests/interestsManagement.do?method=prepare"/>
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thtop mtop0"/>
		</fr:layout>
	</fr:create>

</logic:present>