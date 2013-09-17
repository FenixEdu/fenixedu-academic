<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<fr:view name="occupationPeriod" property="startYearMonthDay"/>
-
<fr:view name="occupationPeriod" property="endYearMonthDay"/>

<logic:present name="occupationPeriod" property="nextPeriod">
	<br/>
	<bean:define id="occupationPeriod" name="occupationPeriod" property="nextPeriod" toScope="request"/>
	<jsp:include page="viewOccupationPeriod.jsp"/>
</logic:present>
