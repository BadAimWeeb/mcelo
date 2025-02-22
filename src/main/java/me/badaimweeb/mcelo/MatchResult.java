package me.badaimweeb.mcelo;

public enum MatchResult {
    WIN(1), DRAW(0.5f), LOSS(0);

    private final float value;

    MatchResult(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public static MatchResult fromValue(float value) {
        for (MatchResult result : values()) {
            if (result.getValue() == value) {
                return result;
            }
        }

        return null;
    }
}
