package mrtjp.projectred

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.{
  FMLInitializationEvent,
  FMLPostInitializationEvent,
  FMLPreInitializationEvent
}
import mrtjp.projectred.compatibility.CompatibilityProxy

@Mod(
  modid = "ProjRed|Compatibility",
  dependencies = "required-after:ProjRed|Core;" +
    "after:ProjRed|Transmission;" +
    "after:ProjRed|Exploration;" +
    "after:ProjRed|Transportation;" +
    "after:TConstruct;" +
    "after:ComputerCraft",
  modLanguage = "scala",
  acceptedMinecraftVersions = "[1.7.10]",
  name = "ProjectRed Compatibility",
  version = ProjectRedCore.VERSION
)
object ProjectRedCompatibility {
  @Mod.EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    CompatibilityProxy.versionCheck()
    CompatibilityProxy.preinit()
  }

  @Mod.EventHandler
  def init(event: FMLInitializationEvent) {
    CompatibilityProxy.init()
  }

  @Mod.EventHandler
  def postInit(event: FMLPostInitializationEvent) {
    CompatibilityProxy.postinit()
  }
}
