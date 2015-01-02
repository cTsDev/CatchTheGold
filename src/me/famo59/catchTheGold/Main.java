
package me.famo59.catchTheGold;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {
    
    int                      punkterot     = 0;
    int                      punkteblau    = 0;
    
    static ArrayList<String> CTGList       = new ArrayList<String>();
    ArrayList<String>        recentlyFired = new ArrayList<String>();
    ArrayList<String>        lobby         = new ArrayList<String>();
    ArrayList<Player>        Smash         = new ArrayList<Player>();
    
    ArrayList<String>        rot           = new ArrayList<String>();
    ArrayList<String>        blau          = new ArrayList<String>();
    public static Main       plugin;
    
    public void onEnable() {
        plugin = this;
        
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, (Plugin) this);
        pm.registerEvents(new DoubleJump(), this);
        
        this.getCommand("setctglobby").setExecutor(new Lobby());
        this.getCommand("setctgspawn").setExecutor(new Lobby());
        this.getCommand("setctgleave").setExecutor(new Lobby());
        this.getCommand("setctgbat").setExecutor(new Lobby());
        
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            
            public void run() {
                recentlyFired.clear();
            }
            
        }, 0L, 100L);
        
    }
    
    public void onDisable() {
        
        plugin = null;
    }
    
    public static boolean isIngame1(Player p) {
        if (CTGList.contains(p.getName())) {
            return true;
        }
        return false;
    }
    
    public static void addPlayer(Player p) {
        if (!isIngame1(p)) {
            CTGList.add(p.getName());
        }
    }
    
    public static void removePlayer(Player p) {
        if (isIngame1(p)) {
            CTGList.remove(p.getName());
        }
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        Player p = (Player) sender;
        
        if (cmd.getName().equalsIgnoreCase("ctg")) {
            if (args.length == 0) {
                
                p.sendMessage("§4Nutze: §1/ctg <join/list/name>");
                return true;
            }
            
            // GameMode gm;
            if (args[0].equalsIgnoreCase("join")) {
                if (GameRunning == false) {
                    if (isIngame1(p)) {
                        p.sendMessage("§8[§cCTG§8] §7Du bist bereits im Spiel.");
                    } else if (!(isIngame1(p))) {
                        if (CTGList.size() == 0) {
                            lobby.add(p.getName());
                            runlobbycounter = true;
                        } else if (CTGList.size() == 6) {
                            p.sendMessage("§8[§cCTG§8] §7Spiel ist voll.");
                            
                        }
                        if (CTGList.size() >= 0 && !(CTGList.size() == 6)) {
                            CTGList.add(p.getName());
                            
                            lobbyCountdown(p);
                            
                            if (rot.size() == 0 && blau.size() == 0) {
                                rot.add(p.getName());
                                // BarAPI.removeBar(p);
                                // BarAPI.setMessage(p, "§2Du bist Team §4ROT");
                            }
                            if (rot.size() == 1 && blau.size() == 0) {
                                if (!(rot.contains(p.getName()))) {
                                    blau.add(p.getName());
                                    // BarAPI.removeBar(p);
                                    // BarAPI.setMessage(p, "§2Du bist Team §1BLAU");
                                }
                            }
                            if (blau.size() == 1 && rot.size() == 1) {
                                if (!(blau.contains(p.getName()))) {
                                    rot.add(p.getName());
                                    // BarAPI.removeBar(p);
                                    // BarAPI.setMessage(p, "§2Du bist Team §4ROT");
                                }
                            }
                            if (rot.size() == 2 && blau.size() == 1) {
                                if (!(rot.contains(p.getName()))) {
                                    blau.add(p.getName());
                                    // BarAPI.removeBar(p);
                                    // BarAPI.setMessage(p, "§2Du bist Team §1BLAU");
                                }
                            }
                            if (blau.size() == 2 && rot.size() == 2) {
                                if (!(blau.contains(p.getName()))) {
                                    rot.add(p.getName());
                                    // BarAPI.removeBar(p);
                                    // BarAPI.setMessage(p, "§2Du bist Team §4ROT");
                                }
                            }
                            if (rot.size() == 3 && blau.size() == 2) {
                                if (!(rot.contains(p.getName()))) {
                                    blau.add(p.getName());
                                    // BarAPI.removeBar(p);
                                    // BarAPI.setMessage(p, "§2Du bist Team §1BLAU");
                                }
                            }
                            // addPlayer(p);
                            GameMode gm;
                            p.setHealth(20.0);
                            p.setFoodLevel(20);
                            
                            File file = new File("plugins/CTG", "warps.yml");
                            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                            
                            p.getInventory().setHelmet(null);
                            
                            gm = GameMode.ADVENTURE;
                            
                            String str = "lobby.";
                            
                            World w = Bukkit.getWorld(cfg.getString(str + "lobby" + "world"));
                            if (w == null) {
                                p.sendMessage("&4Lobby nicht gesetzt!");
                                return true;
                            }
                            double x = cfg.getDouble(str + "lobby" + ".x");
                            double y = cfg.getDouble(str + "lobby" + ".y");
                            double z = cfg.getDouble(str + "lobby" + ".z");
                            double yaw = cfg.getDouble(str + "lobby" + ".yaw");
                            double pitch = cfg.getDouble(str + "lobby" + ".pitch");
                            
                            Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);
                            p.teleport(loc);
                            
                            Smash.add(p);
                            
                            p.getInventory().clear();
                            
                            p.setGameMode(gm);
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §a" + p.getName() + " §7ist CTG beigetreten.");
                            
                            GiveItems(p);
                            
                        }
                    }
                } else {
                    p.sendMessage("§8[§cCTG§8] §7CTG läuft bereits.");
                }
                return true;
            }
            
            if (args.length == 1) {
                
                if (args[0].equalsIgnoreCase("leave")) {
                    
                    if (CTGList.contains(p.getName())) {
                        CTGList.remove(p.getName());
                        lobby.remove(p.getName());
                        p.getInventory().clear();
                        p.getInventory().setChestplate(null);
                        Smash.remove(p);
                        p.setAllowFlight(false);
                        
                        File file = new File("plugins/CTG", "warps.yml");
                        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                        
                        String str = "leave.";
                        
                        World w = Bukkit.getWorld(cfg.getString(str + "leave" + "world"));
                        if (w == null) {
                            p.sendMessage("&4Lobby nicht gesetzt!");
                            return true;
                        }
                        double x = cfg.getDouble(str + "leave" + ".x");
                        double y = cfg.getDouble(str + "leave" + ".y");
                        double z = cfg.getDouble(str + "leave" + ".z");
                        double yaw = cfg.getDouble(str + "leave" + ".yaw");
                        double pitch = cfg.getDouble(str + "leave" + ".pitch");
                        
                        Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);
                        p.teleport(loc);
                        p.getPlayer().kickPlayer("");
                        return true;
                    } else {
                        p.sendMessage("§8[§cCTG§8] §cDu bist nicht in der CTG-Lobby!");
                        return true;
                    }
                }
                
                if (args[0].equalsIgnoreCase("list")) {
                    p.sendMessage(ChatColor.RED + "§8[§cCTG§8] " + "§1Spieler in der CTG-Lobby: §3" + CTGList.size());
                    return true;
                }
                if (args[0].equalsIgnoreCase("name")) {
                    p.sendMessage(ChatColor.RED + "§8[§cCTG§8] " + "§1Spieler in der CTG-Lobby: §3" + CTGList.toString());
                    return true;
                }
                
            }
            
        }
        
        return false;
    }
    
    @EventHandler
    public void PlayerCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        try {
            if (CTGList.contains(p.getName())) {
                if (e.getMessage().equalsIgnoreCase("/ctg leave")) {
                } else {
                    e.setCancelled(true);
                    p.sendMessage("§8[§cCTG§8]" + ChatColor.RED + "Befehl Geblockt!");
                }
                
            }
            
        } catch (NullPointerException ee) {
            
        }
        
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (CTGList.contains(p.getName())) {
            CTGList.remove(p.getName());
            
            lobby.remove(p.getName());
            p.setAllowFlight(false);
            
        }
    }
    
    public void GiveItems(Player p) {
        
        ItemStack istack1 = new ItemStack(Material.WOOD_SWORD);
        ItemMeta istackMeta1 = istack1.getItemMeta();
        istackMeta1.setDisplayName("§4Knife");
        istack1.setItemMeta(istackMeta1);
        istack1.addEnchantment(Enchantment.KNOCKBACK, 2);
        
        ItemStack istack2 = new ItemStack(Material.BOW);
        ItemMeta istackMeta2 = istack2.getItemMeta();
        istackMeta2.setDisplayName("§4Gun");
        istack2.setItemMeta(istackMeta2);
        
        ItemStack istack3 = new ItemStack(Material.ARROW, 14);
        ItemMeta istackMeta3 = istack3.getItemMeta();
        istackMeta3.setDisplayName("§4Bullets");
        istack3.setItemMeta(istackMeta3);
        
        ItemStack istack4 = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta istackMeta4 = istack4.getItemMeta();
        istackMeta4.setDisplayName("§4Protection");
        istack4.setItemMeta(istackMeta4);
        // istack4.addEnchantment(Enchantment.DURABILITY, 3);
        istack4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        // istack4.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
        
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2000000000, 5));
        p.getInventory().setItem(0, istack1);
        p.getInventory().setItem(1, istack2);
        p.getInventory().setItem(2, istack3);
        p.getInventory().setChestplate(istack4);
        
    }
    
    /*
     * @EventHandler
     * public void onDeath(PlayerDeathEvent e){
     * final Player p = e.getEntity();
     * if(CTGList.contains(p.getName()) && !(lobby.contains(p.getName()))){
     * e.setDeathMessage("§8[§cCTG§8] §7" + p.getName() + "§e ist gestorben!");
     * e.getDrops().clear();
     * 
     * plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
     * {
     * public void run()
     * {
     * if(p.isDead())
     * {
     * ((CraftPlayer)p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
     * GiveItems(p);
     * }
     * }
     * });
     * 
     * 
     * 
     * 
     * File file = new File("plugins/CTG", "warps.yml");
     * final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
     * 
     * final String str = "spawn.";
     * plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
     * {
     * public void run()
     * {
     * 
     * GiveItems(p);
     * World w = Bukkit.getWorld(cfg.getString(str + "world"));
     * if(w == null){
     * p.sendMessage("&4Spawn nicht gesetzt!");
     * 
     * }
     * double x = cfg.getDouble(str + "spawn" +".x");
     * double y = cfg.getDouble(str + ".y");
     * double z = cfg.getDouble(str + ".z");
     * double yaw = cfg.getDouble(str + ".yaw");
     * double pitch = cfg.getDouble(str + ".pitch");
     * 
     * Location loc = new Location(w, x, y, z,(float) yaw,(float) pitch);
     * p.teleport(loc);
     * }
     * }, 10L);
     * 
     * }
     * }
     */
    
    public void resetAll() {
        
        runlobbycounter = false;
        runingamecounter = false;
        GameFull = false;
        GameRunning = false;
        running = false;
        peace = true;
        bool1 = false;
        bool2 = false;
        bool3 = false;
        bool4 = false;
        bool5 = false;
        bool6 = false;
        bool7 = false;
        
    }
    
    @EventHandler
    public void Join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String str = "lobby.";
        File file = new File("plugins/CTG", "warps.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        World w = Bukkit.getWorld(cfg.getString(str + "lobby" + "world"));
        if (w == null) {
            p.sendMessage("&4Lobby nicht gesetzt!");
        }
        double x = cfg.getDouble(str + "lobby" + ".x");
        double y = cfg.getDouble(str + "lobby" + ".y");
        double z = cfg.getDouble(str + "lobby" + ".z");
        double yaw = cfg.getDouble(str + "lobby" + ".yaw");
        double pitch = cfg.getDouble(str + "lobby" + ".pitch");
        
        Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);
        p.teleport(loc);
        
        p.performCommand("ctg join");
    }
    
    @EventHandler
    public void blockbreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (CTGList.contains(p.getName())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void blockplace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (CTGList.contains(p.getName())) {
            if ((e.getBlockPlaced().getType() != Material.IRON_BLOCK) || (e.getBlockPlaced().getType() != Material.GOLD_BLOCK)) {
                e.setCancelled(true);
            }
            
            if (e.getBlockPlaced().getType() == Material.IRON_BLOCK) {
                if (e.getBlockPlaced().getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_BLOCK) {
                    if (rot.contains(p.getName())) {
                        e.getBlockPlaced().breakNaturally(null);
                        p.getInventory().removeItem(new ItemStack(Material.IRON_BLOCK));
                        punkterot += 10;
                        Bukkit.broadcastMessage("§8[§cCTG§8] §7Rot erhält 10 Punkte! (" + punkterot + "/100)");
                        if (punkterot == 100) {
                            finish = true;
                            for (Player player : Smash) {
                                player.performCommand("ctg leave");
                                
                                if (bool7 == false) {
                                    bool7 = true;
                                    Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spiel ist zuende! Rot hat gewonnen!");
                                }
                                resetAll();
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                                    
                                    public void run() {
                                        clearAll();
                                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
                                    }
                                }, 60);
                            }
                        }
                    }
                    if (blau.contains(p.getName())) {
                        e.getBlockPlaced().breakNaturally(null);
                        p.getInventory().removeItem(new ItemStack(Material.IRON_BLOCK));
                        punkteblau += 10;
                        Bukkit.broadcastMessage("§8[§cCTG§8] §7Blau erhält 10 Punkte! (" + punkteblau + "/100)");
                        if (punkteblau == 100) {
                            finish = true;
                            for (Player player : Smash) {
                                player.performCommand("ctg leave");
                                
                                if (bool7 == false) {
                                    bool7 = true;
                                    Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spiel ist zuende! Blau hat gewonnen!");
                                }
                                resetAll();
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                                    
                                    public void run() {
                                        clearAll();
                                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
                                    }
                                }, 60);
                                
                            }
                        }
                    }
                }
            }
            if (e.getBlockPlaced().getType() == Material.GOLD_BLOCK) {
                if (e.getBlockPlaced().getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_BLOCK) {
                    if (rot.contains(p.getName())) {
                        e.getBlockPlaced().breakNaturally(null);
                        p.getInventory().removeItem(new ItemStack(Material.GOLD_BLOCK));
                        finish = true;
                        if (bool7 == false) {
                            bool7 = true;
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spiel ist zuende! Rot hat gewonnen!");
                        }
                        resetAll();
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                            
                            public void run() {
                                clearAll();
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
                                
                            }
                        }, 60);
                    }
                    
                    if (blau.contains(p.getName())) {
                        e.getBlockPlaced().breakNaturally(null);
                        p.getInventory().removeItem(new ItemStack(Material.GOLD_BLOCK));
                        finish = true;
                        if (bool7 == false) {
                            bool7 = true;
                            
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spiel ist zuende! Blau hat gewonnen!");
                        }
                        resetAll();
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                            
                            public void run() {
                                clearAll();
                                // Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "butcher -a");
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
                            }
                        }, 60);
                    }
                }
            }
        }
    }
    
    public void clearAll() {
        for (World w : getServer().getWorlds()) {
            for (Chunk c : w.getLoadedChunks()) {
                c.load();
                for (Entity e : c.getEntities()) {
                    e.remove();
                }
                c.unload(true);
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (CTGList.contains(p.getName())) {
                if (event.getCause().equals(DamageCause.FALL)) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    public static boolean runlobbycounter  = false;
    public static boolean runingamecounter = false;
    public static boolean GameFull         = false;
    public static boolean GameRunning      = false;
    public static boolean running          = false;
    public static boolean peace            = true;
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (CTGList.contains(p.getName())) {
            e.setCancelled(true);
        }
        
    }
    
    public void lobbyCountdown(final Player p) {
        
        if (lobby.size() >= 1) {
            if (running == false && GameRunning == false) {
                
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                    
                    public void run() {
                        running = true;
                        runlobbycounter = false;
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spiel beginnt in §e30 §7Sekunden!");
                        
                    }
                    
                }, 20L);
                
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                    
                    public void run() {
                        runlobbycounter = false;
                        
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spiel beginnt in §e20 §7Sekunden!");
                        
                    }
                    
                }, 200L);
                
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                    
                    public void run() {
                        runlobbycounter = false;
                        
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spiel beginnt in §e10 §7Sekunden!");
                        
                    }
                    
                }, 400L);
                
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                    
                    public void run() {
                        runlobbycounter = false;
                        if (CTGList.size() < 6 && CTGList.size() > 0) {
                            
                            resetAll();
                            lobbyCountdown(p);
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Abfrage...");
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §c" + CTGList.size() + "§e/§c6 §7sind im Spiel");
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spieler minimum: §e6");
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §cZu wenig Spieler, Countdown beginnt von vorne!");
                            
                        } else
                        
                        if (CTGList.size() == 0) {
                            
                            resetAll();
                            lobbyCountdown(p);
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Abfrage...");
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §c" + CTGList.size() + "§e/§c6 §7sind im Spiel");
                            Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §cZu wenig Spieler, Countdown beendet!");
                            
                        } else {
                            
                            if (CTGList.size() == 6) {
                                Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spiel startet jetzt!");
                                Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §c" + CTGList.size() + "§e/§c6 §7sind im Spiel");
                                Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spieler minimum: §e6");
                                Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §aGenügend Spieler, Spiel startet jetzt!");
                                GameRunning = true;
                                
                                lobby.remove(p.getName());
                                
                                int i = 1;
                                
                                File file = new File("plugins/CTG", "warps.yml");
                                FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                                
                                String str = "spawn.";
                                
                                World w = Bukkit.getWorld(cfg.getString(str + i + "world"));
                                if (w == null) {
                                    // p.sendMessage("&4Spawn nicht gesetzt!");
                                    
                                }
                                double x = cfg.getDouble(str + i + ".x");
                                double y = cfg.getDouble(str + i + ".y");
                                double z = cfg.getDouble(str + i + ".z");
                                double yaw = cfg.getDouble(str + i + ".yaw");
                                double pitch = cfg.getDouble(str + i + ".pitch");
                                
                                Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);
                                for (Player player : Smash) {
                                    
                                    player.teleport(loc);
                                    gameCountdown(player);
                                    i++;
                                }
                                
                                FileConfiguration cfg1 = YamlConfiguration.loadConfiguration(Lobby.file);
                                String str1 = "bat.";
                                
                                final World w1 = Bukkit.getWorld(cfg1.getString(str1 + "bat" + "world"));
                                
                                final double x1 = cfg1.getDouble(str1 + "bat" + ".x");
                                final double y1 = cfg1.getDouble(str1 + "bat" + ".y");
                                final double z1 = cfg1.getDouble(str1 + "bat" + ".z");
                                // Bukkit.dispatchCommand(sender, "summon Item " + x + y + z + " {Item:{id:42,Count:1},Riding:{id:"+ "Bat " +
                                // ",Invulnerable:true,ActiveEffects:[{Id:14,Amplifier:2,Duration:10000000}]}}");
                                plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                                    
                                    public void run() {
                                        int i = 1;
                                        if (i < 31) {
                                            i++;
                                            Bat bat = (Bat) w1.spawnEntity(new Location(w1, x1, y1, z1), EntityType.BAT);
                                            ItemStack istack1 = new ItemStack(Material.IRON_BLOCK);
                                            Item item = w1.dropItem(bat.getLocation(), istack1);
                                            bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2000000000, 500));
                                            bat.setPassenger(item);
                                            
                                        }
                                    }
                                }, 60L, 60L);
                                
                                Bat bat = (Bat) w1.spawnEntity(new Location(w1, x1, y1, z1), EntityType.BAT);
                                ItemStack istack1 = new ItemStack(Material.GOLD_BLOCK);
                                Item item = w1.dropItem(bat.getLocation(), istack1);
                                bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2000000000, 500));
                                bat.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000000000, 100));
                                bat.setPassenger(item);
                            }
                            
                        }
                        
                    }
                    
                }, 600L);
            }
        }
    }
    
    boolean bool1  = false;
    boolean bool2  = false;
    boolean bool3  = false;
    boolean bool4  = false;
    boolean bool5  = false;
    boolean bool6  = false;
    boolean bool7  = false;
    boolean finish = false;
    
    public void gameCountdown(final Player p) {
        if (!finish)
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                
                public void run() {
                    runingamecounter = false;
                    if (bool1 == false) {
                        bool1 = true;
                        
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Noch §e4 Minuten §7bis Spiel-Ende!");
                    }
                }
                
            }, 600L);
        
        if (!finish)
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                
                public void run() {
                    runingamecounter = false;
                    if (bool2 == false) {
                        bool2 = true;
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Noch §e3 Minuten §7bis Spiel-Ende!");
                    }
                }
                
            }, 1200 + 600L);
        
        if (!finish)
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                
                public void run() {
                    runingamecounter = false;
                    if (bool3 == false) {
                        bool3 = true;
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Noch §e2 Minuten §7bis Spiel-Ende!");
                    }
                }
                
            }, 2400 + 600L);
        if (!finish)
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                
                public void run() {
                    runingamecounter = false;
                    if (bool4 == false) {
                        bool4 = true;
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Noch §e1 Minuten §7bis Spiel-Ende!");
                    }
                }
                
            }, 3600 + 600L);
        if (!finish)
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                
                public void run() {
                    runingamecounter = false;
                    if (bool5 == false) {
                        bool5 = true;
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Noch §e30 Sekunden §7bis Spiel-Ende!");
                    }
                }
                
            }, 4200 + 600L);
        if (!finish)
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                
                public void run() {
                    runingamecounter = false;
                    if (bool6 == false) {
                        bool6 = true;
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Noch §e10 Sekunden §7bis Spiel-Ende!");
                    }
                }
                
            }, 4600 + 600L);
        if (!finish)
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                
                public void run() {
                    runingamecounter = false;
                    
                    p.performCommand("ctg leave");
                    
                    if (bool7 == false) {
                        bool7 = true;
                        Bukkit.getServer().broadcastMessage("§8[§cCTG§8] §7Spiel ist zuende!");
                    }
                    resetAll();
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
                }
                
            }, 4800 + 600L);
        
    }
}
