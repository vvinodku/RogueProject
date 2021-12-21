package game;

import asciiPanel.AsciiPanel;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import parser.*;

public class ObjectDisplayGrid extends JFrame implements KeyListener, InputSubject {

    private static final int DEBUG = 0;
    private static final String CLASSID = ".ObjectDisplayGrid";

    private static AsciiPanel terminal;
    private Char[][] objectGrid = null;

    private List<InputObserver> inputObservers = null;

    private static int height;
    private static int width;

    private Player player;
    private Dungeon dungeon;
    public char oldKey = 'o';
    public int hallucinate = 0;
    public int score = 0;
    public int movesLeft;
    public boolean alive = true;
    public boolean toggle = false;
    public List<Item> pack = new ArrayList<Item>();

    public ObjectDisplayGrid(int _width, int _height, Dungeon _dungeon) {
        width = _width;
        height = _height;
        dungeon = _dungeon;

        terminal = new AsciiPanel(width, height);

        objectGrid = new Char[width][height];

        initializeDisplay();

        super.add(terminal);
        super.setSize(width * 9, height * 16);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //super.repaint();
        //terminal.repaint( );
        super.setVisible(true);
        super.addKeyListener(this);
        terminal.setVisible(true);
        terminal.addKeyListener(this);
        inputObservers = new ArrayList<>();
        super.repaint();

        for(int i = 0; i < dungeon.creatures.size(); i++) {
            if(dungeon.creatures.get(i).getClass() == Player.class) {
                player = (Player) dungeon.creatures.get(i);
            }
        }

        movesLeft = player.hpMoves;

        createPack();
    }

