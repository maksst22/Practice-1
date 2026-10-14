public class Battle {

    public int predictDamage(Hero hero, Monster monster) {
        int heroPower = hero.getPower();
        int monsterPower =
                monster.getLevel() * 10 + monster.getAttack();

        int difference = heroPower - monsterPower;

        if (difference > 30) {
            return 0;
        }

        if (difference >= 1) {
            return Math.max(10, monster.getHealth() / 4);
        }

        return Math.max(40, monster.getHealth() / 2);
    }

    public BattleResult fight(Hero hero, Monster monster) {
        int heroPower = hero.getPower();

        int monsterPower =
                monster.getLevel() * 10 + monster.getAttack();

        int difference = heroPower - monsterPower;
        int damage = predictDamage(hero, monster);

        boolean victory = difference > 0;

        hero.takeDamage(damage);

        boolean healed = hero.useHealingPotion();

        int gold = 0;
        int experience = 0;

        if (victory) {
            gold = monster.getGoldReward();
            experience = monster.getExperienceReward();

            hero.addReward(gold, experience);
        }

        String description =
                "Сила героя: " + heroPower +
                        ", сила монстра: " + monsterPower +
                        ", разница: " + difference +
                        ", урон: " + damage +
                        ", лечение: " + (healed ? "да" : "нет") +
                        ", здоровье монстра после боя: " +
                        (victory ? 0 : monster.getHealth());

        return new BattleResult(
                monster.getName(),
                victory,
                experience,
                gold,
                description
        );
    }
}
