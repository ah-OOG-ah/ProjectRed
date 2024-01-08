package mrtjp.projectred.exploration;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import mrtjp.core.block.TPlantTile;
import mrtjp.core.color.Colors;
import mrtjp.core.math.MathLib;
import mrtjp.core.world.WorldLib;
import mrtjp.projectred.ProjectRedExploration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static mrtjp.core.color.Colors.WHITE;
import static mrtjp.core.color.Colors.YELLOW;
import static mrtjp.core.color.Colors.BLUE;
import static mrtjp.core.color.Colors.RED;
import static mrtjp.core.color.Colors.BLACK;
import static net.minecraftforge.common.BiomeDictionary.Type.BEACH;
import static net.minecraftforge.common.BiomeDictionary.Type.DEAD;
import static net.minecraftforge.common.BiomeDictionary.Type.DENSE;
import static net.minecraftforge.common.BiomeDictionary.Type.DRY;
import static net.minecraftforge.common.BiomeDictionary.Type.END;
import static net.minecraftforge.common.BiomeDictionary.Type.HOT;
import static net.minecraftforge.common.BiomeDictionary.Type.LUSH;
import static net.minecraftforge.common.BiomeDictionary.Type.NETHER;
import static net.minecraftforge.common.BiomeDictionary.Type.OCEAN;
import static net.minecraftforge.common.BiomeDictionary.Type.RIVER;
import static net.minecraftforge.common.BiomeDictionary.Type.SANDY;
import static net.minecraftforge.common.BiomeDictionary.Type.SPARSE;
import static net.minecraftforge.common.BiomeDictionary.Type.SWAMP;
import static net.minecraftforge.common.BiomeDictionary.Type.WASTELAND;
import static net.minecraftforge.common.BiomeDictionary.Type.WATER;
import static net.minecraftforge.common.BiomeDictionary.Type.WET;
import static net.minecraftforge.common.BiomeDictionary.getTypesForBiome;

public class TileLily extends TPlantTile {

    public static final List<Cuboid6> bounds = new ArrayList<>();

    static {
        final List<Double> gains = Arrays.asList(2 / 16d, 2 / 16d, 2 / 16d, 2 / 16d, 2 / 16d, 0.0d, 2 / 16d, 2 / 16d);
        for (int i = 0; i < 8; ++i) {
            bounds.add(new Cuboid6(
                0.5f - 0.2f,
                0.0f,
                0.5f - 0.2f,
                0.5f + 0.2f,
                gains.subList(0, i + 1).stream().reduce(Double::sum).orElse(0D),
                0.5f + 0.2f
           ));
        }
    }

    static final List<Pair<Integer, Integer>> rarity = Arrays.asList(
        new ImmutablePair<>(WHITE().ordinal(), 17),
        new ImmutablePair<>(YELLOW().ordinal(), 25),
        new ImmutablePair<>(BLUE().ordinal(), 13),
        new ImmutablePair<>(RED().ordinal(), 35),
        new ImmutablePair<>(BLACK().ordinal(), 10)
    );

    // The first four bits are the meta, and the last four are the growth
    // mmmm gggg
    public byte phase = 0;
    public boolean pollinated = false;

    public int meta() {
        return phase >> 4 & 0xf;
    }
    public void setMeta(int m) {
        phase = (byte) (phase & 0xf | m << 4);
    }
    public int growth() {
        return phase & 0xf;
    }
    public void setGrowth(int g) {
        phase = (byte) (phase & 0xf0 | g);
    }

    @Override
    public Block getBlock() {
        return ProjectRedExploration.blockLily();
    }

    @Override
    public void save(NBTTagCompound tag) {
        super.save(tag);
        tag.setByte("phase", phase);
    }

    private boolean isNewWorldgen = true;

    @Override
    public void load(NBTTagCompound tag) {
        super.load(tag);
        phase = tag.getByte("phase");
        isNewWorldgen = false;
    }

    @Override
    public void writeDesc(MCDataOutput out) {
        super.writeDesc(out);
        out.writeByte(phase);
    }

    @Override
    public void readDesc(MCDataInput in) {
        super.readDesc(in);
        phase = in.readByte();
    }

    @Override
    public void read(MCDataInput in, int key) {
        if (key == 1) {
            if (world().isRemote) {
                phase = in.readByte();
                markRender();
            }
        } else {
            super.read(in, key);
        }
    }

    public void sendPhaseUpdate() {
        streamToSend(writeStream(1).writeByte(phase)).sendToChunk();
    }

    public void setupPlanted(int m) {
        setMeta(m);
        setGrowth(0);
    }

    @Override
    public void validate() {
        super.validate();
        if (!world().isRemote && isNewWorldgen) {
            setGrowth(7);
            setMeta(MathLib.weightedRandom(TileLily.rarity));
        }
    }

