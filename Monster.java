public final class Monster {
    private final String name;
    private final int level;
    private final int attack;
    private final int health;
    private final String requiredItem;
    private final int goldReward;
    private final int experienceReward;

    public Monster(String name, int level, int attack, int health,
                   String requiredItem, int goldReward,
                   int experienceReward) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Название монстра не может быть пустым");
        }

        if (level <= 0 ||
                health <= 0 ||
                attack < 0 ||
                goldReward < 0 ||
                experienceReward < 0) {

            throw new IllegalArgumentException(
                    "Недопустимые параметры монстра");
        }

        this.name = name.trim();
        this.level = level;
        this.attack = attack;
        this.health = health;

        this.requiredItem =
                requiredItem == null ||
                        requiredItem.trim().isEmpty()
                        ? null
                        : requiredItem.trim();

        this.goldReward = goldReward;
        this.experienceReward = experienceReward;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    public String getRequiredItem() {
        return requiredItem;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    @Override
    public String toString() {
        return name +
                " | уровень: " + level +
                " | атака: " + attack +
                " | здоровье: " + health +
                " | обязательный предмет: " +
                (requiredItem == null ? "нет" : requiredItem) +
                " | награда: " + goldReward +
                " золота, " + experienceReward + " опыта";
    }
}
