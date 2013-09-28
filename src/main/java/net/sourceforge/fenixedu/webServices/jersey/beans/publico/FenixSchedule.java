package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

public class FenixSchedule {

    public static class Lesson {

        public static class Room {

            private String id;
            private String name;
            private String description;

            public Room(String id, String name, String description) {
                this.id = id;
                this.name = name;
                this.description = description;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

        private String weekDay;
        private String lessonType;
        private String start;
        private String end;
        private Room room;

        public Lesson(String weekDay, String lessonType, String start, String end, Room room) {
            this.weekDay = weekDay;
            this.lessonType = lessonType;
            this.start = start;
            this.end = end;
            this.room = room;
        }

        public String getWeekDay() {
            return weekDay;
        }

        public void setWeekDay(String weekDay) {
            this.weekDay = weekDay;
        }

        public String getLessonType() {
            return lessonType;
        }

        public void setLessonType(String lessonType) {
            this.lessonType = lessonType;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public Room getRoom() {
            return room;
        }

        public void setRoom(Room room) {
            this.room = room;
        }

    }

    public static class Period {

        private String start;
        private String end;

        public Period(String start, String end) {
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

    }

    String name;
    String year;
    Integer semester;

    List<Period> periods;
    List<Lesson> lessons;

    public FenixSchedule(String name, String year, Integer semester, List<Period> periods, List<Lesson> lessons) {
        super();
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.periods = periods;
        this.lessons = lessons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

}
