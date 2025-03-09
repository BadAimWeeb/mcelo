package me.badaimweeb.mcelo;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import me.badaimweeb.mcelo.commands.AdminCommandHandler;
import me.badaimweeb.mcelo.commands.PlayerCommandHandler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class MCElo extends JavaPlugin {
    @Getter
    private Dao<EloPlayer, UUID> playerDao;
    @Getter
    private Dao<EloRecord, Integer> recordDao;

    private HikariDataSource hkDataSource;
    private DataSourceConnectionSource connectionSource;

    private PlaceholderHook placeholderHook;
    @Getter
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        try {
            this.saveDefaultConfig();
            this.reload();

            var playerCommand = new PlayerCommandHandler(this);
            var adminCommand = new AdminCommandHandler(this);
            
            var bukkitPlayerCommand = Bukkit.getPluginCommand("elo");
            var bukkitAdminCommand = Bukkit.getPluginCommand("eloadmin");

            bukkitPlayerCommand.setExecutor(playerCommand);
            bukkitPlayerCommand.setTabCompleter(playerCommand);

            bukkitAdminCommand.setExecutor(adminCommand);
            bukkitAdminCommand.setTabCompleter(adminCommand);

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                placeholderHook = new PlaceholderHook(this);
                placeholderHook.register();
            }

            this.adventure = BukkitAudiences.create(this);
        } catch (Exception e) {
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
        try {
            if (hkDataSource != null) {
                hkDataSource.close();
                hkDataSource = null;
            }

            if (connectionSource != null) {
                connectionSource.close();
                connectionSource = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (placeholderHook != null) {
            placeholderHook.unregister();
            placeholderHook = null;
        }

        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public void reload() throws Exception {
        if (hkDataSource != null) {
            hkDataSource.close();
        }

        if (connectionSource != null) {
            connectionSource.close();
        }

        this.reloadConfig();
        Messages.load(this);
        var config = this.getConfig();
        GlobalVariable.initialVolatility = config.getDouble("initial-volatility");
        GlobalVariable.initialRating = config.getDouble("initial-rating");
        GlobalVariable.initialRD = config.getDouble("initial-rd");
        GlobalVariable.tau = config.getDouble("tau");
        GlobalVariable.glixareScale = config.getDouble("glixare-scale");
        GlobalVariable.useModifiedGlixare = config.getBoolean("use-modified-glixare");

        var hkConfig = new HikariConfig();
        String driver = config.getString("database.driver");
        hkConfig.setDataSourceClassName(switch (driver.toLowerCase()) {
            case "h2" -> "org.h2.jdbcx.JdbcDataSource";
            case "sqlite" -> "org.sqlite.SQLiteDataSource";
            case "mariadb" -> "org.mariadb.jdbc.MariaDbDataSource";
            default -> throw new RuntimeException("Unknown database driver");
        });

        if (driver.equalsIgnoreCase("h2") || driver.equalsIgnoreCase("sqlite")) {
            String jdbc = "jdbc:" + (driver.toLowerCase()) + ":"
                    + getDataFolder().toPath().toAbsolutePath().resolve(config.getString("database.path"));
            hkConfig.addDataSourceProperty("url", jdbc);
            hkConfig.setJdbcUrl(jdbc);
        } else {
            hkConfig.setUsername(config.getString("database.username"));
            hkConfig.setPassword(config.getString("database.password"));
            hkConfig.addDataSourceProperty("serverName", config.getString("database.host"));
            hkConfig.addDataSourceProperty("portNumber", config.getString("database.port"));
            hkConfig.addDataSourceProperty("databaseName", config.getString("database.database"));
            hkConfig.addDataSourceProperty("useSSL", config.getString("database.ssl"));
            hkConfig.addDataSourceProperty("verifyServerCertificate", config.getString("database.sslVerify"));
            hkConfig.setJdbcUrl("jdbc:");
        }

        hkDataSource = new HikariDataSource(hkConfig);
        connectionSource = new DataSourceConnectionSource(hkDataSource, hkDataSource.getJdbcUrl());

        playerDao = DaoManager.createDao(connectionSource, EloPlayer.class);
        recordDao = DaoManager.createDao(connectionSource, EloRecord.class);

        TableUtils.createTableIfNotExists(connectionSource, EloPlayer.class);
        TableUtils.createTableIfNotExists(connectionSource, EloRecord.class);
    }
}
