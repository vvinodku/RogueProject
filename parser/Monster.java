package parser;

public class Monster extends Creature{
    
    public String name;
    public int room;
    public int serial;
    public char type;

    public Monster() {
    }

    public void setName(String _name) {
        name = _name;
    }

    public void setID(int _room, int _serial) {
        room = _room;
        serial = _serial;   
    }

    public void setType(char _type) {
        type = _type;
    }
}
