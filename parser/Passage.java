package parser;

public class Passage extends Displayable{

    public String name;
    public int room1;
    public int room2;
    public int posXarr[] = new int[100];
    public int posYarr[] = new int[100];
    public int i = 0;

    public void setName(String _name) {
        name = _name;
    }

    public void setID(int _room1, int _room2) {
        room1 = _room1;
        room2 = _room2;
    }

    public void setPosX(int _posX) {
        posXarr[i] = _posX;
    }

    public void setPosY(int _posY) {
        posYarr[i++] = _posY;
    }
}
