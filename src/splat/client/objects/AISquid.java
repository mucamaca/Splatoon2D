package splat.client.objects;

import splat.client.Main;
import splat.client.ai.SquidAI;
import splat.client.factories.ColorSplatFactory;
import org.newdawn.slick.*;
import org.newdawn.slick.Image;
import splat.client.factories.TileMapHelper;
import splat.client.objects.mainChar.MainLign;
import splat.client.updating.UpdateAble;

import java.awt.*;
import java.awt.Color;

//These's x and y are absolute.
public class AISquid extends GameObject implements UpdateAble {
    private Color color;
    private SquidAI ai;
    private Image representation;
    private HealthBar health;
    public boolean isOnSplat = false;
    private boolean deadReported = false;
    private ColorSplatFactory splatFactory;
    public TileMapHelper mapHelper;
    private Rectangle collisionRectangle;
    public MainLign mainLign;
    public float opacity = 1.0F;

    public AISquid(float x, float y, Color c, Main main) {
        color = c;
        splatFactory = main.splatFactory;
        mainLign = main.mainChar;
        mapHelper = main.mapHelper;
        ai = new SquidAI(this);
        health = new HealthBar(333);
        this.x = x;
        this.y = y;
        try {
            if (c == Color.ORANGE)
                representation = new Image("data/sprites/oranzenLign.png");
            else if (c == Color.BLUE)
                representation = new Image("data/sprites/modrLign.png");
            else
                throw new RuntimeException("Wrong squid color.");
        }catch (Exception e){
            System.exit(-1);
        }
        collisionRectangle = new Rectangle(33, 33);
        representation.setCenterOfRotation(18, 18);
    }

    @Override
    public void update(GameContainer container, float tpf) {
        collisionRectangle.setLocation((int)(x + mainLign.x), (int)(y + mainLign.y));
        isOnSplat = splatFactory.intersectsWithSplat(collisionRectangle, Color.ORANGE, (int)mainLign.x, (int)mainLign.y);
        ai.update(container, tpf);
        representation.setRotation(rotation);
        if (isOnSplat){
            health.damage(1);
        }
        if (health.isDead() && !deadReported){
            //TODO play a sound
            Main.squidsKilled ++;

            deadReported = true;
        }
    }

    public void render(int backX, int backY){
        if (health.isDead()){
            opacity -= 0.05F;
            representation.setAlpha(opacity);
        }
        representation.draw(x + backX, y + backY);
        health.render((int)x + backX, (int)y + backY - 5);
    }

    public Color getColor() {
        return color;
    }

    public Image getRepresentation() {
        return representation;
    }
}
