import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Color;


public class Button extends Rectangle{
    //Instance variables
    private static Image highlight;     //Image object drawn when the button is selected
    private Image texture;              //Image of the button
    private Image hoveredTexture;       //Brighter instance of the basic texture, for drawing when hovered
    private String text;                //Text to draw on the button
    private boolean isSelected;         //Logic value specifying whether button is selected or not (false by default)
    private boolean isTheAnswerRight;   //Logic value storing information whether the answer contained in a button instance is the right one (used in BossFight)
    //Different overloaded constructors
    public Button(float x, float y, float width, float height) {
        super(x, y, width, height);
        setText("");
        setSelected(false);
        setTheAnswerRight(false);
    }
    public Button(float x, float y, float width, float height, String text) {
        super(x, y, width, height);
        setText(text);
        setSelected(false);
        setTheAnswerRight(false);
    }
    public Button(float x, float y, float width, float height,Image texture){
        super(x,y,width,height);
        setTexture(texture);
    }
    public Button(float x, float y, float width, float height,Image texture,String text){
        super(x,y,width,height);
        setText(text);
        setTexture(texture);
    }
    //Draw methods
    /**
     * Draws the text in the centre of a button
     * @param  g  a Graphics object passed from the game state on the runtime
     * @see Graphics
     */
    public void drawText(Graphics g){
        g.drawString(text,(width-g.getFont().getWidth(text))/2+x,(height-g.getFont().getHeight(text))/2+y);
    }
    /**
     * Draws the highlight image if the button is selected
     */
    public void drawHighlight(){
        if(isSelected){
           highlight.draw(this.getX(),this.getY(),width,height);
        }
    }
    public void draw(){
        texture.draw(x,y,width,height);
    }
    public void drawHovered(){
        if(hoveredTexture!=null) {
            hoveredTexture.draw(x, y, width, height);
        }
    }


    public void setHoveredTexture(Image hoveredTexture) {
        this.hoveredTexture = hoveredTexture;
    }
    public Image getTexture() {
        return texture;
    }
    public void setTexture(Image texture) {
        this.texture = texture;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public boolean isTheAnswerRight() {
        return isTheAnswerRight;
    }
    public void setTheAnswerRight(boolean theAnswerRight) {
        isTheAnswerRight = theAnswerRight;
    }
    public static Image getHighlight() {
        return highlight;
    }
    public static void setHighlight(Image highlight) {
        Button.highlight = highlight;
    }
    public boolean isHovered(Input input){
        if(input.getMouseX()>=x&&input.getMouseX()<=x+width){
            if(input.getMouseY()>=y&&input.getMouseY()<=y+height)
                return true;
        }
        return false;
    }
    public boolean isClicked(Input input){
        if(isHovered(input)&&input.isMousePressed(0)){
            return true;
        }
        return false;
    }
    public boolean intersects(java.awt.Rectangle r) {
        float tw = this.width;
        float th = this.height;
        int rw = r.width;
        int rh = r.height;
        if (rw > 0 && rh > 0 && tw > 0 && th > 0) {
            float tx = this.x;
            float ty = this.y;
            int rx = r.x;
            int ry = r.y;
            rw += rx;
            rh += ry;
            tw += tx;
            th += ty;
            return (rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry);
        } else {
            return false;
        }
    }
    public static void paintWrongAnswer(Graphics g, Button button){
        Color myColor=new Color(255,2,2,80);
        g.setColor(myColor);
        g.fillRect(button.getX(), button.getY(),button.getWidth(),button.getHeight());
    }
    public static void paintRightAnswer(Graphics g,Button button){
        Color myColor=new Color(119,249,136,127);
        g.setColor(myColor);
        g.fillRect(button.getX(), button.getY(),button.getWidth(),button.getHeight());

    }
}
