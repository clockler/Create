package com.simibubi.create.compat;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

import com.simibubi.create.AllItems;

public class CuriosCompat {
    public static final String curiosModId = "curios";
    public static final Boolean isCuriosLoaded = ModList.get().isLoaded(curiosModId);

    public static void enqueueIMC(InterModEnqueueEvent event) {
        if(isCuriosLoaded) {
            InterModComms.sendTo(curiosModId, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("head").setSize(1));
        }
    }

    public static boolean areGogglesEquipped(@Nonnull LivingEntity player)
    {
        if (isCuriosLoaded)
        {
            Optional<ImmutableTriple<String, Integer, ItemStack>> maybeDoesNothing = CuriosAPI.getCurioEquipped(AllItems.GOGGLES.get(), player);
            if (maybeDoesNothing.isPresent())
            {
                return true;
            }
        }
        return false;
    }
}