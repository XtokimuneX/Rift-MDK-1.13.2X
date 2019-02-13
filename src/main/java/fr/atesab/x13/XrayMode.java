package fr.atesab.x13;

import java.util.List;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;

import net.minecraft.client.settings.KeyBinding;

import net.minecraft.util.EnumFacing;

import net.minecraft.util.math.BlockPos;

import net.minecraft.world.IBlockReader;

public class XrayMode implements SideRenderer {

	private static final List<XrayMode> MODES = Lists.newArrayList();

	private KeyBinding key;

	public XrayMode(String name, int keyCode) {
		this.key = new KeyBinding(name, keyCode, "key.categories.xray");
		MODES.add(this);
	}



	@Override
	public void shouldSideBeRendered(IBlockState state, IBlockReader reader, BlockPos pos, EnumFacing face,
			CallbackInfoReturnable<Boolean> ci) {

	}

}
