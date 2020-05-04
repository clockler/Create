package com.simibubi.create.modules.contraptions.goggle;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.gui.ScreenElementRenderer;
import com.simibubi.create.compat.CuriosCompat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class GoggleOverlayRenderer {

	private static boolean areGogglesEquipped(ClientPlayerEntity player)
	{
		if (CuriosCompat.areGogglesEquipped(player))
		{
			return true;
		}
		else
		{
			Item headItem = player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem();
			if (headItem == AllItems.GOGGLES.get())
			{
				return true;
			}
		}
		return false;
	}

	@SubscribeEvent
	public static void lookingAtBlocksThroughGogglesShowsTooltip(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.HOTBAR)
			return;

		RayTraceResult objectMouseOver = Minecraft.getInstance().objectMouseOver;
		if (!(objectMouseOver instanceof BlockRayTraceResult))
			return;

		BlockRayTraceResult result = (BlockRayTraceResult) objectMouseOver;
		Minecraft mc = Minecraft.getInstance();
		ClientWorld world = mc.world;
		BlockPos pos = result.getPos();
		TileEntity te = world.getTileEntity(pos);

		boolean goggleInformation = te instanceof IHaveGoggleInformation;
		boolean hoveringInformation = te instanceof IHaveHoveringInformation;

		if (!goggleInformation && !hoveringInformation)
			return;

		List<String> tooltip = new ArrayList<>();

		if (goggleInformation && areGogglesEquipped(mc.player)) {
			IHaveGoggleInformation gte = (IHaveGoggleInformation) te;
			if (!gte.addToGoggleTooltip(tooltip, mc.player.isSneaking()))
				goggleInformation = false;
		}

		if (hoveringInformation) {
			boolean goggleAddedInformation = !tooltip.isEmpty();
			if (goggleAddedInformation)
				tooltip.add("");
			IHaveHoveringInformation hte = (IHaveHoveringInformation) te;
			if (!hte.addToTooltip(tooltip, mc.player.isSneaking()))
				hoveringInformation = false;
			if (goggleAddedInformation && !hoveringInformation)
				tooltip.remove(tooltip.size() - 1);
		}

		if (!goggleInformation && !hoveringInformation)
			return;
		if (tooltip.isEmpty())
			return;

		GlStateManager.pushMatrix();
		Screen tooltipScreen = new Screen(null) {

			@Override
			public void init(Minecraft mc, int width, int height) {
				this.minecraft = mc;
				this.itemRenderer = mc.getItemRenderer();
				this.font = mc.fontRenderer;
				this.width = width;
				this.height = height;
			}

		};

		tooltipScreen.init(mc, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
		tooltipScreen.renderTooltip(tooltip, tooltipScreen.width / 2, tooltipScreen.height / 2);
		ItemStack item = AllItems.GOGGLES.asStack();
		ScreenElementRenderer.render3DItem(() -> {
			GlStateManager.translated(tooltipScreen.width / 2 + 10, tooltipScreen.height / 2 - 16, 0);
			return item;
		});
		GlStateManager.popMatrix();

	}

}
