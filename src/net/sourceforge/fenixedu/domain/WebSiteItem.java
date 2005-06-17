package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Fernanda Quitério 23/09/2003
 * 
 */
public class WebSiteItem extends WebSiteItem_Base {

    /**
     * @return
     */
    public Timestamp getCreationDate() {
        if (this.getCreation() != null) {
            return new Timestamp(this.getCreation().getTime());
        }
        return null;
    }

    /**
     * @param creationDate
     */
    public void setCreationDate(Timestamp creationDate) {
        if (creationDate != null) {
            this.setCreation(new Date(creationDate.getTime()));
        } else {
            this.setCreation(null);
        }
    }

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

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof IWebSiteItem) {
            IWebSiteItem webSiteItem = (IWebSiteItem) arg0;

            if (elementsAreEqual(webSiteItem.getTitle(), this.getTitle())
                    && elementsAreEqual(webSiteItem.getCreationDate(), this.getCreationDate())
                    && elementsAreEqual(webSiteItem.getEditor(), this.getEditor())
                    && elementsAreEqual(webSiteItem.getAuthorName(), this.getAuthorName())
                    && elementsAreEqual(webSiteItem.getAuthorEmail(), this.getAuthorEmail())
                    && elementsAreEqual(webSiteItem.getExcerpt(), this.getExcerpt())
                    && elementsAreEqual(webSiteItem.getItemBeginDay(), this.getItemBeginDay())
                    && elementsAreEqual(webSiteItem.getItemEndDay(), this.getItemEndDay())
                    && elementsAreEqual(webSiteItem.getKeywords(), this.getKeywords())
                    && elementsAreEqual(webSiteItem.getMainEntryText(), this.getMainEntryText())
                    && elementsAreEqual(webSiteItem.getOnlineBeginDay(), this.getOnlineBeginDay())
                    && elementsAreEqual(webSiteItem.getOnlineEndDay(), this.getOnlineEndDay())
                    && elementsAreEqual(webSiteItem.getPublished(), this.getPublished())
                    && elementsAreEqual(webSiteItem.getWebSiteSection(), this.getWebSiteSection())) {
                result = true;
            }
        }
        return result;
    }

    private boolean elementsAreEqual(Object element1, Object element2) {
        boolean result = false;
        if ((element1 == null && element2 == null)
                || (element1 != null && element2 != null && element1.equals(element2))) {
            result = true;
        }
        return result;
    }

}
