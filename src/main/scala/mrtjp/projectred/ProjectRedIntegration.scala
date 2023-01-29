package mrtjp.projectred

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.{
  FMLInitializationEvent,
  FMLPostInitializationEvent,
  FMLPreInitializationEvent
}
import mrtjp.projectred.integration.{
  GateDefinition,
  IntegrationProxy,
  ItemPartGate
}
import net.minecraft.creativetab.CreativeTabs

@Mod(
  modid = "ProjRed|Integration",
  dependencies = "required-after:ProjRed|Core",
  modLanguage = "scala",
  acceptedMinecraftVersions = "[1.7.10]",
  name = "ProjectRed Integration",
  version = ProjectRedCore.VERSION
)
object ProjectRedIntegration {

  /** Multipart items * */
  var itemPartGate2: ItemPartGate = null

  var tabIntegration2 = new CreativeTabs("int") {
    override def getIconItemStack = GateDefinition.OR.makeStack
    override def getTabIconItem = getIconItemStack.getItem
  }

  @Mod.EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    IntegrationProxy.versionCheck()
    IntegrationProxy.preinit()
  }

  @Mod.EventHandler
  def init(event: FMLInitializationEvent) {
    IntegrationProxy.init()
  }

  @Mod.EventHandler
  def postInit(event: FMLPostInitializationEvent) {
    IntegrationProxy.postinit()
  }
}
