package Dominio;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Fernanda Quiterio 23/09/2003
 *  
 */
public interface IWebSiteItem extends IDomainObject {

    public Timestamp getCreationDate();

    public void setCreationDate(Timestamp creationDate);

    public IPessoa getEditor();

    public void setEditor(IPessoa editor);

    public String getExcerpt();

    public void setExcerpt(String excerpt);

    public Date getItemBeginDay();

    public void setItemBeginDay(Date itemBeginDay);

    public Date getItemEndDay();

    public void setItemEndDay(Date itemEndDay);

    public Integer getKeyEditor();

    public void setKeyEditor(Integer keyEditor);

    public Integer getKeyWebSiteSection();

    public String getAuthorName();

    public void setAuthorName(String authorName);

    public String getAuthorEmail();

    public void setAuthorEmail(String authorEmail);

    public void setKeyWebSiteSection(Integer keyWebSiteSection);

    public String getKeywords();

    public void setKeywords(String keywords);

    public String getMainEntryText();

    public void setMainEntryText(String mainEntryText);

    public Date getOnlineBeginDay();

    public void setOnlineBeginDay(Date onlineBeginDay);

    public Date getOnlineEndDay();

    public void setOnlineEndDay(Date onlineEndDay);

    public Boolean getPublished();

    public void setPublished(Boolean published);

    public String getTitle();

    public void setTitle(String title);

    public IWebSiteSection getWebSiteSection();

    public void setWebSiteSection(IWebSiteSection webSiteSection);
}