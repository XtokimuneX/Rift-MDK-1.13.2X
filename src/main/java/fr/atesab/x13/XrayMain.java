package fr.atesab.x13;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;


public class XrayMain {
	private static final Logger log = LogManager.getLogger("tokimune");
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

	private boolean isInit = false;
	private boolean isPreInit = false;
	private List<KeyBinding> kb=Lists.newArrayList();

	private XrayMain(BuildAPI api) {
		if (isAPIRegister())
			throw new IllegalStateException("An API is already register for this mod!");
		this.api = Objects.requireNonNull(api, "You must provide a non-null API");
	}


	/**
	 * メイン部分
	 */
	public void init() {
		if (isInit)
			return;
		log("Initialization");
		Minecraft mc = Minecraft.getInstance();

		//ファイルを読み込みに行く
		TextR();

		mc.gameSettings.loadOptions();
		isInit = true;
	}

	private static void log(String message) {
		log.info("[" + log.getName() + "] " + message);
	}

	/**
	 * pre-init Xray
	 * これは必須
	 * これのどっかで設定画面にキー登録をしてる
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
	 * キーイベント受けとってメッセージにする部分
	 */
	public void processKeybinds() {

		//kbとttでナンバーにズレがあるので注意
		for (int i=0;i<kb.size();i++)
		{
			if(kb.get(i).isPressed())
			Minecraft.getInstance().player.sendChatMessage(tt.get(i+1));
		}

	}

	//メッセージを管理するリスト
	private List<String> tt;

	//ファイルを読み込むメソッド
	public void TextR()
	{
		//リストを初期化
		tt=new ArrayList<String>();

		try {
			// ファイルのパスを指定する
			File file = new File("マクロ設定.txt");

			// ファイルが存在しない場合に作成する
			if (!file.exists()) {
				//作るメソッド
				TextCreate();

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

			//読み込んだリストを元にマクロ枠を作っていく
			//リストの1行目はファイルの1行目であり注意書き
			//なのでリストとマクロ枠のナンバーが１つずれる
			for(int i=1;i<tt.size();i++)
			{
				if(!tt.get(i).isEmpty())
				{
					kb.add(new KeyBinding("マクロ"+i, -1, "マクロ"));
					api.registerKeys(kb.get(i-1));
				}
			}

			// 最後にファイルを閉じてリソースを開放する
			bufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//ファイルがなかった場合は新規作成
	public void TextCreate()
	{
		File file = new File("マクロ設定.txt");

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
