package parser;

public class Teleport extends CreatureAction{

    public String name;

    public Teleport(String _name, Creature _owner) {
        super(_owner);
        name = _name;
    }
}
