package uk.yanwenyuan.tfcsacks.common.container;

import net.dries007.tfc.common.container.ItemStackContainerProvider;

public class TFCSacksContainerProviders {

    public static final ItemStackContainerProvider BURLAP_SACK = new ItemStackContainerProvider(BurlapSackContainer::create);
    public static final ItemStackContainerProvider TOOL_SACK = new ItemStackContainerProvider(ToolSackContainer::create);
}
