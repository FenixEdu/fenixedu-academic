<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:xhtml/>

<logic:equal name="site" property="showIntroduction" value="true">
	<logic:present name="site" property="description">
	<div class="usiteintro">
		<fr:view name="site"  property="description" layout="html"/>
	</div>
	</logic:present>
	<logic:notPresent name="site" property="description">
		<div style="background-color: #eeeeee; height: 150px;">

		</div>
	</logic:notPresent>
</logic:equal>

<logic:equal name="site" property="showBanner" value="true">
	<bean:define id="style" type="java.lang.String" value="width: 100%; float: left; background-color: #019AD7; background-image: url(http://www.ist.utl.pt/img/spot/bolonha/bolonha_bck.gif); background-repeat: repeat-x;" toScope="request"/>
	
	<logic:notEmpty name="site" property="banners">
	 	<bean:define id="banner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner" name="site" property="currentBanner" toScope="request"/>
		<bean:define id="style" type="java.lang.String" value="<%= ((banner.getColor()!=null) ? "background-color: " + banner.getColor() + ";" : "") + (banner.hasBackgroundImage() ? " background-image: url('" + banner.getBackgroundImage().getDownloadUrl() +"'); background-repeat: repeat-x;" : "") %>" toScope="request"/>
	</logic:notEmpty>

	<div class="usitebanner" style="<%=  style %>">
		<logic:present name="banner">	
			<bean:define id="banner" name="banner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner"/>
			<img src="<%= banner.getMainImage().getDownloadUrl() %>"/>
		</logic:present>
		<logic:notPresent name="banner">
			<a href="http://www.bolonha.ist.eu"><img src="http://www.ist.utl.pt/img/spot/bolonha/bolonha.gif" alt="Com o IST, entra no melhor ensino superior europeu - www.bolonha.ist.eu" width="420" height="150" />
		</logic:notPresent>
	</div>
	<div style="clear: both;"></div>
</logic:equal>

<jsp:include flush="true" page="mainBody.jsp"/>