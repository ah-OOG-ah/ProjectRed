package mrtjp.projectred.exploration;

import codechicken.microblock.BlockMicroMaterial;
import mrtjp.core.color.Colors;
import mrtjp.core.inventory.InvWrapper;
import mrtjp.core.world.BlockUpdateHandler;
import mrtjp.core.world.GenLogicSurface;
import mrtjp.core.world.GenLogicUniform;
import mrtjp.core.world.SimpleGenHandler;
import mrtjp.core.world.WorldGenCaveReformer;
import mrtjp.core.world.WorldGenClusterizer;
import mrtjp.core.world.WorldGenDecorator;
import mrtjp.core.world.WorldGenVolcanic;
import mrtjp.projectred.core.Configurator;
import mrtjp.projectred.core.IProxy;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.luaj.vm2.ast.Str;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static mrtjp.projectred.ProjectRedExploration.*;

public class ExplorationProxy_server implements IProxy {

    public final int guiIDBackpack = 1;

    @Override
    public void preinit() {}

    @Override
    public void init() {
        itemWoolGin = new ItemWoolGin();
        itemBackpack = new ItemBackpack();
        itemAthame = new ItemAthame();

        blockOres = new BlockOre();
        for (OreDefs o : OreDefs.values()) {
            blockOres.setHarvestLevel("pickaxe", o.harvest, o.ordinal());
        }

        blockDecoratives = new BlockDecoratives();
        for (DecorativeStoneDefs b : DecorativeStoneDefs.values())
            blockDecoratives.setHarvestLevel("pickaxe", b.harvest, b.ordinal());

        blockDecorativeWalls = new BlockDecorativeWalls();

        blockLily = new BlockLily();
        blockLily.addSingleTile(TileLily.class);
        itemLilySeed = new ItemLilySeeds();
        for (int i = 0; i < 16; ++i) {
            OreDictionary.registerOre(
                    Colors.values()[i].oreDict,
                    new ItemStack(itemLilySeed, 1, i)
            );
        }

        blockBarrel = new BlockBarrel();
        blockBarrel.addTile(TileBarrel.class, 0);

        toolMaterialRuby = EnumHelper.addToolMaterial("RUBY", 2, 512, 8.00f, 3.00f, 10);
        toolMaterialSapphire = EnumHelper.addToolMaterial("SAPPHIRE", 2, 512, 8.00f, 3.00f, 10);
        toolMaterialPeridot = EnumHelper.addToolMaterial("PERIDOT", 2, 512, 7.75f, 2.75f, 14);

        armorMatrialRuby =
                EnumHelper.addArmorMaterial("RUBY", 16, new int[]{3, 8, 6, 3}, 8);
        armorMatrialSapphire =
                EnumHelper.addArmorMaterial("SAPPHIRE", 16, new int[]{3, 8, 6, 3}, 8);
        armorMatrialPeridot =
                EnumHelper.addArmorMaterial("PERIDOT", 14, new int[]{3, 8, 6, 3}, 10);

        itemRubyAxe = new ItemGemAxe(ToolDefs.RUBYAXE);
        itemSapphireAxe = new ItemGemAxe(ToolDefs.SAPPHIREAXE);
        itemPeridotAxe = new ItemGemAxe(ToolDefs.PERIDOTAXE);

        itemRubyHoe = new ItemGemHoe(ToolDefs.RUBYHOE);
        itemSapphireHoe = new ItemGemHoe(ToolDefs.SAPPHIREHOE);
        itemPeridotHoe = new ItemGemHoe(ToolDefs.PERIDOTHOE);

        itemRubyPickaxe = new ItemGemPickaxe(ToolDefs.RUBYPICKAXE);
        itemSapphirePickaxe = new ItemGemPickaxe(ToolDefs.SAPPHIREPICKAXE);
        itemPeridotPickaxe = new ItemGemPickaxe(ToolDefs.PERIDOTPICKAXE);

        itemRubyShovel = new ItemGemShovel(ToolDefs.RUBYSHOVEL);
        itemSapphireShovel = new ItemGemShovel(ToolDefs.SAPPHIRESHOVEL);
        itemPeridotShovel = new ItemGemShovel(ToolDefs.PERIDOTSHOVEL);

        itemRubySword = new ItemGemSword(ToolDefs.RUBYSWORD);
        itemSapphireSword = new ItemGemSword(ToolDefs.SAPPHIRESWORD);
        itemPeridotSword = new ItemGemSword(ToolDefs.PERIDOTSWORD);

        itemGoldSaw = new ItemGemSaw(ToolDefs.GOLDSAW);
        itemRubySaw = new ItemGemSaw(ToolDefs.RUBYSAW);
        itemSapphireSaw = new ItemGemSaw(ToolDefs.SAPPHIRESAW);
        itemPeridotSaw = new ItemGemSaw(ToolDefs.PERIDOTSAW);

        itemWoodSickle = new ItemGemSickle(ToolDefs.WOODSICKLE);
        itemStoneSickle = new ItemGemSickle(ToolDefs.STONESICKLE);
        itemIronSickle = new ItemGemSickle(ToolDefs.IRONSICKLE);
        itemGoldSickle = new ItemGemSickle(ToolDefs.GOLDSICKLE);
        itemRubySickle = new ItemGemSickle(ToolDefs.RUBYSICKLE);
        itemSapphireSickle = new ItemGemSickle(ToolDefs.SAPPHIRESICKLE);
        itemPeridotSickle = new ItemGemSickle(ToolDefs.PERIDOTSICKLE);
        itemDiamondSickle = new ItemGemSickle(ToolDefs.DIAMONDSICKLE);

        itemRubyHelmet = new ItemGemArmor(ArmorDefs.RUBYHELMET, 0);
        itemRubyChestplate = new ItemGemArmor(ArmorDefs.RUBYCHESTPLATE, 1);
        itemRubyLeggings = new ItemGemArmor(ArmorDefs.RUBYLEGGINGS, 2);
        itemRubyBoots = new ItemGemArmor(ArmorDefs.RUBYBOOTS, 3);

        itemSapphireHelmet = new ItemGemArmor(ArmorDefs.SAPPHIREHELMET, 0);
        itemSapphireChestplate = new ItemGemArmor(ArmorDefs.SAPPHIRECHESTPLATE, 1);
        itemSapphireLeggings = new ItemGemArmor(ArmorDefs.SAPPHIRELEGGINGS, 2);
        itemSapphireBoots = new ItemGemArmor(ArmorDefs.SAPPHIREBOOTS, 3);

        itemPeridotHelmet = new ItemGemArmor(ArmorDefs.PERIDOTHELMET, 0);
        itemPeridotChestplate = new ItemGemArmor(ArmorDefs.PERIDOTCHESTPLATE, 1);
        itemPeridotLeggings = new ItemGemArmor(ArmorDefs.PERIDOTLEGGINGS, 2);
        itemPeridotBoots = new ItemGemArmor(ArmorDefs.PERIDOTBOOTS, 3);

        for (DecorativeStoneDefs s : DecorativeStoneDefs.values())
            BlockMicroMaterial.createAndRegister(blockDecoratives, s.ordinal());

        ExplorationRecipes.initRecipes();

        // World Gen

        // Ruby
        if (Configurator.gen_Ruby) {
            final GenLogicUniform logic = new GenLogicUniform();
            logic.name = "pr_ruby";
            logic.resistance = 8 + Configurator.gen_Ruby_resistance;
            logic.allowRetroGen = Configurator.gen_Ruby_retro;
            logic.minY = 12;
            logic.maxY = 20;
            logic.attempts = 1;
            final WorldGenClusterizer gen = new WorldGenClusterizer();
            gen.cluster = new HashSet<>(Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockOres, OreDefs.ORERUBY.ordinal()), 1)));
            gen.clusterSize = 5;
            gen.material = new HashSet<>(Collections.singleton(new ImmutablePair<>(Blocks.stone, 0)));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);
        }

        // Sapphire
        if (Configurator.gen_Sapphire) {
            final GenLogicUniform logic = new GenLogicUniform();
            logic.name = "pr_sapphire";
            logic.resistance = 8 + Configurator.gen_Sapphire_resistance;
            logic.allowRetroGen = Configurator.gen_Sapphire_retro;
            logic.minY = 12;
            logic.maxY = 20;
            logic.attempts = 1;
            final WorldGenClusterizer gen = new WorldGenClusterizer();
            gen.cluster = new HashSet<>(Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockOres, OreDefs.ORESAPPHIRE.ordinal()), 1)));
            gen.clusterSize = 5;
            gen.material = new HashSet<>(Collections.singleton(new ImmutablePair<>(Blocks.stone, 0)));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);
        }

        // Peridot
        if (Configurator.gen_Peridot) {
            final GenLogicUniform logic = new GenLogicUniform();
            logic.name = "pr_peridot";
            logic.resistance = 8 + Configurator.gen_Peridot_resistance;
            logic.allowRetroGen = Configurator.gen_Peridot_retro;
            logic.minY = 16;
            logic.maxY = 28;
            logic.attempts = 2;
            final WorldGenClusterizer gen = new WorldGenClusterizer();
            gen.cluster = new HashSet<>(Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockOres, OreDefs.OREPERIDOT.ordinal()), 1)));
            gen.clusterSize = 5;
            gen.material = new HashSet<>(Collections.singleton(new ImmutablePair<>(Blocks.stone, 0)));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);
        }

        // Marble
        if (Configurator.gen_MarbleCave) {
            final GenLogicUniform logic = new GenLogicUniform();
            logic.name = "pr_marblecave";
            logic.resistance = 4 + Configurator.gen_MarbleCave_resistance;
            logic.allowRetroGen = Configurator.gen_MarbleCave_retro;
            logic.dimensionBlacklist = false;
            logic.dimensions = new HashSet<>(Collections.singleton(0));
            logic.minY = 32;
            logic.maxY = 64;
            final WorldGenCaveReformer gen = new WorldGenCaveReformer();
            gen.cluster = new HashSet<>(
                    Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockDecoratives, DecorativeStoneDefs.MARBLE.ordinal()), 1))
            );
            gen.clusterSize = 4096;
            gen.material = new HashSet<>(Collections.singleton(new ImmutablePair<>(Blocks.stone, 0)));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);
        }

        // Volcano
        if (Configurator.gen_Volcano) {
            final GenLogicUniform logic = new GenLogicUniform();
            logic.name = "pr_volcano";
            logic.resistance = 16 + Configurator.gen_Volcano_resistance;
            logic.allowRetroGen = Configurator.gen_Volcano_retro;
            logic.dimensionBlacklist = false;
            logic.dimensions = new HashSet<>(Collections.singleton(0));
            logic.minY = 0;
            logic.maxY = 64;
            final WorldGenVolcanic gen = new WorldGenVolcanic();
            gen.ashCluster = new HashSet<>(
                    Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockDecoratives, DecorativeStoneDefs.BASALT.ordinal()), 1))
            );
            gen.conduitCluster = gen.ashCluster;
            gen.liq = new ImmutablePair<>(Blocks.lava, 0);
            gen.materialStart = new HashSet<>(Collections.singleton(gen.liq));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);
        }

        // Lily
        if (Configurator.gen_Lily) {
            final GenLogicSurface logic = new GenLogicSurface();
            logic.name = "pr_lily";
            logic.resistance = 8 + Configurator.gen_Lily_resistance;
            logic.allowRetroGen = Configurator.gen_Lily_retro;
            final WorldGenDecorator gen = new WorldGenDecorator();
            gen.cluster = new HashSet<>(Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockLily, 0), 1)));
            gen.material = new HashSet<>(Collections.singleton(new ImmutablePair<>(Blocks.air, 0)));
            gen.soil = new HashSet<>(Arrays.asList(
                    new ImmutablePair<>(Blocks.grass, 0),
                    new ImmutablePair<>(Blocks.dirt, 0)
            ));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);
        }

        // Copper
        if (Configurator.gen_Copper) {
            final GenLogicUniform logic = new GenLogicUniform();
            logic.name = "pr_copper";
            logic.resistance = Configurator.gen_Copper_resistance;
            logic.allowRetroGen = Configurator.gen_Copper_retro;
            logic.minY = 0;
            logic.maxY = 64;
            logic.attempts = 16;
            final WorldGenClusterizer gen = new WorldGenClusterizer();
            gen.cluster = new HashSet<>(Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockOres, OreDefs.ORECOPPER.ordinal()), 1)));
            gen.clusterSize = 8;
            gen.material = new HashSet<>(Collections.singleton(new ImmutablePair<>(Blocks.stone, 0)));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);

        }

        // Tin
        if (Configurator.gen_Tin) {
            final GenLogicUniform logic = new GenLogicUniform();
            logic.name = "pr_tin";
            logic.resistance = Configurator.gen_Tin_resistance;
            logic.allowRetroGen = Configurator.gen_Tin_retro;
            logic.minY = 0;
            logic.maxY = 48;
            logic.attempts = 8;
            final WorldGenClusterizer gen = new WorldGenClusterizer();
            gen.cluster = new HashSet<>(Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockOres, OreDefs.ORETIN.ordinal()), 1)));
            gen.clusterSize = 8;
            gen.material = new HashSet<>(Collections.singleton(new ImmutablePair<>(Blocks.stone, 0)));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);

        }

        // Silver
        if (Configurator.gen_Silver) {
            final GenLogicUniform logic = new GenLogicUniform();
            logic.name = "pr_silver";
            logic.resistance = Configurator.gen_Silver_resistance;
            logic.allowRetroGen = Configurator.gen_Silver_retro;
            logic.minY = 0;
            logic.maxY = 32;
            logic.attempts = 4;
            final WorldGenClusterizer gen = new WorldGenClusterizer();
            gen.cluster = new HashSet<>(Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockOres, OreDefs.ORESILVER.ordinal()), 1)));
            gen.clusterSize = 4;
            gen.material = new HashSet<>(Collections.singleton(new ImmutablePair<>(Blocks.stone, 0)));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);
        }

        // Electrotine
        if (Configurator.gen_Electrotine) {
            final GenLogicUniform logic = new GenLogicUniform();
            logic.name = "pr_electrotine";
            logic.resistance = Configurator.gen_Electrotine_resistance;
            logic.allowRetroGen = Configurator.gen_Electrotine_retro;
            logic.minY = 0;
            logic.maxY = 16;
            logic.attempts = 4;
            final WorldGenClusterizer gen = new WorldGenClusterizer();
            gen.cluster = new HashSet<>(Collections.singleton(new ImmutablePair<>(new ImmutablePair<>(blockOres, OreDefs.OREELECTROTINE.ordinal()), 1)));
            gen.clusterSize = 8;
            gen.material = new HashSet<>(Collections.singleton(new ImmutablePair<>(Blocks.stone, 0)));
            logic.gen = gen;
            SimpleGenHandler.registerStructure(logic);
        }
    }

    @Override
    public void postinit() {
        if (Configurator.gen_SpreadingMoss)
            BlockUpdateHandler.register(MossSpreadHandler$.MODULE$);

        InvWrapper.register(BarrelInvWrapper$.MODULE$);
    }

    @Override
    public String version() {
        return  "@VERSION@";
    }

    @Override
    public String build() {
        return  "@BUILD_NUMBER@";
    }
}
