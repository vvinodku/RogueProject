package parser;

public class Displayable {

    public boolean visibility = true;
    public int maxHit;
    public int hpMoves;
    public int hp;
    public char type;
    public int intValue;
    public int posX;
    public int posY;
    public int height;
    public int width;

    public Displayable() {
    }

    public void setInvisible() {
        visibility = false;
    }

    public void setVisible( ) {
        visibility = true;
    }

    public void setMaxHit(int _maxHit) {
        maxHit = _maxHit;
    }

    public void setHpMove(int _hpMoves) {
        hpMoves = _hpMoves;
    }

    public void setHp(int _hp) {
        hp = _hp;
    }

    public void setType(char _type) {
        type = _type;
    }

    public void setIntValue(int _intValue) {
        intValue = _intValue;
    }

    public void setPosX(int _posX){
        posX = _posX;
    }

    public void setPosY(int _posY) {
        posY = _posY;
    }

    public void setWidth(int _width) {
        width = _width;
    }

    public void setHeight(int _height) {
        height = _height;
    }
}
