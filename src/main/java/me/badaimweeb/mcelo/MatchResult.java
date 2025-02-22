package me.badaimweeb.mcelo;

public enum MatchResult {
    WIN(1), DRAW(0.5), LOSS(0);

    private final double value;

    MatchResult(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
