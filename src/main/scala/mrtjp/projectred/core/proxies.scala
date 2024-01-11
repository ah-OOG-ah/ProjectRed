package mrtjp.projectred.core

import codechicken.lib.packet.PacketCustom
import mrtjp.projectred.ProjectRedCore._
import mrtjp.projectred.core.libmc.recipe.RecipeLib

class CoreProxy_server extends IProxy {
  def preinit() {}

  def init() {
    PacketCustom.assignHandler(CoreSPH.channel, CoreSPH)

    itemPart = new ItemPart
    itemDrawPlate = new ItemDrawPlate
    itemScrewdriver = new ItemScrewdriver
    itemWireDebugger = new ItemWireDebugger
    itemDataCard = new ItemDataCard

    RecipeLib.loadLib()
    CoreRecipes.initCoreRecipes()
  }

  def postinit() {}

  override def version = "@VERSION@"
  override def build = "@BUILD_NUMBER@"
}

object CoreProxy extends CoreProxy_client
