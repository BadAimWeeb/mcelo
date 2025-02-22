package me.badaimweeb.mcelo;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.goochjs.glicko2.Rating;
import org.goochjs.glicko2.RatingCalculator;
import org.goochjs.glicko2.RatingPeriodResults;

@DatabaseTable(tableName = "mcelo-players")
public class EloPlayer {
    @DatabaseField(id = true, uniqueIndexName = "uuid", canBeNull = false, unique = true)
    private UUID uuid;

    @DatabaseField(canBeNull = false, defaultValue = "1500.0")
    private double elo;

    @DatabaseField(canBeNull = false, defaultValue = "350.0")
    private double rd;

    @DatabaseField(canBeNull = false, defaultValue = "0.6")
    private double vol;

    public double getElo() {
        return elo;
    }

    public double getRD() {
        return rd;
    }

    public double getVol() {
        return vol;
    }

    public void setElo(double elo) {
        this.elo = elo;
    }

    public void setRD(double rd) {
        this.rd = rd;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public double getGlixareRating() {
        if (rd > 100) {
            return 0;
        }

        /*
         * round(
         *  10000 / (1 + 10^(((1500 - R) * pi / sqrt(3 * ln(10)^2 * RD^2 + 2500 *
         *  (64 * pi^2 + 147 * ln(10)^2)))))
         * ) / 100
         */
        return Math
                .round(10000 / (1 + Math.pow(10,
                        ((1500 - elo) * Math.PI
                                / Math.sqrt(3 * Math.pow(Math.log(10), 2) * Math.pow(rd, 2)
                                        + 2500 * (64 * Math.pow(Math.PI, 2) + 147 * Math.pow(Math.log(10), 2)))))))
                / 100.0;
    }

    public void updateRating(EloPlayer op, MatchResult result) {
        RatingCalculator rc = new RatingCalculator(GlobalVariable.initialVolatility, GlobalVariable.tau);
        
        Rating r = new Rating("", rc, elo, rd, vol);
        Rating or = new Rating("", rc, op.getElo(), op.getRD(), op.getVol());

        RatingPeriodResults results = new RatingPeriodResults();
        if (result == MatchResult.WIN) {
            results.addResult(r, or);
        } else if (result == MatchResult.LOSS) {
            results.addResult(or, r);
        } else {
            results.addDraw(r, or);
        }

        rc.updateRatings(results);

        elo = r.getRating();
        rd = r.getRatingDeviation();
        vol = r.getVolatility();

        op.setElo(or.getRating());
        op.setRD(or.getRatingDeviation());
        op.setVol(or.getVolatility());
    }

    public void updateRating(double o_elo, double o_rd, MatchResult result) {
        RatingCalculator rc = new RatingCalculator(GlobalVariable.initialVolatility, GlobalVariable.tau);
        
        Rating r = new Rating("", rc, elo, rd, vol);
        Rating or = new Rating("", rc, o_elo, o_rd, vol);

        RatingPeriodResults results = new RatingPeriodResults();
        if (result == MatchResult.WIN) {
            results.addResult(r, or);
        } else if (result == MatchResult.LOSS) {
            results.addResult(or, r);
        } else {
            results.addDraw(r, or);
        }

        rc.updateRatings(results);

        elo = r.getRating();
        rd = r.getRatingDeviation();
        vol = r.getVolatility();
    }
}