    @Override
    public void onNeighborChange(Block b) {
        super.onNeighborChange(b);
        dropIfCantStay();
    }

    @Override
    public Cuboid6 getBlockBounds() {
        return TileLily.bounds.get((growth()));
    }

    @Override
    public List<ItemStack> addHarvestContents(List<ItemStack> ist) {
        if (growth() == 7) {
            final int count = MathLib.weightedRandom(Arrays.asList(
                new ImmutablePair<>(0, 10),
                new ImmutablePair<>(1, 55),
                new ImmutablePair<>(2, 30),
                new ImmutablePair<>(3, 5)
            ));

            if (count > 0) {
                ist.add(new ItemStack(ProjectRedExploration.itemLilySeed(), count, meta()));
            }
        } else {
            if (world().rand.nextDouble() < growth() / 7.0d) {
                ist.add(new ItemStack(ProjectRedExploration.itemLilySeed(), 1, meta()));
            }
        }

        return ist;
    }

    @Override
    public void randomTick(Random rand) {
        if (!world().isRemote) {
            boolean updated = tickGrowth(rand);
            updated |= tickPollination(rand);
            if (updated) {
                markDirty();
                sendPhaseUpdate();
            }
        }
    }

    public boolean tickPollination(Random rand) {
        if (growth() == 7 || pollinated) return false;

        final int delta = (int) (WorldLib.getWindSpeed(world(), xCoord, yCoord, zCoord) * 10);
        final int dx = MathLib.randomFromIntRange(-delta, delta, rand);
        final int dy = MathLib.randomFromIntRange(-delta, delta, rand);
        final int dz = MathLib.randomFromIntRange(-delta, delta, rand);

        TileEntity p = world().getTileEntity(xCoord + dx, yCoord + dy, zCoord + dz);
        if (p instanceof TileLily) {
            if (((TileLily) p).growth() == 7) {
                final Colors.Color mix = Colors.mcMix((Colors.Color) Colors.apply(meta()), (Colors.Color) Colors.apply(((TileLily) p).meta()));
                if (mix != null) {
                    setMeta(mix.ordinal());
                    pollinated = true;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean applyBonemeal() {
        if (growth() < 7) {
            if (!world().isRemote) {
                setGrowth(growth() + 1);
                sendPhaseUpdate();
            }
            return true;
        }
        return false;
    }

    public boolean tickGrowth(Random rand) {
        if (growth() == 7) return false;

        // TODO refine temporary growth algorithm
        if (rand.nextInt(2) != 0) return false;

        double growthChance = 1.0d;
        growthChance *= getPlantLight();
        growthChance *= getSoilSaturation();

        if (rand.nextDouble() < growthChance) {
            setGrowth(growth() + 1);
            return true;
        }
        return false;
    }

    public double getPlantLight() {
        return Math.max(
                WorldLib.getSkyLightValue(world(), xCoord, yCoord, zCoord) / 15.0d * 0.99d,
                WorldLib.getBlockLightValue(world(), xCoord, yCoord, zCoord) / 15.0d * 0.66d
        );
    }

    public double getSoilSaturation() {
        double sat = 0.0d;
        for (int dx = -4; dx <= 4; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -4; dz <= 4; ++dz) {
                    if (world().getBlock(xCoord + dx, yCoord + dy, zCoord + dz) == Blocks.water)
                        sat += (1.0d - new Vector3(dx, 0, dz).mag() / 5.66d) * 0.25d;
                }
            }
        }

        final BiomeDictionary.Type[] tags = getTypesForBiome(world().getBiomeGenForCoords(xCoord, zCoord));
        // Default value is 0.0D
        final Map<BiomeDictionary.Type, Double> satMap = new HashMap<>();
        satMap.put(SPARSE, -0.02D);
        satMap.put(HOT, -0.05D);
        satMap.put(DRY, -0.15D);
        satMap.put(DEAD, -0.04D);
        satMap.put(SANDY, -0.06D);
        satMap.put(WASTELAND, -0.09D);
        satMap.put(NETHER, -0.11D);
        satMap.put(END, -0.03D);
        satMap.put(DENSE, 0.02D);
        satMap.put(WET, 0.15D);
        satMap.put(LUSH, 0.04D);
        satMap.put(OCEAN, 0.13D);
        satMap.put(RIVER, 0.09D);
        satMap.put(WATER, 0.16D);
        satMap.put(SWAMP, 0.01D);
        satMap.put(BEACH, 0.08D);

        sat += Arrays.stream(tags).map(satMap::get).reduce(Double::sum).orElse(0D);

        if (world().isRaining())
            sat += 0.50d * world().rainingStrength;
        else if (world().prevRainingStrength > 0)
            sat += 0.20d * world().prevRainingStrength;

        return Math.min(1.0d, Math.max(sat, 0.0d));
    }
}
