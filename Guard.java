public class Guard {

    public String check(
            Hero hero,
            Monster monster,
            int predictedDamage) {

        if (hero.getHealth() <= 0) {
            return "Герой мёртв";
        }

        if (hero.getLevel() < monster.getLevel()) {
            return "Уровень героя недостаточен";
        }

        String requiredItem = monster.getRequiredItem();

        if (requiredItem != null &&
                !hero.hasItem(requiredItem)) {

            return "Нет обязательного предмета: " + requiredItem;
        }

        if (hero.getHealth() <= predictedDamage) {
            return "Герой не переживёт ожидаемый урон";
        }

        return null;
    }
}
