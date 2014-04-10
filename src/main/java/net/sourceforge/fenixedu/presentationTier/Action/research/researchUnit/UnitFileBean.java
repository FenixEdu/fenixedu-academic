package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;

public class UnitFileBean implements Serializable {

    private final UnitFile file;

    private String name;

    private String description;

    private Group group;

    private String tags;

    protected UnitFileBean() {
        this.file = null;
        group = NobodyGroup.get();
    }

    public UnitFileBean(UnitFile file) {
        this.file = file;
        this.name = file.getDisplayName();
        this.description = file.getDescription();
        setupGroups(file.getPermittedGroup());
        setupTags(file.getUnitFileTags());
    }

    private void setupTags(Collection<UnitFileTag> unitFileTags) {
        String tags = "";
        int i = unitFileTags.size();
        for (UnitFileTag tag : unitFileTags) {
            tags += tag.getName();
            if (--i > 0) {
                tags += " ";
            }
        }
        setTags(tags);
    }

    private void setupGroups(Group permittedGroup) {
        group = group.or(permittedGroup);
    }

    public UnitFile getFile() {
        return file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public Unit getUnit() {
        return getFile().getUnit();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

}
