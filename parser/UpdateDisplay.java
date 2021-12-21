package parser;

public class UpdateDisplay extends CreatureAction{
    
    public String name;

    public UpdateDisplay(String _name, Creature _owner) {
        super(_owner);
        name = _name;
    }
}