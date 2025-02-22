package me.badaimweeb.mcelo;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
}
