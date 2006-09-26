<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.search" /></h2>

<fr:create id="search" action="/parking.do&method=searchParkingParty" 
type="net.sourceforge.fenixedu.domain.parking.ParkingParty"
schema="input.searchParkingParty"/>

