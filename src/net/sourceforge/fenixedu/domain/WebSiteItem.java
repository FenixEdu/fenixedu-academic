package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public class WebSiteItem extends DomainObject implements IWebSiteItem {
    private String title;

    private String mainEntryText;

    private String excerpt;

    private Boolean published;

    private Timestamp creationDate;

    private String keywords;

    private Date onlineBeginDay;

    private Date onlineEndDay;

    private Date itemBeginDay;

    private Date itemEndDay;

    private String authorName;

    private String authorEmail;

    private IWebSiteSection webSiteSection;

    private IPerson editor;

    private Integer keyWebSiteSection;

    private Integer keyEditor;

    /**
     * Construtor
     */
    public WebSiteItem() {
    }

    /**
     * Construtor
     */
    public WebSiteItem(Integer idInternal) {
        setIdInternal(idInternal);
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

    /**
     * @return
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return
     */
    public IPerson getEditor() {
        return editor;
    }

    /**
     * @param editor
     */
    public void setEditor(IPerson editor) {
        this.editor = editor;
    }

    /**
     * @return
     */
    public String getExcerpt() {
        return excerpt;
    }

    /**
     * @param excerpt
     */
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    /**
     * @return
     */
    public Date getItemBeginDay() {
        return itemBeginDay;
    }

    /**
     * @param itemBeginDay
     */
    public void setItemBeginDay(Date itemBeginDay) {
        this.itemBeginDay = itemBeginDay;
    }

    /**
     * @return
     */
    public Date getItemEndDay() {
        return itemEndDay;
    }

    /**
     * @param itemEndDay
     */
    public void setItemEndDay(Date itemEndDay) {
        this.itemEndDay = itemEndDay;
    }

    /**
     * @return
     */
    public Integer getKeyEditor() {
        return keyEditor;
    }

    /**
     * @param keyEditor
     */
    public void setKeyEditor(Integer keyEditor) {
        this.keyEditor = keyEditor;
    }

    /**
     * @return
     */
    public Integer getKeyWebSiteSection() {
        return keyWebSiteSection;
    }

    /**
     * @param keyWebSiteSection
     */
    public void setKeyWebSiteSection(Integer keyWebSiteSection) {
        this.keyWebSiteSection = keyWebSiteSection;
    }

    /**
     * @return
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * @param keywords
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * @return
     */
    public String getMainEntryText() {
        return mainEntryText;
    }

    /**
     * @param mainEntryText
     */
    public void setMainEntryText(String mainEntryText) {
        this.mainEntryText = mainEntryText;
    }

    /**
     * @return
     */
    public Date getOnlineBeginDay() {
        return onlineBeginDay;
    }

    /**
     * @param onlineBeginDay
     */
    public void setOnlineBeginDay(Date onlineBeginDay) {
        this.onlineBeginDay = onlineBeginDay;
    }

    /**
     * @return
     */
    public Date getOnlineEndDay() {
        return onlineEndDay;
    }

    /**
     * @param onlineÊndDay
     */
    public void setOnlineEndDay(Date onlineEndDay) {
        this.onlineEndDay = onlineEndDay;
    }

    /**
     * @return
     */
    public Boolean getPublished() {
        return published;
    }

    /**
     * @param published
     */
    public void setPublished(Boolean published) {
        this.published = published;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return
     */
    public IWebSiteSection getWebSiteSection() {
        return webSiteSection;
    }

    /**
     * @param webSiteSection
     */
    public void setWebSiteSection(IWebSiteSection webSiteSection) {
        this.webSiteSection = webSiteSection;
    }

    /**
     * @return
     */
    public String getAuthorEmail() {
        return authorEmail;
    }

    /**
     * @param authorEmail
     */
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    /**
     * @return
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * @param authorName
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}