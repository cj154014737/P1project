import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Image;

import java.util.ArrayList;


public class Menu extends BasicGameState {
    Image background;
    Image buttonsTexture;
    Image hoveredButtonsTexture;
    Image gameLogo;
    //Image menuMenuBackground;
    private Music music;
    private Sound sound;
    ArrayList<Button> buttons;

    public Menu(int state) {

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        background=new Image("lib/res/img/menuBackground.png");
        buttonsTexture=new Image("lib/res/img/menuButtons.png");
        hoveredButtonsTexture=new Image("lib/res/img/hoveredMenuButtons.png");
        //menuMenuBackground=new Image("lib/res/img/menuMenuBackground.png");
        gameLogo= new Image("lib/res/img/gameLogo.png");
        buttons=new ArrayList<>(4);
        for(int i=0;i<4;i++){
            if(i<2){
                buttons.add(new Button((Settings.getScreenWidth()-Settings.getScreenWidth()/8)/2,Settings.getScreenHeight()/9*(i+3),Settings.getScreenWidth()/8,Settings.getScreenWidth()/16,buttonsTexture.getSubImage(i*64,0,64,32)));
                buttons.get(i).setHoveredTexture(hoveredButtonsTexture.getSubImage(i*64,0,64,32));
            }else{
                buttons.add(new Button((Settings.getScreenWidth()-Settings.getScreenWidth()/8)/2,Settings.getScreenHeight()/9*(i+3),Settings.getScreenWidth()/8,Settings.getScreenWidth()/16,buttonsTexture.getSubImage((i-2)*64,32,64,32)));
                buttons.get(i).setHoveredTexture(hoveredButtonsTexture.getSubImage((i-2)*64,32,64,32));
            }
        }
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw(0,0,Settings.getScreenWidth(),Settings.getScreenHeight());
        //menuMenuBackground.draw((Settings.getScreenWidth()-menuMenuBackground.getWidth())/2,Settings.getScreenHeight()/9,menuMenuBackground.getWidth(),Settings.getScreenHeight()/9*7);
        gameLogo.draw((Settings.getScreenWidth()-gameLogo.getWidth())/2,Settings.getScreenHeight()/9*2);
        for(int i=0;i<4;i++){
            if(buttons.get(i).isSelected()){
                buttons.get(i).drawHovered();
            }else {
                buttons.get(i).draw();
            }
        }

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {


        Input input = gc.getInput();
        for(int i=0;i<4;i++){
            if(buttons.get(i).isHovered(input)){
                buttons.get(i).setSelected(true);
            }else{
                buttons.get(i).setSelected(false);
            }
        }
        if(buttons.get(0).isClicked(input)){
            sbg.getState(1).init(gc,sbg);
            sbg.enterState(1);
        }
        if(buttons.get(1).isClicked(input)){
            gc.exit();
        }
        if(buttons.get(2).isClicked(input)){
            sbg.enterState(3);
        }
        if(buttons.get(3).isClicked(input)){
            sbg.enterState(4);
        }
    }

    public int getID() {
        return 0;

    }

}
