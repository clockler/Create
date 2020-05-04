package com.simibubi.create;

import com.tterrag.registrate.util.RegistryEntry;

import static com.simibubi.create.modules.Sections.SCHEMATICS;
import static com.simibubi.create.modules.Sections.KINETICS;

import com.simibubi.create.modules.contraptions.components.contraptions.mounted.CartAssemblerBlock;
import com.simibubi.create.modules.schematics.block.CreativeCrateBlock;
import com.simibubi.create.modules.schematics.block.SchematicTableBlock;
import com.simibubi.create.modules.schematics.block.SchematicannonBlock;

import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.client.renderer.RenderType;

public class AllBlocksNew {
	
	private static final CreateRegistrate REGISTRATE = Create.registrate();
	
	// Kinetic utilities
	static { REGISTRATE.startSection(KINETICS); }

	public static final RegistryEntry<CartAssemblerBlock> CART_ASSEMBLER = REGISTRATE.block("cart_assembler", CartAssemblerBlock::new)
			.initialProperties(() -> Blocks.PISTON)
			.properties((props) -> props.nonOpaque())
			.blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/" + ctx.getName()))))
			.addLayer(() -> RenderType::getCutout)
			.tag(BlockTags.RAILS)
			.simpleItem()
			.register();


	// Tools for strucuture movement and replication 
	static { REGISTRATE.startSection(SCHEMATICS); }
	
	public static final RegistryEntry<SchematicannonBlock> SCHEMATICANNON = REGISTRATE.block("schematicannon", SchematicannonBlock::new)
			.initialProperties(() -> Blocks.DISPENSER)
			.blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/" + ctx.getName() + "/base"))))
			.item()
				.model((ctx, prov) -> prov.blockItem(ctx.getEntry()::getBlock, "/item"))
				.build()
			.register();
	
	public static final RegistryEntry<CreativeCrateBlock> CREATIVE_CRATE = REGISTRATE.block("creative_crate", CreativeCrateBlock::new)
			.initialProperties(() -> Blocks.CHEST)
			.blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(ctx.getId())))
			.simpleItem()
			.register();
	
	public static final RegistryEntry<SchematicTableBlock> SCHEMATIC_TABLE = REGISTRATE.block("schematic_table", SchematicTableBlock::new)
			.initialProperties(() -> Blocks.LECTERN)
			.blockstate((ctx, prov) -> prov.horizontalBlock(ctx.getEntry(), prov.models().getExistingFile(ctx.getId()), 0))
			.simpleItem()
			.register();

	public static void register() {}
}
