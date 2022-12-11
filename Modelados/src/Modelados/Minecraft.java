package Modelados;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;        
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.GL;
import static com.jogamp.opengl.GL.*; 
import static com.jogamp.opengl.GL2.*;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.jogamp.opengl.GLProfile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


@SuppressWarnings("serial")
public class Minecraft extends GLCanvas implements GLEventListener, MouseMotionListener {
   // Define constants for the top-level container
   private static String TITLE = "MINECRAFT";  // window's title
   private static final int CANVAS_WIDTH = 500;  // width of the drawable
   private static final int CANVAS_HEIGHT = 400; // height of the drawable
   private static final int FPS = 60; // animator's target frames per second
   private static final float factInc = 5.0f; // animator's target frames per second
   float fovy=45.0f;
   float rotX=0.0f;
   float rotY=0.0f;
   float rotZ=0.0f;
   
   float posCamX = 0.0f;
   float posCamY = 0.0f;
   float posCamZ = 0.0f;
   
   Texture textura1;
   private float rotateX, rotateY;
   private int lastX, lastY;
   
   void iniciarVista(){
     SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
           
            GLCanvas canvas = new Minecraft();
            canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
 
            
            final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
 
            // Create the top-level container
            final JFrame frame = new JFrame(); 
            JPanel panel1 = new JPanel();
            panel1.setBackground(Color.BLACK);
            
            JPanel panel2 = new JPanel();
            
            
            FlowLayout fl = new FlowLayout();
            frame.setLayout(fl);
            
            panel1.add(canvas);
            frame.getContentPane().add(panel1);
            frame.getContentPane().add(panel2);
            
       
            frame.addWindowListener(new WindowAdapter() {
               @Override
               public void windowClosing(WindowEvent e) {
                  // Use a dedicate thread to run the stop() to ensure that the
                  // animator stops before program exits.
                  new Thread() {
                     @Override
                     public void run() {
                        if (animator.isStarted()) animator.stop();
                        System.exit(0);
                     }
                  }.start();
               }
            });
                        
            frame.setTitle(TITLE);
            frame.pack();
            frame.setLocation(650, 120);
            frame.setVisible(true);
            animator.start(); // start the animation loop
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
   
   private GLU glu;  
   private GLUT glut;
 
   public Minecraft() {
      this.addGLEventListener(this);
      this.addMouseMotionListener(this);
      
    
     
   }
 

   @Override
   public void init(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();      
      glu = new GLU();                        // GL componentes
      glut = new GLUT();
      gl.glClearColor(0, 0, 0, 0); // Para el color negro
      gl.glClearDepth(1.0f);     
      gl.glEnable(GL_DEPTH_TEST); 
      gl.glDepthFunc(GL_LEQUAL);  
      gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
      
      float[] whiteMaterial={1.0f, 1.0f, 1.0f};
      gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, whiteMaterial,0);
      gl.glShadeModel(GL_SMOOTH); // para suavizar la iluminación
 
      /////////////////////////////ILUMINACION//////////////////////////////////////////////
      float[] ambientLight = { 0.5f, .5f, .5f,0.5f }; 
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0); 
      float[] diffuseLight = { .8f,.8f,.8f,0f };  
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0); 
      float[] specularLight = { 1f,1f,1f,0f }; 
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);       
              
      gl.glEnable(GL2.GL_LIGHTING);      
      gl.glEnable(GL2.GL_LIGHT0);
      
      this.textura1 = this.cargarTextura("imagenes/caja3.png");
      
      // Habilitar el uso de texturas
      gl.glEnable(GL2.GL_TEXTURE_2D);
      gl.glEnable(GL2.GL_BLEND);

      // Especificar la funcion de mezcla "adherencia" de textura
      gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ZERO);       
   }
 
   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
 
      if (height == 0) height = 1;   // prevent divide by zero
      float aspect = (float)width / height;
 
      // Set the view port (display area) to cover the entire window
      gl.glViewport(0, 0, width, height);
 
      // Setup perspective projection, with aspect ratio matches viewport
      gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
      gl.glLoadIdentity();             // reset projection matrix
      glu.gluPerspective(fovy, aspect, 0.1, 50.0); // fovy, aspect, zNear, zFar
 
      // Enable the model-view transform
      gl.glMatrixMode(GL_MODELVIEW);
      gl.glLoadIdentity(); // reset
   }
 
   public void display(GLAutoDrawable drawable) {
       
        float aspect = (float)this.getWidth() / this.getHeight();
        GL2 gl = drawable.getGL().getGL2();  
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 

        float[] lightPos = { 0.0f,5.0f,10.0f,1 };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION,lightPos, 0);

        gl.glLoadIdentity();  
        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glEnable(GL.GL_BLEND);


        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);

        glu.gluLookAt(0.0, 0.0, 5.0, this.posCamX, this.posCamY, this.posCamZ, 0.0, 1.0, 0.0);
        
        if (rotX<0) rotX=360-factInc;
        if (rotY<0) rotY=360-factInc;
        if (rotZ<0) rotZ=360-factInc;

        if (rotX>=360) rotX=0;
        if (rotY>=360) rotY=0;
        if (rotZ>=360) rotZ=0;

        
        gl.glRotatef(rotateX,0,1,0);
        gl.glRotatef(rotateY,1,0,0);

        gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(rotZ, 0.0f, 0.0f, 1.0f);
      
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
       
        float no_mat[] = { 0.0f, 0.0f, 0.0f, 1.0f };
        float mat_ambient[] = { 0.5f, 0.5f, 0.5f, 1.0f };
        float mat_ambient_color[] = { 0.0f, 0.8f, 0.0f, 1.0f };
        float mat_diffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };
        float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float no_shininess[] = { 0.0f };
        float low_shininess[] = { 5.0f };
        float high_shininess[] = { 100.0f };
        float mat_emission[] = { 0.3f, 0.2f, 0.2f, 0.0f };

        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, no_mat, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, no_mat, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, high_shininess, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, mat_emission, 0);

        // Asociar la textura con el canvas
        this.textura1.bind(gl);
        this.textura1.enable(gl); 
                
        //glut.glutSolidTeapot(1);
        
        this.textura1.disable(gl);
        
        //gl.glTranslatef(2.0f, 0.0f, 0.0f);
        this.drawCube(gl);
        
        //this.drawCubeUVWmapped(gl);
        
      
        gl.glFlush();
        
        this.rotY += 0.5f;
      
   }

    void drawCube(GL2 gl){   
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnable(GL_TEXTURE_2D);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glPushMatrix();
        gl.glBegin(GL2.GL_QUADS);

        gl.glBegin(GL2.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Left
        // Back Face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Left
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Left
        // Top Face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);	// Bottom Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        // Bottom Face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);	// Top Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);	// Top Left
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
        // Right face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Left
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
        // Left Face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        gl.glEnd();
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
   @Override
   public void dispose(GLAutoDrawable drawable) {
   glut.glutSolidTeapot(1);
   }
}