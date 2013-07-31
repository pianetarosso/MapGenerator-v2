package objects;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 12.51
 * To change this template use File | Settings | File Templates.
 */
public class Room {

    private final int id;

    public int getId() {
        return id;
    }

    private String name;
    private String people;
    private String other;
    private String link;


    public Room(String name, int id) {

        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String toString() {

        return "Room Id: " + id + "; " +
                "Name: " + name + "; " +
                "People: " + people + "; " +
                "Other: " + other + "; " +
                "Link: " + link + "; ";
    }
}
