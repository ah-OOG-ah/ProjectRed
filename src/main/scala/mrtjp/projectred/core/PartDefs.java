package mrtjp.projectred.core;

import mrtjp.projectred.ProjectRedCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.luaj.vm2.ast.Str;

public enum PartDefs {

    PLATE("plate"),
    CONDUCTIVEPLATE("conductive_plate"),
    WIREDPLATE("wired_plate"),
    BUNDLEDPLATE("bundled_plate"),
    ANODE("anode"),
    CATHODE("cathode"),
    POINTER("pointer"),
    SILICONCHIP("silicon_chip"),
    ENERGIZEDSILICONCHIP("energized_silicon_chip"),
    PLATFORMEDPLATE("platformed_plate"),
        
    REDINGOT("red_ingot"),
    SILICONBOULE("boule"),
    SILICON("silicon"),
    INFUSEDSILICON("infused_silicon"),
    ENERGIZEDSILICON("energized_silicon"),
    MOTOR("motor"),
    COPPERCOIL("copper_coil"),
    IRONCOIL("iron_coil"),
    GOLDCOIL("gold_coil"),

    WHITEILLUMAR("illumar0"),
    ORANGEILLUMAR("illumar1"),
    MAGENTAILLUMAR("illumar2"),
    LIGHTBLUEILLUMAR("illumar3"),
    YELLOWILLUMAR("illumar4"),
    LIMEILLUMAR("illumar5"),
    PINKILLUMAR("illumar6"),
    GREYILLUMAR("illumar7"),
    LIGHTGREYILLUMAR("illumar8"),
    CYANILLUMAR("illumar9"),
    PURPLEILLUMAR("illumar10"),
    BLUEILLUMAR("illumar11"),
    BROWNILLUMAR("illumar12"),
    GREENILLUMAR("illumar13"),
    REDILLUMAR("illumar14"),
    BLACKILLUMAR("illumar15"),

    WOVENCLOTH("cloth"),
    SAIL("sail"),

    RUBY("gemruby"),
    SAPPHIRE("gemsapphire"),
    PERIDOT("gemperidot"),

    REDIRONCOMPOUND("red_iron_comp"),
    SANDYCOALCOMPOUND("sand_coal_comp"),
    REDSILICONCOMPOUND("red_silicon_comp"),
    GLOWINGSILICONCOMPOUND("glow_silicon_comp"),

    NULLROUTINGCHIP("null_chip"),
    NULLUPGRADECHIP("null_upgrd"),
    CHIPUPGRADE_LX("upgrd_lx"),
    CHIPUPGRADE_LY("upgrd_ly"),
    CHIPUPGRADE_LZ("upgrd_lz"),
    CHIPUPGRADE_RX("upgrd_rx"),
    CHIPUPGRADE_RY("upgrd_ry"),
    CHIPUPGRADE_RZ("upgrd_rz"),

    COPPERINGOT("copper_ingot"),
    TININGOT("tin_ingot"),
    SILVERINGOT("silver_ingot"),
    ELECTROTINEINGOT("electrotine_ingot"),

    ELECTROTINE("electrotine_dust"),
    ELECTROTINEIRONCOMPOUND("electrotine_iron_comp"),
    ELECTROTINESILICONCOMPOUND("electrotine_silicon_comp"),
    ELECTROSILICON("electro_silicon");

    // Groups
    public static final PartDefs[] ILLUMARS = new PartDefs[]{
            WHITEILLUMAR,
            ORANGEILLUMAR,
            MAGENTAILLUMAR,
            LIGHTBLUEILLUMAR,
            YELLOWILLUMAR,
            LIMEILLUMAR,
            PINKILLUMAR,
            GREYILLUMAR,
            LIGHTGREYILLUMAR,
            CYANILLUMAR,
            PURPLEILLUMAR,
            BLUEILLUMAR,
            BROWNILLUMAR,
            GREENILLUMAR,
            REDILLUMAR,
            BLACKILLUMAR
    };

    public static final String oreDictDefinitionIllumar = "projredIllumar";
    public static final String oreDictDefinitionRedIngot = "ingotRedAlloy";

    public static ItemPart getItem() {
        return ProjectRedCore.itemPart;
    }

    String iconName;
    IIcon icon;

    PartDefs(String iconName) {
        this.iconName = iconName;
    }

    public void registerIcon(IIconRegister reg) {
        icon = reg.registerIcon("projectred:base/" + iconName);
    }

    // This used to override name(), but Java don't do that
    public String toString() {
        return iconName;
    }

    public ItemStack makeStack() {
        return makeStack(1);
    }
    public ItemStack makeStack(int i) {
        return new ItemStack(getItem(), i, ordinal());
    }
}
