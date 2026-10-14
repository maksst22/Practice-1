import java.util.ArrayList;
import java.util.List;

public class Hero {
    private final String name;
    private final String heroClass;
    private int level;
    private int experience;
    private int health;
    private int maxHealth;
    private int gold;
    private final List<Item> inventory;

    public Hero(String name, String heroClass) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }

        if (heroClass == null ||
                (!heroClass.equals("воин") &&
                        !heroClass.equals("маг") &&
                        !heroClass.equals("лучник"))) {
            throw new IllegalArgumentException(
                    "Недопустимый класс героя");
        }

        this.name = name.trim();
        this.heroClass = heroClass;
        this.level = 1;
        this.experience = 0;
        this.health = 100;
        this.maxHealth = 100;
        this.gold = 500;
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getHeroClass() {
        return heroClass;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getGold() {
        return gold;
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public int getPower() {
        int power = level * 10;

        for (Item item : inventory) {
            power += item.getPower();
        }

        return power;
    }

    public String getTitle() {
        if (experience <= 200) {
            return "Новичок";
        }

        if (experience <= 400) {
            return "Герой Арены";
        }

        return "Легенда";
    }

    public boolean hasItem(String itemName) {
        if (itemName == null) {
            return false;
        }

        for (Item item : inventory) {
            if (item.getName().trim()
                    .equalsIgnoreCase(itemName.trim())) {
                return true;
            }
        }

        return false;
    }

    public boolean canBuy(Item item) {
        if (item == null) {
            return false;
        }

        if (gold < item.getPrice()) {
            return false;
        }

        if (item.getType().equals("оружие") ||
                item.getType().equals("защита")) {

            return !hasItem(item.getName());
        }

        return true;
    }

    public boolean buy(Item item) {
        if (!canBuy(item)) {
            return false;
        }

        gold -= item.getPrice();
        inventory.add(item);

        return true;
    }

    public int sell(int index) {
        if (index < 0 || index >= inventory.size()) {
            return -1;
        }

        Item item = inventory.remove(index);

        int money = item.getPrice() / 2;
        gold += money;

        return money;
    }

    public boolean useHealingPotion() {
        if (health <= 0 || health >= maxHealth) {
            return false;
        }

        if (3 * health >= maxHealth) {
            return false;
        }

        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);

            if (item.isConsumable() &&
                    item.getName().equalsIgnoreCase("Зелье лечения")) {

                if (item.applyEffect(this)) {
                    inventory.remove(i);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean useConsumable(int index) {
        if (index < 0 || index >= inventory.size()) {
            return false;
        }

        Item item = inventory.get(index);

        if (!item.isConsumable()) {
            return false;
        }

        if (!item.applyEffect(this)) {
            return false;
        }

        inventory.remove(index);
        return true;
    }

    boolean applyHealingPotionEffect() {
        if (health <= 0 || health >= maxHealth) {
            return false;
        }

        health = Math.min(maxHealth, health + 50);
        return true;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException(
                    "Урон не может быть отрицательным");
        }

        health = Math.max(0, health - damage);
    }

    public void addReward(int goldReward, int experienceReward) {
        if (goldReward < 0 || experienceReward < 0) {
            throw new IllegalArgumentException(
                    "Награды не могут быть отрицательными");
        }

        gold += goldReward;
        experience += experienceReward;

        int newLevel = 1 + experience / 100;

        if (newLevel > level) {
            int levelsGained = newLevel - level;

            level = newLevel;
            maxHealth = 100 + 20 * (level - 1);

            health += levelsGained * 20;

            if (health > maxHealth) {
                health = maxHealth;
            }
        }
    }

    void restoreState(
            int level,
            int experience,
            int health,
            int maxHealth,
            int gold,
            List<Item> loadedInventory) {

        if (level <= 0) {
            throw new IllegalArgumentException("Некорректный уровень");
        }

        if (experience < 0) {
            throw new IllegalArgumentException("Некорректный опыт");
        }

        if (gold < 0) {
            throw new IllegalArgumentException("Некорректное золото");
        }

        int expectedLevel = 1 + experience / 100;

        if (level != expectedLevel) {
            throw new IllegalArgumentException(
                    "Уровень не соответствует опыту");
        }

        int expectedMaxHealth = 100 + 20 * (level - 1);

        if (maxHealth != expectedMaxHealth) {
            throw new IllegalArgumentException(
                    "Максимальное здоровье не соответствует уровню");
        }

        if (health < 0 || health > maxHealth) {
            throw new IllegalArgumentException(
                    "Некорректное здоровье");
        }

        if (loadedInventory == null) {
            throw new IllegalArgumentException(
                    "Инвентарь не может быть null");
        }

        for (Item item : loadedInventory) {
            if (item == null) {
                throw new IllegalArgumentException(
                        "Инвентарь содержит null");
            }
        }

        for (int i = 0; i < loadedInventory.size(); i++) {
            Item a = loadedInventory.get(i);

            if (!a.getType().equals("оружие") &&
                    !a.getType().equals("защита")) {
                continue;
            }

            for (int j = i + 1; j < loadedInventory.size(); j++) {
                Item b = loadedInventory.get(j);

                if (a.getType().equals(b.getType()) &&
                        a.getName().equalsIgnoreCase(b.getName())) {
                    throw new IllegalArgumentException(
                            "В сохранении есть повторное оружие или защита");
                }
            }
        }

        this.level = level;
        this.experience = experience;
        this.health = health;
        this.maxHealth = maxHealth;
        this.gold = gold;

        inventory.clear();
        inventory.addAll(loadedInventory);
    }

    @Override
    public String toString() {
        return "Имя: " + name +
                "\nКласс: " + heroClass +
                "\nУровень: " + level +
                "\nОпыт: " + experience +
                "\nЗдоровье: " + health + "/" + maxHealth +
                "\nЗолото: " + gold +
                "\nСила: " + getPower() +
                "\nТитул: " + getTitle();
    }
}
