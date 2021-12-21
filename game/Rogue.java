package game;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

import parser.*;

public class Rogue implements Runnable {

    private static final int DEBUG = 0;
    public static final int FRAMESPERSECOND = 60;
    public static final int TIMEPERLOOP = 1000000000 / FRAMESPERSECOND;
    private static ObjectDisplayGrid displayGrid = null;
    private Thread keyStrokePrinter;
    private static DungeonXMLHandler handler;
    private static Dungeon dungeon;
    private static int width;
    private static int height;


    public Rogue() {
        dungeon = handler.getDungeon();
        width = dungeon.width;
        height = dungeon.gameHeight + dungeon.topHeight + dungeon.bottomHeight;
        displayGrid = new ObjectDisplayGrid(width, height, dungeon);
    }

    @Override
    public void run() {
        displayGrid.fireUp();
        display();
        
        

    }

    public static void display() {
        dungeon = handler.getDungeon();

        //height adjustment = 
        int ha = dungeon.topHeight;

        //display rooms
        List<Room> rooms = dungeon.rooms;
        for (int i = 0; i < rooms.size(); i += 1) {
            int x = rooms.get(i).posX;
            int y = rooms.get(i).posY + ha;
            int w = rooms.get(i).width;
            int h = rooms.get(i).height;
            for (int x2 = x; x2 < x + w; x2 += 1) {
                displayGrid.addObjectToDisplay(new Char('X'), x2, y);
                displayGrid.addObjectToDisplay(new Char('X'), x2, y + h - 1);
            }
            for (int y2 = y; y2 < y + h; y2 += 1) {
                displayGrid.addObjectToDisplay(new Char('X'), x, y2);
                displayGrid.addObjectToDisplay(new Char('X'), x + w - 1, y2);
            }

            for(int ctr1 = x + 1; ctr1 < x + w - 1; ctr1 ++) {
                for(int ctr2 = y + 1; ctr2 < y + h - 1; ctr2 ++) {
                    displayGrid.addObjectToDisplay(new Char('.'), ctr1, ctr2);
                }
            }

        }

        //display passages
        List<Passage> passages = dungeon.passages;
        for (int i = 0; i < passages.size(); i += 1) {
            for (int h = 0; h < dungeon.passages.get(i).i - 1; h += 1) {
                if(passages.get(i).posXarr[h] == passages.get(i).posXarr[h + 1]) {
                    //System.out.println("x:" + passages.get(i).posXarr[h] + " y:" + passages.get(i).posYarr[h] + " to " + passages.get(i).posYarr[h + 1]);
                    if(passages.get(i).posYarr[h] > passages.get(i).posYarr[h + 1]) {
                        for(int y = passages.get(i).posYarr[h]; y >=  passages.get(i).posYarr[h + 1]; y -= 1) {
                            displayGrid.addObjectToDisplay(new Char('#'), passages.get(i).posXarr[h], y + ha);
                        }                            
                    }
                    else {
                        for(int y = passages.get(i).posYarr[h]; y <=  passages.get(i).posYarr[h + 1]; y += 1) {
                            displayGrid.addObjectToDisplay(new Char('#'), passages.get(i).posXarr[h], y + ha);
                        } 
                    }
                }            
                else {
                    //System.out.println("y:" + passages.get(i).posYarr[h] + " x:" + passages.get(i).posXarr[h] + " to " + passages.get(i).posXarr[h + 1]);
                    if(passages.get(i).posXarr[h] > passages.get(i).posXarr[h + 1]) {
                        for(int x = passages.get(i).posXarr[h]; x >=  passages.get(i).posXarr[h + 1]; x-= 1) {
                            displayGrid.addObjectToDisplay(new Char('#'), x, passages.get(i).posYarr[h] + ha);
                        }                            
                    }
                    else {
                        for(int x = passages.get(i).posXarr[h]; x <=  passages.get(i).posXarr[h + 1]; x+= 1) {
                            displayGrid.addObjectToDisplay(new Char('#'), x, passages.get(i).posYarr[h] + ha);
                        } 
                    }
                }       
            }
        }
        
        //display items
        List<Item> items = dungeon.items;
        for (int i = 0; i < items.size(); i += 1) {
            Item item = (Item) items.get(i);
            // if(item.name.equals("Sword")) {
            //     displayGrid.addObjectToDisplay(new Char('|'), item.posX, item.posY);
            // }
            // else if(item.name.equals("Armor")) {
            //     displayGrid.addObjectToDisplay(new Char(']'), item.posX, item.posY);
            // }
            // else if(item.name.equals("Scroll")){
            //     displayGrid.addObjectToDisplay(new Char('?'), item.posX, item.posY);
            // }
            displayGrid.addObjectToDisplay(new Char(item.type), item.posX, item.posY);
        }

        //display creatures
        List<Creature> creatures = dungeon.creatures;
        for (int i = 0; i < creatures.size(); i += 1) {
            if(creatures.get(i).getClass() == Player.class) {
                Player player = (Player) creatures.get(i);
                //displayGrid.addObjectToDisplay(new Char('@'), player.posX, player.posY);
                displayGrid.addObjectToDisplay(new Char(player.type), player.posX, player.posY);
            }
            else {
                Monster monster = (Monster) creatures.get(i);
                displayGrid.addObjectToDisplay(new Char(monster.type), monster.posX, monster.posY);
            }
        }

        //Words
        displayGrid.info("Pack:", 0, height - 3);
        displayGrid.info("Info: ", 0, height - 1);
        displayGrid.info("    ", 4, 0);
        displayGrid.info("HP: ", 0, 0);
        displayGrid.info("Score: ", 10, 0);
        displayGrid.info(Integer.toString(displayGrid.score), 17, 0);

        for (int i = 0; i < creatures.size(); i += 1) {
            if(creatures.get(i).getClass() == Player.class) {
                Player player = (Player) creatures.get(i);
                displayGrid.info(Integer.toString(player.hp), 4, 0);
            }
        }
    }