    @Override
    public void registerInputObserver(InputObserver observer) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".registerInputObserver " + observer.toString());
        }
        inputObservers.add(observer);
    }

    public void createPack() {
        if(player.armor != null) {
            pack.add(player.armor); 
            player.armor = null;
        }
        if(player.sword != null) {
            pack.add(player.sword); 
            player.sword = null;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".keyTyped entered" + e.toString());
        }
        KeyEvent keypress = (KeyEvent) e;
        notifyInputObservers(keypress.getKeyChar());
        char key = keypress.getKeyChar();
        this.info(" ", 6, height - 1);
        if(alive) {
            if(hallucinate > 0) {
                hallucinate();
                hallucinate -= 1;
                if(key == 'h') {
                    if(checkMoveable(player.posX - 1, player.posY)) {
                        this.addObjectToDisplay(new Char('.'), player.posX, player.posY);
                        player.setPosX(player.posX - 1);
                        movesLeft -= 1;
                    }
                }
                else if(key == 'l') {
                    if(checkMoveable(player.posX + 1, player.posY)) {
                        this.addObjectToDisplay(new Char('.'), player.posX, player.posY);
                        player.setPosX(player.posX + 1);
                        movesLeft -= 1;
                    }        
                }
                else if(key == 'k') {
                    if(checkMoveable(player.posX, player.posY - 1)) {
                        this.addObjectToDisplay(new Char('.'), player.posX, player.posY);
                        player.setPosY(player.posY - 1);
                        movesLeft -= 1;
                    }        
                }
                else if(key == 'j' && checkMoveable(player.posX, player.posY + 1)) {
                        this.addObjectToDisplay(new Char('.'), player.posX, player.posY);
                        player.setPosY(player.posY + 1);      
                        movesLeft -= 1;
                }
                this.addObjectToDisplay(new Char(player.type), player.posX, player.posY);
            }
            else if(oldKey == 'o') {
                if(key == 'h') {
                    if(checkMoveable(player.posX - 1, player.posY)) {
                        player.setPosX(player.posX - 1);
                        movesLeft -= 1;
                    }
                }
                else if(key == 'l') {
                    if(checkMoveable(player.posX + 1, player.posY)) {
                        player.setPosX(player.posX + 1);
                        movesLeft -= 1;
                    }        
                }
                else if(key == 'k') {
                    if(checkMoveable(player.posX, player.posY - 1)) {
                        player.setPosY(player.posY - 1);
                        movesLeft -= 1;
                    }        
                }
                else if(key == 'j' && checkMoveable(player.posX, player.posY + 1)) {
                        player.setPosY(player.posY + 1);      
                        movesLeft -= 1;
                }
                else if(key == 'p') {
                    pickUp(player.posX, player.posY);
                }
                else if(key == 'i') {
                    toggle = !toggle;
                    togglePack();
                }
                else if(key == 'c') {
                    takeOff();
                }
                else if(key == '?') {
                    this.info("h,l,k,j,i,?,H,c,d,p,R,T,w,E,0-9. H <cmd> for more info", 6, height - 1);
                }
                //these actions require additional keypress
                else if(key == 'r') {
                    oldKey = 'r';
                    info("Choose scroll to read", 6, height - 1);
                }
                else if(key == 'd') {
                    oldKey = 'd';
                    info("Choose item to drop", 6, height - 1);
                }
                else if(key == 't') {
                    oldKey = 't';
                    info("Choose weapon to wield", 6, height - 1);
                }
                else if(key == 'w') {
                    oldKey = 'w';
                    info("Choose armor to wear", 6, height - 1);
                }
                else if(key == 'E') {
                    oldKey = 'w';
                    info("y or Y to end game...", 6, height - 1);
                }
                else if(key == 'H') {
                    oldKey = 'H';
                    info("y or Y to end game...", 6, height - 1);
                }
            }
            //drop item
            else if(oldKey == 'd') {
                drop(Character.getNumericValue(key), player.posX, player.posY);
                oldKey = 'o';
            }
            //wear armor
            else if(oldKey == 'w') {
                wear(Character.getNumericValue(key));
                oldKey = 'o';
            }
            //take out weapon
            else if(oldKey == 't') {
                wield(Character.getNumericValue(key));
                oldKey = 'o';
            }
            //read scroll
            else if(oldKey == 'r') {
                read(Character.getNumericValue(key));
                oldKey = 'o';
            }
            else if(oldKey == 'E') {
                if(key == 'y' || key == 'Y') {
                    //System.exit(0);
                    alive = false;
                }
            }
            else if(oldKey == 'H') {
                if(key == 'k') { 
                    info("k: move up", 6, height - 1); 
                }
                else if(key == 'j') { 
                    info("j: move down", 6, height - 1); 
                }
                else if(key == 'h') { 
                    info("h: move left", 6, height - 1); 
                }
                else if(key == 'l') { 
                    info("l: move right", 6, height - 1); 
                }
                else if(key == 'i') { 
                    info("i: toggle pack", 6, height - 1);
                }
                else if(key == 'c') { 
                    info("c: take off armor", 6, height - 1); 
                }
                else if(key == 'd') { 
                    info("d: drop item from pack", 6, height - 1); 
                }
                else if(key == 'p') { 
                    info("p: pick up item", 6, height - 1); 
                }
                else if(key == 'r') { 
                    info("r: read scroll", 6, height - 1); 
                }
                else if(key == 'w') { 
                    info("w: wear armor", 6, height - 1); 
                }
                else if(key == 't') { 
                    info("t: wield weapon", 6, height - 1); 
                }
            }
        }
        else {
            if(key == 'E') {
                oldKey = 'w';
                info("y or Y to end game...", 6, height - 1);
            }
            else if(oldKey == 'E') {
                if(key == 'y' || key == 'Y') {
                    System.exit(0);
                }
            }
        }

        if(movesLeft == 0) {
            player.hp = player.hp + 1;
            movesLeft = player.hpMoves;
        }

        if(hallucinate == 0) Rogue.display();
    }

    private void hallucinate() {
        char[] characters = {'T', 'H', 'S', '#', 'X', '|', ']', '?'};
        Random randChar = new Random();
        for(int i = 0; i < dungeon.creatures.size(); i ++) {
            if(dungeon.creatures.get(i).getClass() == Monster.class) {
                this.addObjectToDisplay(new Char(characters[randChar.nextInt(7)]), dungeon.creatures.get(i).posX, dungeon.creatures.get(i).posY);
            }
        }
        // for(int h = 0; h < dungeon.items.size(); h ++) {
        //     this.addObjectToDisplay(new Char(characters[randChar.nextInt(7)]), dungeon.items.get(h).posX, dungeon.items.get(h).posY);
        // }
    }

    
    private boolean checkMoveable(int x, int y) {
        if(objectGrid[x][y].getChar() == 'X' || objectGrid[x][y].getChar() == ' ') {
            return false;
        }
        else if(objectGrid[x][y].getChar() == 'T' || objectGrid[x][y].getChar() == 'S' || objectGrid[x][y].getChar() == 'H') {
            for(int i = 0; i < dungeon.creatures.size(); i++) {
                if(dungeon.creatures.get(i).posX == x && dungeon.creatures.get(i).posY == y) {
                    attack((Monster)dungeon.creatures.get(i));
                    return false;
                }
            }
        }
        return true;
    }

    private void pickUp(int x, int y) {
        for(int i = 0; i < dungeon.items.size(); i++) {
            if(dungeon.items.get(i).posX == x && dungeon.items.get(i).posY == y && inPack(dungeon.items.get(i))) {
                pack.add(dungeon.items.get(i));
                dungeon.items.get(i).setType('.');
                dungeon.items.remove(i);
                toggle = true;
                togglePack();
                return;
            }
        }        
    }

    private boolean inPack(Item item) {
        for(int i = 0; i < pack.size(); i ++) {
            if(item == pack.get(i)) {
                return false;
            }
        }
        return true;
    }

    private void drop(int idx, int x, int y) {
        if(idx <= pack.size() && idx > 0) {
            Item item = pack.get(idx - 1);
            pack.remove(idx - 1);
            item.posX = x;
            item.posY = y;
            dungeon.items.add(item);
            if(item.getClass() == Sword.class) {
                item.setType('|');
                info(" ", 6, height - 1);
                info("You dropped the sword", 6, height - 1);
            }
            else if(item.getClass() == Armor.class) {
                item.setType(']');
                info(" ", 6, height - 1);
                info("You dropped the armor", 6, height - 1);
            }
            else if(item.getClass() == Scroll.class) {
                item.setType('?');
                info(" ", 6, height - 1);
                info("You dropped the scroll", 6, height - 1);
            }
            toggle = true;
            togglePack();
        } 
        else if(idx > pack.size()) {
            info(" ", 6, height - 1);
            info("There isn't anything in that pack slot...", 6, height - 1);
            return;
        }
    }

    private void wear(int idx) {
        if(player.armor != null) {
            info(" ", 6, height - 1);
            info("You are already wearing armor, take it off first..." + pack.get(idx - 1).name, 6, height - 1);
        }
        else if((idx <= pack.size()) && (pack.get(idx - 1).getClass() == Armor.class)) {
            //don't forget to notify user of this
            player.setArmor((Armor)pack.get(idx - 1));
            info(" ", 6, height - 1);
            info("Wearing armor: " + pack.get(idx - 1).name, 6, height - 1);
        }
        else if(idx > pack.size()) {
            info(" ", 6, height - 1);
            info("There isn't anything in that pack slot...", 6, height - 1);
        }
        else if((pack.get(idx - 1).getClass() != Armor.class)){
            info(" ", 6, height - 1);
            info("You can't wear that...", 6, height - 1);
        }
        toggle = true;
        togglePack();
        return;
    }

    private void takeOff() {
        if(player.armor != null) {
            player.armor = null;
            info(" ", 6, height - 1);
            info("You took off the armor", 6, height - 1);
        }
        else {
            info(" ", 6, height - 1);
            info("You aren't wearing anything", 6, height - 1);
        }
        toggle = true;
        togglePack();
    }

    private void wield(int idx) {
        if(player.sword != null) {
            info(" ", 6, height - 1);
            info("You are already wielding a sword, take it off first", 6, height - 1);
        }
        else if((idx <= pack.size()) && (pack.get(idx - 1).getClass() == Sword.class)) {
            player.setWeapon(pack.get(idx - 1));
            //don't forget to notify user of this
            player.setWeapon((Sword)pack.get(idx - 1));
            info(" ", 6, height - 1);
            info("Wielding sword: " + pack.get(idx - 1).name, 6, height - 1);
        }
        else if(idx > pack.size()) {
            info(" ", 6, height - 1);
            info("There isn't anything in that pack slot...", 6, height - 1);
        }
        else if((pack.get(idx - 1).getClass() != Sword.class)){
            info(" ", 6, height - 1);
            info("You can't wield that...", 6, height - 1);
        }
        toggle = true;
        togglePack();
        return; 
    }

    private void read(int idx) {
        if((idx <= pack.size()) && (pack.get(idx - 1).getClass() == Scroll.class)) {
            Scroll scroll = (Scroll)pack.get(idx - 1);
            if(scroll.action.name.equals("BlessArmor")) {
                if(player.armor != null) {
                    Armor armor = (Armor)player.armor;
                    armor.setIntValue(armor.intValue + scroll.action.intValue);
                    info(" ", 6, height - 1);
                    info(scroll.action.message, 6, height - 1);
                    toggle = true;
                    togglePack();
                }   
            } 
            else if(scroll.action.name.equals("Hallucinate")) {
                hallucinate = scroll.action.intValue;
                info(" ", 6, height - 1);
                info(scroll.action.message, 6, height - 1);
            }
            return;    
        }
        else if(idx > pack.size()){
            info(" ", 6, height - 1);
            info("There isn't anything in that pack slot...", 6, height - 3);
        }
        else if((pack.get(idx - 1).getClass() != Scroll.class)){
            info(" ", 6, height - 1);
            info("You can't read that...", 6, height - 3);
        }
    }

    private void attack(Monster monster) {
        Random randattack = new Random();
        int playerAttack = randattack.nextInt(player.maxHit + 1);
        if(player.sword != null) {
            playerAttack += player.sword.intValue;
        }
        monster.setHp(monster.hp - playerAttack);
        if(monster.hp <= 0) {
            kill(monster);
            //return;
        }
        int monsterAttack = randattack.nextInt(monster.maxHit + 1);
        if(player.armor != null) {
            if(monsterAttack > player.armor.intValue) {
                player.setHp(player.hp - monsterAttack + player.armor.intValue);
            }
        }
        else {
            player.setHp(player.hp - monsterAttack);
        }
        if(player.hp <= 0) {
            die(player);
            return;
        }

        info(" ", 6, height - 1);
        this.info("Player damaged by " + monsterAttack + " " + monster.name + " damaged by " + playerAttack, 6, height - 1);

        for(int h = 0; h < player.actions.size(); h ++) {
            if(player.actions.get(h).type.equals("hit") && player.actions.get(h).name.equals("DropPack")) {
                if(pack.size() > 0) {
                    drop(pack.size() - 1, player.posX, player.posY);
                    info(" ", 6, height - 1);
                    this.info(player.actions.get(h).message, 6, height - 1);
                }
            }
        }

        for(int i = 0; i < monster.actions.size(); i ++) {
            if(monster.actions.get(i).type.equals("hit") && monster.actions.get(i).name.equals("Teleport")) {
                Random rand = new Random();
                int tempx = 0;
                int tempy = 0;
                while(objectGrid[tempx][tempy].getChar() != '.') {
                    tempx = rand.nextInt(width);
                    tempy = rand.nextInt(height);
                }
                monster.setPosX(tempx);
                monster.setPosY(tempy);
                info(" ", 6, height - 1);
                this.info(monster.actions.get(i).message, 6, height - 1);
            }
        }
    } 

    private void kill(Monster monster) {
        for(int i = 0; i < monster.actions.size(); i ++) {
            if(monster.actions.get(i).type.equals("death") && monster.actions.get(i).name.equals("Remove")) {
                monster.setType('.');
            }
            else if(monster.actions.get(i).type.equals("death") && monster.actions.get(i).name.equals("YouWin")) {
                info(" ", 6, height - 1);
                info(monster.actions.get(i).message, 6, height - 1);
                score += score + monster.actions.get(i).intValue;
            }
        }
    }

    private void die(Player player) {
        player.hp = 0;
        for(int i = 0; i < player.actions.size(); i ++) {
            if(player.actions.get(i).type.equals("death") && player.actions.get(i).name.equals("Remove")) {
                player.setType('.');
            }
            else if(player.actions.get(i).type.equals("death") && player.actions.get(i).name.equals("YouWin")) {
                info(" ", 6, height - 1);
                info(player.actions.get(i).message, 6, height - 1);
                alive = false;
                //System.exit(0);
            }
            else if(player.actions.get(i).type.equals("death") && player.actions.get(i).name.equals("EndGame")) {
                info(" ", 6, height - 1);
                info(player.actions.get(i).message, 6, height - 1);
                alive = false;
                //System.exit(0);
            }
            else if(player.actions.get(i).type.equals("death") && player.actions.get(i).name.equals("ChangeDisplayedType")) {
                player.setType('+');
            }
        }
    }

    private void togglePack() {
        if(toggle == true) {
            if(pack.size() == 0) { 
                info("Your pack is empty...", 6, height - 3); 
            } 
            else {
                info(" ", 6, height - 3); 
                for(int i = 0; i < pack.size(); i ++) {
                    if(pack.get(i).getClass() != Scroll.class) {
                        if(pack.get(i).getClass() == Armor.class && player.armor == pack.get(i)) {
                            info(Integer.toString(i + 1) + ":(w) " + Integer.toString(pack.get(i).intValue) + "pts ARM ", 6 + i * 15, height - 3);
                        }
                        else if(pack.get(i).getClass() == Sword.class && player.sword == pack.get(i)) {
                            info(Integer.toString(i + 1) + ":(w) " + Integer.toString(pack.get(i).intValue) + "pts SW ", 6 + i * 15, height - 3);
                        }   
                        else if(pack.get(i).getClass() == Armor.class) {
                            info(Integer.toString(i + 1) + ": " + Integer.toString(pack.get(i).intValue) + "pts ARM ", 6 + i * 15, height - 3);
                        }
                        else if(pack.get(i).getClass() == Sword.class) {
                            info(Integer.toString(i + 1) + ": " + Integer.toString(pack.get(i).intValue) + "pts SW ", 6 + i * 15, height - 3);
                        }
                    } 
                    else {
                        info(Integer.toString(i + 1) + ":" + pack.get(i).name, 6 + i * 16, height - 3);
                    }
                }
            }
        }
        else {
            info(" ", 6, height - 3);
        }
    }


    public void info(String s, int x, int y) {
        if(s.equals(" ")) {
            for(int x2 = 6; x2 < width; x2 ++) {
                addObjectToDisplay(new Char(' '), x2, y); 
            }
        }
        for(int i = 0; i < s.length(); i ++) { 
            addObjectToDisplay(new Char(s.charAt(i)), x + i, y); 
        }
    }

    private void notifyInputObservers(char ch) {
        for (InputObserver observer : inputObservers) {
            observer.observerUpdate(ch);
            if (DEBUG > 0) {
                System.out.println(CLASSID + ".notifyInputObserver " + ch);
            }
        }
    }

    // we have to override, but we don't use this
    @Override
    public void keyPressed(KeyEvent even) {
    }

    // we have to override, but we don't use this
    @Override
    public void keyReleased(KeyEvent e) {
    }

    public final void initializeDisplay() {
        Char ch = new Char(' ');
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                addObjectToDisplay(ch, i, j);
            }
        }
        terminal.repaint();
    }

    public void fireUp() {
        if (this.requestFocusInWindow()) {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow Succeeded");
        } else {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow FAILED");
        }
    }

    public void addObjectToDisplay(Char ch, int x, int y) {
        if ((0 <= x) && (x < objectGrid.length)) {
            if ((0 <= y) && (y < objectGrid[0].length)) {
                objectGrid[x][y] = ch;
                writeToTerminal(x, y);
            }
        }
    }

    private void writeToTerminal(int x, int y) {
        char ch = objectGrid[x][y].getChar();
        terminal.write(ch, x, y);
        terminal.repaint();
    }
}
