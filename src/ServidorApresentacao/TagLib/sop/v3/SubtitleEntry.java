package ServidorApresentacao.TagLib.sop.v3;

public class SubtitleEntry implements Comparable {
    private String key;

    private String value;

    public SubtitleEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        return o instanceof SubtitleEntry && key.equals(((SubtitleEntry) o).getKey())
                && value.equals(((SubtitleEntry) o).getValue());
    }

    public int compareTo(Object obj) {
        return getValue().compareTo(((SubtitleEntry) obj).getValue());
    }

}

// Created by Nuno Antão
