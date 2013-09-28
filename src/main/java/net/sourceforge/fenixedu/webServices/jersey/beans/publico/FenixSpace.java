package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSpace.Room.RoomEvent.WrittenEvaluationEvent.ExecutionCourse;

import org.codehaus.jackson.annotate.JsonSubTypes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = FenixSpace.Campus.class, name = "CAMPUS"),
        @JsonSubTypes.Type(value = FenixSpace.Building.class, name = "BUILDING"),
        @JsonSubTypes.Type(value = FenixSpace.Floor.class, name = "FLOOR"),
        @JsonSubTypes.Type(value = FenixSpace.Room.class, name = "ROOM") })
public class FenixSpace {

    public static class Campus extends FenixSpace {
        @JsonSerialize(typing = JsonSerialize.Typing.STATIC)
        List<? extends FenixSpace> buildings;

        public Campus(String id, String name) {
            super(id, name);
        }

        public Campus(String id, String name, List<? extends FenixSpace> buildings) {
            super(id, name);
            this.buildings = buildings;
        }

        public List<? extends FenixSpace> getBuildings() {
            return buildings;
        }

        public void setBuildings(List<? extends FenixSpace> buildings) {
            this.buildings = buildings;
        }

    }

    public static class Building extends FenixSpace {

        Campus campus;
        @JsonSerialize(typing = JsonSerialize.Typing.STATIC)
        List<? extends FenixSpace> floors;

        public Building(String id, String name) {
            super(id, name);
        }

        public Building(String id, String name, Campus campus, List<? extends FenixSpace> floors) {
            super(id, name);
            this.campus = campus;
            this.floors = floors;
        }

        public Campus getCampus() {
            return campus;
        }

        public void setCampus(Campus campus) {
            this.campus = campus;
        }

        public List<? extends FenixSpace> getFloors() {
            return floors;
        }

        public void setFloors(List<? extends FenixSpace> floors) {
            this.floors = floors;
        }

    }

    public static class Floor extends FenixSpace {
        Building building;
        @JsonSerialize(typing = JsonSerialize.Typing.STATIC)
        List<Room> rooms;

        public Floor(String id, String name) {
            super(id, name);
        }

        public Floor(String id, String name, Building building, List<Room> rooms) {
            super(id, name);
            this.building = building;
            this.rooms = rooms;
        }

        public Building getBuilding() {
            return building;
        }

        public void setBuilding(Building building) {
            this.building = building;
        }

        public List<Room> getRooms() {
            return rooms;
        }

        public void setRooms(List<Room> rooms) {
            this.rooms = rooms;
        }

    }

    public static class Room extends FenixSpace {

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
        @JsonSubTypes({ @JsonSubTypes.Type(value = RoomEvent.LessonEvent.class, name = "LESSON"),
                @JsonSubTypes.Type(value = RoomEvent.WrittenEvaluationEvent.TestEvent.class, name = "TEST"),
                @JsonSubTypes.Type(value = RoomEvent.WrittenEvaluationEvent.ExamEvent.class, name = "EXAM"),
                @JsonSubTypes.Type(value = RoomEvent.GenericEvent.class, name = "GENERIC") })
        public static abstract class RoomEvent {

            public static class LessonEvent extends RoomEvent {
                String info;
                WrittenEvaluationEvent.ExecutionCourse course;

                public LessonEvent(String start, String end, String weekday, String info, ExecutionCourse course) {
                    super(start, end, weekday);
                    this.info = info;
                    this.course = course;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public WrittenEvaluationEvent.ExecutionCourse getCourse() {
                    return course;
                }

                public void setCourses(WrittenEvaluationEvent.ExecutionCourse course) {
                    this.course = course;
                }

            }

            public static abstract class WrittenEvaluationEvent extends RoomEvent {

                public static class ExecutionCourse {
                    String sigla;
                    String name;
                    String id;

                    public ExecutionCourse(String sigla, String name, String id) {
                        super();
                        this.sigla = sigla;
                        this.name = name;
                        this.id = id;
                    }

                    public String getSigla() {
                        return sigla;
                    }

                    public void setSigla(String sigla) {
                        this.sigla = sigla;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                }

                public static class TestEvent extends WrittenEvaluationEvent {
                    String description;

                    public TestEvent(String start, String end, String weekday, List<ExecutionCourse> courses, String description) {
                        super(start, end, weekday, courses);
                        this.description = description;
                    }

                    public String getDescription() {
                        return description;
                    }

                    public void setDescription(String description) {
                        this.description = description;
                    }

                }

                public static class ExamEvent extends WrittenEvaluationEvent {
                    Integer season;

                    public ExamEvent(String start, String end, String weekday, List<ExecutionCourse> courses, Integer season) {
                        super(start, end, weekday, courses);
                        this.season = season;
                    }

                    public Integer getSeason() {
                        return season;
                    }

                    public void setSeason(Integer season) {
                        this.season = season;
                    }

                }

                List<WrittenEvaluationEvent.ExecutionCourse> courses;

                public WrittenEvaluationEvent(String start, String end, String weekday, List<ExecutionCourse> courses) {
                    super(start, end, weekday);
                    this.courses = courses;
                }

                public List<WrittenEvaluationEvent.ExecutionCourse> getCourses() {
                    return courses;
                }

                public void setCourses(List<WrittenEvaluationEvent.ExecutionCourse> courses) {
                    this.courses = courses;
                }

            }

            public static class GenericEvent extends RoomEvent {
                String description;
                String title;

                public GenericEvent(String start, String end, String weekday, String description, String title) {
                    super(start, end, weekday);
                    this.description = description;
                    this.title = title;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

            }

            String start;
            String end;
            String weekday;

            public RoomEvent(String start, String end, String weekday) {
                super();
                this.start = start;
                this.end = end;
                this.weekday = weekday;
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

            public String getWeekday() {
                return weekday;
            }

            public void setWeekday(String weekday) {
                this.weekday = weekday;
            }

        }

        @JsonSerialize(typing = JsonSerialize.Typing.STATIC)
        Floor floor;
        String description;
        Integer normalCapacity;
        Integer examCapacity;
        private List<? extends RoomEvent> events;

        public Room(String id, String name) {
            super(id, name);
        }

        public Room(String id, String name, Floor floor, String description, Integer normalCapacity, Integer examCapacity,
                List<? extends RoomEvent> events) {
            super(id, name);
            this.floor = floor;
            this.description = description;
            this.normalCapacity = normalCapacity;
            this.examCapacity = examCapacity;
            this.events = events;
        }

        public Floor getFloor() {
            return floor;
        }

        public void setFloor(Floor floor) {
            this.floor = floor;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getNormalCapacity() {
            return normalCapacity;
        }

        public void setNormalCapacity(Integer normalCapacity) {
            this.normalCapacity = normalCapacity;
        }

        public Integer getExamCapacity() {
            return examCapacity;
        }

        public void setExamCapacity(Integer examCapacity) {
            this.examCapacity = examCapacity;
        }

        public List<? extends RoomEvent> getEvents() {
            return events;
        }

        public void setEvents(List<? extends RoomEvent> events) {
            this.events = events;
        }

    }

    String id;
    String name;

    public FenixSpace(String id, String name) {
        super();
        this.id = id;
        this.name = name;
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

}