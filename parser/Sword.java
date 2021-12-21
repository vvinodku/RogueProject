package parser;

public class Sword extends Item{

    public int serial;

    public Sword(String _name) {
        name = _name;
    }

    public void setID(int _room, int _serial) {
        room = _room;
        serial = _serial;
    }
}
