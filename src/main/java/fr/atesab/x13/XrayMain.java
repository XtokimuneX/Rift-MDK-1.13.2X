package fr.atesab.x13;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class XrayMain {
	private static final Logger log = LogManager.getLogger("X13");
	private static XrayMain instance;


	/**
	 * get this mod
	 */
	public static XrayMain getMod() {
		return instance;
	}

	/**
	 * true if an API is already register for it
	 */
	public static boolean isAPIRegister() {
		return instance != null;
	}

	/**
	 * register an API for Xray
	 * 
	 * @throws IllegalStateException
	 *             if an API is already register for it
	 */
	public static XrayMain registerAPI(BuildAPI api) throws IllegalStateException {
		instance = new XrayMain(api);

		return instance;
	}


	private BuildAPI api;

	private List<XrayMode> modes = Lists.newArrayList();
	private KeyBinding fullbright, config,test,test2,test3;
	private boolean isInit = false;
	private boolean isPreInit = false;
	private List<KeyBinding> kb=Lists.newArrayList();

	private XrayMain(BuildAPI api) {
		if (isAPIRegister())
			throw new IllegalStateException("An API is already register for this mod!");
		this.api = Objects.requireNonNull(api, "You must provide a non-null API");
	}


	/**
	 * init Xray
	 */
	public void init() {
		if (isInit)
			return;
		log("Initialization");
		Minecraft mc = Minecraft.getInstance();

		TxstR();

		/**
		api.registerKeys(fullbright = new KeyBinding("マクロ1", -1, "key.categories.xray"),
				config = new KeyBinding("マクロ2", -1, "key.categories.xray"),
				test = new KeyBinding("マクロ3", -1,"key.categories.xray"),
				test2 = new KeyBinding("マクロ4", -1,"key.categories.xray"),
				test3 = new KeyBinding("マクロ5", -1,"key.categories.xray"));
**/

		mc.gameSettings.loadOptions();
		isInit = true;
	}

	private static void log(String message) {
		log.info("[" + log.getName() + "] " + message);
	}

	/**
	 * pre-init Xray
	 * これは必須
	 */
	public void preInit() {
		if (isPreInit)
			return;
		log("Load Mixins...");
		MixinBootstrap.init();
		log("Search Optifine...");
		try {
			Class.forName("net.optifine.Lang"); // search Optifine
			log("Load Mixins for Optifine...");
			Mixins.addConfiguration("mixins.x13.optifine.json");
		} catch (ClassNotFoundException e) {
			log("Load Mixins without Optifine...");
			Mixins.addConfiguration("mixins.x13.json");
		}
		isPreInit = true;
	}

	/**
	 * Process modes and Xray keys
	 */
	public void processKeybinds() {
		/**
		if (fullbright.isPressed())
			Minecraft.getInstance().player.sendChatMessage(tt.get(1));
		else if (config.isPressed())
			Minecraft.getInstance().player.sendChatMessage(tt.get(2));
		else if(test.isPressed())
			Minecraft.getInstance().player.sendChatMessage(tt.get(3));
		else if(test2.isPressed())
			Minecraft.getInstance().player.sendChatMessage(tt.get(4));
		else if(test3.isPressed())
			Minecraft.getInstance().player.sendChatMessage(tt.get(5));
		 **/
		for (int i=0;i<kb.size();i++)
		{
			if(kb.get(i).isPressed())
			Minecraft.getInstance().player.sendChatMessage(tt.get(i+1));
		}

	}

	/**
	 * True if the side should be rendered
	 */
	public void shouldSideBeRendered(IBlockState state, IBlockReader reader, BlockPos pos, EnumFacing face,
			CallbackInfoReturnable<Boolean> ci) {
		for (XrayMode mode : modes)
			mode.shouldSideBeRendered(state, reader, pos, face, ci);
	}

	private List<String> tt;

	public void TxstR()
	{
		tt=new ArrayList<String>();

		try {
			// ファイルのパスを指定する
			File file = new File("test.txt");

			// ファイルが存在しない場合に作成する
			if (!file.exists()) {
				//作るメソッド
				TxstCreate();

				// それでもファイルが存在しない場合に例外が発生するので確認する
				if (!file.exists()) {
					tt.add("ファイルが読み込めませんでした。");
					return;
				}
			}


			// BufferedReaderクラスのreadLineメソッドを使って1行ずつ読み込み表示する
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String data;
			while ((data = bufferedReader.readLine()) != null) {

				tt.add(data);
			}

			for(int i=1;i<tt.size();i++)
			{
				if(!tt.get(i).isEmpty())
				{
					kb.add(new KeyBinding("マクロ"+i, -1, "key.categories.xray"));
					api.registerKeys(kb.get(i-1));
				}
			}

			// 最後にファイルを閉じてリソースを開放する
			bufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void TxstCreate()
	{
		File file = new File("test.txt");

		try {
			// 文字コードを指定する
			PrintWriter p_writer = new PrintWriter(new BufferedWriter
					(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));

			//ファイルに文字列を書き込む
			p_writer.println("※UTF-8で保存してください。");
			p_writer.println("マクロテスト1");
			p_writer.println("マクロテスト2");
			p_writer.println("マクロテスト3");
			p_writer.println("マクロテスト4");
			p_writer.println("マクロテスト5");

			//ファイルをクローズする
			p_writer.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
