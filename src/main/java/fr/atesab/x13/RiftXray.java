package fr.atesab.x13;

import org.apache.commons.lang3.ArrayUtils;
import org.dimdev.rift.listener.client.KeybindHandler;
import org.dimdev.rift.listener.client.OverlayRenderer;
import org.dimdev.riftloader.listener.InitializationListener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

/**
 * A class to interact between Xray and Rift
 */
public class RiftXray implements KeybindHandler, InitializationListener,  BuildAPI {
	private XrayMain mod;

	public RiftXray() {
		if (!XrayMain.isAPIRegister())
			this.mod = XrayMain.registerAPI(this);
	}

	//これは必須
	@Override
	public void onInitialization() {
		if (mod != null)
			mod.preInit();
	}

	//コレは必須
	//キーバインドメイン部分
	@Override
	public void processKeybinds() {
		if (mod != null)
			mod.processKeybinds();
	}

	//コレは必須
	//キー登録なんかをしてるらしい
	@Override
	public void registerKeys(KeyBinding... keys) {
		Minecraft mc = Minecraft.getInstance();
		mc.gameSettings.keyBindings = ArrayUtils.addAll(mc.gameSettings.keyBindings, keys);

	}

	@Override
	public String getAPIName() {
		return "Rift";
	}

}
