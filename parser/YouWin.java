package parser;

public class YouWin extends CreatureAction{
    
    public String name;

    public YouWin(String _name, Creature _owner) {
        super(_owner);
        name = _name;
    }
}