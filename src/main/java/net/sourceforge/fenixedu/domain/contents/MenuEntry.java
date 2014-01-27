package net.sourceforge.fenixedu.domain.contents;

import java.util.Collection;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public interface MenuEntry {

    public String getEntryId();

    public MultiLanguageString getName();

    public MultiLanguageString getTitle();

    public String getPath();

    public boolean isNodeVisible();

    public boolean isAvailable();

    public Collection<MenuEntry> getChildren();

    public Content getReferingContent();

}
