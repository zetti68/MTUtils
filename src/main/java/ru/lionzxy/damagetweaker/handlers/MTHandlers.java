package ru.lionzxy.damagetweaker.handlers;


import minetweaker.MineTweakerAPI;
import minetweaker.api.formatting.IFormattedText;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.api.tooltip.IngredientTooltips;
import minetweaker.mc18.formatting.FormattedString;
import minetweaker.mc18.formatting.IMCFormattedString;
import minetweaker.mc18.item.MCItemStack;
import minetweaker.mc18.oredict.MCOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import ru.lionzxy.damagetweaker.MTUtilsMod;
import ru.lionzxy.damagetweaker.models.DropsObject;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nikit on 10.09.2015.
 */

@ZenClass("mods.MTUtils.Utils")
public class MTHandlers {

    @ZenMethod
    public static void executeCommand(IFormattedText cmd) {
        executeCommand(getStringFromFormattedText(cmd));
    }

    @ZenMethod
    public static void executeCommand(final String cmd) {
        TicksHandler.addTasksAfterSererLoaded(new Runnable() {
            @Override
            public void run() {
                MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), cmd);
            }
        });
    }

    @ZenMethod
    public static void addMultilineShiftTooltip(IItemStack stack, IFormattedText strs, String s) {
        //((FormattedMarkupString) strs)
        for (String str : splitString(MTHandlers.getStringFromFormattedText(strs), s))
            IngredientTooltips.addShiftTooltip(stack, MTHandlers.getIFormatedTextFromString(str));
    }

    @ZenMethod
    public static void addMultilineTooltip(IItemStack stack, IFormattedText strs, String s) {
        for (String str : splitString(MTHandlers.getStringFromFormattedText(strs), s))
            IngredientTooltips.addTooltip(stack, MTHandlers.getIFormatedTextFromString(str));
    }


    public static String[] splitString(String original, char split) {
        return NBTHandlers.splitToByte(original, (byte) split);
    }

    @ZenMethod
    public static String[] splitString(String original, String split) {
        if (split.length() == 1)
            return splitString(original, split.charAt(0));
        return original.split(split);
    }

    @ZenMethod
    public static IFormattedText newLine() {
        return getIFormatedTextFromString("\n");
    }

    @ZenMethod
    public static void setItemMaxDamage(IItemStack input, int damage) {
        MTUtilsMod.toStack(input).getItem().setMaxDamage(damage);
    }

    @ZenMethod
    public static int getItemMaxDamage(IItemStack input) {
        return MTUtilsMod.toStack(input).getItem().getMaxDamage();
    }

    @ZenMethod
    public static int getRandomInt(int range) {
        return new Random().nextInt(range);
    }

    @ZenMethod
    public static int getItemDamage(IItemStack input) {
        return MTUtilsMod.toStack(input).getItemDamage();
    }

    @ZenMethod
    public static void setHarvestLevel(IItemStack input, String tooltip, int harvestLevel) {
        if (MTUtilsMod.toStack(input).getItemDamage() == 0)
            MTUtilsMod.toBlock(input).setHarvestLevel(tooltip, harvestLevel);
        else
            MTUtilsMod.toBlock(input).setHarvestLevel(tooltip, harvestLevel, MTUtilsMod.toBlock(input).getStateFromMeta(MTUtilsMod.toStack(input).getItemDamage()));
    }

    @ZenMethod
    public static void setBlockUnbreakable(IItemStack input) {
        MTUtilsMod.toBlock(input).setBlockUnbreakable();
    }

    @ZenMethod
    public static void setHardness(IItemStack input, float hardness) {
        MTUtilsMod.toBlock(input).setHardness(hardness);
    }

    @ZenMethod
    public static void setLightLevel(IItemStack input, float ll) {
        MTUtilsMod.toBlock(input).setLightLevel(ll);
    }

    @ZenMethod
    public static void setLightOpacity(IItemStack input, int opacity) {
        MTUtilsMod.toBlock(input).setLightOpacity(opacity);
    }

    @ZenMethod
    public static void setResistance(IItemStack input, float resistance) {
        MTUtilsMod.toBlock(input).setResistance(resistance);
    }

    @ZenMethod
    public static int getHarvestLevel(IItemStack input) {
        return MTUtilsMod.toBlock(input).getHarvestLevel(MTUtilsMod.toBlock(input).getStateFromMeta(MTUtilsMod.toStack(input).getItemDamage()));
    }

    @ZenMethod
    public static String getHarvestTool(IItemStack input) {
        return MTUtilsMod.toBlock(input).getHarvestTool(MTUtilsMod.toBlock(input).getStateFromMeta(MTUtilsMod.toStack(input).getItemDamage()));
    }

    /* Only 1.7.10
    @ZenMethod
    public static String getTextureName(IItemStack input, int side) {
        return MTUtilsMod.toBlock(input).getBlockTextureFromSide(side).getIconName();
    }

    @ZenMethod
    public static void setTextureName(IItemStack input, String textureName) {
        MTUtilsMod.toBlock(input).setBlockTextureName(textureName);
    }*/

    @ZenMethod
    public static int getIntFromString(String from) {
        String newStr = "";
        for (int i = 0; i < from.length(); i++)
            if (isStringInt(from.charAt(i) + ""))
                newStr += from.charAt(i);
        return Integer.parseInt(newStr);
    }

    @ZenMethod
    public static float getFloatFromString(String from) {
        return Float.parseFloat(from);
    }

    @ZenMethod
    public static String getStringFromInt(int i) {
        return i + "";
    }

    @ZenMethod
    public static String getStringFromFloat(float i) {
        return i + "";
    }

    @ZenMethod
    public static String getStringFromFormattedText(IFormattedText text) {
        return ((FormattedString) text).getTooltipString();
    }

    @ZenMethod
    public static String getStringFromFormattedString(IMCFormattedString text) {
        return text.getTooltipString();
    }

    @ZenMethod
    public static IItemStack[] getCrossMatch(IOreDictEntry... oreDictEntries) {
        List<ItemStack> oreDict = OreDictionary.getOres(oreDictEntries[0].getName());
        List<IItemStack> toExit = new ArrayList<IItemStack>();
        for (int i = 0; i < oreDict.size(); i++)
            if (contains(oreDict.get(i), oreDictEntries))
                toExit.add(new MCItemStack(oreDict.get(i)));
        IItemStack toExitArr[] = new IItemStack[toExit.size()];
        for (int i = 0; i < toExit.size(); i++)
            toExitArr[i] = toExit.get(i);
        return toExitArr;
    }

    @ZenMethod
    public static IItemStack[] getCrossMatch2(IOreDictEntry oreDictEntry, IOreDictEntry oreDictEntry2) {
        return getCrossMatch(oreDictEntry, oreDictEntry2);
    }

    @ZenMethod
    public static IItemStack[] getCrossMatch3(IOreDictEntry oreDictEntry, IOreDictEntry oreDictEntry2, IOreDictEntry oreDictEntry3) {
        return getCrossMatch(oreDictEntry, oreDictEntry2, oreDictEntry3);
    }

    @ZenMethod
    public static IItemStack[] getCrossMatch4(IOreDictEntry oreDictEntry, IOreDictEntry oreDictEntry2,
                                              IOreDictEntry oreDictEntry3, IOreDictEntry oreDictEntry4) {
        return getCrossMatch(oreDictEntry, oreDictEntry2, oreDictEntry3, oreDictEntry4);
    }

    @ZenMethod
    public static IItemStack[] getCrossMatch5(IOreDictEntry oreDictEntry, IOreDictEntry oreDictEntry2,
                                              IOreDictEntry oreDictEntry3, IOreDictEntry oreDictEntry4,
                                              IOreDictEntry oreDictEntry5) {
        return getCrossMatch(oreDictEntry, oreDictEntry2, oreDictEntry3, oreDictEntry4, oreDictEntry5);
    }

    @ZenMethod
    public static IIngredient getIngredientFromString(String in) {
        String items[] = in.split(":");
        if (items.length == 2)
            return new MCItemStack(new ItemStack(GameRegistry.findItem(items[0], items[1])));

        return new MCItemStack(new ItemStack(GameRegistry.findItem(items[0], items[1]), Integer.parseInt(items[2])));
    }

    @ZenMethod
    public static IItemStack getItemStackFromString(String in) {
        String items[] = in.split(":");
        if (items.length == 2)
            return new MCItemStack(new ItemStack(GameRegistry.findItem(items[0], items[1])));

        return new MCItemStack(new ItemStack(GameRegistry.findItem(items[0], items[1]), Integer.parseInt(items[2])));
    }

    @ZenMethod
    public static IOreDictEntry getIOreDictEntryFromString(String in) {
        return new MCOreDictEntry(in);
    }

    @ZenMethod
    public static IOreDictEntry getIOreDictEntryFromString(IFormattedText in) {
        return getIOreDictEntryFromString(getStringFromFormattedText(in));
    }

    @ZenMethod
    public static IOreDictEntry getIOreDictEntryFromString(IMCFormattedString in) {
        return getIOreDictEntryFromString(getStringFromFormattedString(in));
    }

    @ZenMethod
    public static IFormattedText getIFormatedTextFromString(String in) {
        return new FormattedString(in);
    }

    @ZenMethod
    public static IMCFormattedString getIMCFormattedTextFromString(String in) {
        return new FormattedString(in);
    }

    @ZenMethod
    public static void clearDrops() {
        DropsObject.dropsObj = new ArrayList<DropsObject>();
    }

    @ZenMethod
    public static void setBlockDrops(@Nullable IItemStack harvester, IItemStack block, IItemStack drops[], float quantiDrop[], IItemStack falseDrops[]) {

        if (DropsObject.dropsObj != null)
            if (block != null && MTUtilsMod.toBlock(block) != null) {
                DropsObject.dropsObj.add(new DropsObject(MTUtilsMod.toStack(harvester), MTUtilsMod.toStacks(drops),
                        quantiDrop, MTUtilsMod.toStacks(falseDrops), MTUtilsMod.toStack(block)));
            } else MineTweakerAPI.logError("BLOCK MUST NOT BE NULL!!!");
        else MineTweakerAPI.logError("Use MTUtils.clearDrop(); in start your script!!!");
    }

    @ZenMethod
    public static boolean eqItemStack(IItemStack... stacks) {
        ItemStack[] stacks1 = MTUtilsMod.toStacks(stacks);
        for (int i = 1; i < stacks1.length; i++)
            if (!stacks1[i].isItemEqual(stacks1[i - 1]))
                return false;
        return true;
    }

    @ZenMethod
    public static boolean eqString(String... strs) {
        for (int i = 1; i < strs.length; i++)
            if (!strs[i].equals(strs[i - 1]))
                return false;
        return true;
    }


    public static boolean contains(ItemStack itemStack, IOreDictEntry... oreDictEntry) {
        for (IOreDictEntry oreDictEntry1 : oreDictEntry)
            if (!oreDictEntry1.contains(new MCItemStack(itemStack)))
                return false;
        return true;
    }

    public static boolean isStringInt(String from) {
        return (byte) '0' - 1 < from.charAt(0) && from.charAt(0) > (byte) '9' + 1;
    }
}