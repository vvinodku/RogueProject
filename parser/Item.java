package parser;

public class Item extends Displayable{

    public Creature owner;
    public int room;
    public ItemAction action;
    public String name;
    public String message;
    public char type;

    public void setOwner(Creature _owner) {
        owner = _owner;
    }

    public void setAction(ItemAction _action) {
        action = _action;
    }

    public void setType(char _type) {
        type = _type;
    }

}
