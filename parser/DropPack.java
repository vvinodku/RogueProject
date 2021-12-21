package parser;

public class DropPack extends CreatureAction{
    
    public String name;

    public DropPack(String _name, Creature _owner) {
        super(_owner);
        name = _name;
    }
}