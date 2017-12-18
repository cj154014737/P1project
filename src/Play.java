import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.*;

import java.awt.*;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Timer;


public class Play extends BasicGameState {
  Image map;
  Animation hero,movingUp,movingDown,movingLeft,movingRight;
  public ArrayList<Rectangle>obstacles;
  public ArrayList<Rectangle>movingObstacles;
  public ArrayList<Button>buttons;
  int[] duration={200,200};
  float heroPositionX=0,heroPositionY=0, squareX =heroPositionX+250, squareY =heroPositionY+250;
  Button wrongAnswer;
  int moving,rightAnswerPosition,time=1,score=0;
  boolean collides=false,answerCollides=false,restart=false,questionAnswered=false,quit=false;
  Rectangle obstacle,movingObstacle,square;
  QuestionGenerator question;
  Collision collision;
  static StopWatch sw;


    public Play(int state) {
        movingObstacle=new Rectangle();
        obstacles=new ArrayList<Rectangle>();
        movingObstacles=new ArrayList<Rectangle>();
        buttons=new ArrayList<Button>();
        loadObstacbles();
        addMovingObstacles(true);
        collision=new Collision();
        question=new QuestionGenerator();
        generateAnswers (time,question);
        square=new Rectangle((int) squareX,(int) squareY,50,50);
        obstacle=new Rectangle();
        sw=new StopWatch();
        sw.start();
  }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        map=new Image("lib/res/img/background2.png");
        Score.resetScore();
       Image[] walkUp={new Image(("lib/res/img/Hero back.png")),new Image("lib/res/img/Hero back.png")};
       Image[] walkDown={new Image(("lib/res/img/Hero front.png")),new Image("lib/res/img/Hero front.png")};
       Image[] walkLeft={new Image(("lib/res/img/Hero left.png")),new Image("lib/res/img/Hero left.png")};
       Image[] walkRight={new Image(("lib/res/img/Hero right.png")),new Image("lib/res/img/Hero right.png")};
       movingUp=new Animation(walkUp,duration,false);
       movingDown=new Animation(walkDown,duration,false);
       movingLeft=new Animation(walkLeft,duration,false);
       movingRight=new Animation(walkRight,duration,false);
       hero=movingDown;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
       hero.draw(squareX, squareY);
       paintSquare(g,square);
            if (score<15){
                paintQuestion(g);
       }
       if (quit==true){
        g.drawString("Resume(R)",250,200 );
        g.drawString("Main Menu(M)",250,250 );
        g.drawString("Quit Game(Q)",250,300);
        if (quit==false){
          g.clear();
        }
      }
            for (int i=0;i<obstacles.size();i++) {
                paintObstacles(g,obstacles.get(i),Color.cyan);
            }
            for (int i=0;i<movingObstacles.size();i++){
                paintObstacles(g,movingObstacles.get(i),Color.darkGray);
            }
            for(int i=0; i<buttons.size();i++){
                Color white=new Color(255,255,255,300);

                g.setColor(white);
                g.setFont(OurFonts.getFont22B());
                buttons.get(i).drawText(g);
                if (buttons.get(i).intersects(square)&&buttons.get(i).isTheAnswerRight()){
                    Button.paintRightAnswer(g,buttons.get(i));
                }
                if (buttons.get(i).intersects(square)&&buttons.get(i).isTheAnswerRight()==false){
                    Button.paintObstacles(g,buttons.get(i));
                }
            }
        for(int i=0; i<buttons.size();i++){
            Color grey=new Color(160,160,160,127);
            g.setColor(grey);
            g.setFont(OurFonts.getFont22B());
            buttons.get(i).drawText(g);
        }


    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        moving=1;
        Score.update(delta);
        Input input = gc.getInput();
        menu(gc,sbg);
        barriarsCollision(gc);
        movingCollision();
        if (heroPositionX<-5910){
            sbg.enterState(2);
        }
        for(int z=0;z<movingObstacles.size();z++){
            Rectangle obstacle=movingObstacles.get(z);
            if(obstacle.y>1000){
                movingObstacles.clear();
                addMovingObstacles(true);
            }
        }
       //movingobstacles
        obstaclesMoving(gc);

