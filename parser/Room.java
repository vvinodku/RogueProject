package parser;

public class Room extends Structure{

    public String roomName;
    public int room;
    public Creature monster;

    public Room(String _name) {
        roomName = _name;
    }

    public void setID(int _room) {
        room = _room;
    }

    public void setCreature(Creature _monster) {
        monster = _monster;
    }
}
