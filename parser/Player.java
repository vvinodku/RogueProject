package parser;

public class Player extends Creature{

    public Item sword;
    public Item armor;
    public int room;
    public char type = '@';

    public void setRoom(int _room){
        room = _room;
    }

    public void setWeapon(Item _sword) {
        sword = _sword;
    }

    public void setArmor(Item _armor) {
        armor = _armor;
    }

    public void setType(char _type) {
        type = _type;
    }
}