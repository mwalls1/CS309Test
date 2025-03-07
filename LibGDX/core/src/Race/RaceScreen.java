package Race;

import java.awt.Point;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class RaceScreen implements Screen {
	private boolean[][] passable;
	private TiledMap map;
	private Game game;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Stage stage;
	private TiledMapTileLayer road;
	private TiledMapTileLayer checkpoint;
	private AssetManager manager;
	private SpriteBatch batch;
	private Car player;
	private Texture gaugeTexture;
	private Sprite gaugeSprite;
	private float angle;
	private Point playerPosition;
	private int lap;
	private boolean check1;
	private boolean check2;
	private boolean check3;
	private BitmapFont lapFont;
	private BitmapFont lapOneFont;
	private BitmapFont lapTwoFont;
	private BitmapFont lapThreeFont;
	private BitmapFont finalTimeFont;
	private BitmapFont speedFont;
	private int frameCount;
	private int bigFrameCount;
	private String lapOneTime;
	private String lapTwoTime;
	private String lapThreeTime;
	private String finalTime;
	private float w = Gdx.graphics.getWidth();
	private float h = Gdx.graphics.getHeight();

	public RaceScreen(Game game) {
		this.game = game;
		create();
	}

	public void create() {
		lap = 0;
		lapOneTime = "1: ---";
		lapTwoTime = "2: ---";
		lapThreeTime = "3: ---";
		finalTime = "F: ---";
		angle = 90;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		initAssetManager();
		batch = new SpriteBatch();
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		// track 1 = 320, 320
		// track 2 = 520, 1320
		camera.position.x = 320;
		camera.position.y = 520;

		batch.setProjectionMatrix(camera.combined);
		player = new Car("Race/car1.png", manager, camera);
		player.setPosition(w / 2, h / 2);
		map = new TmxMapLoader().load("Race/track1 copy.tmx");
		road = (TiledMapTileLayer) map.getLayers().get("Road");
		checkpoint = (TiledMapTileLayer) map.getLayers().get("Checkpoint");
		renderer = new OrthogonalTiledMapRenderer(map);
		Gdx.input.setInputProcessor(stage);
		lapFont = new BitmapFont();
		lapOneFont = new BitmapFont();
		lapTwoFont = new BitmapFont();
		lapThreeFont = new BitmapFont();
		finalTimeFont = new BitmapFont();
		speedFont = new BitmapFont();
		gaugeTexture = new Texture("Race/gaugeCluster.png");
		gaugeSprite = new Sprite(gaugeTexture);
		gaugeSprite.setSize(gaugeSprite.getWidth() * .75f, gaugeSprite.getHeight() * .75f);
		frameCount = 0;
		bigFrameCount = 0;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Point playerPosition = player.getTilePosition();
		Gdx.gl.glClearColor(125 / 255f, 188 / 255f, 87 / 255f, 1);
		// 125 188 87
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput();
		renderer.setView(camera);

		if (Gdx.input.isKeyPressed(Keys.R))
			create();
		renderer.render();
		batch.begin();
		player.draw(batch);
		gaugeSprite.setPosition(0, 0);

		gaugeSprite.draw(batch);
		lapFont.draw(batch, lap + "/3 ", player.getX() - 530, player.getY() - 287);
		lapOneFont.draw(batch, lapOneTime, w / 30 + 5, h / 4 - 5);
		lapTwoFont.draw(batch, lapTwoTime, w / 30 + 5, h / 4 - 30);
		lapThreeFont.draw(batch, lapThreeTime, w / 30 + 5, h / 4 - 55);
		finalTimeFont.draw(batch, finalTime, w / 30 + 5, h / 4 - 80);
		double speed = player.getSpeed() * (9 / 2f);
		speedFont.draw(batch, new String("" + (int) speed + "MPH"), player.getX() - 540, player.getY() - 330);
		if (road.getCell(playerPosition.x, playerPosition.y).getTile().getProperties().containsKey("blocked")) {
			player.setSpeed(0);
			player.moveAfterCollision();
		} else {
			player.move();
		}
		batch.end();
		camera.update();
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			System.out.println("Player Position: " + playerPosition.toString());
			System.out.println("Slow tile: "
					+ road.getCell(playerPosition.x, playerPosition.y).getTile().getProperties().containsKey("slow"));
			System.out.println("Blocked tile: " + road.getCell(playerPosition.x, playerPosition.y).getTile()
					.getProperties().containsKey("blocked"));
			System.out.println("Camera Position: " + camera.position.x + ", " + camera.position.y);
			System.out.println();
		}

		if (road.getCell(playerPosition.x, playerPosition.y).getTile().getProperties().containsKey("slow")) {
			player.decSpeed(0.1f);
		}
		if (road.getCell(playerPosition.x, playerPosition.y).getTile().getProperties().containsKey("goal") && check1
				&& check2 && check3) {
			lap++;
			check1 = false;
			check2 = false;
			check3 = false;
			if (lap == 1) {
				int min = frameCount / 3600;
				int sec = (frameCount % 3600) / 60;

				if (sec < 10)
					lapOneTime = "1: " + min + ":0" + sec;
				else
					lapOneTime = "1: " + min + ":" + sec;
			} else if (lap == 2) {
				int min = frameCount / 3600;
				int sec = (frameCount % 3600) / 60;
				if (sec < 10)
					lapTwoTime = "2: " + min + ":0" + sec;
				else
					lapTwoTime = "2: " + min + ":" + sec;
			}

			else if (lap >= 3) {
				int fmin = bigFrameCount / 3600;
				int fsec = (bigFrameCount % 3600) / 60;
				int min = frameCount / 3600;
				int sec = (frameCount % 3600) / 60;
				if (sec < 10)
					lapThreeTime = "3: " + min + ":0" + sec;
				else
					lapThreeTime = "3: " + min + ":" + sec;
				if (fsec < 10)
					finalTime = "3: " + min + ":0" + sec;
				else
					finalTime = "3: " + fmin + ":" + fsec;
			}

			frameCount = 0;
		}
		if (checkpoint.getCell(playerPosition.x, playerPosition.y) != null) {

			if (checkpoint.getCell(playerPosition.x, playerPosition.y).getTile().getProperties()
					.containsKey("checkpoint1")) {
				check1 = true;
				System.out.println("CHECK1");
			}

			if (checkpoint.getCell(playerPosition.x, playerPosition.y).getTile().getProperties()
					.containsKey("checkpoint2")) {
				check2 = true;
				System.out.println("CHECK2");
			}

			if (checkpoint.getCell(playerPosition.x, playerPosition.y).getTile().getProperties()
					.containsKey("checkpoint3")) {
				check3 = true;
				System.out.println("CHECK3");
			}

		}

		frameCount++;
		bigFrameCount++;
	}

	@Override
	public void resize(int width, int height) {
		create();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void initAssetManager() {
		manager = new AssetManager();
		manager.load("Race/car1.png", Texture.class);
		manager.load("Race/gaugeCluster.png", Texture.class);
		manager.finishLoading();
	}

	public void handleInput() {

	}
}
