package Modelados;

import java.awt.event.MouseEvent;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.GL;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import static javax.swing.text.html.HTML.Attribute.TITLE;

public class Tetera extends JFrame implements GLEventListener, MouseMotionListener {

    private static final long serialVersionUID = 7376825297884956163L;

    private float rotateX, rotateY;
    private int lastX, lastY;
    
    Texture textura1;

    public Tetera() {
        super("TETERA");
        setSize(550,450);
        setLocation(120, 120);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glCapabilities.setDoubleBuffered(true);
        GLCanvas glCanvas = new GLCanvas(glCapabilities);
        glCanvas.addGLEventListener(this);
        glCanvas.addMouseMotionListener(this);
        add(glCanvas);
        addMouseMotionListener(this);
        rotateX = 0f; rotateY = 0f;
        Animator a = new Animator(glCanvas);
        a.start();
        
        
    }
    
    void iniciarVista(){
    SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Tetera tetera = new Tetera();
                tetera.setVisible(true);
              
            }
        });
    }
    


    Texture cargarTextura(String imageFile){
        Texture text1 = null;
        try {
             BufferedImage buffImage = ImageIO.read(new File(imageFile));           
             text1 = AWTTextureIO.newTexture(GLProfile.getDefault(),buffImage,false);
        } catch (IOException ioe){
            System.out.println("Problema al cargar el archivo "+imageFile);
        }  
        return text1;
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(128, 0, 128, 0);
        gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1,1,-1,1,-2,2);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glRotatef(rotateX,0,1,0);
        gl.glRotatef(rotateY,1,0,0);
        this.textura1.enable(gl);
        //(new GLUT()).glutSolidCone(0.5, 0.5, 3, 3);
        (new GLUT()).glutSolidTeapot(0.5);
        this.textura1.disable(gl);
     // Asociar la textura con el canvas
        this.textura1.bind(gl);
        
        
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
         gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);   
        gl.glShadeModel(GL_SMOOTH);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR);
        gl.glEnable(GL2.GL_NORMALIZE);
        this.textura1 = this.cargarTextura("imagenes/descargar.jfif");
        
        // Habilitar el uso de texturas
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_BLEND);
        // Especificar la funcion de mezcla "adherencia" de textura
        gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ZERO);       
    }

    @Override
    public void reshape(
            GLAutoDrawable arg0, int arTJOGL2, int arg2, int arg3, int arg4) {
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        rotateX += e.getX() - lastX;
        rotateY += e.getY() - lastY;
        lastX = e.getX();
        lastY = e.getY();
    }
}