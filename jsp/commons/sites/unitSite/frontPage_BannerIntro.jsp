<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:equal name="site" property="showBanner" value="true">

<bean:define id="style" type="java.lang.String" value="width: 100%; float: left;" toScope="request"/>
 
<bean:define id="banner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner" name="site" property="currentBanner" toScope="request"/>

<logic:notEmpty name="site" property="banners">
 <bean:define id="banner" name="site" property="currentBanner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner" toScope="request"/>
 <bean:define id="style" type="java.lang.String" value="<%= ((banner.getColor()!=null) ? "background-color: " + banner.getColor() + ";" : "") + (banner.hasBackgroundImage() ? " background-image: url('" + banner.getBackgroundImage().getDownloadUrl() +"'); background-repeat: repeat-x;" : "") %>" toScope="request"/>
</logic:notEmpty>


<div class="usitebanner" style="<%=  style %>">
	<logic:present name="banner" >
		<img src="<%= banner.getMainImage().getDownloadUrl() %>"/>
	</logic:present>
</div>
<div style="clear: both;"></div>

</logic:equal>

<logic:equal name="site" property="showIntroduction" value="true">
	<div class="usiteintro">
		<fr:view name="site" property="description" layout="html"/>
	</div>
</logic:equal>

<jsp:include flush="true" page="mainBody.jsp"/>