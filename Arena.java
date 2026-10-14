import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Arena {
    private static final String SESSION_FILE = "arena-session.properties";
    private static final String SHOP_FILE = "shop.properties";
    private static final String MONSTERS_FILE = "monsters.properties";

    private final Hero hero;
    private final List<Monster> monsters;
    private final List<Item> shop;
    private final List<BattleResult> history;
    private final Guard guard;
    private final Battle battle;
    private final Scanner scanner;

    public Arena() {
        scanner = new Scanner(System.in);

        System.out.print("Введите имя героя: ");
        String name = scanner.nextLine().trim();

        String heroClass = readHeroClass();

        hero = new Hero(name, heroClass);

        monsters = new ArrayList<>();
        shop = new ArrayList<>();
        history = new ArrayList<>();

        guard = new Guard();
        battle = new Battle();

        initializeShop();
        initializeMonsters();
    }

    private String readHeroClass() {
        while (true) {
            System.out.println("Выберите класс:");
            System.out.println("1. воин");
            System.out.println("2. маг");
            System.out.println("3. лучник");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return "воин";
                case "2":
                    return "маг";
                case "3":
                    return "лучник";
                default:
                    System.out.println("Ошибка ввода.");
            }
        }
    }

    private void initializeShop() {
        shop.add(new Item(
                "Зелье лечения",
                "расходник",
                0,
                100
        ));

        shop.add(new Item(
                "Щит",
                "защита",
                15,
                150
        ));

        shop.add(new Item(
                "Противоядие",
                "расходник",
                0,
                200
        ));

        shop.add(new Item(
                "Серебряный меч",
                "оружие",
                35,
                300
        ));
    }

    private void initializeMonsters() {
        monsters.add(new Monster(
                "Пещерная крыса",
                1,
                0,
                50,
                null,
                50,
                50
        ));

        monsters.add(new Monster(
                "Разбойник",
                2,
                10,
                70,
                null,
                80,
                70
        ));

        monsters.add(new Monster(
                "Ядовитый паук",
                2,
                20,
                60,
                "Противоядие",
                70,
                80
        ));

        monsters.add(new Monster(
                "Оборотень",
                3,
                30,
                100,
                "Серебряный меч",
                100,
                80
        ));

        monsters.add(new Monster(
                "Каменный голем",
                4,
                35,
                140,
                "Щит",
                180,
                150
        ));
    }

    public static void main(String[] args) {
        Arena arena = new Arena();
        arena.run();
    }

    public void run() {
        while (true) {
            printMenu();

            String command;

            try {
                command = scanner.nextLine().trim();
            } catch (Exception e) {
                break;
            }

            switch (command) {
                case "1":
                    showHero();
                    break;

                case "2":
                    showInventory();
                    break;

                case "3":
                    shopMenu();
                    break;

                case "4":
                    showAllMonsters();
                    break;

                case "5":
                    showAvailableMonsters();
                    break;

                case "6":
                    searchMonster();
                    break;

                case "7":
                    sortMonsters();
                    break;

                case "8":
                    startBattle();
                    break;

                case "9":
                    showHistory();
                    break;

                case "10":
                    startSeries();
                    break;

                case "11":
                    saveSession();
                    break;

                case "12":
                    loadSession();
                    break;

                case "13":
                    loadCatalogs();
                    break;

                case "0":
                    System.out.println("Выход из программы.");
                    return;

                default:
                    System.out.println(
                            "Ошибка: неизвестная команда.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n========== АРЕНА ==========");
        System.out.println("1. Информация о герое");
        System.out.println("2. Инвентарь героя");
        System.out.println("3. Лавка");
        System.out.println("4. Полный список противников");
        System.out.println("5. Доступные противники");
        System.out.println("6. Поиск противника");
        System.out.println("7. Сортировка противников");
        System.out.println("8. Начать испытание");
        System.out.println("9. Летопись");
        System.out.println("10. Серия испытаний");
        System.out.println("11. Сохранить сеанс");
        System.out.println("12. Загрузить сеанс");
        System.out.println("13. Загрузить каталоги");
        System.out.println("0. Выход");
        System.out.print("Ваш выбор: ");
    }

    private void showHero() {
        System.out.println("\n--- Герой ---");
        System.out.println(hero);
    }

    private void showInventory() {
        System.out.println("\n--- Инвентарь ---");

        List<Item> inventory = hero.getInventory();

        if (inventory.isEmpty()) {
            System.out.println("Инвентарь пуст.");
            return;
        }

        for (int i = 0; i < inventory.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + inventory.get(i));
        }
    }

    private void shopMenu() {
        while (true) {
            System.out.println("\n--- Лавка ---");
            System.out.println("1. Купить");
            System.out.println("2. Продать");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    buyItem();
                    break;

                case "2":
                    sellItem();
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Ошибка ввода.");
            }
        }
    }

    private void buyItem() {
        if (shop.isEmpty()) {
            System.out.println("Лавка пуста.");
            return;
        }

        System.out.println("\n--- Товары ---");

        for (int i = 0; i < shop.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + shop.get(i));
        }

        System.out.println("0. Отмена");

        int index = readIndex(shop.size());

        if (index == -1) {
            return;
        }

        Item item = shop.get(index);

        if (hero.getGold() < item.getPrice()) {
            System.out.println("Недостаточно золота.");
            return;
        }

        if ((item.getType().equals("оружие") ||
                item.getType().equals("защита"))
                && hero.hasItem(item.getName())) {

            System.out.println("Такой предмет уже есть.");
            return;
        }

        if (hero.buy(item)) {
            System.out.println(
                    "Покупка выполнена: " + item.getName());

            if (hero.useHealingPotion()) {
                System.out.println(
                        "Зелье автоматически применено.");
            }
        }
    }

    private void sellItem() {
        List<Item> inventory = hero.getInventory();

        if (inventory.isEmpty()) {
            System.out.println("Инвентарь пуст.");
            return;
        }

        System.out.println("\n--- Продажа ---");

        for (int i = 0; i < inventory.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + inventory.get(i));
        }

        System.out.println("0. Отмена");

        int index = readIndex(inventory.size());

        if (index == -1) {
            return;
        }

        int money = hero.sell(index);

        if (money >= 0) {
            System.out.println(
                    "Предмет продан. Получено золота: " +
                            money);
        }
    }

    private void showAllMonsters() {
        System.out.println("\n--- Все противники ---");

        if (monsters.isEmpty()) {
            System.out.println(
                    "Список противников пуст.");
            return;
        }

        printMonsters(monsters);
    }

    private void showAvailableMonsters() {
        System.out.println(
                "\n--- Доступные противники ---");

        List<Monster> available = new ArrayList<>();

        for (Monster monster : monsters) {
            if (monster.getLevel() <= hero.getLevel()) {
                available.add(monster);
            }
        }

        if (available.isEmpty()) {
            System.out.println(
                    "Подходящих противников нет.");
            return;
        }

        printMonsters(available);
    }

    private void printMonsters(List<Monster> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + list.get(i));
        }
    }

    private void searchMonster() {
        System.out.print(
                "Введите полное название: ");

        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println(
                    "Название не может быть пустым.");
            return;
        }

        for (Monster monster : monsters) {
            if (monster.getName()
                    .equalsIgnoreCase(name)) {

                System.out.println("Найден:");
                System.out.println(monster);
                return;
            }
        }

        System.out.println("Противник не найден.");
    }

    private void sortMonsters() {
        monsters.sort(
                Comparator.comparingInt(
                        Monster::getLevel));

        System.out.println(
                "Противники отсортированы по уровню.");

        printMonsters(monsters);
    }

    private void startBattle() {
        if (monsters.isEmpty()) {
            System.out.println("Противников нет.");
            return;
        }

        System.out.println(
                "\n--- Выбор противника ---");

        printMonsters(monsters);

        System.out.println("0. Отмена");

        int index = readIndex(monsters.size());

        if (index == -1) {
            return;
        }

        fightOne(monsters.get(index));
    }

    private boolean fightOne(Monster monster) {
        int damage =
                battle.predictDamage(hero, monster);

        String reason =
                guard.check(hero, monster, damage);

        if (reason != null) {
            System.out.println(
                    "Допуск запрещён: " + reason);
            return false;
        }

        System.out.println("Допуск разрешён.");
        System.out.println("Начинается бой...");

        BattleResult result =
                battle.fight(hero, monster);

        history.add(result);

        System.out.println("\n--- Результат боя ---");
        System.out.println(result);

        System.out.println("\nСостояние героя:");
        System.out.println(hero);

        return true;
    }

    private void startSeries() {
        if (monsters.isEmpty()) {
            System.out.println("Противников нет.");
            return;
        }

        System.out.println(
                "\n--- Серия испытаний ---");

        printMonsters(monsters);

        System.out.println(
                "Введите номера противников через пробел.");
        System.out.println(
                "Например: 1 2 1 4");
        System.out.println("0. Отмена");
        System.out.print("Серия: ");

        String input = scanner.nextLine().trim();

        if (input.equals("0")) {
            return;
        }

        if (input.isEmpty()) {
            System.out.println(
                    "Серия не может быть пустой.");
            return;
        }

        String[] parts = input.split("\\s+");
        List<Monster> selected = new ArrayList<>();

        for (String part : parts) {
            try {
                int number = Integer.parseInt(part);

                if (number < 1 ||
                        number > monsters.size()) {

                    System.out.println(
                            "Неверный номер: " + number);
                    return;
                }

                selected.add(
                        monsters.get(number - 1));

            } catch (NumberFormatException e) {
                System.out.println(
                        "Некорректный номер: " + part);
                return;
            }
        }

        System.out.println(
                "\nНачинается серия из " +
                        selected.size() + " испытаний.");

        int battlesBefore = history.size();

        for (int i = 0; i < selected.size(); i++) {
            Monster monster = selected.get(i);

            System.out.println(
                    "\n========== БОЙ " +
                            (i + 1) + "/" +
                            selected.size() + " ==========");

            boolean completed = fightOne(monster);

            if (!completed) {
                System.out.println(
                        "Этот бой серии пропущен.");
            }
        }

        int created =
                history.size() - battlesBefore;

        System.out.println(
                "\nСерия завершена.");

        System.out.println(
                "Записей добавлено в летопись: " +
                        created);
    }

    private void showHistory() {
        System.out.println("\n--- Летопись ---");

        if (history.isEmpty()) {
            System.out.println(
                    "Боёв ещё не было.");
            return;
        }

        int victories = 0;
        int defeats = 0;
        int totalGold = 0;
        int totalExperience = 0;

        for (int i = 0; i < history.size(); i++) {
            BattleResult result = history.get(i);

            System.out.println(
                    (i + 1) + ". " + result);

            if (result.isVictory()) {
                victories++;
            } else {
                defeats++;
            }

            totalGold += result.getGoldGained();
            totalExperience +=
                    result.getExperienceGained();
        }

        System.out.println("\nСтатистика:");
        System.out.println(
                "Всего боёв: " + history.size());
        System.out.println(
                "Побед: " + victories);
        System.out.println(
                "Поражений: " + defeats);
        System.out.println(
                "Получено золота за бои: " +
                        totalGold);
        System.out.println(
                "Получено опыта за бои: " +
                        totalExperience);
    }

    private void saveSession() {
        Properties properties = new Properties();

        properties.setProperty(
                "version", "1");

        properties.setProperty(
                "hero.name", hero.getName());

        properties.setProperty(
                "hero.class", hero.getHeroClass());

        properties.setProperty(
                "hero.level",
                String.valueOf(hero.getLevel()));

        properties.setProperty(
                "hero.experience",
                String.valueOf(hero.getExperience()));

        properties.setProperty(
                "hero.health",
                String.valueOf(hero.getHealth()));

        properties.setProperty(
                "hero.maxHealth",
                String.valueOf(hero.getMaxHealth()));

        properties.setProperty(
                "hero.gold",
                String.valueOf(hero.getGold()));

        List<Item> inventory =
                hero.getInventory();

        properties.setProperty(
                "inventory.count",
                String.valueOf(inventory.size()));

        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);

            String prefix = "inventory." + i + ".";

            properties.setProperty(
                    prefix + "name",
                    item.getName());

            properties.setProperty(
                    prefix + "type",
                    item.getType());

            properties.setProperty(
                    prefix + "power",
                    String.valueOf(item.getPower()));

            properties.setProperty(
                    prefix + "price",
                    String.valueOf(item.getPrice()));
        }

        properties.setProperty(
                "history.count",
                String.valueOf(history.size()));

        for (int i = 0; i < history.size(); i++) {
            BattleResult result = history.get(i);

            String prefix = "history." + i + ".";

            properties.setProperty(
                    prefix + "monster",
                    result.getMonsterName());

            properties.setProperty(
                    prefix + "victory",
                    String.valueOf(
                            result.isVictory()));

            properties.setProperty(
                    prefix + "experience",
                    String.valueOf(
                            result.getExperienceGained()));

            properties.setProperty(
                    prefix + "gold",
                    String.valueOf(
                            result.getGoldGained()));

            properties.setProperty(
                    prefix + "description",
                    result.getDescription());
        }

        try {
            try (var writer = Files.newBufferedWriter(
                    Path.of(SESSION_FILE),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                properties.store(
                        writer,
                        "Arena session");
            }

            System.out.println(
                    "Сеанс сохранён в " +
                            SESSION_FILE);

        } catch (IOException e) {
            System.out.println(
                    "Ошибка сохранения: " +
                            e.getMessage());
        }
    }

    private void loadSession() {
        Path path = Path.of(SESSION_FILE);

        if (!Files.exists(path)) {
            System.out.println(
                    "Файл сохранения не найден.");
            return;
        }

        Properties properties =
                new Properties();

        try {
            try (var reader = Files.newBufferedReader(
                    path,
                    StandardCharsets.UTF_8)) {

                properties.load(reader);
            }

            String version =
                    required(properties, "version");

            if (!version.equals("1")) {
                throw new IllegalArgumentException(
                        "Неподдерживаемая версия сохранения");
            }

            String savedName =
                    required(properties, "hero.name");

            String savedClass =
                    required(properties, "hero.class");

            if (!savedName.equals(hero.getName()) ||
                    !savedClass.equals(hero.getHeroClass())) {

                throw new IllegalArgumentException(
                        "Сохранение относится к другому герою");
            }

            int level =
                    parseNonNegative(
                            properties,
                            "hero.level");

            int experience =
                    parseNonNegative(
                            properties,
                            "hero.experience");

            int health =
                    parseNonNegative(
                            properties,
                            "hero.health");

            int maxHealth =
                    parseNonNegative(
                            properties,
                            "hero.maxHealth");

            int gold =
                    parseNonNegative(
                            properties,
                            "hero.gold");

            List<Item> loadedInventory =
                    loadInventoryFromProperties(
                            properties);

            List<BattleResult> loadedHistory =
                    loadHistoryFromProperties(
                            properties);

            Hero temporaryHero =
                    new Hero(savedName, savedClass);

            temporaryHero.restoreState(
                    level,
                    experience,
                    health,
                    maxHealth,
                    gold,
                    loadedInventory);

            hero.restoreState(
                    temporaryHero.getLevel(),
                    temporaryHero.getExperience(),
                    temporaryHero.getHealth(),
                    temporaryHero.getMaxHealth(),
                    temporaryHero.getGold(),
                    temporaryHero.getInventory());

            history.clear();
            history.addAll(loadedHistory);

            System.out.println(
                    "Сеанс успешно загружен.");

        } catch (IOException |
                 IllegalArgumentException |
                 IndexOutOfBoundsException e) {

            System.out.println(
                    "Загрузка отменена. " +
                            "Текущее состояние не изменено.");

            System.out.println(
                    "Причина: " + e.getMessage());
        }
    }

    private List<Item> loadInventoryFromProperties(
            Properties properties) {

        int count =
                parseCount(
                        properties,
                        "inventory.count");

        List<Item> result =
                new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String prefix =
                    "inventory." + i + ".";

            String name =
                    required(
                            properties,
                            prefix + "name");

            String type =
                    required(
                            properties,
                            prefix + "type");

            int power =
                    parseNonNegative(
                            properties,
                            prefix + "power");

            int price =
                    parsePositive(
                            properties,
                            prefix + "price");

            result.add(
                    new Item(
                            name,
                            type,
                            power,
                            price));
        }

        return result;
    }

    private List<BattleResult> loadHistoryFromProperties(
            Properties properties) {

        int count =
                parseCount(
                        properties,
                        "history.count");

        List<BattleResult> result =
                new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String prefix =
                    "history." + i + ".";

            String monster =
                    required(
                            properties,
                            prefix + "monster");

            String victoryText =
                    required(
                            properties,
                            prefix + "victory");

            boolean victory;

            if (victoryText.equalsIgnoreCase("true")) {
                victory = true;
            } else if (
                    victoryText.equalsIgnoreCase("false")) {
                victory = false;
            } else {
                throw new IllegalArgumentException(
                        "Некорректное значение victory");
            }

            int experience =
                    parseNonNegative(
                            properties,
                            prefix + "experience");

            int gold =
                    parseNonNegative(
                            properties,
                            prefix + "gold");

            String description =
                    required(
                            properties,
                            prefix + "description");

            if (!victory &&
                    (experience != 0 || gold != 0)) {

                throw new IllegalArgumentException(
                        "Поражение содержит награду");
            }

            result.add(
                    new BattleResult(
                            monster,
                            victory,
                            experience,
                            gold,
                            description));
        }

        return result;
    }

    private void loadCatalogs() {
        Path shopPath = Path.of(SHOP_FILE);
        Path monsterPath = Path.of(MONSTERS_FILE);

        if (!Files.exists(shopPath) ||
                !Files.exists(monsterPath)) {

            System.out.println(
                    "Для загрузки нужны оба файла:");
            System.out.println(
                    SHOP_FILE);
            System.out.println(
                    MONSTERS_FILE);
            return;
        }

        try {
            List<Item> newShop =
                    loadShopFile(shopPath);

            List<Monster> newMonsters =
                    loadMonsterFile(monsterPath);

            validateMonsterRequirements(
                    newMonsters,
                    newShop);

            shop.clear();
            shop.addAll(newShop);

            monsters.clear();
            monsters.addAll(newMonsters);

            System.out.println(
                    "Каталоги успешно загружены.");
            System.out.println(
                    "Товаров: " + shop.size());
            System.out.println(
                    "Противников: " + monsters.size());

        } catch (IOException |
                 IllegalArgumentException e) {

            System.out.println(
                    "Загрузка каталогов отменена.");
            System.out.println(
                    "Текущие каталоги не изменены.");
            System.out.println(
                    "Причина: " + e.getMessage());
        }
    }

    private List<Item> loadShopFile(
            Path path) throws IOException {

        Properties properties =
                new Properties();

        try (var reader = Files.newBufferedReader(
                path,
                StandardCharsets.UTF_8)) {

            properties.load(reader);
        }

        int count =
                parseCount(
                        properties,
                        "count");

        List<Item> result =
                new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String prefix = "item." + i + ".";

            String name =
                    required(
                            properties,
                            prefix + "name");

            String type =
                    required(
                            properties,
                            prefix + "type");

            int power =
                    parseNonNegative(
                            properties,
                            prefix + "power");

            int price =
                    parsePositive(
                            properties,
                            prefix + "price");

            Item item =
                    new Item(
                            name,
                            type,
                            power,
                            price);

            ensureUniqueItem(
                    result,
                    item);

            result.add(item);
        }

        return result;
    }

    private List<Monster> loadMonsterFile(
            Path path) throws IOException {

        Properties properties =
                new Properties();

        try (var reader = Files.newBufferedReader(
                path,
                StandardCharsets.UTF_8)) {

            properties.load(reader);
        }

        int count =
                parseCount(
                        properties,
                        "count");

        List<Monster> result =
                new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String prefix =
                    "monster." + i + ".";

            String name =
                    required(
                            properties,
                            prefix + "name");

            int level =
                    parsePositive(
                            properties,
                            prefix + "level");

            int attack =
                    parseNonNegative(
                            properties,
                            prefix + "attack");

            int health =
                    parsePositive(
                            properties,
                            prefix + "health");

            String requiredItem =
                    optional(
                            properties,
                            prefix + "requiredItem");

            int gold =
                    parseNonNegative(
                            properties,
                            prefix + "gold");

            int experience =
                    parseNonNegative(
                            properties,
                            prefix + "experience");

            Monster monster =
                    new Monster(
                            name,
                            level,
                            attack,
                            health,
                            requiredItem,
                            gold,
                            experience);

            ensureUniqueMonster(
                    result,
                    monster);

            result.add(monster);
        }

        return result;
    }

    private void validateMonsterRequirements(
            List<Monster> monsters,
            List<Item> shop) {

        for (Monster monster : monsters) {
            String required =
                    monster.getRequiredItem();

            if (required == null) {
                continue;
            }

            boolean found = false;

            for (Item item : shop) {
                if (item.getName()
                        .equalsIgnoreCase(required)) {

                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException(
                        "Для монстра \"" +
                                monster.getName() +
                                "\" отсутствует " +
                                "обязательный предмет \"" +
                                required + "\"");
            }
        }
    }

    private void ensureUniqueItem(
            List<Item> items,
            Item candidate) {

        for (Item item : items) {
            if (item.getName()
                    .trim()
                    .equalsIgnoreCase(
                            candidate.getName().trim())) {

                throw new IllegalArgumentException(
                        "Повторное название товара: " +
                                candidate.getName());
            }
        }
    }

    private void ensureUniqueMonster(
            List<Monster> monsters,
            Monster candidate) {

        for (Monster monster : monsters) {
            if (monster.getName()
                    .trim()
                    .equalsIgnoreCase(
                            candidate.getName().trim())) {

                throw new IllegalArgumentException(
                        "Повторное название противника: " +
                                candidate.getName());
            }
        }
    }

    private String required(
            Properties properties,
            String key) {

        String value =
                properties.getProperty(key);

        if (value == null ||
                value.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Отсутствует поле: " + key);
        }

        return value.trim();
    }

    private String optional(
            Properties properties,
            String key) {

        String value =
                properties.getProperty(key);

        if (value == null ||
                value.trim().isEmpty()) {

            return null;
        }

        return value.trim();
    }

    private int parseCount(
            Properties properties,
            String key) {

        int value =
                parseNonNegative(
                        properties,
                        key);

        if (value > 100000) {
            throw new IllegalArgumentException(
                    "Слишком большой размер списка: " +
                            key);
        }

        return value;
    }

    private int parsePositive(
            Properties properties,
            String key) {

        String value =
                required(properties, key);

        try {
            int number =
                    Integer.parseInt(value);

            if (number <= 0) {
                throw new IllegalArgumentException(
                        "Значение должно быть положительным: " +
                                key);
            }

            return number;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Некорректное число: " + key);
        }
    }

    private int parseNonNegative(
            Properties properties,
            String key) {

        String value =
                required(properties, key);

        try {
            int number =
                    Integer.parseInt(value);

            if (number < 0) {
                throw new IllegalArgumentException(
                        "Значение не может быть отрицательным: " +
                                key);
            }

            return number;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Некорректное число: " + key);
        }
    }

    private int readIndex(int size) {
        String input =
                scanner.nextLine().trim();

        if (input.equals("0")) {
            return -1;
        }

        try {
            int number =
                    Integer.parseInt(input);

            if (number < 1 ||
                    number > size) {

                System.out.println(
                        "Неверный номер.");

                return -1;
            }

            return number - 1;

        } catch (NumberFormatException e) {
            System.out.println(
                    "Ошибка: необходимо " +
                            "ввести целое число.");

            return -1;
        }
    }
}
