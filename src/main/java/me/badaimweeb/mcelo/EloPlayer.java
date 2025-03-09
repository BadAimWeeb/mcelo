package me.badaimweeb.mcelo;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.goochjs.glicko2.Rating;
import org.goochjs.glicko2.RatingCalculator;
import org.goochjs.glicko2.RatingPeriodResults;

@NoArgsConstructor
@ToString
@DatabaseTable(tableName = "mcelo-players")
public class EloPlayer implements AutoCloseable {
    @Getter
    @Setter
    @DatabaseField(id = true, uniqueIndexName = "uuid", canBeNull = false, unique = true)
    private UUID uuid;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, defaultValue = "1500.0")
    private double elo;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, defaultValue = "350.0")
    private double rd;

    @Getter
    @Setter
    @DatabaseField(canBeNull = false, defaultValue = "0.6")
    private double vol;

    @Getter
    @DatabaseField(canBeNull = false, defaultValue = "0")
    private double glixare;

    public EloPlayer(UUID uuid, double elo, double rd, double vol) {
        this.uuid = uuid;
        this.elo = elo;
        this.rd = rd;
        this.vol = vol;
        calculateGlixareRating();
    }

    /**
     * This will NOT get the cached GLIXARE rating from the database. Use carefully.
     */
    public double calculateGlixareRating() {
        if (rd > 100 && !GlobalVariable.useModifiedGlixare) {
            return glixare = 0;
        }

        // Scale is clamped from 0 to 1
        double scale = GlobalVariable.useModifiedGlixare
                ? Math.min(1, Math.max(0, (-20 / 3 * Math.pow(10, -6) * Math.pow(rd, 2) - 0.001 * rd + 7 / 6)))
                : 1;

        /*
         * 1 / (1 + 10^(((1500 - R) * pi / sqrt(3 * ln(10)^2 * RD^2 + 2500 *
         * (64 * pi^2 + 147 * ln(10)^2)))))
         */
        return glixare = (1 / (1 + Math.pow(10,
                ((1500 - elo) * Math.PI
                        / Math.sqrt(3 * Math.pow(Math.log(10), 2) * Math.pow(rd, 2)
                                + 2500 * (64 * Math.pow(Math.PI, 2) + 147 * Math.pow(Math.log(10), 2)))))))
                * scale;
    }

    public void updateRating(EloPlayer op, MatchResult result) {
        RatingCalculator rc = new RatingCalculator(GlobalVariable.initialVolatility, GlobalVariable.tau);

        Rating r = new Rating("", rc, elo, rd, vol);
        Rating or = new Rating("", rc, op.getElo(), op.getRd(), op.getVol());

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
        op.setRd(or.getRatingDeviation());
        op.setVol(or.getVolatility());

        calculateGlixareRating();
        op.calculateGlixareRating();
    }

    public void updateRating(double o_elo, double o_rd, MatchResult result) {
        RatingCalculator rc = new RatingCalculator(GlobalVariable.initialVolatility, GlobalVariable.tau);

        Rating r = new Rating("", rc, elo, rd, vol);
        Rating or = new Rating("", rc, o_elo, o_rd, GlobalVariable.initialVolatility);

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

        calculateGlixareRating();
    }

    @Override
    public void close() throws Exception {
    }
}
