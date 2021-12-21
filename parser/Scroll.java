package parser;

public class Scroll extends Item{

    public int serial;

    public Scroll(String _name) {
        name = _name;
    }

    public void setID(int _room, int _serial) {
        room = _room;
        serial = _serial;
    }

}
