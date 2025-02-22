package me.badaimweeb.mcelo;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mcelo-records")
public class EloRecord {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private UUID uuid;

    @DatabaseField
    private UUID opponent;

    @DatabaseField
    private float result;

    @DatabaseField
    private long timestamp;

    @DatabaseField
    private double beforeElo;

    @DatabaseField
    private double afterElo;

    @DatabaseField
    private double beforeRD;

    @DatabaseField
    private double afterRD;

    @DatabaseField
    private double beforeVol;

    @DatabaseField
    private double afterVol;

    @DatabaseField
    private double opponentBeforeElo;

    @DatabaseField
    private double opponentAfterElo;

    @DatabaseField
    private double opponentBeforeRD;

    @DatabaseField
    private double opponentAfterRD;

    @DatabaseField
    private double opponentBeforeVol;

    @DatabaseField
    private double opponentAfterVol;

    public UUID getUUID() {
        return uuid;
    }

    public UUID getOpponent() {
        return opponent;
    }

    public float getResult() {
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getBeforeElo() {
        return beforeElo;
    }

    public double getAfterElo() {
        return afterElo;
    }

    public double getBeforeRD() {
        return beforeRD;
    }

    public double getAfterRD() {
        return afterRD;
    }

    public double getBeforeVol() {
        return beforeVol;
    }

    public double getAfterVol() {
        return afterVol;
    }

    public double getOpponentBeforeElo() {
        return opponentBeforeElo;
    }

    public double getOpponentAfterElo() {
        return opponentAfterElo;
    }

    public double getOpponentBeforeRD() {
        return opponentBeforeRD;
    }

    public double getOpponentAfterRD() {
        return opponentAfterRD;
    }

    public double getOpponentBeforeVol() {
        return opponentBeforeVol;
    }

    public double getOpponentAfterVol() {
        return opponentAfterVol;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setOpponent(UUID opponent) {
        this.opponent = opponent;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setBeforeElo(double beforeElo) {
        this.beforeElo = beforeElo;
    }

    public void setAfterElo(double afterElo) {
        this.afterElo = afterElo;
    }

    public void setBeforeRD(double beforeRD) {
        this.beforeRD = beforeRD;
    }

    public void setAfterRD(double afterRD) {
        this.afterRD = afterRD;
    }

    public void setBeforeVol(double beforeVol) {
        this.beforeVol = beforeVol;
    }

    public void setAfterVol(double afterVol) {
        this.afterVol = afterVol;
    }

    public void setOpponentBeforeElo(double opponentBeforeElo) {
        this.opponentBeforeElo = opponentBeforeElo;
    }

    public void setOpponentAfterElo(double opponentAfterElo) {
        this.opponentAfterElo = opponentAfterElo;
    }

    public void setOpponentBeforeRD(double opponentBeforeRD) {
        this.opponentBeforeRD = opponentBeforeRD;
    }

    public void setOpponentAfterRD(double opponentAfterRD) {
        this.opponentAfterRD = opponentAfterRD;
    }

    public void setOpponentBeforeVol(double opponentBeforeVol) {
        this.opponentBeforeVol = opponentBeforeVol;
    }

    public void setOpponentAfterVol(double opponentAfterVol) {
        this.opponentAfterVol = opponentAfterVol;
    }
}
