package ru.lionzxy.damagetweaker;

import com.sun.xml.internal.bind.v2.model.core.ID;
import cpw.mods.fml.common.registry.GameRegistry;
import minetweaker.MineTweakerAPI;
import minetweaker.api.data.DataBool;
import minetweaker.api.data.DataList;
import minetweaker.api.data.DataMap;
import minetweaker.api.data.IData;
import minetweaker.api.formatting.IFormattedText;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.mc1710.data.NBTConverter;
import minetweaker.mc1710.formatting.FormattedString;
import minetweaker.mc1710.formatting.IMCFormattedString;
import minetweaker.mc1710.item.MCItemStack;
import minetweaker.mc1710.oredict.MCOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by nikit on 10.09.2015.
 */

@ZenClass("mods.MTUtils")
public class MTHandlers {
    /*import mods.MTUtils
    Items:
        MTUtils.setItemMaxDamage(itemstack, damage);
        MTUtils.getItemMaxDamage(itemstack); (Return int)
        MTUtils.getItemDamage(itemstack); (Return int)
        Blocks
        MTUtils.setHarvestLevel(itemstack, String tooltip, int harvestLevel);
        MTUtils.setBlockUnbreakable(itemstack);
        MTUtils.setHardness(itemstack, float hardness);
        MTUtils.setLightLevel(itemstack, float lightLevel);
        MTUtils.setLightOpacity(itemstack, int lightOpacity);
        MTUtils.setResistance(itemstack, float resistance);
        MTUtils.setTextureName(itemstack, String texturename);
        MTUtils.getHarvestLevel(itemstack); (Return int)
        MTUtils.getHarvestTool(itemstack); (Return String)
        MTUtils.getTextureName(itemstack, int side); (Return String)
    Utils:
        MTUtils.getIntFromString(string); (Return int)
        MTUtils.getFloatFromString(string); (Return float)
        MTUtils.getStringFromInt(int); (Return String)
        MTUtils.getStringFromFloat(float); (Return String)
        MTUtils.getStringFromFormattedText(IFormattedText text); (Return String)
        MTUtils.getStringFromFormattedString(IMCFormattedString text); (Return String)
        MTUtils.getCrossMatch(IOreDictEntry ... oreDictEntries); (Return IItemStack[])
        MTUtils.getIngredientFromString(String in) (Return IIngredient)
        MTUtils.getItemStackFromString(String in) (Return IItemStack)
        MTUtils.getIOreDictEntryFromString(String in) (Return IOreDictEntry)
        MTUtils.getIFormatedTextFromString(String in) (Return IFormattedText)
        MTUtils.getIMCFormattedTextFromString(String in) (Return IMCFormattedString)
    Drops:
        MTUtils.clearDrops() //Recreated HashMap drops
        MTUtils.setBlockDrops(@Nullable IItemStack harvester, IItemStack block, IItemStack drops[], float quantiDrop[], IItemStack falseDrops[])
    */
    public static IData generateChangeData(IData d) {
        if (d instanceof DataMap) {
            IData toExit = new DataMap(new HashMap<String, IData>(), false);
            for (String s : d.asMap().keySet()) {
                toExit.memberSet(s, generateChangeData(d.asMap().get(s)));
            }
            return toExit;
        } else if (d instanceof DataList) {
            IData toExit = new DataList(new ArrayList<IData>(), false);
            for (IData item : d.asList()) {
                toExit.add(generateChangeData(item));
            }
            return toExit;
        } else return d;
    }

    @ZenMethod
    public static IData writeToNBT(IData data, String path, boolean b) {
        IData toExit = generateChangeData(data);
        String[] pathToPoint = spilitToByte(path, (byte) '.');
        for (int i = 0; i < pathToPoint.length - 1; i++) {
            toExit = toExit.asMap().get(pathToPoint[i]);
        }
        toExit.memberSet(pathToPoint[pathToPoint.length - 1], new DataBool(b));
        return toExit;
    }

    @ZenMethod
    public static IData mergeNBT(IData... datas) {
        datas[datas.length - 1] = generateChangeData(datas[datas.length - 1]);
        for (int i = datas.length - 2; i >= 0; i--) {
            merge(datas[i], datas[datas.length - 1]);
        }
        return datas[datas.length - 1];
    }

