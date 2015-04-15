package org.fenixedu.academic.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Deprecated
public class Strings implements Serializable {

    private final ArrayList<String> stringList;

    public Strings(Collection<String> strings) {
        stringList = new ArrayList<String>(strings);
    }

    public Strings(Strings oldStrings, String... newStrings) {
        this(oldStrings.getUnmodifiableList());
        for (String newString : newStrings) {
            stringList.add(newString);
        }
    }

    public Strings(String[] strings) {
        this(Arrays.asList(strings));
    }

    public Strings(String string) {
        this(Collections.singleton(string));
    }

    public boolean isEmpty() {
        return stringList.isEmpty();
    }

    public int size() {
        return stringList.size();
    }

    public boolean contains(String string) {
        return stringList.contains(string);
    }

    public boolean hasString(String string) {
        return contains(string);
    }

    public Object[] toArray() {
        return stringList.toArray();
    }

    public <T> T[] toArray(T[] arrayType) {
        return stringList.toArray(arrayType);
    }

    public List<String> getUnmodifiableList() {
        return Collections.unmodifiableList(stringList);
    }

    public boolean hasStringIgnoreCase(String string) {
        for (String stringInCollection : stringList) {
            if (stringInCollection.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    public String exportAsString() {
        StringBuilder buffer = new StringBuilder("");
        for (String string : stringList) {
            buffer.append(string.length());
            buffer.append(":");
            buffer.append(string);
        }
        return buffer.toString();
    }

    public static Strings importFromString(String string) {
        if (string == null) {
            return null;
        }
        List<String> strings = new ArrayList<String>();

        int beginIndex = 0;
        int endIndex = find(beginIndex, ':', string);

        while (beginIndex >= 0 && endIndex > beginIndex) {
            int size = Integer.valueOf(string.substring(beginIndex, endIndex++));
            strings.add(string.substring(endIndex, endIndex + size));
            beginIndex = endIndex + size;
            endIndex = beginIndex + find(beginIndex, ':', string);
        }
        return new Strings(strings);
    }

    private static int find(int index, char c, String string) {
        return string.substring(index).indexOf(c);
    }

    public String getPresentationString() {
        final StringBuilder builder = new StringBuilder();
        for (final String string : stringList) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(string);
        }
        return builder.toString();
    }

}
