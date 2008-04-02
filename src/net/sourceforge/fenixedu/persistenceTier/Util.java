package net.sourceforge.fenixedu.persistenceTier;

public class Util {
    public static Object getEnum(Class<? extends Enum> enumClass, String name) {
        return ((name == null) || name.equals("")) ? null : Enum.valueOf(enumClass, name);
    }
}
