public final class BattleResult {
    private final String monsterName;
    private final boolean victory;
    private final int experienceGained;
    private final int goldGained;
    private final String description;

    public BattleResult(
            String monsterName,
            boolean victory,
            int experienceGained,
            int goldGained,
            String description) {

        if (monsterName == null ||
                monsterName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Название монстра не может быть пустым");
        }

        if (experienceGained < 0 ||
                goldGained < 0) {

            throw new IllegalArgumentException(
                    "Награды не могут быть отрицательными");
        }

        this.monsterName = monsterName.trim();
        this.victory = victory;
        this.experienceGained = experienceGained;
        this.goldGained = goldGained;
        this.description =
                description == null ? "" : description;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public boolean isVictory() {
        return victory;
    }

    public int getExperienceGained() {
        return experienceGained;
    }

    public int getGoldGained() {
        return goldGained;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return monsterName +
                " | " +
                (victory ? "ПОБЕДА" : "ПОРАЖЕНИЕ") +
                " | золото: " + goldGained +
                " | опыт: " + experienceGained +
                " | " + description;
    }
}
