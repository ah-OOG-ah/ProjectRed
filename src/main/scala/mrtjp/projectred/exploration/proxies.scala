package mrtjp.projectred.exploration

import java.lang.{Character => JChar}

import codechicken.microblock.BlockMicroMaterial
import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mrtjp.core.block.TileRenderRegistry
import mrtjp.core.color.Colors
import mrtjp.core.gui.GuiHandler
import mrtjp.core.inventory.InvWrapper
import mrtjp.core.world._
import mrtjp.projectred.ProjectRedExploration
import mrtjp.projectred.ProjectRedExploration._
import mrtjp.projectred.core.libmc.recipe._
import mrtjp.projectred.core.{
  Configurator,
  IProxy,
  PartDefs,
  ShapelessOreNBTRecipe
}
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.oredict.{OreDictionary, ShapedOreRecipe}

class ExplorationProxy_client extends ExplorationProxy_server {
  @SideOnly(Side.CLIENT)
  override def init() {
    super.init()
    MinecraftForgeClient.registerItemRenderer(itemGoldSaw, GemSawRenderer)
    MinecraftForgeClient.registerItemRenderer(itemRubySaw, GemSawRenderer)
    MinecraftForgeClient.registerItemRenderer(itemSapphireSaw, GemSawRenderer)
    MinecraftForgeClient.registerItemRenderer(itemPeridotSaw, GemSawRenderer)

    GuiHandler.register(GuiBackpack, guiIDBackpack)

    TileRenderRegistry.setRenderer(blockLily, 0, RenderLily)
    TileRenderRegistry.setRenderer(blockBarrel, 0, RenderBarrel)

    ClientRegistry.bindTileEntitySpecialRenderer(
      classOf[TileBarrel],
      RenderBarrel
    )
  }
}

object ExplorationProxy extends ExplorationProxy_client

object ExplorationRecipes {
  def initRecipes() {
    initOreDict()
    initEtcRecipes()
    initGemToolRecipes()
    initToolRecipes()
    initWorldRecipes()
  }

  private def initOreDict() {
    for (i <- 0 until 16)
      OreDictionary.registerOre(
        ItemBackpack.oreDictionaryVal,
        new ItemStack(ProjectRedExploration.itemBackpack, 1, i)
      )

    OreDictionary.registerOre("oreRuby", OreDefs.ORERUBY.makeStack)
    OreDictionary.registerOre("oreSapphire", OreDefs.ORESAPPHIRE.makeStack)
    OreDictionary.registerOre("orePeridot", OreDefs.OREPERIDOT.makeStack)
    OreDictionary.registerOre("oreCopper", OreDefs.ORECOPPER.makeStack)
    OreDictionary.registerOre("oreTin", OreDefs.ORETIN.makeStack)
    OreDictionary.registerOre("oreSilver", OreDefs.ORESILVER.makeStack)
    OreDictionary.registerOre(
      "oreElectrotine",
      OreDefs.OREELECTROTINE.makeStack
    )

    OreDictionary.registerOre(
      "blockMarble",
      DecorativeStoneDefs.MARBLE.makeStack
    )
    OreDictionary.registerOre(
      "blockRuby",
      DecorativeStoneDefs.RUBYBLOCK.makeStack
    )
    OreDictionary.registerOre(
      "blockSapphire",
      DecorativeStoneDefs.SAPPHIREBLOCK.makeStack
    )
    OreDictionary.registerOre(
      "blockPeridot",
      DecorativeStoneDefs.PERIDOTBLOCK.makeStack
    )
    OreDictionary.registerOre(
      "blockCopper",
      DecorativeStoneDefs.COPPERBLOCK.makeStack
    )
    OreDictionary.registerOre(
      "blockTin",
      DecorativeStoneDefs.TINBLOCK.makeStack
    )
    OreDictionary.registerOre(
      "blockSilver",
      DecorativeStoneDefs.SILVERBLOCK.makeStack
    )
    OreDictionary.registerOre(
      "blockElectrotine",
      DecorativeStoneDefs.ELECTROTINEBLOCK.makeStack
    )
  }

