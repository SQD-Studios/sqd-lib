package net.chamosmp.sqdlib.paper.econ;

import net.chamosmp.sqdlib.exceptions.plugin.PluginNotFoundException;
import net.chamosmp.sqdlib.paper.util.ColorUtil;
import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import net.chamosmp.sqdlib.util.log.LogType;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class EconomyUtil {
    private static Economy economy;

    public EconomyUtil() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            LoggerUtil.log(LogType.SEVERE, "Vault economy setup failed. Disabling ChamoOrders plugin...");
            LoggerUtil.log(LogType.SEVERE, "Please ensure that Vault and a compatible economy plugin are installed.");
            throw new PluginNotFoundException("Could not find Vault and a compatible economy plugin.");
        }

        economy = rsp.getProvider();
    }

    public Economy getEconomy() {
        return economy;
    }

    /**
     * Add money to a player
     *
     * @param player The player to give it to
     * @param amount The amount to give
     * @return The {@link TransactionType} which indicates the "success" of the transaction
     */
    public static TransactionType depositPlayer(OfflinePlayer player, double amount) {
        if (Double.isNaN(amount) || amount < 0) {
            return TransactionType.LESS_THAN_ZERO_D;
        }
        if (economy.depositPlayer(player, amount).transactionSuccess()) {
            return TransactionType.TRANSACTION_SUCCESS;
        }
        return TransactionType.TRANSACTION_FAILED;
    }

    /**
     * Remove money from a player
     *
     * @param player The player to remove from
     * @param amount The amount to remove
     * @return The {@link TransactionType} which indicates the "success" of the transaction
     */
    public static TransactionType withdrawPlayer(OfflinePlayer player, double amount) {
        if (Double.isNaN(amount) || amount < 0) {
            return TransactionType.LESS_THAN_ZERO_W;
        }
        if (!economy.has(player, amount)) {
            return TransactionType.NOT_SUFFICIENT_FUNDS;
        }
        if (economy.withdrawPlayer(player, amount).transactionSuccess()) {
            return TransactionType.TRANSACTION_SUCCESS;
        }
        return TransactionType.TRANSACTION_FAILED;
    }

    /**
     * Transfers money from one player to another
     *
     * @param oldPlayer The player to take the money from
     * @param newPlayer The player to give it to
     * @param amount    The amount to transfer
     * @return The {@link TransactionType} which indicates the "success" of the transaction
     */
    public static TransactionType transferBalance(OfflinePlayer oldPlayer, OfflinePlayer newPlayer, double amount) {
        if (!withdrawPlayer(oldPlayer, amount).isSuccess()) {
            return TransactionType.TRANSACTION_FAILED;
        }
        TransactionType type = withdrawPlayer(oldPlayer, amount);
        switch (type) {
            case LESS_THAN_ZERO_W, NOT_SUFFICIENT_FUNDS, TRANSACTION_FAILED:
                return type;
            case TRANSACTION_SUCCESS:
                break;
        }
        return depositPlayer(newPlayer, amount);
    }

    /**
     * Indicates how the transaction failed or succeed
     */
    public enum TransactionType {

        /**
         * This happens when it tries to withdraw amounts less than zero from the player.
         */
        LESS_THAN_ZERO_W(
                false,
                "Cannot remove amounts less than 0",
                Where.WITHDRAW
        ),

        /**
         * This happens when it tries to deposit amounts less than zero from the player.
         */
        LESS_THAN_ZERO_D(
                false,
                "Cannot add amounts less than 0",
                Where.DEPOSIT
        ),

        /**
         * This happens when a player doesn't have enough money to complete this transaction
         */
        NOT_SUFFICIENT_FUNDS(
                false,
                "You do not have enough balance to make this transaction",
                Where.WITHDRAW
        ),

        /**
         * This means that the transaction was successful
         */
        TRANSACTION_SUCCESS(
                true,
                "Successfully made the transaction",
                Where.BOTH
        ),

        /**
         * This means Vault wasn't able to complete the transaction
         */
        TRANSACTION_FAILED(
                false,
                "Transaction failed",
                Where.BOTH
        );

        private final boolean success;
        private final String message;
        private final Where where;

        TransactionType(boolean success, String message, Where failedWhere) {
            this.success = success;
            this.message = message;
            this.where = failedWhere;
        }

        public boolean isSuccess() {
            return success;
        }

        /**
         * It is deprecated as it returns a hardcoded message
         *
         * @return The {@link Component} of the success (indicated by color) and the message constructor variable
         */
        @Deprecated
        public Component getMessage() {
            if (success) {
                return ColorUtil.parse("<green>" + message + "</green>");
            } else {
                return ColorUtil.parse("<red>" + message + "</red>");
            }
        }

        public Where getWhere() {
            return where;
        }

        public enum Where {
            WITHDRAW,
            DEPOSIT,
            BOTH
        }
    }
}