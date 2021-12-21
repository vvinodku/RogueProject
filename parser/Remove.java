package parser;

public class Remove extends CreatureAction{

    public String name;

    public Remove(String _name, Creature _owner) {
        super(_owner);
        name = _name;
    }
}
