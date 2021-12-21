package parser;

public class ChangedDisplayType extends CreatureAction{
    
    public String name;

    public ChangedDisplayType(String _name, Creature _owner) {
        super(_owner);
        name = _name;
    }
}
