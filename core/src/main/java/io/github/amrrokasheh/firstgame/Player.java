package io.github.amrrokasheh.firstgame;


import com.badlogic.gdx.Gdx;

public class Player {
    public enum UpgradeType {
        HEALTH,
        SPEED,
        ATTACK_SPEED
    }
    private int health = 100;
    private float speed = 200f;
    private float attackSpeed = 1.0f;
    private int upgradePoints = 0;


    // --- Upgrade Points Management ---
    public int getUpgradePoints() {
        return upgradePoints;
    }

    public void addUpgradePoints(int points) {
        if (points > 0) {
            upgradePoints += points;
        }
    }

    private boolean spendUpgradePoints(int cost) {
        if (cost > 0 && upgradePoints >= cost) {
            upgradePoints -= cost;
            return true;
        }
        return false;
    }

    // --- Unified upgrade system ---
    public boolean upgrade(UpgradeType type, float amount, int cost) {
        if (!spendUpgradePoints(cost)) {
            return false; // not enough points
        }

        switch (type) {
            case HEALTH:
                health += (int) amount;
                break;
            case SPEED:
                speed += amount;
                Gdx.app.log("Upgrade", "New Speed = " + speed);
                break;

            case ATTACK_SPEED:
                attackSpeed = Math.max(0.1f, attackSpeed + amount);
                break;
        }
        return true;
    }

    // --- Getters ---
    public int getHealth() { return health; }
    public float getSpeed() { return speed; }
    public float getAttackSpeed() { return attackSpeed; }
}
