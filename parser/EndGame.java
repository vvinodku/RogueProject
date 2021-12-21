package parser;

public class EndGame extends CreatureAction{
    
    public String name;

    public EndGame(String _name, Creature _owner) {
        super(_owner);
        name = _name;
    }
}