package hr.fer.zemris.lab1;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import hr.fer.zemris.lab5.Face3D;
import hr.fer.zemris.lab5.Model3D;
import hr.fer.zemris.lab5.ObjLoader;
import hr.fer.zemris.lab5.Vertex3D;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

public class Main {
	
	public static final int POINTS_PER_SEGMENT = 10;
	public static final float TANGENT_LENGTH = 5.0f;
	public static final IVector START_ORIENTATION = new Vector(0, 0, 1);
	public static final int ANIMATION_DELAY = 16;
	
	public static void main(String[] args) {
		
		if(args.length != 2) {
			System.out.println("Potrebna su 2 argumenta: putanja do .obj datoteke i putanja do datoteke s opisom B-spline krivulje.");
			System.exit(0);
		}
		
		try {
			final Model3D model = ObjLoader.loadObj(Paths.get(args[0]));
			final CubicBSpline curve = Main.loadCurveFromFile(args[1]);
			
			model.calculateVertexNormals();
			SwingUtilities.invokeLater(new Runnable() {
				
				private IVector viewPoint = new Vector(27, 25, 57);
				private IVector eyePoint = new Vector(28, 26, 58);
				private int currentSegment = 0;
				private float currentT = 0;
				private float tStep = 0.01f;
				private Timer timer;
				private float modelScale = 2.0f;
				private boolean follow = true;
				private boolean drawTangents = false;
				private boolean drawCurve = true;
				private boolean drawObject = true;
				
				private GLU glu;
				
				@Override
				public void run() {
					model.normalize();
					GLProfile glprofile = GLProfile.getDefault();
					GLCapabilities glcababilities = new GLCapabilities(glprofile);
					final GLCanvas glcanvas = new GLCanvas(glcababilities);
					
					timer = new Timer(ANIMATION_DELAY, new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								currentT += tStep;
								if(currentT > 1) {
									currentSegment = (currentSegment + 1)%curve.getSegmentCount();	
									currentT = 0;
								}
								glcanvas.display();
							}
						});
					
					glcanvas.addKeyListener(new KeyAdapter() {
						
						@Override
						public void keyPressed(KeyEvent e) {
							int code = e.getKeyCode();
							double translationStep = 0.5f;
							
							IVector v = new Vector(viewPoint.get(0), viewPoint.get(1), viewPoint.get(2)).nSub(eyePoint).normalize();
							IVector left = new Vector(0, 1, 0).nVectorProduct(v);
							switch (code) {
							case KeyEvent.VK_W:
								eyePoint.add(v);
								viewPoint.add(v);
								break;
							case KeyEvent.VK_S:
								eyePoint.sub(v);
								viewPoint.sub(v);
								break;
							case KeyEvent.VK_A:
								eyePoint.add(left);
								viewPoint.add(left);
								break;
							case KeyEvent.VK_D:
								eyePoint.sub(left);
								viewPoint.sub(left);
								break;
							case KeyEvent.VK_Q:
								eyePoint.set(1, eyePoint.get(1) - translationStep);
								viewPoint.set(1, viewPoint.get(1) - translationStep);
								break;
							case KeyEvent.VK_E:
								eyePoint.set(1, eyePoint.get(1) + translationStep);
								viewPoint.set(1, viewPoint.get(1) + translationStep);
								break;
							case KeyEvent.VK_T:
								drawTangents = !drawTangents;
								break;
							case KeyEvent.VK_C:
								drawCurve = !drawCurve;
								break;
							case KeyEvent.VK_O:
								drawObject = !drawObject;
								break;
							case KeyEvent.VK_SPACE:
								if(timer.isRunning()) {
									timer.stop();
								} else {
									timer.start();
								}
								break;
							case KeyEvent.VK_LEFT:
								currentT -= tStep;
								if(currentT < 0) {
									if(currentSegment == 0) {
										currentT = 0;
									} else {
										currentSegment--;
										currentT = 1;
									}
								}
								break;
							case KeyEvent.VK_RIGHT:
								currentT += tStep;
								if(currentT > 1) {
									if(currentSegment < curve.getSegmentCount() - 1) {
										currentSegment++;
										currentT = 0;
									} else {
										currentT = 1;
									}
								}
								break;
							case KeyEvent.VK_F:
								follow = !follow;
								break;
							}
							glcanvas.display();
						}
					});
					
					glcanvas.addGLEventListener(new GLEventListener() {
						
						@Override
						public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
							GL2 gl2 = glautodrawable.getGL().getGL2();
							
							gl2.glMatrixMode(GL2.GL_PROJECTION);
							gl2.glLoadIdentity();
							
							gl2.glFrustum(-0.444, 0.444, -0.25, 0.25, 1, 100);							
							
							gl2.glMatrixMode(GL2.GL_MODELVIEW);
							gl2.glViewport(0, 0, width, height);
						}
						
						@Override
						public void init(GLAutoDrawable arg0) {
							glu = new GLU();
						}
						
						@Override
						public void dispose(GLAutoDrawable arg0) {
						}
						
						@Override
						public void display(GLAutoDrawable glautodrawable) {
							GL2 gl2 = glautodrawable.getGL().getGL2();
							
							gl2.glClearColor(0.05f, 0.05f, 0.05f, 0);
							gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
							
							gl2.glLoadIdentity();
							
							if(follow) {
								viewPoint = curve.point(currentSegment, currentT);
							}
							glu.gluLookAt(eyePoint.get(0), eyePoint.get(1), eyePoint.get(2),
										  viewPoint.get(0), viewPoint.get(1), viewPoint.get(2),
										  0, 1, 0);
							
							setupLighting(gl2);
							
							gl2.glEnable(GL2.GL_DEPTH_TEST);
							gl2.glShadeModel(GL2.GL_SMOOTH);

							gl2.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
							gl2.glEnable(GL.GL_CULL_FACE);
							gl2.glCullFace(GL.GL_BACK);
							
							if(drawObject) {
								drawObject(gl2,
										START_ORIENTATION,
										curve.tangent(currentSegment, currentT),
										curve.point(currentSegment, currentT));
							}
							if(drawCurve) {
								drawCurve(gl2);
							}
							
							if(drawTangents) {
								drawTangents(gl2);
							}
						}
						
						private void drawCurve(GL2 gl2) {
							gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {0.7f, 0.7f, 1f, 1f}, 0);
							gl2.glBegin(GL2.GL_LINE_STRIP);
							for(int seg = 0; seg < curve.getSegmentCount(); seg++) {
								double step = 1.0 / POINTS_PER_SEGMENT;
								for(double t = 0; t <= 1.0; t += step) {
									IVector point = curve.point(seg, t);
									gl2.glVertex3f((float) point.get(0), (float) point.get(1), (float) point.get(2));
								}
							}
							gl2.glEnd();
						}
						
						private void drawTangents(GL2 gl2) {
							gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {1f, 0.05f, 0.05f, 1f}, 0);
							gl2.glBegin(GL2.GL_LINES);
							for(int seg = 0; seg < curve.getSegmentCount(); seg++) {
								double step = 1.0 / POINTS_PER_SEGMENT;
								for(double t = 0; t <= 1.0; t += step) {
									IVector point = curve.point(seg, t);
									IVector tangent = curve.tangent(seg, t).scalarMultiply(TANGENT_LENGTH);
									gl2.glVertex3f((float) point.get(0), (float) point.get(1), (float) point.get(2));
									gl2.glVertex3f((float) point.get(0) + (float) tangent.get(0),
												   (float) point.get(1) + (float) tangent.get(1),
												   (float) point.get(2) + (float) tangent.get(2));
								}
							}
							gl2.glEnd();
						}
						
						private void drawObject(GL2 gl2, IVector startOrientation, IVector endOrientation, IVector point) {
							gl2.glPushMatrix();
							
							gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[] {1f, 1f, 1f, 1f}, 0);
							gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {0.75f, 0.75f, 0.75f, 1f}, 0);
							gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[] {0.01f, 0.01f, 0.01f, 1f}, 0);
							gl2.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 96f);
							
							IVector axis = startOrientation.nVectorProduct(endOrientation);
							float angle = (float) Math.toDegrees(Math.acos(startOrientation.cosine(endOrientation)));
							
							gl2.glTranslatef((float) point.get(0), (float) point.get(1), (float) point.get(2));
							gl2.glRotatef(angle, (float) axis.get(0), (float) axis.get(1), (float) axis.get(2));
							gl2.glScalef(modelScale, modelScale, modelScale);
							for(Face3D triangle : model.getFaces()) {
								gl2.glBegin(GL2.GL_POLYGON);
								for(Vertex3D v : triangle.getVertices()) {
									IVector n = v.getNormal();
									
									gl2.glNormal3d(n.get(0), n.get(1), n.get(2));
									gl2.glVertex3d(v.get(0), v.get(1), v.get(2));
								}
								
								gl2.glEnd();
							}
							
							gl2.glPopMatrix();
						}
						
						private void setupLighting(GL2 gl2) {
							gl2.glEnable(GL2.GL_LIGHTING);
							gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] {0,0,0,0}, 0);
							
							gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {1f, 1f, 1f, 0f}, 0);
							gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[] {0.2f, 0.2f, 0.2f, 1f}, 0);
							gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] {0.8f, 0.8f, 0.8f, 1f}, 0);
							gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] {0f, 0f, 0f, 1f}, 0);
							gl2.glEnable(GL2.GL_LIGHT0);
						}
					});
					
					final JFrame jframe = new JFrame("B-Spline curve animation");
					jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
					jframe.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							jframe.dispose();
							System.exit(0);
						}
					});
					jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
					jframe.setSize(1280, 720);
					jframe.setVisible(true);
					glcanvas.requestFocusInWindow();
				}
			});
		} catch (IOException e) {
			System.out.println("Neuspješno uèitavanje modela.");
			System.exit(0);
		}
	}
	
	public static CubicBSpline loadCurveFromFile(String filePath) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(filePath));
		
		List<Vector> controlPoints = lines.stream()
										  .map(line -> Vector.parseSimple(line))
										  .collect(Collectors.toList());
		
		return new CubicBSpline(controlPoints);
	}
}
