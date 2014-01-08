package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.ByteArray;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class BlueprintFile extends BlueprintFile_Base {

    static {
        getRelationBlueprintBlueprintFile().addListener(new BlueprintBlueprintFileListener());
    }

    public BlueprintFile(Blueprint blueprint, String filename, String displayName, Group permittedGroup, byte[] content) {

        super();
        setBlueprint(blueprint);
        init(filename, displayName, content, permittedGroup);
        setContentFile(new ByteArray(content));
    }

    @Override
    public void setBlueprint(Blueprint blueprint) {
        if (blueprint == null) {
            throw new DomainException("error.blueprintFile.no.blueprint");
        }
        super.setBlueprint(blueprint);
    }

    @Override
    public void delete() {
        super.setBlueprint(null);
        super.delete();
    }

    public String getDirectDownloadUrlFormat() {
        return getDownloadUrl();
    }

    private static class BlueprintBlueprintFileListener extends RelationAdapter<BlueprintFile, Blueprint> {
        @Override
        public void afterRemove(BlueprintFile blueprintFile, Blueprint blueprint) {
            if (blueprintFile != null && blueprint != null) {
                blueprintFile.delete();
            }
        }
    }

    @Override
    public byte[] getContent() {
        return super.getContentFile().getBytes();
    }

    @Deprecated
    public boolean hasBlueprint() {
        return getBlueprint() != null;
    }

    @Deprecated
    public boolean hasContent() {
        return getContent() != null;
    }

}
