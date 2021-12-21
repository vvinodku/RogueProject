package parser;

public class CreatureAction extends Action{

    public Creature owner;
    public String name;
    public String type;

    public CreatureAction(Creature _owner) {
        owner = _owner;
    }
    public CreatureAction(Creature _owner, String _name, String _type) {
        owner = _owner;
        name = _name;
        type = _type;
    }
}
