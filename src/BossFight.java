import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.security.SecureRandom;
import java.util.ArrayList;

public class BossFight extends BasicGameState {

        Image background;
        Image hpHeart;
        QuestionGenerator question;
        ArrayList<Button> buttonList;
        int rightAnswerPosition;
        int selectedPosition=0;
        boolean questionAnswered=false;
        boolean gameWon=false;
        int bossHp=3;   //Starting amount of bosses lifes

        public BossFight(int state) {

        }

        public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        background=new Image("lib/res/img/bossBackground.png");
        hpHeart= new Image("lib/res/img/hpHeart.png");
        question=new QuestionGenerator();
        buttonList=new ArrayList<Button>(4);
        rightAnswerPosition=(question.getGenerator().nextInt(4)+1);     //Determine the position of the right answer
        Button.setHighlight(new Image("lib/res/img/highlight.png"));
        buttonList=generateTheLevel(question,rightAnswerPosition,selectedPosition);     //Method returning the array of Buttons(4 of them);
        }

        public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw(0,0);
        g.setColor(Color.black);
        if(gameWon){
            g.drawString("I just got defeated, press B to go back", 275, 125);
        }else {
            g.drawString(question.toString(), 275, 125);
            for (int i = 0; i < 4; i++) {
                buttonList.get(i).drawText(g);
                buttonList.get(i).drawHighlight();
            }
            for (int i = 0; i < bossHp; i++) {
                hpHeart.draw(i * 64 + 10, 10);
            }
        }
        }

        public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input=gc.getInput();
        if(!gameWon) {
            if (questionAnswered) {
                rightAnswerPosition = (question.getGenerator().nextInt(4) + 1);
                question.regenerate();
                buttonList = generateTheLevel(question, rightAnswerPosition, selectedPosition);
                questionAnswered = false;
            }
            if (input.isKeyDown(Input.KEY_DOWN)) {
                if (selectedPosition == 2) {
                    buttonList.get(0).setSelected(true);
                    buttonList.get(selectedPosition).setSelected(false);
                    selectedPosition = 0;
                } else if (selectedPosition == 3) {
                    buttonList.get(1).setSelected(true);
                    buttonList.get(selectedPosition).setSelected(false);
                    selectedPosition = 1;
                }
            }
            if (input.isKeyDown(Input.KEY_UP)) {
                if (selectedPosition == 0) {
                    buttonList.get(2).setSelected(true);
                    buttonList.get(selectedPosition).setSelected(false);
                    selectedPosition = 2;
                } else if (selectedPosition == 1) {
                    buttonList.get(3).setSelected(true);
                    buttonList.get(selectedPosition).setSelected(false);
                    selectedPosition = 3;
                }
            }
            if (input.isKeyDown(Input.KEY_LEFT)) {
                if (selectedPosition == 1) {
                    buttonList.get(0).setSelected(true);
                    buttonList.get(selectedPosition).setSelected(false);
                    selectedPosition = 0;
                } else if (selectedPosition == 3) {
                    buttonList.get(2).setSelected(true);
                    buttonList.get(selectedPosition).setSelected(false);
                    selectedPosition = 2;
                }
            }
            if (input.isKeyDown(Input.KEY_RIGHT)) {
                if (selectedPosition == 2) {
                    buttonList.get(3).setSelected(true);
                    buttonList.get(selectedPosition).setSelected(false);
                    selectedPosition = 3;
                } else if (selectedPosition == 0) {
                    buttonList.get(1).setSelected(true);
                    buttonList.get(selectedPosition).setSelected(false);
                    selectedPosition = 1;
                }
            }
            if (input.isKeyDown(Input.KEY_ENTER)) {
                if (buttonList.get(selectedPosition).isTheAnswerRight()) {
                    if (!questionAnswered) {
                        bossHp--;
                        questionAnswered = true;
                        if(bossHp<=0)   gameWon=true;
                    }
                }
            }
        }else if(input.isKeyDown(Input.KEY_B)){
            sbg.enterState(0);
        }


        }

        public static ArrayList<Button> generateTheLevel(QuestionGenerator question,int rightAnswerPosition, int selectedPosition){
            ArrayList<Button> buttonList=new ArrayList<Button>(4);
            buttonList.add(new Button(393,630,200,70,Integer.toString(question.generateWrongAnswer())));
            buttonList.add(new Button(594,630,200,70,Integer.toString(question.generateWrongAnswer())));
            buttonList.add(new Button(393,560,200,70,Integer.toString(question.generateWrongAnswer())));
            buttonList.add(new Button(594,560,200,70,Integer.toString(question.generateWrongAnswer())));
            buttonList.get(rightAnswerPosition-1).setTheAnswerRight(true);
            buttonList.get(rightAnswerPosition-1).setText(Integer.toString(question.getRightAnswer()));
            buttonList.get(selectedPosition).setSelected(true);
            return buttonList;
        }
        public int getID() { return 2; }

}
