package mrtjp.projectred.exploration

import cpw.mods.fml.common.registry.GameRegistry
import mrtjp.core.block._
import mrtjp.projectred.ProjectRedExploration
import net.minecraft.block.BlockWall
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.IIcon
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.EnumPlantType

import java.util

class BlockDecorativeWalls extends BlockWall(Blocks.stone) {
  setBlockName("projectred.exploration.stonewalls")
  setCreativeTab(ProjectRedExploration.tabExploration)

  // block registration
  GameRegistry.registerBlock(
    this,
    classOf[ItemBlockCore],
    "projectred.exploration.stonewalls"
  )

  override def getIcon(side: Int, meta: Int) = {
    if (DecorativeStoneDefs.isDefinedAt(meta)) DecorativeStoneDefs(meta).icon
    else super.getIcon(side, meta)
  }

  override def getSubBlocks(item: Item, tab: CreativeTabs, list: util.List[ItemStack]) {
    for (s <- DecorativeStoneDefs.values)
      list.add(new ItemStack(ProjectRedExploration.blockDecorativeWalls, 1, s.meta))
  }

  override def canConnectWallTo(w: IBlockAccess, x: Int, y: Int, z: Int) = {
    val b = w.getBlock(x, y, z)
    if (b != this && b != Blocks.fence_gate)
      b != null && b.getMaterial.isOpaque && b.renderAsNormalBlock && b.getMaterial != Material.gourd
    else true
  }

  override def canPlaceTorchOnTop(w: World, x: Int, y: Int, z: Int) =
    super.canPlaceTorchOnTop(w, x, y, z) || w.getBlock(x, y, z) == this
}

class BlockLily
    extends InstancedBlock("projectred.exploration.lily", Material.plants)
    with TPlantBlock {
  val soil = Seq(Blocks.grass, Blocks.dirt)

  override def initialCanStay(w: World, x: Int, y: Int, z: Int) =
    soil.contains(w.getBlock(x, y - 1, z))

  override def canBlockStay(w: World, x: Int, y: Int, z: Int) =
    initialCanStay(w, x, y, z)

  override def getPlantType(w: IBlockAccess, x: Int, y: Int, z: Int) =
    EnumPlantType.Plains
}
