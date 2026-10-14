public final class Item {
    @FunctionalInterface
    public interface ConsumableEffect {
        boolean apply(Hero hero);
    }

    private final String name;
    private final String type;
    private final int power;
    private final int price;
    private final ConsumableEffect effect;

    public Item(String name, String type, int power, int price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Название предмета не может быть пустым");
        }

        if (type == null ||
                (!type.equals("оружие") &&
                        !type.equals("защита") &&
                        !type.equals("расходник"))) {
            throw new IllegalArgumentException(
                    "Недопустимый тип предмета");
        }

        if (power < 0 || price <= 0) {
            throw new IllegalArgumentException(
                    "Недопустимые параметры предмета");
        }

        if (type.equals("расходник") && power != 0) {
            throw new IllegalArgumentException(
                    "У расходника сила должна быть 0");
        }

        this.name = name.trim();
        this.type = type;
        this.power = power;
        this.price = price;
        this.effect = createEffect();
    }

    private ConsumableEffect createEffect() {
        if (!type.equals("расходник")) {
            return null;
        }
        if (name.equalsIgnoreCase("Зелье лечения")) {
            return Hero::applyHealingPotionEffect;
        }
        return hero -> false;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public int getPrice() {
        return price;
    }

    public boolean isConsumable() {
        return "расходник".equals(type);
    }

    public boolean applyEffect(Hero hero) {
        if (effect == null) {
            return false;
        }

        return effect.apply(hero);
    }

    @Override
    public String toString() {
        return name +
                " | тип: " + type +
                " | сила: " + power +
                " | цена: " + price;
    }
}
