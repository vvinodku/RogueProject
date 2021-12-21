package parser;

import java.util.ArrayList;
import java.util.List;

public class Creature extends Displayable{

    public int hp;
    public int hpMoves;
    public CreatureAction deathAction;
    public CreatureAction hitAction;
    public List<CreatureAction> actions = new ArrayList<CreatureAction>();

    public Creature() {
    }

    public void setHp(int _hp) {
        hp = _hp;
    }

    public void setHpMoves(int _hpMoves) {
        hpMoves = _hpMoves;
    }

    public void setDeathAction(CreatureAction _deathAction) {
        deathAction = _deathAction;
    }

    public void setHitAction(CreatureAction _hitAction) {
        hitAction = _hitAction;
    }

    public void addAction(CreatureAction _action) {
        actions.add(_action);
    }
}
