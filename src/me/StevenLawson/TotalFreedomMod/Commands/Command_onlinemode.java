package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Log;
import me.StevenLawson.TotalFreedomMod.TFM_ServerInterface;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH, block_host_console = true)
@CommandParameters(description = "Switch server online-mode on and off.", usage = "/<command> <on | off>")
public class Command_onlinemode extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            playerMsg("Server is currently running with 'online-mode=" + (server.getOnlineMode() ? "true" : "false") + "'.", ChatColor.WHITE);
            playerMsg("If you have Telnet, you can use \"/onlinemode on\" and \"/onlinemode off\" to change online mode.", ChatColor.WHITE);
        }
        else
        {
            boolean online_mode;
            
            if (sender instanceof Player && !TFM_SuperadminList.isSeniorAdmin(sender))
            {
                playerMsg(TotalFreedomMod.MSG_NO_PERMS);
                return true;
            }
            if (args[0].equalsIgnoreCase("on"))
            {
                online_mode = true;
            }
            else if (args[0].equalsIgnoreCase("off"))
            {
                online_mode = false;
            }
            else
            {
                return false;
            }

            try
            {
                TFM_ServerInterface.setOnlineMode(online_mode);

                if (online_mode)
                {
                    for (Player player : server.getOnlinePlayers())
                    {
                        player.kickPlayer("Server is activating \"online-mode=true\". Please reconnect.");
                    }
                }

                TFM_Util.adminAction(sender.getName(), "Turning player validation " + (online_mode ? "on" : "off") + ".", true);

                server.reload();
            }
            catch (Exception ex)
            {
                TFM_Log.severe(ex);
            }
        }

        return true;
    }
}
