package parser;

public class ItemAction extends Action{

    public Item owner;
    public String name;
    public String type;

    public ItemAction(Item _owner) {
        owner = _owner;
    }

    public ItemAction(Item _owner, String _name, String _type) {
        owner = _owner;
        name = _name;
        type = _type;
    }
}