    public static void main(String[] args) throws Exception {

        String fileName = null;
        switch (args.length) {
        case 1:
            fileName = "xmlfiles/" + args[0];
           break;
        default:
           System.out.println("java Test <xmlfilename>");
	   return;
        }
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            handler = new DungeonXMLHandler();
            saxParser.parse(new File(fileName), handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace(System.out);
        }

        //readjust 
        dungeon = handler.getDungeon();
        List<Creature> creatures = dungeon.creatures;
        for (int i = 0; i < creatures.size(); i += 1) {
            if(creatures.get(i).getClass() == Player.class) {
                Player player = (Player)creatures.get(i);
                player.setPosX(dungeon.rooms.get(player.room - 1).posX + player.posX);
                player.setPosY(dungeon.rooms.get(player.room - 1).posY + player.posY + dungeon.topHeight);
            }
            else {
                Monster monster = (Monster)creatures.get(i);
                monster.setPosX(dungeon.rooms.get(monster.room - 1).posX + monster.posX);
                monster.setPosY(dungeon.rooms.get(monster.room - 1).posY + monster.posY + dungeon.topHeight);
            }
        }
        List<Item> items = dungeon.items;
        for (int i = 0; i < items.size(); i += 1) {
            items.get(i).setPosY(items.get(i).posY + dungeon.topHeight);
            if(items.get(i).getClass() == Sword.class) {
                items.get(i).setType('|');
            }
            else if(items.get(i).getClass() == Armor.class) {
                items.get(i).setType(']');
            }
            else if(items.get(i).getClass() == Scroll.class) {
                items.get(i).setType('?');
            }
        }

        Rogue test = new Rogue();
        Thread testThread = new Thread(test);
        testThread.start();

        test.keyStrokePrinter = new Thread(new KeyStrokePrinter(displayGrid));
        test.keyStrokePrinter.start();

        testThread.join();
        test.keyStrokePrinter.join();
    }
}
