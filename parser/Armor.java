package parser;

public class Armor extends Item{

    public int serial;

    public Armor(String _name) {
        name = _name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public void setID(int _room, int _serial) {
        room = _room;
        serial = _serial;
    }
}
