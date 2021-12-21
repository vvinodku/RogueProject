package parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DungeonXMLHandler extends DefaultHandler {

    // the two lines that follow declare a DEBUG flag to control
    // debug print statements and to allow the class to be easily
    // printed out.  These are not necessary for the parser.
    private static final int DEBUG = 1;
    private static final String CLASSID = "DungeonXMLHandler";

    // data can be called anything, but it is the variables that
    // contains information found while parsing the xml file
    private StringBuilder data = null;

    //The dungeon
    public Dungeon dungeon;

    // The current section being parsed
    private Displayable displayableBeingParsed = null;
    private Action actionBeingParsed = null;
    private Creature creatureBeingParsed = null;
    //private Room roomBeingParsed = null;
    //private Player playerBeingParsed = null;
    //private Monster monsterBeingParsed = null;
    //private CreatureAction caBeingParsed = null;
    private Item itemBeingParsed = null;
    //private ItemAction iaBeignParsed = null;

    //booleans
    private boolean visible = false;
    private boolean hp = false;
    private boolean hpMoves = false;
    private boolean posX = false;
    private boolean posY = false;
    private boolean maxHit = false;
    private boolean width = false;
    private boolean height = false;
    private boolean type = false;
    private boolean itemIntValue = false;
    private boolean actionMessage = false;
    private boolean actionIntValue = false;
    private boolean actionCharValue = false;
    

    // Used by code outside the class to get the Dungeon parsed
    public Dungeon getDungeon() {
        return dungeon;
    }

    // A constructor for this class.  It makes an implicit call to the
    // DefaultHandler zero arg constructor, which does the real work
    // DefaultHandler is defined in org.xml.sax.helpers.DefaultHandler;
    // imported above, and we don't need to write it.  We get its 
    // functionality by deriving from it!
    public DungeonXMLHandler() {
    }

    // startElement is called when a <some element> is called as part of 
    // <some element> ... </some element> start and end tags.
    // Rather than explain everything, look at the xml file in one screen
    // and the code below in another, and see how the different xml elements
    // are handled.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (DEBUG > 1) {
            System.out.println(CLASSID + ".startElement qName: " + qName);
        }

        if (qName.equalsIgnoreCase("Dungeon")) {
            String name = attributes.getValue("name");
            int width = Integer.parseInt(attributes.getValue("width"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));
            dungeon = new Dungeon(name, topHeight, width, gameHeight, bottomHeight);
            //System.out.println("Dungeon: " + name + topHeight + " " + width + " " + gameHeight + " " + bottomHeight);

        } 
        else if (qName.equalsIgnoreCase("Rooms")) {
            assert true;

        } 
        else if (qName.equalsIgnoreCase("Room")) {
            String roomNum = attributes.getValue("room");
            Room room = new Room(roomNum);
            displayableBeingParsed = room;
            dungeon.addRoom(room);
            //System.out.println();
            //System.out.println("Room: " + roomNum);

        } 
        else if (qName.equalsIgnoreCase("Passages")) {
            assert true;

        } 
        else if (qName.equalsIgnoreCase("Passage")) {
            int room1 = Integer.parseInt(attributes.getValue("room1"));
            int room2 = Integer.parseInt(attributes.getValue("room2"));
            Passage passage = new Passage();
            passage.setID(room1, room2);
            displayableBeingParsed = passage;
            dungeon.addPassage(passage);
            //System.out.println("Passage");

        } 
        else if (qName.equalsIgnoreCase("Monster")) {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Monster monster = new Monster();
            monster.setName(name);
            monster.setID(room, serial);
            monster.setType(name.charAt(0));
            creatureBeingParsed = monster;
            displayableBeingParsed = monster;
            dungeon.addCreature(monster);
            //System.out.println("Monster");

        } 
        else if (qName.equalsIgnoreCase("Player")) {
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Player player = new Player();
            player.setRoom(room);
            creatureBeingParsed = player;
            displayableBeingParsed = player;
            dungeon.addCreature(player);
            //System.out.println("Player");

        } 
        else if (qName.equalsIgnoreCase("CreatureAction")) {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            CreatureAction creatureAction = new CreatureAction((Creature) creatureBeingParsed, name, type);
            actionBeingParsed = creatureAction;
            //accomodate for multiple death actions
            creatureBeingParsed.addAction(creatureAction);
            // if(type == "death") {
            //     creatureBeingParsed.setDeathAction(creatureAction);
            // }
            // else {
            //     creatureBeingParsed.setHitAction(creatureAction);
            // }
            //System.out.println("Creature Action");

        } 
        else if (qName.equalsIgnoreCase("Armor")) {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Armor armor = new Armor(name);
            armor.setID(room, serial);
            itemBeingParsed = armor;
            displayableBeingParsed = armor;
            if(creatureBeingParsed.getClass() == Player.class) {
                ((Player)creatureBeingParsed).setArmor(armor);
            }
            else {
                dungeon.addItem(armor);
            }
            //System.out.println("Armor");

        } 
        else if (qName.equalsIgnoreCase("Sword")) {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Sword sword = new Sword(name);
            sword.setID(room, serial);
            itemBeingParsed = sword;
            displayableBeingParsed = sword;
            if(creatureBeingParsed.getClass() == Player.class) {
                ((Player)creatureBeingParsed).setWeapon(sword);
            }
            else {
                dungeon.addItem(sword);
            }
            //System.out.println("Sword");

        } 
        else if (qName.equalsIgnoreCase("Scroll")) {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            Scroll scroll = new Scroll(name);
            scroll.setID(room, serial);
            itemBeingParsed = scroll;
            displayableBeingParsed = scroll;
            dungeon.addItem(scroll);
            //System.out.println("Scroll");

        } 
        else if (qName.equalsIgnoreCase("ItemAction")) {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            ItemAction itemAction = new ItemAction((Item)displayableBeingParsed, name, type);
            itemBeingParsed.setAction(itemAction);
            actionBeingParsed = itemAction;
            //System.out.println("Item Action");

        } 
        else if(qName.equalsIgnoreCase("visible")) {
            visible = true;
        } 
        else if(qName.equalsIgnoreCase("hp")) {
            hp = true;
        } 
        else if(qName.equalsIgnoreCase("hpMoves")) {
            hpMoves = true;
        } 
        else if(qName.equalsIgnoreCase("posX")) {
            posX = true;
        } 
        else if(qName.equalsIgnoreCase("posY")) {
            posY = true;
        } 
        else if(qName.equalsIgnoreCase("maxHit")) {
            maxHit = true;
        } 
        else if(qName.equalsIgnoreCase("width")) {
            width = true;
        } 
        else if(qName.equalsIgnoreCase("height")) {
            height = true;
        } else if(qName.equalsIgnoreCase("type")) {
            type = true;
        } 
        else if(qName.equalsIgnoreCase("ItemIntValue")) {
            itemIntValue = true;
        } 
        else if(qName.equalsIgnoreCase("actionMessage")) {
            actionMessage = true;
        } 
        else if(qName.equalsIgnoreCase("actionIntValue")) {
            actionIntValue = true;
        } 
        else if(qName.equalsIgnoreCase("actionCharValue")) {
            actionCharValue = true;
        }
            
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (visible) {
            visible = false;
            displayableBeingParsed.setVisible();

        } 
        else if (hp) {
            hp = false;
            int hp_val = Integer.parseInt(data.toString());
            displayableBeingParsed.setHp(hp_val);

        } 
        else if (hpMoves) {
            hpMoves = false;
            int hpMoves_val = Integer.parseInt(data.toString());
            displayableBeingParsed.setHpMove(hpMoves_val);

        } 
        else if (posX) {
            posX = false;
            int posX_val = Integer.parseInt(data.toString());
            displayableBeingParsed.setPosX(posX_val);

        } 
        else if (posY) {
            posY = false;
            int posY_val = Integer.parseInt(data.toString());
            displayableBeingParsed.setPosY(posY_val);

        } 
        else if (maxHit) {
            maxHit = false;
            int maxHit_val = Integer.parseInt(data.toString());
            displayableBeingParsed.setMaxHit(maxHit_val);

        } 
        else if (width) {
            width = false;
            int width_val = Integer.parseInt(data.toString());
            displayableBeingParsed.setWidth(width_val);

        } 
        else if (height) {
            height = false;
            int height_val = Integer.parseInt(data.toString());
            displayableBeingParsed.setHeight(height_val);


        } 
        else if (type) {
            type = false;
            char type_val = data.charAt(0);
            displayableBeingParsed.setType(type_val);

        } 
        else if (itemIntValue) {
            itemIntValue = false;
            int itemIntValue_val = Integer.parseInt(data.toString());
            displayableBeingParsed.setIntValue(itemIntValue_val);

        } 
        else if (actionMessage) {
            actionMessage = false;
            String actionMessage_val = data.toString();
            actionBeingParsed.setMessage(actionMessage_val);

        } 
        else if (actionIntValue) {
            actionIntValue = false;
            int actionIntValue_val = Integer.parseInt(data.toString());
            actionBeingParsed.setIntValue(actionIntValue_val);

        } 
        else if (actionCharValue) {
            actionCharValue = false;
            char actionCharValue_val = data.charAt(0);
            actionBeingParsed.setCharValue(actionCharValue_val);
        }
        
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
        if (DEBUG > 1) {
            System.out.println(CLASSID + ".characters: " + new String(ch, start, length));
            System.out.flush();
        }
    }

    /*
    @Override
    public String toString() {
        String str = "StudentsXMLHandler\n";
        str += "   maxStudents: " + maxStudents + "\n";
        str += "   studentCount: " + studentCount + "\n";
        for (Student student : students) {
            str += student.toString() + "\n";
        }
        str += "   studentBeingParsed: " + studentBeingParsed.toString() + "\n";
        str += "   activityBeingParsed: " + activityBeingParsed.toString() + "\n";
        return str;
    }
    */
}
