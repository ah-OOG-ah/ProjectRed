/*
 * Copyright (c) 2016.
 * Created by MrTJP.
 * All rights reserved.
 */
package mrtjp.projectred.core

import mrtjp.core.data.{SpecialConfigGui, TModGuiFactory}
import net.minecraft.client.gui.GuiScreen

class ProjectRedConfigGui(parent: GuiScreen)
    extends SpecialConfigGui(parent, "ProjRed|Core", Configurator.instance.config)
class GuiConfigFactory extends TModGuiFactory {
  override def mainConfigGuiClass() = classOf[ProjectRedConfigGui]
}
