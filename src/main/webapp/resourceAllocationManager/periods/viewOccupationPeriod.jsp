<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<fr:view name="occupationPeriod" property="startYearMonthDay"/>
-
<fr:view name="occupationPeriod" property="endYearMonthDay"/>

<logic:present name="occupationPeriod" property="nextPeriod">
	<br/>
	<bean:define id="occupationPeriod" name="occupationPeriod" property="nextPeriod" toScope="request"/>
	<jsp:include page="viewOccupationPeriod.jsp"/>
</logic:present>
