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

    private Rectangle paddle;

    private Vector2 ballPosition;
    private Vector2 ballVelocity;

    public static final float BALL_RADIUS = 20f;
    public static final float PADDLE_WIDTH = 200f;
    public static final float PADDLE_HEIGHT = 20f;
    private static final float PADDLE_SPEED = 400f;
    private static final float PADDING = 20f;

    @Override
    public void create() {
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();

        layout = new GlyphLayout();
        font = new BitmapFont();

        paddle = new Rectangle(Gdx.graphics.getWidth() / 2f - PADDLE_WIDTH / 2f, PADDING, PADDLE_WIDTH, PADDLE_HEIGHT);

        ballPosition = new Vector2(Gdx.graphics.getWidth() / 2f, PADDING + paddle.height);
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
        renderer.setColor(Color.GREEN);
        renderer.rect(paddle.x, paddle.y, paddle.width, paddle.height);
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
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            paddle.x -= PADDLE_SPEED * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            paddle.x += PADDLE_SPEED * Gdx.graphics.getDeltaTime();
        }

        if (paddle.x < 0) paddle.x = 0;
        if (paddle.x > Gdx.graphics.getWidth() - paddle.width)
            paddle.x = Gdx.graphics.getWidth() - paddle.width;
    }

    private void updateBallPosition() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        ballPosition.y += ballVelocity.y * deltaTime;
        ballPosition.x += ballVelocity.x * deltaTime;

        if (ballPosition.y > Gdx.graphics.getHeight() - BALL_RADIUS) {
            ballVelocity.y = -ballVelocity.y;
        }

        if (ballPosition.x - BALL_RADIUS < 0 || ballPosition.x + BALL_RADIUS > Gdx.graphics.getWidth()) {
            ballVelocity.x = -ballVelocity.x;
        }

        if (ballPosition.y - BALL_RADIUS < paddle.y + paddle.height
            && ballPosition.x > paddle.x && ballPosition.x < paddle.x + paddle.width) {
            ballVelocity.y = -ballVelocity.y;
            ballPosition.y = paddle.y + paddle.height + BALL_RADIUS; // avoid getting stuck
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
