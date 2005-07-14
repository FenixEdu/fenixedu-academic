package net.sourceforge.fenixedu.domain;


/**
 * @author Fernanda Quitério 23/09/2003
 * 
 */
public class WebSiteItem extends WebSiteItem_Base {

    public String toString() {
        String result = "[WEBSITEITEM";
        result += ", codInt=" + this.getIdInternal();
        result += ", title=" + this.getTitle();
        result += ", mainEntryText=" + this.getMainEntryText();
        result += ", excerpt=" + this.getExcerpt();
        result += ", published=" + this.getPublished();
        result += ", creationDate=" + this.getCreationDate();
        result += ", keywords=" + this.getKeywords();
        result += ", onlineBeginDay=" + this.getOnlineBeginDay();
        result += ", onlineEndDay=" + this.getOnlineEndDay();
        result += ", itemBeginDay=" + this.getItemBeginDay();
        result += ", itemEndDay=" + this.getItemEndDay();
        result += ", webSiteSection=" + this.getWebSiteSection();
        result += ", editor=" + this.getEditor();
        result += ", authorName=" + this.getAuthorName();
        result += ", authorEmail=" + this.getAuthorEmail();
        result += "]";
        return result;
    }

}