    static boolean merge(IData d, IData d2) {
       /* if (d instanceof DataMap)
            for (String key : d.asMap().keySet()) {
                IData data = d.asMap().get(key);
                IData data1 = d2.asMap().get(key);
                if (data1 == null || !(data instanceof DataMap
                        || data1 instanceof DataMap) || !(
                        data instanceof DataList
                                || data1 instanceof DataList))
                    d2.memberSet(key, data);
                else merge(data, data1);
            }
        else if (d instanceof DataList)
            for (IData data :) {
            }*/
        return true;
    }

    @ZenMethod
    public static IFormattedText newLine() {
        return getIFormatedTextFromString("\r\n");
    }

    @ZenMethod
    public static void setItemMaxDamage(IItemStack input, int damage) {
        DamageTweaker.toStack(input).getItem().setMaxDamage(damage);
    }

    @ZenMethod
    public static int getItemMaxDamage(IItemStack input) {
        return DamageTweaker.toStack(input).getItem().getMaxDamage();
    }

    @ZenMethod
    public static int getItemDamage(IItemStack input) {
        return DamageTweaker.toStack(input).getItemDamage();
    }

    @ZenMethod
    public static void setHarvestLevel(IItemStack input, String tooltip, int harvestLevel) {
        if (DamageTweaker.toStack(input).getItemDamage() == 0)
            DamageTweaker.toBlock(input).setHarvestLevel(tooltip, harvestLevel);
        else
            DamageTweaker.toBlock(input).setHarvestLevel(tooltip, harvestLevel, DamageTweaker.toStack(input).getItemDamage());
    }

    @ZenMethod
    public static void setBlockUnbreakable(IItemStack input) {
        DamageTweaker.toBlock(input).setBlockUnbreakable();
    }

    @ZenMethod
    public static void setHardness(IItemStack input, float hardness) {
        DamageTweaker.toBlock(input).setHardness(hardness);
    }

    @ZenMethod
    public static void setLightLevel(IItemStack input, float ll) {
        DamageTweaker.toBlock(input).setLightLevel(ll);
    }

    @ZenMethod
    public static void setLightOpacity(IItemStack input, int opacity) {
        DamageTweaker.toBlock(input).setLightOpacity(opacity);
    }

    @ZenMethod
    public static void setResistance(IItemStack input, float resistance) {
        DamageTweaker.toBlock(input).setResistance(resistance);
    }

    @ZenMethod
    public static int getHarvestLevel(IItemStack input) {
        return DamageTweaker.toBlock(input).getHarvestLevel(DamageTweaker.toStack(input).getItemDamage());
    }

    @ZenMethod
    public static String getHarvestTool(IItemStack input) {
        return DamageTweaker.toBlock(input).getHarvestTool(DamageTweaker.toStack(input).getItemDamage());
    }

    @ZenMethod
    public static String getTextureName(IItemStack input, int side) {
        return DamageTweaker.toBlock(input).getBlockTextureFromSide(side).getIconName();
    }

    @ZenMethod
    public static void setTextureName(IItemStack input, String textureName) {
        DamageTweaker.toBlock(input).setBlockTextureName(textureName);
    }

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
        return text.toString();
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
        return new MCOreDictEntry(OreDictionary.getOreID(in));
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
            if (block != null && DamageTweaker.toBlock(block) != null) {
                DropsObject.dropsObj.add(new DropsObject(DamageTweaker.toStack(harvester), DamageTweaker.toStacks(drops),
                        quantiDrop, DamageTweaker.toStacks(falseDrops), DamageTweaker.toStack(block)));
            } else MineTweakerAPI.logError("BLOCK MUST NOT BE NULL!!!");
        else MineTweakerAPI.logError("Use MTUtils.clearDrop(); in start your script!!!");
    }


    public static boolean contains(ItemStack itemStack, IOreDictEntry... oreDictEntry) {
        for (IOreDictEntry oreDictEntry1 : oreDictEntry)
            if (!oreDictEntry1.contains(new MCItemStack(itemStack)))
                return false;
        return true;
    }

    public static boolean isStringInt(String from) {
        for (int i = 0; i < 10; i++)
            if (from.equalsIgnoreCase(i + ""))
                return true;
        return false;
    }

    public static String[] spilitToByte(String str, byte b) {
        List<String> toExit = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
            if ((byte) str.charAt(i) == b) {
                toExit.add(sb.toString());
                sb = new StringBuilder();
            } else sb.append(str.charAt(i));
        toExit.add(sb.toString());
        return toExit.toArray(new String[toExit.size()]);
    }
}
