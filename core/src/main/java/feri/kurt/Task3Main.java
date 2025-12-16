package feri.kurt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class Task3Main extends ApplicationAdapter {

    private ShapeRenderer renderer;
    private SpriteBatch batch;

    private GlyphLayout layout;
    private BitmapFont font;

    private Rectangle paddle1;
    private Rectangle paddle2;

    private Vector2 ballPosition;
    private Vector2 ballVelocity;

    public static final float BALL_RADIUS = 15f;
    public static final float PADDLE_WIDTH = 150f;
    public static final float PADDLE_HEIGHT = 20f;
    private static final float PADDLE_SPEED = 400f;
    private static final float PADDING = 20f;

    @Override
    public void create() {
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();

        layout = new GlyphLayout();
        font = new BitmapFont();

        paddle1 = new Rectangle(Gdx.graphics.getWidth() / 2f - PADDLE_WIDTH / 2f, PADDING, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle2 = new Rectangle(Gdx.graphics.getWidth() / 2f - PADDLE_WIDTH / 2f, Gdx.graphics.getHeight() - PADDLE_HEIGHT - PADDING, PADDLE_WIDTH, PADDLE_HEIGHT);

        ballPosition = new Vector2(Gdx.graphics.getWidth() / 2f, PADDING + paddle1.height);
        ballVelocity = new Vector2(200, 200);   // x and y speed
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        handleGameInput();
        handlePaddleInput();
        updateBallPosition();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);
        renderer.circle(ballPosition.x, ballPosition.y, BALL_RADIUS);
        renderer.setColor(Color.RED);
        renderer.rect(paddle1.x, paddle1.y, paddle1.width, paddle1.height);
        renderer.setColor(Color.BLUE);
        renderer.rect(paddle2.x, paddle2.y, paddle2.width, paddle2.height);
        renderer.end();

        batch.begin();
        layout.setText(font, "Press 'Esc' to exit");
        float messageX = Gdx.graphics.getWidth() - layout.width - PADDING;
        font.draw(batch, layout, messageX, PADDING + font.getCapHeight());
        batch.end();
    }

    private void handleGameInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void handlePaddleInput() {
        //input for paddle 1
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            paddle1.x -= PADDLE_SPEED * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            paddle1.x += PADDLE_SPEED * Gdx.graphics.getDeltaTime();
        }

        if (paddle1.x < 0) paddle1.x = 0;
        if (paddle1.x > Gdx.graphics.getWidth() - paddle1.width)
            paddle1.x = Gdx.graphics.getWidth() - paddle1.width;

        //input for paddle 2
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            paddle2.x -= PADDLE_SPEED * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            paddle2.x += PADDLE_SPEED * Gdx.graphics.getDeltaTime();
        }

        if (paddle2.x < 0) paddle2.x = 0;
        if (paddle2.x > Gdx.graphics.getWidth() - paddle2.width)
            paddle2.x = Gdx.graphics.getWidth() - paddle2.width;
    }

    private void updateBallPosition() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        ballPosition.y += ballVelocity.y * deltaTime;
        ballPosition.x += ballVelocity.x * deltaTime;

        if (ballPosition.x - BALL_RADIUS < 0 || ballPosition.x + BALL_RADIUS > Gdx.graphics.getWidth()) {
            ballVelocity.x = -ballVelocity.x;
        }

        if (ballPosition.y - BALL_RADIUS < paddle1.y + paddle1.height
            && ballPosition.x > paddle1.x && ballPosition.x < paddle1.x + paddle1.width) {
            ballVelocity.y = -ballVelocity.y;
            ballPosition.y = paddle1.y + paddle1.height + BALL_RADIUS; // avoid getting stuck
        }

        if (ballPosition.y + BALL_RADIUS > paddle2.y
            && ballPosition.x > paddle2.x && ballPosition.x < paddle2.x + paddle2.width) {
            ballVelocity.y = -ballVelocity.y;
            ballPosition.y = paddle2.y - BALL_RADIUS; // avoid getting stuck
        }

    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