  private def initGemToolRecipes() {

    /** Axes * */
    addAxeRecipe(new ItemStack(ProjectRedExploration.itemRubyAxe), "gemRuby")
    addAxeRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireAxe),
      "gemSapphire"
    )
    addAxeRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotAxe),
      "gemPeridot"
    )

    /** Hoes * */
    addHoeRecipe(new ItemStack(ProjectRedExploration.itemRubyHoe), "gemRuby")
    addHoeRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireHoe),
      "gemSapphire"
    )
    addHoeRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotHoe),
      "gemPeridot"
    )

    /** Pickaxe * */
    addPickaxeRecipe(
      new ItemStack(ProjectRedExploration.itemRubyPickaxe),
      "gemRuby"
    )
    addPickaxeRecipe(
      new ItemStack(ProjectRedExploration.itemSapphirePickaxe),
      "gemSapphire"
    )
    addPickaxeRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotPickaxe),
      "gemPeridot"
    )

    /** Shovel * */
    addShovelRecipe(
      new ItemStack(ProjectRedExploration.itemRubyShovel),
      "gemRuby"
    )
    addShovelRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireShovel),
      "gemSapphire"
    )
    addShovelRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotShovel),
      "gemPeridot"
    )

    /** Sword * */
    addSwordRecipe(
      new ItemStack(ProjectRedExploration.itemRubySword),
      "gemRuby"
    )
    addSwordRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireSword),
      "gemSapphire"
    )
    addSwordRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotSword),
      "gemPeridot"
    )

    /** Saw * */
    addSawRecipe(new ItemStack(ProjectRedExploration.itemGoldSaw), "ingotGold")
    addSawRecipe(new ItemStack(ProjectRedExploration.itemRubySaw), "gemRuby")
    addSawRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireSaw),
      "gemSapphire"
    )
    addSawRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotSaw),
      "gemPeridot"
    )

    /** Sickle * */
    addSickleRecipe(
      new ItemStack(ProjectRedExploration.itemWoodSickle),
      "plankWood"
    )
    addSickleRecipe(
      new ItemStack(ProjectRedExploration.itemStoneSickle),
      new ItemStack(Items.flint)
    )
    addSickleRecipe(
      new ItemStack(ProjectRedExploration.itemIronSickle),
      "ingotIron"
    )
    addSickleRecipe(
      new ItemStack(ProjectRedExploration.itemGoldSickle),
      "ingotGold"
    )
    addSickleRecipe(
      new ItemStack(ProjectRedExploration.itemRubySickle),
      "gemRuby"
    )
    addSickleRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireSickle),
      "gemSapphire"
    )
    addSickleRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotSickle),
      "gemPeridot"
    )
    addSickleRecipe(
      new ItemStack(ProjectRedExploration.itemDiamondSickle),
      "gemDiamond"
    )

    /** Armor * */
    addHelmetRecipe(
      new ItemStack(ProjectRedExploration.itemRubyHelmet),
      "gemRuby"
    )
    addChestplateRecipe(
      new ItemStack(ProjectRedExploration.itemRubyChestplate),
      "gemRuby"
    )
    addLeggingsRecipe(
      new ItemStack(ProjectRedExploration.itemRubyLeggings),
      "gemRuby"
    )
    addBootsRecipe(
      new ItemStack(ProjectRedExploration.itemRubyBoots),
      "gemRuby"
    )
    addHelmetRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireHelmet),
      "gemSapphire"
    )
    addChestplateRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireChestplate),
      "gemSapphire"
    )
    addLeggingsRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireLeggings),
      "gemSapphire"
    )
    addBootsRecipe(
      new ItemStack(ProjectRedExploration.itemSapphireBoots),
      "gemSapphire"
    )
    addHelmetRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotHelmet),
      "gemPeridot"
    )
    addChestplateRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotChestplate),
      "gemPeridot"
    )
    addLeggingsRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotLeggings),
      "gemPeridot"
    )
    addBootsRecipe(
      new ItemStack(ProjectRedExploration.itemPeridotBoots),
      "gemPeridot"
    )
  }

  private def addHelmetRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(new ShapedOreRecipe(o, "mmm", "m m", 'm': JChar, m))
  }

  private def addChestplateRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(
      new ShapedOreRecipe(o, "m m", "mmm", "mmm", 'm': JChar, m)
    )
  }

  private def addLeggingsRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(
      new ShapedOreRecipe(o, "mmm", "m m", "m m", 'm': JChar, m)
    )
  }

  private def addBootsRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(new ShapedOreRecipe(o, "m m", "m m", 'm': JChar, m))
  }

  private def addAxeRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        o,
        "mm",
        "ms",
        " s",
        'm': JChar,
        m,
        's': JChar,
        "stickWood"
      )
    )
  }

  private def addHoeRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        o,
        "mm",
        " s",
        " s",
        'm': JChar,
        m,
        's': JChar,
        "stickWood"
      )
    )
  }

  private def addPickaxeRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        o,
        "mmm",
        " s ",
        " s ",
        'm': JChar,
        m,
        's': JChar,
        "stickWood"
      )
    )
  }

  private def addShovelRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        o,
        "m",
        "s",
        "s",
        'm': JChar,
        m,
        's': JChar,
        "stickWood"
      )
    )
  }

  private def addSwordRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        o,
        "m",
        "m",
        "s",
        'm': JChar,
        m,
        's': JChar,
        "stickWood"
      )
    )
  }

  private def addSawRecipe(o: ItemStack, m: String) {
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        o,
        "srr",
        "sbb",
        's': JChar,
        "stickWood",
        'r': JChar,
        "rodStone",
        'b': JChar,
        m
      )
    )
  }

  private def addSickleRecipe(o: ItemStack, m: AnyRef) {
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        o,
        " m ",
        "  m",
        "sm ",
        's': JChar,
        "stickWood",
        'm': JChar,
        m
      )
    )
  }

  private def initEtcRecipes() {

    /** Wool Gin to string recipe * */
    GameRegistry.addRecipe(
      new ItemStack(Items.string, 4),
      "gw",
      'g': JChar,
      new ItemStack(
        ProjectRedExploration.itemWoolGin,
        1,
        OreDictionary.WILDCARD_VALUE
      ),
      'w': JChar,
      Blocks.wool
    )

    /** Item Barrel  * */
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        new ItemStack(ProjectRedExploration.blockBarrel),
        "lwl",
        "i i",
        "lll",
        'l': JChar,
        "logWood",
        'w': JChar,
        "slabWood",
        'i': JChar,
        "ingotIron"
      )
    )
  }

  private def initToolRecipes() {

    /** Wool Gin * */
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        new ItemStack(ProjectRedExploration.itemWoolGin),
        "sis",
        "sss",
        " s ",
        's': JChar,
        "stickWood",
        'i': JChar,
        PartDefs.IRONCOIL.makeStack
      )
    )

    /** Backpacks * */
    for (i <- 0 until 16) {
      GameRegistry.addRecipe(
        new ShapedOreRecipe(
          new ItemStack(ProjectRedExploration.itemBackpack, 1, i),
          "ccc",
          if (i == 0) "c c" else "cdc",
          "ccc",
          'c': JChar,
          PartDefs.WOVENCLOTH.makeStack,
          'd': JChar,
          Colors.apply(i).oreDict
        )
      )

      GameRegistry.addRecipe(
        new ShapelessOreNBTRecipe(
          new ItemStack(ProjectRedExploration.itemBackpack, 1, i),
          ItemBackpack.oreDictionaryVal,
          Colors.apply(i).oreDict
        ).setKeepNBT()
      )
    }

    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        new ItemStack(ProjectRedExploration.itemAthame),
        "s",
        "w",
        's': JChar,
        "ingotSilver",
        'w': JChar,
        "stickWood"
      )
    )
  }

  private def initWorldRecipes() {

    /** Marble brick * */
    GameRegistry.addRecipe(
      new ShapedOreRecipe(
        DecorativeStoneDefs.MARBLEBRICK.makeStack(4),
        "bb",
        "bb",
        'b': JChar,
        "blockMarble"
      )
    )

    /** Basalt brick * */
    GameRegistry.addRecipe(
      DecorativeStoneDefs.BASALTBRICK.makeStack(4),
      "bb",
      "bb",
      'b': JChar,
      DecorativeStoneDefs.BASALT.makeStack
    )

    /** Basalt * */
    addSmeltingRecipe(
      DecorativeStoneDefs.BASALTCOBBLE.makeStack,
      DecorativeStoneDefs.BASALT.makeStack
    )

    /** Ore Smelting* */
    addSmeltingRecipe(OreDefs.ORERUBY.makeStack, PartDefs.RUBY.makeStack)
    addSmeltingRecipe(
      OreDefs.ORESAPPHIRE.makeStack,
      PartDefs.SAPPHIRE.makeStack
    )
    addSmeltingRecipe(OreDefs.OREPERIDOT.makeStack, PartDefs.PERIDOT.makeStack)
    addSmeltingRecipe(
      OreDefs.ORECOPPER.makeStack,
      PartDefs.COPPERINGOT.makeStack
    )
    addSmeltingRecipe(OreDefs.ORETIN.makeStack, PartDefs.TININGOT.makeStack)
    addSmeltingRecipe(
      OreDefs.ORESILVER.makeStack,
      PartDefs.SILVERINGOT.makeStack
    )
    addSmeltingRecipe(
      OreDefs.OREELECTROTINE.makeStack,
      PartDefs.ELECTROTINE.makeStack
    )

    /** Storage blocks * */
    addStorageBlockRecipe(
      "gemRuby",
      PartDefs.RUBY.makeStack(9),
      "blockRuby",
      DecorativeStoneDefs.RUBYBLOCK.makeStack
    )
    addStorageBlockRecipe(
      "gemSapphire",
      PartDefs.SAPPHIRE.makeStack(9),
      "blockSapphire",
      DecorativeStoneDefs.SAPPHIREBLOCK.makeStack
    )
    addStorageBlockRecipe(
      "gemPeridot",
      PartDefs.PERIDOT.makeStack(9),
      "blockPeridot",
      DecorativeStoneDefs.PERIDOTBLOCK.makeStack
    )
    addStorageBlockRecipe(
      "ingotCopper",
      PartDefs.COPPERINGOT.makeStack(9),
      "blockCopper",
      DecorativeStoneDefs.COPPERBLOCK.makeStack
    )
    addStorageBlockRecipe(
      "ingotTin",
      PartDefs.TININGOT.makeStack(9),
      "blockTin",
      DecorativeStoneDefs.TINBLOCK.makeStack
    )
    addStorageBlockRecipe(
      "ingotSilver",
      PartDefs.SILVERINGOT.makeStack(9),
      "blockSilver",
      DecorativeStoneDefs.SILVERBLOCK.makeStack
    )
    addStorageBlockRecipe(
      "dustElectrotine",
      PartDefs.ELECTROTINE.makeStack(9),
      "blockElectrotine",
      DecorativeStoneDefs.ELECTROTINEBLOCK.makeStack
    )

    for (i <- 0 until DecorativeStoneDefs.values.size) {
      val s: DecorativeStoneDefs.StoneVal = DecorativeStoneDefs.values
        .apply(i)
        .asInstanceOf[DecorativeStoneDefs.StoneVal]
      addWallRecipe(
        new ItemStack(ProjectRedExploration.blockDecorativeWalls, 6, s.meta),
        s.makeStack
      )
    }
  }

  private def addSmeltingRecipe(in: ItemStack, out: ItemStack) {
    (RecipeLib.newSmeltingBuilder
      += new ItemIn(in)
      += new ItemOut(out)).registerResults()
  }

  private def addStorageBlockRecipe(
      itemOre: String,
      item: ItemStack,
      blockOre: String,
      block: ItemStack
  ) {
    (RecipeLib.newShapedBuilder <-> "xxxxxxxxx"
      += new OreIn(itemOre).to("x")
      += new ItemOut(block)).registerResult()
    (RecipeLib.newShapelessBuilder
      += new OreIn(blockOre)
      += new ItemOut(item)).registerResult()
  }

  private def addWallRecipe(o: ItemStack, m: ItemStack) {
    GameRegistry.addRecipe(o, "mmm", "mmm", 'm': JChar, m)
  }
}
