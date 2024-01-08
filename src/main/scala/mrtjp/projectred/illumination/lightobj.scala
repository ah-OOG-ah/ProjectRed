package mrtjp.projectred.illumination

import codechicken.lib.render.CCModel
import codechicken.lib.vec._
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.client.IItemRenderer.ItemRenderType._

import scala.collection.JavaConversions._

object LightObjFixture extends LightObject {
  val bounds = bakedBoxes(
    new Cuboid6(3.5 / 16d, 0, 3.5 / 16d, 12.5 / 16d, 6.5 / 16d, 12.5 / 16d)
  )
  val lBounds = bakedBoxes(
    new Cuboid6(4 / 16d, 1.5 / 16, 4 / 16d, 12 / 16d, 6.5 / 16d, 12 / 16d)
  )

  var icon: IIcon = _

  val bulbModels = new Array[CCModel](6)
  val chassiModels = new Array[CCModel](6)

  override def getItemName = "projectred.illumination.fixture"
  override def getType = "pr_fixture"

  override def getBounds(side: Int) = bounds(side)
  override def getLBounds(side: Int) = lBounds(side)

  override def getModelBulb(side: Int) = bulbModels(side)
  override def getModelChassi(side: Int) = chassiModels(side)

  override def getIcon = icon
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister) {
    icon = reg.registerIcon("projectred:lighting/fixture")
  }

  override def loadModels() {
    val models = parseModel("fixture")
    val chassi = models.get("chassi")
    val bulb = models.get("bulb")

    for (s <- 0 until 6) {
      bulbModels(s) = bakeCopy(s, bulb)
      chassiModels(s) = bakeCopy(s, chassi)
    }
  }
}

object LightObjFallout extends LightObject {
  val bounds = bakedBoxes(
    new Cuboid6(2 / 16d, 0, 2 / 16d, 14 / 16d, 11 / 16d, 14 / 16d)
  )
  val lBounds = bakedBoxes(
    new Cuboid6(4 / 16d, 1.5 / 16, 4 / 16d, 12 / 16d, 10 / 16d, 12 / 16d)
      .expand(-0.002)
  )

  var icon: IIcon = _

  val bulbModels = new Array[CCModel](6)
  val chassiModels = new Array[CCModel](6)

  override def getItemName = "projectred.illumination.cagelamp"
  override def getType = "pr_cagelamp"

  override def getBounds(side: Int) = bounds(side)
  override def getLBounds(side: Int) = lBounds(side)

  override def getModelBulb(side: Int) = bulbModels(side)
  override def getModelChassi(side: Int) = chassiModels(side)

  override def getIcon = icon
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister) {
    icon = reg.registerIcon("projectred:lighting/fallout")
  }

  override def loadModels() {
    val models = parseModel("fallout")
    val chassi = models.get("chassi")
    val bulb = models.get("bulb")

    for (s <- 0 until 6) {
      bulbModels(s) = bakeCopy(s, bulb)
      chassiModels(s) = bakeCopy(s, chassi)
    }
  }
}

object LightObjCage extends LightObject {
  val bounds = bakedBoxes(
    new Cuboid6(3.5 / 16d, 0, 3.5 / 16d, 12.5 / 16d, 12 / 16d, 12.5 / 16d)
  )
  val lBounds = bakedBoxes(
    new Cuboid6(
      4.5 / 16d,
      1.5 / 16,
      4.5 / 16d,
      11.5 / 16d,
      11.5 / 16d,
      11.5 / 16d
    )
  )

  var icon: IIcon = _

  val bulbModels = new Array[CCModel](6)
  val chassiModels = new Array[CCModel](6)

  override def getItemName = "projectred.illumination.cagelamp2"
  override def getType = "pr_cagelamp2"

  override def getBounds(side: Int) = bounds(side)
  override def getLBounds(side: Int) = lBounds(side)

  override def getModelBulb(side: Int) = bulbModels(side)
  override def getModelChassi(side: Int) = chassiModels(side)

  override def getIcon = icon
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister) {
    icon = reg.registerIcon("projectred:lighting/cage_lamp")
  }

  override def loadModels() {
    val models = parseModel("cagelamp")
    val chassi = models.get("chassi")
    val bulb = models.get("bulb")

    for (s <- 0 until 6) {
      bulbModels(s) = bakeCopy(s, bulb)
      chassiModels(s) = bakeCopy(s, chassi)
    }
  }
}
