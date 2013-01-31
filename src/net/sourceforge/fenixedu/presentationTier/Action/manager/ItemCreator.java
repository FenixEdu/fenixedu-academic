package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ItemCreator implements Serializable {

	private static final long serialVersionUID = 1L;

	private MultiLanguageString name;
	private MultiLanguageString information;
	private boolean visible;
	private boolean showName;

	private Section section;
	private Item nextItem;

	private Group permittedGroup;

	public ItemCreator(Section section) {
		super();

		this.section = section;
		this.nextItem = null;
		this.visible = true;
		this.showName = true;
		this.permittedGroup = new EveryoneGroup();
	}

	public MultiLanguageString getInformation() {
		return this.information;
	}

	public void setInformation(MultiLanguageString information) {
		this.information = information;
	}

	public MultiLanguageString getName() {
		return this.name;
	}

	public void setName(MultiLanguageString name) {
		this.name = name;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isShowName() {
		return showName;
	}

	public void setShowName(boolean showName) {
		this.showName = showName;
	}

	public Item getNextItem() {
		return this.nextItem;
	}

	public void setNextItem(Item nextItem) {
		this.nextItem = nextItem;
	}

	public Section getSection() {
		return this.section;
	}

	public Group getPermittedGroup() {
		return this.permittedGroup;
	}

	public void setPermittedGroup(Group permittedGroup) {
		this.permittedGroup = permittedGroup;
	}

	public void createItem() {
		Item item = new Item(getSection(), getName());

		item.setBody(getInformation());
		item.setNextItem(getNextItem());
		item.setPermittedGroup(getPermittedGroup());
		item.setVisible(isVisible());
		item.setShowName(isShowName());
		item.logCreateItemtoSection();
	}
}
