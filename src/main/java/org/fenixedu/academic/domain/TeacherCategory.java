package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

public class TeacherCategory extends TeacherCategory_Base implements Comparable<TeacherCategory> {

    protected TeacherCategory() {
        super();
        setRoot(Bennu.getInstance());
    }

    public TeacherCategory(String bame, LocalizedString name, Integer weight) {
        this();
        setName(name);
        setWeight(weight);
    }

    public static TeacherCategory find(String name) {
        return Bennu.getInstance().getTeacherCategorySet().stream().filter(c -> c.getName().getContent().equalsIgnoreCase(name))
                .findAny().orElse(null);
    }

    @Override
    public int compareTo(TeacherCategory o) {
        int weigth = getWeight().compareTo(o.getWeight());
        if (weigth != 0) {
            return weigth;
        }
        int byName = getName().compareTo(o.getName());
        if (byName != 0) {
            return byName;
        }
        return getExternalId().compareTo(o.getExternalId());
    }
}
