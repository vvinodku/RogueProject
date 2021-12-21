package parser;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {

    public String name;
    public int topHeight;
    public int width;
    public int gameHeight;
    public int bottomHeight;

    public List<Room> rooms = new ArrayList<Room>();
    public List<Creature> creatures = new ArrayList<Creature>();
    public List<Passage> passages = new ArrayList<Passage>();
    public List<Item> items = new ArrayList<Item>();

    public Dungeon(String _name, int _topHeight, int _width, int _gameHeight, int _bottomHeight) {
        name = _name;
        topHeight = _topHeight;
        width = _width;
        gameHeight = _gameHeight;   
        bottomHeight = _bottomHeight;
    }

    public void getDungeon(String _name, int _width, int _gameHeight) {
        
    }

    public void addRoom(Room _room) {
        rooms.add(_room);
    }

    public void addCreature(Creature _creature) {
        creatures.add(_creature);
    }

    public void addPassage(Passage _passage) {
        passages.add(_passage);
    }
    
    public void addItem(Item _item) {
        items.add(_item);
    }
}
