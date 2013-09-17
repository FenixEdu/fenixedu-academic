<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
