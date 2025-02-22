package me.badaimweeb.mcelo;

import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Main extends JavaPlugin {
    private Dao<EloPlayer, UUID> playerDao;
    private Dao<EloRecord, Integer> recordDao;

    private HikariDataSource hkDataSource;
    private DataSourceConnectionSource connectionSource;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        try {
            this.reload();
        } catch (Exception e) {
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    void reload() throws Exception {
        if (hkDataSource != null) {
            hkDataSource.close();
        }

        if (connectionSource != null) {
            connectionSource.close();
        }

        this.reloadConfig();
        var config = this.getConfig();
        GlobalVariable.initialVolatility = config.getDouble("initial-volatility");
        GlobalVariable.initialRating = config.getDouble("initial-rating");
        GlobalVariable.initialRD = config.getDouble("initial-rd");
        GlobalVariable.tau = config.getDouble("tau");

        var hkConfig = new HikariConfig();
        String driver = config.getString("database.driver");
        hkConfig.setDataSourceClassName(switch (driver.toLowerCase()) {
            case "h2" -> "org.h2.jdbcx.JdbcDataSource";
            case "sqlite" -> "org.sqlite.SQLiteDataSource";
            case "mariadb" -> "org.mariadb.jdbc.MariaDbDataSource";
            default -> throw new RuntimeException("Unknown database driver");
        });

        if (driver.equalsIgnoreCase("h2") || driver.equalsIgnoreCase("sqlite")) {
            hkConfig.addDataSourceProperty("url", config.getString("database.file"));
        } else {
            hkConfig.setUsername(config.getString("database.username"));
            hkConfig.setPassword(config.getString("database.password"));
            hkConfig.addDataSourceProperty("serverName", config.getString("database.host"));
            hkConfig.addDataSourceProperty("portNumber", config.getString("database.port"));
            hkConfig.addDataSourceProperty("databaseName", config.getString("database.database"));
            hkConfig.addDataSourceProperty("useSSL", config.getString("database.ssl"));
            hkConfig.addDataSourceProperty("verifyServerCertificate", config.getString("database.sslVerify"));
        }

        hkDataSource = new HikariDataSource(hkConfig);
        connectionSource = new DataSourceConnectionSource(hkDataSource, hkDataSource.getJdbcUrl());

        playerDao = DaoManager.createDao(connectionSource, EloPlayer.class);
        recordDao = DaoManager.createDao(connectionSource, EloRecord.class);
    }
}