       //answer moving
        anwserCheck(gc);
        mapMoving(gc);

        }

    public void addObstacles(boolean start){
        int width=50;
        int height=100;
        if (start) {
            obstacles.add(new Rectangle((int) heroPositionX + 500, (int) heroPositionY, width, height));
            obstacles.add(new Rectangle((int) heroPositionX + 500, (int) heroPositionY + 220, width, height));
            obstacles.add(new Rectangle((int) heroPositionX + 500,(int) heroPositionY + 440, width, height));
            obstacles.add(new Rectangle((int) heroPositionX + 500,(int) heroPositionY + 660, width, height));

            // obstacles.add(new Rectangle((int)heroPositionX + 900,(int)heroPositionY,width,height));
        }
        else {
            obstacles.add(new Rectangle(obstacles.get(obstacles.size()-1).x+400, (int) heroPositionY, width, height));
            obstacles.add(new Rectangle(obstacles.get(obstacles.size()-1).x, (int) heroPositionY + 220, width, height));
            obstacles.add(new Rectangle(obstacles.get(obstacles.size()-1).x,(int) heroPositionY + 440, width, height));
            obstacles.add(new Rectangle(obstacles.get(obstacles.size()-1).x,(int) heroPositionY + 660, width, height));

            // obstacles.add(new Rectangle(obstacles.get(obstacles.size()-1).x+400,(int)heroPositionY,width,height));
        }
    }
    public void addMovingObstacles(boolean start){
        int width=40;
        int height=150;
        if (start) {
            movingObstacles.add(new Rectangle((int) heroPositionX + 700, (int) heroPositionY-50 , width, height));
            for (int i =0;i<14;i++){
                movingObstacles.add(new Rectangle(movingObstacles.get(movingObstacles.size()-1).x+400, (int) heroPositionY-50 , width, height));
                //  addMovingObstacles(false);
            }
        }


    }
    public void generateAnswers(int time,QuestionGenerator question) {
        float width=40;
        float height=80;
        rightAnswerPosition=question.getGenerator().nextInt(3);
        question.clearWrongAnswers();
        //  System.out.println(rightAnswerPosition);
        float x=(heroPositionX+500)+400*(time-1);
        buttons.add(new Button(x,heroPositionY+120,width,height,Integer.toString(question.generateWrongAnswer())));
        buttons.add(new Button(x,heroPositionY+340,width,height,Integer.toString(question.generateWrongAnswer())));
        buttons.add(new Button(x,heroPositionY+560,width,height,Integer.toString(question.generateWrongAnswer())));
        buttons.get(rightAnswerPosition).setTheAnswerRight(true);
        buttons.get(rightAnswerPosition).setText(Integer.toString(question.getRightAnswer()));

    }
    public void paintSquare(Graphics g,Rectangle square){
        Color myColor=new Color(255,2,2,10);
        g.setColor(myColor);
        g.fillRect(square.x,square.y,square.width,square.height);
    }
    public void paintObstacles(Graphics g, Rectangle obstacle,Color color){
        g.setColor(color);
        g.fillRect(obstacle.x, obstacle.y,obstacle.width,obstacle.height);
    }
    public void paintQuestion(Graphics g){
        Color myColor=new Color(255,2,2,300);
        g.setFont(OurFonts.getFont22B());
        g.setColor(myColor);
        g.drawString(question.toString(),heroPositionX+time*(400)+100,heroPositionY-40);
        g.drawString(question.toString(),heroPositionX+time*(400)+100,heroPositionY+770);


    }
    public void barriarsCollision(GameContainer gc){
        Input input=gc.getInput();
        for (int i=0;i<obstacles.size();i++) {

            obstacle=obstacles.get(i);
            if (square.intersects(obstacle)){
                collides=true;
            }
            else {
                collides=false;
            }
            if (input.isKeyDown(Input.KEY_UP)) {
                obstacle.y += moving;
                if (collides&&heroPositionY<155-(getNum(i)-1)*220){
                 //   System.out.println("obstacleY "+obstacles.get(i).y+" "+heroPositionY);
                    heroPositionY-=20;
                    Collision.up(obstacles,movingObstacles,buttons,20);
                }
            }
            if (input.isKeyDown(Input.KEY_DOWN)) {
                obstacle.y -= moving;
                if (collides&&heroPositionY>298-(getNum(i)-1)*220){
                    System.out.println("obstacleY "+obstacles.get(i).y+" "+heroPositionY);

                    heroPositionY+=20;
                    Collision.down(obstacles,movingObstacles,buttons,20);
                }
            }
            if (input.isKeyDown(Input.KEY_LEFT)) {
                obstacle.x+=moving;
                if (collides && heroPositionX<0-obstacle.x-50-400*(getColumn(i)-1)){
                    heroPositionX-=20;
                    Collision.left(obstacles,movingObstacles,buttons,20);
                }
            }
            if (input.isKeyDown(Input.KEY_RIGHT)) {
                obstacle.x -= moving;
                if (collides&&heroPositionX>0-obstacle.x+50-400*(getColumn(i)-1)){
                    heroPositionX+=20;
                    Collision.right(obstacles,movingObstacles,buttons,20);
                }
            }
        }
    }
    public void movingCollision(){
        for (int i=0;i<movingObstacles.size();i++) {
            Rectangle movingObstacle=movingObstacles.get(i);
            if (square.intersects(movingObstacle)) {
              heroPositionX=-(movingObstacle.x+50)-i*400;
              heroPositionY=0;
              obstacles.clear();
              //addObstacles(false);
                loadObstacbles();
              movingObstacles.clear();
              addMovingObstacles(true);
              buttons.clear();
              generateAnswers(time,question);
           } else {
              continue;
           }
        }
    }

    public void obstaclesMoving(GameContainer gc){
        for (int i=0;i<movingObstacles.size();i++) {
            Input input = gc.getInput();
            movingObstacle = movingObstacles.get(i);
            movingObstacle.y++;

            if (input.isKeyDown(Input.KEY_UP)) {
                movingObstacle.y += moving;

            }
            if (input.isKeyDown(Input.KEY_DOWN)) {
                movingObstacle.y -= moving;
            }
            if (input.isKeyDown(Input.KEY_LEFT)) {
                movingObstacle.x += moving;
            }

            if (input.isKeyDown(Input.KEY_RIGHT)) {
                movingObstacle.x -= moving;
            }
        }
    }
    public void mapMoving(GameContainer gc){
        Input input = gc.getInput();

        if (input.isKeyDown(Input.KEY_UP)) {
            hero = movingUp;
            heroPositionY += moving;
            if(heroPositionY>278){
                heroPositionY-=20;
                Collision.up(obstacles,movingObstacles,buttons,20);
            }
        }
        if (input.isKeyDown(Input.KEY_DOWN)) {
            hero = movingDown;
            heroPositionY -= moving;
            if(heroPositionY<-493){
                heroPositionY+=20;
                Collision.down(obstacles,movingObstacles,buttons,20);
            }
        }
        if (input.isKeyDown(Input.KEY_LEFT)) {
            hero = movingLeft;
            heroPositionX += moving;
        }
        if (input.isKeyDown(Input.KEY_RIGHT)) {

            hero = movingRight;
            heroPositionX -= moving;
        }
    }
    public void anwserCheck(GameContainer gc){
        Input input=gc.getInput();
        for (int i=0;i<buttons.size();i++){
                Button button =buttons.get(i);
                if (buttons.get(i).isTheAnswerRight()==false) {

                wrongAnswer = buttons.get(i);

                if (wrongAnswer.intersects(square) && wrongAnswer != null) {
                    answerCollides = true;
                    Score.wrongAnswers++;
                    question.regenerate();
                    timea();
                    buttons.clear();
                    generateAnswers(time, question);

                }  else {
                    answerCollides = false;
                }
            }
           else if(button.intersects(square)&&button.isTheAnswerRight()){
                    questionAnswered=true;
                    Timer timer = new Timer();
                    score++;
                    Score.score++;
                    time++;
                    buttons.clear();
                    if(score<15) {
                        question.regenerate();
                        timea();
                        generateAnswers(time, question);
                    }
                    else {
                        break;
                    }
                }

            if (input.isKeyDown(Input.KEY_UP)) {
                button.setY(button.getY()+moving);
                if (answerCollides){
                    heroPositionY-=100;
                    collision.up(obstacles,movingObstacles,buttons,100);
                }
            }
            if (input.isKeyDown(Input.KEY_DOWN)) {
                button.setY(button.getY()-moving);
                if (answerCollides){
                    heroPositionY+=100;
                    collision.down(obstacles,movingObstacles,buttons,100);
                }
            }
            if (input.isKeyDown(Input.KEY_LEFT)) {
                button.setX(button.getX()+moving);
                if (answerCollides){
                    heroPositionX-=100;
                    collision.left(obstacles,movingObstacles,buttons,100);
                }
            }

            if (input.isKeyDown(Input.KEY_RIGHT)) {
                button.setX(button.getX()-moving);
                if (answerCollides){
                    heroPositionX+=100;
                    collision.right(obstacles,movingObstacles,buttons,100);
                }
            }
        }
    }


    public void menu(GameContainer gc,StateBasedGame sbg){
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_ESCAPE)){
            quit=true;
        }
        if (quit == true){
            if (input.isKeyDown(Input.KEY_Q)){
                gc.exit();
            }

            if (input.isKeyDown(Input.KEY_M)){
                sbg.enterState(0);
            }

            if (input.isKeyDown(Input.KEY_R)){
                restart();
                quit = false;
            }

        }
    }
    public void loadObstacbles(){
        boolean start =true;
        for (int i=0;i<15;i++){
            addObstacles(start);
            start=false;
        }
    }
    public void restart(){
        score=0;
        heroPositionX=0;
        heroPositionY=0;
        time=1;
        movingObstacles.clear();
        obstacles.clear();
        loadObstacbles();
        addMovingObstacles(true);
        buttons.clear();
        generateAnswers(time,question);
        sw.reset();
        sw.start();
        restart=false;
    }
    public void timea(){
        try{
            Thread.sleep(100);
        }
        catch (Exception e){

        }
    }
    public int getNum(int i){
        int x= getColumn(i);

        return (i+1)-(x-1)*4;
    }
    public int getColumn(int i){
        int x=i+1;
        if(x%4==0){
            return x/4;
        }
        else if (x/4==0){
            return 1;
        }
        else {
            return x/4+1;
        }
    }
    public int getID() {
        return 1;
    }
}
